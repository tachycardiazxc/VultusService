package ru.sruit.vultusservice.services.entity.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sruit.vultusservice.models.entity.Role;
import ru.sruit.vultusservice.repositories.RoleRepository;
import ru.sruit.vultusservice.services.entity.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }
}
