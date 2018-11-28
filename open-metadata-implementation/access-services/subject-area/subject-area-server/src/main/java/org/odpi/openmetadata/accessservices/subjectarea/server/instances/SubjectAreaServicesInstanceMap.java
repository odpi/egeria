/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.instances;


import java.util.HashMap;
import java.util.Map;

/**
 * SubjectAreaServicesInstanceMap provides the mapping for inbound REST requests to the appropriate instances
 * for the requested server.  The map is maintained in a static so it is scoped to the class loader.
 *
 * Instances of this class call the synchronized static methods to work with the map.
 */
public class SubjectAreaServicesInstanceMap
{
    private static  Map<String, SubjectAreaServicesInstance>   instanceMap = new HashMap<>();


    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance instance object
     */
    static synchronized void  setNewInstanceForJVM(String                         serverName,
                                                   SubjectAreaServicesInstance   instance)
    {
        instanceMap.put(serverName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    private static synchronized SubjectAreaServicesInstance getInstanceForJVM(String    serverName)
    {
        SubjectAreaServicesInstance   instance = instanceMap.get(serverName);

        return instance;
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     */
    static synchronized void removeInstanceForJVM(String   serverName)
    {
        instanceMap.remove(serverName);
    }


    /**
     * Constructor
     */
    public SubjectAreaServicesInstanceMap()
    {
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    public SubjectAreaServicesInstance getInstance(String    serverName)
    {
        return SubjectAreaServicesInstanceMap.getInstanceForJVM(serverName);
    }
}
