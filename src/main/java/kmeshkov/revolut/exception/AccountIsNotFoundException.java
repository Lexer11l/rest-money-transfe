package kmeshkov.revolut.exception;

public class AccountIsNotFoundException extends Exception {
    public AccountIsNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
