package dev.angelcruzl.users.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class WrongPermissionsException extends RuntimeException {

    public WrongPermissionsException(String message) {
        super(message);
    }

}
