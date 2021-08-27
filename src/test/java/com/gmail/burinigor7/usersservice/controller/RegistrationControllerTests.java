package com.gmail.burinigor7.usersservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.burinigor7.usersservice.domain.User;
import com.gmail.burinigor7.usersservice.dto.RegistrationDto;
import com.gmail.burinigor7.usersservice.security.JwtTokenProvider;
import com.gmail.burinigor7.usersservice.service.UserService;
import com.gmail.burinigor7.usersservice.util.RoleByTitleConverter;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationControllerTests {
    @MockBean
    private UserService userService;

    @MockBean
    private RoleByTitleConverter roleByTitleConverter;

    @MockBean
    private JwtTokenProvider jwtTokenProvider; // necessary for jwtSecurityConfig bean

    @Autowired
    private MockMvc mockMvc; // fake http requests sending

    @Autowired
    private ObjectMapper objectMapper; // request body serialization

    @Test
    public void register_whenValidInput_thenReturns201() throws Exception {
        RegistrationDto requestBodyDto = new RegistrationDto();
        requestBodyDto.setLogin("login");
        requestBodyDto.setEmail("email@email.com");
        requestBodyDto.setPassword("pass");
        requestBodyDto.setFirstName("firstName");
        requestBodyDto.setLastName("lastName");
        requestBodyDto.setPhoneNumber("+70000000000");

        User returnedByUserService = requestBodyDto.toOrdinaryUser();
        returnedByUserService.setId(2L);

        when(userService.newUser(requestBodyDto.toOrdinaryUser()))
                .thenReturn(returnedByUserService);

        MvcResult mvcResult = mockMvc.perform(post("/registration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestBodyDto)))
                .andExpect(status().isCreated())
                .andReturn();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).newUser(userCaptor.capture());
        assertEquals(requestBodyDto.toOrdinaryUser(), userCaptor.getValue());

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(jsonResponse);

        assertResponseBody(jsonObject, returnedByUserService);
    }

    @Test
    public void register_whenValidationNotPassed_thenReturns422() throws Exception {
        RegistrationDto requestBodyDto = new RegistrationDto();
        requestBodyDto.setLogin("login");
        requestBodyDto.setEmail("email");
        requestBodyDto.setPassword("pass");
        requestBodyDto.setFirstName("firstName");
        requestBodyDto.setLastName("lastName");
        requestBodyDto.setPhoneNumber("+70000000000");

        mockMvc.perform(post("/registration")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestBodyDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    private void assertResponseBody(JSONObject jsonObject, User returnedByUserService)
            throws JSONException {
        assertEquals("null", jsonObject.getString("password"));
        assertEquals(returnedByUserService.getLogin(), jsonObject.getString("login"));
        assertEquals(returnedByUserService.getEmail(), jsonObject.getString("email"));
        assertEquals(returnedByUserService.getRole().getId(),
                jsonObject.getJSONObject("role").getLong("id"));
        assertEquals("null", jsonObject.getString("patronymic"));
        assertEquals(returnedByUserService.getFirstName(), jsonObject.getString("firstName"));
        assertEquals(returnedByUserService.getLastName(), jsonObject.getString("lastName"));
        assertEquals(returnedByUserService.getPhoneNumber(), jsonObject.getString("phoneNumber"));
        assertEquals(returnedByUserService.getStatus().toString(),
                jsonObject.getString("status"));
    }
}
