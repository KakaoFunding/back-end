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
                        QProduct.product.productId.as("productId"),
                        QProduct.product.name,
                        QProduct.product.price,
                        QProduct.product.type,
                        QProduct.product.photo,
                        QProduct.product.productDetail.description.as("description"),
                        QProduct.product.productDescriptionPhotos.as("descriptionPhotos"),
                        QProduct.product.productDetail.hasPhoto.as("hasPhoto"),
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
        QProduct qProduct = QProduct.product;
        QProductDetail qProductDetail = QProductDetail.productDetail;

        return queryFactory
                .select(Projections.constructor(DetailResponse.class,
                        qProduct.productId,
                        qProduct.name,
                        qProduct.price,
                        qProduct.type,
                        qProductDetail.hasPhoto,
                        qProductDetail.productName,
                        qProductDetail.origin,
                        qProductDetail.manufacturer,
                        qProductDetail.tel,
                        qProductDetail.deliverDescription,
                        qProductDetail.billingNotice,
                        qProductDetail.caution,
                        qProduct.options.any(),
                        qProduct.brand))
                .from(qProduct)
                .leftJoin(qProduct.productDetail, qProductDetail)
                .where(qProduct.productId.eq(productId))
                .fetchOne();
    }
}
