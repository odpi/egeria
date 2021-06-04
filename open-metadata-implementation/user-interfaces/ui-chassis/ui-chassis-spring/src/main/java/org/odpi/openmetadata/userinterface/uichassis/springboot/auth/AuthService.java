/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public interface AuthService {

    String AUTH_HEADER_NAME = "x-auth-token";

    Authentication getAuthentication(HttpServletRequest request);

    /**
     * Add the authentication on the response after performs other operations like persistence server side
     * @param request the http request
     * @param response the http response
     * @param authentication the authentication
     */
    void addAuthentication(HttpServletRequest request,
                           HttpServletResponse response,
                           Authentication authentication) ;

    /**
     *
     * @param roles a collection of roles
     * @return the intersection between aplication defined roles and the one from the collection
     */
    Collection<String> extractUserAppRoles(Collection<String> roles);

    /**
     *
     * @param authentication the spring security Authentication
     * @return the Token user
     */
    default TokenUser getTokenUser(Authentication authentication) {
        TokenUser tokenUser;
        Object principal = authentication.getPrincipal();
        if (principal instanceof InetOrgPerson) {
            InetOrgPerson person = (InetOrgPerson) principal;
            Collection<String> userRoles = person.getAuthorities().stream()
                    .map( a -> a.getAuthority() )
                    .collect(Collectors.toSet());
            tokenUser = new TokenUser( person,
                                       extractUserAppRoles(userRoles));
        }else {
            UserDetails userDetails = (UserDetails) principal;
            tokenUser = new TokenUser(userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map( a -> a.getAuthority() )
                            .collect(Collectors.toSet()));
        }
        return tokenUser;
    }

    /**
     *
     * @param userJSON representation of the TokenUser
     * @return the TokenUser
     */
    default TokenUser fromJSON(final String userJSON) {
        try {
            return new ObjectMapper().readValue(userJSON, TokenUser.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     *
     * @param user the TokenUser to be serializes
     * @return the json string representing TokenUser
     */
    default String toJSON(TokenUser user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     *
     * @param token the encoded token
     * @param secret secret phrase to decode
     * @return
     */
    default TokenUser parseUserFromToken(String token, String secret) {
        String userJSON = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return fromJSON(userJSON);
    }

    /**
     *
     * @param user the user to create token for
     * @param secret the secret for signature
     * @return jwt token
     */
    default String createTokenForUser(TokenUser user, String secret) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + getTokenTimeout()))
                .setSubject(toJSON(user))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     *
     * @return milliseconds until expiration
     */
     long getTokenTimeout();
}
