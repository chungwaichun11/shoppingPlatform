package fdm.shoppingPlatform.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fdm.shoppingPlatform.model.User;
import fdm.shoppingPlatform.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    Logger logger = LogManager.getLogger(UserService.class);

    public boolean registerNewUser(User user) {
        try {
            Optional<User> userOptional = userRepo.findByUsername(user.getUsername());

            if (userOptional.isEmpty()) {
                userRepo.save(user);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to register a new user: {}", e.getMessage());
            return false;
        }
    }

    public User findUser(long userId) {
        try {
            Optional<User> userOptional = userRepo.findById(userId);
            return userOptional.orElse(null);
        } catch (Exception e) {
            logger.error("Failed to find a user with ID {}: {}", userId, e.getMessage());
            return null;
        }
    }

    public User findVerifiedUser(String username) {
        try {
            return userRepo.findByUsername(username).orElse(null);
        } catch (Exception e) {
            logger.error("Failed to find a user with username {}: {}", username, e.getMessage());
            return null;
        }
    }

    public long findUserId(String username) {
        try {
            Optional<User> userOptional = userRepo.findByUsername(username);
            return userOptional.orElse(null).getUserId();
        } catch (Exception e) {
            logger.error("Failed to find a user with username {}: {}", username, e.getMessage());
            return -1;
        }
    }

    public List<User> findAllUsers() {
        try {
            return userRepo.findAll();
        } catch (Exception e) {
            logger.error("Failed to retrieve all users: {}", e.getMessage());
            return null;
        }
    }

    public boolean verifyUserCredentials(String username, String password) {
        try {
            Optional<User> userOptional = userRepo.findByUsername(username);

            if (userOptional.isEmpty()) {
                return false;
            } else {
                return userOptional.get().getPassword().equals(password);
            }
        } catch (Exception e) {
            logger.error("Failed to verify user credentials for username {}: {}", username, e.getMessage());
            return false;
        }
    }

    public boolean updateUserProfile(User updatedUser) {
        try {
            Optional<User> userOptional = userRepo.findByUsername(updatedUser.getUsername());

            if (userOptional.isPresent()) {
                userRepo.save(updatedUser);
                logger.info("Successfully update the user info of {}:", userOptional.get().getUsername());
                logger.info("(New)Email: {}, Address: {}", updatedUser.getEmail(), updatedUser.getAddress());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to update the user profile for username {}: {}", updatedUser.getUsername(),
                    e.getMessage());
            return false;
        }
    }
}