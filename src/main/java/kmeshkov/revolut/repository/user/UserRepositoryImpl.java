package kmeshkov.revolut.repository.user;

import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.exception.UserIsNotFoundException;
import kmeshkov.revolut.model.Currency;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.model.user.User;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> database;
    private AtomicLong keyCounter;

    UserRepositoryImpl() {
        if (Boolean.parseBoolean(System.getProperty("isDebug"))) {
            initStorage();
        } else {
            database = new ConcurrentHashMap<>();
            keyCounter = new AtomicLong(1);
        }
    }

    @Override
    public User createUser(User entity) throws StorageException {
        try {
            long uid = keyCounter.getAndIncrement();
            entity.setId(uid);
            database.put(uid, entity);
            return entity;
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
    }

    @Override
    public User getUser(Long id) throws StorageException, UserIsNotFoundException {
        User user;
        try {
            user =  database.get(id);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
        if (user == null)
            throw new UserIsNotFoundException("User with id " + id + " is not found");
        return user;
    }

    @Override
    public User updateUser(User entity) throws StorageException, UserIsNotFoundException {
        Long userId = entity.getId();
        if (!database.containsKey(userId))
            throw new UserIsNotFoundException("User with id " + userId + " is not found");
        try {
            return database.put(userId, entity);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
    }

    @Override
    public boolean deactivateUser(Long id) throws StorageException, UserIsNotFoundException {
        User user;
        try {
            user = database.get(id);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
        if (user == null)
            throw new UserIsNotFoundException("User with id " + id + " is not found");
        user.disable();
        return database.get(id).isActive();
    }

    @Override
    public List<Account> getAccountsByUserId(Long id) throws StorageException, UserIsNotFoundException {
        User user;
        try {
            user = database.get(id);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
        if (user == null)
            throw new UserIsNotFoundException("User with id " + id + " is not found");
        return user.getAccounts();
    }

    private void initStorage() {
        keyCounter = new AtomicLong(6);
        List<Account> firstUserAccounts = new CopyOnWriteArrayList<>();
        firstUserAccounts.add(new Account(1L, true, new BigDecimal(1000), Calendar.getInstance().getTime(), 1L, Currency.RUB));
        firstUserAccounts.add(new Account(2L, true, new BigDecimal(10000), Calendar.getInstance().getTime(), 1L, Currency.RUB));

        List<Account> secondUserAccounts = new CopyOnWriteArrayList<>();
        secondUserAccounts.add(new Account(3L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 2L, Currency.RUB));

        List<Account> thirdUserAccounts = new CopyOnWriteArrayList<>();
        thirdUserAccounts.add(new Account(4L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 3L, Currency.RUB));

        List<Account> fourthUserAccounts = new CopyOnWriteArrayList<>();
        fourthUserAccounts.add(new Account(5L, false, new BigDecimal(1000), Calendar.getInstance().getTime(), 4L, Currency.RUB));

        List<Account> fifthUserAccounts = new CopyOnWriteArrayList<>();

        database = new ConcurrentHashMap<Long, User>() {{
            put(1L, new User(1L, true, "Oleg", "Dal", firstUserAccounts));
            put(2L, new User(2L, true, "Ivan", "Dubrov", secondUserAccounts));
            put(3L, new User(3L, true, "Nikolay", "Petrov", thirdUserAccounts));
            put(4L, new User(4L, true, "John", "Campbell", fourthUserAccounts));
            put(5L, new User(5L, true, "Petr", "Ivanov", fifthUserAccounts));
        }};
    }
}
