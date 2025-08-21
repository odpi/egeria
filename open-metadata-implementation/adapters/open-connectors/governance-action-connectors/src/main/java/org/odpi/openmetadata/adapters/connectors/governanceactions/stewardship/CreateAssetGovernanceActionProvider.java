/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.List;

/**
 * CreateAssetGovernanceActionProvider is the OCF connector provider for the "Create Asset"
 * Governance Action Service.
 */
public class CreateAssetGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "e3067653-363b-4851-a839-0bf0b421cac7";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Stewardship:CreateAsset";
    private static final String  connectorTypeDisplayName = "Create Asset Governance Action Service";
    private static final String  connectorTypeDescription = "Governance Action Service that creates an asset and passes its GUID as a new asset action target.";

    private static final String connectorClassName = CreateAssetGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CreateAssetGovernanceActionProvider()
    {
        super();

        super.supportedRequestParameters = ManageAssetRequestParameter.getRequestParameterTypes();
        super.producedGuards = ManageAssetGuard.getGuardTypes();
        super.producedActionTargetTypes = List.of(ActionTarget.NEW_ASSET.getActionTargetType());

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

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{
                DeployedImplementationType.SOFTWARE_SERVER,
                DeployedImplementationType.DATA_ASSET});

    }
}
