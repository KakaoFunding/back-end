package org.kakaoshare.backend.domain.hashtag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;


@Entity
@Getter
public class Hashtag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashtagId;

    @Column(nullable = false)
    private String name;
    

}
