/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * OpenLineageEventReceiverIntegrationProvider is the connector provider for the OpenLineageEventReceiverIntegrationConnector.
 */
public class OpenLineageEventReceiverIntegrationProvider extends ConnectorProviderBase
{
    private static final String connectorTypeGUID          = "20a7cfe0-e2c1-4ce6-9c06-2d7005553d23";
    private static final String connectorTypeQualifiedName = "Egeria:IntegrationConnector:Lineage:OpenLineageEventReceiver";
    private static final String connectorTypeDisplayName   = "Open Lineage Event Receiver Integration Connector";
    private static final String connectorTypeDescription   = "Connector to receive and publish open lineage events from an event broker topic and publish" +
                                                                     "them to lineage integration connectors with listeners registered in the same " +
                                                                     "instance of the Lineage Integrator OMIS.";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public OpenLineageEventReceiverIntegrationProvider()
    {
        super();

        super.setConnectorClassName(OpenLineageEventReceiverIntegrationConnector.class.getName());

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
