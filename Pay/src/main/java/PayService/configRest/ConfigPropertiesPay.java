package PayService.configRest;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "rest")
public class ConfigPropertiesPay {

    private RestProperties conf;

    @Data
    @RequiredArgsConstructor
    public static class RestProperties {
            private String baseurl;
            private Duration readTimeout;
            private Duration connectTimeout;
    }
}
