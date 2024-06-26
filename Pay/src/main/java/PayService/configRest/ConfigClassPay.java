package PayService.configRest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"PayService"})
public class ConfigClassPay {

    @Bean
    @Primary
    public ObjectMapper getConfigObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }

    @Bean
    public RestTemplate restTemplateClient(ConfigPropertiesPay configProperties) {
        return new RestTemplateBuilder()
                .rootUri(configProperties.getConf().getBaseurl())
                .setConnectTimeout(configProperties.getConf().getConnectTimeout())
                .setReadTimeout(configProperties.getConf().getReadTimeout())
                .build();
    }

    @Bean
    public RestTemplate restTemplateClientWthoutURL(ConfigPropertiesPay configProperties) {
        return new RestTemplateBuilder()
                .setConnectTimeout(configProperties.getConf().getConnectTimeout())
                .setReadTimeout(configProperties.getConf().getReadTimeout())
                .build();
    }
}
