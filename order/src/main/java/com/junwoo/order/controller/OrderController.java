package com.junwoo.order.controller;

import com.junwoo.common.vo.ResultVO;
import com.junwoo.order.command.CreateOrderCommand;
import com.junwoo.order.dto.OrderReqCreateDTO;
import com.junwoo.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author junnukim1007gmail.com
 * @date 26. 1. 6.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    @Operation(summary = "신규 상품 주문 API")
    private ResultVO<CreateOrderCommand> createOrder(
            @RequestBody OrderReqCreateDTO request
    ) {
        ResultVO<CreateOrderCommand> retVO = orderService.createOrder(request);

        log.info("[@PostMapping] Executing createOrder is Finished");

        return retVO;
    }
}
