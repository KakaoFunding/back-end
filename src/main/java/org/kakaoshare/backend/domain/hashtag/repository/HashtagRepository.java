package org.kakaoshare.backend.domain.hashtag.repository;

import org.kakaoshare.backend.domain.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
