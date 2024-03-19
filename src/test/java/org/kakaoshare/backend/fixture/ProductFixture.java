package org.kakaoshare.backend.fixture;

import java.math.BigDecimal;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.product.entity.Product;

public enum ProductFixture {
    TEST_PRODUCT("Test Product", new BigDecimal("999.99"), "Test Type");

    private final String name;
    private final BigDecimal price;
    private final String type;

    ProductFixture(String name, BigDecimal price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public Product 생성() {
        return Product.builder()
                .name(name)
                .price(price)
                .type(type)
                .build();
    }

    public Product 생성(final Brand brand) {
        return Product.builder()
                .name(name)
                .price(price)
                .type(type)
                .brand(brand)
                .build();
    }


}
