package com.junwoo.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.EntityId;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = -4210248413827382355L;

    @EmbeddedId
    private OrderDetailIdentity orderDetailIdentity;

    @Column(name = "qty", nullable = false)
    private int qty;

    @Column(name = "order_amt", nullable = false)
    private int orderAmt;
}
