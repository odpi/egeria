/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ActionTarget provides some standard action type names to use in the implementation of governance services.
 * Using standard action target names helps to simplify the use of the governance services in
 * governance action processes.
 */
public enum CocoClinicalTrialActionTarget
{
    /**
     * Project used to control the clinical trial.
     */
    PARENT_PROJECT("clinicalTrialParentProject",
            "Campaign used to control all clinical trials.",
            OpenMetadataType.PROJECT.typeName,
            null,
            true),

    /**
     * Project used to control the clinical trial.
     */
    PROJECT("clinicalTrialProject",
            "Project used to control the clinical trial.",
            OpenMetadataType.PROJECT.typeName,
            null,
            true),

    DATA_SPEC_PROJECT("clinicalTrialDataSpecProject",
                      "Effort to develop the data specification needed by the clinical trial.",
                      OpenMetadataType.PROJECT.typeName,
                      null,
                      true),

    DATA_SHARING_AGREEMENT_PROJECT("clinicalTrialDataSharingAgreementProject",
                      "Effort to develop the baseline data sharing agreement with hospitals and patients needed by the clinical trial.",
                      OpenMetadataType.PROJECT.typeName,
                      null,
                      true),

    DEV_PROJECT("clinicalTrialComponentsProject",
                "Effort to develop the specific templates and software components that support the data specification.",
                OpenMetadataType.PROJECT.typeName,
                null,
                true),

    ONBOARD_PIPELINE_PROJECT("clinicalTrialOnboardPipelinesProject",
                "Effort to deploy the onboarding pipelines to bring data from the hospitals into the data lake.",
                OpenMetadataType.PROJECT.typeName,
                null,
                true),

    TEMPLATE_PROJECT("clinicalTrialTemplateProject",
                     "Effort to build out the templates used throughout the clinical trial that support the desired data specification.",
                     OpenMetadataType.PROJECT.typeName,
                     null,
                     true),

    DQ_PROJECT("clinicalTrialDataQualityImplementation",
               "Effort to develop the data quality surveyors needed to validate data that is being introduced into the clinical trial.",
               OpenMetadataType.PROJECT.typeName,
               null,
               true),

    ANALYSIS_PROJECT("clinicalTrialAnalysis",
               "Effort to analyse the data coming from the clinical trial to determine efficacy and side-effects.",
               OpenMetadataType.PROJECT.typeName,
               null,
               true),

    HOSPITAL_MANAGEMENT_PROJECT("clinicalTrialHospitalManagement",
                     "Effort to manage the relationship, data sharing agreements and certifications with the hospitals.",
                     OpenMetadataType.PROJECT.typeName,
                     null,
                     true),

    DATA_SPEC("dataSpecification",
              "Data specification describing the data used in the clinical trial.",
              OpenMetadataType.COLLECTION.typeName,
              null,
              true),

    /**
     * Hospital that will be supplying data for the clinical trial.
     */
    HOSPITAL("hospital",
            "Hospital that will be supplying data for the clinical trial.",
            OpenMetadataType.ORGANIZATION.typeName,
            null,
            true),

    /**
     * The person who can be contacted if there are problems with the data from this hospital.
     */
    CONTACT_PERSON("hospitalContactPerson",
                   "The person who can be contacted if there are problems with the data from this hospital.",
                   OpenMetadataType.PERSON.typeName,
                   null,
                   true),

    /**
     * The person who can be contacted if there are problems with the data from this hospital.
     */
    CLINICAL_TRIAL_OWNER("clinicalTrialOwner",
                         "The person who is accountable for the correct management of the clinical trial.",
                         OpenMetadataType.ACTOR.typeName,
                         null,
                         true),

    /**
     * The person who can be contacted if there are problems with the data from this hospital.
     */
    CLINICAL_TRIAL_MANAGER("clinicalTrialManager",
                           "The person who manages the relationships and agreements with the hospitals and patients.",
                           OpenMetadataType.ACTOR.typeName,
                           null,
                           true),

    /**
     * Person responsible for the delivery of the new templates and components for the clinical trial.
     */
    IT_PROJECT_MANAGER("itProjectManager",
                       "Person responsible for the delivery of the new templates and components for the clinical trial.",
                       OpenMetadataType.ACTOR.typeName,
                       null,
                       true),

    /**
     * Person responsible for the design of the data stores and templates, plus management of the data pipelines.
     */
    DATA_ENGINEER("dataEngineer",
                  "Person responsible for the design of the data stores and templates, plus management of the data pipelines.",
                  OpenMetadataType.ACTOR.typeName,
                  null,
                  true),

