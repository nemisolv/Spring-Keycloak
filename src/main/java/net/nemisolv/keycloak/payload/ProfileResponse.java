package net.nemisolv.keycloak.payload;

public record ProfileResponse(
        Long id,
        String name,
        String email,
        String username
) {
}
