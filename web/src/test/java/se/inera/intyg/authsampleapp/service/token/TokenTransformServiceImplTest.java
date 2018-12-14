package se.inera.intyg.authsampleapp.service.token;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class TokenTransformServiceImplTest {

    private TokenTransformServiceImpl testee = new TokenTransformServiceImpl();

    @Test
    public void testBase64ToString() throws IOException {
        String base64 = IOUtils.toString(new ClassPathResource("ineradev/assertion.base64").getInputStream());
        byte[] decodedBytes = Base64.getMimeDecoder().decode(base64.getBytes(Charset.forName("UTF-8")));
        System.out.println(new String(decodedBytes, Charset.forName("UTF-8")));
    }

    @Test
    public void testTransformAssertion() {
        try {
            String responseXml = IOUtils.toString(new ClassPathResource("ineradev/assertion-1.xml").getInputStream());

            String encoded = testee.extractUsingRawStringMatcher(responseXml);
            System.out.println(encoded);
            encoded = encoded.replaceAll("\\n", "");
            System.out.println(encoded);

            assertNotNull(encoded);
            System.out.println(Base64.getUrlEncoder().withoutPadding().encodeToString(encoded.getBytes(Charset.forName("UTF-8"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
    @Test
    public void testEncodeBase64Url() throws IOException {
        String samlMessage = IOUtils.toString(new ClassPathResource("grim/response-decoded.xml").getInputStream());
        String assertionXml = testee.extractUsingRawStringMatcher(samlMessage);
        String base64UrlEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(assertionXml.getBytes(Charset.forName("UTF-8")));
        String base64Encoded = Base64.getEncoder().withoutPadding().encodeToString(assertionXml.getBytes(Charset.forName("UTF-8")));
        assertNotEquals(base64UrlEncoded, base64Encoded);
        System.out.println(base64UrlEncoded);
        System.out.println(base64Encoded);
    }

    @Test
    public void test2Way() throws IOException {
        String base64 = IOUtils.toString(new ClassPathResource("ineradev/rawresponse_nosamlresponse_key.txt").getInputStream());
        byte[] xml = Base64.getUrlDecoder().decode(base64);
        String data = new String(xml, Charset.forName("UTF-8"));
        System.out.println(data);
    }
}
