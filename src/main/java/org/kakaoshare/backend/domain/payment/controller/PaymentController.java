package org.kakaoshare.backend.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.payment.dto.cancel.request.PaymentCancelRequest;
import org.kakaoshare.backend.domain.payment.dto.cancel.request.PaymentFundingCancelRequest;
import org.kakaoshare.backend.domain.payment.dto.cancel.request.PaymentFundingDetailCancelRequest;
import org.kakaoshare.backend.domain.payment.dto.preview.PaymentPreviewRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentFundingReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentGiftReadyRequest;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payments/preview")
    public ResponseEntity<?> preview(@RequestBody final List<PaymentPreviewRequest> paymentPreviewRequests) {
        return ResponseEntity.ok(paymentService.preview(paymentPreviewRequests));
    }

    @PostMapping("/payments/ready")
    public ResponseEntity<?> ready(@LoggedInMember final String providerId,
                                   @RequestBody final PaymentGiftReadyRequest paymentGiftReadyRequest) {
        return ResponseEntity.ok(paymentService.ready(providerId, paymentGiftReadyRequest));
    }

    @PostMapping("/funding/payments/ready")
    public ResponseEntity<?> ready(@LoggedInMember final String providerId,
                                   @RequestBody final PaymentFundingReadyRequest paymentFundingReadyRequest) {
        return ResponseEntity.ok(paymentService.readyFunding(providerId, paymentFundingReadyRequest));
    }

    @PostMapping("/payments/success")
    public ResponseEntity<?> success(@LoggedInMember final String providerId,
                                     @RequestBody final PaymentSuccessRequest paymentSuccessRequest) {
        return ResponseEntity.ok(paymentService.approve(providerId, paymentSuccessRequest));
    }

    @PostMapping("/funding/payments/success")
    public ResponseEntity<?> successFunding(@LoggedInMember final String providerId,
                                            @RequestBody final PaymentSuccessRequest paymentSuccessRequest) {
        return ResponseEntity.ok(paymentService.approveFunding(providerId, paymentSuccessRequest));
    }

    @PostMapping("/payments/cancel")
    public ResponseEntity<?> cancel(@LoggedInMember final String providerId,
                                    @RequestBody final PaymentCancelRequest paymentCancelRequest) {
        paymentService.cancel(providerId, paymentCancelRequest);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/funding/payments/cancel")
    public ResponseEntity<?> cancelFunding(@LoggedInMember final String providerId,
                                           @RequestBody final PaymentFundingCancelRequest paymentFundingCancelRequest) {
        paymentService.cancelFunding(providerId, paymentFundingCancelRequest);
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/funding/detail/payments/cancel")
    public ResponseEntity<?> cancelFundingDetail(@LoggedInMember final String providerId,
                                                 @RequestBody final PaymentFundingDetailCancelRequest paymentFundingDetailCancelRequest) {
        paymentService.cancelFundingDetail(providerId, paymentFundingDetailCancelRequest);
        return ResponseEntity.ok()
                .build();
    }
}
