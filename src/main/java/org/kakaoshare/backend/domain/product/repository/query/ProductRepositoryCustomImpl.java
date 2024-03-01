package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.entity.QProduct;
import org.kakaoshare.backend.domain.product.entity.QProductDetail;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public DescriptionResponse findProductWithDetailsAndPhotos(Long productId) {
        return queryFactory
                .select(Projections.bean(DescriptionResponse.class,
                        QProduct.product.name,
                        QProduct.product.price,
                        QProduct.product.type,
                        QProduct.product.photo,
                        QProduct.product.productDetail.description.as("description"),
                        QProduct.product.productDescriptionPhotos.as("descriptionPhotos"),
                        QProduct.product.productDetail.as("hasPhoto"),
                        QProduct.product.productDetail.productName.as("productName"),
                        QProduct.product.options,
                        QProduct.product.brand))
                .from(QProduct.product)
                .leftJoin(QProduct.product.productDetail).fetchJoin()
                .leftJoin(QProduct.product.productDescriptionPhotos).fetchJoin()
                .leftJoin(QProduct.product.options).fetchJoin()
                .where(QProduct.product.productId.eq(productId))
                .fetchOne();
    }

    public DetailResponse findProductDetail(Long productId) {
        return queryFactory
                .select(Projections.constructor(DetailResponse.class,
                        QProduct.product.productId,
                        QProduct.product.name,
                        QProduct.product.price,
                        QProduct.product.type,
                        QProduct.product.productDetail.hasPhoto,
                        QProduct.product.productDetail.productName,
                        QProduct.product.productDetail.origin,
                        QProduct.product.productDetail.manufacturer,
                        QProduct.product.productDetail.tel,
                        QProduct.product.productDetail.deliverDescription,
                        QProduct.product.productDetail.billingNotice,
                        QProduct.product.productDetail.caution,
                        QProduct.product.options.any(),
                        QProduct.product.brand))
                .from(QProduct.product)
                .leftJoin(QProduct.product.productDetail, QProductDetail.productDetail)
                .where(QProduct.product.productId.eq(productId))
                .fetchOne();
    }
}
