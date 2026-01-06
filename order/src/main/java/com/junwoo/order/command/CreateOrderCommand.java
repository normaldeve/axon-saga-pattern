package com.junwoo.order.command;

import com.junwoo.common.dto.OrderDetailDTO;
import com.junwoo.common.dto.PaymentDetailDTO;
import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Value
@Builder
public class CreateOrderCommand {

    @TargetAggregateIdentifier
    String orderId;
    String userId;
    LocalDateTime orderDateTime;
    String orderStatus;
    int totalOrderAmt;
    List<OrderDetailDTO> orderDetails;
    String paymentId;
    List<PaymentDetailDTO> paymentDetails;
    int totalPaymentAmt;
    boolean isCompensation;
}
