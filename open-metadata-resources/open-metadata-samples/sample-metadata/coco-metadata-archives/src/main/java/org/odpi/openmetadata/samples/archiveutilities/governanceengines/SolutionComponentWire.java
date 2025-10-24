/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.governanceengines;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.SolutionComponentWireDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Define the linkage between solution components defined for Coco Pharmaceuticals.
 * Still experimenting on the usage of the
 */
public enum SolutionComponentWire implements SolutionComponentWireDefinition
{
    HOSPITAL_TO_LANDING_AREA(SolutionComponent.HOSPITAL,
                             SolutionComponent.HOSPITAL_LANDING_AREA_FOLDER,
                             "publish",
                             "Certified patient readings are published to the appropriate landing area directory.",
                             null),
    LANDING_AREA_TO_ONBOARDING_PIPELINE(SolutionComponent.HOSPITAL_LANDING_AREA_FOLDER,
                                        SolutionComponent.LANDING_FOLDER_CATALOGUER,
                                        "detect new files",
                                        "When new files arrive, they are catalogued and the onboarding pipeline is called for each file.",
                                        null),
    CATALOGUER_TO_ONBOARDING_PIPELINE(SolutionComponent.LANDING_FOLDER_CATALOGUER,
                                      SolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE,
                                        "request onboarding",
                                        "Initiates the request to move the files into the data lake.",
                                        null),

    ONBOARDING_PIPELINE_TO_DATA_LAKE(SolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE,
                                     SolutionComponent.WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER,
                                     "save new files",
                                     "Incoming files are moved into the data lake since the landing area is less secure.",
                                     null),
    DATA_LAKE_TO_POPULATE_SANDBOX(SolutionComponent.WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER,
                                  SolutionComponent.POPULATE_SANDBOX,
                                  "read certified files",
                                  "Each of the files that has passed the quality checks is retrieved from the data lake directory.",
                                  null),
    POPULATE_SANDBOX_TO_SANDBOX(SolutionComponent.POPULATE_SANDBOX,
                                SolutionComponent.TREATMENT_VALIDATION_SANDBOX,
                                "write patient measurements",
                                "The patient measurements from the data lake files are added to the sandbox.",
                                null),
    SANDBOX_TO_ANALYSIS(SolutionComponent.TREATMENT_VALIDATION_SANDBOX,
                        SolutionComponent.ANALYSE_PATIENT_DATA,
                        "retrieve patient data",
                        "The data needed for the treatment analysis is retrieved from the various tables in the sandbox.",
                        null),
    ANALYSIS_TO_EVIDENCE(SolutionComponent.ANALYSE_PATIENT_DATA,
                         SolutionComponent.TREATMENT_EFFICACY_EVIDENCE,
                         "publish results",
                         "The results of the treatment analysis are written to the evidence repository.",
                         null),
    EVIDENCE_TO_ASSEMBLE_REPORT(SolutionComponent.TREATMENT_EFFICACY_EVIDENCE,
                                SolutionComponent.ASSEMBLE_REPORT,
                                "retrieve evidence",
                                "Appropriate evidence is retrieved from the evidence repository.",
                                null),
    REPORT_TO_VALIDATE_AND_DELIVERY(SolutionComponent.ASSEMBLE_REPORT,
                                    SolutionComponent.REPORT_VALIDATION_AND_DELIVERY,
                                    "publish results",
                                    "The results of the clinical trial is published to various stakeholders.",
                                    null),

    MOVE_FILES_TO_CHECK_QUALITY(SolutionComponent.MOVE_FILE_TO_DATA_LAKE,
                                SolutionComponent.CHECK_QUALITY_OF_DATA,
                                "provisioning complete",
                                "The data is in position and ready to be validated.",
                                null),

    MOVE_FILE_TO_SEEK_ORIGIN(SolutionComponent.MOVE_FILE_TO_DATA_LAKE,
                             SolutionComponent.DETERMINE_ORIGIN_OF_DATA,
                             "provisioning complete",
                             "The data is in position and ready for the origin to be validated.",
                             null),

    CHECK_QUALITY_TO_REPORT_ISSUES(SolutionComponent.CHECK_QUALITY_OF_DATA,
                                   SolutionComponent.REPORT_QUALITY_ISSUES,
                                   "quality analysis complete",
                                   "The quality checks have been completed and the results published.",
                                   null),

    SEEK_ORIGIN_TO_SET_RETENTION(SolutionComponent.DETERMINE_ORIGIN_OF_DATA,
                                 SolutionComponent.SET_RETENTION_PERIOD,
                                 "origin determined",
                                 "The origin of the data is understood and the retention period can now be set up.",
                                 null),

    SET_RETENTION_TO_PUBLISH_ASSET(SolutionComponent.SET_RETENTION_PERIOD,
                                   SolutionComponent.PUBLISH_ASSET,
                                   "classifications assigned",
                                   "Classification of the asset is complete.",
                                   null),

