package com.junwoo.order.service;

import com.junwoo.common.config.Constants;
import com.junwoo.common.dto.InventoryDTO;
import com.junwoo.common.dto.OrderDetailDTO;
import com.junwoo.common.dto.PaymentDetailDTO;
import com.junwoo.common.dto.PaymentKindEnum;
import com.junwoo.common.queries.GetInventoryByProductIdQuery;
import com.junwoo.common.vo.ResultVO;
import com.junwoo.order.command.CreateOrderCommand;
import com.junwoo.order.dto.OrderReqCreateDTO;
import com.junwoo.order.dto.OrderReqDetailDTO;
import com.junwoo.order.dto.PaymentReqDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private transient final QueryGateway queryGateway;
    private transient final CommandGateway commandGateway;

    public ResultVO<CreateOrderCommand> createOrder(OrderReqCreateDTO request) {
        log.info("[OrderService] Executing <createOrder>: {}", request.toString());

        log.info("==== [Create Order] START Transaction ====");

        ResultVO<CreateOrderCommand> retVO = new ResultVO<>();

        List<PaymentReqDetailDTO> payDetails = request.getPaymentReqDetails();
        ResultVO<String> retPay = iisValidPaymentInput(payDetails);
        if (!retPay.isReturnCode()) {
            log.info(retPay.getReturnMessage());
            retVO.setReturnCode(retPay.isReturnCode());
            retVO.setReturnMessage(retPay.getReturnMessage());
            return retVO;
        }

        log.info("==== [Create Order] #!: <isValidInventory> ====");
        List<ResultVO<InventoryDTO>> inventories = getInventory(request.getOrderReqDetails());
        String retCheck = isValidInventory(inventories);
        if (!retCheck.isEmpty()) {
            retVO.setReturnCode(false);
            retVO.setReturnMessage(retCheck);
            return retVO;
        }

        String orderId = "ORDER_" + RandomStringUtils.random(9, false, true);
        String paymentId = "PAY_" + RandomStringUtils.random(11, false, true);
        String userId = request.getUserId();

        List<OrderDetailDTO> newOrderDetails = request.getOrderReqDetails().stream()
                .map(o -> new OrderDetailDTO(orderId, o.getProductId(), o.getQty(), o.getQty() * getUnitPrice(inventories, o.getProductId())))
                .toList();

        int totalOrderAmt = newOrderDetails.stream().mapToInt(OrderDetailDTO::getOrderAmt).sum();

        List<PaymentDetailDTO> newPaymentDetails = request.getPaymentReqDetails().stream()
                .map(p -> new PaymentDetailDTO(orderId, paymentId, p.getPaymentKind(), (int) Math.round(p.getPaymentRate() * totalOrderAmt)))
                .toList();

        int totalPaymentAmt = newPaymentDetails.stream().mapToInt(PaymentDetailDTO::getPaymentAmt).sum();

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(orderId)
                .userId(userId)
                .orderDateTime(LocalDateTime.now())
                .totalOrderAmt(totalOrderAmt)
                .orderDetails(newOrderDetails)
                .paymentId(paymentId)
                .paymentDetails(newPaymentDetails)
                .totalPaymentAmt(totalPaymentAmt)
                .build();

        try {
            log.info("==== [Create Order] #2: <CreateOrderCommand> ====");

            commandGateway.sendAndWait(createOrderCommand, Constants.GATEWAY_TIMEOUT, TimeUnit.SECONDS);
            retVO.setReturnCode(true);
            retVO.setReturnMessage("Order Created");
            retVO.setResult(createOrderCommand);
        } catch (Exception e) {
            retVO.setReturnCode(false);
            retVO.setReturnMessage(e.getMessage());
        }

        return retVO;
    }

    private ResultVO<String> iisValidPaymentInput(List<PaymentReqDetailDTO> paymentReqDetails) {
        ResultVO<String> retVO = new ResultVO<>();
        for (PaymentReqDetailDTO item : paymentReqDetails) {
            Optional<PaymentKindEnum> optEnum = Arrays.stream(PaymentKindEnum.values())
                    .filter(o -> o.value().equals(item.getPaymentKind()))
                    .findFirst();

            if (optEnum.isEmpty()) {
                retVO.setReturnCode(false);
                retVO.setReturnMessage("지불 수단 <" + item.getPaymentKind() + ">은 정상적인 지불 수단이 아닙니다");
                return retVO;
            }
        }

        if (paymentReqDetails.stream().mapToDouble(PaymentReqDetailDTO::getPaymentRate).sum() != 1) {
            retVO.setReturnCode(false);
            retVO.setReturnMessage("결제 비율의 합은 1이어야 합니다.");
            return retVO;
        }

        retVO.setReturnCode(true);
        retVO.setReturnMessage("유효한 결제수단 및 결제비율임");
        return retVO;
    }

    private List<ResultVO<InventoryDTO>> getInventory(List<OrderReqDetailDTO> orderDetails) {
        log.info("[OrderService] Executing getInventory");

        GetInventoryByProductIdQuery getInventoryByProductIdQuery;
        List<ResultVO<InventoryDTO>> inventories = new ArrayList<>();
        ResultVO<InventoryDTO> retVO;
        int reqQty;

        InventoryDTO inventoryDTO;

        try {
            for (OrderReqDetailDTO orderDetail : orderDetails) {
                getInventoryByProductIdQuery = new GetInventoryByProductIdQuery(orderDetail.getProductId());
                inventoryDTO = queryGateway.query(getInventoryByProductIdQuery, ResponseTypes.instanceOf(InventoryDTO.class)).join();

                reqQty = orderDetail.getQty();
                retVO = new ResultVO<>();
                retVO.setResult(inventoryDTO);
                retVO.setReturnCode(reqQty <= inventoryDTO.getInventoryQty() && inventoryDTO.getInventoryQty() != 0);
                inventories.add(retVO);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return inventories;
    }

    private String isValidInventory(List<ResultVO<InventoryDTO>> inventories) {
        log.info("[OrderService] Executing <isValidInventory>");

        if (inventories.isEmpty()) return "재고 정보 없음";

        for (ResultVO<InventoryDTO> retVO : inventories) {
            if (!retVO.isReturnCode()
            ) {
                return "재고 없음: " + retVO.getResult().getProductId();
            }
        }

        return "";
    }

    private int getUnitPrice(List<ResultVO<InventoryDTO>> inventories, String productId) {
        log.info("[OrderService] Executing <getUnitPrice>");

        Optional<ResultVO<InventoryDTO>> optInvetory = inventories.stream()
                .filter(obj -> productId.equals(obj.getResult().getProductId()))
                .findFirst();

        return (optInvetory.map(inventoryDTOResultVO ->
                inventoryDTOResultVO.getResult().getUnitPrice()).orElse(0));
    }
}
