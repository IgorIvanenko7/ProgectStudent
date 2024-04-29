package exerciseCRUD.DAO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"exerciseCRUD"})
public class ConfigClass {

    final static int SIZE_POLL = 7;
    final static String URL_DB_CONNECT = "jdbc:postgresql://localhost:8001/DB_NEW",
                        USERNAME = "postgres",
                        PASSWORD = "postgres";

    @Bean
    public DataSource dataSourceDbPostgresSQL() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL_DB_CONNECT);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(SIZE_POLL);
        return new HikariDataSource(config);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplatePostgresSQL(@Qualifier("dataSourceDbPostgresSQL") DataSource dataSource) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        System.out.println("### Create NamedParameterJdbcTemplate for DB ###");
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
