/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthService;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.redis.TokenRedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    @Autowired(required = false)
    TokenRedisClient tokenRedisClient;

    @GetMapping
    public RedirectView getBuildProperties(HttpServletRequest request) throws HttpClientErrorException {
        String token = request.getHeader(AuthService.AUTH_HEADER_NAME);
        if(tokenRedisClient != null && token !=null){
           tokenRedisClient.del(token);
        }
        return new RedirectView("/logout",true);
    }

    @Bean
    @ConditionalOnProperty(value = "authentication.mode", havingValue = "token", matchIfMissing = true)
    public TokenRedisClient tokenRedisClient(){
        return null;
    }

}
