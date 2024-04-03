package fdm.shoppingPlatform.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GEN")
    private long userId;
    private String username;
    private String password;
    private String address;
    private String email;

    @OneToMany(mappedBy = "seller")
    private List<Product> products;

    @OneToMany(mappedBy = "buyer")
    private List<Transaction> boughtTransactions;

    @OneToMany(mappedBy = "seller")
    private List<Transaction> soldTransactions;

    public User() {
    }

    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Transaction> getBoughtTransactions() {
        return boughtTransactions;
    }

    public void setBoughtTransactions(List<Transaction> boughtTransactions) {
        this.boughtTransactions = boughtTransactions;
    }

    public List<Transaction> getSoldTransactions() {
        return soldTransactions;
    }

    public void setSoldTransactions(List<Transaction> soldTransactions) {
        this.soldTransactions = soldTransactions;
    }
}
