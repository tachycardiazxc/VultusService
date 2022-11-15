package ru.sruit.vultusservice.services.entity.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sruit.vultusservice.config.jwt.JwtAuthentication;
import ru.sruit.vultusservice.config.jwt.JwtProvider;
import ru.sruit.vultusservice.models.entity.User;
import ru.sruit.vultusservice.models.response.jwt.JwtResponse;
import ru.sruit.vultusservice.models.response.jwt.LoginJwtRequest;
import ru.sruit.vultusservice.services.entity.AuthService;
import ru.sruit.vultusservice.services.entity.UserService;

import javax.security.auth.message.AuthException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponse login(@NonNull LoginJwtRequest authRequest) throws AuthException {
        final User user = (User) userService.loadUserByUsername(authRequest.getUsername());
        if (user == null) { throw new AuthException("User not found"); }
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            user.setJwtToken(refreshToken);
            userService.save(user);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Wrong password");
        }
    }

    @Override
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = ((User) userService.loadUserByUsername(username)).getJwtToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = (User) userService.loadUserByUsername(username);
                if (user == null) { throw new AuthException("User not found"); }
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    @Override
    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = ((User) userService.loadUserByUsername(username)).getJwtToken();
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = (User) userService.loadUserByUsername(username);
                if (user == null) { throw new AuthException("User not found"); }
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                user.setJwtToken(newRefreshToken);
                userService.save(user);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Invalid JWT token");
    }

    @Override
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
