/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.redis.TokenRedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenLogoutSuccessHandler extends
        SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger( TokenLogoutSuccessHandler.class );

    private TokenRedisClient tokenRedisClient;

    TokenLogoutSuccessHandler(TokenRedisClient tokenRedisClient){
        this.tokenRedisClient = tokenRedisClient;
    }

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {


        String refererUrl = request.getHeader("Referer");
        log.debug("Logout from: " + refererUrl);

        String token = request.getHeader(AuthService.AUTH_HEADER_NAME);

        if(tokenRedisClient!=null && token != null ){
            tokenRedisClient.del(token);
        }
        response.addHeader(AuthService.AUTH_HEADER_NAME,"");
        response.sendRedirect("login?logoutSuccessful");
        super.onLogoutSuccess(request, response, authentication);
    }
}

