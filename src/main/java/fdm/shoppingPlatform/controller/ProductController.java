package fdm.shoppingPlatform.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fdm.shoppingPlatform.model.Product;
import fdm.shoppingPlatform.model.User;
import fdm.shoppingPlatform.service.ProductService;
import fdm.shoppingPlatform.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("/add-item")
    public String goToAddItem(HttpSession session, Model model) {
        String username = (String) session.getAttribute("current_user");
        User user = userService.findVerifiedUser(username);
        model.addAttribute("user", user);
        return "add-item";
    }

    @PostMapping("/add")
    public String addNewItem(Product product, HttpSession session) {
        productService.createNewProduct(product);
        logger.info("{} successfully added the item {}", product.getSeller().getUsername(), product.getProductName());
        return "redirect:/add-item";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") long productId, Model model, HttpSession session) {
        String username = (String) session.getAttribute("current_user");
        User user = userService.findVerifiedUser(username);
        model.addAttribute("user", user);
        Product product = productService.findProduct(productId);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/edit-product/{id}")
    public String goToEditProduct(@PathVariable("id") long productId, Model model, HttpSession session) {
        String username = (String) session.getAttribute("current_user");
        User user = userService.findVerifiedUser(username);
        model.addAttribute("user", user);
        Product product = productService.findProduct(productId);
        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/edit-product/{id}")
    public String editProduct(Product product, Model model, HttpSession session) {
        if (productService.editProduct(product)) {
            return "redirect:/product/" + product.getProductId();
        } else {
            return "redirect:/product/" + product.getProductId();
        }
    }

    @GetMapping("/delete-product/{id}")
    public String deleteItem(@PathVariable("id") long productId, Model model, HttpSession session) {
        String username = (String) session.getAttribute("current_user");
        User user = userService.findVerifiedUser(username);
        if (productService.deleteProduct(productId)) {
            return "redirect:/users/" + user.getUserId();
        } else {
            return "redirect:/users/" + user.getUserId();
        }
    }
}
