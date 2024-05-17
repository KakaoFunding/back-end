package org.kakaoshare.backend.domain.order.dto.inquiry;

import org.kakaoshare.backend.domain.option.dto.OptionSummaryResponse;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.util.List;

public record OrderHistoryDetailDto(ProductDto product, Integer quantity, List<OptionSummaryResponse> options) {

    public OrderHistoryDetailDto {
    }
}
