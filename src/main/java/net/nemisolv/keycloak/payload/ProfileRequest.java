package net.nemisolv.keycloak.payload;

public record ProfileRequest(
        String firstName,
        String lastname,
        String password,
        String email,
        String username
) {
}
