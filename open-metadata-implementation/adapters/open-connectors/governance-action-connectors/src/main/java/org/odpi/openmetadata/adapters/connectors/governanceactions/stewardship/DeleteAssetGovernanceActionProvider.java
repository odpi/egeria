/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * DeleteAssetGovernanceActionProvider is the OCF connector provider for the "Delete Asset"
 * Governance Action Service.
 */
public class DeleteAssetGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "ab56c5ad-ee01-4486-9940-8409065df5e3";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Stewardship:DeleteAsset";
    private static final String  connectorTypeDisplayName = "Delete Asset Governance Action Service";
    private static final String  connectorTypeDescription = "Governance Action Service that deletes an asset that was created by a template, and passes its GUID as a deleted asset action target.";

    private static final String connectorClassName = DeleteAssetGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public DeleteAssetGovernanceActionProvider()
    {
        super();

        super.supportedRequestParameters = ManageAssetRequestParameter.getRequestParameterTypes();
        super.producedGuards = ManageAssetGuard.getGuardTypes();
        super.producedActionTargetTypes = List.of(ActionTarget.DELETED_ASSET.getActionTargetType());

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
