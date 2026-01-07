package com.junwoo.delivery.queries;

import com.junwoo.common.config.Constants;
import com.junwoo.common.dto.DeliveryDTO;
import com.junwoo.delivery.entity.Delivery;
import com.junwoo.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 7.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryQueryHandler {

    private final DeliveryRepository deliveryRepository;

    @QueryHandler(queryName = Constants.QUERY_REPORT)
    private DeliveryDTO handle(String orderId) {
        log.info("[@QueryHandler] Handle <{}> for Order Id: {}", Constants.QUERY_REPORT, orderId);
        Optional<Delivery> optDelivery = deliveryRepository.findByOrderId(orderId);
        if (optDelivery.isPresent()) {
            Delivery delivery = optDelivery.get();
            DeliveryDTO deliveryDTO = new DeliveryDTO();
            BeanUtils.copyProperties(delivery, deliveryDTO);
            return deliveryDTO;
        } else {
            log.info("Can't find delivery info for Order Id: {}", orderId);
            return null;
        }
    }
}
