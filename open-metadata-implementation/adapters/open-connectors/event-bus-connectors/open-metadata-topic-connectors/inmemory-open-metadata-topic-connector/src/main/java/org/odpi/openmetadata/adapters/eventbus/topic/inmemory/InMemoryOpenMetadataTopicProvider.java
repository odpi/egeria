/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.inmemory;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * InMemoryOpenMetadataTopicProvider provides implementation of the connector provider for the InMemoryOpenMetadataTopicConnector.
 */
public class InMemoryOpenMetadataTopicProvider extends OpenMetadataTopicProvider
{
    static final String  connectorTypeGUID = "ed8e682b-2fec-4403-b551-02f8c46322ef";
    static final String  connectorTypeName = "In Memory Open Metadata Topic Connector";
    static final String  connectorTypeDescription = "In Memory Open Metadata Topic Connector supports string based events over an in memory event bus.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public InMemoryOpenMetadataTopicProvider()
    {
        Class<?>    connectorClass = InMemoryOpenMetadataTopicConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(sleepTimeProperty);

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}