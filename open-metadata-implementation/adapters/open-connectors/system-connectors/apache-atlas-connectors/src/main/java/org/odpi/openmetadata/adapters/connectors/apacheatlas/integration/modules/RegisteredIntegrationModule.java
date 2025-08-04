/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;

import java.util.List;

/**
 * RegisteredIntegrationModule defines the interface for an integration module that wants to be called to synchronize assets.
 */
public interface RegisteredIntegrationModule
{
    /**
     * Return the list of entity types that this module is maintaining.
     *
     * @return list of type names
     */
    List<String> getSupportedEntityTypes();


    /**
     * Return the list of open metadata types that this module supports events for.
     *
     * @return list of types
     */
    List<String> getListenForTypes();


    /**
     * Return the name of this module for messages.
     *
     * @return module name
     */
    public String getModuleName();


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    void refresh() throws ConnectorCheckedException;


    /**
     * Process an event that was published by the Asset Manager OMAS.  The listener is only registered if metadata is flowing
     * from the open metadata ecosystem to Apache Atlas.
     *
     * @param event event object
     */
    void processEvent(OpenMetadataOutTopicEvent event);
}
