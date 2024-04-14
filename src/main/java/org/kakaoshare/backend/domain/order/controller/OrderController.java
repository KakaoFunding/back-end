package org.kakaoshare.backend.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.order.dto.preview.OrderPreviewRequest;
import org.kakaoshare.backend.domain.order.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private static final int DEFAULT_PRODUCT_SIZE = 2;

    private final OrderService orderService;

    @PostMapping("/preview")
    public ResponseEntity<?> preview(@RequestBody final List<OrderPreviewRequest> orders,
                                     @PageableDefault(size = DEFAULT_PRODUCT_SIZE) final Pageable pageable) {
        return ResponseEntity.ok(orderService.preview(orders, pageable));
    }
}
