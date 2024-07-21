package org.kakaoshare.backend.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.order.dto.inquiry.request.OrderHistoryRequest;
import org.kakaoshare.backend.domain.order.dto.preview.OrderPreviewRequest;
import org.kakaoshare.backend.domain.order.service.OrderService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private static final int DEFAULT_PAGE_SIZE = 2;
    private static final int DEFAULT_ORDER_PAGE_SIZE = 10;

    private final OrderService orderService;

    @PostMapping("/preview")
    public ResponseEntity<?> preview(@RequestBody final List<OrderPreviewRequest> orders,
                                     @PageableDefault(size = DEFAULT_PAGE_SIZE) final Pageable pageable) {
        return ResponseEntity.ok(orderService.preview(orders, pageable));
    }

    @GetMapping
    public ResponseEntity<?> lookUp(@LoggedInMember final String providerId,
                                    @ModelAttribute final OrderHistoryRequest orderHistoryRequest,
                                    @PageableDefault(size = DEFAULT_ORDER_PAGE_SIZE) final Pageable pageable) {
        return ResponseEntity.ok(orderService.lookUp(providerId, orderHistoryRequest, pageable));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> lookUpDetail(@PathVariable("orderId") final Long orderId) {
        return ResponseEntity.ok(orderService.lookUpDetail(orderId));
    }
}
