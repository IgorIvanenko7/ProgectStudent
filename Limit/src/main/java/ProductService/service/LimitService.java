package ProductService.service;

import ProductService.Utils.CollectionModelMapper;
import ProductService.Utils.DateTimeUtils;
import ProductService.dto.*;
import ProductService.entity.PaymentEntity;
import ProductService.handleExeption.HandlerExeptionLimit;
import ProductService.handleExeption.HandlerExeptionProduct;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
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
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");

    // Ссохранение (выполнение) платежа в Payment
    @Transactional
    public RevisionResponseLimit<PaymentDto> runPaymentJPA(Long userId, BigDecimal sumPay) {

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

        // Save in entity Payments
        var paySaveEntity = paymentRepo.save(paymentEntity);
        // TODO Создать DTO возврата  paymentDto + LimitDto
        paymentDto = collectionModelMapper.map(paySaveEntity, PaymentDto.class);

        // Проверка наличия лимита для пользователя & инкремент лимита
        // insert with Predicate => если нет userId => добавить запись минус значение
        var limitEntity = limitRepo.runLimit(userId, sumPay /*, revisionPay*/);

//        if (limitEntity == null) {
//            throw new HandlerExeptionLimit(
//                    "Пользователь :" + username," не найден");
//        }

        var  limitDto = Optional.ofNullable(collectionModelMapper.map(limitEntity, LimitDto.class))
                .map(limitDtoo -> {
                    if (limitDtoo.getSumLimit().compareTo(BigDecimal.ZERO) < 0){
                        log.info("Выход за дневной лимит:{}", limitDtoo.getSumLimit());
                        throw new HandlerExeptionLimit(
                                "Проведение платежа отменено, пользователь ID:" + userId + ";\n",
                                " выход за лимит:" + limitDtoo.getSumLimit());
                    }
                    return limitDtoo;})
                .orElseThrow(() -> new HandlerExeptionLimit(
                        "Платеж не проведен, ID пользователя:", userId.toString()));
        // TODO вернуть -> UserEntity ( Там есть Payments & Limits)

        // Get Response
        return RevisionResponseLimit.of(revisionPay, paymentDto);
    }
    //------------------------------------------------------------------------------------------------



}
