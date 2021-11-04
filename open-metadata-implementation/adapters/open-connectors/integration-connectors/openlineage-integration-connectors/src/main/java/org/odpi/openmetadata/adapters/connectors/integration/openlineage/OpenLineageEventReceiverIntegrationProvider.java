/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.ArrayList;
import java.util.List;


/**
 * KafkaOpenLineageReceiverIntegrationProvider is the connector provider for the KafkaOpenLineageReceiverIntegrationConnector.
 */
public class KafkaOpenLineageReceiverIntegrationProvider extends ConnectorProviderBase
{
    private static final String connectorTypeGUID          = "20a7cfe0-e2c1-4ce6-9c06-2d7005553d23";
    private static final String connectorTypeQualifiedName = "Egeria:IntegrationConnector:Lineage:KafkaOpenLineageReceiver";
    private static final String connectorTypeDisplayName   = "Kafka Open Lineage Receiver Integration Connector";
    private static final String connectorTypeDescription   = "Connector to receive and publish open lineage events from a kafka topic and publish" +
                                                                     "them to lineage integration connectors with listeners registered in the same " +
                                                                     "instance of the Lineage Integrator OMIS.";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public KafkaOpenLineageReceiverIntegrationProvider()
    {
        super();

        super.setConnectorClassName(KafkaOpenLineageReceiverIntegrationConnector.class.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}
