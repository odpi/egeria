/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

public class CocoClinicalTrialNominateHospitalProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "f89e7b05-d05b-449f-8c5c-d5dd7833acec";
    private static final String  connectorTypeQualifiedName = "CocoPharmaceuticals:GovernanceActionService:ClinicalTrial:NominateHospital";
    private static final String  connectorTypeDisplayName = "Clinical Trial Nominate Hospital Governance Action Service";
    private static final String  connectorTypeDescription = "Adds an incomplete certification relationship the Hospital organization that identifies the hospital contacts that will complete the certification process.  Any previous certifications tha the hospital has to this clinical trial are cancelled (ended).";

    private static final String connectorClassName = CocoClinicalTrialNominateHospitalService.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CocoClinicalTrialNominateHospitalProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = CocoClinicalTrialRequestParameter.getNominateHospitalRequestParameterTypes();
        supportedActionTargetTypes = CocoClinicalTrialActionTarget.getNominateHospitalActionTargetTypes();

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
