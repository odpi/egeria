/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;

/**
 * DaysOfWeekGovernanceActionProvider is the OCF connector provider for the "Days Of the Week" Governance Action Service.
 */
public class DaysOfWeekGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "17a118aa-d7fc-4356-a9a7-04f1d93885c8";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Classification:DaysOfWeek";
    private static final String  connectorTypeDisplayName = "Days of Week Governance Action Service";
    private static final String  connectorTypeDescription = "Outputs guards based on current day of week.";


    private static final String connectorClassName = DaysOfWeekGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public DaysOfWeekGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        producedGuards = DaysOfWeekGuard.getGuardTypes();

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
    }
}
