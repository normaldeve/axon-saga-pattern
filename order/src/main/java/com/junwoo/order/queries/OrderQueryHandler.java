package com.junwoo.order.queries;

import com.junwoo.common.config.Constants;
import com.junwoo.common.dto.OrderDTO;
import com.junwoo.common.dto.OrderDetailDTO;
import com.junwoo.order.entity.Order;
import com.junwoo.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderQueryHandler {

    private final OrderRepository orderRepository;

    @QueryHandler(queryName = Constants.QUERY_REPORT)
    private OrderDTO handleReportQuery(String orderId) {
        log.info("[@QueryHandler] Handle <{}> for Order Id: {}", Constants.QUERY_REPORT, orderId);

        Optional<Order> optOrder = orderRepository.findById(orderId);
        if (optOrder.isPresent()) {
            Order order = optOrder.get();
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(order, orderDTO);
            List<OrderDetailDTO> newOrderDetails = order.getOrderDetails().stream()
                    .map(o -> new OrderDetailDTO(orderId, o.getOrderDetailIdentity().getProductId(), o.getQty(), o.getOrderAmt()))
                    .toList();

            orderDTO.setOrderDetails(newOrderDetails);

            return orderDTO;
        } else {
            return null;
        }
    }
}
