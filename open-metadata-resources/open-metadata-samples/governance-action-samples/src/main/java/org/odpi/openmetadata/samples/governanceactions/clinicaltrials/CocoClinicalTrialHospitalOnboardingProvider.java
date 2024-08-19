/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

public class CocoClinicalTrialHospitalOnboardingProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "a2963773-e3e3-4d46-92c9-cb836f71a751";
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

        supportedActionTargetTypes = CocoClinicalTrialActionTarget.getHospitalOnboardingActionTargetTypes();
        supportedRequestParameters = CocoClinicalTrialRequestParameter.getHospitalOnboardingRequestParameterTypes();

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
