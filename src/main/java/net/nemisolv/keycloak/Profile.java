package net.nemisolv.keycloak;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profile")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String keycloakId;

    private String name;
    private String email;
    private String username;
}
