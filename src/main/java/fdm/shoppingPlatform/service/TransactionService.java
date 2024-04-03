package fdm.shoppingPlatform.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fdm.shoppingPlatform.model.Product;
import fdm.shoppingPlatform.model.Transaction;
import fdm.shoppingPlatform.repository.ProductRepository;
import fdm.shoppingPlatform.repository.TransactionRepository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepo;
    @Autowired
    private ProductRepository productRepo;

    Logger logger = LogManager.getLogger(TransactionService.class);

    public boolean createNewTransaction(Transaction transaction) {
        try {
            transactionRepo.save(transaction);
            Product product = productRepo.findById(transaction.getProduct().getProductId()).orElse(null);
            if (product != null) {
                product.setStatus("HOLD");
                product.setTransaction(transaction);
                productRepo.save(product);
            } else {
                logger.error("Failed to find the product with ID {} for creating a new transaction",
                        transaction.getProduct().getProductId());
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("Failed to create a new transaction: {}", e.getMessage());
            return false;
        }
    }

    public List<Transaction> findAllTransactions() {
        try {
            return transactionRepo.findAll();
        } catch (Exception e) {
            logger.error("Failed to retrieve all transactions: {}", e.getMessage());
            return null;
        }
    }

    public Transaction findTransaction(long transactionId) {
        Optional<Transaction> transactionOptional = transactionRepo.findById(transactionId);
        Transaction transaction = transactionOptional.orElse(new Transaction());
        return transaction;
    }

    public boolean updateTransaction(Transaction transaction) {
        try {
            Optional<Transaction> transactionOptional = transactionRepo.findById(transaction.getTransactionId());
            Product product = productRepo.findById(transaction.getProduct().getProductId()).orElse(null);

            if (transactionOptional.isPresent() && product != null) {
                if (transactionOptional.get().isPaid())
                    transaction.setPaid(true);
                if (transactionOptional.get().isReceived())
                    transaction.setReceived(true);

                if (transaction.isPaid() && transaction.isReceived()) {
                    transaction.setCompleteDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    transaction.setStatus("COMPLETED");
                    product.setStatus("SOLD");
                } else {
                    transaction.setCompleteDate(null);
                    transaction.setStatus("PROCESSING");
                    product.setStatus("HOLD");
                }

                logger.info("The payment status is changed from {} to {}", transactionOptional.get().isPaid(),
                        transaction.isPaid());
                logger.info("The delivery status is changed from {} to {}", transactionOptional.get().isReceived(),
                        transaction.isReceived());
                logger.info("The transaction status changed from {} to {}", transactionOptional.get().getStatus(),
                        transaction.getStatus());
                logger.info("The product status of {} -- #{} is now changed to {}",
                        transaction.getProduct().getProductName(),
                        transaction.getProduct().getProductId(), product.getStatus());

                productRepo.save(product);
                transactionRepo.save(transaction);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to update the transaction with ID {}: {}", transaction.getTransactionId(),
                    e.getMessage());
            return false;
        }
    }
}