    /**
     * Person building the data quality evaluation components.
     */
    INTEGRATION_DEVELOPER("integrationDeveloper",
                          "Person building the data quality evaluation components.",
                          OpenMetadataType.ACTOR.typeName,
                          null,
                          true),

    /**
     * Person responsible for the analysis of the clinical trial data.
     */
    DATA_SCIENTIST("dataScientist",
                          "Person responsible for the analysis of the clinical trial data.",
                          OpenMetadataType.ACTOR.typeName,
                          null,
                          true),

    /**
     * Catalog where data for clinical trial is to reside.
     */
    CATALOG("dataLakeCatalog",
            "Schema where the weekly measurements volume is to reside.",
            UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName(),
            UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType(),
            true),

    /**
     * Integration connector that maintains the last update date for the volume.
     */
    LAST_UPDATE_CONNECTOR("lastUpdateConnector",
                          "Integration connector that maintains the last update date for the volume.",
                          DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                          DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                          false),

    /**
     * Integration connector that maintains the last update date for the volume.
     */
    LANDING_AREA_CONNECTOR("landingAreaConnector",
                          "Integration connector that maintains the last update date for the volume.",
                           DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                           DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType(),
                          true),

    /**
     * The type of licences granted by the hospital for data that originates from them for this clinical trial.
     */
    HOSPITAL_DATA_LICENSE_TYPE("hospitalDataLicenseType",
                               "The type of licences granted by the hospital for data that originates from them for this clinical trial.",
                               OpenMetadataType.LICENSE_TYPE.typeName,
                               null,
                               true),

    /**
     * A data processing purpose that has been approved for data from this hospital.  It is a combination of permitted purposes from the license, and Coco Pharmaceutical's own governance practices.
     */
    APPROVED_PURPOSES("approvedPurpose",
                      "A data processing purpose that has been approved for data from this hospital.  It is a combination of permitted purposes from the license, and Coco Pharmaceutical's own governance practices.",
                      OpenMetadataType.DATA_PROCESSING_PURPOSE.typeName,
                      null,
                      false),

    /**
     * The certification type that should be used to certify the clinical trial.
     */
    CLINICAL_TRIAL_CERTIFICATION_TYPE("clinicalTrialCertificationType",
                                "The certification type that should be used to certify the clinical trial as approved.",
                                OpenMetadataType.CERTIFICATION_TYPE.typeName,
                                null,
                                true),

    /**
     * The template to create the certification type that should be used to certify the hospital.
     */
    HOSPITAL_CERTIFICATION_TYPE_TEMPLATE("hospitalCertificationTypeTemplate",
                                "The template to create the certification type that should be used to certify the hospital.",
                                OpenMetadataType.CERTIFICATION_TYPE.typeName,
                                null,
                                true),

    /**
     * The certification type that should be used to certify the hospital.
     */
    HOSPITAL_CERTIFICATION_TYPE("hospitalCertificationType",
                                "The certification type that should be used to certify the hospital.",
                                OpenMetadataType.CERTIFICATION_TYPE.typeName,
                                null,
                                true),

    /**
     * The template to create the certification type that should be used to certify the hospital.
     */
    DATA_QUALITY_CERTIFICATION_TYPE_TEMPLATE("dataQualityCertificationTypeTemplate",
                                    "The template to create the certification type that should be used to certify that data from the hospital conforms to specification.",
                                    OpenMetadataType.CERTIFICATION_TYPE.typeName,
                                    null,
                                    true),

    /**
     * The certification type that should be used to certify the hospital.
     */
    DATA_QUALITY_CERTIFICATION_TYPE("dataQualityCertificationType",
                                "The certification type that should be used to certify that data from the hospital conforms to specification.",
                                OpenMetadataType.CERTIFICATION_TYPE.typeName,
                                null,
                                true),

    GENERIC_ONBOARDING_PIPELINE("onboardingPipeline",
                                "The standard onboarding pipeline that brings data from the landing area to the data lake that is not customized for a project or hospital.",
                                OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                null,
                                true),

    GENERIC_HOSPITAL_NOMINATION_GAT("hospitalNominationGovernanceActionType",
                                    "The Governance Action Type that tentatively adds a hospital to a clinical trial.",
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName,
                                    null,
                                    true),

    GENERIC_HOSPITAL_CERTIFICATION_GAT("hospitalCertificationGovernanceActionType",
                                       "The Governance Action Type that implements the hospital certification process.",
                                       OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName,
                                       null,
                                       true),

    GENERIC_HOSPITAL_ONBOARDING_GAT("hospitalOnboardingGovernanceActionType",
                                       "The Governance Action Type that implements the hospital certification process.",
                                       OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName,
                                       null,
                                       true),

