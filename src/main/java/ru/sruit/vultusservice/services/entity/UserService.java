package ru.sruit.vultusservice.services.entity;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.sruit.vultusservice.models.dto.user.UserDto;
import ru.sruit.vultusservice.models.dto.user.UserEditDto;
import ru.sruit.vultusservice.models.entity.User;

public interface UserService extends UserDetailsService {

    User save(User user);

    User saveByDto(UserDto userDto);

    User editUserWithDto(User oldUser, UserEditDto userEditDto);

    void deleteUserByUsername(String username);

}
