/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;

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
                             InformationSupplyChain.CLINICAL_TRIALS_TREATMENT_VALIDATION,
                             null),

    LANDING_AREA_TO_DATA_LAKE("38635d38-f728-400d-a8ec-7c26e68b7c0f",
                              "Egeria Provisioning Pipeline",
                              "Weekly Measurements Onboarding",
                              "Validating, cataloguing and moving incoming patient measurements from the landing area to the data lake.",
                              ScopeDefinition.WITHIN_PROJECT,
                              SolutionRoleDefinition.CLINICAL_TRIAL_DATA_ENGINEER.getGUID(),
                              InformationSupplyChain.CLINICAL_TRIALS_TREATMENT_VALIDATION,
                              new InformationSupplyChainSegment[]{HOSPITAL_TO_LANDING_AREA}),

    DATA_LAKE_TO_SANDBOX("7edca02c-e726-4570-815c-280bdf5498b9",
                         "Airflow DAG",
                         "Data Lake to Sandbox",
                         "Copying data from certified files into a PostgreSQL table for further processing.",
                         ScopeDefinition.WITHIN_PROJECT,
                         SolutionRoleDefinition.CLINICAL_TRIAL_DATA_SCIENTIST.getGUID(),
                         InformationSupplyChain.CLINICAL_TRIALS_TREATMENT_VALIDATION,
                         new InformationSupplyChainSegment[]{LANDING_AREA_TO_DATA_LAKE}),
    ASSESS_TREATMENT("e4303326-e418-4f77-b8e7-fd5d34717594",
                     "ML Flow Model Pipeline",
                     "Access Treatment under Trial",
                     "Perform agreed analysis of patient measurements to assess the efficacy of the treatment.",
                     ScopeDefinition.WITHIN_PROJECT,
                     SolutionRoleDefinition.CLINICAL_TRIAL_DATA_SCIENTIST.getGUID(),
                     InformationSupplyChain.CLINICAL_TRIALS_TREATMENT_VALIDATION,
                     new InformationSupplyChainSegment[]{DATA_LAKE_TO_SANDBOX}),
    DELIVER_REPORT("4fc47e60-f1b5-469c-b666-e1f4570e749f",
                   "Manual Procedures",
                   "Deliver Treatment Assessment Report",
                   "Validate that the report is valid and results are as expected, and deliver report to regulator.",
                   ScopeDefinition.WITHIN_PROJECT,
                   SolutionRoleDefinition.CLINICAL_TRIAL_SPONSOR.getGUID(),
                   InformationSupplyChain.CLINICAL_TRIALS_TREATMENT_VALIDATION,
                   new InformationSupplyChainSegment[]{ASSESS_TREATMENT}),

    ;


    private final String                          guid;
    private final String                          integrationStyle;
    private final String                          displayName;
    private final String                          description;
    private final ScopeDefinition                 scope;
    private final String                          ownerGUID;
    private final InformationSupplyChain          owningSupplyChain;
    private final InformationSupplyChainSegment[] linkedFromSegment;


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
     * @param linkedFromSegment array of segments that precess this segment
     */
    InformationSupplyChainSegment(String                          guid,
                                  String                          integrationStyle,
                                  String                          displayName,
                                  String                          description,
                                  ScopeDefinition                 scope,
                                  String                          ownerGUID,
                                  InformationSupplyChain          owningSupplyChain,
                                  InformationSupplyChainSegment[] linkedFromSegment)
    {
        this.guid              = guid;
        this.integrationStyle  = integrationStyle;
        this.displayName       = displayName;
        this.description       = description;
        this.scope             = scope;
        this.ownerGUID         = ownerGUID;
        this.owningSupplyChain = owningSupplyChain;
        this.linkedFromSegment = linkedFromSegment;
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
     * Return the segments that preceded this segment.
     *
     * @return list of segments
     */
    public List<InformationSupplyChainSegment> getLinkedFromSegment()
    {
        if (linkedFromSegment == null)
        {
            return null;
        }

        return Arrays.asList(linkedFromSegment);
    }


    /**
     * Return the unique name of the information supply chain.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "InformationSupplyChainSegment:" + displayName + ":" + scope;
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
