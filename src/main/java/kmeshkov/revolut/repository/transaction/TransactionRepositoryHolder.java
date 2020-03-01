package kmeshkov.revolut.repository.transaction;

public class TransactionRepositoryHolder {
    private static volatile TransactionRepository instance;

    public static TransactionRepository getInstance() {
        TransactionRepository localInstance = instance;
        if (localInstance == null) {
            synchronized (TransactionRepositoryHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new TransactionRepositoryImpl();
                }
            }
        }
        return localInstance;
    }
}
