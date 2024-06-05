package org.kakaoshare.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.flyway.baseline-on-migrate=false"
})
class BackEndApplicationTests {

	@Test
	void contextLoads() {
	}

}
