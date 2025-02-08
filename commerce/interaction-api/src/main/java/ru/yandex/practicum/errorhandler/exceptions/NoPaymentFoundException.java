package ru.yandex.practicum.errorhandler.exceptions;

public class NoPaymentFoundException extends RuntimeException {
    public NoPaymentFoundException(String message) {
        super(message);
    }
}