/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.security.springboot.auth;

import org.odpi.openmetadata.userinterface.security.springboot.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SessionAuthService implements AuthService {

    public static final String USER = "user";

    @Value("${token.secret}")
    private String secret;

    @Override
    public User addAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        TokenUser tokenUser = getTokenUser(authentication);
        request.getSession().setAttribute(USER, tokenUser.getUser());
        String token = createTokenForUser(tokenUser.getUser(), secret);
        response.addHeader(AUTH_HEADER_NAME, token);
        return tokenUser.getUser();
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        Object userJSON = request.getSession().getAttribute(USER);
        if (!StringUtils.isEmpty(userJSON)) {
            User user = fromJSON(String.valueOf(userJSON));
            if (user != null) {
                return new UserAuthentication(new TokenUser(user));
            }
        }
        return null;
    }

}
