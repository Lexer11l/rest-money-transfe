package kmeshkov.revolut.repository.transaction;

import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.model.transaction.Transaction;

public interface TransactionRepository {
    Transaction create(Transaction entity) throws StorageException;

    Transaction get(Long id) throws StorageException;
}
