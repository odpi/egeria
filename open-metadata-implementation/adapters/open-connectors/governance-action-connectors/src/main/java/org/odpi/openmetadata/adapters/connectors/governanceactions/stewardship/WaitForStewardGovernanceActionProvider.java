/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.surveyaction.controls.SurveyActionTarget;

/**
 * EvaluateAnnotationsGovernanceActionProvider is the OCF connector provider for the "Evaluate Annotations"
 * Governance Action Service.
 */
public class WaitForStewardGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "18d2ee53-5f1c-440e-827d-e141cb7b53db";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Stewardship:WaitForSteward";
    private static final String  connectorTypeDisplayName = "Wait For Steward Governance Action Service";
    private static final String  connectorTypeDescription = "Governance Action Service that waits for a steward to complete a to do.";


    private static final String connectorClassName = WaitForStewardGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public WaitForStewardGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedActionTargetTypes = SurveyActionTarget.getStewardshipHandoverActionTargetTypes();

        producedGuards = WaitForStewardGuard.getGuardTypes();

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
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
