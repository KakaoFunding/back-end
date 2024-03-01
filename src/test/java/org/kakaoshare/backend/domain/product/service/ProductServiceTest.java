package org.kakaoshare.backend.domain.product.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.query.ProductRepositoryCustomImpl;
import org.kakaoshare.backend.fixture.ProductFixture;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepositoryCustomImpl productRepositoryCustomImpl;

    @InjectMocks
    private ProductService productService;

    private final Long nonExistingProductId = 2L;

    @Test
    @DisplayName("상품 상세정보 조회 성공")
    void getProductDetail_Success() {
        final Product product = ProductFixture.TEST_PRODUCT.생성();
        final Long productId = product.getProductId();

        DetailResponse expectedDetailResponse = DetailResponse.builder()
                .deliverDescription("배송 설명")
                .build();

        doReturn(expectedDetailResponse)
                .when(productRepositoryCustomImpl)
                .findProductDetail(productId);

        final DetailResponse actual = productService.getProductDetail(productId);

        assertEquals(expectedDetailResponse.getDeliverDescription(), actual.getDeliverDescription());
    }

    @Test
    @DisplayName("상품 상세설명 조회 성공")
    void getProductDescription_Success() {
        final Long productId = 1L;
        DescriptionResponse expectedDescriptionResponse = DescriptionResponse.builder()
                .description("설명")
                .build();

        doReturn(expectedDescriptionResponse)
                .when(productRepositoryCustomImpl)
                .findProductWithDetailsAndPhotos(productId);

        DescriptionResponse actualDescriptionResponse = productService.getProductDescription(productId);

        assertEquals(expectedDescriptionResponse, actualDescriptionResponse);
        verify(productRepositoryCustomImpl).findProductWithDetailsAndPhotos(productId);
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 조회 시 예외 발생")
    void getProductDetail_WhenProductNotFound_ThenThrowException() {
        final Long nonExistingProductId = 999L;

        when(productRepositoryCustomImpl.findProductWithDetailsAndPhotos(nonExistingProductId)).thenReturn(null);

        assertThatThrownBy(() -> productService.getProductDescription(nonExistingProductId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product not found with id: " + nonExistingProductId);
    }

}
