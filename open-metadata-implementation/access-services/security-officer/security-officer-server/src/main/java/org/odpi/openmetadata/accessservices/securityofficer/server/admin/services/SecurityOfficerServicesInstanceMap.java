/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.services;

import java.util.HashMap;
import java.util.Map;

/**
 * SecurityOfficerServicesInstanceMap provides the mapping for inbound REST requests to the appropriate instances
 * for the requested server.  The map is maintained in a static so it is scoped to the class loader.
 * <p>
 * Instances of this class call the synchronized static methods to work with the map.
 */
class SecurityOfficerServicesInstanceMap {
    private static Map<String, SecurityOfficerServicesInstance> instanceMap = new HashMap<>();


    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance   instance object
     */
    static synchronized void setNewInstanceForJVM(String serverName, SecurityOfficerServicesInstance instance) {
        instanceMap.put(serverName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    private static synchronized SecurityOfficerServicesInstance getInstanceForJVM(String serverName) {
        return instanceMap.get(serverName);
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     */
    static synchronized void removeInstanceForJVM(String serverName) {
        instanceMap.remove(serverName);
    }

    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    public SecurityOfficerServicesInstance getInstance(String serverName) {
        return SecurityOfficerServicesInstanceMap.getInstanceForJVM(serverName);
    }
}
