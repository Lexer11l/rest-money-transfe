package kmeshkov.revolut.contoller.user;

import kmeshkov.revolut.exception.StorageException;
import kmeshkov.revolut.exception.UserIsNotFoundException;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.model.user.User;
import kmeshkov.revolut.service.user.UserService;
import kmeshkov.revolut.service.user.UserServiceHolder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/user")
//TODO Make methods async
public class UserControllerImpl implements UserController {
    private static final String INTERNAL_ERROR_MESSAGE = "Internal error. Please contact with support";
    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User is not found. Please check that ownerId is correct." +
            "You should create user prior to create account for him";

    private UserService userService;

    public UserControllerImpl(){
        this.userService = UserServiceHolder.getInstance();
    }

    @POST
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User entity) {
        try {
            return Response.ok(userService.createUser(entity)).build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        }
    }

    @GET
    @Path("/{id}")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) {
        try {
            return Response.ok(userService.getUserById(id)).build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        } catch (UserIsNotFoundException e) {
            return Response.serverError().status(500, USER_NOT_FOUND_ERROR_MESSAGE).build();
        }
    }

    @PUT
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(User entity) {
        try {
            return Response.ok(userService.updateUser(entity)).build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        } catch (UserIsNotFoundException e) {
            return Response.serverError().status(500, USER_NOT_FOUND_ERROR_MESSAGE).build();
        }
    }

    @DELETE
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    //We don't delete accounts to save history info, can only disable it
    public Response deactivateUser(@PathParam("id") Long id) {
        try {
            boolean result = userService.deactivateUser(id);
            if (result)
                return Response.status(200, "User t has been successfully deactivated").build();
            else return Response.status(500, "Cannot deactivate user. Please contact support").build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        } catch (UserIsNotFoundException e) {
            return Response.serverError().status(500, USER_NOT_FOUND_ERROR_MESSAGE).build();
        }
    }

    @GET
    @Path("/{id}/balance")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    //TODO Implement authentication to show balance only for owner or other authorized person
    public Response checkUserBalance(@PathParam("id") Long id) {
        try {
            BigDecimal balanceAmount = userService.getBalanceByUserId(id);
            return Response.ok(balanceAmount).build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        } catch (UserIsNotFoundException e) {
            return Response.serverError().status(500, USER_NOT_FOUND_ERROR_MESSAGE).build();
        }
    }


    @GET
    @Path("/{id}/accounts")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUserAccounts(@PathParam("id") Long id) {
        try {
            return Response.ok(userService.getAccountsByUserId(id)).build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        } catch (UserIsNotFoundException e) {
            return Response.serverError().status(500, USER_NOT_FOUND_ERROR_MESSAGE).build();
        }
    }
}
