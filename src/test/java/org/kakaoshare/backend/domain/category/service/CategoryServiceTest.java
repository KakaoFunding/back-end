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
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    public static final long PARENT_ID = 1L;
    public static final long CHILD_ID = 6L;
    @InjectMocks
    CategoryService categoryService;
    @Mock
    CategoryRepository categoryRepository;
    
    
    @Test
    @DisplayName("카테고리는 계층간 연관이 명확해야한다.")
    void testCreateCategoryHierarchy() {
        StopWatch stopWatch = new StopWatch();
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
        // given
        Category parent = getParent();
        parent.getChildren().addAll(mockChildren(parent));
        given(categoryRepository.findParentCategoryWithChildren(PARENT_ID)).willReturn(Optional.of(parent));
        
        
        CategoryDto category = categoryService.getParentCategory(PARENT_ID);
        assertThat(category).isNotNull();
        assertThat(category.getParentId()).isNull();
        assertThat(category.getLevel()).isEqualTo(1);
    }
    
    private static Category getParent() {
        return Category.builder()
                .categoryId(PARENT_ID)
                .parent(null)
                .children(new ArrayList<>())
                .build();
    }
    
    @Test
    @DisplayName("자식 카테고리는 자신의 자식 카테고리를 가지고 있지 않다")
    void testChildCategory() {
        Category child=mockChild();
        given(categoryRepository.findChildCategoryWithParentCheck(PARENT_ID,CHILD_ID)).willReturn(Optional.of(child));
        CategoryDto category = categoryService.getChildCategory(PARENT_ID, CHILD_ID);
        
        assertThat(category).isNotNull();
        assertThat(category.getSubCategories()).isEmpty();
        assertThat(category.getParentId()).isEqualTo(PARENT_ID);
        assertThat(category.getLevel()).isEqualTo(2);
    }
    
    private Category mockChild() {
        return Category
                .builder()
                .categoryId(CHILD_ID)
                .name("child category"+CHILD_ID)
                .parent(getParent())
                .children(new ArrayList<>())
                .build();
    }
    
    private void assertCategoryDetails(CategoryDto category, Long parentCategoryId, int expectedSubCategorySize) {
        assertThat(category.getCategoryId()).isNotEqualTo(parentCategoryId);
        assertThat(category.getSubCategories()).hasSize(expectedSubCategorySize);
    }
    
    private List<Category> mockChildren(final Category parent) {
        return IntStream
                .range(0, 5)
                .mapToObj(i -> Category
                        .builder()
                        .categoryId((long) (i + 5))
                        .name("child category"+i+1)
                        .parent(parent)
                        .children(new ArrayList<>())
                        .build())
                .toList();
    }
}
