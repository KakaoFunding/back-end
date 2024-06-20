package org.kakaoshare.backend.domain.order.dto.inquiry.response;

import org.kakaoshare.backend.domain.order.dto.inquiry.OrderProductDto;
import org.kakaoshare.backend.domain.product.dto.ProductDto;

import java.time.LocalDateTime;

public record OrderHistoryResponse(Long id,
                                   String orderNumber,
                                   String receiverName,
                                   Boolean self,
                                   ProductDto product,
                                   Integer quantity,
                                   LocalDateTime orderDate,
                                   String status) {
    public static OrderHistoryResponse of(final OrderProductDto orderProductDto, final String providerId) {
        return new OrderHistoryResponse(
                orderProductDto.id(),
                orderProductDto.orderNumber(),
                orderProductDto.receiverName(),
                orderProductDto.receiverProviderId().equals(providerId),
                orderProductDto.product(),
                orderProductDto.quantity(),
                orderProductDto.orderDate(),
                orderProductDto.status()
        );
    }
}
