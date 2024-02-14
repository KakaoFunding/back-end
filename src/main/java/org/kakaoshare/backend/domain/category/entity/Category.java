package org.kakaoshare.backend.domain.category.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;

import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "category_id")
    private Category parent;
    
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Category> children;
    
    private Category(String name, Category parent, List<Category> children) {
        this.name = name;
        this.parent = parent;
        this.children = children;
    }
    
    public static Category of(String name, Category parent, List<Category> children) {
        return new Category(name, parent, children);
    }
    
    @Override
    public String toString() {
        return "Category{" + '\n' +
                "categoryId=" + categoryId + '\n' +
                ", name='" + name + '\n' +
                ", parent id=" + parent.getCategoryId() + '\n' +
                ", children id=" +
                children.stream()
                        .map(Category::getCategoryId)
                        .toList() +
                '}';
    }
}
