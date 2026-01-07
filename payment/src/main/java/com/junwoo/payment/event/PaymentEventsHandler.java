package com.junwoo.payment.event;

import com.junwoo.common.dto.PaymentDetailDTO;
import com.junwoo.common.dto.PaymentStatusEnum;
import com.junwoo.common.events.create.CreatedPaymentEvent;
import com.junwoo.common.events.create.FailedCreatePaymentEvent;
import com.junwoo.payment.entity.Payment;
import com.junwoo.payment.entity.PaymentDetail;
import com.junwoo.payment.entity.PaymentDetailIdentity;
import com.junwoo.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventsHandler {

    private final PaymentRepository paymentRepository;
    private transient final EventGateway eventGateway;

    @EventHandler
    private void on(CreatedPaymentEvent event) {
        log.info("[@EventHandler] Handle <CreatedPaymentEvent> for Payment Id: {}", event.getPaymentId());

        log.info(event.toString());

        List<PaymentDetail> newPaymentDetails = new ArrayList<>();

        try {
            Payment payment = new Payment();
            payment.setPaymentId(event.getPaymentId());
            payment.setOrderId(event.getOrderId());
            payment.setTotalPaymentAmt(event.getTotalPaymentAmt());
            payment.setPaymentStatus(PaymentStatusEnum.COMPLETED.value());

            for (PaymentDetailDTO paymentDetail : event.getPaymentDetails()) {
                PaymentDetailIdentity paymentDetailIdentity = new PaymentDetailIdentity(paymentDetail.getPaymentId(), paymentDetail.getPaymentKind());

                PaymentDetail newPaymentDetail = new PaymentDetail();
                newPaymentDetail.setPaymentDetailIdentity(paymentDetailIdentity);
                newPaymentDetail.setPaymentAmt(paymentDetail.getPaymentAmt());
                newPaymentDetails.add(newPaymentDetail);
            }
            payment.setPaymentDetails(newPaymentDetails);

            paymentRepository.save(payment);
        } catch (Exception e) {
            log.error("Error is occurred during handle <PaymentProcessedEvent>: {}", e.getMessage());
            if (!event.isCompensation()) {
                eventGateway.publish(new FailedCreatePaymentEvent(event.getPaymentId(), event.getOrderId()));
            }
        }
    }
}
