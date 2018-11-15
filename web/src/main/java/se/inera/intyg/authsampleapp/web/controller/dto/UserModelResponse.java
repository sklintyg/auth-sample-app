package se.inera.intyg.authsampleapp.web.controller.dto;

import se.inera.intyg.authsampleapp.auth.UserModel;

public class UserModelResponse {

    private UserModel userModel;
    private boolean authenticated;

    public UserModelResponse(UserModel userModel, boolean authenticated) {
        this.userModel = userModel;
        this.authenticated = authenticated;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
