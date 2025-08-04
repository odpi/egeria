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
 * CatalogTargetAssetGovernanceActionProvider is the OCF connector provider for the "catalog-asset"
 * Governance Action Service.
 */
public class CatalogTargetAssetGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "e55f9c8d-188e-4ffd-b9c8-fe980fefa7cf";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Stewardship:CatalogAsset";
    private static final String  connectorTypeDisplayName = "Catalog Asset Governance Action Service";
    private static final String  connectorTypeDescription = "Governance Action Service that attaches an asset entity it to the appropriate integration connector as a catalog target.";

    private static final String connectorClassName = CatalogTargetAssetGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CatalogTargetAssetGovernanceActionProvider()
    {
        super();

        super.supportedRequestParameters = ManageAssetRequestParameter.getRequestParameterTypes();
        super.producedGuards = CatalogTargetAssetGuard.getGuardTypes();
        super.supportedActionTargetTypes = List.of(ActionTarget.INTEGRATION_CONNECTOR.getActionTargetType());
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
