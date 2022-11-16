package ru.sruit.vultusservice.controller.user;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sruit.vultusservice.models.entity.Profile;
import ru.sruit.vultusservice.models.entity.Role;
import ru.sruit.vultusservice.models.entity.User;
import ru.sruit.vultusservice.repositories.ProfileRepository;
import ru.sruit.vultusservice.repositories.RoleRepository;
import ru.sruit.vultusservice.repositories.UserRepository;
import ru.sruit.vultusservice.util.ContextIT;

import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.sruit.vultusservice.util.DateFormatter.DATE_FORMATTER;

public class UserInfoControllerIT extends ContextIT {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    Role initRole(String name) {
        return roleRepository.save(new Role(name));
    }

    User initUser(String username, String password, Set<Role> roleSet) {
        return userRepository.save(new User(
                username,
                passwordEncoder.encode(password),
                roleSet
        ));
    }

    Profile initUserProfile(User user, String firstName, String lastName,
                            String surname, String phone, LocalDate birthday,
                            String position, String subdivision, String workPhone,
                            int workPlace, String description, String statusName,
                            String statusText, boolean superBusy, String photoUrl
    ) {
        return profileRepository.save(new Profile(
                user,
                firstName,
                lastName,
                surname,
                phone,
                birthday,
                position,
                subdivision,
                workPhone,
                workPlace,
                description,
                statusName,
                statusText,
                superBusy,
                photoUrl
        ));
    }

    @AfterEach
    void clear() {
        profileRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    private int intInRange(int startBorder, int endBorder) {
        Random random = new Random(System.nanoTime());
        return startBorder + random.nextInt(endBorder - startBorder);
    }

    @Test
    public void getCurrentUserInfoTest() throws Exception {
        Role adminRole = initRole("ADMIN");
        Role userRole = initRole("USER");

        User adminUser = initUser("adminUser", "adminPwd", Set.of(adminRole, userRole));

        Profile adminProfile = initUserProfile(
                adminUser,
                "adminFirstName",
                "adminLastName",
                "adminSurname",
                "adminPhone",
                LocalDate.now().minusYears(intInRange(18, 50)),
                "adminPosition",
                "adminSubdivision",
                "adminWorkPhone",
                intInRange(1, 5),
                "adminDesc",
                "adminStatusName",
                "adminStatusText",
                intInRange(0, 1) == 1,
                "adminPhotoUrl"
        );

        accessToken = tokenUtil.obtainNewAccessToken("adminUser", "adminPwd", mockMvc);

        mockMvc.perform(get("/api/user/info")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", Is.is(200)))
                .andExpect(jsonPath("$.data.firstName", Is.is(adminProfile.getFirstName())))
                .andExpect(jsonPath("$.data.lastName", Is.is(adminProfile.getLastName())))
                .andExpect(jsonPath("$.data.surname", Is.is(adminProfile.getSurname())))
                .andExpect(jsonPath("$.data.phone", Is.is(adminProfile.getPhone())))
                .andExpect(jsonPath("$.data.birthday", Is.is(adminProfile.getBirthday().format(DATE_FORMATTER))))
                .andExpect(jsonPath("$.data.position", Is.is(adminProfile.getPosition())))
                .andExpect(jsonPath("$.data.subdivision", Is.is(adminProfile.getSubdivision())))
                .andExpect(jsonPath("$.data.workPhone", Is.is(adminProfile.getWorkPhone())))
                .andExpect(jsonPath("$.data.workPlace", Is.is(adminProfile.getWorkPlace())))
                .andExpect(jsonPath("$.data.description", Is.is(adminProfile.getDescription())))
                .andExpect(jsonPath("$.data.statusName", Is.is(adminProfile.getStatusName())))
                .andExpect(jsonPath("$.data.statusText", Is.is(adminProfile.getStatusText())))
                .andExpect(jsonPath("$.data.superBusy", Is.is(adminProfile.isSuperBusy())))
                .andExpect(jsonPath("$.data.photoUrl", Is.is(adminProfile.getPhotoUrl())))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

}
