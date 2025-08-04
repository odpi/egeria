/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * This governance action service sets up the processes for a new clinical trial.
 * It takes details about the project, hospitals involved and the Data Lake Catalog
 * to use.  It creates a set of specific processes for running the clinical trial
 * that includes many of the properties that are pre-defined.
 */
public class CocoClinicalTrialSetUpProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "b8f96da4-5f01-40bf-89e4-5d631574d12c";
    private static final String  connectorTypeQualifiedName = "CocoPharmaceuticals:GovernanceActionService:ClinicalTrial:SetUp";
    private static final String  connectorTypeDisplayName = "Clinical Trial Set Up Governance Action Service";
    private static final String  connectorTypeDescription = "Sets up the processes for a new clinical trial. It takes details about the project, hospitals involved and the Data Lake Catalog to use.  It creates a set of specific processes for running the clinical trial that includes many of the properties that are pre-defined.";

    private static final String connectorClassName = CocoClinicalTrialSetUpService.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CocoClinicalTrialSetUpProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = CocoClinicalTrialRequestParameter.getClinicalTrialSetUpRequestParameterTypes();
        supportedActionTargetTypes = CocoClinicalTrialActionTarget.getClinicalTrialSetupActionTargetTypes();

        producedGuards = CocoClinicalTrialGuard.getGuardTypes();

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
