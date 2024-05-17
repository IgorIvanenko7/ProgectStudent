package PayService.controller;

import PayService.dto.UserProductType;
import PayService.handleExeption.HandlerExeptionPay;
import PayService.service.PayService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService productService;

    // Получить продукт по id
    @GetMapping("/productId/{productId}")
    public JsonNode getPayProductId(
            @PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    // Получить все продукты
    @GetMapping("/productAll")
    public JsonNode getPayProductAll() {
        return productService.getProductAll();
    }

    // Получить продукты по userId
    @GetMapping("/productForUserId/{userId}")
    public JsonNode getPayProductForUserId(
            @PathVariable Long userId) {
        return productService.getProductForUserId(userId);
    }

    // Выполнить платеж пользователя
    @GetMapping("/runPay")
    public JsonNode payProductForUserId(
            @RequestParam Long userId,
            @RequestParam UserProductType typeProduct,
            @RequestParam BigDecimal sumPay) throws HandlerExeptionPay {
        return productService.runingPay(userId, typeProduct, sumPay);
    }
}
