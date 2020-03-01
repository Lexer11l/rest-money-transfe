package kmeshkov.revolut.repository.user;

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
public class UserRepositoryImplTest {

    private static UserRepository repository;
    private static List<Account> emptyArray = new ArrayList<>();
    private static List<Account> accountsList = new ArrayList<>();
    private static User user1 = new User(1L, true, "Ivan", "Mokrov", emptyArray);
    private static User user2 = new User(2L, true, "Oleksiy", "Kuma", emptyArray);
    private static User user3 = new User(3L, true, "Pert", "Malick", accountsList);

    @BeforeClass
    public static void init() throws StorageException {
        repository = UserRepositoryHolder.getInstance();
        accountsList.add(new Account(1L, true, BigDecimal.ZERO, Calendar.getInstance().getTime(), 3L, Currency.RUB));
        repository.createUser(user1);
        repository.createUser(user2);
        repository.createUser(user3);
    }

    @Test
    public void createUser() throws StorageException {
        User user4 = new User(4L, true, "Pert", "Malick", emptyArray);
        User createdUser = repository.createUser(user4);
        Assert.assertNotNull(createdUser);
        long userId = createdUser.getId();
        Assert.assertEquals(4L, userId);
        Assert.assertEquals(createdUser, user4);
    }

    @Test
    public void getUser() throws StorageException, UserIsNotFoundException {
        User user = repository.getUser(1L);
        Assert.assertEquals(user, user1);
    }

    @Test(expected = UserIsNotFoundException.class)
    public void getUserWithWrongId() throws StorageException, UserIsNotFoundException {
        User user = repository.getUser(7L);
        Assert.assertNull(user);
    }

    @Test
    public void updateUser() throws UserIsNotFoundException, StorageException {
        repository.updateUser(new User(2L, true, "Igor", "Krutoy", emptyArray));
    }

    @Test(expected = UserIsNotFoundException.class)
    public void updateNotExistUser() throws UserIsNotFoundException, StorageException {
        repository.updateUser(new User(100L, true, "Igor", "Krutoy", emptyArray));
    }

    @Test
    public void deactivateUser() throws UserIsNotFoundException, StorageException {
        Assert.assertFalse(repository.deactivateUser(2L));
    }

    @Test
    public void getAccountsByUserId() throws StorageException, UserIsNotFoundException {
        List<Account> actualAccountList = repository.getAccountsByUserId(3L);
        Assert.assertArrayEquals(user3.getAccounts().toArray(), actualAccountList.toArray());
    }
}