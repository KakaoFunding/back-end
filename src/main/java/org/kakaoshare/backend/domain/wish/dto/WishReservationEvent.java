package org.kakaoshare.backend.domain.wish.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kakaoshare.backend.domain.product.dto.WishEvent;
import org.kakaoshare.backend.domain.product.dto.WishType;
import org.kakaoshare.backend.domain.product.entity.Product;
@Getter
@AllArgsConstructor(staticName = "of")
public final class WishReservationEvent implements WishEvent {
    @NotNull
    private final String providerId;
    private final WishType type;
    @NotNull
    private final Product product;
}
