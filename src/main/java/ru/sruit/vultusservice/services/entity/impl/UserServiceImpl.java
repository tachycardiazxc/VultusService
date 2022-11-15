package ru.sruit.vultusservice.services.entity.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sruit.vultusservice.models.entity.User;
import ru.sruit.vultusservice.repositories.UserRepository;
import ru.sruit.vultusservice.services.entity.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
