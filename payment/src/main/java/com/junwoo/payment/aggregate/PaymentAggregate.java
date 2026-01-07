package com.junwoo.payment.aggregate;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.junwoo.common.command.create.CreatePaymentCommand;
import com.junwoo.common.dto.PaymentDTO;
import com.junwoo.common.dto.PaymentDetailDTO;
import com.junwoo.common.events.create.CreatedPaymentEvent;
import com.junwoo.payment.entity.PaymentDetail;
import com.junwoo.payment.entity.PaymentDetailIdentity;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@Aggregate
@NoArgsConstructor
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;

    @AggregateMember
    private String orderId;

    @AggregateMember
    private int totalPaymentAmt;

    @AggregateMember
    private String paymentStatus;

    @AggregateMember
    private List<PaymentDetail> paymentDetails;

    private final List<PaymentDTO> aggregateHistory = new ArrayList<>();

    @CommandHandler
    private PaymentAggregate(CreatePaymentCommand createPaymentCommand) {
        log.info("[@CommandHandler] Executing CreatePaymentCommand..");
        log.info(createPaymentCommand.toString());

        CreatedPaymentEvent createdPaymentEvent = new CreatedPaymentEvent();
        BeanUtils.copyProperties(createPaymentCommand, createdPaymentEvent);

        try {
            AggregateLifecycle.apply(createdPaymentEvent);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @EventSourcingHandler
    private void on(CreatedPaymentEvent event) {
        log.info("[@EventSourcingHandler] Executing CreatedPaymentEvent..");

        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
        this.totalPaymentAmt = event.getTotalPaymentAmt();
        this.paymentDetails = event.getPaymentDetails().stream()
                .map(o -> new PaymentDetail(new PaymentDetailIdentity(event.getPaymentId(), o.getPaymentKind()), o.getPaymentAmt()))
                .toList();
        this.aggregateHistory.add(cloneAggregate(this));

    }

    private PaymentDTO cloneAggregate(PaymentAggregate paymentAggregate) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderId(paymentAggregate.orderId);
        paymentDTO.setPaymentId(paymentAggregate.paymentId);
        paymentDTO.setTotalPaymentAmt(paymentAggregate.totalPaymentAmt);
        paymentDTO.setPaymentStatus(paymentAggregate.paymentStatus);
        paymentDTO.setPaymentDetails(paymentAggregate.paymentDetails.stream()
                .map(o -> new PaymentDetailDTO(paymentAggregate.orderId, paymentAggregate.paymentId, o.getPaymentDetailIdentity().getPaymentKind(), o.getPaymentAmt()))
                .toList());

        return paymentDTO;
    }
}
