package org.kakaoshare.backend.domain.option.repository;

import org.kakaoshare.backend.domain.option.entity.OptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OptionDetailRepository extends JpaRepository<OptionDetail, Long> {
    @Query("SELECT od.name FROM OptionDetail od WHERE od.optionDetailId IN :ids")
    List<String> findNamesByIds(final List<Long> ids);
}
