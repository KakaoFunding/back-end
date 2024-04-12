package org.kakaoshare.backend.domain.wish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kakaoshare.backend.domain.product.dto.WishType;
import org.kakaoshare.backend.domain.product.entity.Product;
@Getter
@AllArgsConstructor(staticName = "of")
public final class WishReservationEvent {
    private final String providerId;
    private final WishType type;
    private final Product product;
}
