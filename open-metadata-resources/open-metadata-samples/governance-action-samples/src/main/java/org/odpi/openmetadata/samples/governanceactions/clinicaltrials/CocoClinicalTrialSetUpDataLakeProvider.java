/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;

public class CocoClinicalTrialSetUpDataLakeProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "2e82b30e-0c31-4cf0-82a8-9f84afe63e8a";
    private static final String  connectorTypeQualifiedName = "CocoPharmaceuticals:GovernanceActionService:ClinicalTrial:InitiateDataLake";
    private static final String  connectorTypeDisplayName = "Clinical Trial Initiate Data Lake Governance Action Service";
    private static final String  connectorTypeDescription = "Sets up the storage definitions that support the receipt of weekly patient measurement data for a clinical trial.  This data is accessible through OSS Unity Catalog (UC).";

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
