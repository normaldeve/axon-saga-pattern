package com.junwoo.order.aggregate;

import com.junwoo.common.dto.OrderDTO;
import com.junwoo.order.command.CompleteOrderCreateCommand;
import com.junwoo.order.command.CreateOrderCommand;
import com.junwoo.order.entity.OrderDetail;
import com.junwoo.order.entity.OrderDetailIdentity;
import com.junwoo.order.event.CompletedCreateOrderEvent;
import com.junwoo.order.event.CreatedOrderEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@Aggregate
@NoArgsConstructor
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;

    @AggregateMember
    private String userId;

    @AggregateMember
    private LocalDateTime orderDateTime;

    @AggregateMember
    private String orderStatus;

    @AggregateMember
    private int totalOrderAmt;

    @AggregateMember
    private List<OrderDetail> orderDetails;

    private final List<OrderDTO> aggregateHistory = new ArrayList<>();

    @CommandHandler
    private OrderAggregate(CreateOrderCommand createOrderCommand) {
        log.info("[@CommandHandler] Executing <CreateOrderCommand> for Order Id: {}", createOrderCommand.getOrderId());

        CreatedOrderEvent createOrderEvent = new CreatedOrderEvent();
        createOrderEvent.setOrderId(createOrderCommand.getOrderId());
        createOrderEvent.setUserId(createOrderCommand.getUserId());
        createOrderEvent.setOrderDateTime(createOrderCommand.getOrderDateTime());
        createOrderEvent.setOrderStatus(createOrderCommand.getOrderStatus());
        createOrderEvent.setTotalOrderAmt(createOrderCommand.getTotalOrderAmt());
        createOrderEvent.setOrderDetails(createOrderCommand.getOrderDetails());
        createOrderEvent.setPaymentId(createOrderCommand.getPaymentId());

        createOrderEvent.setPaymentDetails(createOrderCommand.getPaymentDetails());
        createOrderEvent.setTotalOrderAmt(createOrderCommand.getTotalOrderAmt());

        createOrderEvent.setTotalPaymentAmt(createOrderCommand.getTotalPaymentAmt());

        AggregateLifecycle.apply(createOrderEvent);
    }

    @CommandHandler
    private void handle(CompleteOrderCreateCommand completeOrderCreateCommand) throws RuntimeException {

        log.info("[@CommandHandler] Executing <CompleteOrderCreateCommand> for Order Id: {}", completeOrderCreateCommand.getOrderId());

        CompletedCreateOrderEvent completedCreateOrderEvent = new CompletedCreateOrderEvent();
        BeanUtils.copyProperties(completeOrderCreateCommand, completedCreateOrderEvent);

        AggregateLifecycle.apply(completedCreateOrderEvent);
    }

    @EventSourcingHandler
    private void on(CreatedOrderEvent event) {
        log.info("[@EventSourcingHandler] Executing <CreatedOrderEvent> for Order Id: {}", event.getOrderId());

        this.orderId = event.getOrderId();
        this.userId = event.getUserId();
        this.orderDateTime = event.getOrderDateTime();
        this.orderStatus = event.getOrderStatus();
        this.orderDetails = event.getOrderDetails().stream()
                .map(o -> new OrderDetail((new OrderDetailIdentity(this.orderId, o.getProductId())), o.getQty(), o.getOrderAmt()))
                .toList();
        this.totalOrderAmt = event.getTotalOrderAmt();
    }

    @EventSourcingHandler
    private void on(CompletedCreateOrderEvent event) {
        log.info("[@EventSourcingHandler] Executing <CompletedCreateOrderEvent> for Order Id: {}", event.getOrderId());

        this.orderStatus = event.getOrderStatus();
    }
}
