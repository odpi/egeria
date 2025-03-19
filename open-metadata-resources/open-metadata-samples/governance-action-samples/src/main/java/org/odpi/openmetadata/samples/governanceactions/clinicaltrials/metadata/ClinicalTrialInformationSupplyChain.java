/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata;


/**
 * A description of the predefined information supply chains.
 */
public enum ClinicalTrialInformationSupplyChain
{
    /**
     * Delivering data relating to a clinical trial from the hospitals to the Coco Researchers.
     */
    CLINICAL_TRIALS_TREATMENT_VALIDATION("Clinical Trial Treatment Validation"),


    /**
     * Delivering the data necessary to add a person as a subject in a clinical trial
     */
    CLINICAL_TRIAL_SUBJECT_ONBOARDING("Clinical Trial Subject Onboarding"),

    ;


    private final String displayName;


    /**
     * Construct an enum instance.
     *
     * @param displayName unique identifier
     */
    ClinicalTrialInformationSupplyChain(String displayName)
    {
        this.displayName = displayName;

    }


    /**
     * Return the display name of the solution blueprint.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the unique name of the solution blueprint.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "InformationSupplyChain:" + displayName;
    }

    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ClinicalTrialInformationSupplyChain{" + displayName + '}';
    }
}
