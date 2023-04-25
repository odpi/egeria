/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.UserNotAuthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This class provides a method to access the user name from the servlet session. This class should be subclassed so that the user
 * can be obtained and then used on omas calls.
 */
public class SecureController {
    public static final String PAGE_OFFSET_DEFAULT_VALUE = "0";
    public static final String PAGE_SIZE_DEFAULT_VALUE = "0";

//    @Autowired
//    private AuthService authService;

    /**
     * Return user name if there is one or null. Passing null as the user to a rest call should result in a user not authorized error.
     * @return userName or null if there is not one
     */
    protected String getUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userName ==  null){
            throw new UserNotAuthorizedException("User is not authorized");
        }
        return userName;
    }
}
