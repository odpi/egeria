/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Define the linkage between solution components.
 */
public enum SolutionComponentWire
{
    HOSPITAL_TO_LANDING_AREA(SolutionComponent.HOSPITAL,
                             SolutionComponent.HOSPITAL_LANDING_AREA_FOLDER,
                             new InformationSupplyChainSegment[]{InformationSupplyChainSegment.HOSPITAL_TO_LANDING_AREA}),
    LANDING_AREA_TO_ONBOARDING_PIPELINE(SolutionComponent.HOSPITAL_LANDING_AREA_FOLDER,
                                        SolutionComponent.LANDING_FOLDER_CATALOGUER,
                                        new InformationSupplyChainSegment[]{InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE}),
    CATALOGUER_TO_ONBOARDING_PIPELINE(SolutionComponent.LANDING_FOLDER_CATALOGUER,
                                      SolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE,
                                        new InformationSupplyChainSegment[]{InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE}),

    ONBOARDING_PIPELINE_TO_DATA_LAKE(SolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE,
                                     SolutionComponent.WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER,
                                     new InformationSupplyChainSegment[]{InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE}),
    DATA_LAKE_TO_POPULATE_SANDBOX(SolutionComponent.WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER,
                                  SolutionComponent.POPULATE_SANDBOX,
                                  new InformationSupplyChainSegment[]{InformationSupplyChainSegment.DATA_LAKE_TO_SANDBOX}),
    POPULATE_SANDBOX_TO_SANDBOX(SolutionComponent.POPULATE_SANDBOX,
                                SolutionComponent.TREATMENT_VALIDATION_SANDBOX,
                                new InformationSupplyChainSegment[]{InformationSupplyChainSegment.DATA_LAKE_TO_SANDBOX}),
    SANDBOX_TO_ANALYSIS(SolutionComponent.TREATMENT_VALIDATION_SANDBOX,
                        SolutionComponent.ANALYSE_PATIENT_DATA,
                        new InformationSupplyChainSegment[]{InformationSupplyChainSegment.ASSESS_TREATMENT}),
    ANALYSIS_TO_EVIDENCE(SolutionComponent.ANALYSE_PATIENT_DATA,
                         SolutionComponent.TREATMENT_EFFICACY_EVIDENCE,
                         new InformationSupplyChainSegment[]{InformationSupplyChainSegment.ASSESS_TREATMENT}),
    EVIDENCE_TO_ASSEMBLE_REPORT(SolutionComponent.TREATMENT_EFFICACY_EVIDENCE,
                                SolutionComponent.ASSEMBLE_REPORT,
                                new InformationSupplyChainSegment[]{InformationSupplyChainSegment.DELIVER_REPORT}),
    REPORT_TO_VALIDATE_AND_DELIVERY(SolutionComponent.ASSEMBLE_REPORT,
                                    SolutionComponent.REPORT_VALIDATION_AND_DELIVERY,
                                    new InformationSupplyChainSegment[]{InformationSupplyChainSegment.DELIVER_REPORT}),

    ;

    final SolutionComponent               component1;
    final SolutionComponent               component2;
    final InformationSupplyChainSegment[] informationSupplyChains;

    SolutionComponentWire(SolutionComponent               component1,
                          SolutionComponent               component2,
                          InformationSupplyChainSegment[] informationSupplyChains)
    {
        this.component1              = component1;
        this.component2              = component2;
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
