package com.junwoo.order.event;

import com.junwoo.common.dto.OrderDetailDTO;
import com.junwoo.common.dto.PaymentDetailDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Data
@NoArgsConstructor
public class CreatedOrderEvent {

    private String orderId;
    private String userId;
    private LocalDateTime orderDateTime;
    private String orderStatus;
    private int totalOrderAmt;
    private List<OrderDetailDTO> orderDetails;
    private String paymentId;
    private List<PaymentDetailDTO> paymentDetails;
    private int totalPaymentAmt;

    private boolean isCompensation;
}
