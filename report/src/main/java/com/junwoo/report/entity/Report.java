package com.junwoo.report.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.junwoo.common.command.create.CreateReportCommand;
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
import java.time.LocalDateTime;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 7.
 */
@Slf4j
@Aggregate
@Data
@Entity
@Table(name = "report")
@NoArgsConstructor
public class Report implements Serializable {

    @Serial
    private static final long serialVersionUID = -9122517338617820115L;

    @AggregateIdentifier
    @Id
    @Column(name = "report_id", nullable = false, length = 15)
    private String reportId;

    @AggregateMember
    @Column(name = "order_id", nullable = false, length = 15)
    private String orderId;

    @AggregateMember
    @Column(name = "user_id", nullable = false, length = 30)
    private String userId;

    @AggregateMember
    @Column(name = "order_datetime", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime orderDatetime;

    @AggregateMember
    @Column(name = "total_order_amt", nullable = false)
    private int totalOrderAmt;

    @AggregateMember
    @Column(name = "order_status", nullable = false, length = 2)
    private String orderStatus;

    @AggregateMember
    @Column(name = "order_details", length = 15)
    private String orderDetails;

    @AggregateMember
    @Column(name = "payment_id", nullable = false, length = 15)
    private String paymentId;

    @AggregateMember
    @Column(name = "total_payment_amt", nullable = false)
    private int totalPaymentAmt;

    @AggregateMember
    @Column(name = "payment_status", nullable = false, length = 2)
    private String paymentStatus;

    @AggregateMember
    @Column(name = "payment_details", length = 3000)
    private String paymentDetails;

    @AggregateMember
    @Column(name = "delivery_id", nullable = false, length = 15)
    private String deliveryId;

    @AggregateMember
    @Column(name = "delivery_status", nullable = false, length = 2)
    private String deliveryStatus;

    @CommandHandler
    private Report(CreateReportCommand createReportCommand) {
        log.info("[@CommandHandler] Executing CreateReportCommand for Report Id: {}", createReportCommand.getReportId());
        log.info(createReportCommand.toString());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        this.reportId = createReportCommand.getReportId();
        this.orderId = createReportCommand.getOrderId();
        this.userId = createReportCommand.getUserId();
        this.orderDatetime = createReportCommand.getOrderDatetime();
        this.totalOrderAmt = createReportCommand.getTotalOrderAmt();
        this.orderStatus = createReportCommand.getOrderStatus();
        this.orderDetails = gson.toJson(createReportCommand.getOrderDetails());
        this.paymentId = createReportCommand.getPaymentId();
        this.totalPaymentAmt = createReportCommand.getTotalPaymentAmt();
        this.paymentStatus = createReportCommand.getPaymentStatus();
        this.paymentDetails = gson.toJson(createReportCommand.getPaymentDetails());
        this.deliveryId = createReportCommand.getDeliveryId();
        this.deliveryStatus = createReportCommand.getDeliveryStatus();
    }

}
