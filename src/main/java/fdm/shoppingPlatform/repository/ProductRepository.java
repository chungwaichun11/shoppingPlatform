package fdm.shoppingPlatform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fdm.shoppingPlatform.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);

    List<Product> findAllBySellerUserId(long sellerId);

    List<Product> findAllBySellerUserIdNot(long sellerId);

    List<Product> findAllByStatusAndSellerUserIdNot(String status, long sellerId);
}
