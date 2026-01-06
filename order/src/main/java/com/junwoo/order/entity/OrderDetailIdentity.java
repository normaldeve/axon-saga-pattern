package com.junwoo.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Embedded
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailIdentity implements Serializable {

    @Serial
    private static final long serialVersionUID = 954814450841698679L;

    @Column(name = "order_id", nullable = false, length = 15)
    private String orderId;

    @Column(name = "product_id", nullable = false, length = 15)
    private String productId;

}
