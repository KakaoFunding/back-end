package org.kakaoshare.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

@EntityScan(basePackages = {"org.kakaoshare.backend.*"})
@SpringBootApplication
public class FlywayApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(FlywayApplication.class, args);
        configurableApplicationContext.close();
    }
}
