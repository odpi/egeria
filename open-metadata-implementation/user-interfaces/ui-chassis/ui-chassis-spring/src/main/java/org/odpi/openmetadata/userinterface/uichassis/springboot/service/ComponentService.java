/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This component is responsible to provide model used by UI to show/hide components
 */
@Service
@ConfigurationProperties(prefix = "role")
@EnableConfigurationProperties
public class ComponentService {

    private final Map<String, List<String>> visibleComponents = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public Map<String, List<String>> getVisibleComponents() {
        return visibleComponents;
    }

    /**
     *
     * @param roles the list of roles
     * @return a set of  components to be displayed
     */
    public Set<String> getVisibleComponentsForRoles(Collection<String> roles) {
        Set<String> components = new HashSet<>();
        roles.stream()
                .map(visibleComponents::get)
                .filter(Objects::nonNull)
                .forEach(components::addAll);
        return components;
    }

    /**
     *
     * @return the set of roles used by the app
     * this is configuration of the application.properties with role.visibleComponents.[ROLE] values
     */
    public final Set<String> getAppRoles(){
        return visibleComponents.keySet();
    }
}
