/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
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
                                                                 "instance of the integration daemon.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/integration/open-lineage-event-receiver-integration-connector/";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageEventReceiverIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public OpenLineageEventReceiverIntegrationProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClassName);

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.LINEAGE_INTEGRATION_CONNECTOR.getDeployedImplementationType());

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
