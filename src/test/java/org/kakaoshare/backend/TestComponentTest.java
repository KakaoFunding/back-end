package org.kakaoshare.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles(value="test")
class TestComponentTest {
    @Autowired
    TestComponent testComponent;
    @Autowired
    TestEntityRepository testEntityRepository;
    @Test
    void test_ci() {
        // given
        assertThat(testComponent.testMethod()).isEqualTo(1L);
        // when
        
        // then
        
    }
    
    @Test
    void test_h2() {
        // given
        testEntityRepository.save(new TestEntity());
        // when
        assertThat(testEntityRepository.findAll().size()).isEqualTo(1);
        // then
        
    }
    
}