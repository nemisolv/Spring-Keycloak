package net.nemisolv.keycloak;

import feign.QueryMap;
import net.nemisolv.keycloak.payload.TokenExchange;
import net.nemisolv.keycloak.payload.TokenExchangeRequest;
import net.nemisolv.keycloak.payload.UserCreationParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "identity-service", url = "http://localhost:8082")
public interface IdentityClient {
    @PostMapping(value = "/realms/nemisolv/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchange exchangeToken(@QueryMap TokenExchangeRequest param);

    @PostMapping(value = "/admin/realms/nemisolv/users")
     ResponseEntity<?> createUser(@RequestHeader("Authorization") String token, @RequestBody UserCreationParam param);

}
