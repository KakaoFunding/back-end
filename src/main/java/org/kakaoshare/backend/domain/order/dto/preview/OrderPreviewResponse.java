package org.kakaoshare.backend.domain.order.dto.preview;

import com.querydsl.core.annotations.QueryProjection;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.util.List;

public record OrderPreviewResponse(ProductDto product, List<String> optionNames, Integer quantity) {
    @QueryProjection
    public OrderPreviewResponse {
    }
}
