package com.junwoo.common.events.delete;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailedDeletePaymentEvent {
    private String paymentId;
    private String orderId;
}
