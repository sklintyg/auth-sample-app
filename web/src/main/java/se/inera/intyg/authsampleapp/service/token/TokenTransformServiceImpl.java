package se.inera.intyg.authsampleapp.service.token;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;

@Component
public class TokenTransformServiceImpl implements TokenTransformService {

    private static final byte[] ASSERTION_PREFIX = "<saml2:Assertion".getBytes(Charset.forName("UTF-8"));
    private static final byte[] ASSERTION_SUFFIX = "</saml2:Assertion>".getBytes(Charset.forName("UTF-8"));

    @Override
    public byte[] extractFromByteArray(byte[] samlAuthentication) {


        byte[] decodedSamlResponse = Base64.getDecoder().decode(samlAuthentication);

        int first = indexOf(decodedSamlResponse, ASSERTION_PREFIX);
        int last = indexOf(decodedSamlResponse, ASSERTION_SUFFIX);
        return Arrays.copyOfRange(decodedSamlResponse, first, last + "</saml2:Assertion>".length());
    }

    private int indexOf(byte[] outerArray, byte[] smallerArray) {
        for(int i = 0; i < outerArray.length - smallerArray.length+1; ++i) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i+j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;
    }
}
