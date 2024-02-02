package org.kakaoshare.backend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TestEntity {
    @Id
    @GeneratedValue
    @Column(name="test-entity"+"_id")
    private Long id;
}
