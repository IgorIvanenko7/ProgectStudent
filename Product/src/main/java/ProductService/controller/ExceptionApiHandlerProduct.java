package ProductService.controller;

import ProductService.dto.ExceptionDto;
import ProductService.handleExeption.HandlerExeptionProduct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandlerProduct {

    @ExceptionHandler(HandlerExeptionProduct.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto customException(HandlerExeptionProduct exception) {
        return ExceptionDto.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage()).build();
    }
}
