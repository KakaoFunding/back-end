package org.kakaoshare.backend.domain.product.dto;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public enum WishType {
    ME, OTHERS;
    
    public Boolean isPublic() {
        return this.equals(WishType.OTHERS);
    }
}
