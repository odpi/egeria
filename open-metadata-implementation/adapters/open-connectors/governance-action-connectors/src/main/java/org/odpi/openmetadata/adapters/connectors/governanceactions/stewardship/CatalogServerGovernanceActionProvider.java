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

/**
 * EvaluateAnnotationsGovernanceActionProvider is the OCF connector provider for the "Evaluate Annotations"
 * Governance Action Service.
 */
public class CatalogServerGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "e55f9c8d-188e-4ffd-b9c8-fe980fefa7cf";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Stewardship:CatalogServer";
    private static final String  connectorTypeDisplayName = "Catalog Server Governance Action Service";
    private static final String  connectorTypeDescription = "Governance Action Service that attaches a software server entity it to the appropriate integration connector as a catalog target.";

    private static final String connectorClassName = CatalogServerGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CatalogServerGovernanceActionProvider()
    {
        super();

        super.supportedRequestParameters = CreateServerRequestParameter.getRequestParameterTypes();
        super.producedGuards = CatalogServerGuard.getGuardTypes();
        super.supportedActionTargetTypes = Arrays.asList(new ActionTargetType[]{ActionTarget.INTEGRATION_CONNECTOR.getActionTargetType()});
        super.producedActionTargetTypes = Arrays.asList(new ActionTargetType[]{ActionTarget.NEW_ASSET.getActionTargetType()});

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

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.SOFTWARE_SERVER});
    }
}
