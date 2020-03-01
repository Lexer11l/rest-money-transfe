package kmeshkov.revolut.repository.account;

import kmeshkov.revolut.exception.AccountIsNotFoundException;
import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.model.Currency;
import kmeshkov.revolut.model.account.Account;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class AccountRepositoryImpl implements AccountRepository {
    private Map<Long, Account> database;
    private AtomicLong keyCounter;

    AccountRepositoryImpl() {
        if (Boolean.parseBoolean(System.getProperty("isDebug"))) {
            initStorage();
        } else {
            database = new ConcurrentHashMap<>();
            keyCounter = new AtomicLong(1);
        }
    }

    @Override
    public Account createAccount(Account entity) throws StorageException {
        try {
            Long uid = keyCounter.getAndIncrement();
            entity.setId(uid);
            database.putIfAbsent(uid, entity);
            return entity;
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
    public Account updateAccount(Account entity) throws StorageException {
        try {
            return database.put(entity.getId(), entity);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
    }

    @Override
    public boolean deactivateAccount(Long id) throws StorageException {
        try {
            database.get(id).disable();
            return database.get(id).isActive();
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
    }

    private void initStorage() {
        keyCounter = new AtomicLong(6);
        database = new ConcurrentHashMap<Long, Account>() {{
            put(1L, new Account(1L, true, new BigDecimal(1000), Calendar.getInstance().getTime(), 1L, Currency.RUB));
            put(2L, new Account(2L, true, new BigDecimal(10000), Calendar.getInstance().getTime(), 1L, Currency.RUB));
            put(3L, new Account(3L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 2L, Currency.RUB));
            put(4L, new Account(4L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 3L, Currency.RUB));
            put(5L, new Account(5L, false, new BigDecimal(1000), Calendar.getInstance().getTime(), 4L, Currency.RUB));
        }};
    }
}
