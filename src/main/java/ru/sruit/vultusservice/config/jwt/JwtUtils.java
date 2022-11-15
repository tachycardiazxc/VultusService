package ru.sruit.vultusservice.config.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sruit.vultusservice.models.entity.Role;
import ru.sruit.vultusservice.services.entity.RoleService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public final class JwtUtils {

    private final RoleService roleService;

    public JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    private Set<Role> getRoles(Claims claims) {
        final List<LinkedHashMap<String, String>> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(LinkedHashMap::entrySet)
                .map(x -> {
                    Role[] role = {null};
                    x.forEach(y -> {
                        if (y.getKey().equals("name")) {
                            role[0] = roleService.getRoleByName(y.getValue());
                        }});
                    return role[0];
                })
                .collect(Collectors.toSet());
    }

}
