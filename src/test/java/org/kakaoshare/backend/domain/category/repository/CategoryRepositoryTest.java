package org.kakaoshare.backend.domain.category.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kakaoshare.backend.common.CustomDataJpaTest;
import org.kakaoshare.backend.domain.category.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@CustomDataJpaTest
class CategoryRepositoryTest {
    public static final String ROOT_NAME = "Root";
    public static final String SUBCATEGORY = "Subcategory";
    
    StopWatch stopWatch=new StopWatch();
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Test
    @DisplayName(value = "각 카테고리는 계층간 관게를 보장받는다")
    void testFindAllCategories() {
        // given
        List<Category> categories = categoryRepository.findAll();
        
        Category rootCategory = categories.stream()
                .filter(category -> Objects.isNull(category.getParent()))
                .findFirst()
                .orElseThrow();
        
        List<Category> parentCategories = categories.stream()
                .filter(category -> !Objects.isNull(category.getParent()))
                .filter(category -> category.getChildren().size() == 5)
                .toList();
        
        List<Category> childCategories = categories.stream()
                .filter(category -> category.getChildren().isEmpty())
                .toList();
        // root
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
    
    @Test
    @DisplayName("단일 ID를 통해 조회한 카테고리들은 부모 카테고리다")
    void testFindByCategoryId() {
        // given
        List<Category> parentCategories = getParentCategories();
        
        for (Category parentCategory : parentCategories) {
            Long categoryId = parentCategory.getCategoryId();
            Category parent = categoryRepository.findById(categoryId).orElseThrow();
            
            assertThat(parentCategory.getCategoryId()).isEqualTo(parent.getCategoryId());
            assertThat(parent.getName()).doesNotContain(SUBCATEGORY);
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
            Category category
                    = categoryRepository.findById(categoryId).orElseThrow();
            
            // then
            assertThat(category.getParent()).isNotNull();
            assertThat(category.getChildren()).isEmpty();
            assertThat(category.getName()).contains(SUBCATEGORY);
        }
    }
    
    @Test
    @DisplayName("루트 카테고리는 부모 카테고리는 없지만 자식 카테고리가 있다")
    void testRoot() {
        stopWatch.start("root");
        Category root = categoryRepository.findByCategoryIdWithParentAndChildren(1L).orElseThrow();
        stopWatch.stop();
        assertThat(root).isNotNull();
        assertThat(root.getParent()).isNull();
        assertThat(root.getChildren()).isNotEmpty();
        assertThat(root.getChildren().size()).isEqualTo(5);
        System.out.println(stopWatch.prettyPrint());
    }
    
    @Test
    @DisplayName("루트 카테고리의 자식 카테고리는 부모 카테고리와 자식 카테고리가 있다")
    void testFirstGen() {
        stopWatch.start("first gen");
        Category root = categoryRepository.findByCategoryIdWithParentAndChildren(2L).orElseThrow();
        stopWatch.stop();
        assertThat(root).isNotNull();
        assertThat(root.getParent()).isNotNull();
        assertThat(root.getChildren()).isNotEmpty();
        assertThat(root.getChildren().size()).isEqualTo(5);
        System.out.println(stopWatch.prettyPrint());
    }
    
    @Test
    @DisplayName("말단 카테고리는 부모 카테고리는 있지만 자식 카테고리는 없다")
    void testSecondGen() {
        stopWatch.start("second gen");
        Category root = categoryRepository.findByCategoryIdWithParentAndChildren(7L).orElseThrow();
        stopWatch.stop();
        assertThat(root).isNotNull();
        assertThat(root.getParent()).isNotNull();
        assertThat(root.getChildren()).isEmpty();
        System.out.println(stopWatch.prettyPrint());
    }
    
    private List<Category> getParentCategories() {// 부모 카테고리만 반환
        return categoryRepository.findAll().stream()
                .filter(this::haveNoParent)
                .toList()
                .stream()
                .filter(category -> category.getParent().getName().equals(ROOT_NAME))
                .toList();
    }
    
    private List<Category> getChildCategories() {// 자식 카테고리만 반환
        return categoryRepository.findAll().stream()
                .filter(category -> List.of(category.getChildren()).isEmpty())
                .toList();
    }
    
    
    private boolean haveNoParent(Category category) {
        return Objects.nonNull(category.getParent());
    }
    
    
    private static void assertParentCategory(Category parent, Category rootCategory) {
        assertThat(parent.getParent()).isNotNull();
        assertThat(parent.getParent()).isEqualTo(rootCategory);
        assertThat(parent.getChildren().size()).isEqualTo(5);
    }
    
    private static void assertChildCategory(Category child) {
        assertThat(child.getParent().getName()).contains("Category");
        assertThat(child.getParent().getName()).doesNotContain(SUBCATEGORY);
        assertThat(child.getChildren()).isEmpty();
    }
}