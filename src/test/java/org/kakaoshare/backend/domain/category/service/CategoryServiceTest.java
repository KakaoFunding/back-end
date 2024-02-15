package org.kakaoshare.backend.domain.category.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.domain.brand.dto.TabType;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CategoryServiceTest {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @BeforeEach
    @Transactional
    void setUp() {
        List<Category> categories = createTestCategories();
        categoryRepository.saveAll(categories);
    }
    
    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }
    
    @Test
    @DisplayName("카테고리는 계층간 연관이 명확해야한다.")
    void testCreateCategoryHierarchy() {
        CategoryDto root = categoryService.createCategoryRoot();
        
        assertThat(root.getSubCategories()).isNotNull().hasSize(5);
        root.getSubCategories().forEach(parent -> {
            assertCategoryDetails(parent, root.getCategoryId(), 5);
            parent.getSubCategories().forEach(child -> assertCategoryDetails(child, parent.getCategoryId(), 0));
        });
    }
    
    private List<Category> createTestCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Category parentCategory = Category.builder()
                    .name("Category " + i)
                    .parent(null)
                    .children(new ArrayList<>())
                    .build();
            for (int j = 1; j <= 5; j++) {
                Category subCategory = Category.builder()
                        .name("Category " + i + " - Subcategory " + j)
                        .parent(parentCategory)
                        .children(new ArrayList<>())
                        .build();
                parentCategory.getChildren().add(subCategory);
                categories.add(subCategory);
            }
            categories.add(parentCategory);
        }
        return categories;
    }
    
    private void assertCategoryDetails(CategoryDto category, Long parentCategoryId, int expectedSubCategorySize) {
        assertThat(category.getCategoryId()).isNotEqualTo(parentCategoryId);
        assertThat(category.getSubCategories()).hasSize(expectedSubCategorySize);
        assertThat(category.getDefaultTab()).isEqualTo(TabType.BRAND);
    }
}
