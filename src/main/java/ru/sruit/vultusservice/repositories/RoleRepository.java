package ru.sruit.vultusservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sruit.vultusservice.models.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleByName(String name);

}
