/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthService;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.TokenUser;
import org.odpi.openmetadata.userinterface.uichassis.springboot.domain.User;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserInfoController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ComponentService componentService;

    @GetMapping( value ="/current")
    public User getUser(HttpServletRequest request) throws HttpClientErrorException{
        return getTokenUser(request).getUser();
    }

    @GetMapping( value ="/components")
    public Collection<String> getVisibleComponents(HttpServletRequest request) throws HttpClientErrorException{
        return componentService.getVisibleComponentsForRoles(getTokenUser(request).getRole());
    }

    private TokenUser getTokenUser(HttpServletRequest request){
        Authentication auth = authService.getAuthentication(request);

        if(auth == null || auth.getDetails() == null || !(auth.getDetails() instanceof TokenUser)){
            throw new UserNotAuthorizedException("User is not authorized");
        }

        return (TokenUser) auth.getDetails();
    }

}
