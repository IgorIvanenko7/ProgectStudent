package PayService.controller;

import PayService.dto.ExceptionDto;
import PayService.handleExeption.HandlerExeptionPay;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandlerPay {

    @ExceptionHandler(HandlerExeptionPay.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto customException(HandlerExeptionPay exception) {
        return ExceptionDto.builder().code(HttpStatus.CONFLICT.value())
                .message(exception.getMessage()).build();
    }
}
