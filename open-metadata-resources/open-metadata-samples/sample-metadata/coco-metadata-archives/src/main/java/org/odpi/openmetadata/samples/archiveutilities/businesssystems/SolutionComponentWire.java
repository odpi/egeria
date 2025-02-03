/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.businesssystems;

import java.util.ArrayList;
import java.util.List;

/**
 * Define the linkage between solution components in te clinical .
 */
public enum SolutionComponentWire
{
    HOSPITAL_TO_LANDING_AREA(SolutionComponent.HOSPITAL,
                             SolutionComponent.HOSPITAL_LANDING_AREA_FOLDER,
                             "publish",
                             "Certified patient readings are published to the appropriate landing area directory.",
                             new InformationSupplyChainSegment[]{InformationSupplyChainSegment.HOSPITAL_TO_LANDING_AREA}),
    LANDING_AREA_TO_ONBOARDING_PIPELINE(SolutionComponent.HOSPITAL_LANDING_AREA_FOLDER,
                                        SolutionComponent.LANDING_FOLDER_CATALOGUER,
                                        "detect new files",
                                        "When new files arrive, they are catalogued and the onboarding pipeline is called for each file.",
                                        new InformationSupplyChainSegment[]{InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE}),
    CATALOGUER_TO_ONBOARDING_PIPELINE(SolutionComponent.LANDING_FOLDER_CATALOGUER,
                                      SolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE,
                                        "request onboarding",
                                        "Initiates the request to move the files into the data lake.",
                                        new InformationSupplyChainSegment[]{InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE}),

    ONBOARDING_PIPELINE_TO_DATA_LAKE(SolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE,
                                     SolutionComponent.WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER,
                                     "save new files",
                                     "Incoming files are moved into the data lake since the landing area is less secure.",
                                     new InformationSupplyChainSegment[]{InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE}),
    DATA_LAKE_TO_POPULATE_SANDBOX(SolutionComponent.WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER,
                                  SolutionComponent.POPULATE_SANDBOX,
                                  "read certified files",
                                  "Each of the files that has passed the quality checks is retrieved from the data lake directory.",
                                  new InformationSupplyChainSegment[]{InformationSupplyChainSegment.DATA_LAKE_TO_SANDBOX}),
    POPULATE_SANDBOX_TO_SANDBOX(SolutionComponent.POPULATE_SANDBOX,
                                SolutionComponent.TREATMENT_VALIDATION_SANDBOX,
                                "write patient measurements",
                                "The patient measurements from the data lake files are added to the sandbox.",
                                new InformationSupplyChainSegment[]{InformationSupplyChainSegment.DATA_LAKE_TO_SANDBOX}),
    SANDBOX_TO_ANALYSIS(SolutionComponent.TREATMENT_VALIDATION_SANDBOX,
                        SolutionComponent.ANALYSE_PATIENT_DATA,
                        "retrieve patient data",
                        "The data needed for the treatment analysis is retrieved from the various tables in the sandbox.",
                        new InformationSupplyChainSegment[]{InformationSupplyChainSegment.ASSESS_TREATMENT}),
    ANALYSIS_TO_EVIDENCE(SolutionComponent.ANALYSE_PATIENT_DATA,
                         SolutionComponent.TREATMENT_EFFICACY_EVIDENCE,
                         "publish results",
                         "The results of the treatment analysis are written to the evidence repository.",
                         new InformationSupplyChainSegment[]{InformationSupplyChainSegment.ASSESS_TREATMENT}),
    EVIDENCE_TO_ASSEMBLE_REPORT(SolutionComponent.TREATMENT_EFFICACY_EVIDENCE,
                                SolutionComponent.ASSEMBLE_REPORT,
                                "retrieve evidence",
                                "Appropriate evidence is retrieved from the evidence repository.",
                                new InformationSupplyChainSegment[]{InformationSupplyChainSegment.DELIVER_REPORT}),
    REPORT_TO_VALIDATE_AND_DELIVERY(SolutionComponent.ASSEMBLE_REPORT,
                                    SolutionComponent.REPORT_VALIDATION_AND_DELIVERY,
                                    "publish results",
                                    "The results of the clinical trial is published to various stakeholders.",
                                    new InformationSupplyChainSegment[]{InformationSupplyChainSegment.DELIVER_REPORT}),

