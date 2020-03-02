package kmeshkov.revolut.contoller.account;

import kmeshkov.revolut.exception.AccountIsNotFoundException;
import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.exception.UserIsNotFoundException;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.service.account.AccountService;
import kmeshkov.revolut.service.account.AccountServiceHolder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
//TODO Make methods async
public class AccountControllerImpl implements AccountController {

    private static final String INTERNAL_ERROR_MESSAGE = "Internal error. Please contact with support";
    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User is not found. Please check that ownerId is correct." +
            "You should create user prior to create account for him";
    private static final String ACCOUNT_NOT_FOUND_ERROR_MESSAGE = "Account is not found. Please check that id is correct.";
    private AccountService accountService;

    public AccountControllerImpl() {
        accountService = AccountServiceHolder.getInstance();
    }

    @POST
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(Account entity) {
        try {
            Account account = accountService.createAccount(entity);
            return Response.ok(account).build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        } catch (UserIsNotFoundException e) {
            return Response.serverError().status(500, USER_NOT_FOUND_ERROR_MESSAGE).build();
        }
    }

    @GET
    @Path("/{id}")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("id") Long id) {
        try {
            Account account = accountService.getAccountById(id);
            return Response.ok(account).build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        } catch (AccountIsNotFoundException e) {
            return Response.serverError().status(500, ACCOUNT_NOT_FOUND_ERROR_MESSAGE).build();
        }
    }

    @PUT
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAccount(Account entity) {
        try {
            Account account = accountService.updateAccount(entity);
            return Response.ok(account).build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        }
    }

    @DELETE
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    //We don't delete accounts to save history info, can only disable it
    public Response deactivateAccount(@PathParam("id") Long id) {
        try {
            boolean result = accountService.deactivateAccount(id);
            if (result)
                return Response.status(200, "Account has been successfully deactivated").build();
            else return Response.status(500, "Cannot deactivate account. Please contact support").build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        }
    }
}
