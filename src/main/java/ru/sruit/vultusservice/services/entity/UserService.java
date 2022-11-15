package ru.sruit.vultusservice.services.entity;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.sruit.vultusservice.models.entity.User;

public interface UserService extends UserDetailsService {

    User save(User user);

}
