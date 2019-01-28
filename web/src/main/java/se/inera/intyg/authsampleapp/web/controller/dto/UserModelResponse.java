/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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


    public static final class UserModelResponseBuilder {
        private UserModel userModel;
        private boolean authenticated;

        private UserModelResponseBuilder() {
        }

        public static UserModelResponseBuilder anUserModelResponse() {
            return new UserModelResponseBuilder();
        }

        public UserModelResponseBuilder withUserModel(UserModel userModel) {
            this.userModel = userModel;
            return this;
        }

        public UserModelResponseBuilder withAuthenticated(boolean authenticated) {
            this.authenticated = authenticated;
            return this;
        }

        public UserModelResponse build() {
            return new UserModelResponse(userModel, authenticated);
        }
    }
}
