package net.nemisolv.keycloak;

import net.nemisolv.keycloak.payload.ProfileRequest;
import net.nemisolv.keycloak.payload.ProfileResponse;
import org.springframework.stereotype.Service;

@Service
public class ProfileMapper {
    public Profile toProfile(ProfileRequest request) {
        String buildName = request.firstName() + ((request.lastname()!=null &&!request.lastname().isEmpty()) ? " " + request.lastname() : "");
        return Profile.builder()
                .name(buildName)
                .email(request.email())
                .username(request.username())
                .build();
    }

    public ProfileResponse toProfileResponse(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getEmail(),
                profile.getUsername()
        );
    }
}
