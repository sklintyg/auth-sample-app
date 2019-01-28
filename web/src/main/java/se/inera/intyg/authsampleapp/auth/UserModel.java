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
package se.inera.intyg.authsampleapp.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Example of a User Principal carrying the HSA-id identifying the user as well as the original SAML response as well
 * as OAuth access- and refresh tokens.
 */
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
