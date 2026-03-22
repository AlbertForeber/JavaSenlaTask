package com.senla.app.model.dto.response;

public class AuthenticationResponse {

    private final String accessToken;
    private final String refreshToken;
    private final String tokenType = "Bearer";

    public AuthenticationResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
