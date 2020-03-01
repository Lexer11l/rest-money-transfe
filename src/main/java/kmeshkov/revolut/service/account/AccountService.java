package kmeshkov.revolut.service.account;

import kmeshkov.revolut.exception.AccountIsNotFoundException;
import kmeshkov.revolut.exception.UserIsNotFoundException;
import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.model.account.Account;

public interface AccountService {
    Account createAccount(Account entity) throws StorageException, UserIsNotFoundException;

    Account getAccountById(Long id) throws StorageException, AccountIsNotFoundException;

    Account updateAccount(Account entity) throws StorageException;

    boolean deactivateAccount(Long id) throws StorageException;
}
