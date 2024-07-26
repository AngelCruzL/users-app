package dev.angelcruzl.users.app.constants;

public class ErrorStatusCodeConstants {

    public static final String EMAIL_ALREADY_TAKEN = "EMAIL_ALREADY_TAKEN";
    public static final String USERNAME_ALREADY_TAKEN = "USERNAME_ALREADY_TAKEN";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String ROLE_NOT_FOUND = "ROLE_NOT_FOUND";
    public static final String WRONG_PASSWORD = "WRONG_PASSWORD";
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String INVALID_PERMISSION = "INVALID_PERMISSION";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    private ErrorStatusCodeConstants() {
        throw new IllegalStateException("Utility class");
    }

}
