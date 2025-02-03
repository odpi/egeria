/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata;

/**
 * A description of the predefined solution components.  The pre-defined GUIDs are used by the
 * Clinical trial governance actions that set up specific processes for the clinical trial.
 * The implementation of the solution components is managed by the coco archives.
 */
public enum ClinicalTrialSolutionComponent
{
    HOSPITAL_LANDING_AREA_FOLDER("1c150d6e-30cf-481c-9afb-3b06c9c9e78f"),

    LANDING_FOLDER_CATALOGUER("07705e15-efff-4f80-8992-f04ac85e0ef1"),

    WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE("7f5dca65-50b4-4103-9ac7-3a406a09047a"),

    WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER("a5d4d638-6836-47e5-99d0-fdcde637e13f"),

    NOMINATE_HOSPITAL("11c7c850-c67c-41cc-9423-d74db47cbf3a"),

    CERTIFY_HOSPITAL("37b8560d-84d4-434b-9b0d-105420fcc924"),

    ONBOARD_HOSPITAL("e9c2f911-ffcb-40c6-aeee-8c4d43811576"),

    SET_UP_DATA_LAKE_FOLDER("fb32bef2-e79f-4893-b500-2e547f24d482"),


    ;


    private final String guid;


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     */
    ClinicalTrialSolutionComponent(String guid)
    {
        this.guid = guid;
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
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ClinicalTrialSolutionComponent{" + guid + '}';
    }
}
