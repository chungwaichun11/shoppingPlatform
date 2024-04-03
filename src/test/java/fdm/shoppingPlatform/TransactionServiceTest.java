package fdm.shoppingPlatform;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.contains;

import fdm.shoppingPlatform.model.Product;
import fdm.shoppingPlatform.model.Transaction;
import fdm.shoppingPlatform.repository.ProductRepository;
import fdm.shoppingPlatform.repository.TransactionRepository;
import fdm.shoppingPlatform.service.TransactionService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepositoryMock;
    
    @Mock
    private ProductRepository productRepositoryMock;
    
    @InjectMocks
    private TransactionService transactionService;
    
    @Test
    public void testCreateNewTransaction() {
        Product product = new Product();
        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        when(transactionRepositoryMock.save(transaction)).thenReturn(transaction);
        when(productRepositoryMock.findById(product.getProductId())).thenReturn(Optional.of(product));
        
        boolean result = transactionService.createNewTransaction(transaction);
        
        assertTrue(result);
        assertEquals("HOLD", product.getStatus());
        assertNotNull(product.getTransaction());
        verify(transactionRepositoryMock, times(1)).save(transaction);
        verify(productRepositoryMock, times(1)).save(product);
    }
    
    @Test
    public void testFindTransaction_ExistingTransaction() {
        long transactionId = 1L;
        Transaction existingTransaction = new Transaction();
        when(transactionRepositoryMock.findById(transactionId)).thenReturn(Optional.of(existingTransaction));
        
        Transaction result = transactionService.findTransaction(transactionId);
        
        assertEquals(existingTransaction, result);
        verify(transactionRepositoryMock, times(1)).findById(transactionId);
    }
    
    @Test
    public void testUpdateTransaction_CompletedTransaction() {
    	
        Product product = new Product();
        product.setProductId(1L);
        product.setStatus("HOLD");
        
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setPaid(true);
        transaction.setReceived(true);
        transaction.setProduct(product);
        
        Transaction existingTransaction = new Transaction();
        existingTransaction.setTransactionId(1L);
        existingTransaction.setPaid(false);
        existingTransaction.setReceived(false);
        existingTransaction.setStatus("PROCESSING");
        existingTransaction.setProduct(product);
        
        when(transactionRepositoryMock.findById(transaction.getTransactionId())).thenReturn(Optional.of(existingTransaction));
        when(productRepositoryMock.findById(product.getProductId())).thenReturn(Optional.of(product));
        
        boolean result = transactionService.updateTransaction(transaction);
        
        assertTrue(result);
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), transaction.getCompleteDate());
        assertEquals("COMPLETED", transaction.getStatus());
        assertEquals("SOLD", product.getStatus());
        verify(transactionRepositoryMock, times(1)).findById(transaction.getTransactionId());
        verify(productRepositoryMock, times(1)).findById(product.getProductId());
        verify(productRepositoryMock, times(1)).save(product);
        verify(transactionRepositoryMock, times(1)).save(transaction);
    }
    
    @Test
    public void testUpdateTransaction_IncompleteTransaction() {
        Product product = new Product();
        product.setProductId(1L);
        product.setStatus("SOLD");

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setPaid(false);
        transaction.setReceived(false);
        transaction.setProduct(product);

        Transaction existingTransaction = new Transaction();
        existingTransaction.setTransactionId(1L);
        existingTransaction.setPaid(true);
        existingTransaction.setReceived(true);
        existingTransaction.setStatus("COMPLETED");
        existingTransaction.setProduct(product);

        when(transactionRepositoryMock.findById(transaction.getTransactionId())).thenReturn(Optional.of(existingTransaction));
        when(productRepositoryMock.findById(product.getProductId())).thenReturn(Optional.of(product));
        
        boolean result = transactionService.updateTransaction(transaction);
        
        assertTrue(result);
        assertNull(transaction.getCompleteDate());
        assertEquals("PROCESSING", transaction.getStatus());
        assertEquals("HOLD", product.getStatus());
        verify(transactionRepositoryMock, times(1)).findById(transaction.getTransactionId());
        verify(productRepositoryMock, times(1)).findById(product.getProductId());
        verify(productRepositoryMock, times(1)).save(product);
        verify(transactionRepositoryMock, times(1)).save(transaction);
    }
    
    @Test
    public void testFindTransaction_NonExistingTransaction() {
        // Arrange
    	Transaction existingTransaction = new Transaction();
        
        // Act
        List<Transaction> transactionList = transactionService.findAllTransactions();
        
        // Assert
        assertThat(transactionList, not(contains(existingTransaction)));
    }
}