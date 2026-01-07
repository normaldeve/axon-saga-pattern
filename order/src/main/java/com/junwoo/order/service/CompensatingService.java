package com.junwoo.order.service;

import com.junwoo.common.command.create.CreateDeliveryCommand;
import com.junwoo.common.command.create.CreateReportCommand;
import com.junwoo.common.config.Constants;
import com.junwoo.common.dto.DeliveryDTO;
import com.junwoo.common.dto.OrderDTO;
import com.junwoo.common.dto.PaymentDTO;
import com.junwoo.common.dto.ServiceNameEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompensatingService {

    private transient final QueryGateway queryGateway;
    private transient final CommandGateway commandGateway;

    public void cancelCreateOrder(HashMap<String, String> aggregateIdMap) {
        log.info("[CompensatingService] Executing <cancelCreateOrder> for Order Id: {}", aggregateIdMap.get(ServiceNameEnum.ORDER.value()));
    }

    public void cancelCreatePayment(HashMap<String, String> aggregateMap) {
        log.info("[CompensatingService] Executing <cancelCreatePayment> for Order Id: {}", aggregateMap.get(ServiceNameEnum.ORDER.value()));

    }

    public void cancelCreateDelivery(HashMap<String, String> aggregateMap) {
        log.info("[CompensatingService] Executing <cancelCreateDelivery> for Order Id: {}", aggregateMap.get(ServiceNameEnum.ORDER.value()));
    }

    public void updateReport(String orderId, boolean isCreate) {
        log.info("==== START {} Report ====", isCreate ? "Create" : "Update");

        try {
            OrderDTO order = queryGateway.query(Constants.QUERY_REPORT, orderId, ResponseTypes.instanceOf(OrderDTO.class)).join();

            PaymentDTO payment = queryGateway.query(Constants.QUERY_REPORT, orderId, ResponseTypes.instanceOf(PaymentDTO.class)).join();

            DeliveryDTO delivery = queryGateway.query(Constants.QUERY_REPORT, orderId, ResponseTypes.instanceOf(DeliveryDTO.class)).join();

            if (isCreate) {
                CreateReportCommand cmd = CreateReportCommand.builder()
                        .reportId(RandomStringUtils.random(15, false, true))
                        .orderId(order.getOrderId())
                        .userId(order.getUserId())
                        .orderDatetime(order.getOrderDatetime())
                        .totalOrderAmt(order.getTotalOrderAmt())
                        .orderDetails(order.getOrderDetails())
                        .paymentId(payment.getPaymentId())
                        .totalPaymentAmt(payment.getTotalPaymentAmt())
                        .paymentStatus(payment.getPaymentStatus())
                        .paymentDetails(payment.getPaymentDetails())
                        .deliveryId(delivery.getDeliveryId())
                        .deliveryStatus(delivery.getDeliveryStatus())
                        .build();

                commandGateway.send(cmd);
            } else {

            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        log.info("==== END {} report", isCreate ? "Create" : "Update");

    }
}
