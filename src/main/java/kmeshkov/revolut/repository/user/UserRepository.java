package kmeshkov.revolut.repository.user;

import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.exception.UserIsNotFoundException;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.model.user.User;

import java.util.List;

public interface UserRepository {

    User createUser(User entity) throws StorageException;

    User getUser(Long id) throws StorageException, UserIsNotFoundException;

    User updateUser(User entity) throws StorageException, UserIsNotFoundException;

    boolean deactivateUser(Long id) throws StorageException, UserIsNotFoundException;

    List<Account> getAccountsByUserId(Long id) throws StorageException, UserIsNotFoundException;
}
