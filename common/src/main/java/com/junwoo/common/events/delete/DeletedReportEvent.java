package com.junwoo.common.events.delete;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletedReportEvent {
    private String reportId;
    private String orderId;
}
