package PayService.controller;

import PayService.service.PayService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService productService;

    //-- Proxy Request
    // Получить продукт по id
    @GetMapping("/productId/{productId}")
    public JsonNode getProductId(
            @PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    // Получить все продукты
    @GetMapping("/productAll")
    public JsonNode getProductAll() {
        return productService.getProductAll();
    }

    // Получить продукты по userId
    @GetMapping("/productForUserId/{userId}")
    public JsonNode getProductForUserId(
            @PathVariable Long userId) {
        return productService.getProductForUserId(userId);
    }
    //-- Run Pay


}
