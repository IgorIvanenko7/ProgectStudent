package configPay;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = {"exerciseCRUD"})
public class ConfigClass {

    @Bean
    public DataSource dataSourceDbStudentPostgresSQL(ConfigPropertiesProduct configProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(configProperties.getDatasource().getUrl());
        config.setUsername(configProperties.getDatasource().getUsername());
        config.setPassword(configProperties.getDatasource().getPassword());
        config.setMaximumPoolSize(configProperties.getDatasource().getSize_pool());
        return new HikariDataSource(config);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplateStudentPostgresSQL(DataSource dataSource) {
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
