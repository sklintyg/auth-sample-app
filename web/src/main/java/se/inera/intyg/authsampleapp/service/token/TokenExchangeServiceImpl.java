package se.inera.intyg.authsampleapp.service.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Base64;

@Service
public class TokenExchangeServiceImpl implements TokenExchangeService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenExchangeServiceImpl.class);

    private static final String BEARER_VALUE = "urn:ietf:params:oauth:grant-type:saml2-bearer";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String GRANT_TYPE = "grant_type";
    private static final String ASSERTION = "assertion";

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
    public String exchange(byte[] samlResponse) {

        try {
            byte[] samlAssertion = tokenTransformService.extractSamlAssertion(samlResponse);
            byte[] samlAssertionAsBase64 = Base64.getEncoder().encode(samlAssertion);
            String assertion = new String(samlAssertionAsBase64, Charset.forName("UTF-8"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add(CLIENT_ID, clientId);
            map.add(CLIENT_SECRET, clientSecret);
            map.add(GRANT_TYPE, BEARER_VALUE);
            map.add(ASSERTION, assertion);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(tokenExchangeEndpointUrl, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
