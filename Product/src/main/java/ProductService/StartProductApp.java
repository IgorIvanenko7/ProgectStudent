package ProductService;

import configPay.ConfigPropertiesProduct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({ConfigPropertiesProduct.class})
@ComponentScan(basePackages = {"exerciseCRUD", "ProductService", "configPay"})
public class StartProductApp {
    public static void main(String[] args) {
        SpringApplication.run(StartProductApp.class, args);
    }
}