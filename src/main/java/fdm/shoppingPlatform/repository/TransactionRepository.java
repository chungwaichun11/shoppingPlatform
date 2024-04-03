package fdm.shoppingPlatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fdm.shoppingPlatform.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findById(long id);

}
