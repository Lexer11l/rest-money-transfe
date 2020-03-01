package kmeshkov.revolut.contoller.transaction;

import kmeshkov.revolut.exception.*;
import kmeshkov.revolut.model.transaction.Transaction;
import kmeshkov.revolut.service.transaction.TransactionService;
import kmeshkov.revolut.service.transaction.TransactionServiceHolder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transaction")
//TODO Make methods async
public class TransactionControllerImpl implements TransactionController {
    private static final String INTERNAL_ERROR_MESSAGE = "Internal error. Please contact with support";
    private static final String ACCOUNT_NOT_FOUND_ERROR_MESSAGE = "Account is not found. Please check that id is correct.";
    private static final String NOT_ENOUGH_MONEY_ERROR_MESSAGE = "Not enough money to make operation";
    public static final String NON_POSITIVE_AMOUNT_ERROR_MESSAGE = "Transaction amount should be positive number";
    public static final String ACCOUNT_IS_DEACTIVATED_ERROR_MESSAGE = "Account is deactivated";

    private final TransactionService transactionService;

    public TransactionControllerImpl() {
        this.transactionService = TransactionServiceHolder.getInstance();
    }

    @POST
    @Path("/deposit")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(Transaction transaction) {
        try {
            return Response.ok(transactionService.deposit(transaction)).build();
        } catch (StorageException e) {
            return Response.status(500, INTERNAL_ERROR_MESSAGE).build();
        } catch (NegativeAmountException e) {
            return Response.serverError().status(400, NON_POSITIVE_AMOUNT_ERROR_MESSAGE).build();
        } catch (DeactivatedAccountException e) {
            return Response.serverError().status(500, ACCOUNT_IS_DEACTIVATED_ERROR_MESSAGE).build();
        } catch (AccountIsNotFoundException e) {
            return Response.serverError().status(500, ACCOUNT_NOT_FOUND_ERROR_MESSAGE).build();

        }
    }

    @POST
    @Path("/withdraw")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdraw(Transaction transaction) {
        try {
            return Response.ok(transactionService.withdraw(transaction)).build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        } catch (NotEnoughMoneyException e) {
            return Response.serverError().status(500, NOT_ENOUGH_MONEY_ERROR_MESSAGE).build();
        } catch (NegativeAmountException e) {
            return Response.serverError().status(400, NON_POSITIVE_AMOUNT_ERROR_MESSAGE).build();
        } catch (DeactivatedAccountException e) {
            return Response.serverError().status(500, ACCOUNT_IS_DEACTIVATED_ERROR_MESSAGE).build();
        } catch (AccountIsNotFoundException e) {
            return Response.serverError().status(500, ACCOUNT_NOT_FOUND_ERROR_MESSAGE).build();
        }
    }

    @POST
    @Path("/transfer")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(Transaction transaction) {
        try {
            return Response.ok(transactionService.transfer(transaction)).build();
        } catch (StorageException e) {
            return Response.serverError().status(500, INTERNAL_ERROR_MESSAGE).build();
        } catch (NegativeAmountException e) {
            return Response.serverError().status(400, NON_POSITIVE_AMOUNT_ERROR_MESSAGE).build();
        } catch (NotEnoughMoneyException e) {
            return Response.serverError().status(500, NOT_ENOUGH_MONEY_ERROR_MESSAGE).build();
        } catch (AccountIsNotFoundException e) {
            return Response.serverError().status(500, ACCOUNT_NOT_FOUND_ERROR_MESSAGE).build();
        } catch (DeactivatedAccountException e) {
            return Response.serverError().status(500, ACCOUNT_IS_DEACTIVATED_ERROR_MESSAGE).build();
        }
    }

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionById(@PathParam("id") Long id) {
        //TODO Implement
        return Response.noContent().build();
    }
}
