package ru.sruit.vultusservice.config.jwt;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer ";
    private Long id;
    private String username;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String email, List<String> roles) {
        this.token = type + accessToken;
        this.id = id;
        this.username = email;
        this.roles = roles;
    }

}
