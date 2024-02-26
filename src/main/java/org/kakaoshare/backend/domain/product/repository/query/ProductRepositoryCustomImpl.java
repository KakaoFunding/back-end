package org.kakaoshare.backend.domain.product.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.option.entity.QOption;
import org.kakaoshare.backend.domain.product.dto.DetailDescriptionResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.QProduct;
import org.kakaoshare.backend.domain.product.entity.QProductDescriptionPhoto;
import org.kakaoshare.backend.domain.product.entity.QProductDetail;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public DetailDescriptionResponse findProductWithDetailsAndPhotos(Long productId) {
        return queryFactory
                .select(Projections.bean(DetailDescriptionResponse.class,
                        QProduct.product.productId.as("productId"),
                        QProduct.product.name,
                        QProduct.product.price,
                        QProduct.product.type,
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
}
