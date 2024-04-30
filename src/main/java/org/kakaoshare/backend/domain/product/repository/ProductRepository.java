package org.kakaoshare.backend.domain.product.repository;

import org.kakaoshare.backend.domain.product.dto.ProductDto;
import org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.repository.query.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    @Query("SELECT NEW org.kakaoshare.backend.domain.product.dto.ProductSummaryResponse(p.brandName, p.name, p.price) " +
            "FROM Product p " +
            "WHERE p.productId =:productId")
    ProductSummaryResponse findAllProductSummaryById(final Long productId);

    @Query("SELECT NEW org.kakaoshare.backend.domain.product.dto.ProductDto(p.productId, p.name, p.photo, p.price) " +
            "FROM Product p " +
            "WHERE p.productId =:productId")
    Optional<ProductDto> findProductDtoById(final Long productId);
}
