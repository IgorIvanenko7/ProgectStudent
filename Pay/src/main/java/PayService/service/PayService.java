package PayService.service;

import PayService.dto.ProductDto;
import PayService.dto.UserProductType;
import PayService.handleExeption.HandlerExeptionPay;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import configRest.ConfigPropertiesPay;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class PayService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplateClient;
    private final RestTemplate restTemplateClientWthoutURL;
    private final ConfigPropertiesPay configPropertiesPay;

    public PayService(ObjectMapper objectMapper,
                      ConfigPropertiesPay configPropertiesPay,
                      @Qualifier("restTemplateClient") RestTemplate restTemplateClient,
                      @Qualifier("restTemplateClientWthoutURL") RestTemplate restTemplateClientWthoutURL) {
        this.objectMapper = objectMapper;
        this.configPropertiesPay = configPropertiesPay;
        this.restTemplateClient = restTemplateClient;
        this.restTemplateClientWthoutURL = restTemplateClientWthoutURL;
    }

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");

    public JsonNode getProduct(Long idUser) {
        return restTemplateClient.getForObject("/productId/" + idUser,
                JsonNode.class);
    }

    public JsonNode getProductAll() {
        return restTemplateClient.getForObject("/productAll",
                JsonNode.class);
    }

    public JsonNode getProductForUserId(Long userId) {
        return restTemplateClient.getForObject("/productForUserId/" + userId ,
                JsonNode.class);
    }

    //-------------------------------------------------------------------------------------
    public JsonNode runingPay(Long userId, UserProductType typeProduct, BigDecimal sumPay)  {

        JsonNode listProducts = restTemplateClient.getForObject("/productForUserId/" + userId, JsonNode.class);

        var respDtoList = Optional.ofNullable(listProducts.findValue("content"))
                .map(listJson -> {
                    List<ProductDto> productDtoList = objectMapper.convertValue(listJson, new TypeReference<>() {});
                    return productDtoList;})
                .orElseThrow(() -> new HandlerExeptionPay("Пользователь не найден", String.valueOf(userId)));

        var currentDto = respDtoList.stream()
                .filter(productDto -> productDto.getTypeProduct().equals(typeProduct))
                .findFirst()
                .orElseThrow(() -> new HandlerExeptionPay("Пользователь с ID:" + userId + " не имеет продукта типа ",
                        typeProduct.toString()));

        JsonNode result = executePay(userId, typeProduct, sumPay, currentDto);
        log.info("Response Product(/runPayProduct): {}", result);
        return result;
 }

    public JsonNode executePay(Long userId, UserProductType typeProduct,
                               BigDecimal sumPay, ProductDto productDto) {

        if (productDto.getBalans().compareTo(sumPay) == -1) {
           throw new HandlerExeptionPay(" Платеж не выполним -> недостаточно суммы. Пользователь ID:" + userId + ", продукт: ",
                    typeProduct.toString());
        }
        URI uriProductService = UriComponentsBuilder.fromUriString(configPropertiesPay.getConf()
                        .getBaseurl() + "/runPayProduct")
                .queryParam("userId", userId)
                .queryParam("typeProduct", typeProduct)
                .queryParam("sumPay", sumPay)
                .build()
                .toUri();
        return restTemplateClientWthoutURL.getForObject(uriProductService, JsonNode.class);
    }

}
