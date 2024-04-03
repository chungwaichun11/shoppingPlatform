package fdm.shoppingPlatform.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Product {
    @Id
    @SequenceGenerator(name = "PRODUCT_SEQ_GEN", sequenceName = "product_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_SEQ_GEN")
    private long productId;
    @ManyToOne
    private User seller;
    @OneToOne
    @JoinColumn(name = "transactionId")
    private Transaction transaction;
    private String productName;
    private double price;
    private String description;
    private String status;

    public Product() {
        this.status = "AVAILABLE";
    }

    public Product(String productName, double price, User seller, String description) {
        super();
        this.productName = productName;
        this.price = price;
        this.seller = seller;
        this.description = description;
        this.status = "AVAILABLE";
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

}
