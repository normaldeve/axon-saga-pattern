package com.junwoo.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "payment_detail")
public class PaymentDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = -5184055455900848486L;

    @EmbeddedId
    private PaymentDetailIdentity paymentDetailIdentity;

    @Column(name = "payment_amt", nullable = false)
    private int paymentAmt;
}
