/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The GovernanceActionOpenLineageIntegrationProvider provides the connector provider for GovernanceActionOpenLineageIntegrationConnector.
 */
public class GovernanceActionOpenLineageIntegrationProvider extends ConnectorProviderBase
{
    private static final String connectorTypeGUID          = "de7320e7-3928-4266-8552-06a860533b99";
    private static final String connectorTypeQualifiedName = "Egeria:IntegrationConnector:Lineage:GovernanceActionOpenLineage";
    private static final String connectorTypeDisplayName   = "Governance Action to Open Lineage Integration Connector";
    private static final String connectorTypeDescription   = "Connector to listen for governance actions executing in the open metadata ecosystem, " +
                                                                     "generate open lineage events for them and publish them to any integration " +
                                                                     "connectors running in the same instance of Lineage Integrator OMIS.";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific connector implementation.
     */
    public GovernanceActionOpenLineageIntegrationProvider()
    {
        super();

        super.setConnectorClassName(GovernanceActionOpenLineageIntegrationConnector.class.getName());

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

