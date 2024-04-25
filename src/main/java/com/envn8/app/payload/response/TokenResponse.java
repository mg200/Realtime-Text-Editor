package com.envn8.app.payload.response;

public class TokenResponse {
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
