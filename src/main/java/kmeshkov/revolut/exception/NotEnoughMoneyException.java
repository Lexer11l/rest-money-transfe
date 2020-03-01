package kmeshkov.revolut.exception;

public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(String errorMessage){
        super(errorMessage);
    }
}
