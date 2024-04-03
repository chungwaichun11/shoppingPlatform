package fdm.shoppingPlatform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.contains;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import fdm.shoppingPlatform.model.Product;
import fdm.shoppingPlatform.repository.ProductRepository;
import fdm.shoppingPlatform.service.ProductService;

@SpringBootTest
public class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepositoryMock; 
    
    @InjectMocks
    private ProductService productService; 
    
    @Test
    public void testCreateNewProduct_SuccessfulCreation() {
        Product product = new Product();
        when(productRepositoryMock.save(any(Product.class))).thenReturn(product); 
        
        boolean result = productService.createNewProduct(product);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testFindProduct_ExistingProduct() {
        long productId = 1L;
        Product product = new Product();
        when(productRepositoryMock.findById(product.getProductId())).thenReturn(Optional.of(product));
        
        Product result = productService.findProduct(productId);

        assertEquals(result.getProductId(), product.getProductId());
    }
    
    @Test
    public void testFindAllProduct_NonExistingProduct() {
        // Arrange
        Product existingProduct = new Product();
        
        // Act
        List<Product> productList = productService.findAllProduct();
        
        // Assert
        assertThat(productList, not(contains(existingProduct)));
    }
}