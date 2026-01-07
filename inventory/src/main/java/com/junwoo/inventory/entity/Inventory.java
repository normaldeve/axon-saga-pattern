package com.junwoo.inventory.entity;

import com.junwoo.common.command.create.CreateInventoryCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 7.
 */
@Slf4j
@Aggregate
@Data
@Entity
@Table(name = "inventory")
@NoArgsConstructor
public class Inventory implements Serializable {
    @Serial
    private static final long serialVersionUID = 2169444340219001818L;

    @Id
    @AggregateIdentifier
    @Column(name = "product_id", nullable = false, length = 10)
    private String productId;

    @AggregateMember
    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @AggregateMember
    @Column(name = "unit_price", nullable = false)
    private int unitPrice;

    @AggregateMember
    @Column(name = "inventory_qty", nullable = false)
    private int inventoryQty;

    @CommandHandler
    private Inventory(CreateInventoryCommand createInventoryCommand) {
        log.info("[@CommandHandler] Executing <CreateInventoryCommand> for Product Id:{}", createInventoryCommand.getProductId());

        //--State Stored Aggregator 는 자신의 상태 업데이트를 CommandHandler 에서 수행
        this.productId = createInventoryCommand.getProductId();
        this.productName = createInventoryCommand.getProductName();
        this.unitPrice = createInventoryCommand.getUnitPrice();
        this.inventoryQty = createInventoryCommand.getInventoryQty();

    }
}
