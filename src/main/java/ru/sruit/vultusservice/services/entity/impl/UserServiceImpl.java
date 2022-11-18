package ru.sruit.vultusservice.services.entity.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sruit.vultusservice.enums.RolesEnum;
import ru.sruit.vultusservice.models.dto.user.UserDto;
import ru.sruit.vultusservice.models.dto.user.UserEditDto;
import ru.sruit.vultusservice.models.entity.Role;
import ru.sruit.vultusservice.models.entity.User;
import ru.sruit.vultusservice.repositories.UserRepository;
import ru.sruit.vultusservice.services.entity.RoleService;
import ru.sruit.vultusservice.services.entity.UserService;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public User save(User user) {
        if (!user.getPassword().equals(this.loadUserByUsername(user.getUsername()).getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public User saveByDto(UserDto userDto) {
        Set<Role> roles = new HashSet<>();
        userDto.getRoles().forEach(rolesEnum -> roles.add(roleService.getRoleByName(rolesEnum.name())));
        return this.save(new User(userDto.getUsername(), userDto.getPassword(), roles));
    }

    @Override
    public User editUserWithDto(User oldUser, UserEditDto userEditDto) {
        String newUsername = userEditDto.getNewUsername();
        String newPassword = userEditDto.getPassword();
        Set<RolesEnum> newRoles = userEditDto.getRoles();

        Set<Role> roles = new HashSet<>();

        if (newUsername != null) {
            oldUser.setUsername(newUsername);
        }
        if (newPassword != null) {
            oldUser.setPassword(newPassword);
        }
        if (newRoles != null && newRoles.size() > 0) {
            newRoles.forEach(rolesEnum -> roles.add(roleService.getRoleByName(rolesEnum.name())));
            oldUser.setRoles(roles);
        }

        return this.save(oldUser);
    }

    @Override
    @Transactional
    public void deleteUserByUsername(String username) {
        userRepository.deleteUserByUsername(username);
    }
}