    GOODS_MOVEMENT(SolutionComponent.GOODS_INVENTORY,
                   SolutionComponent.SUSTAINABILITY_ODS,
                   "goods movement",
                   "Notifications about the movement of goods into, through and out of Coco Pharmaceuticals.",
                   new InformationSupplyChainSegment[]{InformationSupplyChainSegment.SUSTAINABILITY_DATA_GATHERING}),

    SPENDING_TYPES(SolutionComponent.ACCOUNTING_LEDGER,
                   SolutionComponent.SUSTAINABILITY_ODS,
                   "spending types",
                   "Summaries on the levels of spending and income by accounting codes.",
                   new InformationSupplyChainSegment[]{InformationSupplyChainSegment.SUSTAINABILITY_DATA_GATHERING}),

    PUBLISH_HAZMAT(SolutionComponent.HAZMAT_INVENTORY,
                   SolutionComponent.SUSTAINABILITY_ODS,
                   "publish hazmat levels",
                   "Summaries on the levels of hazardous materials acquired, stored and used in Coco Pharmaceuticals.",
                   new InformationSupplyChainSegment[]{InformationSupplyChainSegment.SUSTAINABILITY_DATA_GATHERING}),

    TRAVEL_INVENTORY(SolutionComponent.EMPLOYEE_EXPENSE_TOOL,
                   SolutionComponent.SUSTAINABILITY_ODS,
                   "travel inventory",
                   "Summaries of the flights and other high-carbon activities made by employees.",
                   new InformationSupplyChainSegment[]{InformationSupplyChainSegment.SUSTAINABILITY_DATA_GATHERING}),

    SUSTAINABILITY_RAW_DATA_REQUEST(SolutionComponent.SUSTAINABILITY_ODS,
                                    SolutionComponent.SUSTAINABILITY_CALCULATORS,
                                    "raw data request",
                                    "Query retrieving key facts needed for sustainability calculations.",
                                    new InformationSupplyChainSegment[]{InformationSupplyChainSegment.SUSTAINABILITY_ASSESSMENT}),
    SUSTAINABILITY_RESULTS(SolutionComponent.SUSTAINABILITY_CALCULATORS,
                           SolutionComponent.SUSTAINABILITY_ODS,
                           "results publishing",
                           "Storing results of the calculations.",
                           new InformationSupplyChainSegment[]{InformationSupplyChainSegment.SUSTAINABILITY_ASSESSMENT}),
    SUSTAINABILITY_REPORT_DATA_REQUEST(SolutionComponent.SUSTAINABILITY_ODS,
                                       SolutionComponent.SUSTAINABILITY_DASHBOARDS,
                                       "travel inventory",
                                       "Summaries of the flights and other high-carbon activities made by employees.",
                                       new InformationSupplyChainSegment[]{InformationSupplyChainSegment.DELIVER_SUSTAINABILITY_REPORT}),




    ;

    final SolutionComponent               component1;
    final SolutionComponent               component2;
    final InformationSupplyChainSegment[] informationSupplyChains;
    final String                          label;
    final String                          description;

    SolutionComponentWire(SolutionComponent               component1,
                          SolutionComponent               component2,
                          String                          label,
                          String                          description,
                          InformationSupplyChainSegment[] informationSupplyChains)
    {
        this.component1              = component1;
        this.component2              = component2;
        this.label                   = label;
        this.description             = description;
        this.informationSupplyChains = informationSupplyChains;
    }


    public SolutionComponent getComponent1()
    {
        return component1;
    }

    public SolutionComponent getComponent2()
    {
        return component2;
    }

    public String getLabel()
    {
        return label;
    }

    public String getDescription()
    {
        return description;
    }

    public List<String> getInformationSupplySegmentGUIDs()
    {
        if (informationSupplyChains == null)
        {
            return null;
        }

        List<String> guids = new ArrayList<>();

        for (InformationSupplyChainSegment informationSupplyChainSegment : informationSupplyChains)
        {
            guids.add(informationSupplyChainSegment.getGUID());
        }
        return guids;
    }
}
