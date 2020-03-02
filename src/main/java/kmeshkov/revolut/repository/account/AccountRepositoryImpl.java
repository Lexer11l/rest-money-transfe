package kmeshkov.revolut.repository.account;

import kmeshkov.revolut.exception.AccountIsNotFoundException;
import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.model.account.Account;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class AccountRepositoryImpl implements AccountRepository {
    private Map<Long, Account> database;
    private AtomicLong keyCounter;

    AccountRepositoryImpl() {
        database = new ConcurrentHashMap<>();
        keyCounter = new AtomicLong(1);
    }

    @Override
    public Account createAccount(Account entity) throws StorageException {
        try {
            Long uid = keyCounter.getAndIncrement();
            entity.setId(uid);
            database.putIfAbsent(uid, entity);
            return database.get(uid);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
    }

    @Override
    public Account getAccountById(Long id) throws StorageException, AccountIsNotFoundException {
        Account account;
        try {
            account = database.get(id);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
        if (account == null) {
            throw new AccountIsNotFoundException("Account " + id + "is not found");
        }
        return account;
    }

    @Override
    public Account updateAccount(Account entity) throws StorageException, AccountIsNotFoundException {
        long userId = entity.getId();
        if (!database.containsKey(userId))
            throw new AccountIsNotFoundException("Account with id " + userId + " is not found");
        Account account;
        try {
            database.put(userId, entity);
            account = database.get(userId);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
        return account;
    }

    @Override
    public boolean deactivateAccount(Long id) throws StorageException, AccountIsNotFoundException {
        Account account;
        try {
            account = database.get(id);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
        if (account == null) {
            throw new AccountIsNotFoundException("Account " + id + "is not found");
        }
        account.disable();
        return database.get(id).isActive();
    }
}
