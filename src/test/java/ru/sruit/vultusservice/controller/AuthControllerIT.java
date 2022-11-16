package ru.sruit.vultusservice.controller;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import ru.sruit.vultusservice.models.entity.Role;
import ru.sruit.vultusservice.models.entity.User;
import ru.sruit.vultusservice.models.response.jwt.LoginJwtRequest;
import ru.sruit.vultusservice.models.response.jwt.RefreshJwtRequest;
import ru.sruit.vultusservice.repositories.RoleRepository;
import ru.sruit.vultusservice.repositories.UserRepository;
import ru.sruit.vultusservice.util.ContextIT;

import java.util.LinkedHashMap;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// FIXME: 15.11.2022 тесты работают раз через раз
public class AuthControllerIT extends ContextIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JacksonJsonParser jsonParser;

    Role initRole(String name) {
        return roleRepository.save(new Role(name));
    }

    User initUser(String username, String password, Set<Role> roleSet) {
        return userRepository.save(new User(
                username, passwordEncoder.encode(password), roleSet
        ));
    }

    @AfterEach
    void clear() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void loginTest() throws Exception {
        Role roleAdmin = initRole("ADMIN");
        Role roleUser = initRole("USER");

        initUser("adminUsername", "adminPwd", Set.of(roleAdmin, roleUser));

        mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(new LoginJwtRequest("adminUsername", "adminPwd")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", Is.is(200)))
//                .andDo(x -> System.out.println(x.getResponse().getContentAsString()))
        ;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getNewAccessTokenTest() throws Exception {
        Role roleAdmin = initRole("ADMIN");
        Role roleUser = initRole("USER");

        initUser("adminUsername", "adminPwd", Set.of(roleAdmin, roleUser));

        RefreshJwtRequest request = new RefreshJwtRequest();

        MvcResult response =
                mockMvc.perform(post("/api/auth/login")
                                .content(objectMapper.writeValueAsString(new LoginJwtRequest("adminUsername", "adminPwd")))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>)
                jsonParser.parseMap(response.getResponse().getContentAsString())
                        .get("data");

        String[] accessToken = {null};
        String[] refreshToken = {null};

        linkedHashMap.forEach((key, value) -> {
            switch (key) {
                case "accessToken": {
                    accessToken[0] = value;
                }
                case "refreshToken": {
                    refreshToken[0] = value;
                }
            }
        });

        request.setRefreshToken(refreshToken[0]);

        mockMvc.perform(post("/api/auth/token")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken", IsNot.not(accessToken[0])))
                .andDo(x -> System.out.println(x.getResponse().getContentAsString()))
        ;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getNewRefreshTokenTest() throws Exception {
        Role roleAdmin = initRole("ADMIN");
        Role roleUser = initRole("USER");

        initUser("adminUsername", "adminPwd", Set.of(roleAdmin, roleUser));

        RefreshJwtRequest request = new RefreshJwtRequest();

        MvcResult response =
                mockMvc.perform(post("/api/auth/login")
                                .content(objectMapper.writeValueAsString(new LoginJwtRequest("adminUsername", "adminPwd")))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>)
                jsonParser.parseMap(response.getResponse().getContentAsString())
                        .get("data");

        String[] accessToken = {null};
        String[] refreshToken = {null};

        linkedHashMap.forEach((key, value) -> {
            switch (key) {
                case "accessToken": {
                    accessToken[0] = value;
                }
                case "refreshToken": {
                    refreshToken[0] = value;
                }
            }
        });

        request.setRefreshToken(refreshToken[0]);

        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", "Bearer " + accessToken[0])
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.refreshToken", IsNot.not(refreshToken[0])))
                .andExpect(jsonPath("$.data.accessToken", IsNot.not(accessToken[0])))
                .andDo(x -> System.out.println(x.getResponse().getContentAsString()))
        ;
    }

}
