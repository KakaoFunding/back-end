package org.kakaoshare.backend.fixture;

import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.product.entity.Product;

public enum ProductFixture {
    TEST_PRODUCT("Test Product", 999L, "Test Type"),
    CAKE("케이크", 10_000L, "Dessert"),
    COFFEE("커피", 3_000L, "Beverage");

    private final String name;
    private final Long price;
    private final String type;

    ProductFixture(String name, Long price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public Product 생성() {
        return 생성(null);
    }

    public Product 생성(final Long providerId) {
        return 생성(providerId, null);
    }

    public Product 생성(final Long providerId,
                      final Brand brand) {
        return Product.builder()
                .productId(providerId)
                .brand(brand)
                .name(name)
                .price(price)
                .type(type)
                .build();
    }
}
