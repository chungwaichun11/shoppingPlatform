package fdm.shoppingPlatform.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fdm.shoppingPlatform.model.Transaction;
import fdm.shoppingPlatform.model.User;
import fdm.shoppingPlatform.service.TransactionService;
import fdm.shoppingPlatform.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping("/product/{id}/purchase")
    public String purchaseItem(Transaction transaction, Model model) {
        if (transactionService.createNewTransaction(transaction)) {
            logger.info("{} successfully create the transaction for {} -- #{}", transaction.getBuyer().getUsername(),
                    transaction.getProduct().getProductName(),
                    transaction.getTransactionId());
            return "redirect:/home";
        } else {
            logger.info("Fail to create the transaction for {} -- #{}", transaction.getProduct().getProductName(),
                    transaction.getTransactionId());
            model.addAttribute("error", "Unable to purchase the item");
            return "error";
        }
    }

    @GetMapping("/transaction/{id}")
    public String getTransaction(@PathVariable("id") long transactionId, Model model, HttpSession session) {
        String username = (String) session.getAttribute("current_user");
        User user = userService.findVerifiedUser(username);
        model.addAttribute("user", user);
        Transaction transaction = transactionService.findTransaction(transactionId);
        model.addAttribute("transaction", transaction);
        return "transaction";
    }

    @PostMapping("/transaction/{id}/update")
    public String updateTransaction(Transaction transaction, Model model, HttpSession session) {
        String username = (String) session.getAttribute("current_user");
        User user = userService.findVerifiedUser(username);
        logger.info("{} updating the transaction for Transaction #{} -- {} ",
                user.getUsername(), transaction.getTransactionId(),
                transaction.getProduct().getProductName());
        if (transactionService.updateTransaction(transaction)) {
            return "redirect:/users/" + user.getUserId();
        } else {
            logger.info("Fail to update the transaction for {} -- #{}",
                    transaction.getProduct().getProductName(),
                    transaction.getTransactionId());
            model.addAttribute("error", "Unable to update the transaction");
            return "error";
        }

    }
}
