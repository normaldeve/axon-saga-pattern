package com.junwoo.delivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Data
@Entity
@Table(name = "shipping")
public class Delivery implements Serializable {

    @Serial
    private static final long serialVersionUID = 2440030471661785389L;

    @Id
    @Column(name = "delivery_id", nullable = false, length = 15)
    private String deliveryId;

    @Column(name = "order_id", nullable = false, length = 15)
    private String orderId;

    @Column(name = "delivery_status", nullable = false, length = 2)
    private String deliveryStatus;
}
