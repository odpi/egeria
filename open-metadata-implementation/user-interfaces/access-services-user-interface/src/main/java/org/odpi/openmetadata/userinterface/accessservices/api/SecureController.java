/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;

import org.odpi.openmetadata.userinterface.accessservices.domain.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class provides a method to access the user name from the servlet session. This class should be subclassed so that the user
 * can be obtained and then used on omas calls.
 */
public class SecureController {
    /**
     * Return user name if there is one or null. Passing null as the user to a rest call should result in a user not authorized error.
     * @param request servlet request
     * @return userName or null if there is not one
     */
    protected String getUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName =null;
        if (session !=null) {
             User user = (User) session.getAttribute("user");
             if (user.getName() !=null) {
                 userName = user.getUsername();
             }
        }
        return userName;
    }
}
