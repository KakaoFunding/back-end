package org.kakaoshare.backend.domain.category.repository;

import org.kakaoshare.backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
