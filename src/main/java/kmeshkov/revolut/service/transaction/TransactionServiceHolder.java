package kmeshkov.revolut.service.transaction;

public class TransactionServiceHolder {
    private static volatile TransactionService instance;

    public static TransactionService getInstance() {
        TransactionService localInstance = instance;
        if (localInstance == null) {
            synchronized (TransactionServiceHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new TransactionServiceImpl();
                }
            }
        }
        return localInstance;
    }
}
