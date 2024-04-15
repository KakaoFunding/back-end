package org.kakaoshare.backend.common.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"prod","dev"})
@Configuration
public class FlywayConfig {
    @Bean
    public FlywayMigrationStrategy repairMigrationStrategy(){
        return flyway ->{
            flyway.repair();
            flyway.migrate();
            flyway.validate();
            flyway.info();
        };
    }
}
