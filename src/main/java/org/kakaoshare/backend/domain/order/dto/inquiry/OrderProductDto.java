package org.kakaoshare.backend.domain.order.dto.inquiry;

import com.querydsl.core.annotations.QueryProjection;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.time.LocalDateTime;

public record OrderProductDto(Long id,
                              String orderNumber,
                              String receiverName,
                              String receiverProviderId,
                              ProductDto product,
                              Integer quantity,
                              LocalDateTime orderDate,
                              String status) {
    @QueryProjection
    public OrderProductDto {
    }
}
