package kmeshkov.revolut.service.user;

import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.exception.UserIsNotFoundException;
import kmeshkov.revolut.model.Currency;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.model.user.User;
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
public class UserServiceImplTest {

    private static UserService userService;
    private static List<Account> emptyArray = new ArrayList<>();
    private static List<Account> accountsList = new ArrayList<>();
    private static User user1 = new User(1L, true, "Ivan", "Mokrov", emptyArray);
    private static User user2 = new User(2L, true, "Oleksiy", "Kuma", emptyArray);
    private static User user3 = new User(3L, true, "Pert", "Malick", accountsList);

    @BeforeClass
    public static void init() throws StorageException, NoSuchFieldException, IllegalAccessException {
        userService = UserServiceHolder.getInstance();
        accountsList.add(new Account(1L, true, BigDecimal.TEN, Calendar.getInstance().getTime(), 3L, Currency.RUB));
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
    }

    @Test
    public void createUser() throws StorageException {
        User user4 = new User(4L, true, "Pert", "Malick", emptyArray);
        User createdUser = userService.createUser(user4);
        Assert.assertNotNull(createdUser);
        long userId = createdUser.getId();
        Assert.assertEquals(4L, userId);
        Assert.assertEquals(createdUser, user4);
    }

    @Test
    public void getUser() throws StorageException, UserIsNotFoundException {
        User user = userService.getUserById(1L);
        Assert.assertEquals(user, user1);
    }

    @Test(expected = UserIsNotFoundException.class)
    public void getUserWithWrongId() throws StorageException, UserIsNotFoundException {
        userService.getUserById(7L);
    }

    @Test
    public void updateUser() throws UserIsNotFoundException, StorageException {
        User updatedUser = new User(2L, true, "Igor", "Krutoy", emptyArray);
        User savedUser = userService.updateUser(updatedUser);
        Assert.assertEquals(updatedUser, savedUser);
    }

    @Test(expected = UserIsNotFoundException.class)
    public void updateNotExistUser() throws UserIsNotFoundException, StorageException {
        userService.updateUser(new User(100L, true, "Igor", "Krutoy", emptyArray));
    }

    @Test
    public void deactivateUser() throws UserIsNotFoundException, StorageException {
        Assert.assertFalse(userService.deactivateUser(2L));
    }

    @Test(expected = UserIsNotFoundException.class)
    public void deactivateUserWithWrongId() throws UserIsNotFoundException, StorageException {
        Assert.assertFalse(userService.deactivateUser(112L));
    }

    @Test
    public void getAccountsByUserId() throws StorageException, UserIsNotFoundException {
        List<Account> actualAccountList = userService.getAccountsByUserId(3L);
        Assert.assertArrayEquals(user3.getAccounts().toArray(), actualAccountList.toArray());
    }

    @Test(expected = UserIsNotFoundException.class)
    public void getAccountsByNotExistingUserId() throws StorageException, UserIsNotFoundException {
        List<Account> actualAccountList = userService.getAccountsByUserId(1000L);
        Assert.assertArrayEquals(user3.getAccounts().toArray(), actualAccountList.toArray());
    }

    @Test
    public void getBalanceByUserId() throws UserIsNotFoundException, StorageException {
        BigDecimal balance = userService.getBalanceByUserId(2L);
        Assert.assertEquals(BigDecimal.ZERO, balance);
    }

    @Test
    public void getZeroBalanceByUserId() throws UserIsNotFoundException, StorageException {
        BigDecimal balance = userService.getBalanceByUserId(3L);
        Assert.assertEquals(BigDecimal.TEN, balance);
    }

    @Test(expected = UserIsNotFoundException.class)
    public void getBalanceByNotExistingUserId() throws UserIsNotFoundException, StorageException {
        userService.getBalanceByUserId(310L);
    }
}