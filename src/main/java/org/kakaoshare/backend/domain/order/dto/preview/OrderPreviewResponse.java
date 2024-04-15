package org.kakaoshare.backend.domain.order.dto.preview;

import com.querydsl.core.annotations.QueryProjection;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.util.List;

public record OrderPreviewResponse(
        Long productId, String name, String photo, Long totalAmount,
        List<String> optionNames, Integer quantity
) {
    @QueryProjection
    public OrderPreviewResponse {
    }

    public static OrderPreviewResponse of(final ProductDto productDto, final List<String> optionNames, final Integer quantity) {
        return new OrderPreviewResponse(
                productDto.getProductId(),
                productDto.getName(),
                productDto.getPhoto(),
                productDto.getPrice() * quantity,
                optionNames,
                quantity
        );
    }
}
