/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.admin;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides the mapping for inbound REST requests to the appropriate instances
 * for the requested server. The map is maintained in a static map so it is scoped to the class loader.
 * <p>
 * Instances of this class call the synchronized static methods to work with the map.
 */
public class GlossaryViewServicesInstanceMap {

    private static Map<String, GlossaryViewServicesInstance> instanceMap = new HashMap<>();

    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance   instance object
     */
    static synchronized void setNewInstanceForJVM(String serverName, GlossaryViewServicesInstance instance) {
        instanceMap.put(serverName, instance);
    }

    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     *
     * @return OMRSRepositoryServicesInstance object
     */
     public static synchronized GlossaryViewServicesInstance getInstanceForJVM(String serverName) {
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


    public static boolean containsInstanceForJvm(String serverName){
        return instanceMap.containsKey(serverName);
    }

    private GlossaryViewServicesInstanceMap() {
    }
}

