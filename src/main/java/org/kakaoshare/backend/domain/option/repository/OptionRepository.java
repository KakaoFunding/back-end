package org.kakaoshare.backend.domain.option.repository;

import org.kakaoshare.backend.domain.option.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OptionRepository extends JpaRepository<Option, Long> {
    @Query("SELECT od.option " +
            "FROM OptionDetail od " +
            "LEFT JOIN od.option " +
            "WHERE od.optionDetailId IN :optionDetailIds")
    List<Option> findByOptionDetailIds(final List<Long> optionDetailIds);
}
