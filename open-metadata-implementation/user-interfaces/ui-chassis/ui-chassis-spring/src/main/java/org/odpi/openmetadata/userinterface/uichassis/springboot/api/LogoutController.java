/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import jakarta.servlet.http.HttpServletRequest;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.TokenClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    @Autowired(required = false)
    TokenClient tokenClient;


    @GetMapping
    public void logout(HttpServletRequest request) throws HttpClientErrorException {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getHeader("Authorization");
        if(tokenClient != null && token != null){
           tokenClient.del(token);
        }
    }


}
