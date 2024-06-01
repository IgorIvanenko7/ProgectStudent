package ProductService;

import ProductService.config.ConfigPropertiesLimit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({ConfigPropertiesLimit.class})
@ComponentScan(basePackages = {"ProductService", "ProductService"})
public class StartLimitApp {
    public static void main(String[] args) {
        SpringApplication.run(StartLimitApp.class, args);
    }
}