    GENERIC_SET_UP_DATA_LAKE_GAT("setUpDataLakeGovernanceActionType",
                                    "The Governance Action Type that sets up the volume in the data lake catalog.",
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName,
                                    null,
                                    true),

    ONBOARD_HOSPITAL_PROCESS("onboardHospitalProcess",
                                 "The Governance Action Process that sets up the onboarding pipeline.  SetUpDataLake needs to pass the destination directory onto this process.",
                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                 null,
                                 true),


    VALIDATED_WEEKLY_FILES_DATA_SET("validatedWeeklyFilesDataSet",
                                    "The data set that accumulates the list of weekly measurement files that are validated.",
                                    OpenMetadataType.DATA_FILE_COLLECTION.typeName,
                                    null,
                                    true),

    INFORMATION_SUPPLY_CHAIN_TEMPLATE("informationSupplyChainTemplate",
                                      "The template to create the information supply chain for this clinical trial.",
                                      OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName,
                                      null,
                                      true),

    INFORMATION_SUPPLY_CHAIN("informationSupplyChain",
                             "The information supply chain for this clinical trial.",
                             OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName,
                             null,
                             true),

    ;

    /**
     * Action target name
     */
    public final String name;

    /**
     * Description of the action target.
     */
    public final String description;

    /**
     * The open metadata type name of the element that can be this type of action target.
     */
    public final String typeName;

    /**
     * The deployed implementation type allows the service to be more specific about the resources it works with.
     */
    private final String deployedImplementationType;

    /**
     * Is this ActionTarget required for the service to work successfully.
     */
    private final boolean required;


    /**
     * Constructor for the enum.
     *
     * @param name ActionTargetName
     * @param description description of action target
     * @param typeName Open Metadata Type Name for action target
     * @param deployedImplementationType optional deployed implementation type
     * @param required Is this ActionTarget required for the service to work successfully.
     */
    CocoClinicalTrialActionTarget(String  name,
                                  String  description,
                                  String  typeName,
                                  String  deployedImplementationType,
                                  boolean required)
    {
        this.name                       = name;
        this.description                = description;
        this.typeName                   = typeName;
        this.deployedImplementationType = deployedImplementationType;
        this.required                   = required;
    }



    /**
     * Return the action targets defined for this service.
     *
     * @return list
     */
    public static List<ActionTargetType> getClinicalTrialSetupActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        actionTargetTypes.add(PARENT_PROJECT.getActionTargetType());
        actionTargetTypes.add(CLINICAL_TRIAL_OWNER.getActionTargetType());
        actionTargetTypes.add(CLINICAL_TRIAL_MANAGER.getActionTargetType());
        actionTargetTypes.add(DATA_SCIENTIST.getActionTargetType());
        actionTargetTypes.add(IT_PROJECT_MANAGER.getActionTargetType());
        actionTargetTypes.add(DATA_ENGINEER.getActionTargetType());
        actionTargetTypes.add(INTEGRATION_DEVELOPER.getActionTargetType());


        /*
         * Set up in CocoCombo (see CocoRequestTypeDefinition)
         */
        actionTargetTypes.add(HOSPITAL_CERTIFICATION_TYPE_TEMPLATE.getActionTargetType());
        actionTargetTypes.add(DATA_QUALITY_CERTIFICATION_TYPE_TEMPLATE.getActionTargetType());
        actionTargetTypes.add(INFORMATION_SUPPLY_CHAIN_TEMPLATE.getActionTargetType());

        actionTargetTypes.add(GENERIC_HOSPITAL_ONBOARDING_GAT.getActionTargetType());
        actionTargetTypes.add(GENERIC_HOSPITAL_NOMINATION_GAT.getActionTargetType());
        actionTargetTypes.add(GENERIC_HOSPITAL_CERTIFICATION_GAT.getActionTargetType());
        actionTargetTypes.add(GENERIC_SET_UP_DATA_LAKE_GAT.getActionTargetType());

