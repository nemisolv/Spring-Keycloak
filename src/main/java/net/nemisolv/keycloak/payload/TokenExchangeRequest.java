package net.nemisolv.keycloak.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenExchangeRequest {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String scope;
}
