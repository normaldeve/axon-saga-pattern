package com.junwoo.inventory.queries;

import com.junwoo.common.dto.InventoryDTO;
import com.junwoo.common.queries.GetInventoryByProductIdQuery;
import com.junwoo.inventory.entity.Inventory;
import com.junwoo.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 7.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryQueryHandler {

    private final InventoryRepository inventoryRepository;

    @QueryHandler
    private InventoryDTO handle(GetInventoryByProductIdQuery query) {
        log.info("[@QueryHandler] Handle <GetInventoryByProductIdQuery> for Product Id: {}", query.getProductId());

        Optional<Inventory> optInventory = inventoryRepository.findById(query.getProductId());
        if(optInventory.isPresent()) {
            Inventory inventory = optInventory.get();
            return new InventoryDTO(
                    inventory.getProductId(), inventory.getProductName(),
                    inventory.getUnitPrice(), inventory.getInventoryQty());
        } else {
            return null;
        }
    }
}
