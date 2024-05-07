package exerciseEndpoints.controller;

import exerciseEndpoints.dto.ProductDto;
import exerciseEndpoints.dto.RevisionContent;
import exerciseEndpoints.dto.SaveEntityUserProducts;
import exerciseEndpoints.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductUserController {

    private final ProductService productService;

    // Получить продукт по id
    @GetMapping("/productId/{productId}")
    public List<ProductDto> getProductId(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    // Получить все продукты
    @GetMapping("/productAll")
    public List<ProductDto> getProductAll() {
        return productService.getProduct(null);
    }

    // Получить продукты по userId
    @GetMapping("/productForUserId/{userId}")
    public List<ProductDto> getProductForUserId(@PathVariable Long userId) {
        return productService.getProductForUserId(userId);
    }

    // Добавление пользователя и его продуктов
    @PostMapping("/addEntity")
    public RevisionContent<SaveEntityUserProducts> saveProductForUserId(
            @RequestBody SaveEntityUserProducts requestEntity) {
        return productService.saveProductForUserId(requestEntity);
    }

    // Удаление пользователя
    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam String username) {
        productService.deleteUser(username);
    }
}
