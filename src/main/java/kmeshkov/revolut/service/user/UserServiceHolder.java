package kmeshkov.revolut.service.user;

public class UserServiceHolder {
    private static volatile UserService instance;

    public static UserService getInstance() {
        UserService localInstance = instance;
        if (localInstance == null) {
            synchronized (UserServiceHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserServiceImpl();
                }
            }
        }
        return localInstance;
    }
}
