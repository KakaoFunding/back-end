package org.kakaoshare.backend.fixture;
import org.kakaoshare.backend.domain.brand.entity.Brand;

public enum BrandFixture {
    BRAND_A("Brand A", "iconPhotoA.jpg"),
    BRAND_B("Brand B", "iconPhotoB.jpg"),
    BRAND_C("Brand C"); // 아이콘 없는 예시

    private final String name;
    private final String iconPhoto;

    BrandFixture(String name, String iconPhoto) {
        this.name = name;
        this.iconPhoto = iconPhoto;
    }

    BrandFixture(String name) {
        this.name = name;
        this.iconPhoto = null;
    }

    public Brand 생성() {
        return Brand.builder()
                .name(this.name)
                .iconPhoto(this.iconPhoto)
                .build();
    }
}

