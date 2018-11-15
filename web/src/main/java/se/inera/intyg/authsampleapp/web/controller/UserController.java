/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.inera.intyg.authsampleapp.auth.UserModel;
import se.inera.intyg.authsampleapp.web.controller.dto.GetConfigResponse;
import se.inera.intyg.authsampleapp.web.controller.dto.UserModelResponse;

/**
 * Created by marced on 2016-02-09.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @GetMapping(produces = "application/json")
    public UserModelResponse getUser() {
        if (isAuthenticated()) {
            return new UserModelResponse((UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), true);
        }
        return new UserModelResponse(null, false);
    }

    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserModel;
    }
}
