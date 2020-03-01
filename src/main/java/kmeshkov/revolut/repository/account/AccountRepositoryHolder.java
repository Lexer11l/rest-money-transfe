package kmeshkov.revolut.repository.account;

public class AccountRepositoryHolder {
    private static volatile AccountRepository instance;

    public static AccountRepository getInstance() {
        AccountRepository localInstance = instance;
        if (localInstance == null) {
            synchronized (AccountRepositoryHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AccountRepositoryImpl();
                }
            }
        }
        return localInstance;
    }
}
