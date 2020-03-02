package kmeshkov.revolut.service.account;

import kmeshkov.revolut.exception.AccountIsNotFoundException;
import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.exception.UserIsNotFoundException;
import kmeshkov.revolut.model.Currency;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.model.user.User;
import kmeshkov.revolut.service.user.UserService;
import kmeshkov.revolut.service.user.UserServiceHolder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(BlockJUnit4ClassRunner.class)
public class AccountServiceImplTest {

    private static AccountService accountService;
    private static UserService userService;
    private static List<Account> emptyArray = new ArrayList<>();
    private static User user1 = new User(1L, true, "Ivan", "Mokrov", emptyArray);
    private static User user2 = new User(2L, true, "Oleksiy", "Kuma", emptyArray);

    private static Account account1 = new Account(1L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 1L, Currency.RUB);
    private static Account account2 = new Account(2L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 2L, Currency.RUB);

    @BeforeClass
    public static void init() throws StorageException, UserIsNotFoundException {
        accountService = AccountServiceHolder.getInstance();
        userService = UserServiceHolder.getInstance();
        userService.createUser(user1);
        userService.createUser(user2);
        accountService.createAccount(account1);
        accountService.createAccount(account2);
    }

    @Test
    public void createAccount() throws StorageException, UserIsNotFoundException {
        Account account3 = new Account(3L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 1L, Currency.RUB);
        Account createdAccount = accountService.createAccount(account3);
        Assert.assertNotNull(createdAccount);
        long accountId = createdAccount.getId();
        Assert.assertEquals(3L, accountId);
        Assert.assertEquals(createdAccount, account3);
    }

    @Test(expected = UserIsNotFoundException.class)
    public void createAccountForNotExistingUser() throws StorageException, UserIsNotFoundException {
        Account account4 = new Account(4L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 1000L, Currency.RUB);
        accountService.createAccount(account4);
    }

    @Test
    public void getAccount() throws StorageException, AccountIsNotFoundException {
        Account account = accountService.getAccountById(1L);
        Assert.assertEquals(account, account1);
    }

    @Test(expected = AccountIsNotFoundException.class)
    public void getAccountWithWrongId() throws StorageException, AccountIsNotFoundException {
        accountService.getAccountById(7L);
    }

    @Test
    public void updateAccount() throws StorageException, AccountIsNotFoundException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 100);
        Account updatedAccount = new Account(1L, false, BigDecimal.TEN, calendar.getTime(), 2L, Currency.USD);
        Account savedAccount = accountService.updateAccount(updatedAccount);
        Assert.assertEquals(updatedAccount.getBalance(), savedAccount.getBalance());
        Assert.assertEquals(updatedAccount.getId(), savedAccount.getId());
        Assert.assertEquals(updatedAccount.getOwnerUid(), savedAccount.getOwnerUid());
        Assert.assertEquals(updatedAccount.getCurrency(), savedAccount.getCurrency());
    }

    @Test(expected = AccountIsNotFoundException.class)
    public void updateNotExistingAccount() throws StorageException, AccountIsNotFoundException {
        accountService.updateAccount(new Account(1000L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 1L, Currency.RUB));
    }

    @Test
    public void deactivateAccount() throws StorageException, AccountIsNotFoundException {
        Assert.assertFalse(accountService.deactivateAccount(2L));
    }

    @Test(expected = AccountIsNotFoundException.class)
    public void deactivateAccountWithWrongId() throws StorageException, AccountIsNotFoundException {
        Assert.assertFalse(accountService.deactivateAccount(112L));
    }
}