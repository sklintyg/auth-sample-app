package se.inera.intyg.authsampleapp.service.token;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.Assert.assertTrue;

public class TokenTransformServiceImplTest {

    private TokenTransformServiceImpl testee = new TokenTransformServiceImpl();

  //  @Test
    public void testSAMLBase64ToXml() throws IOException {
        byte[] samlResponseBase64 = IOUtils.toByteArray(new ClassPathResource("working/SAMLResponse.txt").getInputStream());
        byte[] samlResponseXml = IOUtils.toByteArray(new ClassPathResource("working/SAMLResponse_decoded.xml").getInputStream());
        byte[] decoded = Base64.getDecoder().decode(samlResponseBase64);

        boolean equals = Arrays.equals(decoded, samlResponseXml);
        assertTrue(equals);
    }


//    @Test
    public void testSAMLBase64ToXml2() throws IOException {
        byte[] samlResponseBase64BA = IOUtils.toByteArray(new ClassPathResource("working/SAMLResponse.txt").getInputStream());
        byte[] samlResponseXmlBA = IOUtils.toByteArray(new ClassPathResource("working/SAMLResponse_decoded.xml").getInputStream());

        String samlResponseBase64 = new String(samlResponseBase64BA, Charset.forName("UTF-8")); //IOUtils.toString(new ClassPathResource("working/SAMLResponse.txt").getInputStream(), Charset.forName("UTF-8"));
        String samlResponseXml = new String(samlResponseXmlBA, Charset.forName("UTF-8")); //IOUtils.toString(new ClassPathResource("working/SAMLResponse_decoded.xml").getInputStream(), Charset.forName("UTF-8"));
        byte[] decoded = Base64.getDecoder().decode(samlResponseBase64);
        String decodedString = new String(decoded, Charset.forName("UTF-8"));
        String decodedAndFixedLineBreaks = decodedString.replaceAll("\n", "\r\n");

        // After decoding and then manually fixing the line breaks the string read from disk and our deoded
        // version are equal.
        Assert.assertEquals(samlResponseXml, decodedAndFixedLineBreaks);

        byte[] encoded = Base64.getEncoder().encode(decodedAndFixedLineBreaks.getBytes(Charset.forName("UTF-8")));
        // String encodedString = org.apache.commons.codec.binary.Base64.encodeBase64String(decodedAndFixedLineBreaks.getBytes(Charset.forName("UTF-8")));
        byte[] encodeUrl = Base64.getUrlEncoder().encode(decodedAndFixedLineBreaks.getBytes(Charset.forName("UTF-8")));
        Assert.assertEquals(samlResponseBase64, new String(encodeUrl, Charset.forName("UTF-8")));
        //boolean equals = Arrays.equals(decoded, samlResponseXml);
        //assertTrue(equals);
    }

    @Test
    public void testAssertionXmlToBase64() throws IOException {


        //byte[] decodedXml = IOUtils.toByteArray(new ClassPathResource("working/SAMLResponse_decoded.xml").getInputStream());
        byte[] encodedXml = IOUtils.toByteArray(new ClassPathResource("working/Assertion_encoded.txt").getInputStream());

        // Decode to XML
        byte[] decodedAssertion = Base64.getDecoder().decode(encodedXml);
        String decodedXml = new String(decodedAssertion, Charset.forName("UTF-8"));

        byte[] encodedByUsXml = Base64.getEncoder().encode(decodedAssertion);

        boolean equals = Arrays.equals(encodedXml, encodedByUsXml);
        assertTrue(equals);
    }

    @Test
    public void testFullFlow() throws IOException {
        byte[] prefix = "<saml2:Assertion".getBytes(Charset.forName("UTF-8"));
        byte[] suffix = "</saml2:Assertion>".getBytes(Charset.forName("UTF-8"));
        byte[] samlResponseBase64BA = IOUtils.toByteArray(new ClassPathResource("working/SAMLResponse.txt").getInputStream());
        byte[] decodedSamlResponse = Base64.getDecoder().decode(samlResponseBase64BA);

        int first = indexOf(decodedSamlResponse, prefix);
        int last = indexOf(decodedSamlResponse, suffix);
        assertTrue(first > 0);
        assertTrue(first < last);

        byte[] assertion = Arrays.copyOfRange(decodedSamlResponse, first, last + "</saml2:Assertion>".length());
        assertTrue(assertion.length > 0);

        byte[] assertionEncoded = Base64.getEncoder().encode(assertion);

        byte[] encodedOriginalAssertion = IOUtils.toByteArray(new ClassPathResource("working/Assertion_encoded.txt").getInputStream());

        boolean equals = Arrays.equals(assertionEncoded, encodedOriginalAssertion);
        assertTrue(equals);
    }

   // @Test
    public void testFullFlow2() throws IOException {
        byte[] prefix = "<saml2:Assertion".getBytes(Charset.forName("UTF-8"));
        byte[] suffix = "</saml2:Assertion>".getBytes(Charset.forName("UTF-8"));
        byte[] samlResponseBase64BA = IOUtils.toByteArray(new ClassPathResource("eriks/samlresponse.txt").getInputStream());
        byte[] decodedSamlResponse = Base64.getDecoder().decode(samlResponseBase64BA);

        int first = indexOf(decodedSamlResponse, prefix);
        int last = indexOf(decodedSamlResponse, suffix);
        assertTrue(first > 0);
        assertTrue(first < last);

        byte[] assertion = Arrays.copyOfRange(decodedSamlResponse, first, last + "</saml2:Assertion>".length());
        assertTrue(assertion.length > 0);

        byte[] assertionEncoded = Base64.getEncoder().encode(assertion);

        byte[] encodedOriginalAssertion = IOUtils.toByteArray(new ClassPathResource("working/Assertion_encoded.txt").getInputStream());

        boolean equals = Arrays.equals(assertionEncoded, encodedOriginalAssertion);
        assertTrue(equals);
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