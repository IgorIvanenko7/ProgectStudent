package PayService.service;

import com.fasterxml.jackson.databind.JsonNode;
import configRest.ConfigPropertiesPay;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
@RequiredArgsConstructor
public class PayService {

    private final ConfigPropertiesPay configProperties;
    private final RestTemplate restTemplate;

    public JsonNode getProduct(Long idUser) {
        return restTemplate.getForObject("/productId/" + idUser,
                JsonNode.class);
    }

    public JsonNode getProductAll() {
        return restTemplate.getForObject("/productAll",
                JsonNode.class);
    }

    public JsonNode getProductForUserId(Long userId) {
        return restTemplate.getForObject("/productForUserId/" + userId ,
                JsonNode.class);
    }

}
