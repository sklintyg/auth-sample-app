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

public class GetConfigResponse {

    private String webcertUrl;

    public String getWebcertUrl() {
        return webcertUrl;
    }

    public void setWebcertUrl(String webcertUrl) {
        this.webcertUrl = webcertUrl;
    }


    public static final class GetConfigResponseBuilder {
        private String webcertUrl;

        private GetConfigResponseBuilder() {
        }

        public static GetConfigResponseBuilder aGetConfigResponse() {
            return new GetConfigResponseBuilder();
        }

        public GetConfigResponseBuilder withWebcertUrl(String webcertUrl) {
            this.webcertUrl = webcertUrl;
            return this;
        }

        public GetConfigResponse build() {
            GetConfigResponse getConfigResponse = new GetConfigResponse();
            getConfigResponse.setWebcertUrl(webcertUrl);
            return getConfigResponse;
        }
    }
}
