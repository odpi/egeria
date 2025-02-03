/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.businesssystems;

/**
 * Define the linkage between information supply chain segments.
 */
public enum InformationSupplyChainLink
{
    HOSPITAL_TO_LANDING_AREA(InformationSupplyChainSegment.HOSPITAL_TO_LANDING_AREA,
                             InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE,
                             "publish",
                             "Certified patient readings are published to the appropriate landing area directory."),
    LANDING_AREA_TO_DATA_LAKE(InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE,
                              InformationSupplyChainSegment.DATA_LAKE_TO_SANDBOX,
                              "capture and catalog",
                              "When new files arrive, they are catalogued validated and moved into the appropriate folder in the data lake."),
    DATA_LAKE_TO_ASSESSMENT(InformationSupplyChainSegment.DATA_LAKE_TO_SANDBOX,
                            InformationSupplyChainSegment.ASSESS_TREATMENT,
                            "assemble",
                            "The data from the clinical trial is made available for assessment"),

    ASSESSMENT_TO_PUBLISH(InformationSupplyChainSegment.ASSESS_TREATMENT,
                          InformationSupplyChainSegment.DELIVER_REPORT,
                          "publish results",
                          "The results of the clinical trial is published to various stakeholders."),

    SUSTAINABILITY_DATA_GATHERING_TO_ASSESSMENT(InformationSupplyChainSegment.SUSTAINABILITY_DATA_GATHERING,
                                                InformationSupplyChainSegment.SUSTAINABILITY_ASSESSMENT,
                                                "deliver data",
                                                "Data from across the organization is made available to the sustainability calculators."),

    SUSTAINABILITY_ASSESSMENT_TO_REPORT(InformationSupplyChainSegment.SUSTAINABILITY_ASSESSMENT,
                                        InformationSupplyChainSegment.DELIVER_SUSTAINABILITY_REPORT,
                                        "publish report",
                                        "The sustainability assessment is published to stakeholders."),

    ;

    final InformationSupplyChainSegment segment1;
    final InformationSupplyChainSegment segment2;
    final String                        label;
    final String                        description;

    InformationSupplyChainLink(InformationSupplyChainSegment segment1,
                               InformationSupplyChainSegment segment2,
                               String                        label,
                               String                        description)
    {
        this.segment1    = segment1;
        this.segment2    = segment2;
        this.label       = label;
        this.description = description;
    }


    public InformationSupplyChainSegment getSegment1()
    {
        return segment1;
    }

    public InformationSupplyChainSegment getSegment2()
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
