package config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = {"exerciseCRUD"})
public class ConfigClass {

    @Autowired
    private Environment environment;

    final static int SIZE_POLL = 7;

    @Bean
    public DataSource dataSourceDbStudentPostgresSQL() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getRequiredProperty("spring.datasource.url"));
        config.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        config.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        config.setMaximumPoolSize(SIZE_POLL);
        return new HikariDataSource(config);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplateStudentPostgresSQL(@Qualifier("dataSourceDbStudentPostgresSQL") DataSource dataSource) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        System.out.println("### Create NamedParameterJdbcTemplate for DB PostgresSQL(Student) ###");
        return jdbcTemplate;
    }

    @Bean
    @Primary
    public ObjectMapper getConfigObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }

}
