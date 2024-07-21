package org.kakaoshare.backend.domain.theme.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;


@Entity
@Getter
public class Theme extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long themeId;

    @Column(nullable = false)
    private String photo;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

}
