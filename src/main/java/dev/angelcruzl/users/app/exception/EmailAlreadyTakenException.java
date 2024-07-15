package dev.angelcruzl.users.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyTakenException extends RuntimeException {

    private String message;

    public EmailAlreadyTakenException(String message) {
        super(message);
    }

}
