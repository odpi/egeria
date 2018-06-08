/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.eventmapper;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The GraphOMRSRepositoryEventMapperProvider is the connector provider for the GraphOMRSRepositoryEventMapperProvider.
 * It extends OMRSRepositoryEventMapperProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 *
 * The GraphOMRSRepositoryEventMapperProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class GraphOMRSRepositoryEventMapperProvider extends OMRSRepositoryConnectorProviderBase
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public GraphOMRSRepositoryEventMapperProvider()
    {
        Class    connectorClass = GraphOMRSRepositoryEventMapper.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}