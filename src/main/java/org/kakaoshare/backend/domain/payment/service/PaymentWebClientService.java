package org.kakaoshare.backend.domain.payment.service;

import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.approve.request.KakaoPayApproveRequest;
import org.kakaoshare.backend.domain.payment.dto.approve.response.KakaoPayApproveResponse;
import org.kakaoshare.backend.domain.payment.dto.ready.request.KakaoPayReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.response.KakaoPayReadyResponse;
import org.kakaoshare.backend.domain.payment.dto.success.request.PaymentSuccessRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PaymentWebClientService {
    private static final String SECRET_KEY_PREFIX = "DEV_SECRET_KEY ";
    private final String readyUrl;
    private final String approveUrl;
    private final String secretKey;
    private final WebClient webClient;
    private final KakaoPayRequestProvider requestProvider;

    public PaymentWebClientService(@Value("${pay.request-url.ready}") final String readyUrl,
                                   @Value("${pay.request-url.approve}") final String approveUrl,
                                   @Value("${pay.secret-key}") final String secretKey,
                                   final WebClient webClient,
                                   final KakaoPayRequestProvider requestProvider) {
        this.readyUrl = readyUrl;
        this.approveUrl = approveUrl;
        this.secretKey = secretKey;
        this.webClient = webClient;
        this.requestProvider = requestProvider;
    }

    public KakaoPayReadyResponse ready(final String providerId,
                                       final List<PaymentReadyRequest> paymentReadyRequests,
                                       final String orderNumber) {
        final KakaoPayReadyRequest kakaoPayReadyRequest = requestProvider.createReadyRequest(providerId, paymentReadyRequests, orderNumber);
        return webClient.post()
                .uri(readyUrl)
                .header(HttpHeaders.AUTHORIZATION, SECRET_KEY_PREFIX + secretKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(kakaoPayReadyRequest)
                .retrieve()
                .bodyToMono(KakaoPayReadyResponse.class)
                .block();
    }

    public KakaoPayApproveResponse approve(final String providerId,
                                           final PaymentSuccessRequest paymentSuccessRequest) {
        final KakaoPayApproveRequest kakaoPayApproveRequest = requestProvider.createApproveRequest(providerId, paymentSuccessRequest);
        return webClient.post()
                .uri(approveUrl)
                .header(HttpHeaders.AUTHORIZATION, SECRET_KEY_PREFIX + secretKey)
                .bodyValue(kakaoPayApproveRequest)
                .retrieve()
                .bodyToMono(KakaoPayApproveResponse.class)
                .block();
    }
}
