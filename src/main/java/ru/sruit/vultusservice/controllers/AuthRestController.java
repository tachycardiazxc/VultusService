package ru.sruit.vultusservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sruit.vultusservice.models.response.contoller.Response;
import ru.sruit.vultusservice.models.response.jwt.JwtResponse;
import ru.sruit.vultusservice.models.response.jwt.LoginJwtRequest;
import ru.sruit.vultusservice.models.response.jwt.RefreshJwtRequest;
import ru.sruit.vultusservice.services.entity.AuthService;

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    public Response<JwtResponse> login(@RequestBody LoginJwtRequest authRequest) throws AuthException {
        final JwtResponse token = authService.login(authRequest);
        return Response.ok(token);
    }

    @PostMapping("/token")
    public Response<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return Response.ok(token);
    }

    @PostMapping("/refresh")
    public Response<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return Response.ok(token);
    }
}
