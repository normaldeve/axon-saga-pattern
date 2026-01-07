package com.junwoo.payment.queries;

import com.junwoo.common.config.Constants;
import com.junwoo.common.dto.PaymentDTO;
import com.junwoo.common.dto.PaymentDetailDTO;
import com.junwoo.payment.entity.Payment;
import com.junwoo.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentQueryHandler {

    private final PaymentRepository paymentRepository;

    @QueryHandler(queryName = Constants.QUERY_REPORT)
    private PaymentDTO handle(String orderId) {
        log.info("[@QueryHandler] Handle <{}> for Order Id: {}", Constants.QUERY_REPORT, orderId);

        Optional<Payment> optPayment = paymentRepository.findByOrderId(orderId);
        if (optPayment.isPresent()) {
            Payment payment = optPayment.get();
            PaymentDTO paymentDTO = new PaymentDTO();
            BeanUtils.copyProperties(payment, paymentDTO);
            List<PaymentDetailDTO> newDetails = payment.getPaymentDetails().stream()
                    .map(o -> new PaymentDetailDTO(orderId, o.getPaymentDetailIdentity().getPaymentId(), o.getPaymentDetailIdentity().getPaymentKind(), o.getPaymentAmt()))
                    .toList();
            paymentDTO.setPaymentDetails(newDetails);

            return paymentDTO;
        } else {
            return null;
        }
    }
}
