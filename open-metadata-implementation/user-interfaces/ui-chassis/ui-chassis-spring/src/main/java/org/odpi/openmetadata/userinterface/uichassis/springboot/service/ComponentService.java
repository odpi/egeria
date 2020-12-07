/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * This component is responsible to provide model used by UI to show/hide components
 */
@Service
@ConfigurationProperties(prefix = "role")
public class ComponentService {

    private static final String ADMIN_ROLE_PERMISSION = "*";
    private final Map<String, List<String>> visibleComponents = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public Map<String, List<String>> getVisibleComponents() {
        return visibleComponents;
    }

    /**
     *
     * @param roles the list of roles
     * @return a set of  components to be displayed
     */
    public Set<String> getVisibleComponentsForRoles(List<String> roles) {
        Set<String> components = new HashSet<>();
        roles.stream()
                .map(visibleComponents::get)
                .filter(Objects::nonNull)
                .forEach(components::addAll);
        if (components.contains(ADMIN_ROLE_PERMISSION)) {
            return Collections.emptySet();
        }
        return components;
    }
}
