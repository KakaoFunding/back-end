package org.kakaoshare.backend.domain.brand.entity.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class SimpleBrandDto {
    private Long brandId;
    private String name;
    private String iconPhoto;
    
    @QueryProjection
    public SimpleBrandDto(final Long brandId, final String name, final String iconPhoto) {
        this.brandId = brandId;
        this.name = name;
        this.iconPhoto = iconPhoto;
    }
}