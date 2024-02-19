package org.kakaoshare.backend.domain.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.QProduct;
import org.kakaoshare.backend.domain.product.entity.QProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.QProductDetail;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Product findProductWithDetailsAndPhotos(Long productId) {
        QProduct qProduct = QProduct.product;
        QProductDetail qProductDetail = QProductDetail.productDetail;
        QProductDescriptionPhoto qProductDescriptionPhoto = QProductDescriptionPhoto.productDescriptionPhoto;


        return queryFactory
                .selectFrom(qProduct)
                .leftJoin(qProduct.productDetail, qProductDetail).fetchJoin()
                .leftJoin(qProduct.productDescriptionPhotos, qProductDescriptionPhoto).fetchJoin()
                .where(qProduct.productId.eq(productId))
                .fetchOne();
    }
}
