package com.junwoo.order.saga;

import com.junwoo.common.command.create.CreatePaymentCommand;
import com.junwoo.common.config.Constants;
import com.junwoo.common.dto.ServiceNameEnum;
import com.junwoo.order.event.CreatedOrderEvent;
import com.junwoo.order.service.CompensatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.spring.stereotype.Saga;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Saga
@Slf4j
@RequiredArgsConstructor
public class OrderCreatingSaga {

    private final HashMap<String, String> aggregateIdMap = new HashMap<>();

    private transient final CommandGateway commandGateway;

    private final CompensatingService compensatingService;

    private void on(CreatedOrderEvent event) {
        log.info("[Saga] <CreatedOrderEvent> is received for Order Id: {}", event.getOrderId());

        log.info("==== [Creat Order] #3: <CreatePaymentCommand> ====");

        aggregateIdMap.put(ServiceNameEnum.ORDER.value(), event.getOrderId());

        CreatePaymentCommand createPaymentCommand = CreatePaymentCommand.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .totalPaymentAmt(event.getTotalPaymentAmt())
                .paymentDetails(event.getPaymentDetails())
                .build();

        try {
            commandGateway.sendAndWait(createPaymentCommand, Constants.GATEWAY_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error is occurred during handle <CreatePaymentCommand>: {}", e.getMessage());
            log.info("==== [CreateOrder] Compensate <CancelCreateOrderCommand>");
            compensatingService.cancelCreateOrder(aggregateIdMap);
        }
    }


}
