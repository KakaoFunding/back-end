package org.kakaoshare.backend.domain.category.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.domain.category.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:/application-test.yml")
class CategoryServiceTest {
    public static final long PARENT_ID = 1L;
    public static final long CHILD_ID = 6L;
    @Autowired
    private CategoryService categoryService;
    
    
    @Test
    @DisplayName("카테고리는 계층간 연관이 명확해야한다.")
    void testCreateCategoryHierarchy() {
        StopWatch stopWatch=new StopWatch();
        stopWatch.start("find root");
        
        List<CategoryDto> categoryDtos = categoryService.getParentCategories();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        
        for (CategoryDto parentDto : categoryDtos) {
            assertThat(parentDto).isNotNull();
            assertThat(parentDto.getCategoryName()).isNotNull();
            assertThat(parentDto.getParentId()).isEqualTo(-1);
            assertThat(parentDto.getSubCategories()).isNotEmpty().hasSize(5);
            assertThat(parentDto.getLevel()).isEqualTo(1);
            parentDto.getSubCategories().forEach(childDto -> {
                assertThat(childDto.getParentId()).isEqualTo(parentDto.getCategoryId());
                assertThat(childDto.getLevel()).isEqualTo(2);
                assertCategoryDetails(childDto, parentDto.getCategoryId(), 0);
            });
        }
    }
    
    @Test
    @DisplayName("부모 카테고리는 자신의 부모 ID를 가지고 있지 않다")
    void testParentCategory() {
        CategoryDto category = categoryService.getParentCategory(PARENT_ID);
        assertThat(category).isNotNull();
        assertThat(category.getParentId()).isNotNull();
        assertThat(category.getLevel()).isEqualTo(1);
    }
    @Test
    @DisplayName("자식 카테고리는 자신의 자식 카테고리를 가지고 있지 않다")
    void testChildCategory() {
        CategoryDto category = categoryService.getChildCategory(PARENT_ID, CHILD_ID);
        
        assertThat(category).isNotNull();
        assertThat(category.getSubCategories()).isEmpty();
        assertThat(category.getParentId()).isEqualTo(PARENT_ID);
        assertThat(category.getLevel()).isEqualTo(2);
    }
    
    private void assertCategoryDetails(CategoryDto category, Long parentCategoryId, int expectedSubCategorySize) {
        assertThat(category.getCategoryId()).isNotEqualTo(parentCategoryId);
        assertThat(category.getSubCategories()).hasSize(expectedSubCategorySize);
    }
}
