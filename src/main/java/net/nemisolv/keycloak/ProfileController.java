package net.nemisolv.keycloak;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nemisolv.keycloak.payload.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileRepository profileRepository;
    private final ProfileMapper mapper;
    private final IdentityClient client;

    private String clientId = "nemisolv-client";
    private String clientSecret = "weOY4XioC6syk0dLhQgu4GnqprmpcGcw";

    @PostMapping
    public Profile createProfile(@RequestBody ProfileRequest request) {

        var tokenExchange = client.exchangeToken(TokenExchangeRequest.builder()
                .grant_type("client_credentials")
                .client_id(clientId)
                .client_secret(clientSecret)
                .scope("openid")
                .build());

        log.info("Token: {}", tokenExchange.getAccessToken());

        var creationResponse = client.createUser(
                String.format("Bearer %s", tokenExchange.getAccessToken()),
                UserCreationParam.builder()
                        .username(request.username())
                        .firstName(request.firstName())
                        .lastName(request.lastname())

                        .enabled(true)
                        .email(request.email())
                        .emailVerified(false)
                        .credentials(List.of(
                                UserCreationParam.Credential.builder()
                                        .type("password")
                                        .value(request.password())
                                        .temporary(false)
                                        .build()
                        ))
                        .build()

        );

        String userId = extractUserId(creationResponse);


        Profile profile = mapper.toProfile(request);
            profile.setKeycloakId(userId);
        return profileRepository.save(profile);
    }

    @GetMapping("/{id}")
    public ProfileResponse getProfile(@PathVariable Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));


        return mapper.toProfileResponse(profile);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProfileResponse> getProfiles() {
        return profileRepository.findAll().stream()
                .map(mapper::toProfileResponse)
                .collect(Collectors.toList());
    }

    public String extractUserId(@NonNull ResponseEntity response) {
        String locationHeader = Objects.requireNonNull(response.getHeaders().get("Location")).get(0);
        String[] locationParts = locationHeader.split("/");
        return locationParts[locationParts.length - 1];

    }

}
