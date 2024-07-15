/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

public class CocoClinicalTrialHospitalOnboardingProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "c6186776-aef8-4444-8a88-d613a5867177";
    private static final String  connectorTypeQualifiedName = "CocoPharmaceuticals:GovernanceActionService:ClinicalTrial:OnboardHospital";
    private static final String  connectorTypeDisplayName = "Clinical Trial Hospital Onboarding Governance Action Service";
    private static final String  connectorTypeDescription = "Sets up the data onboarding mechanisms for a new hospital joining a clinical trial.";

    private static final String connectorClassName = CocoClinicalTrialHospitalOnboardingService.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CocoClinicalTrialHospitalOnboardingProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedActionTargetTypes = CocoClinicalTrialActionTarget.getHospitalActionTargetTypes();

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

        super.connectorTypeBean = connectorType;
    }
}
