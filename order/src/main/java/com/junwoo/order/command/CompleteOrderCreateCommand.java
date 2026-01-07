package com.junwoo.order.command;

import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.HashMap;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Value
@Builder
public class CompleteOrderCreateCommand {
    @TargetAggregateIdentifier
    String orderId;
    String orderStatus;
    HashMap<String, String> aggregateIdMap;
}
