package org.kakaoshare.backend.domain.product.repository.query;

import static org.kakaoshare.backend.domain.product.entity.QProduct.*;
import static org.kakaoshare.backend.domain.product.entity.QProductDetail.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.product.dto.DescriptionResponse;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public DescriptionResponse findProductWithDetailsAndPhotos(Long productId) {
        return queryFactory
                .select(Projections.bean(DescriptionResponse.class,
                        product.name,
                        product.price,
                        product.type,
                        product.photo,
                        product.productDetail.description.as("description"),
                        product.productDescriptionPhotos.as("descriptionPhotos"),
                        product.productDetail.as("hasPhoto"),
                        product.productDetail.productName.as("productName"),
                        product.options,
                        product.brand))
                .from(product)
                .leftJoin(product.productDetail).fetchJoin()
                .leftJoin(product.productDescriptionPhotos).fetchJoin()
                .leftJoin(product.options).fetchJoin()
                .where(product.productId.eq(productId))
                .fetchOne();
    }

    public DetailResponse findProductDetail(Long productId) {
        return queryFactory
                .select(Projections.constructor(DetailResponse.class,
                        product.productId,
                        product.name,
                        product.price,
                        product.type,
                        product.productDetail.hasPhoto,
                        product.productDetail.productName,
                        product.productDetail.origin,
                        product.productDetail.manufacturer,
                        product.productDetail.tel,
                        product.productDetail.deliverDescription,
                        product.productDetail.billingNotice,
                        product.productDetail.caution,
                        product.options.any(),
                        product.brand))
                .from(product)
                .leftJoin(product.productDetail, productDetail)
                .where(product.productId.eq(productId))
                .fetchOne();
    }
}
