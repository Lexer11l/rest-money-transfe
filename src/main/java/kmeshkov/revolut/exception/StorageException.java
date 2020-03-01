package kmeshkov.revolut.exception;

public class StorageException extends Exception {
    public StorageException(Exception error) {
        super(error);
    }
}
