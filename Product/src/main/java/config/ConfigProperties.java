package config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@Component
@ConfigurationProperties("spring.datasource")
public class ConfigProperties {

        private String url;
        private String username;
        private String password;


//    private final DbProperties dbProperties = new DbProperties();
//
//    @Data
//    public static class DbProperties {
//        private String url;
//        private String username;
//        private String password;
//    }
}


