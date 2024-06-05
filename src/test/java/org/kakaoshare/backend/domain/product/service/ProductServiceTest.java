package org.kakaoshare.backend.domain.product.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.member.repository.MemberRepository;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.exception.ProductException;
import org.kakaoshare.backend.domain.product.repository.ProductRepository;
import org.kakaoshare.backend.fixture.MemberFixture;
import org.kakaoshare.backend.fixture.ProductFixture;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private ProductService productService;

    private final Long nonExistingProductId = 2L;

    @Test
    @DisplayName("상품 상세정보 조회 성공")
    void getProductDetail_Success() {
        // Arrange
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성();
        Long productId = product.getProductId();

        DetailResponse expectedDetailResponse = DetailResponse.builder()
                .deliverDescription("배송 설명")
                .build();

        when(memberRepository.findMemberByProviderId(member.getProviderId()))
                .thenReturn(Optional.of(member));
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));
        when(productRepository.findProductDetailWithMember(product, member))
                .thenReturn(expectedDetailResponse);

        DetailResponse actual = productService.getProductDetail(productId, member.getProviderId());

        assertEquals(expectedDetailResponse.getDeliverDescription(), actual.getDeliverDescription());
    }

    @Test
    @DisplayName("상품 상세설명 조회 성공")
    void getProductDescription_Success() {
        Member member = MemberFixture.KAKAO.생성();
        Product product = ProductFixture.TEST_PRODUCT.생성();
        Long productId = product.getProductId();

        DescriptionResponse expectedDescriptionResponse = DescriptionResponse.builder()
                .description("설명")
                .build();

        when(memberRepository.findMemberByProviderId(member.getProviderId()))
                .thenReturn(Optional.of(member));
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));
        doReturn(expectedDescriptionResponse)
                .when(productRepository)
                .findProductWithDetailsAndPhotosWithMember(product,member);

        DescriptionResponse actualDescriptionResponse = productService.getProductDescription(productId,member.getProviderId());

        assertEquals(expectedDescriptionResponse, actualDescriptionResponse);
        verify(productRepository).findProductWithDetailsAndPhotosWithMember(product,member);
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 조회 시 예외 발생")
    void getProductDetail_WhenProductNotFound_ThenThrowException() {
        Long nonExistingProductId = 999L;

        assertThatThrownBy(() -> productService.getProductDescription(nonExistingProductId, null))
                .isInstanceOf(ProductException.class);
    }
}
