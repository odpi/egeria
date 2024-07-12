/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;

import java.util.ArrayList;
import java.util.List;

public class CocoClinicalTrialSetUpDataLakeProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "4afcfed3-b0b9-456a-8983-cb84568c3cd9";
    private static final String  connectorTypeQualifiedName = "CocoPharmaceuticals:GovernanceActionService:ClinicalTrial:InitiateDataLake";
    private static final String  connectorTypeDisplayName = "Clinical Trial Initiate Data Lake Governance Action Service";
    private static final String  connectorTypeDescription = "Sets up the data lake to receive data for a new clinical trial.";

    private static final String connectorClassName = CocoClinicalTrialSetUpDataLakeService.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CocoClinicalTrialSetUpDataLakeProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = CocoClinicalTrialRequestParameter.getSetUpDataLakeRequestParameterTypes();

        supportedActionTargetTypes = CocoClinicalTrialActionTarget.getSetUpDataLakeActionTargetTypes();

        producedGuards = CocoClinicalTrialGuard.getGuardTypes();

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

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(CocoClinicalTrialRequestParameter.DATA_LAKE_CATALOG.getName());
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
