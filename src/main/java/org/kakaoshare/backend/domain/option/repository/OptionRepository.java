package org.kakaoshare.backend.domain.option.repository;

import org.kakaoshare.backend.domain.option.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OptionRepository extends JpaRepository<Option, Long> {
}
