package org.kakaoshare.backend.domain.category.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:/application-test.yml")
class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;
    
    
    @Test
    @DisplayName("카테고리는 계층간 연관이 명확해야한다.")
    void testCreateCategoryHierarchy() {
        StopWatch stopWatch=new StopWatch();
        stopWatch.start("find root");
        
        CategoryDto rootCategory = categoryService.getRootCategory();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        
        assertThat(rootCategory).isNotNull();
        assertThat(rootCategory.getCategoryName()).isNotNull();
        assertThat(rootCategory.getParentId()).isEqualTo(-1);
        assertThat(rootCategory.getSubCategories()).isNotEmpty().hasSize(5);
        rootCategory.getSubCategories().forEach(parent -> {
            assertThat(parent.getParentId()).isEqualTo(rootCategory.getCategoryId());
            assertCategoryDetails(parent, rootCategory.getCategoryId(), 5);
            parent.getSubCategories().forEach(child -> {
                assertThat(child.getParentId()).isEqualTo(parent.getCategoryId());
                assertCategoryDetails(child, parent.getCategoryId(), 0);
            });
        });
    }
    
    @ValueSource(longs = {1L,2L,7L})
    @ParameterizedTest
    @DisplayName("카테고리 단일조회")
    void testSpecificCategory(long id) {
        CategoryDto category = categoryService.getSpecificCategory(id);
        assertThat(category).isNotNull();
        if(id!= CategoryDto.ROOT_ID){
            assertThat(category.getParentId()).isNotNull();
        }
        else {
            assertThat(category.getParentId()).isNull();
        }
    }
    
    private void assertCategoryDetails(CategoryDto category, Long parentCategoryId, int expectedSubCategorySize) {
        assertThat(category.getCategoryId()).isNotEqualTo(parentCategoryId);
        assertThat(category.getSubCategories()).hasSize(expectedSubCategorySize);
    }
}
