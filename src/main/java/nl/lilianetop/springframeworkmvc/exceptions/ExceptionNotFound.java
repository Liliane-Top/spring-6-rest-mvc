package nl.lilianetop.springframeworkmvc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value Not Found")
public class ExceptionNotFound extends RuntimeException {
    public ExceptionNotFound() {
    }

    public ExceptionNotFound(String message) {
        super(message);
    }

    public ExceptionNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionNotFound(Throwable cause) {
        super(cause);
    }

    public ExceptionNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
