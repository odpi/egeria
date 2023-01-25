/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Set;
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
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            tokenUser = new TokenUser( person,
                                       extractUserAppRoles(userRoles));
        }else {
            UserDetails userDetails = (UserDetails) principal;
            tokenUser = new TokenUser(userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet()));
        }
        tokenUser.setVisibleComponents(getVisibleComponents( tokenUser.getRoles()) );
        return tokenUser;
    }


    /**
     *
     * @return milliseconds until expiration
     */
     long getTokenTimeout();

    /**
     *
     * @return collection of visible ui components for roles
     * @param roles the collection of roles
     */
    Set<String> getVisibleComponents(Collection<String> roles);
}
