package com.junwoo.order.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Getter
@AllArgsConstructor
public class FailedCompleteCreateOrderEvent {
    private String orderId;
}
