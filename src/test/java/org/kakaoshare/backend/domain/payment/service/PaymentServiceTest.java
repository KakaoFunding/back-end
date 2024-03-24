package org.kakaoshare.backend.domain.payment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.order.dto.OrderSummary;
import org.kakaoshare.backend.domain.order.entity.Order;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.payment.dto.PaymentDetail;
import org.kakaoshare.backend.domain.payment.dto.approve.response.Amount;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.fixture.BrandFixture.STARBUCKS;
import static org.kakaoshare.backend.fixture.MemberFixture.KAKAO;
import static org.kakaoshare.backend.fixture.ProductFixture.CAKE;
import static org.kakaoshare.backend.fixture.ProductFixture.COFFEE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PaymentWebClientService webClientService;

    @Mock
    private OrderNumberProvider orderNumberProvider;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;


    @Test
    @DisplayName("결제 준비")
    public void ready() throws Exception {
        final int stockQuantity = 1;
        final String providerId = "1234";
        final String orderNumber = "12345678";
        final Product cake = CAKE.생성(1L);
        final Product coffee = COFFEE.생성(2L);
        final List<PaymentReadyRequest> readyRequests = List.of(
                createPaymentReadyRequest(cake, 1),
                createPaymentReadyRequest(coffee, 1)
        );

        final KakaoPayReadyResponse readyResponse = createReadyResponse();
        doReturn(orderNumber).when(orderNumberProvider).createOrderNumber();
        doReturn(readyResponse).when(webClientService).ready(providerId, readyRequests, orderNumber);

        final List<PaymentDetail> details = createDetails(stockQuantity, cake, coffee);

        final PaymentReadyResponse expect = new PaymentReadyResponse(
                readyResponse.tid(),
                details,
                readyResponse.next_redirect_pc_url(),
                orderNumber
        );
        final PaymentReadyResponse actual = paymentService.ready(providerId, readyRequests);
        assertThat(actual).isEqualTo(expect);   // TODO: 3/15/24 equals() 및 hashCode()가 재정의되있으므로 isEqualTo() 사용
    }

    @Test
    @DisplayName("결제 승인")
    public void approve() throws Exception {
        final Member member = KAKAO.생성();
        final int stockQuantity = 1;
        final String providerId = member.getProviderId();
        final String orderNumber = "12345678";
        final String pgToken = "pgToken";
        final String tid = "tid";

        final Brand brand = STARBUCKS.생성(1L);
        final Product cake = CAKE.생성(1L, brand);
        final Product coffee = COFFEE.생성(2L, brand);
        final List<PaymentDetail> details = createDetails(stockQuantity, cake, coffee);
        final PaymentSuccessRequest paymentSuccessRequest = new PaymentSuccessRequest(details, orderNumber, pgToken, tid);

        final int totalAmount = cake.getPrice().add(coffee.getPrice()).intValue();
        final String itemName = cake.getName() + " 외 1건";

        final KakaoPayApproveResponse approveResponse = createApproveResponse(tid, orderNumber, providerId, totalAmount, itemName, 2);
        final Payment payment = createPayment(totalAmount);
        final List<Order> orders = createOrders(member, orderNumber, payment, cake, coffee);

        doReturn(approveResponse).when(webClientService).approve(providerId, paymentSuccessRequest);
        doReturn(payment).when(paymentRepository).save(any());  // TODO: 3/16/24 save() 에서 new로 다른 객체가 생성되므로 any()로 대체
        doReturn(Optional.of(member)).when(memberRepository).findByProviderId(providerId);
        doReturn(orders).when(orderRepository).saveAll(any());  // TODO: 3/16/24 saveAll() 에서 new로 다른 객체가 생성되므로 any()로 대체
        doReturn(List.of(cake, coffee)).when(productRepository).findAllById(List.of(cake.getProductId(), coffee.getProductId()));

        final Receiver receiver = Receiver.empty();
        final List<OrderSummary> orderSummaries = orders.stream()
                .map(OrderSummary::from)
                .toList();

        final PaymentSuccessResponse expect = new PaymentSuccessResponse(receiver, orderSummaries);
        final PaymentSuccessResponse actual = paymentService.approve(providerId, paymentSuccessRequest);

        assertThat(actual).isEqualTo(expect);   // TODO: 3/16/24 equals() 및 hashCode()가 재정의되있으므로 isEqualTo() 사용
    }

    private List<Order> createOrders(final Member member,
                                     final String orderNumber,
                                     final Payment payment,
                                     final Product... products) {
        return Arrays.stream(products)
                .map(product -> createOrder(member, payment, orderNumber, product))
                .toList();
    }

    private Order createOrder(final Member member,
                              final Payment payment,
                              final String orderNumber,
                              final Product product) {
        return Order.builder()
                .member(member)
                .payment(payment)
                .product(product)
                .orderNumber(orderNumber)
                .stockQuantity(1)
                .build();
    }

    private Payment createPayment(final int totalAmount) {
        return Payment.builder()
                .purchasePrice(BigDecimal.valueOf(totalAmount))
                .totalPrice(BigDecimal.valueOf(totalAmount))
                .build();
    }

    private List<PaymentDetail> createDetails(final int stockQuantity,
                                              final Product... products) {
        return Arrays.stream(products)
                .map(product -> new PaymentDetail(product.getProductId(), stockQuantity))
                .toList();
    }

    private PaymentReadyRequest createPaymentReadyRequest(final Product product,
                                                          final int stockQuantity) {
        return new PaymentReadyRequest(
                product.getProductId(),
                product.getPrice().intValue(),
                0,
                stockQuantity,
                product.getName()
        );
    }

    private KakaoPayReadyResponse createReadyResponse() {
        return new KakaoPayReadyResponse(
                "tid",
                "app_redirect_url",
                "mobile_redirect_url",
                "pc_redirect_url",
                null,
                null,
                LocalDateTime.now()
        );
    }

    private KakaoPayApproveResponse createApproveResponse(final String tid,
                                                          final String orderNumber,
                                                          final String providerId,
                                                          final int totalAmount,
                                                          final String itemName,
                                                          final int totalStockQuantity) {
        return new KakaoPayApproveResponse(
                "aid", tid, "cid", "sid",
                orderNumber, providerId, null,
                new Amount(totalAmount, 0, 0, 0, 0, 0),
                null, itemName, null, totalStockQuantity, null, null, null
        );
    }

    private Amount createAmount(final int totalAmount) {
        return new Amount(totalAmount, 0, 0, 0, 0, 0);
    }
}