package com.senla.app.model.dto.request;

public class RefreshTokenRequest {

    private String requestToken;

    public RefreshTokenRequest() {
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}
