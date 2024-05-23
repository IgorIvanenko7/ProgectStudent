package ProductService.controller;

import ProductService.dto.ProductDto;
import ProductService.dto.RevisionResponse;
import ProductService.dto.EntityUserProducts;
import ProductService.dto.User;
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
        return productService.getProductJPA(productId);
    }

    // Получить все продукты
    @GetMapping("/productAll")
    public RevisionResponse<List<ProductDto>> getProductAll() {
        return productService.getProductJPA(null);
    }

    // Получить продукты по userId
    @GetMapping("/productForUserId/{userId}")
    public RevisionResponse<List<ProductDto>> getProductForUserId(
            @PathVariable Long userId) {
        return productService.getProductForUserIdJPA(userId);
    }

    // Добавление пользователя и его продуктов
    @PostMapping("/addEntity")
    public RevisionResponse<EntityUserProducts> saveProductForUserId(
            @RequestBody EntityUserProducts requestEntity) {
        return productService.saveProductForUserIdJPA(requestEntity);
    }

    // Удаление пользователя
    @DeleteMapping("/deleteUser")
    public ResponseEntity<User> deleteUserJPA(
            @RequestParam String username) {
        return productService.deleteUserJPA(username);
    }

    /* Платеж по userId, typeProduct, на сумму sumPay
       Вызывается ендпоинтом (/runPay) из Pay сервиса. Вся валидация
       реализвана в Pay сервисе
     */
    @GetMapping("/runPayProduct")
    public RevisionResponse<List<ProductDto>> payProductForUserIdJPA(
            @RequestParam Long userId,
            @RequestParam String typeProduct,
            @RequestParam BigDecimal sumPay) {
        return productService.payProductJPA(userId, typeProduct, sumPay);
    }
}
