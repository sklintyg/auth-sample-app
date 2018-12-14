package se.inera.intyg.authsampleapp.auth;

import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLAuthenticationToken;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.util.SAMLUtil;

public class SampleSAMLAuthenticationProvider extends SAMLAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication principal = super.authenticate(authentication);
        SAMLAuthenticationToken token = (SAMLAuthenticationToken) authentication;
        SAMLMessageContext context = token.getCredentials();

        if (principal.isAuthenticated()) {
            try {
                String messageStr = XMLHelper.nodeToString(SAMLUtil.marshallMessage(context.getInboundSAMLMessage()));

                // Attach to principal
                ((UserModel) principal.getPrincipal()).setSamlAuthentication(messageStr);

            } catch (MessageEncodingException e) {
                throw new AuthenticationServiceException(e.getMessage());
            }
        }
        return principal;
    }
}
