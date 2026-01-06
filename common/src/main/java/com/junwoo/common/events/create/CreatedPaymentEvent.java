package com.junwoo.common.events.create;

import com.junwoo.common.dto.PaymentDetailDTO;
import lombok.Data;

import java.util.List;

@Data
public class CreatedPaymentEvent {
    private String paymentId;
    private String orderId;
    private int totalPaymentAmt;
    private List<PaymentDetailDTO> paymentDetails;
    private boolean isCompensation;
}
