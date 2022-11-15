package ru.sruit.vultusservice.services.entity;

import ru.sruit.vultusservice.config.jwt.JwtAuthentication;
import ru.sruit.vultusservice.models.response.jwt.JwtResponse;
import ru.sruit.vultusservice.models.response.jwt.LoginJwtRequest;

import javax.security.auth.message.AuthException;
import javax.validation.constraints.NotNull;

public interface AuthService {

    JwtResponse login(@NotNull LoginJwtRequest authRequest) throws AuthException;

    JwtResponse getAccessToken(@NotNull String refreshToken) throws AuthException;

    JwtResponse refresh(@NotNull String refreshToken) throws AuthException;

    JwtAuthentication getAuthInfo();

}
