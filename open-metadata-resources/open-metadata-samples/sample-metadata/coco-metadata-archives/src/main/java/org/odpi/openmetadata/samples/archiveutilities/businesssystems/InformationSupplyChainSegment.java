/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.businesssystems;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * A description of the segments in an information supply chain.
 */
public enum InformationSupplyChainSegment
{
    HOSPITAL_TO_LANDING_AREA("04ae768e-3816-47bc-bddb-c9ae25018684",
                             "Unknown",
                             "Hospital Delivers Patient Weekly Readings",
                             "Each week, the certified hospitals deliver agreed measurements from each of their patients in the clinical trial.",
                             ScopeDefinition.WITHIN_PROJECT,
                             SolutionRoleDefinition.PARTICIPATING_HOSPITAL_COORDINATOR.getGUID(),
                             InformationSupplyChain.CLINICAL_TRIALS_TREATMENT_VALIDATION_TEMPLATE),

    LANDING_AREA_TO_DATA_LAKE("38635d38-f728-400d-a8ec-7c26e68b7c0f",
                              "Egeria Provisioning Pipeline",
                              "Weekly Measurements Onboarding",
                              "Validating, cataloguing and moving incoming patient measurements from the landing area to the data lake.",
                              ScopeDefinition.WITHIN_PROJECT,
                              SolutionRoleDefinition.CLINICAL_TRIAL_DATA_ENGINEER.getGUID(),
                              InformationSupplyChain.CLINICAL_TRIALS_TREATMENT_VALIDATION_TEMPLATE),

    DATA_LAKE_TO_SANDBOX("7edca02c-e726-4570-815c-280bdf5498b9",
                         "Airflow DAG",
                         "Data Lake to Sandbox",
                         "Copying data from certified files into a PostgreSQL table for further processing.",
                         ScopeDefinition.WITHIN_PROJECT,
                         SolutionRoleDefinition.CLINICAL_TRIAL_DATA_SCIENTIST.getGUID(),
                         InformationSupplyChain.CLINICAL_TRIALS_TREATMENT_VALIDATION_TEMPLATE),
    ASSESS_TREATMENT("e4303326-e418-4f77-b8e7-fd5d34717594",
                     "ML Flow Model Pipeline",
                     "Assess Treatment under Trial",
                     "Perform agreed analysis of patient measurements to assess the efficacy of the treatment.",
                     ScopeDefinition.WITHIN_PROJECT,
                     SolutionRoleDefinition.CLINICAL_TRIAL_DATA_SCIENTIST.getGUID(),
                     InformationSupplyChain.CLINICAL_TRIALS_TREATMENT_VALIDATION_TEMPLATE),
    DELIVER_REPORT("4fc47e60-f1b5-469c-b666-e1f4570e749f",
                   "Manual Procedures",
                   "Deliver Treatment Assessment Report",
                   "Validate that the report is valid and results are as expected, and deliver report to regulator.",
                   ScopeDefinition.WITHIN_PROJECT,
                   SolutionRoleDefinition.CLINICAL_TRIAL_SPONSOR.getGUID(),
                   InformationSupplyChain.CLINICAL_TRIALS_TREATMENT_VALIDATION_TEMPLATE),


    SUSTAINABILITY_DATA_GATHERING("9fb47e51-4121-495d-be31-f766a1f70856",
                                  "ETL Jobs",
                                  "Sustainability Data Gathering",
                                  "Gather information that describes activity in Coco Pharmaceuticals that affect its sustainability.",
                                  ScopeDefinition.ALL_COCO,
                                  SolutionRoleDefinition.SUSTAINABILITY_LEADER.getGUID(),
                                  InformationSupplyChain.SUSTAINABILITY_REPORTING),

    SUSTAINABILITY_ASSESSMENT("a491accc-aaaa-455b-893e-b6f7b0bf0afb",
                              "ETL Jobs",
                              "Sustainability Assessment",
                              "Perform sustainability calculations and store the results.",
                              ScopeDefinition.ALL_COCO,
                              SolutionRoleDefinition.SUSTAINABILITY_LEADER.getGUID(),
                              InformationSupplyChain.SUSTAINABILITY_REPORTING),

    DELIVER_SUSTAINABILITY_REPORT("af07dc2b-ae07-442b-9b06-43d4ef52138a",
                                  "ETL Jobs",
                                  "Sustainability Report Delivery",
                                  "Deliver sustainability report to stakeholders.",
                                  ScopeDefinition.ALL_COCO,
                                  SolutionRoleDefinition.SUSTAINABILITY_LEADER.getGUID(),
                                  InformationSupplyChain.SUSTAINABILITY_REPORTING),

    ;


    private final String                          guid;
    private final String                          integrationStyle;
    private final String                          displayName;
    private final String                          description;
    private final ScopeDefinition                 scope;
    private final String                          ownerGUID;
    private final InformationSupplyChain          owningSupplyChain;


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param integrationStyle   type of information supply chain segment - for example, is it a process, of file or database.
     * @param displayName display name of information supply chain segment
     * @param description description of information supply chain segment
     * @param scope version identifier of the information supply chain segment
     * @param ownerGUID identifier of owner
     * @param owningSupplyChain the blueprint that this belongs to
     */
    InformationSupplyChainSegment(String                          guid,
                                  String                          integrationStyle,
                                  String                          displayName,
                                  String                          description,
                                  ScopeDefinition                 scope,
                                  String                          ownerGUID,
                                  InformationSupplyChain          owningSupplyChain)
    {
        this.guid              = guid;
        this.integrationStyle  = integrationStyle;
        this.displayName       = displayName;
        this.description       = description;
        this.scope             = scope;
        this.ownerGUID         = ownerGUID;
        this.owningSupplyChain = owningSupplyChain;
    }


    /**
     * Return the GUID for the element.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return the integration style of information supply chain segment.
     *
     * @return string
     */
    public String getIntegrationStyle()
    {
        return integrationStyle;
    }


    /**
     * Return the display name of the information supply chain segment.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the information supply chain segment.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the version identifier of the information supply chain segment.
     *
     * @return enum
     */
    public ScopeDefinition getScope()
    {
        return scope;
    }


    /**
     * Return the identifier of the owner.
     *
     * @return string
     */
    public String getOwner()
    {
        return ownerGUID;
    }


    /**
     * Return the type name of the element describing the owner.
     *
     * @return string
     */
    public String getOwnerTypeName()
    {
        return OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName;
    }


    /**
     * Return the property name used to identify the owner.
     *
     * @return string
     */
    public String getOwnerPropertyName()
    {
        return OpenMetadataProperty.GUID.name;
    }


    /**
     * Return the supply chain that this segment is a part of.
     *
     * @return information supply chain
     */
    public InformationSupplyChain getOwningSupplyChain()
    {
        return owningSupplyChain;
    }


    /**
     * Return the unique name of the information supply chain.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "InformationSupplyChainSegment::" + displayName + "::" + scope;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "InformationSupplyChainSegment{" + displayName + '}';
    }
}
