package org.kakaoshare.backend.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kakaoshare.backend.domain.brand.dto.TabType;
import org.kakaoshare.backend.domain.category.entity.Category;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryDto {
    private final Long categoryId;
    private final String categoryName;
    private final Long parentId;
    private final List<CategoryDto> subCategories = new ArrayList<>();
    private final TabType defaultTab;
    
    public CategoryDto(Long categoryId, String categoryName, Long parentId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.defaultTab = TabType.BRAND;//브랜드를 조회하는것이 화면 로딩과정에서 쿼리를 최소화 가능해보임
    }
    
    public static CategoryDto of(Category category) {
        return new CategoryDto(
                category.getCategoryId(),
                category.getName(),
                category.getParent().getCategoryId());
    }
    
    @Override
    public String toString() {
        return "CategoryDto{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", subCategory id=" +
                subCategories.stream()
                        .map(CategoryDto::getCategoryId)
                        .toList() +
                ", defaultTab=" + defaultTab +
                '}';
    }
}
