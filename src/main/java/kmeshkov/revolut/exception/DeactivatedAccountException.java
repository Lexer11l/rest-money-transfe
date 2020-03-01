package kmeshkov.revolut.exception;

public class DeactivatedAccountException extends Exception {
    public DeactivatedAccountException(String errorMessage){
        super(errorMessage);
    }
}
