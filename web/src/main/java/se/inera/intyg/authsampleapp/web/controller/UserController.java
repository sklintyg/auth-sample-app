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
package se.inera.intyg.authsampleapp.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.authsampleapp.auth.UserModel;
import se.inera.intyg.authsampleapp.service.token.TokenExchangeService;
import se.inera.intyg.authsampleapp.web.controller.dto.UserModelResponse;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by marced on 2016-02-09.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final TokenExchangeService tokenExchangeService;

    @Autowired
    public UserController(TokenExchangeService tokenExchangeService) {
        this.tokenExchangeService = tokenExchangeService;
    }

    @RequestMapping(value = "", method = GET, produces = "application/json")
    public UserModelResponse getUser() {
        if (isAuthenticated()) {
            return new UserModelResponse((UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), true);
        }
        return UserModelResponse.UserModelResponseBuilder.anUserModelResponse()
                .withUserModel(null)
                .withAuthenticated(false)
                .build();
    }

    @RequestMapping(value = "/refresh", method = GET, produces = "application/json")
    public UserModelResponse refresh() {
        if (!isAuthenticated()) {
            throw new IllegalStateException("Cannot refresh token without logging in first.");
        }
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userModel.getRefreshToken() == null) {
            throw new IllegalStateException("Cannot refresh token without having a refresh token first.");
        }
        return getUserModelResponse(userModel, tokenExchangeService.refresh(userModel.getRefreshToken()));
    }

    @RequestMapping(value = "/exchange", method = GET, produces = "application/json")
    public UserModelResponse exchange() {
        if (!isAuthenticated()) {
            throw new IllegalStateException("Cannot initiate token exchange without logging in first.");
        }
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserModelResponse(userModel, tokenExchangeService.exchange(userModel.getSamlAuthentication()));
    }

    @RequestMapping(value = "/exchange2", method = GET, produces = "application/json")
    public UserModelResponse exchange2() {
        if (!isAuthenticated()) {
            throw new IllegalStateException("Cannot initiate token exchange without logging in first.");
        }
        SAMLCredential samlCredential = (SAMLCredential) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserModelResponse(userModel, tokenExchangeService.exchange2(samlCredential));
    }

    private UserModelResponse getUserModelResponse(UserModel userModel, String token) {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(token);
            userModel.setAccessToken(jsonNode.get("access_token").textValue());
            userModel.setRefreshToken(jsonNode.get("refresh_token").textValue());
            return UserModelResponse.UserModelResponseBuilder.anUserModelResponse()
                    .withUserModel(userModel)
                    .withAuthenticated(true)
                    .build();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserModel;
    }
}
