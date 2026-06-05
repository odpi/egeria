/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.governanceengines;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.InformationSupplyChainDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.InformationSupplyChainLinkDefinition;

/**
 * Define the linkage between information supply chain segments.
 */
public enum InformationSupplyChainLink implements InformationSupplyChainLinkDefinition
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


    /**
     * Constructor to define a linkage between two information supply chain segments,
     * with additional descriptive details.
     *
     * @param segment1 the first information supply chain segment
     * @param segment2 the second information supply chain segment
     * @param label a brief label describing the linkage
     * @param description a detailed description of the linkage and its purpose
     */
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


    /**
     * Retrieves the first segment in the information supply chain linkage.
     *
     * @return the first information supply chain segment in the linkage
     */
    @Override
    public InformationSupplyChainDefinition getSegment1()
    {
        return segment1;
    }


    /**
     * Retrieves the second segment in the information supply chain linkage.
     *
     * @return the definition of the second information supply chain segment
     */
    @Override
    public InformationSupplyChainDefinition getSegment2()
    {
        return segment2;
    }


    /**
     * Retrieves the label associated with the information supply chain link.
     *
     * @return the label describing the linkage between the supply chain segments
     */
    @Override
    public String getLabel()
    {
        return label;
    }


    /**
     * Retrieves the description of the information supply chain link.
     *
     * @return the description providing details about the linkage between the supply chain segments
     */
    @Override
    public String getDescription()
    {
        return description;
    }
}
