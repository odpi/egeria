/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthService;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.TokenUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides a method to access the user name from the servlet session. This class should be subclassed so that the user
 * can be obtained and then used on omas calls.
 */
public class SecureController {
    public static final String PAGE_OFFSET_DEFAULT_VALUE = "0";
    public static final String PAGE_SIZE_DEFAULT_VALUE = "0";

    @Autowired
    private AuthService authService;

    /**
     * Return user name if there is one or null. Passing null as the user to a rest call should result in a user not authorized error.
     * @param request servlet request
     * @return userName or null if there is not one
     */
    protected String getUser(HttpServletRequest request) {
        String userName = null;
        Authentication auth = authService.getAuthentication(request);
        if(auth != null && auth.getDetails() != null && (auth.getDetails() instanceof TokenUser)){
            userName = ((TokenUser) auth.getDetails()).getUsername();
        }

        if(userName ==  null){
            throw new UserNotAuthorizedException("User is not authorized");
        }
        return userName;
    }
}
