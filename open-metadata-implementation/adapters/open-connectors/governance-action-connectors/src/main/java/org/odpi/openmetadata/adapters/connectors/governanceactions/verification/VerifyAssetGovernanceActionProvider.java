/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;

import java.util.ArrayList;

/**
 * VerifyAssetGovernanceActionProvider is the OCF connector provider for the "Verify Asset" Governance Action Service.
 * This is a Verification Governance Action Service.
 */
public class VerifyAssetGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "26df5406-8c4b-4738-96f6-1aea5dfd71de";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Verification:VerifyAsset";
    private static final String  connectorTypeDisplayName = "Verify that an asset is correctly set up.";
    private static final String  connectorTypeDescription = "This governance service checks the classifications for an Asset to ensure it has governance zones, an owner and an origin set up.";

    private static final String connectorClassName = VerifyAssetGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public VerifyAssetGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedActionTargetTypes = new ArrayList<>();
        supportedActionTargetTypes.add(ActionTarget.ANY_ASSET.getActionTargetType());

        producedGuards = VerifyAssetGuard.getGuardTypes();

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
