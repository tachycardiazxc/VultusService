package ru.sruit.vultusservice.controllers.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sruit.vultusservice.config.jwt.JwtAuthentication;
import ru.sruit.vultusservice.models.dto.user.UserProfileDto;
import ru.sruit.vultusservice.models.dto.user.converter.UserDtoConverter;
import ru.sruit.vultusservice.models.entity.Profile;
import ru.sruit.vultusservice.models.response.contoller.Response;
import ru.sruit.vultusservice.services.entity.ProfileService;
import ru.sruit.vultusservice.util.validation.ApiValidationUtils;

@RestController
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
@RequestMapping("/api/user/info")
public class UserInfoController {

    private final ProfileService profileService;

    @Operation(summary = "Get current user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info was found"),
    })
    @GetMapping("/currentUser")
    public Response<UserProfileDto> getCurrentUserInfo() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.getProfileByUserUsername(jwtAuthentication.getUsername());
        return Response.ok(UserDtoConverter.profileEntityToDto(profile));
    }

    @Operation(summary = "Get user info by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info by username was found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "411", description = "Inserted username length is incorrect")
    })
    @GetMapping("/{username}")
    public Response<UserProfileDto> getUserInfoByUsername(@PathVariable("username") String username) {
        ApiValidationUtils.expectedFalse(username.trim().length() < 1,
                411, "Inserted username length less than 1");
        Profile profile = profileService.getProfileByUserUsername(username);
        ApiValidationUtils.expectedNotNull(profile, 404, "User not found");
        return Response.ok(UserDtoConverter.profileEntityToDto(profile));
    }

}
