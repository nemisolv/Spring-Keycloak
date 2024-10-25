package net.nemisolv.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtCustomConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = source.getClaimAsMap(REALM_ACCESS);
        Object roles = realmAccess.get(ROLES);
        if(roles instanceof List stringRoles) {
            log.info("Roles: {}", stringRoles);
            return ((List<String>) stringRoles).stream()
                    .map(role ->new  SimpleGrantedAuthority(ROLE_PREFIX + role))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
