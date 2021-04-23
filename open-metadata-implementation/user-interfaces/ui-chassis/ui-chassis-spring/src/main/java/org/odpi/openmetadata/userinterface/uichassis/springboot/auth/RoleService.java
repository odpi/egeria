/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.odpi.openmetadata.userinterface.uichassis.springboot.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.stream.Collectors;

public class RoleService {

    @Autowired
    ComponentService componentService;

    /**
     *
     * @param roles collection of roles
     * @return the intersection
     */
    public Collection<String> extractUserAppRoles(Collection<String> roles) {
        Collection<String> appRoles = componentService.getAppRoles();
        return  roles.stream()
                .filter( appRoles::contains )
                .collect( Collectors.toSet() );
    }

}
