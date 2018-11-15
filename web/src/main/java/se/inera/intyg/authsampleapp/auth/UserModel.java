package se.inera.intyg.authsampleapp.auth;

public class UserModel {

    private String hsaId;

    public UserModel(String hsaId) {
        this.hsaId = hsaId;
    }

    public String getHsaId() {
        return hsaId;
    }

    public void setHsaId(String hsaId) {
        this.hsaId = hsaId;
    }
}
