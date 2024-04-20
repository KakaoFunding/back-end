package org.kakaoshare.backend.domain.funding.dto.preview.response;

import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.util.List;

public record FundingPreviewResponse(Long productId, String name, String photo, List<String> optionNames, FundingPreviewAmount amount) {

    public static FundingPreviewResponse of(final ProductDto productDto, final Long attributeAmount) {
        return new FundingPreviewResponse(
                productDto.getProductId(),
                productDto.getName(),
                productDto.getPhoto(),
                null,   // TODO: 4/20/24 Funding 에 옵션 관련 컬럼이 없어 null로 대체
                FundingPreviewAmount.of(productDto.getPrice(), attributeAmount)
        );
    }
}
