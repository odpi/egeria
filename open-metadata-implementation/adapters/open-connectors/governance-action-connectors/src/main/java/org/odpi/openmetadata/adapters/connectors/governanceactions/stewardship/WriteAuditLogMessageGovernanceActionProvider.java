/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

/**
 * WriteAuditLogMessageGovernanceActionProvider is the OCF connector provider for the "Write to Audit Log"
 * Governance Action Service.
 */
public class WriteAuditLogMessageGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "9ad61312-e578-43c9-b493-ec5a8510c576";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Stewardship:EvaluateAnnotations";
    private static final String  connectorTypeDisplayName = "Write Audit Log Governance Action Service";
    private static final String  connectorTypeDescription = "Governance Action Service that writes requested messages to the Audit Log Destinations.";

    private static final String connectorClassName = WriteAuditLogMessageGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public WriteAuditLogMessageGovernanceActionProvider()
    {
        super();

        super.supportedRequestParameters = WriteAuditLogRequestParameter.getRequestParameterTypes();
        super.producedGuards = WriteAuditLogGuard.getGuardTypes();

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;
    }
}
