/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.odpi.openmetadata.userinterface.uichassis.springboot.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SessionAuthService extends TokenSettings implements AuthService {

    public static final String USER_ATTRIBUTE_NAME = "user";

    @Override
    public User addAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        TokenUser tokenUser = getTokenUser(authentication);
        request.getSession().setAttribute(USER_ATTRIBUTE_NAME, toJSON(tokenUser.getUser()));
        String token = createTokenForUser(tokenUser.getUser(), tokenSecret);
        response.addHeader(AUTH_HEADER_NAME, token);
        return tokenUser.getUser();
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        Object userJSON = request.getSession().getAttribute(USER_ATTRIBUTE_NAME);
        if (!ObjectUtils.isEmpty(userJSON)) {
            User user = fromJSON(String.valueOf(userJSON));
            if (user != null) {
                return new UserAuthentication(new TokenUser(user));
            }
        }
        return null;
    }

}
