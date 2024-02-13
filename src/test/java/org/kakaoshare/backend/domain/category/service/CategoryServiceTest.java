package org.kakaoshare.backend.domain.category.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.kakaoshare.backend.domain.category.repository.CategoryRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @Test
    @DisplayName(value = "카테고리 계층적 구조화 테스트")
    void testCreateCategoryHierarchy() {
        // given
        List<Category> categories = createCategories();
        given(categoryRepository.findAllWithChildren())
                .willReturn(categories);
        
        // when
        Category root = categories.stream()
                .filter(category -> category.getName().equals("Root"))
                .findFirst().orElseThrow();
        
        CategoryDto rootDto = categoryService.createCategoryRoot();
        
        // then
        assertThat(rootDto.getCategoryId()).isEqualTo(CategoryDto.of(root).getCategoryId());
        assertThat(rootDto.getLevel()).isEqualTo(0);
        assertThat(rootDto.getCategoryName()).isEqualTo("Root");
    }
    
    List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        Category rootCategory = Category.of(0L, "Root", 0, null, new ArrayList<>());
        
        for (int i = 1; i <= 5; i++) {
            Category parentCategory = Category.of(0L, "Category " + i, 1, rootCategory, new ArrayList<>());
            categories.add(parentCategory);
            
            for (int j = 1; j <= 5; j++) {
                Category subCategory = Category.of(0L, "Category " + i + " - Subcategory " + j, 2, parentCategory, new ArrayList<>());
                parentCategory.getChildren().add(subCategory);
                categories.add(subCategory);
            }
            rootCategory.getChildren().add(parentCategory);
            categories.add(parentCategory);
        }
        categories.add(rootCategory);
        return categories;
    }
}