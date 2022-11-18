package ru.sruit.vultusservice.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sruit.vultusservice.enums.RolesEnum;
import ru.sruit.vultusservice.models.dto.user.UserDto;
import ru.sruit.vultusservice.models.dto.user.UserEditDto;
import ru.sruit.vultusservice.models.entity.User;
import ru.sruit.vultusservice.models.response.contoller.Response;
import ru.sruit.vultusservice.services.entity.RoleService;
import ru.sruit.vultusservice.services.entity.UserService;
import ru.sruit.vultusservice.util.validation.ApiValidationUtils;

import java.util.Set;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Operation(summary = "Create an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
            @ApiResponse(responseCode = "411", description = "Username or password length is incorrect"),
            @ApiResponse(responseCode = "417", description = "User roles are not provided")
    })
    @PostMapping("/create")
    public Response<Void> createUser(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String pwd = userDto.getPassword();
        Set<RolesEnum> roles = userDto.getRoles();

        ApiValidationUtils.expectedTrue(pwd.trim().length() >= 10,
                411, "Password length less than 10");
        ApiValidationUtils.expectedNull(userService.loadUserByUsername(username),
                409, "User already exists");
        ApiValidationUtils.expectedTrue(username.trim().length() >= 4,
                411, "Username length less than 10");
        ApiValidationUtils.expectedNotNull(roles, 417, "User roles are not provided");
        ApiValidationUtils.expectedTrue(roles.size() > 0, 417, "User roles are not provided");
        roles.forEach(role -> ApiValidationUtils.expectedNotNull(roleService.getRoleByName(role.name()),
                404, String.format("%s role not found", role.name())));

        userService.saveByDto(userDto);
        return Response.ok();
    }

    @Operation(summary = "Edit an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User edited successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Username already taken"),
            @ApiResponse(responseCode = "411", description = "Username or password length is incorrect"),
    })
    @PostMapping("/edit")
    public Response<Void> editUser(@RequestBody UserEditDto userEditDto) {
        String newUsername = userEditDto.getNewUsername();
        String pwd = userEditDto.getPassword();
        Set<RolesEnum> roles = userEditDto.getRoles();

        User oldUser = (User) userService.loadUserByUsername(userEditDto.getOldUsername());

        ApiValidationUtils.expectedNotNull(oldUser, 404, "User not found");
        if (newUsername != null) {
            ApiValidationUtils.expectedTrue(newUsername.trim().length() >= 4,
                    411, "Username length less than 10");
            ApiValidationUtils.expectedNull(userService.loadUserByUsername(newUsername),
                    409, "Username already taken");
        }
        if (pwd != null) {
            ApiValidationUtils.expectedTrue(pwd.trim().length() >= 10,
                    411, "Password length less than 10");
        }
        if (roles != null) {
            roles.forEach(role -> ApiValidationUtils.expectedNotNull(roleService.getRoleByName(role.name()),
                    404, String.format("%s role not found", role.name())));
        }

        userService.editUserWithDto(oldUser, userEditDto);

        return Response.ok();
    }

    @Operation(summary = "Delete an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    @PostMapping("/delete")
    public Response<Void> deleteUserByUsername(@RequestParam("username") String username) {
        ApiValidationUtils.expectedNotNull(userService.loadUserByUsername(username),
                404, "User not found");

        userService.deleteUserByUsername(username);

        return Response.ok();
    }

}
