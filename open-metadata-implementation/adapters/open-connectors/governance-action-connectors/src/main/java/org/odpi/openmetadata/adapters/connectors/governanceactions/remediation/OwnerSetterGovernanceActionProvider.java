/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * OwnerSetterGovernanceActionProvider is the OCF connector provider for the Owner Setter Governance Action Service.
 * This is a Remediation Governance Action Service.
 */
public class OwnerSetterGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "6a6d5b6d-ef29-417f-9d83-c586d94bf04f";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Remediation:OwnerSetter";
    private static final String  connectorTypeDisplayName = "Owner Setter Governance Action Service";
    private static final String  connectorTypeDescription = "Sets the Ownership classification on the requested element(s) using the owner from the configuration properties and request parameters.";

    private static final String connectorClassName = OwnerSetterGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public OwnerSetterGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = OwnerSetterRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();
        supportedActionTargetTypes.add(ActionTarget.ANY_REFERENCEABLE.getActionTargetType());

        producedGuards = OwnerSetterGuard.getGuardTypes();

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        for (OwnerSetterRequestParameter requestParameter : OwnerSetterRequestParameter.values())
        {
            recognizedConfigurationProperties.add(requestParameter.getName());
        }
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
