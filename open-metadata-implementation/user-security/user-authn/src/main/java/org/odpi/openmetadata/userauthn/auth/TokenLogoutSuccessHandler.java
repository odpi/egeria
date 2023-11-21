/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

public class TokenLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler
{
    private static final Logger log = LoggerFactory.getLogger( TokenLogoutSuccessHandler.class );

    private final TokenClient tokenClient;

    TokenLogoutSuccessHandler(TokenClient tokenClient){
        this.tokenClient = tokenClient;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException
    {
        String refererUrl = request.getHeader("Referer");
        log.debug("Logout from: {}", refererUrl);

//        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getHeader("Authorization");

        if (tokenClient!=null && token != null )
        {
            tokenClient.del(token);
        }

        response.sendRedirect("login?logoutSuccessful");

        super.onLogoutSuccess(request, response, authentication);
    }
}

