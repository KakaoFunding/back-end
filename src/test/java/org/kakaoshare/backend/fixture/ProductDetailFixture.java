package org.kakaoshare.backend.fixture;

import org.kakaoshare.backend.domain.product.entity.ProductDetail;

public enum ProductDetailFixture {
    TEST_PRODUCT_DETAIL("상세정보");
    private final String deliverDescription;

    ProductDetailFixture(String deliverDescription) {
        this.deliverDescription = deliverDescription;
    }

    public ProductDetail 생성() {
        return ProductDetail.builder()
                .deliverDescription(deliverDescription)
                .build();
    }
}
