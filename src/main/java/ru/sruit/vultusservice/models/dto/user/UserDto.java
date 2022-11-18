package ru.sruit.vultusservice.models.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.sruit.vultusservice.enums.RolesEnum;

import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {

    @NotNull private String username;
    @NotNull private String password;
    @NotNull private Set<RolesEnum> roles;

}
