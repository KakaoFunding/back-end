package org.kakaoshare.backend.common.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.brand.entity.Brand;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

@Component
@Profile("dev")
@EnableJpaAuditing
@RequiredArgsConstructor
public class DatabaseInitializer {
    private final CategoryRepository categoryRepository;
    
    @PostConstruct
    @Transactional
    public void initDatabase() {
        createParentCategories(5);
    }
    
    private void createParentCategories(final int count) {
        for (int i = 0; i < count; i++) {
            Category parent =Category
                    .builder()
                    .name("Parent Category " + (i+1))
                    .children(new ArrayList<>())
                    .build();
            createChildCategories(parent, 5);
            categoryRepository.save(parent);
        }
    }
    
    private void createChildCategories(Category parent,final int count) {
        for (int i = 0; i < count; i++) {
            Category child = Category.builder()
                    .name("Child Category " + (i + 1))
                    .parent(parent)
                    .children(new ArrayList<>())
                    .brands(new ArrayList<>())
                    .build();
            parent.getChildren().add(child);
            createBrands(child, 5);
        }
    }
    
    private void createBrands(Category category,final int count) {
        for (int i = 0; i < count; i++) {
            Brand brand = Brand
                    .builder()
                    .name(category.getName()+" Brand " + (i + 1))
                    .category(category)
                    .products(new ArrayList<>())
                    .build();
            createProducts(brand, 10);
            category.getBrands().add(brand);
        }
    }
    
    private void createProducts(Brand brand,final int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int price = 1000 + (random.nextInt(200) * 100);
            Product product = Product.builder()
                    .name(brand.getName()+" Product " + (i + 1))
                    .price(BigDecimal.valueOf(price))
                    .brand(brand)
                    .wishes(new ArrayList<>())
                    .type("type"+(i+1))
                    .build();
            brand.getProducts().add(product);
        }
    }
}