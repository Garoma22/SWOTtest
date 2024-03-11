package pack1.exceptions;

public class NoSuchUserException extends RuntimeException {

    private final ErrorMesageEnum errorMessage;

    public NoSuchUserException(ErrorMesageEnum errorMessage){ // кладем обхект энма в парметры чтобы получить его строку
        super(errorMessage.getMessage()); // забираем строку из энама
        this.errorMessage = errorMessage; // вообще создаем обхект этого энама
    }
    public ErrorMesageEnum getErrorMessage(){
        return errorMessage;
    }
}
