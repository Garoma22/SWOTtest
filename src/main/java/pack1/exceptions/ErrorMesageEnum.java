package pack1.exceptions;

public enum ErrorMesageEnum {

    USER_NOT_FOUND("User not found"),
    INVALID_REQUEST("Invalid request");


    private final String message;

    ErrorMesageEnum(String message) {
        this.message = message;
    }

    public String getMessage() { // вот этим методом.
        return message;
    }
}