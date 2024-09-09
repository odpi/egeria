/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

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
    PROJECT("clinicalTrialProject",
            "Project used to control the clinical trial.",
            OpenMetadataType.PROJECT.typeName,
            null,
            true),


    /**
     * Hospital that will be supplying data for the clinical trial..
     */
    HOSPITAL("hospital",
            "Hospital that will be supplying data for the clinical trial.",
            OpenMetadataType.ORGANIZATION_TYPE_NAME,
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
    PROCESS_OWNER("processOwner",
                  "The person who is accountable for the correct management of the data.",
                  OpenMetadataType.PERSON.typeName,
                  null,
                  true),


    /**
     * The person who can be contacted if there are problems with the data from this hospital.
     */
    CUSTODIAN("custodian",
              "The person who is responsible for the correct management of data and can be contacted inside of Coco pharmaceuticals.",
              OpenMetadataType.PERSON.typeName,
              null,
              true),

    /**
     * The person, or team, in Coco Pharmaceuticals that sets up metadata for data.
     */
    STEWARD (ActionTarget.STEWARD.getName(),
             "The person, or team in Coco Pharmaceuticals that sets up metadata for data.",
             OpenMetadataType.ACTOR.typeName,
             null,
             true),

    /**
     * Catalog where data for clinical trial is to reside.
     */
    CATALOG("dataLakeCatalog",
            "Schema where the weekly measurements volume is to reside.",
            DeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName(),
            DeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType(),
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
                               OpenMetadataType.LICENSE_TYPE_TYPE_NAME,
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
     * The certification type that should be used to certify the hospital.
     */
    HOSPITAL_CERTIFICATION_TYPE("hospitalCertificationType",
                                "The certification type that should be used to certify the hospital.",
                                OpenMetadataType.CERTIFICATION_TYPE_TYPE_NAME,
                                null,
                                true),

    /**
     * The certification type that should be used to certify the hospital.
     */
    DATA_QUALITY_CERTIFICATION_TYPE("dataQualityCertificationType",
                                "The certification type that should be used to certify that data from the hospital conforms to specification.",
                                OpenMetadataType.CERTIFICATION_TYPE_TYPE_NAME,
                                null,
                                true),

    GENERIC_ONBOARDING_PIPELINE("onboardingPipeline",
                                "The standard onboarding pipeline that brings data from the landing area to the data lake that is not customized for a project or hospital.",
                                OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                null,
                                true),

    GENERIC_HOSPITAL_NOMINATION_GAT("hospitalNominationGovernanceActionType",
                                    "The Governance Action Type that tentatively adds a hospital to a clinical trial.",
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                    null,
                                    true),

    GENERIC_HOSPITAL_CERTIFICATION_GAT("hospitalCertificationGovernanceActionType",
                                       "The Governance Action Type that implements the hospital certification process.",
                                       OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                       null,
                                       true),

    GENERIC_HOSPITAL_ONBOARDING_GAT("hospitalOnboardingGovernanceActionType",
                                       "The Governance Action Type that implements the hospital certification process.",
                                       OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                       null,
                                       true),

    GENERIC_SET_UP_DATA_LAKE_GAT("setUpDataLakeGovernanceActionType",
                                    "The Governance Action Type that sets up the volume in the data lake catalog.",
                                    OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                    null,
                                    true),

    ONBOARD_HOSPITAL_PROCESS("onboardHospitalProcess",
                                 "The Governance Action Process that sets up the onboarding pipeline.  SetUpDataLake needs to pass the destination directory onto this process.",
                                 OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
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

        actionTargetTypes.add(PROJECT.getActionTargetType());
        actionTargetTypes.add(HOSPITAL_CERTIFICATION_TYPE.getActionTargetType());
        actionTargetTypes.add(PROCESS_OWNER.getActionTargetType());
        actionTargetTypes.add(CUSTODIAN.getActionTargetType());
        actionTargetTypes.add(STEWARD.getActionTargetType());

        actionTargetTypes.add(LANDING_AREA_CONNECTOR.getActionTargetType());
        actionTargetTypes.add(LAST_UPDATE_CONNECTOR.getActionTargetType());

        actionTargetTypes.add(GENERIC_ONBOARDING_PIPELINE.getActionTargetType());
        actionTargetTypes.add(GENERIC_HOSPITAL_ONBOARDING_GAT.getActionTargetType());
        actionTargetTypes.add(GENERIC_HOSPITAL_NOMINATION_GAT.getActionTargetType());
        actionTargetTypes.add(GENERIC_HOSPITAL_CERTIFICATION_GAT.getActionTargetType());
        actionTargetTypes.add(GENERIC_SET_UP_DATA_LAKE_GAT.getActionTargetType());

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
        actionTargetTypes.add(CUSTODIAN.getActionTargetType());
        actionTargetTypes.add(PROCESS_OWNER.getActionTargetType());
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
        actionTargetTypes.add(CUSTODIAN.getActionTargetType());

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
        actionTargetTypes.add(STEWARD.getActionTargetType());
        actionTargetTypes.add(LAST_UPDATE_CONNECTOR.getActionTargetType());
        actionTargetTypes.add(ONBOARD_HOSPITAL_PROCESS.getActionTargetType()); // pass on destination folder

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
        List<ActionTargetType> actionTargetTypes = getCertifyWeeklyMeasurementsActionTargetTypes();

        /*
         * Predefined by ClinicalTrialSetUp
         */
        actionTargetTypes.add(PROJECT.getActionTargetType());
        actionTargetTypes.add(HOSPITAL_CERTIFICATION_TYPE.getActionTargetType());
        actionTargetTypes.add(LANDING_AREA_CONNECTOR.getActionTargetType());
        actionTargetTypes.add(GENERIC_ONBOARDING_PIPELINE.getActionTargetType());
        actionTargetTypes.add(STEWARD.getActionTargetType());

        /*
         * Supplied by the caller
         */
        actionTargetTypes.add(HOSPITAL.getActionTargetType());

        return actionTargetTypes;
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
        actionTargetType.setTypeName(typeName);
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
