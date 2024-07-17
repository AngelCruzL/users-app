package dev.angelcruzl.users.app.exception;

public class WrongPasswordException extends RuntimeException {

    private String message;

    public WrongPasswordException(String message) {
        super(message);
    }

}
