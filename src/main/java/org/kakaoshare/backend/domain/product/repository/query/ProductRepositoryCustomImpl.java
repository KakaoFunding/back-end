package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.option.entity.QOption;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.QProduct;
import org.kakaoshare.backend.domain.product.entity.QProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.QProductDetail;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl {
    private final JPAQueryFactory queryFactory;

    public Product findProductWithDetailsAndPhotos(Long productId) {
        return queryFactory
                .selectFrom(QProduct.product)
                .leftJoin(QProduct.product.productDetail, QProductDetail.productDetail).fetchJoin()
                .leftJoin(QProduct.product.productDescriptionPhotos, QProductDescriptionPhoto.productDescriptionPhoto)
                .fetchJoin()
                .leftJoin(QProduct.product.options, QOption.option).fetchJoin()
                .where(QProduct.product.productId.eq(productId))
                .fetchOne();
    }
}