    REPORT_ISSUES_TO_PUBLISH_ASSET(SolutionComponent.REPORT_QUALITY_ISSUES,
                                   SolutionComponent.PUBLISH_ASSET,
                                   "quality assessment complete",
                                   "The quality assessment is complete and any necessary actions are in place.",
                                   null),

    GOODS_MOVEMENT(SolutionComponent.GOODS_INVENTORY,
                   SolutionComponent.SUSTAINABILITY_ODS,
                   "goods movement",
                   "Notifications about the movement of goods into, through and out of Coco Pharmaceuticals.",
                   new InformationSupplyChain[]{InformationSupplyChain.SUSTAINABILITY_REPORTING}),

    SPENDING_TYPES(SolutionComponent.ACCOUNTING_LEDGER,
                   SolutionComponent.SUSTAINABILITY_ODS,
                   "spending types",
                   "Summaries on the levels of spending and income by accounting codes.",
                   new InformationSupplyChain[]{InformationSupplyChain.SUSTAINABILITY_REPORTING}),

    PUBLISH_HAZMAT(SolutionComponent.HAZMAT_INVENTORY,
                   SolutionComponent.SUSTAINABILITY_ODS,
                   "publish hazmat levels",
                   "Summaries on the levels of hazardous materials acquired, stored and used in Coco Pharmaceuticals.",
                   new InformationSupplyChain[]{InformationSupplyChain.SUSTAINABILITY_REPORTING}),

    TRAVEL_INVENTORY(SolutionComponent.EMPLOYEE_EXPENSE_TOOL,
                   SolutionComponent.SUSTAINABILITY_ODS,
                   "travel inventory",
                   "Summaries of the flights and other high-carbon activities made by employees.",
                   new InformationSupplyChain[]{InformationSupplyChain.SUSTAINABILITY_REPORTING}),

    SUSTAINABILITY_RAW_DATA_REQUEST(SolutionComponent.SUSTAINABILITY_ODS,
                                    SolutionComponent.SUSTAINABILITY_CALCULATORS,
                                    "raw data request",
                                    "Query retrieving key facts needed for sustainability calculations.",
                                    new InformationSupplyChain[]{InformationSupplyChain.SUSTAINABILITY_REPORTING}),
    SUSTAINABILITY_RESULTS(SolutionComponent.SUSTAINABILITY_CALCULATORS,
                           SolutionComponent.SUSTAINABILITY_ODS,
                           "results publishing",
                           "Storing results of the calculations.",
                           new InformationSupplyChain[]{InformationSupplyChain.SUSTAINABILITY_REPORTING}),
    SUSTAINABILITY_REPORT_DATA_REQUEST(SolutionComponent.SUSTAINABILITY_ODS,
                                       SolutionComponent.SUSTAINABILITY_DASHBOARDS,
                                       "report request",
                                       "Summaries of the flights and other high-carbon activities made by employees.",
                                       new InformationSupplyChain[]{InformationSupplyChain.SUSTAINABILITY_REPORTING}),




    ;

    final SolutionComponent        component1;
    final SolutionComponent        component2;
    final InformationSupplyChain[] informationSupplyChains;
    final String                   label;
    final String                   description;

    SolutionComponentWire(SolutionComponent        component1,
                          SolutionComponent        component2,
                          String                   label,
                          String                   description,
                          InformationSupplyChain[] informationSupplyChains)
    {
        this.component1              = component1;
        this.component2              = component2;
        this.label                   = label;
        this.description             = description;
        this.informationSupplyChains = informationSupplyChains;
    }


    /**
     * Return the component for end 1
     *
     * @return component definition
     */
    @Override
    public SolutionComponentDefinition getComponent1()
    {
        return component1;
    }


    /**
     * Return the component for end 2
     *
     * @return component definition
     */
    @Override
    public SolutionComponentDefinition getComponent2()
    {
        return component2;
    }


    /**
     * Return the relationship label.
     *
     * @return string
     */
    @Override
    public String getLabel()
    {
        return label;
    }


    /**
     * Return the relationship description.
     *
     * @return string
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the list of ISC qualified names that the wire belongs to.
     *
     * @return list of strings
     */
    public List<String> getISCQualifiedNames()
    {
        if (informationSupplyChains == null)
        {
            return null;
        }

        List<String> guids = new ArrayList<>();

        for (InformationSupplyChain informationSupplyChain : informationSupplyChains)
        {
            guids.add(informationSupplyChain.getQualifiedName());
        }
        return guids;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SolutionComponentWire{" +
                "component1=" + component1 +
                ", component2=" + component2 +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", ISCQualifiedNames=" + getISCQualifiedNames() +
                "} " + super.toString();
    }
}
