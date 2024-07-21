package org.kakaoshare.backend.domain.payment.service;

import org.kakaoshare.backend.domain.payment.dto.approve.request.KakaoPayApproveRequest;
import org.kakaoshare.backend.domain.payment.dto.cancel.request.PaymentCancelDto;
import org.kakaoshare.backend.domain.payment.dto.kakaopay.cancel.request.KakaoPayCancelRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.request.KakaoPayReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentReadyProductDto;
import org.kakaoshare.backend.domain.payment.dto.success.request.PaymentSuccessRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KakaoPayRequestProvider {
    private static final String PRODUCT_NAME_SEPARATOR = " 외 ";
    private static final String PRODUCT_NAME_SUFFIX = "건";
    private final String cid;
    private final String cidSecret;
    private final String approvalUrl;
    private final String cancelUrl;
    private final String failUrl;

    public KakaoPayRequestProvider(@Value("${pay.client.id}") final String cid,
                                   @Value("${pay.client.secret}") final String cidSecret,
                                   @Value("${pay.redirect-url.approval}") final String approvalUrl,
                                   @Value("${pay.redirect-url.cancel}") final String cancelUrl,
                                   @Value("${pay.redirect-url.fail}") final String failUrl) {
        this.cid = cid;
        this.cidSecret = cidSecret;
        this.approvalUrl = approvalUrl;
        this.cancelUrl = cancelUrl;
        this.failUrl = failUrl;
    }

    public KakaoPayReadyRequest createReadyRequest(final String providerId,
                                                   final List<PaymentReadyProductDto> paymentReadyProductDtos,
                                                   final String orderNumber) {
        final int totalAmount = getTotalAmount(paymentReadyProductDtos);
        final int quantity = getQuantity(paymentReadyProductDtos);
        final String productName = getProductName(paymentReadyProductDtos);
        return new KakaoPayReadyRequest(cid, cidSecret, orderNumber, providerId, productName, quantity, totalAmount, 0, approvalUrl, cancelUrl, failUrl);
    }

    public KakaoPayApproveRequest createApproveRequest(final String providerId,
                                                       final PaymentSuccessRequest paymentSuccessRequest) {
        final String tid = paymentSuccessRequest.tid();
        final String orderNumber = paymentSuccessRequest.orderNumber();
        final String pgToken = paymentSuccessRequest.pgToken();
        return new KakaoPayApproveRequest(cid, cidSecret, tid, orderNumber, providerId, pgToken);
    }

    public KakaoPayCancelRequest createCancelRequest(final PaymentCancelDto paymentCancelDto) {
        final String tid = paymentCancelDto.tid();
        final Long amount = paymentCancelDto.amount();
        return new KakaoPayCancelRequest(cid, tid, amount, 0L);
    }

    private int getTotalAmount(final List<PaymentReadyProductDto> paymentReadyProductDtos) {
        return paymentReadyProductDtos.stream()
                .mapToInt(PaymentReadyProductDto::totalAmount)
                .sum();
    }

    private int getQuantity(final List<PaymentReadyProductDto> paymentReadyProductDtos) {
        return paymentReadyProductDtos.stream()
                .mapToInt(PaymentReadyProductDto::quantity)
                .sum();
    }

    private String getProductName(final List<PaymentReadyProductDto> paymentReadyProductDtos) {
        final String firstProductName = paymentReadyProductDtos.get(0).name();
        final StringBuilder stringBuilder = new StringBuilder(firstProductName);
        if (paymentReadyProductDtos.size() > 1) {
            stringBuilder.append(PRODUCT_NAME_SEPARATOR);
            stringBuilder.append(paymentReadyProductDtos.size() - 1);
            stringBuilder.append(PRODUCT_NAME_SUFFIX);
        }

        return stringBuilder.toString();
    }
}
