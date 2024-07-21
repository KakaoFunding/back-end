package org.kakaoshare.backend.common;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DataJpaTest(showSql = false, includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@Import(value = {TestQueryDslConfig.class, TestJpaAuditingConfig.class})
@ActiveProfiles("test")
@PropertySource("classpath:/application-test.yml")
public @interface RepositoryTest {
}