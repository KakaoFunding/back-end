package org.kakaoshare.backend.domain.category.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    
    @BeforeEach
    @Transactional
    void setUp() {
        List<Category> categories = new ArrayList<>();
        
        Category rootCategory = Category.of(0L, "Root", 0, null, new ArrayList<>());
        categories.add(rootCategory);
        
        for (int i = 1; i <= 5; i++) {
            Category parentCategory = Category.of(0L, "Category " + i, 1, rootCategory, new ArrayList<>());
            categories.add(parentCategory);
            
            for (int j = 1; j <= 5; j++) {
                Category subCategory = Category.of(0L, "Category " + i + " - Subcategory " + j, 2, parentCategory, new ArrayList<>());
                categories.add(subCategory);
            }
        }
        
        categoryRepository.saveAll(categories);
    }
    
    @Test
    @DisplayName(value = "카테고리 전체 조회")
    void testFindAllCategories() {
        List<Category> categories = categoryRepository.findAllWithChildren();
        
        assertThat(categories.stream().filter(c -> c.getName().equals("Root")).toList().size())
                .isOne();
        
        
        List<Category> parentCategories = categories.stream()
                .filter(c -> c.getLevel() == 1)
                .toList();
        
        assertThat(parentCategories.size()).isEqualTo(5);
        
        
        for (Category parent : parentCategories) {
            assertThat(parent.getChildren().size()).isEqualTo(5);
            for (int i = 1; i <= 5; i++) {
                String expectedSubcategoryName = parent.getName() + " - Subcategory " + i;
                assertThat(parent.getChildren().get(i-1).getName()).isEqualTo(expectedSubcategoryName);
                assertThat(parent.getChildren().get(i-1).getLevel()).isEqualTo(2);
            }
        }
        
        // 출력은 실제 데이터를 보기 위해 사용됩니다 (옵션)
        categories.forEach(System.out::println);
    }
}