package se.inera.intyg.authsampleapp.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserModel {

    private String hsaId;
    private byte[] samlAuthentication;
    private String accessToken;
    private String refreshToken;

    public UserModel(String hsaId) {
        this.hsaId = hsaId;
    }

    public String getHsaId() {
        return hsaId;
    }

    public void setHsaId(String hsaId) {
        this.hsaId = hsaId;
    }

    @JsonIgnore
    public byte[] getSamlAuthentication() {
        return samlAuthentication;
    }

    public void setSamlAuthentication(byte[] samlAuthentication) {
        this.samlAuthentication = samlAuthentication;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
