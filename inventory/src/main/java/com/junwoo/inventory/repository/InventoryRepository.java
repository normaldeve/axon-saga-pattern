package com.junwoo.inventory.repository;

import com.junwoo.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 7.
 */
public interface InventoryRepository extends JpaRepository<Inventory, String> {

}
