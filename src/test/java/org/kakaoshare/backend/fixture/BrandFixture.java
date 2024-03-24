package org.kakaoshare.backend.fixture;


import org.kakaoshare.backend.domain.brand.entity.Brand;

public enum BrandFixture {
    STARBUCKS("스타벅스");

    private final String name;

    BrandFixture(final String name) {
        this.name = name;
    }

    public Brand 생성() {
        return 생성(null);
    }

    public Brand 생성(final Long brandId) {
        return Brand.builder()
                .brandId(brandId)
                .name(name)
                .build();
    }
}

