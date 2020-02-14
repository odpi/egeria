/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.initialization;

import org.odpi.openmetadata.viewservices.subjectarea.services.SubjectAreaViewServicesInstance;
import java.util.HashMap;
import java.util.Map;

/**
 * SubjectAreaViewServicesInstanceMap provides the mapping for inbound REST requests to the appropriate instances
 * for the requested server.  The map is maintained in a static so it is scoped to the class loader.
 *
 * Instances of this class call the synchronized static methods to work with the map.
 */
public class SubjectAreaViewServicesInstanceMap
{
    private static  Map<String, SubjectAreaViewServicesInstance>   instanceMap = new HashMap<>();


    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance instance object
     */
    static public synchronized void  setNewInstanceForJVM(String                         serverName,
                                                   SubjectAreaViewServicesInstance instance)
    {
        instanceMap.put(serverName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    private static synchronized SubjectAreaViewServicesInstance getInstanceForJVM(String    serverName)
    {
        SubjectAreaViewServicesInstance instance = instanceMap.get(serverName);

        return instance;
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     */
    static public synchronized void removeInstanceForJVM(String   serverName)
    {
        instanceMap.remove(serverName);
    }


    /**
     * Constructor
     */
    public SubjectAreaViewServicesInstanceMap()
    {
    }
    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    public SubjectAreaViewServicesInstance getInstance(String    serverName)
    {
        return SubjectAreaViewServicesInstanceMap.getInstanceForJVM(serverName);
    }
}
