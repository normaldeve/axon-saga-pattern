package com.junwoo.order.repository;

import com.junwoo.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
public interface OrderRepository extends JpaRepository<Order, String> {

}
