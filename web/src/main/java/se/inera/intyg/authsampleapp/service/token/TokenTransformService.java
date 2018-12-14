package se.inera.intyg.authsampleapp.service.token;

public interface TokenTransformService {
    String extractUsingRawStringMatcher(String samlMessage);

    String extractAndEncode(String samlMessage);

    String base64Encode(String messageStr);
}
