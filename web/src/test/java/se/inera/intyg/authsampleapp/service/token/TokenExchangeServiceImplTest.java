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

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenExchangeServiceImplTest {

    @Spy
    private TokenTransformServiceImpl tokenTransformService = new TokenTransformServiceImpl();

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TokenExchangeServiceImpl testee;

    @Test
    public void test() throws IOException {
        ReflectionTestUtils.setField(testee, "tokenExchangeEndpointUrl", "http://some.url");
        ReflectionTestUtils.setField(testee, "clientId", "CLIENT_ID");
        ReflectionTestUtils.setField(testee, "clientSecret", "CLIENT_SECRET");
        String responseXml = IOUtils.toString(new ClassPathResource("ineradev/assertion.base64").getInputStream(), Charset.forName("UTF-8"));

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(ResponseEntity.ok("ok"));
        String token = testee.exchange(responseXml.getBytes(Charset.forName("UTF-8")));
    }
}
