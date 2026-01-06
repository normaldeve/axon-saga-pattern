package com.junwoo.common.events.delete;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletedDeliveryEvent {
    private String deliveryId;
    private String orderId;
}
