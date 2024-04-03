package fdm.shoppingPlatform.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fdm.shoppingPlatform.model.Product;
import fdm.shoppingPlatform.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;

    Logger logger = LogManager.getLogger(UserService.class);

    public boolean createNewProduct(Product product) {
        try {
            productRepo.save(product);
            return true;
        } catch (Exception e) {
            logger.error("Failed to create a new product: {}", e.getMessage());
            return false;
        }
    }

    public Product findProduct(long productId) {
        Optional<Product> productOptional = productRepo.findById(productId);
        Product product = productOptional.orElse(new Product());
        return product;
    }

    public Product findPostedProduct(String productName) {
        try {
            return productRepo.findByProductName(productName).orElse(null);
        } catch (Exception e) {
            logger.error("Failed to find the product with product name '{}': {}", productName, e.getMessage());
            return null;
        }
    }

    public List<Product> findAllProduct() {
        try {
            return productRepo.findAll();
        } catch (Exception e) {
            logger.error("Failed to retrieve all products: {}", e.getMessage());
            return null;
        }
    }

    public List<Product> findAllAvailableProduct(long sellerId) {
        try {
            return productRepo.findAllByStatusAndSellerUserIdNot("AVAILABLE", sellerId);
        } catch (Exception e) {
            logger.error("Failed to retrieve all available products for seller with ID {}: {}", sellerId,
                    e.getMessage());
            return null;
        }
    }

    public List<Product> findAllProductByUserID(long sellerId) {
        try {
            return productRepo.findAllBySellerUserId(sellerId);
        } catch (Exception e) {
            logger.error("Failed to retrieve all products for seller with ID {}: {}", sellerId, e.getMessage());
            return null;
        }
    }

    public List<Product> findAllProductByUserIdNot(long sellerId) {
        try {
            return productRepo.findAllBySellerUserIdNot(sellerId);
        } catch (Exception e) {
            logger.error("Failed to retrieve all products for sellers other than ID {}: {}", sellerId, e.getMessage());
            return null;
        }
    }

    public boolean editProduct(Product product) {
        try {
            Optional<Product> productOptional = productRepo.findById(product.getProductId());

            if (productOptional.isPresent()) {
                product.setStatus(productOptional.get().getStatus());
                product.setTransaction(productOptional.get().getTransaction());
                productRepo.save(product);
                logger.info("Successfully updated the product {}:", productOptional.get().getProductName());
                logger.info("(New)Price: ${}, Description: {}", product.getPrice(), product.getDescription());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to edit the product with ID {}: {}", product.getProductId(), e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(long productId) {
        try {
            Optional<Product> productOptional = productRepo.findById(productId);

            if (productOptional.isPresent()) {
                productRepo.delete(productOptional.get());
                logger.info("Successfully deleted the product {}:", productOptional.get().getProductName());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to delete the product with ID {}: {}", productId, e.getMessage());
            return false;
        }
    }
}
