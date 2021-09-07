package com.gmail.burinigor7.userscrudservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.burinigor7.userscrudservice.domain.Role;
import com.gmail.burinigor7.userscrudservice.domain.Status;
import com.gmail.burinigor7.userscrudservice.domain.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class EndToEndTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String authorizationHeaderValue;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void endToEndScenario() throws Exception {
        authorizationHeaderValue = getAuthorizationHeader();

        // check current user; '/self' url
        isAdminCurrentUser();

        // create, pull and assert role1
        createRole("Role1");
        JSONObject roleJsonObject = getRoleJsonObjectByTitle("Role1");
        Role role1 = new Role(roleJsonObject.getLong("id"), roleJsonObject.getString("title"));
        assertEquals("Role1", role1.getTitle());

        // create, pull and assert user with role1
        User user = new User(null, "Ivan", "Ivanov", "Petrovich",
                "+79871111111", role1,
                "test@email.com", "ivanov1", "user", Status.ACTIVE);
        createUser(user);
        JSONObject userJsonObjectWithRole1 = getUserJsonObjectByLogin("ivanov1");
        user.setId(userJsonObjectWithRole1.getLong("id"));
        assertSingleUser(user, userJsonObjectWithRole1);

        // create, pull and assert role2
        createRole("Role2");
        JSONObject role2JsonObject = getRoleJsonObjectByTitle("Role2");
        Role role2 = new Role(role2JsonObject.getLong("id"), role2JsonObject.getString("title"));
        assertEquals("Role2", role2.getTitle());

        // check count of roles
        assertEquals(4, countOfRoles());

        // replace user's role
        user.setRole(role2);
        replaceUser(user);
        JSONObject userJsonObjectWithRole2= getUserJsonObjectByLogin(user.getLogin());
        assertSingleUser(user, userJsonObjectWithRole2);

        // check count of users
        assertEquals(2, countOfUsers());

        // delete user
        deleteUser(user.getId());

        // check count of users
        assertEquals(1, countOfUsers());
    }

    private String getAuthorizationHeader() throws Exception {
        MvcResult tokenMvcResult = mockMvc.perform(post("/auth/login")
                        /*.contentType("application/json")
                        .content(objectMapper.writeValueAsString(
                                new AuthenticationRequestDto("admin", "admin")))*/)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonObject =
                new JSONObject(tokenMvcResult.getResponse().getContentAsString());
        return "Bearer_" + jsonObject.getString("token");
    }

    private void assertSingleUser(User user, JSONObject jsonObject) throws JSONException {
        assertEquals(user.getEmail(), jsonObject.getString("email"));
        assertEquals(user.getLogin(), jsonObject.getString("login"));
        assertEquals(user.getPhoneNumber(), jsonObject.getString("phoneNumber"));
        assertEquals(user.getLastName(), jsonObject.getString("lastName"));
        assertEquals(user.getFirstName(), jsonObject.getString("firstName"));
        assertEquals(user.getPatronymic(), jsonObject.getString("patronymic"));
        assertEquals(user.getRole().getTitle(),
                jsonObject.getJSONObject("role").getString("title"));
        assertTrue(passwordEncoder.matches(user.getPassword(),
                jsonObject.getString("password")));
        assertEquals(user.getStatus().toString(), jsonObject.getString("status"));
        jsonObject.getLong("id"); // exception thrown is id not presented
        assertNotNull(jsonObject.getJSONObject("_links").getJSONObject("self"));
        assertNotNull(jsonObject.getJSONObject("_links").getJSONObject("users"));
    }

    private void isAdminCurrentUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users/self")
                        .header("Authorization", authorizationHeaderValue))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(json);
        assertEquals(1L, jsonObject.getLong("id"));
    }

    private void createRole(String roleTitle) throws Exception {
        Role role = new Role(null, roleTitle);
        mockMvc.perform(post("/roles")
                        .header("Authorization", authorizationHeaderValue)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated());
    }

    private void createUser(User user) throws Exception {
        mockMvc.perform(post("/users")
                        .header("Authorization", authorizationHeaderValue)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    private JSONObject getUserJsonObjectByLogin(String login) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .header("Authorization", authorizationHeaderValue)
                        .param("login", login))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        return new JSONObject(json);
    }

    private JSONObject getRoleJsonObjectByTitle(String title) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/roles")
                        .header("Authorization", authorizationHeaderValue)
                        .param("title", title))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        return new JSONObject(json);
    }

    private void replaceUser(User user) throws Exception {
        mockMvc.perform(put("/users/{userId}", user.getId())
                        .header("Authorization", authorizationHeaderValue)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    private int countOfUsers() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .header("Authorization", authorizationHeaderValue))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        try {
            return jsonObject.getJSONObject("_embedded").getJSONArray("userList")
                    .length();
        } catch (JSONException e) {
            return 0;
        }
    }

    private int countOfRoles() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/roles")
                        .header("Authorization", authorizationHeaderValue))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        try {
            return jsonObject.getJSONObject("_embedded").getJSONArray("roleList")
                    .length();
        } catch (JSONException e) {
            return 0;
        }
    }

    private void deleteUser(long id) throws Exception{
        mockMvc.perform(delete("/users/{userId}", id)
                        .header("Authorization", authorizationHeaderValue))
                .andExpect(status().isNoContent());
    }
}
