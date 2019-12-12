/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.security.springboot.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.odpi.openmetadata.userinterface.security.springboot.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public interface AuthService {

    //30 minutes in millis
    long VALIDITY_TIME_MS = 30 * 60 * 1000;
    String AUTH_HEADER_NAME = "x-auth-token";

    Authentication getAuthentication(HttpServletRequest request);

    User addAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    default TokenUser getTokenUser(Authentication authentication) {
        TokenUser tokenUser;
        Object principal = authentication.getPrincipal();
        if (principal instanceof TokenUser) {
            tokenUser = (TokenUser) principal;
        } else {
            tokenUser = new TokenUser((InetOrgPerson) principal);
        }
        return tokenUser;
    }

    default User fromJSON(final String userJSON) {
        try {
            return new ObjectMapper().readValue(userJSON, User.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    default String toJSON(User user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    default TokenUser parseUserFromToken(String token, String secret) {
        String userJSON = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return new TokenUser(fromJSON(userJSON));
    }

    default String createTokenForUser(User user, String secret) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
                .setSubject(toJSON(user))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
