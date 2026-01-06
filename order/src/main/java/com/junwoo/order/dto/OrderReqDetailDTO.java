package com.junwoo.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReqDetailDTO {
    private String productId;
    private int qty;
}
