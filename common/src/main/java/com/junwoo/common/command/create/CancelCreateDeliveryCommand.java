package com.junwoo.common.command.create;

import com.junwoo.common.dto.DeliveryStatusEnum;
import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class CancelCreateDeliveryCommand {
    @TargetAggregateIdentifier
    String deliveryId;
    String orderId;
    String deliveryStatus = DeliveryStatusEnum.CANCELED.value();
}
