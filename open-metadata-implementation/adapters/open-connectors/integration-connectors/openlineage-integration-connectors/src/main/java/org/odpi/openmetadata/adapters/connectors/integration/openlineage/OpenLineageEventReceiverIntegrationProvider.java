/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;


/**
 * OpenLineageEventReceiverIntegrationProvider is the connector provider for the OpenLineageEventReceiverIntegrationConnector.
 */
public class OpenLineageEventReceiverIntegrationProvider extends IntegrationConnectorProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 658;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "20a7cfe0-e2c1-4ce6-9c06-2d7005553d23";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:Lineage:OpenLineageEventReceiver";
    private static final String connectorDisplayName   = "Open Lineage Event Receiver Integration Connector";
    private static final String connectorDescription   = "Connector to receive and publish open lineage events from an event broker topic and publish" +
                                                                 "them to lineage integration connectors with listeners registered in the same " +
                                                                 "instance of the Lineage Integrator OMIS.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/integration/open-lineage-event-receiver-integration-connector/";

    /*
     * Class of the connector.
     */
    private static final Class<?> connectorClass       = OpenLineageEventReceiverIntegrationConnector.class;


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public OpenLineageEventReceiverIntegrationProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClass.getName());

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentName(connectorQualifiedName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
