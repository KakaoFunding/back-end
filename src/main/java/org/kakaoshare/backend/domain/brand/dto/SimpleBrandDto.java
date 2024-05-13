package org.kakaoshare.backend.domain.brand.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.kakaoshare.backend.domain.brand.entity.Brand;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
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

    public static SimpleBrandDto from(Brand brand) {
        return SimpleBrandDto.builder()
                .brandId(brand.getBrandId())
                .name(brand.getName())
                .iconPhoto(brand.getIconPhoto())
                .build();
    }
}