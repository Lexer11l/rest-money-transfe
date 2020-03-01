package kmeshkov.revolut.service.user;

import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.exception.UserIsNotFoundException;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.model.user.User;
import kmeshkov.revolut.repository.user.UserRepository;
import kmeshkov.revolut.repository.user.UserRepositoryHolder;

import java.math.BigDecimal;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl() {
        this.userRepository = UserRepositoryHolder.getInstance();
    }

    @Override
    //TODO Check whether user already exists by unique field in future
    public User createUser(User entity) throws StorageException {
        return userRepository.createUser(entity);
    }

    @Override
    public User getUserById(Long id) throws StorageException, UserIsNotFoundException {
        return userRepository.getUser(id);
    }

    @Override
    public User updateUser(User entity) throws StorageException, UserIsNotFoundException {
        return userRepository.updateUser(entity);
    }

    @Override
    public boolean deactivateUser(Long id) throws StorageException, UserIsNotFoundException {
        return userRepository.deactivateUser(id);
    }

    @Override
    public List<Account> getAccountsByUserId(Long id) throws StorageException, UserIsNotFoundException {
        return userRepository.getAccountsByUserId(id);
    }

    @Override
    public BigDecimal getBalanceByUserId(Long id) throws UserIsNotFoundException, StorageException {
        return getAccountsByUserId(id).stream()
                .map(Account::getBalance)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

}
