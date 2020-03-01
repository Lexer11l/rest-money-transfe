package kmeshkov.revolut.service.transaction;

import kmeshkov.revolut.exception.*;
import kmeshkov.revolut.model.transaction.Transaction;

public interface TransactionService {
    Transaction deposit(Transaction transaction) throws StorageException, NegativeAmountException, DeactivatedAccountException, AccountIsNotFoundException;
    Transaction withdraw(Transaction transaction) throws StorageException, NotEnoughMoneyException, NegativeAmountException, DeactivatedAccountException, AccountIsNotFoundException;
    Transaction transfer(Transaction transaction) throws StorageException, NegativeAmountException, NotEnoughMoneyException, DeactivatedAccountException, AccountIsNotFoundException;
}
