package kmeshkov.revolut.contoller.transaction;

import kmeshkov.revolut.model.transaction.Transaction;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface TransactionController {
    Response deposit(Transaction transaction);
    Response withdraw(Transaction transaction);
    Response transfer(Transaction transaction);
    Response getTransactionById( Long id);
}
