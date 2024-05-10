package exerciseEndpoints;

import config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
@ComponentScan(basePackages = {"exerciseCRUD", "exerciseEndpoints", "config"})
public class StartApp {
    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
    }
}
