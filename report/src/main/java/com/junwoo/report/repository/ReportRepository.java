package com.junwoo.report.repository;

import com.junwoo.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 7.
 */
public interface ReportRepository extends JpaRepository<ReportRepository, String> {

    Optional<Report> findByOrderId(String orderId);
}
