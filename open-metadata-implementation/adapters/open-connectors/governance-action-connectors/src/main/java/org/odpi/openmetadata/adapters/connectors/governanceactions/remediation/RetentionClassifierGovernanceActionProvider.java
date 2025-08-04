/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * RetentionClassifierGovernanceActionProvider is the OCF connector provider for the Retention Classifier Governance Action Service.
 * This is a Remediation Governance Action Service.
 */
public class RetentionClassifierGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "04e94902-4198-44ca-a142-fa3ae836f16b";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Remediation:RetentionClassifier";
    private static final String  connectorTypeDisplayName = "Retention Classifier Governance Action Service";
    private static final String  connectorTypeDescription = "Assigns the configured retention classification to the requested asset.";

    private static final String connectorClassName = RetentionClassifierGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public RetentionClassifierGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = RetentionClassifierRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();
        supportedActionTargetTypes.add(ActionTarget.NEW_ASSET.getActionTargetType());
        supportedActionTargetTypes.add(ActionTarget.STEWARD.getActionTargetType());

        producedGuards = RetentionClassifierGuard.getGuardTypes();

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
