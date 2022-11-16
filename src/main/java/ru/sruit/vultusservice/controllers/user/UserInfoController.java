package ru.sruit.vultusservice.controllers.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sruit.vultusservice.config.jwt.JwtAuthentication;
import ru.sruit.vultusservice.models.dto.user.UserProfileDto;
import ru.sruit.vultusservice.models.dto.user.converter.UserDtoConverter;
import ru.sruit.vultusservice.models.entity.Profile;
import ru.sruit.vultusservice.models.response.contoller.Response;
import ru.sruit.vultusservice.services.entity.ProfileService;

@RestController
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserInfoController {

    private final ProfileService profileService;

    @GetMapping("/info")
    public Response<UserProfileDto> getCurrentUserInfo() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Profile profile = profileService.getProfileByUserUsername(jwtAuthentication.getUsername());
        return Response.ok(UserDtoConverter.profileEntityToDto(profile));
    }

}
