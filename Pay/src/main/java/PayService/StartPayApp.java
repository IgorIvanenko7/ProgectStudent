package PayService;

import PayService.configRest.ConfigPropertiesPay;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(ConfigPropertiesPay.class)
@ComponentScan(basePackages = {"PayService", "PayService"})
public class StartPayApp {
    public static void main(String[] args) {SpringApplication.run(StartPayApp.class, args);}
}
