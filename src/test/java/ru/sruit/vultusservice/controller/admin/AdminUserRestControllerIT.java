package ru.sruit.vultusservice.controller.admin;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sruit.vultusservice.enums.RolesEnum;
import ru.sruit.vultusservice.models.dto.user.UserDto;
import ru.sruit.vultusservice.models.dto.user.UserEditDto;
import ru.sruit.vultusservice.models.entity.Role;
import ru.sruit.vultusservice.models.entity.User;
import ru.sruit.vultusservice.repositories.RoleRepository;
import ru.sruit.vultusservice.repositories.UserRepository;
import ru.sruit.vultusservice.util.ContextIT;

import javax.persistence.NoResultException;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminUserRestControllerIT extends ContextIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    User initUser(String username, String password, Set<Role> roles) {
        return userRepository.save(new User(
                username,
                passwordEncoder.encode(password),
                roles
        ));
    }

    Role initRole(String name) {
        return roleRepository.save(new Role(name));
    }

    @AfterEach
    void clear() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void createUserTest() throws Exception {
        Role roleAdmin = initRole("ADMIN");
        Role userRole = initRole("USER");

        initUser("admin", "pwd", Set.of(roleAdmin, userRole));

        accessToken = tokenUtil.obtainNewAccessToken("admin", "pwd", mockMvc);

        UserDto userDto = new UserDto("userUsername", "userPwd000", Set.of(RolesEnum.USER));

        mockMvc.perform(post("/api/admin/user/create")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", Is.is(200)))
                .andExpect(jsonPath("$.data", IsNull.nullValue()))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        mockMvc.perform(post("/api/admin/user/create")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", Is.is(409)))
                .andExpect(jsonPath("$.data", IsNull.nullValue()))
                .andExpect(jsonPath("$.message", Is.is("User already exists")))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        UserDto userDtoIncorrectPwd = new UserDto("userUsername", "userPwd", Set.of(RolesEnum.USER));

        mockMvc.perform(post("/api/admin/user/create")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoIncorrectPwd))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", Is.is(411)))
                .andExpect(jsonPath("$.data", IsNull.nullValue()))
                .andExpect(jsonPath("$.message", Is.is("Password length less than 10")))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        UserDto userDtoRolesIsNull = new UserDto("userUsername1", "userPwd000", null);

        mockMvc.perform(post("/api/admin/user/create")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRolesIsNull))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", Is.is(417)))
                .andExpect(jsonPath("$.data", IsNull.nullValue()))
                .andExpect(jsonPath("$.message", Is.is("User roles are not provided")))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        User userQry = entityManager.createQuery("SELECT u " +
                        "FROM User u " +
                        "JOIN FETCH u.roles " +
                        "WHERE u.username = :username", User.class)
                .setParameter("username", "userUsername")
                .getSingleResult();

        Assertions.assertEquals(userQry.getUsername(), userDto.getUsername());

    }

    @Test
    public void editUserTest() throws Exception {
        Role adminRole = initRole("ADMIN");
        Role userRole = initRole("USER");

        initUser("adminUsername", "adminPwd", Set.of(adminRole, userRole));
        initUser("userUsername", "userPwd", Set.of(userRole));

        accessToken = tokenUtil.obtainNewAccessToken("adminUsername", "adminPwd", mockMvc);

        UserEditDto userEditDto = new UserEditDto("userUsername",
                "newUserUsername",
                null,
                Set.of(RolesEnum.USER, RolesEnum.ADMIN));

        mockMvc.perform(post("/api/admin/user/edit")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEditDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", Is.is(200)))
                .andExpect(jsonPath("$.data", IsNull.nullValue()))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        UserEditDto userNotFoundEditDto = new UserEditDto("notExistingUsername",
                "newUserUsername",
                null,
                Set.of(RolesEnum.USER, RolesEnum.ADMIN));

        mockMvc.perform(post("/api/admin/user/edit")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userNotFoundEditDto))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", Is.is(404)))
                .andExpect(jsonPath("$.data", IsNull.nullValue()))
                .andExpect(jsonPath("$.message", Is.is("User not found")))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        initUser("existingUsername", "userPwd1", Set.of(userRole));
        UserEditDto userWithExistingUsernameEditDto = new UserEditDto("newUserUsername",
                "existingUsername",
                null,
                Set.of(RolesEnum.USER, RolesEnum.ADMIN));

        mockMvc.perform(post("/api/admin/user/edit")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithExistingUsernameEditDto))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", Is.is(409)))
                .andExpect(jsonPath("$.data", IsNull.nullValue()))
                .andExpect(jsonPath("$.message", Is.is("Username already taken")))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        UserEditDto userEditDtoIncorrectPwdLength = new UserEditDto("newUserUsername",
                "newUserUsername1",
                "123",
                Set.of(RolesEnum.USER, RolesEnum.ADMIN));

        mockMvc.perform(post("/api/admin/user/edit")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEditDtoIncorrectPwdLength))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", Is.is(411)))
                .andExpect(jsonPath("$.data", IsNull.nullValue()))
                .andExpect(jsonPath("$.message", Is.is("Password length less than 10")))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        User editedRemovedUserQry;
        try {
            editedRemovedUserQry = entityManager.createQuery("SELECT u " +
                            "FROM User u " +
                            "WHERE u.username = :username", User.class)
                    .setParameter("username", "userUsername")
                    .getSingleResult();
        } catch (NoResultException ignored) {
            editedRemovedUserQry = null;
        }

        User editedSavedUserQry = entityManager.createQuery("SELECT u " +
                        "FROM User u " +
                        "WHERE u.username = :username", User.class)
                .setParameter("username", "newUserUsername")
                .getSingleResult();

        Assertions.assertNull(editedRemovedUserQry);
        Assertions.assertNotNull(editedSavedUserQry);
    }

    @Test
    public void deleteUserByUsernameTest() throws Exception {
        Role adminRole = initRole("ADMIN");
        Role userRole = initRole("USER");

        initUser("adminUsername", "adminPwd", Set.of(adminRole, userRole));
        initUser("userUsername", "userPwd", Set.of(userRole));

        accessToken = tokenUtil.obtainNewAccessToken("adminUsername", "adminPwd", mockMvc);

        mockMvc.perform(post("/api/admin/user/delete?username={username}", "userUsername")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", Is.is(200)))
                .andExpect(jsonPath("$.data", IsNull.nullValue()))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        mockMvc.perform(post("/api/admin/user/delete?username={username}", "notExistingUser")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code", Is.is(404)))
                .andExpect(jsonPath("$.data", IsNull.nullValue()))
                .andExpect(jsonPath("$.message", Is.is("User not found")))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        User userQry;
        try {
            userQry = entityManager.createQuery("SELECT u " +
                            "FROM User u" +
                            " WHERE u.username = :username", User.class)
                    .setParameter("username", "userUsername")
                    .getSingleResult();
        } catch (NoResultException ignored) {
            userQry = null;
        }

        Assertions.assertNull(userQry);

    }

}
