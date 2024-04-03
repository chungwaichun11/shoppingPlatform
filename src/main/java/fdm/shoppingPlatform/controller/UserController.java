package fdm.shoppingPlatform.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fdm.shoppingPlatform.model.Product;
import fdm.shoppingPlatform.model.User;
import fdm.shoppingPlatform.service.ProductService;
import fdm.shoppingPlatform.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String goToIndex() {
        return "landing";
    }

    Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("/landing")
    public String goToLanding(Model model, HttpSession session) {
        if (session.getAttribute("current_user") == null) {
            model.addAttribute("user", new User());
            return "landing";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/register")
    public String goToRegister(Model model, HttpSession session) {
        // if (session.getAttribute("current_user") == null) {
        // model.addAttribute("user", new User());
        // return "register";
        // } else {
        // return "redirect:/home";
        // }

        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String userRegistration(User user, Model model) {
        if (userService.registerNewUser(user)) {
            logger.info("Successfully register: {}", user.getUsername());
            return "redirect:/login";
        } else {
            logger.warn("Fail to register: {}", user.getUsername());
            model.addAttribute("error_register", "User account existed");
            return "register";
        }
    }

    @GetMapping("/login")
    public String goToLogin(HttpSession session) {
        if (session.getAttribute("current_user") == null) {
            return "login";
        } else {
            return "redirect:/home";
        }
    }

    @PostMapping("/login")
    public String verifyUser(@RequestParam String username, @RequestParam String password, HttpSession session,
            Model model) {
        logger.info("Login attempt for username: {}", username);
        if (userService.verifyUserCredentials(username, password)) {
            long userId = userService.findUserId(username);
            session.setAttribute("current_userId", userId);
            session.setAttribute("current_user", username);
            logger.info("Successful login for username: {}", username);

            return "redirect:/home";
        } else {
            logger.warn("Fail to login for username: {}", username);
            model.addAttribute("error_login", "User account doesn't exists");
            return "login";
        }
    }

    @GetMapping("/home")
    public String userHomePage(HttpSession session, Model model) {
        if (session.getAttribute("current_user") == null) {
            return "redirect:/landing";
        } else {
            String username = (String) session.getAttribute("current_user");
            User user = userService.findVerifiedUser(username);
            model.addAttribute("user", user);
            List<Product> products = productService.findAllAvailableProduct(user.getUserId());
            model.addAttribute("products", products);
            return "home";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        logger.info("Logout for username: {}", session.getAttribute("current_user"));
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/users/{id}")
    public String getUser(@PathVariable("id") long userId, Model model, HttpSession session) {
        if (session.getAttribute("current_user").equals(userService.findUser(userId).getUsername())) {
            User user = userService.findUser(userId);
            model.addAttribute("user", user);
            model.addAttribute("products", user.getProducts());
            model.addAttribute("transactions_bought", user.getBoughtTransactions());
            model.addAttribute("transactions_sold", user.getSoldTransactions());
            return "profile";
        } else {
            model.addAttribute("error", "Unable to access other user");
            return "error";
        }
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

    @PostMapping("/users/update/{userId}")
    public String updateUser(User user, Model model) {
        if (userService.updateUserProfile(user)) {
            model.addAttribute("error_update", "Update successful");
            return "redirect:/users/" + user.getUserId();
        } else {
            logger.warn("Fail to update the user info of {} -- #{}", user.getUsername(), user.getUserId());
            model.addAttribute("error_update", "Update failed");
            return "redirect:/users/" + user.getUserId();
        }
    }
}
