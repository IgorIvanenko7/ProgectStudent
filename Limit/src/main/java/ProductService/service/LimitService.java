package ProductService.service;

import ProductService.Utils.CollectionModelMapper;
import ProductService.Utils.DateTimeUtils;
import ProductService.dto.*;
import ProductService.entity.PaymentEntity;
import ProductService.handleExeption.HandlerExeptionLimit;
import ProductService.repo.LimitRepo;
import ProductService.repo.PaymentRepo;
import ProductService.repo.ProductRepo;
import ProductService.repo.UserRepo;
import ProductService.repository.DbOperations;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.*;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class LimitService {

    private final DbOperations dbOperations;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final PaymentRepo paymentRepo;
    private final LimitRepo limitRepo;
    private final CollectionModelMapper collectionModelMapper;

    public RevisionResponseLimit<LimitDto> changeLimitUser(Long userId, BigDecimal sumNewBaseLimit) {

         Instant dateInstallLimit = DateTimeUtils.uniqueTimestampMicros();
         var limitEntityResp = Optional.ofNullable(limitRepo.findLimitForUser(userId))
                .map(limitEntity -> collectionModelMapper.map(limitEntity, LimitDto.class))
                .map(limitDto -> {
                    /* Вычисление разницы в месяцах Лимита пользователя
                     * с корректным учетом перехода между годами
                     * Если > 2 мес. Тогда обновление Лимта
                     */
                    ZonedDateTime  dateTimeCurrentLimit = limitDto.getDateInstall().atZone(ZoneId.systemDefault());
                    ZonedDateTime  dateTimeNow  = Instant.now().atZone(ZoneId.systemDefault());
                    int monthsDiff = Period.between(dateTimeCurrentLimit.toLocalDate(), dateTimeNow.toLocalDate()).getMonths();

                    if (monthsDiff < 2) {
                        throw new HandlerExeptionLimit(
                                "Изменение бащового лимта отменено, пользователь ID:" + userId,
                                "; текущий базовый лимит изменен(создан) < 2 мес. назад");
                    }
                    return limitRepo.updateLimit(userId, sumNewBaseLimit, dateInstallLimit);
                })
                .orElseThrow(() -> new HandlerExeptionLimit(
                        "Лимит пользователь c ID:" + userId," не найден"));

        return RevisionResponseLimit.of(dateInstallLimit,
                collectionModelMapper.map(limitEntityResp, LimitDto.class));
    }

    @Transactional
    public RevisionResponseLimit<PaymentResponseDto> runPayment(Long userId, BigDecimal sumPay) {

        // Validate exists user on ID
        if (userRepo.findUserId(userId) == null) {
            throw new HandlerExeptionLimit(
                    "Пользователь c ID:" + userId," не найден, платеж не выполнен");
        }
        Instant revisionPay = DateTimeUtils.uniqueTimestampMicros();
        PaymentDto paymentDto  = PaymentDto.createNewPayment(
                userId, sumPay, revisionPay);

        var paymentEntity = Optional.ofNullable(collectionModelMapper.map(paymentDto, PaymentEntity.class))
                .map(paymentEnt ->{
                    log.info("Convert PayDto to Entity: {}", paymentEnt);
                    return paymentEnt;})
                .orElseThrow(() -> new HandlerExeptionLimit(
                        "Ошибка Маппирования в PaymentEntity, ID пользователя:", userId.toString()));

        var paySaveEntity = paymentRepo.save(paymentEntity);
        paymentDto = collectionModelMapper.map(paySaveEntity, PaymentDto.class);

        /* Проверка наличия лимита для пользователя, уменьшение лимита на сумму платежа
         * В случае отсутствия лимита -> создание лимита = 10000.00 "минус" сумма текущего платежа
         */
        var limitEntity = limitRepo.runLimit(userId, sumPay , revisionPay);
        limitEntity.setSumDaylimit(limitEntity.getSumDaylimit().subtract(sumPay));

        var limitDto = Optional.ofNullable(collectionModelMapper.map(limitEntity, LimitDto.class))
                .map(limitDtoo -> {
                    if (limitDtoo.getSumDaylimit().compareTo(BigDecimal.ZERO) < 0){
                        log.info("Выход за дневной лимит:{}", limitDtoo.getSumDaylimit());
                        throw new HandlerExeptionLimit(
                                "Проведение платежа отменено, пользователь ID:" + userId,
                                "; выход за лимит:" + limitDtoo.getSumDaylimit());
                    }
                    return limitDtoo;})
                .orElseThrow(() -> new HandlerExeptionLimit(
                        "Платеж не проведен, ID пользователя:", userId.toString()));

        return RevisionResponseLimit.of(revisionPay,
                PaymentResponseDto.createPaymentResponseDto(paymentDto, limitDto));
    }
}
