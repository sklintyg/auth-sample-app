package se.inera.intyg.authsampleapp.service.token;

public interface TokenExchangeService {

    /**
     * Given a SAML response, exchange the underlying assertion for a JWT token.
     *
     * @param samlResponse
     *      SAML response
     * @return
     */
    String exchange(byte[] samlResponse);
}
