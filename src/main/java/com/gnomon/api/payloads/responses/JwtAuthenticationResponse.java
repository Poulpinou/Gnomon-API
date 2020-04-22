package com.gnomon.api.payloads.responses;

import lombok.Data;
import lombok.NonNull;

@Data
public class JwtAuthenticationResponse {
	@NonNull
    private String accessToken;
    
    private String tokenType = "Bearer";
}
