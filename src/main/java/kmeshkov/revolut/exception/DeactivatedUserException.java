package kmeshkov.revolut.exception;

public class DeactivatedUserException extends Exception {
    public DeactivatedUserException(String errorMessage){
        super(errorMessage);
    }
}