        actionTargetTypes.add(LANDING_AREA_CONNECTOR.getActionTargetType());
        actionTargetTypes.add(LAST_UPDATE_CONNECTOR.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return the action targets defined for this service.
     *
     * @return list
     */
    public static List<ActionTargetType> getNominateHospitalActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        /*
         * Predefined by ClinicalTrialSetup
         */
        actionTargetTypes.add(PROJECT.getActionTargetType());
        actionTargetTypes.add(CLINICAL_TRIAL_MANAGER.getActionTargetType());
        actionTargetTypes.add(CLINICAL_TRIAL_OWNER.getActionTargetType());
        actionTargetTypes.add(HOSPITAL_CERTIFICATION_TYPE.getActionTargetType());

        /*
         * Supplied by caller
         */
        actionTargetTypes.add(HOSPITAL.getActionTargetType());
        actionTargetTypes.add(CONTACT_PERSON.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return the action targets defined for this service.
     *
     * @return list
     */
    public static List<ActionTargetType> getCertifyHospitalActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        /*
         * Predefined by ClinicalTrialSetup
         */
        actionTargetTypes.add(PROJECT.getActionTargetType());
        actionTargetTypes.add(HOSPITAL_CERTIFICATION_TYPE.getActionTargetType());
        actionTargetTypes.add(CLINICAL_TRIAL_MANAGER.getActionTargetType());

        /*
         * Supplied by caller
         */
        actionTargetTypes.add(HOSPITAL.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return the action targets defined for this service.
     *
     * @return list
     */
    public static List<ActionTargetType> getSetUpDataLakeActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        /*
         * All predefined by ClinicalTrialSetUp
         */
        actionTargetTypes.add(PROJECT.getActionTargetType());
        actionTargetTypes.add(DATA_ENGINEER.getActionTargetType());
        actionTargetTypes.add(LAST_UPDATE_CONNECTOR.getActionTargetType());
        actionTargetTypes.add(ONBOARD_HOSPITAL_PROCESS.getActionTargetType()); // pass on destination folder
        actionTargetTypes.add(GENERIC_ONBOARDING_PIPELINE.getActionTargetType());

        /*
         * Supplied by caller
         */
        actionTargetTypes.add(CATALOG.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return the action targets defined for this service.
     *
     * @return list
     */
    public static List<ActionTargetType> getHospitalOnboardingActionTargetTypes()
    {
        Map<String, ActionTargetType> actionTargetTypes = new HashMap<>();

        for (ActionTargetType actionTargetType : getCertifyWeeklyMeasurementsActionTargetTypes())
        {
            actionTargetTypes.put(actionTargetType.getName(), actionTargetType);
        }

        /*
         * Predefined by ClinicalTrialSetUp
         */
        actionTargetTypes.put(PROJECT.getActionTargetType().getName(),
                              PROJECT.getActionTargetType());
        actionTargetTypes.put(HOSPITAL_CERTIFICATION_TYPE.getActionTargetType().getName(),
                              HOSPITAL_CERTIFICATION_TYPE.getActionTargetType());
        actionTargetTypes.put(LANDING_AREA_CONNECTOR.getActionTargetType().getName(),
                              LANDING_AREA_CONNECTOR.getActionTargetType());
        actionTargetTypes.put(DATA_ENGINEER.getActionTargetType().getName(),
                              DATA_ENGINEER.getActionTargetType());

        /*
         * Predefined by DataLake SetUp
         */
        actionTargetTypes.put(GENERIC_ONBOARDING_PIPELINE.getActionTargetType().getName(),
                              GENERIC_ONBOARDING_PIPELINE.getActionTargetType());
        actionTargetTypes.put(VALIDATED_WEEKLY_FILES_DATA_SET.getActionTargetType().getName(),
                              VALIDATED_WEEKLY_FILES_DATA_SET.getActionTargetType());

        /*
         * Supplied by the caller
         */
        actionTargetTypes.put(HOSPITAL.getActionTargetType().getName(),
                              HOSPITAL.getActionTargetType());

        return new ArrayList<>(actionTargetTypes.values());
    }



    /**
     * Return the action targets defined for this service.
     *
     * @return list
     */
    public static List<ActionTargetType> getCertifyWeeklyMeasurementsActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        /*
         * Predefined by ClinicalTrialSetUp
         */
        actionTargetTypes.add(DATA_QUALITY_CERTIFICATION_TYPE.getActionTargetType());

        /*
         * Supplied by the caller
         */
        actionTargetTypes.add(HOSPITAL.getActionTargetType());

        return actionTargetTypes;
    }




    /**
     * Return an action target type for use in the governance action service's provider.
     *
     * @return action target type
     */
    public ActionTargetType getActionTargetType()
    {
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(name);
        actionTargetType.setDescription(description);
        actionTargetType.setOpenMetadataTypeName(typeName);
        actionTargetType.setDeployedImplementationType(deployedImplementationType);
        actionTargetType.setRequired(required);

        return actionTargetType;
    }


    /**
     * Return the name of the action target.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the action target.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the type name of the element that is linked to for this type of action target.
     *
     * @return type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the preferred value of the deployed implementation type of the element that is linked to for
     * this type of action target.  This is typically only set for assets.
     *
     * @return preferred value string
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ActionTarget{name='" + name + "}";
    }
}
