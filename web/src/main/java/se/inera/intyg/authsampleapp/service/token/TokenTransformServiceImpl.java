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
package se.inera.intyg.authsampleapp.service.token;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;

/**
 * Class responseible for extracting a saml2:Assertion from a SAML response.
 *
 * Operates directly on byte arrays in order to not in any way modify the contents of the extracted Assertion as that
 * could break signature validations.
 */
@Component
public class TokenTransformServiceImpl implements TokenTransformService {

    private static final byte[] ASSERTION_PREFIX = "<saml2:Assertion".getBytes(Charset.forName("UTF-8"));
    private static final byte[] ASSERTION_SUFFIX = "</saml2:Assertion>".getBytes(Charset.forName("UTF-8"));

    @Override
    public byte[] extractSamlAssertion(byte[] samlResponse) {

        byte[] decodedSamlResponse = Base64.getDecoder().decode(samlResponse);

        int first = indexOf(decodedSamlResponse, ASSERTION_PREFIX);
        int last = indexOf(decodedSamlResponse, ASSERTION_SUFFIX);
        return Arrays.copyOfRange(decodedSamlResponse, first, last + "</saml2:Assertion>".length());
    }

    /**
     * Finds the first index of a given sequence of bytes in a byte array.
     *
     * @param payload
     *          Byte array to search.
     * @param searchSequence
     *          Sequence of bytes to find in payload.
     * @return
     *          Index of the first byte of a sequence of bytes in payload matching the searchSequence argument.
     */
    private int indexOf(byte[] payload, byte[] searchSequence) {
        for (int i = 0; i < payload.length - searchSequence.length + 1; ++i) {
            boolean found = true;
            for (int j = 0; j < searchSequence.length; ++j) {
                if (payload[i + j] != searchSequence[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }
}
