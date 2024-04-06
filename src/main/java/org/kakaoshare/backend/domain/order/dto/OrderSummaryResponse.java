package org.kakaoshare.backend.domain.order.dto;

import org.kakaoshare.backend.domain.option.dto.OptionSummaryResponse;
import org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse;

import java.util.List;

public record OrderSummaryResponse(ProductSummaryResponse product, Integer quantity, List<OptionSummaryResponse> options) {

}
