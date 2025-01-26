package ru.yandex.practicum.errorhandler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ErrorMessage {
    private Throwable cause;
    private StackTraceElement[] stackTrace;
    private HttpStatus httpstatus;
    private String userMessage;
    private String message;
    private Throwable[] suppressed;
    private String localizedMessage;
}