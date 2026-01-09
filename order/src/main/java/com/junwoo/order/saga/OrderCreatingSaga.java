package com.junwoo.order.saga;

import com.junwoo.common.command.create.CreateDeliveryCommand;
import com.junwoo.common.command.create.CreatePaymentCommand;
import com.junwoo.common.config.Constants;
import com.junwoo.common.dto.DeliveryStatusEnum;
import com.junwoo.common.dto.OrderStatusEnum;
import com.junwoo.common.dto.ServiceNameEnum;
import com.junwoo.common.events.create.CreatedDeliveryEvent;
import com.junwoo.common.events.create.CreatedPaymentEvent;
import com.junwoo.common.events.create.FailedCreatePaymentEvent;
import com.junwoo.order.command.CompleteOrderCreateCommand;
import com.junwoo.order.event.CancelledCreateOrderEvent;
import com.junwoo.order.event.CompletedCreateOrderEvent;
import com.junwoo.order.event.CreatedOrderEvent;
import com.junwoo.order.event.FailedCreateOrderEvent;
import com.junwoo.order.service.CompensatingService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Saga
@Slf4j
@NoArgsConstructor
public class OrderCreatingSaga {

    private final HashMap<String, String> aggregateIdMap = new HashMap<>();

    private transient CommandGateway commandGateway;

    private CompensatingService compensatingService;

    @Autowired
    public void setCommandGateway(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Autowired
    public void setCompensatingService(CompensatingService compensatingService) {
        this.compensatingService = compensatingService;
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
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

    @SagaEventHandler(associationProperty = "orderId")
    private void on(CreatedPaymentEvent event) {
        log.info("[Saga] <CreatedPaymentEvent> is received for Order Id: {}", event.getOrderId());

        log.info("==== [Create Order] #4: <CreateDeliveryCommand> ====");
        aggregateIdMap.put(ServiceNameEnum.PAYMENT.value(), event.getPaymentId());

        CreateDeliveryCommand createDeliveryCommand = CreateDeliveryCommand.builder()
                .deliveryId("SHIP_" + RandomStringUtils.random(10, false, true))
                .orderId(event.getOrderId())
                .deliveryStatus(DeliveryStatusEnum.CREATED.value())
                .build();

        try {
            commandGateway.sendAndWait(createDeliveryCommand, Constants.GATEWAY_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.info("Error is occurred during handle <CreateDeliveryCommand>: {}", e.getMessage());

            log.info("==== [Create Order] Compensate <CancelCreatePaymentCommand> ====");
            compensatingService.cancelCreateOrder(aggregateIdMap);
            log.info("==== [Create Order] Compensate <CancelCreateOrderCommand> ====");
            compensatingService.cancelCreateOrder(aggregateIdMap);
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void on(CreatedDeliveryEvent event) {
        log.info("[Saga] <CreatedDeliveryEvent> is received for Order Id: {}", event.getOrderId());

        log.info("==== [Create Order] #5 <CompleteOrderCreateCommand> ====");

        aggregateIdMap.put(ServiceNameEnum.DELIVERY.value(), event.getDeliveryId());

        CompleteOrderCreateCommand completeOrderCreateCommand = CompleteOrderCreateCommand.builder()
                .orderId(event.getOrderId())
                .orderStatus(OrderStatusEnum.COMPLETED.value())
                .build();

        try {
            commandGateway.sendAndWait(completeOrderCreateCommand, Constants.GATEWAY_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.info("Error is occurred during handle <CompleteOrderCreateCommand>: {}", e.getMessage());

            log.info("==== [Create Order] Compensate <CancelCreateDeliveryCommand> ====");

            compensatingService.cancelCreateDelivery(aggregateIdMap);

            log.info("==== [Create Order] Compensate <CancelCreateOrderCommand> ====");

            compensatingService.cancelCreateOrder(aggregateIdMap);
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void on(CompletedCreateOrderEvent event) {
        log.info("[Saga] [CompletedCreateOrderEvent] is received for Order Id: {}", event.getOrderId());

        log.info("==== [Create Order] Transaction is FINISHED ====");

        compensatingService.updateReport(event.getOrderId(), true);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void on(FailedCreateOrderEvent event) {
        log.info("[Saga] <FailedCreateOrderEvent> is received for Order Id: {}", event.getOrderId());

        log.info("==== [Create Order] Compensate <CancelCreateOrderCommand> ====");

        compensatingService.cancelCreateOrder(this.aggregateIdMap);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void on(FailedCreatePaymentEvent event) {
        log.info("[Saga] <FailedCreatePaymentEvent> is received for Order Id: {}", event.getOrderId());

        log.info("==== [Create Order] Compensate <CancelCreateOrderCommand> ====");

        compensatingService.cancelCreateOrder(this.aggregateIdMap);

    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void on(CancelledCreateOrderEvent event) {
        log.info("[Saga] CancelledCreateOrderEvent is received for Order Id: {}", event.getOrderId());

        log.info("==== [Create Order] Transaction is Aborted ====");
    }
}
