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

import org.opensaml.ws.transport.http.HTTPInTransport;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLAuthenticationToken;
import org.springframework.security.saml.context.SAMLMessageContext;

import java.nio.charset.Charset;

public class SampleSAMLAuthenticationProvider extends SAMLAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication principal = super.authenticate(authentication);

        if (authentication instanceof SAMLAuthenticationToken) {
            SAMLAuthenticationToken token = (SAMLAuthenticationToken) authentication;
            SAMLMessageContext context = token.getCredentials();

            if (principal.isAuthenticated()) {
                try {
                    // Extract SAMLResponse from inbound message transport
                    byte[] samlResponseBase64 = ((HTTPInTransport) context.getInboundMessageTransport()).getParameterValue("SAMLResponse")
                            .getBytes(Charset.forName("UTF-8"));

                    // Attach to principal
                    ((UserModel) principal.getPrincipal()).setSamlAuthentication(samlResponseBase64);

                } catch (Exception e) {
                    throw new AuthenticationServiceException(e.getMessage());
                }
            }
            return principal;
        }

        throw new AuthenticationServiceException("Cannot authenticate, provided Authentication class is not a SAMLAuthenticationToken");
    }
}
