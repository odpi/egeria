/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Sets up user roles.
 */
@Service
public class RoleService
{
    @Autowired
    ComponentService componentService;

    /**
     *
     * @param userRoles collection of roles of user
     * @return the intersection between all user roles and roles used for the application
     */
    public Collection<String> extractUserAppRoles(Collection<String> userRoles)
    {
        Collection<String> appRoles = componentService.getAppRoles();
        return  userRoles.stream()
                .filter( appRoles::contains )
                .collect( Collectors.toSet() );
    }


    /**
     * Return the visible components for the supplied roles.
     *
     * @param roles list of roles
     * @return set of visible components for the supplied roles
     */
    public Set<String> getVisibleComponents(Collection<String> roles)
    {
        return componentService.getVisibleComponentsForRoles(roles);
    }

}
