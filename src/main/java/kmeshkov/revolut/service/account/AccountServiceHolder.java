package kmeshkov.revolut.service.account;

public class AccountServiceHolder {
    private static volatile AccountService instance;

    public static AccountService getInstance() {
        AccountService localInstance = instance;
        if (localInstance == null) {
            synchronized (AccountServiceHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AccountServiceImpl();
                }
            }
        }
        return localInstance;
    }
}
