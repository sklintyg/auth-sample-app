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

//    @Test
//    public void testBase64ToString() throws IOException {
//        String base64 = IOUtils.toString(new ClassPathResource("ineradev/assertion.base64").getInputStream());
//        byte[] decodedBytes = Base64.getMimeDecoder().decode(base64.getBytes(Charset.forName("UTF-8")));
//        System.out.println(new String(decodedBytes, Charset.forName("UTF-8")));
//    }
//
//    @Test
//    public void testTransformAssertion() {
//        try {
//            String responseXml = IOUtils.toString(new ClassPathResource("ineradev/assertion-1.xml").getInputStream());
//
//            String encoded = testee.extractUsingRawStringMatcher(responseXml);
//            System.out.println(encoded);
//            encoded = encoded.replaceAll("\\n", "");
//            System.out.println(encoded);
//
//            assertNotNull(encoded);
//            System.out.println(Base64.getUrlEncoder().withoutPadding().encodeToString(encoded.getBytes(Charset.forName("UTF-8"))));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
    @Test
    public void testSAMLBase64ToXml() throws IOException {
        byte[] samlResponseBase64 = IOUtils.toByteArray(new ClassPathResource("working/SAMLResponse.txt").getInputStream());
        byte[] samlResponseXml = IOUtils.toByteArray(new ClassPathResource("working/SAMLResponse_decoded.xml").getInputStream());
        byte[] decoded = Base64.getDecoder().decode(samlResponseBase64);

        boolean equals = Arrays.equals(decoded, samlResponseXml);
        assertTrue(equals);
    }

    @Test
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


        byte[] decodedXml = IOUtils.toByteArray(new ClassPathResource("working/SAMLResponse_decoded.xml").getInputStream());
        byte[] encodedXml = IOUtils.toByteArray(new ClassPathResource("working/Assertion_encoded.txt").getInputStream());

        byte[] encodedByUsXml = Base64.getEncoder().encode(decodedXml);

        boolean equals = Arrays.equals(encodedXml, encodedByUsXml);
        assertTrue(equals);
    }
}
