package org.kakaoshare.backend.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductDetail;
import org.kakaoshare.backend.domain.product.repository.ProductQueryRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductQueryRepository productQueryRepository;

    @InjectMocks
    private ProductService productService;

    private final Long existingProductId = 1L;
    private final Long nonExistingProductId = 2L;

    @BeforeEach
    void setUp() {
        Product mockProduct = mock(Product.class);
        when(mockProduct.getProductId()).thenReturn(existingProductId);
        when(mockProduct.getName()).thenReturn("Test Product");
        when(mockProduct.getPrice()).thenReturn(new BigDecimal("999.99"));
        when(mockProduct.getType()).thenReturn("Test Type");

        ProductDetail mockProductDetail = mock(ProductDetail.class);
        when(mockProductDetail.getDescription()).thenReturn("Test Description");
        when(mockProductDetail.getHasPhoto()).thenReturn(true);
        when(mockProductDetail.getProductName()).thenReturn("Test Product Name");
        when(mockProductDetail.getOrigin()).thenReturn("Test Origin");
        when(mockProductDetail.getManufacturer()).thenReturn("Test Manufacturer");
        when(mockProductDetail.getTel()).thenReturn("Test Tel");
        when(mockProductDetail.getDeliverDescription()).thenReturn("Test Deliver Description");
        when(mockProductDetail.getBillingNotice()).thenReturn("Test Billing Notice");
        when(mockProductDetail.getCaution()).thenReturn("Test Caution");

        when(mockProduct.getProductDetail()).thenReturn(mockProductDetail);
        when(mockProduct.getProductDescriptionPhotos()).thenReturn(new ArrayList<>());
        when(mockProduct.getOptions()).thenReturn(new ArrayList<>());
        when(mockProduct.getBrand()).thenReturn(mock(Brand.class));

        when(productQueryRepository.findProductWithDetailsAndPhotos(existingProductId)).thenReturn(mockProduct);
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void getProductDetail_Success() {
        DetailResponse result = productService.getProductDetail(existingProductId);

        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(existingProductId);
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 조회 시 예외 발생")
    void getProductDetail_WhenProductNotFound_ThenThrowException() {

        assertThatThrownBy(() -> productService.getProductDetail(nonExistingProductId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product not found with id: " + nonExistingProductId);
    }
}
