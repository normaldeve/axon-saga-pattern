package com.junwoo.common.events.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FailedCreateDeliveryEvent {
    private String deliveryId;
    private String orderId;
}
