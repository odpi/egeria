/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.inmemory;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicProvider;


/**
 * InMemoryOpenMetadataTopicProvider provides implementation of the connector provider for the InMemoryOpenMetadataTopicConnector.
 */
public class InMemoryOpenMetadataTopicProvider extends OpenMetadataTopicProvider
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.eventbus.topic.inmemory.InMemoryOpenMetadataTopicConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public InMemoryOpenMetadataTopicProvider()
    {
        super(EgeriaOpenConnectorDefinition.IN_MEMORY_TOPIC_CONNECTOR,
              connectorClassName,
              null);
    }
}