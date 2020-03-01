package kmeshkov.revolut.repository.user;

public class UserRepositoryHolder {
    private static volatile UserRepository instance;

    public static UserRepository getInstance() {
        UserRepository localInstance = instance;
        if (localInstance == null) {
            synchronized (UserRepositoryImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserRepositoryImpl();
                }
            }
        }
        return localInstance;
    }

}
