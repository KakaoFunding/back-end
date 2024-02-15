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
    public static final String SUBCATEGORY = "Subcategory";
    @Autowired
    private CategoryRepository categoryRepository;
    
    @BeforeEach
    @Transactional
    void setUp() {
        List<Category> categories = new ArrayList<>();
        
        Category rootCategory = Category.builder()
                .name(ROOT_NAME)
                .parent(null)
                .children(new ArrayList<>())
                .build();
        
        for (int i = 1; i <= 5; i++) {
            Category parentCategory = Category.builder()
                    .name("Category " + i)
                    .parent(rootCategory)
                    .children(new ArrayList<>())
                    .build();
            
            for (int j = 1; j <= 5; j++) {
                Category subCategory = Category.builder()
                        .name("Category " + i + " - " + SUBCATEGORY + " " + j)
                        .parent(parentCategory)
                        .children(new ArrayList<>())
                        .build();
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
    
    @Test
    @DisplayName("단일 ID를 통해 조회한 카테고리들은 부모 카테고리다")
    void testFindByCategoryId() {
        // given
        List<Category> parentCategories = getParentCategories();
        
        for (Category parentCategory : parentCategories) {
            Long categoryId = parentCategory.getCategoryId();
            Category parent = categoryRepository.findByCategoryIdWithChildren(categoryId).orElseThrow();
            
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
                    = categoryRepository.findByCategoryIdWithChildren(categoryId).orElseThrow();
            
            // then
            assertThat(category.getParent()).isNotNull();
            assertThat(category.getChildren()).isEmpty();
            assertThat(category.getName()).contains(SUBCATEGORY);
        }
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