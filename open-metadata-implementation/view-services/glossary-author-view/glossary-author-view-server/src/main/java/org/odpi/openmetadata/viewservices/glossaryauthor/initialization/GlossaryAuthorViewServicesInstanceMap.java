/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.initialization;

import org.odpi.openmetadata.viewservices.glossaryauthor.admin.serviceinstances.GlossaryAuthorViewServicesInstance;
import java.util.HashMap;
import java.util.Map;

/**
 * GlossaryAuthorViewServicesInstanceMap provides the mapping for inbound REST requests to the appropriate instances
 * for the requested server.  The map is maintained in a static so it is scoped to the class loader.
 *
 * Instances of this class call the synchronized static methods to work with the map.
 */
public class GlossaryAuthorViewServicesInstanceMap
{
    private static  Map<String, GlossaryAuthorViewServicesInstance>   instanceMap = new HashMap<>();


    /**
     * Add a new server instance to the server map.
     *
     * @param serverName name of the server
     * @param instance instance object
     */
    static public synchronized void  setNewInstanceForJVM(String                         serverName,
                                                   GlossaryAuthorViewServicesInstance instance)
    {
        instanceMap.put(serverName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    private static synchronized GlossaryAuthorViewServicesInstance getInstanceForJVM(String    serverName)
    {
        GlossaryAuthorViewServicesInstance instance = instanceMap.get(serverName);

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
    public GlossaryAuthorViewServicesInstanceMap()
    {
    }
    /**
     * Return the instance for this server.
     *
     * @param serverName name of the server
     * @return OMRSRepositoryServicesInstance object
     */
    public GlossaryAuthorViewServicesInstance getInstance(String    serverName)
    {
        return GlossaryAuthorViewServicesInstanceMap.getInstanceForJVM(serverName);
    }
}
