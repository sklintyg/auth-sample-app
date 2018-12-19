package se.inera.intyg.authsampleapp.auth;

import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.transport.http.HTTPInTransport;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLAuthenticationToken;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.util.SAMLUtil;

import java.nio.charset.Charset;
import java.util.Base64;

public class SampleSAMLAuthenticationProvider extends SAMLAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication principal = super.authenticate(authentication);
        SAMLAuthenticationToken token = (SAMLAuthenticationToken) authentication;
        SAMLMessageContext context = token.getCredentials();

        if (principal.isAuthenticated()) {
            try {
                // Extract SAMLResponse from inbound message transport
                byte[] samlResponseBase64 = ((HTTPInTransport) context.getInboundMessageTransport()).getParameterValue("SAMLResponse").getBytes(Charset.forName("UTF-8"));

                // Attach to principal
                ((UserModel) principal.getPrincipal()).setSamlAuthentication(samlResponseBase64);

            } catch (Exception e) {
                throw new AuthenticationServiceException(e.getMessage());
            }
        }
        return principal;
    }
}
