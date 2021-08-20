package com.gmail.burinigor7.usersservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.domain.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @TestConfiguration
    static class PostgreSQLContainerConfig {
        @Value("${testcontainer.postgresql.image}")
        private String imageName;

        @Value("${testcontainer.postgresql.database}")
        private String databaseName;

        @Value("${spring.datasource.username}")
        private String username;

        @Value("${spring.datasource.password}")
        private String password;

        @Bean
        public PostgreSQLContainer postgreSQLContainer() {
            return new PostgreSQLContainer(imageName)
                    .withDatabaseName(databaseName)
                    .withUsername(username)
                    .withPassword(password);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getUserById() throws Exception {
        // create, pull and assert role1
        createRole("Role1");
        JSONObject roleJsonObject = getRoleJsonObjectByTitle("Role1");
        Role role1 = new Role(roleJsonObject.getLong("id"), roleJsonObject.getString("title"));
        assertEquals("Role1", role1.getTitle());

        // create, pull and assert user with role1
        User user = new User(null, "Ivan", "Ivanov", "Petrovich",
                "+79871111111", role1,
                "test@email.com", "ivanov1");
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
        assertEquals(2, countOfRoles());

        // replace user's role
        user.setRole(role2);
        replaceUser(user);
        JSONObject userJsonObjectWithRole2= getUserJsonObjectByLogin(user.getLogin());
        assertSingleUser(user, userJsonObjectWithRole2);

        // check count of users
        assertEquals(1, countOfUsers());

        // delete user
        deleteUser(user.getId());

        // check count of users
        assertEquals(0, countOfUsers());
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
        jsonObject.getLong("id"); // exception thrown is id not presented
        assertNotNull(jsonObject.getJSONObject("_links").getJSONObject("self"));
        assertNotNull(jsonObject.getJSONObject("_links").getJSONObject("users"));
    }

    private void createRole(String roleTitle) throws Exception {
        Role role = new Role(null, roleTitle);
        mockMvc.perform(post("/roles")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated());
    }

    private void createUser(User user) throws Exception {
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    private JSONObject getUserJsonObjectByLogin(String login) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .param("login", login))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        return new JSONObject(json);
    }

    private JSONObject getRoleJsonObjectByTitle(String title) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/roles")
                        .param("title", title))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        return new JSONObject(json);
    }

    private void replaceUser(User user) throws Exception {
        mockMvc.perform(put("/users/{userId}", user.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    private int countOfUsers() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/users"))
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
        MvcResult mvcResult = mockMvc.perform(get("/roles"))
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
        mockMvc.perform(delete("/users/{userId}", id))
                .andExpect(status().isNoContent());
    }
}