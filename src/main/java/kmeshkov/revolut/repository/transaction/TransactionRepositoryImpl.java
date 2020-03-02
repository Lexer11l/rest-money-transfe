package kmeshkov.revolut.repository.transaction;

import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.model.transaction.Transaction;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Log4j2
public class TransactionRepositoryImpl implements TransactionRepository {
    private Map<Long, Transaction> database;
    private AtomicLong keyCounter;

    TransactionRepositoryImpl() {
        database = new ConcurrentHashMap<>();
        keyCounter = new AtomicLong(1);
    }

    @Override
    public Transaction create(Transaction entity) throws StorageException {
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
    public Transaction get(Long id) throws StorageException {
        try {
            return database.get(id);
        } catch (Exception e) {
            log.error(e);
            throw new StorageException(e);
        }
    }
}
