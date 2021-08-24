package com.gmail.burinigor7.usersservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.burinigor7.usersservice.dao.RoleRepository;
import com.gmail.burinigor7.usersservice.domain.Role;
import com.gmail.burinigor7.usersservice.exception.RoleNotFoundException;
import com.gmail.burinigor7.usersservice.service.RoleService;
import com.gmail.burinigor7.usersservice.util.RoleModelAssembler;
import org.json.JSONObject;
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

@WebMvcTest(controllers = RoleController.class)
public class RoleControllerTests {
    @MockBean
    private RoleService roleService;

    @MockBean
    private RoleRepository roleRepository; // necessary for roleByTitleConverter bean

    @Autowired
    private MockMvc mockMvc; // fake http requests sending

    @Autowired
    private ObjectMapper objectMapper; // request body serialization

    private RoleModelAssembler roleModelAssembler;

    @TestConfiguration
    static class RoleModelAssemblerBeanConfiguration {
        @Bean
        public RoleModelAssembler roleModelAssembler() {
            return new RoleModelAssembler();
        }
    }

    @BeforeEach
    private void setup(ApplicationContext context) {
        roleModelAssembler = context.getBean(RoleModelAssembler.class);
    }

    @Test
    public void role_whenValidInput_thenReturns_200() throws Exception {
        long roleId = 1L;
        Role returnedByRoleService = new Role(roleId, "Role1");

        when(roleService.role(roleId)).thenReturn(returnedByRoleService);

        MvcResult mvcResult = mockMvc.perform(get("/roles/{roleId}", roleId))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(roleService, times(1)).role(idCaptor.capture());
        assertEquals(idCaptor.getValue(), roleId);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains(payloadOfHalResponse(returnedByRoleService));
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("roles");
    }

    @Test
    public void role_whenInputIsNotExistentRole_thenReturns404() throws Exception {
        long roleId = -1L;

        when(roleService.role(roleId)).thenThrow(RoleNotFoundException.class);

        mockMvc.perform(get("/roles/{roleId}", roleId))
                .andExpect(status().isNotFound())
                .andReturn();

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(roleService, times(1)).role(idCaptor.capture());
        assertEquals(roleId, idCaptor.getValue());
    }

    @Test
    public void all_whenValidInput_thenReturns200() throws Exception {
        List<Role> returnedByRoleService = List.of(new Role(1L, "Role1"),
                new Role(2L, "Role2"));
        when(roleService.all()).thenReturn(returnedByRoleService);

        MvcResult mvcResult = mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andReturn();

        verify(roleService, times(1)).all();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        for(Role role : returnedByRoleService) {
            assertThat(actualResponseBody).contains(payloadOfHalResponse(role));
        }
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("roles");
    }

    @Test
    public void roleByTitle_whenValidInput_thenReturns200() throws Exception {
        String roleTitle = "Role1";
        Role returnedByRoleService = new Role(1L, "Role1");

        when(roleService.roleByTitle(roleTitle)).thenReturn(returnedByRoleService);

        MvcResult mvcResult = mockMvc.perform(get("/roles")
                        .param("title", roleTitle))
                        .andExpect(status().isOk())
                        .andReturn();

        ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
        verify(roleService, times(1)).roleByTitle(titleCaptor.capture());
        assertEquals(titleCaptor.getValue(), roleTitle);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).contains(payloadOfHalResponse(returnedByRoleService));
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("roles");
    }

    @Test
    public void newRole_whenValidInput_thenReturns201() throws Exception {
        Role requestBodyPojo = new Role(null, "Role1");
        Role returnedByRoleService = new Role(1L, "Role1");

        when(roleService.newRole(requestBodyPojo)).thenReturn(returnedByRoleService);

        MvcResult mvcResult = mockMvc.perform(post("/roles")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestBodyPojo)))
                .andExpect(status().isCreated())
                .andReturn();

        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleService, times(1)).newRole(roleCaptor.capture());
        assertEquals(roleCaptor.getValue().getTitle(), requestBodyPojo.getTitle());
        assertNull(roleCaptor.getValue().getId());

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).contains(payloadOfHalResponse(returnedByRoleService));
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("roles");
    }

    @Test
    public void replaceRole_whenValidInput_thenReturns201() throws Exception {
        long replacedRoleId = 1L;
        Role requestBodyPojo = new Role(null, "Role1");
        Role returnedByRoleService = new Role(replacedRoleId, "Role1");

        when(roleService.replaceRole(requestBodyPojo, replacedRoleId)).thenReturn(returnedByRoleService);

        MvcResult mvcResult = mockMvc.perform(put("/roles/{roleId}", replacedRoleId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestBodyPojo)))
                .andExpect(status().isCreated())
                .andReturn();

        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(roleService, times(1)).replaceRole(roleCaptor.capture(), idCaptor.capture());
        assertEquals(requestBodyPojo.getTitle(), roleCaptor.getValue().getTitle());
        assertNull(roleCaptor.getValue().getId());
        assertEquals(replacedRoleId, idCaptor.getValue());

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).contains(payloadOfHalResponse(returnedByRoleService));
        assertThat(actualResponseBody).contains("_links");
        assertThat(actualResponseBody).contains("self");
        assertThat(actualResponseBody).contains("roles");
    }

    @Test
    public void replaceRole_whenNotExistentRoleIdSpecified_thenReturns404() throws Exception {
        long replacedRoleId = -1L;
        Role requestBodyPojo = new Role(null, "NotExistentRole");

        when(roleService.replaceRole(requestBodyPojo, replacedRoleId))
                .thenThrow(RoleNotFoundException.class);

        mockMvc.perform(put("/roles/{roleId}", replacedRoleId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestBodyPojo)))
                .andExpect(status().isNotFound());

        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(roleService, times(1)).replaceRole(roleCaptor.capture(), idCaptor.capture());
        assertEquals(requestBodyPojo.getTitle(), roleCaptor.getValue().getTitle());
        assertNull(roleCaptor.getValue().getId());
        assertEquals(replacedRoleId, idCaptor.getValue());
    }

    @Test
    public void deleteRole_whenValidInput_thenReturns204() throws Exception {
        long deletedRoleId = 1L;

        mockMvc.perform(delete("/roles/{roleId}", deletedRoleId))
                .andExpect(status().isNoContent());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(roleService, times(1)).deleteRole(idCaptor.capture());
        assertEquals(deletedRoleId, idCaptor.getValue());
    }

    @Test
    public void newRole_whenInvalidRoleTitle_thenReturns422() throws Exception {
        Role requestBody = new Role(1L, "a");

        MvcResult mvcResult = mockMvc.perform(post("/roles")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(json);
        jsonObject.getString("title"); // throws exception if 'title' not presented
    }

    @Test
    public void replaceRole_whenInvalidRoleTitle_thenReturns422() throws Exception {
        Role requestBody = new Role(1L, "a");

        MvcResult mvcResult = mockMvc.perform(put("/roles/{roleId}", requestBody.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(json);
        jsonObject.getString("title"); // throws exception if 'title' not presented
    }

    private String payloadOfHalResponse(Role pojo) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(pojo);
        return json.substring(0, json.length() - 1);
    }
}
