package net.nemisolv.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@SpringBootApplication
@EnableFeignClients
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringKeycloakApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringKeycloakApplication.class, args);
    }

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());

        http.authorizeHttpRequests(req -> req.requestMatchers("/profile/**").permitAll().anyRequest().authenticated());

        http.csrf(AbstractHttpConfigurer::disable);

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .jwt(Customizer.withDefaults())
        )
                ;
        http.sessionManagement(ss -> ss.sessionCreationPolicy(STATELESS))
                ;

        return http.build();
    }
    @Bean
    public JwtAuthenticationConverter  jwtAuthenticationConverter(){
        JwtAuthenticationConverter jwtAuthenticationConverter  = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtCustomConverter());

        return jwtAuthenticationConverter;
    }

}
