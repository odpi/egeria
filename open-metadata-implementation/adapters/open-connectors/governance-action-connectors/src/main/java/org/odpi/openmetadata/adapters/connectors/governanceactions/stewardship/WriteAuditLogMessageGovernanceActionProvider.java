/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;

import java.util.HashMap;

/**
 * EvaluateAnnotationsGovernanceActionProvider is the OCF connector provider for the "Evaluate Annotations"
 * Governance Action Service.
 */
public class WriteAuditLogMessageGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "9ad61312-e578-43c9-b493-ec5a8510c576";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Verification:EvaluateAnnotations";
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
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;
    }
}
