package kmeshkov.revolut.contoller.account;

import kmeshkov.revolut.model.account.Account;

import javax.ws.rs.core.Response;

public interface AccountController {
    Response createAccount(Account entity);
    Response getAccountById(Long id);
    Response updateAccount(Account entity);
    Response deactivateAccount(Long id);
}
