package io.github.lasyard.spring.data.jdbc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@TestConfiguration
@EnableJdbcRepositories
public class JdbcConfiguration extends AbstractJdbcConfiguration {
}
