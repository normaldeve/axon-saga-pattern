package com.junwoo.common.events.update;

import com.junwoo.common.dto.PaymentDetailDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdatedPaymentToReportEvent {
    private String paymentId;
    private String orderId;
    private int totalPaymentAmt;
    private String paymentStatus;
    private List<PaymentDetailDTO> paymentDetails;
}
