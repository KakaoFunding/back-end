package org.kakaoshare.backend.domain.product.dto;

import lombok.AllArgsConstructor;
import org.kakaoshare.backend.common.util.RequestedEnum;
@AllArgsConstructor
public enum WishType implements RequestedEnum {
    ME, OTHERS;
}
