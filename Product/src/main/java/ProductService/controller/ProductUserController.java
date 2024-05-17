package ProductService.controller;

import ProductService.dto.ProductDto;
import ProductService.dto.RevisionResponse;
import ProductService.dto.EntityUserProducts;
import ProductService.dto.UserProductType;
import ProductService.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductUserController {

    private final ProductService productService;

    // Получить продукт по id
    @GetMapping("/productId/{productId}")
    public RevisionResponse<List<ProductDto>> getProductId(
            @PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    // Получить все продукты
    @GetMapping("/productAll")
    public RevisionResponse<List<ProductDto>> getProductAll() {
        return productService.getProduct(null);
    }

    // Получить продукты по userId
    @GetMapping("/productForUserId/{userId}")
    public RevisionResponse<List<ProductDto>> getProductForUserId(
            @PathVariable Long userId) {
        return productService.getProductForUserId(userId);
    }

    // Добавление пользователя и его продуктов
    @PostMapping("/addEntity")
    public RevisionResponse<EntityUserProducts> saveProductForUserId(
            @RequestBody EntityUserProducts requestEntity) {
        return productService.saveProductForUserId(requestEntity);
    }

    // Удаление пользователя
    @DeleteMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestParam String username) {
        productService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    /* Платеж по userId, typeProduct, на сумму sumPay
       Вызывается ендпоинтом (/runPay) из Pay сервиса. Вся валидация
       реализвана в Pay сервисе
     */
    @GetMapping("/runPayProduct")
    public RevisionResponse<List<ProductDto>> payProductForUserId(
            @RequestParam Long userId,
            @RequestParam UserProductType typeProduct,
            @RequestParam BigDecimal sumPay) {
        return productService.payProduct(userId, typeProduct, sumPay);
    }
}
