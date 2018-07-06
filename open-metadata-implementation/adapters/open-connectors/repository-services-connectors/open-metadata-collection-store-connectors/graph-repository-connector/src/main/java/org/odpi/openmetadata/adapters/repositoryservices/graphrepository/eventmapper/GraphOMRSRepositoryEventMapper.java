/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.eventmapper;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperBase;


/**
 * GraphOMRSRepositoryEventMapper supports the event mapper function for the default graph store open
 * metadata repository.
 */
public class GraphOMRSRepositoryEventMapper extends OMRSRepositoryEventMapperBase
{
    /**
     * Default constructor
     */
    public GraphOMRSRepositoryEventMapper()
    {
        super();
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();
    }

    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    public void processEvent(String event)
    {

    }

    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
