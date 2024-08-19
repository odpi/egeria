/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;

/**
 * OriginSeekerGovernanceActionProvider is the OCF connector provider for the Origin Seeker Governance Action Service.
 * This is a Remediation Governance Action Service.
 */
public class OriginSeekerGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "1c6939c4-de2c-44aa-a044-0ec64df0560f";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Remediation:OriginSeeker";
    private static final String  connectorTypeDisplayName = "Origin Seeker Governance Action Service";
    private static final String  connectorTypeDescription = "Follows the lineage mapping for an action target element to determine its origin.";

    /**
     * Name of the target action where the asset's unique identifier.
     */
    private static final String ACTION_TARGET_NAME      = "assetGUID";

    private static final String connectorClassName = OriginSeekerGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public OriginSeekerGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        producedGuards = OriginSeekerGuard.getGuardTypes();

        supportedActionTargetTypes = new ArrayList<>();
        super.supportedActionTargetTypes.add(ActionTarget.NEW_ASSET.getActionTargetType());

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
