package org.kakaoshare.backend.domain.order.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.common.vo.date.exception.DateException;
import org.kakaoshare.backend.domain.option.dto.OptionSummaryResponse;
import org.kakaoshare.backend.domain.option.repository.OptionDetailRepository;
import org.kakaoshare.backend.domain.order.dto.inquiry.OrderHistoryDetailDto;
import org.kakaoshare.backend.domain.order.dto.inquiry.response.OrderHistoryDetailResponse;
import org.kakaoshare.backend.domain.order.dto.inquiry.request.OrderHistoryRequest;
import org.kakaoshare.backend.domain.order.dto.inquiry.OrderProductDto;
import org.kakaoshare.backend.domain.order.dto.inquiry.response.OrderHistoryResponse;
import org.kakaoshare.backend.domain.order.dto.preview.OrderPreviewRequest;
import org.kakaoshare.backend.domain.order.dto.preview.OrderPreviewResponse;
import org.kakaoshare.backend.domain.order.exception.OrderException;
import org.kakaoshare.backend.domain.order.repository.OrderRepository;
import org.kakaoshare.backend.domain.order.vo.OrderHistoryDate;
import org.kakaoshare.backend.domain.payment.dto.inquiry.PaymentHistoryDto;
import org.kakaoshare.backend.domain.payment.repository.PaymentRepository;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.kakaoshare.backend.domain.order.entity.OrderStatus.COMPLETE_PAYMENT;
import static org.kakaoshare.backend.fixture.ProductFixture.CAKE;
import static org.kakaoshare.backend.fixture.ProductFixture.COFFEE;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private OptionDetailRepository optionDetailRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private OrderService orderService;

    String providerId;

    @BeforeEach
    public void setUp() {
        providerId = "providerId";
    }

    @Test
    @DisplayName("주문 페이지에서 주문할 상품 조회")
    public void preview() throws Exception {
        final Product cake = CAKE.생성(1L);
        final int cakeQuantity = 1;
        final Product coffee = COFFEE.생성(2L);
        final int coffeeQuantity = 2;
        final Pageable pageable = Pageable.unpaged();

        final List<OrderPreviewRequest> orderPreviewRequests = List.of(
                new OrderPreviewRequest(cake.getProductId(), cakeQuantity, Collections.emptyList()),
                new OrderPreviewRequest(coffee.getProductId(), coffeeQuantity, Collections.emptyList())
        );

        final List<ProductDto> productDtos = List.of(
                getProductDto(cake),
                getProductDto(coffee)
        );

        final Page<ProductDto> pageProductDtos = new PageImpl<>(productDtos, pageable, 2L);
        final List<Long> productIds = List.of(cake.getProductId(), coffee.getProductId());

        doReturn(Collections.emptyList()).when(optionDetailRepository).findNamesByIds(Collections.emptyList());
        doReturn(pageProductDtos).when(productRepository).findAllByProductIds(productIds, pageable);

        final Page<OrderPreviewResponse> page = new PageImpl<>(
                List.of(
                        new OrderPreviewResponse(productDtos.get(0), Collections.emptyList(), cakeQuantity),
                        new OrderPreviewResponse(productDtos.get(1), Collections.emptyList(), coffeeQuantity)
                ),
                pageable,
                2L
        );
        final PageResponse<?> expect = PageResponse.from(page);
        final PageResponse<?> actual = orderService.preview(orderPreviewRequests, pageable);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("주문 내역 조회")
    public void lookUp() throws Exception {
        final LocalDate startDate = LocalDate.of(2024, 1, 1);
        final LocalDate endDate = LocalDate.of(2024, 2, 1);
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt"));

        final Product cake = CAKE.생성();
        final Product coffee = COFFEE.생성();

        final OrderHistoryRequest orderHistoryRequest = new OrderHistoryRequest(startDate, endDate);
        final List<OrderProductDto> orderProductDtos = List.of(
                new OrderProductDto(1L, "123", "받는이1", providerId, getProductDto(cake), 1, LocalDateTime.of(2024, 1, 1, 0, 0), COMPLETE_PAYMENT.getDescription()),
                new OrderProductDto(2L, "456", "받는이2", providerId, getProductDto(coffee), 2, LocalDateTime.of(2024, 1, 3, 0, 0), COMPLETE_PAYMENT.getDescription())
        );

        final OrderHistoryDate date = orderHistoryRequest.toDate();
        final Page<OrderProductDto> page = new PageImpl<>(orderProductDtos, pageable, orderProductDtos.size());
        doReturn(page).when(orderRepository).findAllOrderProductDtoByCondition(providerId, date, pageable);

        final Page<OrderHistoryResponse> orderHistoryResponses = page.map(
                orderProductDto -> OrderHistoryResponse.of(orderProductDto, providerId)
        );

        final PageResponse<?> expect = PageResponse.from(orderHistoryResponses);
        final PageResponse<?> actual = orderService.lookUp(providerId, orderHistoryRequest, pageable);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("주문 내역 조회 시 조회 시작, 종료일이 모두 NULL이면 예외 발생")
    public void lookUpWhenDateIsNull() throws Exception {
        final LocalDate startDate = null;
        final LocalDate endDate = null;
        final OrderHistoryRequest orderHistoryRequest = new OrderHistoryRequest(startDate, endDate);
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt"));
        assertThatThrownBy(() -> orderService.lookUp(providerId, orderHistoryRequest, pageable))
                .isInstanceOf(DateException.class);
    }

    @Test
    @DisplayName("주문 내역 조회 시 종료일이 시작일보다 앞서면 예외 발생")
    public void lookUpWhenInvalidDate() throws Exception {
        final LocalDate startDate = LocalDate.of(2024, 2, 1);
        final LocalDate endDate = LocalDate.of(2024, 1, 1);
        final OrderHistoryRequest orderHistoryRequest = new OrderHistoryRequest(startDate, endDate);
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt"));
        assertThatThrownBy(() -> orderService.lookUp(providerId, orderHistoryRequest, pageable))
                .isInstanceOf(DateException.class);
    }

    @Test
    @DisplayName("주문 내역 조회 기간이 1년이 넘으면 예외 발생")
    public void lookUpWhenInvalidDateRange() throws Exception {
        final LocalDate startDate = LocalDate.of(2025, 9, 1);
        final LocalDate endDate = LocalDate.of(2024, 9, 1);
        final OrderHistoryRequest orderHistoryRequest = new OrderHistoryRequest(startDate, endDate);
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt"));
        assertThatThrownBy(() -> orderService.lookUp(providerId, orderHistoryRequest, pageable))
                .isInstanceOf(DateException.class);
    }

    @Test
    @DisplayName("주문 내역 상세 조회")
    public void lookUpDetail() throws Exception {
        final Long orderId = 1L;
        final Product cake = CAKE.생성();
        final List<OptionSummaryResponse> options = List.of(
                new OptionSummaryResponse("맛", "생크림"),
                new OptionSummaryResponse("크기", "도시락")
        );

        final OrderHistoryDetailDto orderHistoryDetailDto = new OrderHistoryDetailDto(getProductDto(cake), 1, options);
        final PaymentHistoryDto paymentHistoryDto = new PaymentHistoryDto(0L, cake.getPrice());

        doReturn(Optional.of(orderHistoryDetailDto)).when(orderRepository).findHistoryDetailById(orderId);
        doReturn(Optional.of(paymentHistoryDto)).when(paymentRepository).findHistoryByOrderId(orderId);

        final OrderHistoryDetailResponse expect = new OrderHistoryDetailResponse(orderHistoryDetailDto, paymentHistoryDto);
        final OrderHistoryDetailResponse actual = orderService.lookUpDetail(orderId);

        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("주문 내역 상세 조회 시 PK가 파라미터 orderId인 레코드가 없으면 예외 발생")
    public void lookUpDetailWhenNotExistByOrderId() throws Exception {
        final Long orderId = 1L;
        doReturn(Optional.empty()).when(orderRepository).findHistoryDetailById(orderId);
        assertThatThrownBy(() -> orderService.lookUpDetail(orderId))
                .isInstanceOf(OrderException.class);
    }

    private ProductDto getProductDto(final Product product) {
        return new ProductDto(product.getProductId(), product.getName(), product.getPhoto(), product.getPrice(), product.getBrandName());
    }
}