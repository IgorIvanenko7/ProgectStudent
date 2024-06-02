package ProductService.controller;

import ProductService.dto.*;
import ProductService.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class LimitController {

    private final LimitService limitService;

    // Изменение лимита пользователя(клиента), не чаше 1 раза в 2 мес.
    @PutMapping("/changeLimit")
    public RevisionResponseLimit<LimitDto> changeLimitUser(
            @RequestParam Long userId,
            @RequestParam BigDecimal sumLimit) {
        return limitService.changeLimitUser(userId, sumLimit);
    }

    /* Проведение платежа по userId, на сумму sumPay
     * В случае превышения установленного лимита -> откат транзакционного блока
     * Каждая операции платежа(списания) сохраняется в сущностях с уникальным timestamp
     * Получение уникального timestamp реализована в классе -> DateTimeUtils.uniqueTimestampMicros(),
     * уникальность обеспечена в т.ч. в многопоточности
     */
    @GetMapping("/runPayment")
    public RevisionResponseLimit<PaymentResponseDto> paymentLimitJPA(
            @RequestParam Long userId,
            @RequestParam BigDecimal sumPay) {
        return limitService.runPayment(userId, sumPay);
    }
}
