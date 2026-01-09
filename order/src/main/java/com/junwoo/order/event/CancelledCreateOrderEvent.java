package com.junwoo.order.event;

import lombok.Data;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 9.
 */
@Data
public class CancelledCreateOrderEvent {
    private String orderId;
    private String orderStatus;
}
