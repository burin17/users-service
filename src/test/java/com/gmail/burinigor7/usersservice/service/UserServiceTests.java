package com.gmail.burinigor7.usersservice.service;

import com.gmail.burinigor7.usersservice.dao.UserRepository;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.User;
import com.gmail.burinigor7.usersservice.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void user_existentUser_returnFetchedUser() {
        final long id = 1L; // existent user
        final User fetched = new User();
        fetched.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(fetched));
        User returned = userService.user(id);
        assertEquals(fetched, returned);
    }

    @Test
    public void user_notExistentUser_exceptionThrown() {
        final long id = 2L; // not existent user
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.user(id));
    }

    @Test
    public void all_anyPersistedUsers_returnFetchedUsers() {
        List<User> fetched = List.of(new User());
        when(userRepository.findAll()).thenReturn(fetched);
        Iterable<User> returned = userService.all();
        assertEquals(fetched, returned);
    }

    @Test
    public void userByEmail_existentUser_returnFetchedUser() {
        final String email = "test@email.com"; // existent user's email
        final User fetched = new User();
        fetched.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(fetched));
        User returned = userService.userByEmail(email);
        assertEquals(fetched, returned);
    }

    @Test
    public void userByEmail_notExistentUser_exceptionThrown() {
        final String email = "test@email.com"; // not existent user's email
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.userByEmail(email));
    }

    @Test
    public void userByLogin_existentUser_returnFetchedUser() {
        final String login = "login"; // existent user's login
        final User fetched = new User();
        fetched.setLogin(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(fetched));
        User returned = userService.userByLogin(login);
        assertEquals(fetched, returned);
    }

    @Test
    public void userByLogin_notExistentUser_exceptionThrown() {
        final String email = "login"; // not existent user's login
        when(userRepository.findByLogin(email)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.userByLogin(email));
    }

    @Test
    public void usersByName_existentUserWithSpecifiedLastNameAndPatronymic_returnFetchedUser() {
        final String lastName = "Petrov";
        final String patronymic = "Ivanovich";
        final User fetchedUser = new User();
        fetchedUser.setLastName(lastName);
        fetchedUser.setPatronymic(patronymic);
        final List<User> fetched = List.of(fetchedUser);
        when(userRepository.findAll(Example.of(fetchedUser))).thenReturn(fetched);
        List<User> returned = userService.usersByName(null, lastName, patronymic);
        assertEquals(fetched, returned);
    }

    @Test
    public void usersByRole_existentUserWithSpecifiedRole_returnFetchedUser() {
        final Role role = new Role(1L, "Role1");
        Iterable<User> fetched = List.of(new User(), new User(), new User());
        fetched.forEach(user -> user.setRole(role));
        when(userRepository.findByRole(role)).thenReturn(fetched);
        Iterable<User> returned = userService.usersByRole(role);
        assertEquals(fetched, returned);
    }

    @Test
    public void userByPhoneNumber_existentUser_returnFetchedUser() {
        final String phoneNumber = "89871111111"; // existent user's phoneNumber
        final User fetched = new User();
        fetched.setEmail(phoneNumber);
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(fetched));
        User returned = userService.userByPhoneNumber(phoneNumber);
        assertEquals(fetched, returned);
    }

    @Test
    public void userByPhoneNumber_notExistentUser_exceptionThrown() {
        final String phoneNumber = "89871111111"; // not existent user's phoneNumber
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.userByPhoneNumber(phoneNumber));
    }

    @Test
    public void newUser_persistUser_returnUserWithSpecifiedDataAndWithId() {
        final User newUser = new User(null, "Ivan", "Ivanov", "Petrovich",
                "89871111111", new Role(1L, "User"),
                "test@email.com", "ivanov1");
        final User persistedUser = new User(1L, newUser.getFirstName(), newUser.getLastName(),
                newUser.getPatronymic(), newUser.getPhoneNumber(), newUser.getRole(),
                newUser.getEmail(), newUser.getLogin());
        when(userRepository.save(newUser)).thenReturn(persistedUser);
        User returned = userService.newUser(newUser);
        assertEquals(persistedUser, returned);
        assertNotNull(returned.getId());
    }

    @Test
    public void replaceUser_existentUserReplacement_returnUserWithNewData() {
        final long userId = 1L;
        final String newUserLogin = "NewUserLogin";
        final User persistent = new User();
        persistent.setId(userId);
        persistent.setLogin(newUserLogin);
        final User passed = new User();
        passed.setLogin(newUserLogin);
        when(userRepository.findById(userId)).thenReturn(Optional.of(persistent));
        when(userRepository.save(persistent)).thenReturn(persistent);
        User returned = userService.replaceUser(passed, userId);
        assertEquals(newUserLogin, returned.getLogin());
    }

    @Test
    public void replaceUser_notExistentUserReplacement_exceptionThrown() {
        final long userId = 1L; // not existent user id
        final String newUserLogin = "NewUserLogin";
        final User passed = new User();
        passed.setLogin(newUserLogin);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService
                .replaceUser(passed, userId));
    }
}
