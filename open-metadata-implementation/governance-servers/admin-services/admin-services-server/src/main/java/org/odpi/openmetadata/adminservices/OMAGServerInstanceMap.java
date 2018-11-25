/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import java.util.HashMap;
import java.util.Map;

/**
 * OMAGServerInstanceMap provides the mapping between server name and the Java instances of the active services
 * for a server.
 *
 * The Java service instances for a server are defined by the configuration document.  When the server is initialized
 * the configuration document is read and the appropriate services are initialized.
 *
 * It is possible that multiple OMAG servers are initialized in a since JVM/process.   The role of this class is to
 * keep references to the service instances for each server so that when a request is made to a specific server,
 * it can be routed to the appropriate service instance.
 */
public class OMAGServerInstanceMap
{
    private static Map<String, OMAGOperationalServicesInstance> instanceMap = new HashMap<>();


    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance instance object
     */
    private static synchronized void  setNewInstanceForJVM(String                           serverName,
                                                           OMAGOperationalServicesInstance   instance)
    {
        instanceMap.put(serverName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    private static synchronized OMAGOperationalServicesInstance getInstanceForJVM(String    serverName)
    {
        OMAGOperationalServicesInstance   instance = instanceMap.get(serverName);

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
    public OMAGServerInstanceMap()
    {
    }


    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance instance object
     */
    public void  setNewInstance(String                           serverName,
                                OMAGOperationalServicesInstance   instance)
    {
        OMAGServerInstanceMap.setNewInstanceForJVM(serverName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    public OMAGOperationalServicesInstance getInstance(String    serverName)
    {
        return OMAGServerInstanceMap.getInstanceForJVM(serverName);
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     */
    public void removeInstance(String   serverName)
    {
        OMAGServerInstanceMap.removeInstanceForJVM(serverName);
    }
}
