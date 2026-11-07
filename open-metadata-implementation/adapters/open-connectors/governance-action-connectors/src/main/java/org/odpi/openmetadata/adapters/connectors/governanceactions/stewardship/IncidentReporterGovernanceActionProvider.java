/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * IncidentReporterGovernanceActionProvider is the OCF connector provider for the Incident Reporter Governance Action Service.
 * This is a Stewardship Governance Action Service.
 */
public class IncidentReporterGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "a8298203-c2f3-4030-9b23-4e61e45782cb";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Stewardship:IncidentReporter";
    private static final String  connectorTypeDisplayName = "Incident Reporter Governance Action Service";
    private static final String  connectorTypeDescription = "Creates an incident report using the configured template.  The request parameters fill out the template and the action targets are linked to the report as impacted resources.";

    private static final String connectorClassName = IncidentReporterGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public IncidentReporterGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = IncidentReporterRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = new ArrayList<>();
        supportedActionTargetTypes.add(ActionTarget.ANY_REFERENCEABLE.getActionTargetType());

        producedActionTargetTypes = new ArrayList<>();
        producedActionTargetTypes.add(ActionTarget.NEW_ASSET.getActionTargetType());

        producedGuards = IncidentReporterGuard.getGuardTypes();

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        for (IncidentReporterRequestParameter requestParameter : IncidentReporterRequestParameter.values())
        {
            recognizedConfigurationProperties.add(requestParameter.getName());
        }
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
