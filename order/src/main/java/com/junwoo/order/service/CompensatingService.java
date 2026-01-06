package com.junwoo.order.service;

import com.junwoo.common.dto.ServiceNameEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@Service
public class CompensatingService {

    public void cancelCreateOrder(HashMap<String, String> aggregateIdMap) {
        log.info("[CompensatingService] Executing <cancelCreateOrder> for Order Id: {}", aggregateIdMap.get(ServiceNameEnum.ORDER.value()));
    }
}
