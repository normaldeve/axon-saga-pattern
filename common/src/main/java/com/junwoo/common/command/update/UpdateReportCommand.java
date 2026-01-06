package com.junwoo.common.command.update;

import com.junwoo.common.dto.OrderDetailDTO;
import com.junwoo.common.dto.PaymentDetailDTO;
import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class UpdateReportCommand {
    @TargetAggregateIdentifier
    String reportId;
    String orderId;
    String userId;
    LocalDateTime orderDatetime;
    int totalOrderAmt;
    String orderStatus;
    List<OrderDetailDTO> orderDetails;
    String paymentId;
    int totalPaymentAmt;
    String paymentStatus;
    List<PaymentDetailDTO> paymentDetails;
    String deliveryId;
    String deliveryStatus;
}
