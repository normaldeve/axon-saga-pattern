package com.junwoo.delivery.aggregate;

import com.junwoo.common.command.create.CreateDeliveryCommand;
import com.junwoo.common.dto.DeliveryDTO;
import com.junwoo.common.events.create.CreatedDeliveryEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

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
public class DeliveryAggregate {

    @AggregateIdentifier
    private String deliveryId;

    @AggregateMember
    private String orderId;

    @AggregateMember
    private String deliveryStatus;

    private final List<DeliveryDTO> aggregateHistory = new ArrayList<>();

    @CommandHandler
    private DeliveryAggregate(CreateDeliveryCommand createDeliveryCommand) {
        log.info("[@CommandHandler] Executing <CreateDeliveryCommand> for Order Id: {} and Delivery ID: {}", createDeliveryCommand.getOrderId(), createDeliveryCommand.getDeliveryId());

        CreatedDeliveryEvent createdDeliveryEvent = new CreatedDeliveryEvent();
        BeanUtils.copyProperties(createDeliveryCommand, createdDeliveryEvent);

        AggregateLifecycle.apply(createdDeliveryEvent);
    }

    @EventSourcingHandler
    private void on(CreatedDeliveryEvent event) {
        log.info("[@EventSourcingHandler] Executing <CreatedDeliveryEvent> for Order Id: {}, and Delivery Id: {}", event.getOrderId(), event.getDeliveryId());

        this.orderId = event.getOrderId();
        this.deliveryId = event.getDeliveryId();
        this.deliveryStatus = event.getDeliveryStatus();
        this.aggregateHistory.add(cloneAggregate(this));
    }

    private DeliveryDTO cloneAggregate(DeliveryAggregate deliveryAggregate) {
        DeliveryDTO delivery = new DeliveryDTO();
        delivery.setDeliveryId(deliveryAggregate.deliveryId);
        delivery.setOrderId(deliveryAggregate.orderId);
        delivery.setDeliveryStatus(deliveryAggregate.deliveryStatus);
        return delivery;
    }
}
