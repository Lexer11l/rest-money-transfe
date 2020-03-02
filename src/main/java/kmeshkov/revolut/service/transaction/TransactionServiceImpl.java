package kmeshkov.revolut.service.transaction;

import kmeshkov.revolut.exception.*;
import kmeshkov.revolut.model.account.Account;
import kmeshkov.revolut.model.transaction.Transaction;
import kmeshkov.revolut.repository.account.AccountRepository;
import kmeshkov.revolut.repository.account.AccountRepositoryHolder;
import kmeshkov.revolut.repository.transaction.TransactionRepository;
import kmeshkov.revolut.repository.transaction.TransactionRepositoryHolder;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

@Log4j2
public class TransactionServiceImpl implements TransactionService {
    public static final int MIN_TRANSACTION_AMOUNT = 0;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    TransactionServiceImpl() {
        this.transactionRepository = TransactionRepositoryHolder.getInstance();
        this.accountRepository = AccountRepositoryHolder.getInstance();
    }

    @Override
    public Transaction deposit(Transaction transaction) throws StorageException, NegativeAmountException, DeactivatedAccountException, AccountIsNotFoundException {
        depositToAccount(transaction);
        return saveTransaction(transaction);
    }

    @Override
    public Transaction withdraw(Transaction transaction) throws StorageException, NotEnoughMoneyException, NegativeAmountException, DeactivatedAccountException, AccountIsNotFoundException {
        withdrawFromAccount(transaction);
        return saveTransaction(transaction);
    }

    @Override
    public Transaction transfer(Transaction transaction) throws StorageException, NegativeAmountException, NotEnoughMoneyException, DeactivatedAccountException, AccountIsNotFoundException {
        withdrawFromAccount(transaction);
        depositToAccount(transaction);
        return saveTransaction(transaction);
    }

    private void depositToAccount(Transaction transaction) throws StorageException, NegativeAmountException, DeactivatedAccountException, AccountIsNotFoundException {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= MIN_TRANSACTION_AMOUNT) {
            throw new NegativeAmountException("Passed transaction amount is less or equal " + MIN_TRANSACTION_AMOUNT + ". Cannot deposit");
        }
        Account account = accountRepository.getAccountById(transaction.getToAccount());
        if (!account.isActive()){
            throw new DeactivatedAccountException("Account " + transaction.getToAccount() + " is deactivated");
        }
        account.setBalance(account.getBalance().add(transaction.getAmount()));
    }

    private void withdrawFromAccount(Transaction transaction) throws StorageException, NotEnoughMoneyException, NegativeAmountException, DeactivatedAccountException, AccountIsNotFoundException {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= MIN_TRANSACTION_AMOUNT) {
            throw new NegativeAmountException("Passed transaction amount is less or equal " + MIN_TRANSACTION_AMOUNT + ". Cannot deposit");
        }
        Account account = accountRepository.getAccountById(transaction.getFromAccount());
        if (!account.isActive()){
            throw new DeactivatedAccountException("Account " + transaction.getFromAccount() + " is deactivated");
        }
        if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new NotEnoughMoneyException("Not enough money to make operation");
        }
        account.setBalance(account.getBalance().subtract(transaction.getAmount()));
    }

    private Transaction saveTransaction(Transaction transaction) throws StorageException {
        Transaction savedTransaction = transactionRepository.create(transaction);
        log.info("Successful transaction:\n {}", savedTransaction);
        return savedTransaction;
    }
}
