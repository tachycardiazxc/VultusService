package ru.sruit.vultusservice.models.response.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginJwtRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}