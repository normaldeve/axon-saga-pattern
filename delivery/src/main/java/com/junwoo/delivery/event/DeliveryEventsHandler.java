package com.junwoo.delivery.event;

import com.junwoo.common.events.create.CreatedDeliveryEvent;
import com.junwoo.common.events.create.FailedCreateDeliveryEvent;
import com.junwoo.delivery.entity.Delivery;
import com.junwoo.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryEventsHandler {

    private final DeliveryRepository deliveryRepository;
    private transient final CommandGateway commandGateway;
    private transient final EventGateway eventGateway;
    private transient final QueryGateway queryGateway;

    @EventHandler
    private void on(CreatedDeliveryEvent event) {
        log.info("[@EventHandler] Handle CreatedDeliveryEvent");

        try {
            Delivery delivery = new Delivery();
            BeanUtils.copyProperties(event, delivery);
            deliveryRepository.save(delivery);
        } catch (Exception e) {
            log.error("Error is occurred during handle CreateDeliveryEvent: {}", e.getMessage());

            if (!event.isCompensation()) {
                eventGateway.publish(new FailedCreateDeliveryEvent(event.getDeliveryId(), event.getOrderId()));
            }
        }
    }
}
