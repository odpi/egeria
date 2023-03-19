/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IntegrationConnectorCacheMap maintains the map of connectorIds to integration connector handlers.
 * It is synchronized because the map is being rebuilt periodically.
 */
public class IntegrationConnectorCacheMap
{
    private volatile Map<String, IntegrationConnectorHandler> integrationConnectorLookupTable = new HashMap<>();
    private volatile List<IntegrationConnectorHandler>        integrationConnectorProcessingList = new ArrayList<>();

    /**
     * Remove all integration connectors from the hash map
     */
    synchronized void clear()
    {
        integrationConnectorLookupTable = new HashMap<>();
        integrationConnectorProcessingList = new ArrayList<>();
    }


    /**
     * Add a new integration connector to the map/list.  New connectors are added to the front of the list, so they get processed first in the next
     * iteration.
     *
     * @param connectorId unique identifier of the connector
     * @param integrationConnectorHandler mapped integration connector
     */
    public synchronized void putHandlerByConnectorId(String                      connectorId,
                                                     IntegrationConnectorHandler integrationConnectorHandler)
    {
        integrationConnectorLookupTable.put(connectorId, integrationConnectorHandler);

        if (! integrationConnectorHandler.needsDedicatedThread())
        {
            integrationConnectorProcessingList.add(0, integrationConnectorHandler);
        }
    }


    /**
     * Retrieve the integration connector for the connectorId.
     *
     * @param connectorId identifier of the connector.
     * @return connector handler
     */
    public synchronized IntegrationConnectorHandler getHandlerByConnectorId(String connectorId)
    {
        return integrationConnectorLookupTable.get(connectorId);
    }


    /**
     * Return the list of connectorIds registered with an integration daemon.
     *
     * @return list of connector ids.
     */
    public synchronized List<String> getConnectorIds()
    {
        if (integrationConnectorLookupTable.isEmpty())
        {
            return null;
        }

        return new ArrayList<>(integrationConnectorLookupTable.keySet());
    }


    /**
     * Return the list of connectors that should be processed by the integration daemon thread.
     *
     * @return list of non-blocking connectors
     */
    public synchronized List<IntegrationConnectorHandler> getIntegrationConnectorProcessingList()
    {
        if (integrationConnectorProcessingList.isEmpty())
        {
            return null;
        }

        return new ArrayList<>(integrationConnectorProcessingList);
    }
}
