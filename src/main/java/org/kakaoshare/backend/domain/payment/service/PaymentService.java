package org.kakaoshare.backend.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.order.dto.OrderSummary;
import org.kakaoshare.backend.domain.order.entity.Order;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.payment.dto.PaymentDetail;
import org.kakaoshare.backend.domain.payment.dto.approve.response.KakaoPayApproveResponse;
import org.kakaoshare.backend.domain.payment.dto.ready.request.PaymentReadyRequest;
import org.kakaoshare.backend.domain.payment.dto.ready.response.KakaoPayReadyResponse;
import org.kakaoshare.backend.domain.payment.dto.ready.response.PaymentReadyResponse;
import org.kakaoshare.backend.domain.payment.dto.success.request.PaymentSuccessRequest;
import org.kakaoshare.backend.domain.payment.dto.success.response.PaymentSuccessResponse;
import org.kakaoshare.backend.domain.payment.dto.success.response.Receiver;
import org.kakaoshare.backend.domain.payment.entity.Payment;
import org.kakaoshare.backend.domain.payment.repository.PaymentRepository;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderNumberProvider orderNumberProvider;
    private final PaymentRepository paymentRepository;
    private final PaymentWebClientService webClientService;
    private final ProductRepository productRepository;

    public PaymentReadyResponse ready(final String providerId,
                                      final List<PaymentReadyRequest> paymentRequests) {
        final String orderNumber = orderNumberProvider.createOrderNumber();
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
        // TODO: 3/16/24 PaymentDetail -> Order 를 만들어주는 별도의 방법 필요. 아래 방법은 불안정
        final List<Long> productIds = extractedProductIds(details);
        final Map<Long, Product> productById = findProductsByIdsGroupById(productIds);
        return details.stream()
                .map(detail -> createOrder(member, orderNumber, payment, productById.get(detail.productId()), detail.stockQuantity()))
                .toList();
    }

    private Map<Long, Product> findProductsByIdsGroupById(final List<Long> productIds) {
        return productRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(
                        Product::getProductId,
                        product -> product
                ));
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

    private List<Long> extractedProductIds(final List<PaymentDetail> details) {
        return details.stream()
                .map(PaymentDetail::productId)
                .toList();
    }

    private Payment createPayment(final KakaoPayApproveResponse approveResponse) {
        return Payment.builder()
                .totalPrice(Long.valueOf(approveResponse.amount().total()))
                .purchasePrice(Long.valueOf(approveResponse.amount().total()))
                .build();
    }

    private Order createOrder(final Member member,
                              final String orderNumber,
                              final Payment payment,
                              final Product product,
                              final int stockQuantity) {
        return Order.builder()
                .product(product)
                .member(member)
                .payment(payment)
                .orderNumber(orderNumber)
                .stockQuantity(stockQuantity)
                .build();
    }

    private Member findMemberByProviderId(final String providerId) {
        return memberRepository.findByProviderId(providerId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
