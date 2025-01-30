package ru.yandex.practicum.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.errorhandler.exceptions.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(NotFoundException e) {
        return createMessage(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ValidationException.class, ValidationException.class,
            SpecifiedProductAlreadyInWarehouseException.class,
            ProductInShoppingCartLowQuantityInWarehouse.class, NoSpecifiedProductInWarehouseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequest(Exception e) {
        return createMessage(e, HttpStatus.BAD_REQUEST);
    }

    private ErrorMessage createMessage(Exception e, HttpStatus httpstatus) {
        return ErrorMessage.builder()
                .cause(e.getCause())
                .stackTrace(e.getStackTrace())
                .httpstatus(httpstatus)
                .userMessage(e.getMessage())
                .message(e.getMessage())
                .suppressed(e.getSuppressed())
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}