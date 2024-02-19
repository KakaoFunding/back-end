package org.kakaoshare.backend.domain.product.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailResponse {
    private Long productId;
    private String name;
    private BigDecimal price;
    private String photo;
    private String type;
    private String description;
    private List<String> additionalPhotos;

}
