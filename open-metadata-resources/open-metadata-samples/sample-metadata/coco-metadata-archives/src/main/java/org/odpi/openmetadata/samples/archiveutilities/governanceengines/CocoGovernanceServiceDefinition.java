/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.governanceengines;


import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileGovernanceActionProvider;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceProvider;
import org.odpi.openmetadata.samples.archiveutilities.GovernanceActionDescription;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.*;

/**
 * Define the Governance Action Services configuration shipped with Egeria.
 */
public enum CocoGovernanceServiceDefinition
{
    /**
     * Sets up the processes that will govern the clinical trial.
     */
    SET_UP_CLINICAL_TRIAL("61417d87-509b-4ac6-9ff3-45dccd9b8c95",
                          "set-up-clinical-trial-governance-action-service",
                          "Set up new clinical trial",
                          new CocoClinicalTrialSetUpProvider(),
                          ResourceUse.PROVISION_RESOURCE,
                          DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR),

    /**
     * Sets up the storage definitions that support the receipt of weekly patient measurement data for a clinical trial.  This data is accessible through OSS Unity Catalog (UC).
     */
    SET_UP_DATA_LAKE("422d2711-a333-4188-98ca-aa09fcee5a6d",
                          "set-up-data-lake-for-clinical-trial-governance-action-service",
                          "Set up Data Lake to capture weekly patient measurements",
                          new CocoClinicalTrialSetUpDataLakeProvider(),
                          ResourceUse.PROVISION_RESOURCE,
                          DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR),

    /**
     * Checks that the certification type matches the one for the clinical trial project and sets up the certification
     * relationship between the hospital and the certification type.  The start date is null.
     * The certification relationship identifies the people involved in completing the certification process.
     */
    NOMINATE_HOSPITAL("d6f3540f-d187-46bf-9e43-7dc410417ca2",
                      "nominate-hospital-for-clinical-trial-governance-action-service",
                      "Nominate a hospital has legal and data management arrangements in place to capture and supply weekly patient measurements as part of a clinical trial.",
                      new CocoClinicalTrialNominateHospitalProvider(),
                      ResourceUse.PROVISION_RESOURCE,
                      DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR),

    /**
     * Checks that the certification type matches the one for the clinical trial project and sets up the start date
     * in the certification relationship between the hospital and the certification type.
     */
    CERTIFY_HOSPITAL("ababa090-2a39-42cb-8214-6f6b44a10c7d",
                     "certify-hospital-for-clinical-trial-governance-action-service",
                     "Certify that a hospital has legal and data management arrangements in place to capture and supply weekly patient measurements as part of a clinical trial.",
                     new CocoClinicalTrialCertifyHospitalProvider(),
                     ResourceUse.CERTIFY_RESOURCE,
                     DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR),

    /**
     * Sets up the landing area for data from a hospital as part of a clinical trial, along with the pipeline
     * that catalogued the data and moved it into the data lake.
     * The aim is that the data is moved from the landing area as soon as possible.
     */
    HOSPITAL_ONBOARDING("d3c663a9-83d3-4437-bbf7-9df1a62b8fa6",
                        "onboard-hospital-for-clinical-trial-governance-action-service",
                        "Onboard a Hospital into a Clinical Trial Governance Action Service",
                        new CocoClinicalTrialHospitalOnboardingProvider(),
                        ResourceUse.PROVISION_RESOURCE,
                        DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR),

    /**
     * Checks that the schema (format) and values match the required specification.
     */
    WEEKLY_MEASUREMENTS_DATA_QUALITY("580a9730-eab0-4aa9-a786-3a0c544bae40",
                                     "weekly-measurements-data-quality-survey-action-service",
                                     "Validate that the data in a weekly measurements file passes data validation checks.",
                                     new CocoClinicalTrialCertifyWeeklyMeasurementsProvider(),
                                     ResourceUse.VALIDATE_RESOURCE,
                                     DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR),

    ;

    private final String            guid;
    private final String            name;
    private final String            displayName;
    private final GovernanceServiceProviderBase connectorProvider;
    private final ResourceUse resourceUse;
    private final DeployedImplementationType deployedImplementationType;

    CocoGovernanceServiceDefinition(String                        guid,
                                    String                        name,
                                    String                        displayName,
                                    GovernanceServiceProviderBase connectorProvider,
                                    ResourceUse                   resourceUse,
                                    DeployedImplementationType    deployedImplementationType)
    {
        this.guid              = guid;
        this.name              = name;
        this.displayName       = displayName;
        this.connectorProvider = connectorProvider;
        this.resourceUse       = resourceUse;
        this.deployedImplementationType = deployedImplementationType;
    }

    /**
     * Return the unique identifier of the governance service.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the unique name of the governance service.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the display name of the governance service.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the governance service.
     *
     * @return string
     */
    public String getDescription()
    {
        return connectorProvider.getConnectorType().getDescription();
    }


    /**
     * Return the name of the governance service provider implementation class.
     *
     * @return string
     */
    public String getConnectorProviderClassName()
    {
        return connectorProvider.getClass().getName();
    }


    /**
     * Return the deployed implementation type for the service.
     *
     * @return DeployedImplementationType enum
     */
    public DeployedImplementationType getDeployedImplementationType()
    {
        return deployedImplementationType;
    }

    /**
     * Create a governance action description from the governance service's provider.
     *
     * @return governance action description
     */
    public GovernanceActionDescription getGovernanceActionDescription()
    {
        GovernanceActionDescription governanceActionDescription = new GovernanceActionDescription();

        governanceActionDescription.governanceServiceGUID        = guid;
        governanceActionDescription.resourceUse                  = resourceUse;
        governanceActionDescription.supportedTechnologies        = connectorProvider.getSupportedTechnologyTypes();
        governanceActionDescription.supportedRequestTypes        = connectorProvider.getSupportedRequestTypes();
        governanceActionDescription.supportedRequestParameters   = connectorProvider.getSupportedRequestParameters();
        governanceActionDescription.supportedActionTargets       = connectorProvider.getSupportedActionTargetTypes();
        governanceActionDescription.producedRequestParameters    = connectorProvider.getProducedRequestParameters();
        governanceActionDescription.producedActionTargets        = connectorProvider.getProducedActionTargetTypes();
        governanceActionDescription.producedGuards               = connectorProvider.getProducedGuards();

        if (connectorProvider instanceof SurveyActionServiceProvider surveyActionServiceProvider)
        {
            governanceActionDescription.supportedAnalysisSteps = surveyActionServiceProvider.getSupportedAnalysisSteps();
            governanceActionDescription.supportedAnnotationTypes = surveyActionServiceProvider.getProducedAnnotationTypes();
        }

        governanceActionDescription.governanceServiceDescription = this.getDescription();

        return governanceActionDescription;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "GovernanceServiceDefinition{" + "name='" + name + '\'' + "}";
    }
}
