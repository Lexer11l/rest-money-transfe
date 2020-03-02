package kmeshkov.revolut.service.transaction;

import kmeshkov.revolut.exception.*;
import kmeshkov.revolut.model.Currency;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.model.transaction.Transaction;
import kmeshkov.revolut.model.user.User;
import kmeshkov.revolut.service.account.AccountService;
import kmeshkov.revolut.service.account.AccountServiceHolder;
import kmeshkov.revolut.service.user.UserService;
import kmeshkov.revolut.service.user.UserServiceHolder;
import kmeshkov.revolut.service.user.UserServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(BlockJUnit4ClassRunner.class)
public class TransactionServiceImplTest {

    private static AccountService accountService;
    private static UserService userService;
    private static TransactionService transactionService;
    private static List<Account> emptyArray = new ArrayList<>();
    private static User user1 = new User(1L, true, "Ivan", "Mokrov", emptyArray);
    private static User user2 = new User(2L, true, "Oleksiy", "Kuma", emptyArray);

    private static Account account1 = new Account(1L, true, BigDecimal.valueOf(10000), Calendar.getInstance().getTime(), 1L, Currency.RUB);
    private static Account account2 = new Account(2L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 2L, Currency.RUB);
    private static Account account3 = new Account(3L, false, BigDecimal.ZERO, Calendar.getInstance().getTime(), 2L, Currency.RUB);

    @BeforeClass
    public static void init() throws StorageException, UserIsNotFoundException {
        userService = UserServiceHolder.getInstance();
        accountService = AccountServiceHolder.getInstance();
        transactionService = TransactionServiceHolder.getInstance();
        userService.createUser(user1);
        userService.createUser(user2);
        accountService.createAccount(account1);
        accountService.createAccount(account2);
        accountService.createAccount(account3);
    }

    @Test
    public void depositTest() throws StorageException, AccountIsNotFoundException, NegativeAmountException, DeactivatedAccountException {
        BigDecimal initialAmount = accountService.getAccountById(1L).getBalance();
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAmount(BigDecimal.valueOf(10000));
        expectedTransaction.setToAccount(1);
        Transaction actualTransaction = transactionService.deposit(expectedTransaction);
        Assert.assertEquals(expectedTransaction.getAmount(), actualTransaction.getAmount());
        Assert.assertEquals(expectedTransaction.getToAccount(), actualTransaction.getToAccount());
        Assert.assertEquals(accountService.getAccountById(1L).getBalance(), initialAmount.add(actualTransaction.getAmount()));
    }

    @Test(expected = NegativeAmountException.class)
    public void depositNegativeAmount() throws StorageException, DeactivatedAccountException, NegativeAmountException, AccountIsNotFoundException {
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAmount(BigDecimal.valueOf(-10000));
        expectedTransaction.setToAccount(1);
        transactionService.deposit(expectedTransaction);

    }

    @Test(expected = AccountIsNotFoundException.class)
    public void depositNonExistingAmount() throws StorageException, DeactivatedAccountException, NegativeAmountException, AccountIsNotFoundException {
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAmount(BigDecimal.valueOf(10000));
        expectedTransaction.setToAccount(1000);
        transactionService.deposit(expectedTransaction);
    }

    @Test(expected = DeactivatedAccountException.class)
    public void depositDeactivatedAccount() throws DeactivatedAccountException, StorageException, AccountIsNotFoundException, NegativeAmountException {
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAmount(BigDecimal.valueOf(10000));
        expectedTransaction.setToAccount(3);
        transactionService.deposit(expectedTransaction);
    }

    @Test
    public void withdrawTest() throws StorageException, DeactivatedAccountException, NotEnoughMoneyException, NegativeAmountException, AccountIsNotFoundException {
        BigDecimal initialBalance = accountService.getAccountById(1L).getBalance();
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAmount(BigDecimal.valueOf(4000));
        expectedTransaction.setFromAccount(1);
        Transaction actualTransaction = transactionService.withdraw(expectedTransaction);
        Assert.assertEquals(expectedTransaction.getAmount(), actualTransaction.getAmount());
        Assert.assertEquals(expectedTransaction.getToAccount(), actualTransaction.getToAccount());
        Assert.assertEquals(accountService.getAccountById(1L).getBalance(), initialBalance.subtract(actualTransaction.getAmount()));
    }

    @Test(expected = NegativeAmountException.class)
    public void withdrawNegativeNumber() throws StorageException, DeactivatedAccountException, NotEnoughMoneyException, NegativeAmountException, AccountIsNotFoundException {
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAmount(BigDecimal.valueOf(-10000));
        expectedTransaction.setToAccount(1);
        transactionService.withdraw(expectedTransaction);
    }

    @Test(expected = AccountIsNotFoundException.class)
    public void withdrawFromNotExistingAccount() throws DeactivatedAccountException, NotEnoughMoneyException, StorageException, AccountIsNotFoundException, NegativeAmountException {
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAmount(BigDecimal.valueOf(10000));
        expectedTransaction.setFromAccount(1000);
        transactionService.withdraw(expectedTransaction);
    }


    @Test(expected = NotEnoughMoneyException.class)
    public void withdrawFromAccountWithNotEnoughMoney() throws DeactivatedAccountException, NotEnoughMoneyException, StorageException, AccountIsNotFoundException, NegativeAmountException {
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAmount(BigDecimal.valueOf(100000));
        expectedTransaction.setFromAccount(1);
        transactionService.withdraw(expectedTransaction);
    }

    @Test(expected = DeactivatedAccountException.class)
    public void withdrawFromDeactivatedAccount() throws DeactivatedAccountException, NotEnoughMoneyException, StorageException, AccountIsNotFoundException, NegativeAmountException {
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAmount(BigDecimal.valueOf(10000000));
        expectedTransaction.setFromAccount(3);
        transactionService.withdraw(expectedTransaction);
    }

    @Test
    public void transferTest() throws StorageException, DeactivatedAccountException, NegativeAmountException, AccountIsNotFoundException, NotEnoughMoneyException {
        BigDecimal initialBalance = accountService.getAccountById(1L).getBalance();
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setAmount(BigDecimal.valueOf(5000));
        expectedTransaction.setToAccount(2);
        expectedTransaction.setFromAccount(1);
        Transaction actualTransaction = transactionService.transfer(expectedTransaction);
        Assert.assertEquals(expectedTransaction.getAmount(), actualTransaction.getAmount());
        Assert.assertEquals(expectedTransaction.getToAccount(), actualTransaction.getToAccount());
        Assert.assertEquals(accountService.getAccountById(1L).getBalance(), initialBalance.subtract(actualTransaction.getAmount()));
        Assert.assertEquals(accountService.getAccountById(2L).getBalance(), actualTransaction.getAmount());
    }
}