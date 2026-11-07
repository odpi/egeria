/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * StewardInformerGovernanceActionProvider is the OCF connector provider for the Steward Informer Governance Action Service.
 * This is a Stewardship Governance Action Service.
 */
public class StewardInformerGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "2fec1619-2648-426c-99f5-5fd41daf6327";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Stewardship:StewardInformer";
    private static final String  connectorTypeDisplayName = "Steward Informer Governance Action Service";
    private static final String  connectorTypeDescription = "Creates a to do for a steward using the configured template.  The request parameters fill out the template and the action targets are attached to the to do.";

    private static final String connectorClassName = StewardInformerGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public StewardInformerGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = StewardInformerRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();
        supportedActionTargetTypes.add(ActionTarget.STEWARD.getActionTargetType());
        supportedActionTargetTypes.add(ActionTarget.SPONSOR.getActionTargetType());
        supportedActionTargetTypes.add(ActionTarget.ANY_REFERENCEABLE.getActionTargetType());

        producedActionTargetTypes = new ArrayList<>();
        producedActionTargetTypes.add(ActionTarget.NEW_ASSET.getActionTargetType());

        producedGuards = StewardInformerGuard.getGuardTypes();

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        for (StewardInformerRequestParameter requestParameter : StewardInformerRequestParameter.values())
        {
            recognizedConfigurationProperties.add(requestParameter.getName());
        }
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
