package fdm.shoppingPlatform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import fdm.shoppingPlatform.model.User;
import fdm.shoppingPlatform.repository.UserRepository;
import fdm.shoppingPlatform.service.UserService;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterNewUser_SuccessfulRegistration() {
        User user = new User("testuser", "password");
        when(userRepositoryMock.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepositoryMock.save(any(User.class))).thenReturn(user);

        boolean result = userService.registerNewUser(user);

        assertEquals(true, result);
    }

    @Test
    public void testRegisterNewUser_UserAlreadyExists() {
        User user = new User("testuser", "password");
        when(userRepositoryMock.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean result = userService.registerNewUser(user);

        assertEquals(false, result);
    }

    @Test
    public void testVerifyUser_ValidCredentials() {
        String username = "testuser";
        String password = "password";
        User user = new User(username, password);
        when(userRepositoryMock.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.verifyUserCredentials(username, password);

        assertEquals(true, result);
    }

    @Test
    public void testVerifyUser_InvalidUsername() {
        String username = "testuser";
        String password = "password";
        when(userRepositoryMock.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = userService.verifyUserCredentials(username, password);

        assertEquals(false, result);
    }

    @Test
    public void testVerifyUser_InvalidPassword() {
        String username = "testuser";
        String password = "password";
        User user = new User(username, "wrongpassword");
        when(userRepositoryMock.findByUsername(username)).thenReturn(Optional.of(user));

        boolean result = userService.verifyUserCredentials(username, password);

        assertEquals(false, result);
    }

}