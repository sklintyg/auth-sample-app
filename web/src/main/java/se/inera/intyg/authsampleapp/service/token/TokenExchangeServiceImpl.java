package se.inera.intyg.authsampleapp.service.token;

import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Base64;

@Service
public class TokenExchangeServiceImpl implements TokenExchangeService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenExchangeServiceImpl.class);

    private static final String URL_ENCODED_BEARER_VALUE = "urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Asaml2-bearer";
    private static final String BEARER_VALUE = "urn:ietf:params:oauth:grant-type:saml2-bearer";

    @Value("${token.exchange.endpoint.url}")
    private String tokenExchangeEndpointUrl;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenTransformService tokenTransformService;

    @Override
    public String exchange(String samlAssertion) {

        try {
            String extractedXml = tokenTransformService.extractUsingRawStringMatcher(samlAssertion);
            String base64EncodedAssertion = Base64.getEncoder()
                    .encodeToString(extractedXml.getBytes(Charset.forName("UTF-8")));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("grant_type", BEARER_VALUE);
            map.add("assertion", base64EncodedAssertion);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(tokenExchangeEndpointUrl, request, String.class);
            LOG.info("Response body: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String exchangePreStored() {
        try {
            String assertionBase64 = IOUtils.toString(new ClassPathResource("Assertion_encoded_2.txt").getInputStream());
            String urlEncoded = URLEncoder.encode(assertionBase64, "UTF-8");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("grant_type", BEARER_VALUE);
            map.add("assertion", assertionBase64);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(tokenExchangeEndpointUrl, request, String.class);
            LOG.info("Response body: {}", response.getBody());
            return response.getBody();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
