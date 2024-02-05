package org.kakaoshare.backend.domain.theme.repository;

import org.kakaoshare.backend.domain.theme.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
