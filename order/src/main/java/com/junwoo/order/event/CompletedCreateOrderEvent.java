package com.junwoo.order.event;

import lombok.Data;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Data
public class CompletedCreateOrderEvent {
    private String orderId;
    private String orderStatus;
}
