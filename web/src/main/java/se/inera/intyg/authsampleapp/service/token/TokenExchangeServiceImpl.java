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

import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.util.SAMLUtil;
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
    private static final String RESRESH_TOKEN = "refresh_token";

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
    public String exchange2(SAMLCredential samlCredential) {

        try {

            String assertionString = XMLHelper.nodeToString(SAMLUtil.marshallMessage(samlCredential.getAuthenticationAssertion()));
            String assertion = Base64.getEncoder().encodeToString(assertionString.getBytes(Charset.forName("UTF-8")));

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

    @Override
    public String refresh(String refreshToken) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add(CLIENT_ID, clientId);
            map.add(CLIENT_SECRET, clientSecret);
            map.add(GRANT_TYPE, RESRESH_TOKEN);
            map.add(RESRESH_TOKEN, refreshToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(tokenExchangeEndpointUrl, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
