package com.junwoo.order.event;

import com.junwoo.common.dto.OrderDetailDTO;
import com.junwoo.order.entity.Order;
import com.junwoo.order.entity.OrderDetail;
import com.junwoo.order.entity.OrderDetailIdentity;
import com.junwoo.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventsHandler {

    private final OrderRepository orderRepository;
    private transient final EventGateway eventGateway;

    @EventHandler
    private void on(CreatedOrderEvent event) {
        log.info("[@EventHandler] Handle <CreatedOrderEvent> for Order Id: {}", event.getOrderId());

        List<OrderDetail> newOrderDetails = new ArrayList<>();

        try {
            Order order = new Order();
            order.setOrderId(event.getOrderId());
            order.setUserId(event.getUserId());
            order.setOrderDateTime(event.getOrderDateTime());
            order.setOrderStatus(event.getOrderStatus());
            order.setTotalOrderAmt(event.getTotalOrderAmt());

            for (OrderDetailDTO orderDetail : event.getOrderDetails()) {
                OrderDetailIdentity newOrderDetailIdentity = new OrderDetailIdentity(orderDetail.getOrderId(), orderDetail.getProductId());
                OrderDetail newOrderDetail = new OrderDetail();
                newOrderDetail.setOrderDetailIdentity(newOrderDetailIdentity);
                newOrderDetail.setQty(orderDetail.getQty());
                newOrderDetail.setOrderAmt(orderDetail.getOrderAmt());
                newOrderDetails.add(newOrderDetail);
            }

            order.setOrderDetails(newOrderDetails);
            orderRepository.save(order);
        } catch (Exception e) {
            log.error(e.getMessage());
            eventGateway.publish(new FailedCreateOrderEvent(event.getOrderId()));
        }
    }
}
