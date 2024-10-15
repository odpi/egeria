/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

import java.util.ArrayList;
import java.util.List;

public class CocoClinicalTrialCertifyHospitalProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "c07a2573-f191-459b-b567-831200702b11";
    private static final String  connectorTypeQualifiedName = "CocoPharmaceuticals:GovernanceActionService:ClinicalTrial:CertifyHospital";
    private static final String  connectorTypeDisplayName = "Clinical Trial Certify Hospital Governance Action Service";
    private static final String  connectorTypeDescription = "Adds a certification to the Hospital organization so that its data can be included in a clinical trial.";

    private static final String connectorClassName = CocoClinicalTrialCertifyHospitalService.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CocoClinicalTrialCertifyHospitalProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = CocoClinicalTrialRequestParameter.getCertifyHospitalRequestParameterTypes();
        supportedActionTargetTypes = CocoClinicalTrialActionTarget.getCertifyHospitalActionTargetTypes();

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
