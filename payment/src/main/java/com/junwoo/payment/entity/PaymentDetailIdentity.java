package com.junwoo.payment.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailIdentity implements Serializable {

    @Serial
    private static final long serialVersionUID = 4042014740046499497L;

    @Column(name = "payment_id", nullable = false, length = 15)
    private String paymentId;

    @Column(name = "payment_kind", nullable = false, length = 2)
    private String paymentKind;
}
