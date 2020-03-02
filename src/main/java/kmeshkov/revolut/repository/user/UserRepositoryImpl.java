package kmeshkov.revolut.repository.user;

import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.exception.UserIsNotFoundException;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.model.user.User;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> database;
    private AtomicLong keyCounter;

    UserRepositoryImpl() {
        database = new ConcurrentHashMap<>();
        keyCounter = new AtomicLong(1);
    }

    @Override
    public User createUser(User entity) throws StorageException {
        try {
            long uid = keyCounter.getAndIncrement();
            entity.setId(uid);
            database.put(uid, entity);
            return database.get(uid);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
    }

    @Override
    public User getUser(Long id) throws StorageException, UserIsNotFoundException {
        User user;
        try {
            user = database.get(id);
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
            database.put(userId, entity);
            return database.get(userId);
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
}
