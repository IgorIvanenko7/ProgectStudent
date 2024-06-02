package ProductService;

import ProductService.config.ConfigPropertiesLimit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties({ConfigPropertiesLimit.class})
@ComponentScan(basePackages = {"ProductService"})
public class StartLimitApp {
    public static void main(String[] args) {
        SpringApplication.run(StartLimitApp.class, args);
    }
}
