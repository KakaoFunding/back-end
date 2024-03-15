package org.kakaoshare.backend.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.order.dto.OrderSummary;
import org.kakaoshare.backend.domain.order.entity.Order;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.payment.dto.PaymentDetail;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.response.PaymentReadyResponse;
import org.kakaoshare.backend.domain.payment.dto.approve.response.KakaoPayApproveResponse;
import org.kakaoshare.backend.domain.payment.dto.ready.response.KakaoPayReadyResponse;
import org.kakaoshare.backend.domain.payment.dto.success.request.PaymentSuccessRequest;
import org.kakaoshare.backend.domain.payment.dto.success.response.PaymentSuccessResponse;
import org.kakaoshare.backend.domain.payment.dto.success.response.Receiver;
import org.kakaoshare.backend.domain.payment.entity.Payment;
import org.kakaoshare.backend.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentWebClientService webClientService;

    public PaymentReadyResponse ready(final String providerId,
                                      final List<PaymentReadyRequest> paymentRequests) {
        final String orderNumber = createOrderNumber();
        final KakaoPayReadyResponse kakaoPayReadyResponse = webClientService.ready(providerId, paymentRequests, orderNumber);
        final List<PaymentDetail> details = extractedDetails(paymentRequests);
        return new PaymentReadyResponse(kakaoPayReadyResponse.tid(), details, kakaoPayReadyResponse.next_redirect_pc_url(), orderNumber);
    }

    @Transactional
    public PaymentSuccessResponse approve(final String providerId,
                                          final PaymentSuccessRequest paymentSuccessRequest) {
        final KakaoPayApproveResponse approveResponse = webClientService.approve(providerId, paymentSuccessRequest);
        final Payment payment = savePayment(approveResponse);
        final List<Order> orders = saveOrders(providerId, paymentSuccessRequest, payment);
        final List<OrderSummary> orderSummaries = extractedOrderSummaries(orders);
        return new PaymentSuccessResponse(Receiver.empty(), orderSummaries); // TODO: 3/15/24 친구 목록이 구현되지 않아 Receiver.empty()로 대체
    }

    private Payment savePayment(final KakaoPayApproveResponse approveResponse) {
        final Payment payment = createPayment(approveResponse);
        return paymentRepository.save(payment);
    }

    private List<Order> saveOrders(final String providerId,
                                   final PaymentSuccessRequest paymentSuccessRequest,
                                   final Payment payment) {
        final Member member = findMemberByProviderId(providerId);
        final String orderNumber = paymentSuccessRequest.orderNumber();
        final List<PaymentDetail> details = paymentSuccessRequest.details();
        final List<Order> orders = extractedOrder(member, details, orderNumber, payment);
        return orderRepository.saveAll(orders);
    }

    private List<Order> extractedOrder(final Member member,
                                       final List<PaymentDetail> details,
                                       final String orderNumber,
                                       final Payment payment) {
        return details.stream()
                .map(detail -> detail.of(member, orderNumber, payment))
                .toList();
    }

    private List<OrderSummary> extractedOrderSummaries(final List<Order> orders) {
        return orders.stream()
                .map(OrderSummary::from)
                .toList();
    }

    private List<PaymentDetail> extractedDetails(final List<PaymentReadyRequest> paymentRequests) {
        return paymentRequests.stream()
                .map(paymentRequest -> new PaymentDetail(paymentRequest.productId(), paymentRequest.stockQuantity()))
                .toList();
    }

    private Payment createPayment(final KakaoPayApproveResponse approveResponse) {
        return Payment.builder()
                .totalPrice(BigDecimal.valueOf(approveResponse.amount().total()))
                .purchasePrice(BigDecimal.valueOf(approveResponse.amount().total()))
                .build();
    }

    private Member findMemberByProviderId(final String providerId) {
        return memberRepository.findByProviderId(providerId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private String createOrderNumber() {
        return String.valueOf(UUID.randomUUID().toString().hashCode()); // TODO: 3/15/24 주문 번호를 UUID의 해시코드 값으로 설정
    }
}
