package dev.angelcruzl.users.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FieldAlreadyTakenException extends RuntimeException {

    private final String fieldName;
    private final String fieldValue;
    private final String statusCode;

    public FieldAlreadyTakenException(String fieldName, String fieldValue, String statusCode) {
        super(String.format("%s: '%s' already in use", fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.statusCode = statusCode;
    }

}