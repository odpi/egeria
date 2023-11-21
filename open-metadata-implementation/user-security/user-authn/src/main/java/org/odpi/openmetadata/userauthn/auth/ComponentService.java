/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * This component is responsible to provide the configuration properties used by UI to show/hide components.
 */
@Service
@ConfigurationProperties(prefix = "role")
@EnableConfigurationProperties
public class ComponentService
{
    private final Map<String, List<String>> visibleComponents = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);


    /**
     * Retrieve the visible components.
     *
     * @return map of user role to visible components
     */
    public Map<String, List<String>> getVisibleComponents()
    {
        return visibleComponents;
    }

    /**
     * List the UI components that the supplied roles can use.
     *
     * @param roles the list of roles
     * @return a set of  components to be displayed
     */
    public Set<String> getVisibleComponentsForRoles(Collection<String> roles)
    {
        Set<String> components = new HashSet<>();

        roles.stream()
                .map(visibleComponents::get)
                .filter(Objects::nonNull)
                .forEach(components::addAll);

        return components;
    }

    /**
     * Returns the set of roles used by the runtime.
     * This is configured in the application.properties with role.visibleComponents.[ROLE] values
     *
     * @return list of string names
     */
    public final Set<String> getAppRoles(){
        return visibleComponents.keySet();
    }
}
