package kmeshkov.revolut.repository.account;

import kmeshkov.revolut.exception.AccountIsNotFoundException;
import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.model.account.Account;

public interface AccountRepository {
    Account createAccount(Account entity) throws StorageException;

    Account getAccountById(Long id) throws StorageException, AccountIsNotFoundException;

    Account updateAccount(Account entity) throws StorageException;

    boolean deactivateAccount(Long id) throws StorageException;
}
