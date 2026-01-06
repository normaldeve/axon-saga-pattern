package com.junwoo.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReqCreateDTO {
    private String userId;
    private List<OrderReqDetailDTO> orderReqDetails;
    private List<PaymentReqDetailDTO> paymentReqDetails;
}
