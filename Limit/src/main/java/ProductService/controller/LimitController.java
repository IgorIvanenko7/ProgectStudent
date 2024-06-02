package ProductService.controller;

import ProductService.dto.*;
import ProductService.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LimitController {

    private final LimitService limitService;

    // Изменение Базового лимита пользователя(клиента), не чаше 1 раза в 2 мес.
    @PutMapping("/changeLimit")
    public RevisionResponseLimit<LimitDto> changeLimitUser(
            @RequestParam Long userId,
            @RequestParam BigDecimal sumNewBaseLimit) {
        return limitService.changeLimitUser(userId, sumNewBaseLimit);
    }

    /* Проведение платежа по userId, на сумму sumPay
     * В случае превышения установленного лимита -> откат транзакционного блока
     * Каждая операции платежа(списания) сохраняется в сущностях с уникальным timestamp
     * Получение уникального timestamp реализована в классе -> DateTimeUtils.uniqueTimestampMicros(),
     */
    @GetMapping("/runPayment")
    public RevisionResponseLimit<PaymentResponseDto> paymentLimit(
            @RequestParam Long userId,
            @RequestParam BigDecimal sumPay) {
        return limitService.runPayment(userId, sumPay);
    }
    //-------------------------------------------------------------------------

    // Получить все платежи по userId
    @GetMapping("/paymentsForUserId/{userId}")
    public RevisionResponseLimit<List<PaymentDto>> getProductForUserId(
            @PathVariable Long userId) {
        return limitService.getPaymensForUserId(userId);
    }

}
