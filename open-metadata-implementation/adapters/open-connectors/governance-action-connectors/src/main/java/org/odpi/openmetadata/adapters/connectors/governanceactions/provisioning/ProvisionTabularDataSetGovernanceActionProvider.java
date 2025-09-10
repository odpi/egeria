/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.List;

/**
 * ProvisionTabularDataSetGovernanceActionProvider is the OCF connector provider for the Tabular Data Provisioning Governance Action Service.
 * This is a Provisioning Governance Action Service.
 */
public class ProvisionTabularDataSetGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID          = "e789b16e-2a45-4c40-b2b8-91624cd72527";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Provisioning:TabularDataSet";
    private static final String  connectorTypeDisplayName   = "Tabular Data Set Provisioning Governance Action Service";
    private static final String  connectorTypeDescription   = "Copies data from one tabular data set to another.";



    private static final String connectorClassName = ProvisionTabularDataSetGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public ProvisionTabularDataSetGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = ProvisionTabularDataSetRequestParameter.getRequestParameterTypes();

        supportedActionTargetTypes = ProvisionTabularDatasetActionTarget.getActionTargetTypes();

        producedGuards = ProvisionTabularDataSetGuard.getGuardTypes();

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
