/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthService;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.TokenClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    @Autowired(required = false)
    TokenClient tokenClient;


    @GetMapping
    public void logout(HttpServletRequest request) throws HttpClientErrorException {
        String token = request.getHeader(AuthService.AUTH_HEADER_NAME);
        if(tokenClient != null && token != null){
           tokenClient.del(token);
        }
    }


}
