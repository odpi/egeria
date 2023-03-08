/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.server;

import java.util.HashMap;
import java.util.Map;

/**
 * ConformanceServicesInstanceMap provides the mapping between server name and the Java instances of the active services
 * for a server.
 *
 * The Java service instances for a server are defined by the configuration document.  When the server is initialized
 * the configuration document is read and the appropriate services are initialized.
 *
 * It is possible that multiple OMAG servers are initialized in a since JVM/process.   The role of this class is to
 * keep references to the service instances for each server so that when a request is made to a specific server,
 * it can be routed to the appropriate service instance.
 */
public class ConformanceServicesInstanceMap
{
    private static Map<String, ConformanceServicesInstance> instanceMap = new HashMap<>();



    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance instance object
     */
    private static synchronized void  setNewInstanceForJVM(String                      serverName,
                                                           ConformanceServicesInstance instance)
    {
        instanceMap.put(serverName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return ConformanceServicesInstance object
     */
    private static synchronized ConformanceServicesInstance getInstanceForJVM(String    serverName)
    {
        return instanceMap.get(serverName);
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
    public ConformanceServicesInstanceMap()
    {
    }


    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance instance object
     */
    public void  setNewInstance(String                                 serverName,
                                ConformanceServicesInstance instance)
    {
        ConformanceServicesInstanceMap.setNewInstanceForJVM(serverName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    public ConformanceServicesInstance getInstance(String    serverName)
    {
        return ConformanceServicesInstanceMap.getInstanceForJVM(serverName);
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     */
    public void removeInstance(String   serverName)
    {
        ConformanceServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
