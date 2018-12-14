package se.inera.intyg.authsampleapp.auth;

public class UserModel {

    private String hsaId;
    private String samlAuthentication;
    private String tokenAuthentication;

    public UserModel(String hsaId) {
        this.hsaId = hsaId;
    }

    public String getHsaId() {
        return hsaId;
    }

    public void setHsaId(String hsaId) {
        this.hsaId = hsaId;
    }

    public String getSamlAuthentication() {
        return samlAuthentication;
    }

    public void setSamlAuthentication(String samlAuthentication) {
        this.samlAuthentication = samlAuthentication;
    }

    public String getTokenAuthentication() {
        return tokenAuthentication;
    }

    public void setTokenAuthentication(String tokenAuthentication) {
        this.tokenAuthentication = tokenAuthentication;
    }
}
