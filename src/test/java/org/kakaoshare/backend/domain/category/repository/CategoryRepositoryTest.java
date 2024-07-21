package org.kakaoshare.backend.domain.category.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kakaoshare.backend.common.RepositoryTest;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class CategoryRepositoryTest {
    public static final long PARENT_ID = 1L;
    public static final long CHILD_ID = 7L;
    
    StopWatch stopWatch=new StopWatch();
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Test
    @DisplayName(value = "각 카테고리는 계층간 관게를 보장받는다")
    void testFindAllCategories() {
        // given
        List<Category> categories = categoryRepository.findAll();
        
        List<Category> parentCategories = categories.stream()
                .filter(category -> Objects.isNull(category.getParent()))
                .filter(category -> category.getChildren().size() == 5)
                .toList();
        
        List<Category> childCategories = categories.stream()
                .filter(category -> category.getChildren().isEmpty())
                .toList();
        
        // parent
        parentCategories
                .forEach(CategoryRepositoryTest::assertParentCategory);
        
        // child
        childCategories
                .forEach(CategoryRepositoryTest::assertChildCategory);
    }
    
    @Test
    @DisplayName("단일 ID를 통해 조회한 카테고리들은 부모 카테고리다")
    void testFindByCategoryId() {
        // given
        List<Category> parentCategories = getParentCategories();
        
        for (Category parentCategory : parentCategories) {
            Long categoryId = parentCategory.getCategoryId();
            Category parent = categoryRepository.findById(categoryId).orElseThrow();
            
            assertThat(parentCategory.getCategoryId()).isEqualTo(parent.getCategoryId());
            assertThat(parent.getChildren().size()).isEqualTo(5);
        }
    }
    
    @Test
    @DisplayName("자식 카테고리도 ID를 통해 조회 가능하다")
    void testFindByCategoryIdAndSubCategoryId() {
        // given
        List<Category> childCategories = getChildCategories();
        
        for (Category childCategory : childCategories) {
            // when
            Long categoryId = childCategory.getParent().getCategoryId();
            Category parent
                    = categoryRepository.findById(categoryId).orElseThrow();
            
            // then
            assertThat(parent).isNotNull();
            assertThat(parent.getChildren().size()).isEqualTo(5);
        }
    }
    
    @Test
    @DisplayName("루트 카테고리의 자식 카테고리는 부모 카테고리와 자식 카테고리가 있다")
    void testFirstGen() {
        stopWatch.start("first gen");
        Category parent = categoryRepository.findParentCategoryWithChildren(PARENT_ID).orElseThrow();
        stopWatch.stop();
        assertThat(parent).isNotNull();
        assertThat(parent.getParent()).isNull();
        assertThat(parent.getChildren()).isNotEmpty();
        assertThat(parent.getChildren().size()).isEqualTo(5);
        System.out.println(stopWatch.prettyPrint());
    }
    
    @Test
    @DisplayName("말단 카테고리는 부모 카테고리는 있지만 자식 카테고리는 없다")
    void testSecondGen() {
        stopWatch.start("second gen");
        Category child = categoryRepository.findChildCategoryWithParentCheck(PARENT_ID,CHILD_ID).orElseThrow();
        stopWatch.stop();
        assertThat(child).isNotNull();
        assertThat(child.getParent()).isNotNull();
        assertThat(child.getChildren()).isEmpty();
        System.out.println(stopWatch.prettyPrint());
    }
    
    @Test
    @DisplayName("부모 카테고리를 통해 브랜드 갯수 조회")
    void testCountBrand() {
        // given
        Long brandCount = categoryRepository.countBrand(PARENT_ID);
        // then
        assertThat(brandCount).isEqualTo(50);
    }
    @ParameterizedTest
    @ValueSource(longs = {1,2,3,4,5})
    @DisplayName("부모 카테고리를 통해 상품 갯수 조회")
    void testCountProduct(Long id) {
        // given
        Long productCount = categoryRepository.countProduct(id);
        // then
        assertThat(productCount).isEqualTo(2000);
    }
    
    private List<Category> getParentCategories() {// 부모 카테고리만 반환
        return categoryRepository.findAll().stream()
                .filter(category ->Objects.isNull(category.getParent()))
                .toList();
    }
    
    private List<Category> getChildCategories() {// 자식 카테고리만 반환
        return categoryRepository.findAll().stream()
                .filter(category -> category.getChildren().isEmpty())
                .toList();
    }
    
    
    private static void assertParentCategory(Category parent) {
        assertThat(parent.getParent()).isNull();
        assertThat(parent.getChildren().size()).isEqualTo(5);
    }
    
    private static void assertChildCategory(Category child) {
        assertThat(child.getParent()).isNotNull();
        assertThat(child.getChildren()).isEmpty();
    }
}