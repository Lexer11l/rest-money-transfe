package kmeshkov.revolut.exception;

public class UserIsNotFoundException extends Exception {
    public UserIsNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
