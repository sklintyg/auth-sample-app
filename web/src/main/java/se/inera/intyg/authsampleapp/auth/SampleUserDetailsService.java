package se.inera.intyg.authsampleapp.auth;

import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.security.saml.util.SAMLUtil;

public class SampleUserDetailsService implements SAMLUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(SampleUserDetailsService.class);

    @Override
    public Object loadUserBySAML(SAMLCredential credential) {
        LOG.info("User authentication was successful. SAML credential is " + credential);

        SakerhetstjanstAssertion sakerhetstjanstAssertion = new SakerhetstjanstAssertion(credential.getAuthenticationAssertion());
        return new UserModel(sakerhetstjanstAssertion.getHsaId());
    }
}
