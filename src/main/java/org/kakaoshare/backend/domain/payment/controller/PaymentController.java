package org.kakaoshare.backend.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.payment.dto.preview.PaymentPreviewRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.success.request.PaymentSuccessRequest;
import org.kakaoshare.backend.domain.payment.service.PaymentService;
import org.kakaoshare.backend.jwt.util.LoggedInMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/preview")
    public ResponseEntity<?> preview(@RequestBody final List<PaymentPreviewRequest> paymentPreviewRequests) {
        return ResponseEntity.ok(paymentService.preview(paymentPreviewRequests));
    }

    @PostMapping("/ready")
    public ResponseEntity<?> ready(@LoggedInMember final String providerId,
                                   @RequestBody final List<PaymentReadyRequest> requests) {
        return ResponseEntity.ok(paymentService.ready(providerId, requests));
    }

    @PostMapping("/success")
    public ResponseEntity<?> success(@LoggedInMember final String providerId,
                                     @RequestBody final PaymentSuccessRequest requests) {
        return ResponseEntity.ok(paymentService.approve(providerId, requests));
    }

    // TODO: 3/15/24 결제 취소 및 실패 관련 API는 추후 추가 예정
    @PostMapping("/cancel")
    public void cancel() {

    }

    @PostMapping("/fail")
    public void fail() {

    }
}
