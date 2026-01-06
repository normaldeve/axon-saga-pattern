package com.junwoo.common.command.create;

import com.junwoo.common.dto.PaymentStatusEnum;
import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class CancelCreatePaymentCommand {
    @TargetAggregateIdentifier
    String paymentId;
    String orderId;
    String paymentStatus = PaymentStatusEnum.CANCELED.value();
}
