/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.services;


import java.util.HashMap;
import java.util.Map;

/**
 * OMRSRepositoryServicesInstanceMap provides the mapping for inbound REST requests to the appropriate instances
 * for the requested server.  The map is maintained in a static so it is scoped to the class loader.
 *
 * Instances of this class call the synchronized static methods to work with the map.
 */
public class OMRSRepositoryServicesInstanceMap
{
    private static  Map<String, OMRSRepositoryServicesInstance>   instanceMap = new HashMap<>();


    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance instance object
     */
    private static synchronized void  setNewInstanceForJVM(String                           serverName,
                                                           OMRSRepositoryServicesInstance   instance)
    {
        instanceMap.put(serverName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    private static synchronized OMRSRepositoryServicesInstance getInstanceForJVM(String    serverName)
    {
        OMRSRepositoryServicesInstance   instance = instanceMap.get(serverName);

        return instance;
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     */
    private static synchronized void removeInstanceForJVM(String   serverName)
    {
        instanceMap.remove(serverName);
    }


    /**
     * Constructor
     */
    public OMRSRepositoryServicesInstanceMap()
    {
    }


    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance instance object
     */
    public void  setNewInstance(String                           serverName,
                                OMRSRepositoryServicesInstance   instance)
    {
        OMRSRepositoryServicesInstanceMap.setNewInstanceForJVM(serverName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    public OMRSRepositoryServicesInstance getInstance(String    serverName)
    {
        return OMRSRepositoryServicesInstanceMap.getInstanceForJVM(serverName);
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     */
    public void removeInstance(String   serverName)
    {
        OMRSRepositoryServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
