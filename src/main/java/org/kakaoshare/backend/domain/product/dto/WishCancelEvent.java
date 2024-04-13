package org.kakaoshare.backend.domain.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kakaoshare.backend.domain.product.entity.Product;
@Getter
@AllArgsConstructor(staticName = "of")
public final class WishCancelEvent implements WishEvent{
    @NotNull
    private final String providerId;
    @NotNull
    private final Product product;
}
