package ProductService.controller;

import ProductService.dto.*;
import ProductService.service.LimitService;
import ProductService.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LimitController {

    private final LimitService limitService;


    /* Проведение платежа по userId, на сумму sumPay
       Вызывается ендпоинтом (/runPay) из Pay сервиса. Вся валидация
       реализвана в Pay сервисе
     */
    @GetMapping("/runPayment")
    public RevisionResponseLimit<PaymentDto> paymentLimitJPA(
            @RequestParam Long userId,
            @RequestParam BigDecimal sumPay) {
        return limitService.runPaymentJPA(userId, sumPay);
    }
}
