/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthService;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.TokenUser;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserInfoController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ComponentService componentService;

    /**
     *
     * @param request the http request
     * @return current authenticated user
     * @throws HttpClientErrorException exception when executing the request
     */
    @GetMapping( value ="/current")
    public TokenUser getUser(HttpServletRequest request) throws HttpClientErrorException{
        return getTokenUser(request);
    }

    /**
     *
     * @param request the http request
     * @return visible ui components according to user roles
     * @throws HttpClientErrorException exception when executing the request
     */
    @GetMapping( value ="/components")
    public Collection<String> getVisibleComponents(HttpServletRequest request) throws HttpClientErrorException{
        return componentService.getVisibleComponentsForRoles(getTokenUser(request).getRoles());
    }

    /**
     *
     * @param request http request
     * @return token user from request
     */
    private TokenUser getTokenUser(HttpServletRequest request){
        Authentication auth = authService.getAuthentication(request);

        if(auth == null || auth.getDetails() == null || !(auth.getDetails() instanceof TokenUser)){
            throw new UserNotAuthorizedException("User is not authorized");
        }

        return (TokenUser) auth.getDetails();
    }

    /**
     *
     * @param request the http request
     * @return the intersection between user roles and app roles
     * @throws HttpClientErrorException exception when executing the request
     */
    @GetMapping( value ="/roles")
    public Collection<String> getRoles(HttpServletRequest request) throws HttpClientErrorException{
        TokenUser tokenUser =  getTokenUser(request);
        Set<String> appRoles  =  componentService.getAppRoles();
        return  tokenUser.getRoles().stream()
                .filter( appRoles::contains )
                .collect( Collectors.toSet() );
     }

}
