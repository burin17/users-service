package com.gmail.burinigor7.usersservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.User;
import com.gmail.burinigor7.usersservice.exception.UserNotFoundException;
import com.gmail.burinigor7.usersservice.service.UserService;
import com.gmail.burinigor7.usersservice.util.RoleByTitleConverter;
import com.gmail.burinigor7.usersservice.util.UserModelAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {
    @MockBean
    private UserService userService;

    @MockBean
    private RoleByTitleConverter roleByTitleConverter;

    @Autowired
    private MockMvc mockMvc; // fake http requests sending

    @Autowired
    private ObjectMapper objectMapper; // request body serialization

    private UserModelAssembler userModelAssembler;

    @TestConfiguration
    static class UserModelAssemblerBeanConfiguration {
        @Bean
        public UserModelAssembler userModelAssembler() {
            return new UserModelAssembler();
        }
    }

    @BeforeEach
    private void setup(ApplicationContext context) {
        userModelAssembler = context.getBean(UserModelAssembler.class);
    }

    @Test
    public void user_whenValidInput_thenReturns_200() throws Exception {
        long userId = 1L;
        final User returnedByUserService = new User(null, "Ivan", "Ivanov", "Petrovich",
                "89871111111", new Role(1L, "User"),
                "test@email.com", "ivanov1");

        when(userService.user(userId)).thenReturn(returnedByUserService);

        MvcResult mvcResult = mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userService, times(1)).user(idCaptor.capture());
        assertEquals(idCaptor.getValue(), userId);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains(payloadOfHalResponse(returnedByUserService));
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("users");
    }

    @Test
    public void user_whenInputIsNotExistentRole_thenReturns404() throws Exception {
        long userId = -1L;

        when(userService.user(userId)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isNotFound())
                .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userService, times(1)).user(idCaptor.capture());
        assertEquals(userId, idCaptor.getValue());
    }

    @Test
    public void all_whenValidInput_thenReturns200() throws Exception {
        List<User> returnedByRoleService = List.of(
                new User(1L, "Ivan", "Ivanov", "Petrovich",
                        "89871111111", new Role(1L, "User"),
                        "test@email.com", "ivanov1"),
                new User(2L, "Ivan", "Ivanov", "Petrovich",
                        "89871111112", new Role(1L, "User"),
                        "test2@email.com", "ivanov2")
        );
        when(userService.all()).thenReturn(returnedByRoleService);

        MvcResult mvcResult = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService, times(1)).all();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        for(User user : returnedByRoleService) {
            assertThat(actualResponseBody).contains(payloadOfHalResponse(user));
        }
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("users");
    }

    @Test
    public void userByEmail_whenValidInput_thenReturns200() throws Exception {
        String userEmail = "test@email.com";
        User returnedByUserService = new User(1L, "Ivan", "Ivanov", "Petrovich",
                "89871111111", new Role(1L, "User"),
                "test@email.com", "ivanov1");

        when(userService.userByEmail(userEmail)).thenReturn(returnedByUserService);

        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .param("email", userEmail))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
        verify(userService, times(1)).userByEmail(titleCaptor.capture());
        assertEquals(titleCaptor.getValue(), userEmail);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains(payloadOfHalResponse(returnedByUserService));
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("users");
    }

    @Test
    public void userByLogin_whenValidInput_thenReturns200() throws Exception {
        String userLogin = "ivanov1";
        User returnedByUserService = new User(1L, "Ivan", "Ivanov", "Petrovich",
                "89871111111", new Role(1L, "User"),
                "test@email.com", userLogin);

        when(userService.userByLogin(userLogin)).thenReturn(returnedByUserService);

        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .param("login", userLogin))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
        verify(userService, times(1)).userByLogin(titleCaptor.capture());
        assertEquals(titleCaptor.getValue(), userLogin);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains(payloadOfHalResponse(returnedByUserService));
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("users");
    }

    @Test
    public void userByName_whenValidInput_thenReturns200() throws Exception {
        String firstName = "Ivan";
        String lastName = "Ivanov";
        String patronymic = "Petrovich";
        List<User> returnedByUserService = List.of(new User(1L, firstName, lastName, patronymic,
                "89871111111", new Role(1L, "User"),
                "test@email.com", "ivanov1"));

        when(userService.usersByName(firstName, lastName, patronymic)).thenReturn(returnedByUserService);

        MvcResult mvcResult = mockMvc.perform(get("/users/filter-by-name")
                        .param("first-name", firstName)
                        .param("last-name", lastName)
                        .param("patronymic", patronymic))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> firstNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> lastNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> patronymicCaptor = ArgumentCaptor.forClass(String.class);
        verify(userService, times(1)).usersByName(firstNameCaptor.capture(),
                lastNameCaptor.capture(), patronymicCaptor.capture());
        assertEquals(firstName, firstNameCaptor.getValue());
        assertEquals(lastName, lastNameCaptor.getValue());
        assertEquals(patronymic, patronymicCaptor.getValue());

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        for(User user : returnedByUserService) {
            assertThat(actualResponseBody).contains(payloadOfHalResponse(user));
        }
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("users");
    }

    @Test
    public void userByRole_whenValidInput_thenReturns200() throws Exception {
        Role userRole = new Role(1L, "User");
        List<User> returnedByUserService = List.of(new User(1L, "Ivan", "Ivanov", "Petrovich",
                "89871111111", userRole,
                "test@email.com", "ivanov1"));

        when(userService.usersByRole(userRole)).thenReturn(returnedByUserService);
        when(roleByTitleConverter.convert(userRole.getTitle())).thenReturn(userRole);

        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .param("role", userRole.getTitle()))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        verify(userService, times(1)).usersByRole(roleCaptor.capture());
        assertEquals(roleCaptor.getValue(), userRole);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        for(User user : returnedByUserService) {
            assertThat(actualResponseBody).contains(payloadOfHalResponse(user));
        }
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("users");
    }

    @Test
    public void userByPhoneNumber_whenValidInput_thenReturns200() throws Exception {
        String phoneNumber = "89871111111";
        User returnedByUserService = new User(1L, "Ivan", "Ivanov", "Petrovich",
                phoneNumber, new Role(1L, "User"),
                "test@email.com", "ivanov1");

        when(userService.userByPhoneNumber(phoneNumber)).thenReturn(returnedByUserService);

        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .param("phone-number", phoneNumber))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<String> phoneNumberCaptor = ArgumentCaptor.forClass(String.class);
        verify(userService, times(1)).userByPhoneNumber(phoneNumberCaptor.capture());
        assertEquals(phoneNumberCaptor.getValue(), phoneNumber);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains(payloadOfHalResponse(returnedByUserService));
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("users");
    }

    @Test
    public void newUser_whenValidInput_thenReturns201() throws Exception {
        User requestBodyPojo = new User(null, "Ivan", "Ivanov", "Petrovich",
                "89871111111", new Role(1L, "User"),
                "test@email.com", "ivanov1");
        User returnedByRoleService = new User(1L, requestBodyPojo.getFirstName(),
                requestBodyPojo.getLastName(), requestBodyPojo.getPatronymic(),
                requestBodyPojo.getPhoneNumber(), requestBodyPojo.getRole(),
                requestBodyPojo.getEmail(), requestBodyPojo.getLogin());

        when(userService.newUser(requestBodyPojo)).thenReturn(returnedByRoleService);

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestBodyPojo)))
                .andExpect(status().isCreated())
                .andReturn();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).newUser(userCaptor.capture());
        assertEquals(requestBodyPojo.getFirstName(), userCaptor.getValue().getFirstName());
        assertEquals(requestBodyPojo.getLastName(), userCaptor.getValue().getLastName());
        assertEquals(requestBodyPojo.getPatronymic(), userCaptor.getValue().getPatronymic());
        assertEquals(requestBodyPojo.getLogin(), userCaptor.getValue().getLogin());
        assertEquals(requestBodyPojo.getEmail(), userCaptor.getValue().getEmail());
        assertEquals(requestBodyPojo.getPhoneNumber(), userCaptor.getValue().getPhoneNumber());
        assertEquals(requestBodyPojo.getRole(), userCaptor.getValue().getRole());
        assertNull(userCaptor.getValue().getId());

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).contains(payloadOfHalResponse(returnedByRoleService));
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("users");
    }

    @Test
    public void replaceUser_whenValidInput_thenReturns201() throws Exception {
        long replacedUserId = 1L;
        User requestBodyPojo = new User(null, "Ivan", "Ivanov", "Petrovich",
                "89871111111", new Role(1L, "User"),
                "test@email.com", "ivanov1");
        User returnedByRoleService = new User(replacedUserId, requestBodyPojo.getFirstName(),
                requestBodyPojo.getLastName(), requestBodyPojo.getPatronymic(),
                requestBodyPojo.getPhoneNumber(), requestBodyPojo.getRole(),
                requestBodyPojo.getEmail(), requestBodyPojo.getLogin());

        when(userService.replaceUser(requestBodyPojo, replacedUserId))
                .thenReturn(returnedByRoleService);

        MvcResult mvcResult = mockMvc.perform(put("/users/{userId}", replacedUserId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestBodyPojo)))
                .andExpect(status().isCreated())
                .andReturn();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userService, times(1))
                .replaceUser(userCaptor.capture(), idCaptor.capture());
        assertEquals(requestBodyPojo.getFirstName(), userCaptor.getValue().getFirstName());
        assertEquals(requestBodyPojo.getLastName(), userCaptor.getValue().getLastName());
        assertEquals(requestBodyPojo.getPatronymic(), userCaptor.getValue().getPatronymic());
        assertEquals(requestBodyPojo.getLogin(), userCaptor.getValue().getLogin());
        assertEquals(requestBodyPojo.getEmail(), userCaptor.getValue().getEmail());
        assertEquals(requestBodyPojo.getPhoneNumber(), userCaptor.getValue().getPhoneNumber());
        assertEquals(requestBodyPojo.getRole(), userCaptor.getValue().getRole());
        assertNull(userCaptor.getValue().getId());
        assertEquals(replacedUserId, idCaptor.getValue());

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains(payloadOfHalResponse(returnedByRoleService));
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("users");
    }

    @Test
    public void replaceUser_whenNotExistentRoleIdSpecified_thenReturns404() throws Exception {
        long replacedUserId = -1L;
        User requestBodyPojo = new User(null, "Ivan", "Ivanov", "Petrovich",
                "89871111111", new Role(1L, "User"),
                "test@email.com", "ivanov1");

        when(userService.replaceUser(requestBodyPojo, replacedUserId))
                .thenThrow(UserNotFoundException.class);

        mockMvc.perform(put("/users/{userId}", replacedUserId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestBodyPojo)))
                .andExpect(status().isNotFound())
                .andReturn();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userService, times(1))
                .replaceUser(userCaptor.capture(), idCaptor.capture());
        assertEquals(requestBodyPojo.getFirstName(), userCaptor.getValue().getFirstName());
        assertEquals(requestBodyPojo.getLastName(), userCaptor.getValue().getLastName());
        assertEquals(requestBodyPojo.getPatronymic(), userCaptor.getValue().getPatronymic());
        assertEquals(requestBodyPojo.getLogin(), userCaptor.getValue().getLogin());
        assertEquals(requestBodyPojo.getEmail(), userCaptor.getValue().getEmail());
        assertEquals(requestBodyPojo.getPhoneNumber(), userCaptor.getValue().getPhoneNumber());
        assertEquals(requestBodyPojo.getRole(), userCaptor.getValue().getRole());
        assertNull(userCaptor.getValue().getId());
        assertEquals(replacedUserId, idCaptor.getValue());
    }

    @Test
    public void deleteRole_whenValidInput_thenReturns204() throws Exception {
        long deleteUserId = 1L;

        mockMvc.perform(delete("/users/{userId}", deleteUserId))
                .andExpect(status().isNoContent());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userService, times(1)).deleteUser(idCaptor.capture());
        assertEquals(deleteUserId, idCaptor.getValue());
    }

    private String payloadOfHalResponse(User pojo) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(pojo);
        return json.substring(0, json.length() - 1);
    }
}
