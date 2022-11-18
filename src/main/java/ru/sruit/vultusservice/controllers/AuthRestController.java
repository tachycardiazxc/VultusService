package ru.sruit.vultusservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get JWT token by username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token and refresh token successfully generated")
    })
    @PostMapping("/login")
    public Response<JwtResponse> login(@RequestBody LoginJwtRequest authRequest) throws AuthException {
        final JwtResponse token = authService.login(authRequest);
        return Response.ok(token);
    }

    @Operation(summary = "Get new access token by refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token successfully regenerated")
    })
    @PostMapping("/token")
    public Response<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return Response.ok(token);
    }

    @Operation(summary = "Get new access and refresh token by refresh token and access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access and refresh token successfully regenerated")
    })
    @PostMapping("/refresh")
    public Response<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return Response.ok(token);
    }
}
