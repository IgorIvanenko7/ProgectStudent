package exerciseEndpoints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"exerciseCRUD", "exerciseEndpoints", "config"})
public class StartApp {

    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
    }
}
