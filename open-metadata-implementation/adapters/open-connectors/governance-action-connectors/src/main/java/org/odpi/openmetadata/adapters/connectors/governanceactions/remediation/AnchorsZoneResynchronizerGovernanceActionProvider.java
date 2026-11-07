/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;

import java.util.ArrayList;

/**
 * AnchorsZoneResynchronizerGovernanceActionProvider is the OCF connector provider for the Anchors Zone
 * Resynchronizer Governance Action Service.  This is a Remediation Governance Action Service.
 */
public class AnchorsZoneResynchronizerGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String connectorTypeGUID          = "0cda831a-e3c6-4d3f-8c0f-cce0b5ff8e1e";
    private static final String connectorTypeQualifiedName = "Egeria:GovernanceActionService:Remediation:AnchorsZoneResynchronizer";
    private static final String connectorTypeDisplayName   = "Anchors Zone Resynchronizer Governance Action Service";
    private static final String connectorTypeDescription   = "Corrects the governance zones held in Anchors classifications so that they match the zones that govern each element.  " +
                                                             "With action targets, each target is checked, along with the members of any target that is an anchor; " +
                                                             "without them, every element in the repository is checked.";

    private static final String connectorClassName = AnchorsZoneResynchronizerGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * connector implementation.
     */
    public AnchorsZoneResynchronizerGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedActionTargetTypes = new ArrayList<>();
        supportedActionTargetTypes.add(ActionTarget.ANY_ELEMENT.getActionTargetType());

        producedGuards = AnchorsZoneResynchronizerGuard.getGuardTypes();

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;
    }
}
