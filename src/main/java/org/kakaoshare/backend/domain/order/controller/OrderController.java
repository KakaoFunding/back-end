package org.kakaoshare.backend.domain.order.controller;

import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.order.dto.preview.OrderPreviewRequest;
import org.kakaoshare.backend.domain.order.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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
    public ResponseEntity<?> lookUp(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent final LocalDate endDate,
                                    @PageableDefault(size = DEFAULT_ORDER_PAGE_SIZE) final Pageable pageable) {
        return ResponseEntity.ok(orderService.lookUp(startDate, endDate, pageable));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> lookUpDetail(@PathVariable("orderId") final Long orderId) {
        return ResponseEntity.ok(orderService.lookUpDetail(orderId));
    }
}
