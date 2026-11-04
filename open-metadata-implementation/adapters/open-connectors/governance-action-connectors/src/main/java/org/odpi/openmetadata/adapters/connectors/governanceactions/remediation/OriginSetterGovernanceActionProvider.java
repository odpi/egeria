/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * OriginSetterGovernanceActionProvider is the OCF connector provider for the Origin Setter Governance Action Service.
 * This is a Remediation Governance Action Service.
 */
public class OriginSetterGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "3e5360b3-a6b9-4173-b174-a667687e3547";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Remediation:OriginSetter";
    private static final String  connectorTypeDisplayName = "Origin Setter Governance Action Service";
    private static final String  connectorTypeDescription = "Sets the DigitalResourceOrigin classification on the requested element(s) using the origin from the configuration properties and request parameters.";

    private static final String connectorClassName = OriginSetterGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public OriginSetterGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = OriginSetterRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();
        supportedActionTargetTypes.add(ActionTarget.ANY_REFERENCEABLE.getActionTargetType());

        producedGuards = OriginSetterGuard.getGuardTypes();

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        for (OriginSetterRequestParameter requestParameter : OriginSetterRequestParameter.values())
        {
            recognizedConfigurationProperties.add(requestParameter.getName());
        }
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
