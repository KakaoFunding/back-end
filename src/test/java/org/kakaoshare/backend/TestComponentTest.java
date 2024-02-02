package org.kakaoshare.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TestComponentTest {
    @Autowired
    TestComponent testComponent;
    @Test
    void test_ci() {
        // given
        assertThat(testComponent.testMethod()).isEqualTo(1L);
        // when
        
        // then
        
    }
    
}