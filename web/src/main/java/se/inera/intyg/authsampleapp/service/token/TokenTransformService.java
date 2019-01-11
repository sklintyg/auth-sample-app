package se.inera.intyg.authsampleapp.service.token;

public interface TokenTransformService {

    /**
     * Given a SAML Response XML document, this methos extracts the Assertion element and its contents exactly "as-is".
     *
     * @param samlResponse
     *      A SAML Response XML document as a byte-array. Base64 encoded.
     * @return
     *      The extracted SAML assertion as a byte-array.
     */
    byte[] extractSamlAssertion(byte[] samlResponse);

}
