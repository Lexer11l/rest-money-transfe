package kmeshkov.revolut.service.account;

import kmeshkov.revolut.exception.AccountIsNotFoundException;
import kmeshkov.revolut.exception.UserIsNotFoundException;
import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.model.user.User;
import kmeshkov.revolut.repository.account.AccountRepository;
import kmeshkov.revolut.repository.account.AccountRepositoryHolder;
import kmeshkov.revolut.repository.user.UserRepository;
import kmeshkov.revolut.repository.user.UserRepositoryHolder;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl() {
        accountRepository = AccountRepositoryHolder.getInstance();
        userRepository = UserRepositoryHolder.getInstance();
    }

    @Override
    public Account createAccount(Account entity) throws StorageException, UserIsNotFoundException {
        User user = userRepository.getUser(entity.getOwnerUid());
        Account account = accountRepository.createAccount(entity);
        user.addAccount(account);
        return account;
    }

    @Override
    public Account getAccountById(Long id) throws StorageException, AccountIsNotFoundException {
        return accountRepository.getAccountById(id);
    }

    @Override
    public Account updateAccount(Account entity) throws StorageException, AccountIsNotFoundException {
        return accountRepository.updateAccount(entity);
    }

    @Override
    public boolean deactivateAccount(Long id) throws StorageException, AccountIsNotFoundException {
        return accountRepository.deactivateAccount(id);
    }

}
