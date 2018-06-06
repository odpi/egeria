/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.omrstopicconnector.inmemory;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;


/**
 * InMemoryOMRSTopicProvider provides implementation of the connector provider for the InMemoryOMRSTopicConnector.
 */
public class InMemoryOMRSTopicProvider extends ConnectorProviderBase
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public InMemoryOMRSTopicProvider()
    {
        Class    connectorClass = InMemoryOMRSTopicConnector.class;

        super.setConnectorClassName(connectorClass.getName());
    }
}
