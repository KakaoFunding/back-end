package org.kakaoshare.backend.domain.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.common.dto.PageResponse;
import org.kakaoshare.backend.domain.option.repository.OptionDetailRepository;
import org.kakaoshare.backend.domain.order.dto.preview.OrderPreviewRequest;
import org.kakaoshare.backend.domain.order.dto.preview.OrderPreviewResponse;
import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kakaoshare.backend.fixture.ProductFixture.CAKE;
import static org.kakaoshare.backend.fixture.ProductFixture.COFFEE;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private OptionDetailRepository optionDetailRepository;

    @InjectMocks
    private OrderService orderService;

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
                        OrderPreviewResponse.of(productDtos.get(0), Collections.emptyList(), cakeQuantity),
                        OrderPreviewResponse.of(productDtos.get(1), Collections.emptyList(), coffeeQuantity)
                ),
                pageable,
                2L
        );
        final PageResponse<?> expect = PageResponse.from(page);
        final PageResponse<?> actual = orderService.preview(orderPreviewRequests, pageable);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expect);
    }

    private ProductDto getProductDto(final Product product) {
        return new ProductDto(product.getProductId(), product.getName(), product.getPhoto(), product.getPrice());
    }
}