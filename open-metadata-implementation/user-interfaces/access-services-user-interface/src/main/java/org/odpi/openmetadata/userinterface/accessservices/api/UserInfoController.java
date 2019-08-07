/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;

import org.odpi.openmetadata.userinterface.accessservices.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserInfoController {

    @RequestMapping( value ="/current", method = RequestMethod.GET)
    public User getUser(HttpServletRequest request) throws HttpClientErrorException{
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            throw new UserNotAuthorizedException("User is not authorized");
        }
        return user;
    }
}
