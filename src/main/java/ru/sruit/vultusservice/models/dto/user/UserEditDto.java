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
@Getter
@Setter
@Builder
public class UserEditDto {

    @NotNull private String oldUsername;
    private String newUsername;
    private String password;
    private Set<RolesEnum> roles;

}
