package org.kakaoshare.backend.domain.product.repository;

import org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.query.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    @Query("SELECT SUM(p.price) FROM Product p " +
            "WHERE p.productId IN :productIds")
    Long findTotalPriceByIds(List<Long> productIds);

    @Query("SELECT NEW org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse(p.brandName, p.name, p.price) " +
            "FROM Product p " +
            "WHERE p.productId =:productId")
    ProductSummaryResponse findAllProductSummaryById(final Long productId);
}
