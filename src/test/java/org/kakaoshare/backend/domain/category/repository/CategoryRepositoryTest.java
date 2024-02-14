package org.kakaoshare.backend.domain.category.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CategoryRepositoryTest {
    public static final String ROOT_NAME = "Root";
    @Autowired
    private CategoryRepository categoryRepository;
    
    @BeforeEach
    @Transactional
    void setUp() {
        List<Category> categories = new ArrayList<>();
        
        Category rootCategory = Category.of(ROOT_NAME, null, new ArrayList<>());
        
        for (int i = 1; i <= 5; i++) {
            Category parentCategory = Category.of(
                    "Category " + i,
                    rootCategory,
                    new ArrayList<>());
            
            for (int j = 1; j <= 5; j++) {
                Category subCategory = Category.of(
                        "Category " + i + " - Subcategory " + j,
                        parentCategory
                        , new ArrayList<>());
                parentCategory.getChildren().add(subCategory);
                categories.add(subCategory);
            }
            rootCategory.getChildren().add(parentCategory);
            categories.add(parentCategory);
        }
        categories.add(rootCategory);
        categoryRepository.saveAll(categories);
    }
    
    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }
    
    @Test
    @DisplayName(value = "각 카테고리는 계층간 관게를 보장받는다")
    void testFindAllCategories() {
        // given
        List<Category> categories = categoryRepository.findAllWithChildren();
        
        List<Category> rootCategories = categories.stream()
                .filter(category -> Objects.isNull(category.getParent()))
                .toList();
        
        List<Category> parentCategories = categories.stream()
                .filter(category -> !Objects.isNull(category.getParent()))
                .filter(category -> category.getChildren().size() == 5)
                .toList();
        
        List<Category> childCategories = categories.stream()
                .filter(category -> category.getChildren().isEmpty())
                .toList();
        // root
        assertThat(rootCategories).size().isEqualTo(1);
        Category rootCategory = rootCategories.get(0);
        assertThat(rootCategory.getParent()).isNull();
        assertThat(rootCategory.getName()).isEqualTo(ROOT_NAME);
        
        // parent
        parentCategories
                .forEach(parent -> {
                    assertParentCategory(parent, rootCategory);
                });
        
        // child
        childCategories
                .forEach(CategoryRepositoryTest::assertChildCategory);
    }
    
    private static void assertParentCategory(Category parent, Category rootCategory) {
        assertThat(parent.getParent()).isNotNull();
        assertThat(parent.getParent()).isEqualTo(rootCategory);
        assertThat(parent.getChildren().size()).isEqualTo(5);
    }
    
    private static void assertChildCategory(Category child) {
        assertThat(child.getParent().getName()).contains("Category");
        assertThat(child.getParent().getName()).doesNotContain("SubCategory");
        assertThat(child.getChildren()).isEmpty();
    }
}