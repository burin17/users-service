package com.gmail.burinigor7.userscrudservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.burinigor7.userscrudservice.domain.Role;
import com.gmail.burinigor7.userscrudservice.domain.Status;
import com.gmail.burinigor7.userscrudservice.domain.User;
import com.gmail.burinigor7.userscrudservice.dto.AuthenticationRequestDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SecurityTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql("classpath:db/security_tests.sql")
    public void testAdminRole() throws Exception {
        String authHeader = getAuthorizationHeader("admin");

        MvcResult mvcResult = mockMvc.perform(get("/users/{id}", 1L)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        assertResponseForAdminRole(new JSONObject(json));

        User user = new User(null, "Ivan", "Ivanov", "Petrovich",
                "+79871111111", new Role(2L, null),
                "test@email.com", "ivanov1", "user", Status.ACTIVE);

        mockMvc.perform(post("/users")
                        .header("Authorization", authHeader)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    @Sql("classpath:db/security_tests.sql")
    public void testUserRole() throws Exception {
        String authHeader = getAuthorizationHeader("user");

        MvcResult mvcResult = mockMvc.perform(get("/users/{id}", 1L)
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        assertResponseForUserRole(new JSONObject(json));

        User user = new User(null, "Ivan", "Ivanov", "Petrovich",
                "+79871111111", new Role(2L, null),
                "test@email.com", "ivanov1", "user", Status.ACTIVE);

        mockMvc.perform(post("/users")
                        .header("Authorization", authHeader)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isForbidden());
    }

    private String getAuthorizationHeader(String username) throws Exception {
        MvcResult tokenMvcResult = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(
                                new AuthenticationRequestDto(username, username)))) // login and password equals in test environment
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonObject =
                new JSONObject(tokenMvcResult.getResponse().getContentAsString());
        return "Bearer_" + jsonObject.getString("token");
    }

    private void assertResponseForAdminRole(JSONObject jsonObject) throws JSONException {
        jsonObject.getJSONObject("role");
        jsonObject.getString("status");
        jsonObject.getLong("id");
        jsonObject.getString("password");
    }

    private void assertResponseForUserRole(JSONObject jsonObject) throws JSONException {
        assertThrows(JSONException.class, () -> jsonObject.getJSONObject("role"));
        assertThrows(JSONException.class, () -> jsonObject.getString("status"));
        assertThrows(JSONException.class, () -> jsonObject.getLong("id"));
        assertThrows(JSONException.class, () -> jsonObject.getString("password"));
    }
}
