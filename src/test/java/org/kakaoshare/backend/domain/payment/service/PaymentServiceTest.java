package org.kakaoshare.backend.domain.payment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.common.util.RedisUtils;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.gift.repository.GiftRepository;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.option.repository.OptionDetailRepository;
import org.kakaoshare.backend.domain.order.dto.OrderSummaryResponse;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.payment.dto.OrderDetail;
import org.kakaoshare.backend.domain.payment.dto.OrderDetails;
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
import org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    private GiftRepository giftRepository;

    @Mock
    private OrderNumberProvider orderNumberProvider;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OptionDetailRepository optionDetailRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RedisUtils redisUtils;

    @InjectMocks
    private PaymentService paymentService;


    @Test
    @DisplayName("결제 준비")
    public void ready() throws Exception {
        final String providerId = "1234";
        final String orderDetailsKey = "12345678";

        final Product cake = CAKE.생성(1L);
        final int cakeStockQuantity = 1;

        final Product coffee = COFFEE.생성(2L);
        final int coffeeStockQuantity = 2;

        final List<PaymentReadyRequest> readyRequests = List.of(
                createPaymentReadyRequest(cake, cakeStockQuantity),
                createPaymentReadyRequest(coffee, coffeeStockQuantity)
        );

        final Map<Long, Long> pricesGroupByIds = Map.of(cake.getProductId(), cake.getPrice(), coffee.getProductId(), coffee.getPrice());
        final KakaoPayReadyResponse readyResponse = createReadyResponse();
        doReturn(orderDetailsKey).when(orderNumberProvider).createOrderDetailKey();
        doReturn(pricesGroupByIds).when(productRepository).findAllPriceByIdsGroupById(List.of(cake.getProductId(), coffee.getProductId()));
        doReturn(readyResponse).when(webClientService).ready(providerId, readyRequests, orderDetailsKey);

        final PaymentReadyResponse expect = new PaymentReadyResponse(
                readyResponse.tid(),
                readyResponse.next_redirect_pc_url(),
                orderDetailsKey
        );
        final PaymentReadyResponse actual = paymentService.ready(providerId, readyRequests);
        assertThat(actual).isEqualTo(expect);   // TODO: 3/15/24 equals() 및 hashCode()가 재정의되있으므로 isEqualTo() 사용
    }

    @Test
    @DisplayName("결제 승인 (나에게 선물, 옵션 선택 X)")
    public void approve() throws Exception {
        final Member member = KAKAO.생성();
        final String providerId = member.getProviderId();

        final String pgToken = "pgToken";
        final String tid = "tid";
        final String orderDetailKey = "orderDetailKey";

        final Brand brand = STARBUCKS.생성(1L);

        final Product cake = CAKE.생성(1L, brand);
        final int cakeStockQuantity = 1;
        final String cakeOrderNumber = "1111";

        final Product coffee = COFFEE.생성(2L, brand);
        final int coffeeStockQuantity = 1;
        final String coffeeOrderNumber = "2222";

        final OrderDetails orderDetails = new OrderDetails(
                List.of(
                        new OrderDetail(cakeOrderNumber, cake.getProductId(), cakeStockQuantity, null),
                        new OrderDetail(coffeeOrderNumber, coffee.getProductId(), coffeeStockQuantity, null)
                )
        );

        final PaymentSuccessRequest paymentSuccessRequest = new PaymentSuccessRequest(orderDetailKey, pgToken, tid);

        final int totalAmount = (int) (cake.getPrice() * cakeStockQuantity + coffee.getPrice() * coffeeStockQuantity);
        final String itemName = cake.getName() + " 외 1건";

        final KakaoPayApproveResponse approveResponse = createApproveResponse(tid, orderDetailKey, providerId, totalAmount, itemName, cakeStockQuantity + coffeeStockQuantity);
        final Payment payment = createPayment(tid, totalAmount);

        doReturn(orderDetails).when(redisUtils).remove(orderDetailKey, OrderDetails.class);
        doReturn(approveResponse).when(webClientService).approve(providerId, paymentSuccessRequest);
        doReturn(payment).when(paymentRepository).save(any());  // TODO: 3/16/24 save() 에서 new로 다른 객체가 생성되므로 any()로 대체
        doReturn(Optional.of(member)).when(memberRepository).findByProviderId(providerId);
        doReturn(cake).when(productRepository).getReferenceById(cake.getProductId());
        doReturn(coffee).when(productRepository).getReferenceById(coffee.getProductId());
        doReturn(null).when(giftRepository).saveAll(any());
        doReturn(null).when(orderRepository).saveAll(any());  // TODO: 3/16/24 saveAll() 에서 new로 다른 객체가 생성되므로 any()로 대체

        final ProductSummaryResponse cakeSummaryResponse = new ProductSummaryResponse(brand.getName(), cake.getName(), cake.getPrice() * cakeStockQuantity);
        final ProductSummaryResponse coffeeSummaryResponse = new ProductSummaryResponse(brand.getName(), coffee.getName(), coffee.getPrice() * coffeeStockQuantity);
        doReturn(cakeSummaryResponse).when(productRepository).findAllProductSummaryById(cake.getProductId());
        doReturn(coffeeSummaryResponse).when(productRepository).findAllProductSummaryById(coffee.getProductId());

        final List<OrderSummaryResponse> orderSummaries = List.of(
                new OrderSummaryResponse(cakeSummaryResponse, cakeStockQuantity, Collections.emptyList()),
                new OrderSummaryResponse(coffeeSummaryResponse, coffeeStockQuantity, Collections.emptyList())
        );
        final Receiver receiver = Receiver.from(member);
        final PaymentSuccessResponse expect = new PaymentSuccessResponse(receiver, orderSummaries);
        final PaymentSuccessResponse actual = paymentService.approve(providerId, paymentSuccessRequest);
        assertThat(actual).isEqualTo(expect);   // TODO: 3/16/24 equals() 및 hashCode()가 재정의되있으므로 isEqualTo() 사용
    }

    private Payment createPayment(final String paymentNumber,
                                  final int totalAmount) {
        return Payment.builder()
                .paymentNumber(paymentNumber)
                .purchasePrice((long) totalAmount)
                .totalPrice((long) totalAmount)
                .build();
    }

    private PaymentReadyRequest createPaymentReadyRequest(final Product product,
                                                          final int stockQuantity) {
        return new PaymentReadyRequest(
                product.getProductId(),
                product.getName(),
                product.getPrice().intValue(),
                0,
                stockQuantity,
                null
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
}