/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthService;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.TokenUser;
import org.odpi.openmetadata.userinterface.uichassis.springboot.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserInfoController {

    @Autowired
    private AuthService authService;

    @GetMapping( value ="/current")
    public User getUser(HttpServletRequest request) throws HttpClientErrorException{
        Authentication auth = authService.getAuthentication(request);

        if(auth == null || auth.getDetails() == null || !(auth.getDetails() instanceof TokenUser)){
            throw new UserNotAuthorizedException("User is not authorized");
        }

        TokenUser tokenUser = (TokenUser) auth.getDetails();
        return tokenUser.getUser();
    }

}
