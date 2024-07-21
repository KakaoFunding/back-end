package org.kakaoshare.backend.domain.funding.dto.preview.response;

import org.kakaoshare.backend.domain.funding.dto.preview.request.FundingProductDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.util.List;

public record FundingPreviewResponse(Long productId, String brandName, String name, String photo, List<String> optionNames, FundingPreviewAmount amount) {

    public static FundingPreviewResponse of(final ProductDto productDto,
                                            final FundingProductDto fundingProductDto) {
        return new FundingPreviewResponse(
                productDto.getProductId(),
                productDto.getBrandName(),
                productDto.getName(),
                productDto.getPhoto(),
                null,   // TODO: 4/20/24 Funding 에 옵션 관련 컬럼이 없어 null로 대체
                new FundingPreviewAmount(productDto.getPrice(), fundingProductDto.goalAmount(), fundingProductDto.remainAmount())
        );
    }
}
