/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.verification;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * TypeDetectorGovernanceActionProvider is the OCF connector provider for the Type Detector Governance Action Service.
 * This is a Verification Governance Action Service.
 */
public class TypeDetectorGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "b47cd071-6745-4799-b7ad-2ef210fb00cd";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Verification:TypeDetector";
    private static final String  connectorTypeDisplayName = "Type Detector Governance Action Service";
    private static final String  connectorTypeDescription = "Checks that the type of each action target is one of the acceptable types from the configuration properties and request parameters, and outputs a guard that reports the result.";

    private static final String connectorClassName = TypeDetectorGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public TypeDetectorGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = TypeDetectorRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();
        supportedActionTargetTypes.add(ActionTarget.ANY_REFERENCEABLE.getActionTargetType());

        producedGuards = TypeDetectorGuard.getGuardTypes();

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        for (TypeDetectorRequestParameter requestParameter : TypeDetectorRequestParameter.values())
        {
            recognizedConfigurationProperties.add(requestParameter.getName());
        }
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
