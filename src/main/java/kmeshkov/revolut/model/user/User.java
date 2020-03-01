package kmeshkov.revolut.model.user;

import kmeshkov.revolut.model.account.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User{
    private Long id;
    private boolean isActive = true;
    private String firstName;
    private String secondName;
    private List<Account> accounts = new CopyOnWriteArrayList<>();

    public void disable(){
        isActive = false;
        accounts.forEach(Account::disable);
    }

    public void addAccount(Account account){
        accounts.add(account);
    }
}
