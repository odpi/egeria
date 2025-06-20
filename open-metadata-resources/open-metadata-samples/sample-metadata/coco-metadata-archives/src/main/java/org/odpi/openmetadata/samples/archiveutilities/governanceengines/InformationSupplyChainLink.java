/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.governanceengines;

/**
 * Define the linkage between information supply chain segments.
 */
public enum InformationSupplyChainLink
{
    SUBJECT_ONBOARDING_TO_TREATMENT_VALIDATION(InformationSupplyChain.CLINICAL_TRIAL_SUBJECT_ONBOARDING_TEMPLATE,
                             InformationSupplyChain.CLINICAL_TRIAL_TREATMENT_VALIDATION_TEMPLATE,
                             "validate treatment",
                             "Weekly measurements are taken from the clinical trial subjects to validate that the treatment is having the desired effect without unwanted side-effects."),

    ;

    final InformationSupplyChain segment1;
    final InformationSupplyChain segment2;
    final String                 label;
    final String                 description;

    InformationSupplyChainLink(InformationSupplyChain segment1,
                               InformationSupplyChain segment2,
                               String                 label,
                               String                 description)
    {
        this.segment1    = segment1;
        this.segment2    = segment2;
        this.label       = label;
        this.description = description;
    }


    public InformationSupplyChain getSegment1()
    {
        return segment1;
    }

    public InformationSupplyChain getSegment2()
    {
        return segment2;
    }

    public String getLabel()
    {
        return label;
    }


    public String getDescription()
    {
        return description;
    }
}
