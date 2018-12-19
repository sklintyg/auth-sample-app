package se.inera.intyg.authsampleapp.service.token;

public interface TokenTransformService {

    byte[] extractFromByteArray(byte[] samlAuthentication);

}
