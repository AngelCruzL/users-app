package dev.angelcruzl.users.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongFieldException extends RuntimeException {

    private final String statusCode;

    public WrongFieldException(String message, String statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
