/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.businesssystems;

import org.odpi.openmetadata.archiveutilities.openconnectors.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.samples.archiveutilities.governanceengines.CocoRequestTypeDefinition;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata.ClinicalTrialSolutionComponent;

import java.util.Arrays;
import java.util.List;

/**
 * A description of the predefined solution components.
 */
public enum SolutionComponent
{
    HOSPITAL("ee2bb773-e630-4cf9-bdf1-7c2dd64fe4ec",
             SolutionComponentType.THIRD_PARTY_PROCESS.getSolutionComponentType(),
             "External Organization Processes",
             "Hospital Processes",
             "Processes running in the hospital that negotiate data sharing agreements, recruit patients, train staff and deliver data.",
             "V1.0",
             new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
             null,
             new InformationSupplyChainSegment[]{
                     InformationSupplyChainSegment.HOSPITAL_TO_LANDING_AREA},
             null),

    HOSPITAL_LANDING_AREA_FOLDER(ClinicalTrialSolutionComponent.HOSPITAL_LANDING_AREA_FOLDER.getGUID(),
                                 SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                 "File Directory",
                                 "Hospital Landing Area Folder",
                                 "Destination for incoming files from a particular hospital.",
                                 "V1.0",
                                 new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                                 null,
                                 new InformationSupplyChainSegment[]{
                                         InformationSupplyChainSegment.HOSPITAL_TO_LANDING_AREA,
                                         InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE},
                                 null),

    LANDING_FOLDER_CATALOGUER(ClinicalTrialSolutionComponent.LANDING_FOLDER_CATALOGUER.getGUID(),
                              SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                              "Integration Connector",
                              "Landing Folder Cataloguer",
                              "Integration connector that is cataloguing files arriving in the hospital landing area folders and invoking the onboarding pipeline.",
                              "V1.0",
                              new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                              null,
                              new InformationSupplyChainSegment[]{
                                      InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE},
                              null),

    MOVE_FILE_TO_DATA_LAKE("18931474-d170-4394-97a9-0e627e2212ac",
                           SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                           "Governance Action Process Step",
                           "Move File To Data Lake",
                           "Move landing area files to data lake, catalog files in data lake with lineage from the landing area and validate/certify that the data contains valid values.  The cataloguing includes lineage, retention, origin, governance zones.  The quality validation survey will add a certification to the file asset if the data contains valid values.",
                           "V1.0",
                           null,
                           null,
                           null,
                           RequestTypeDefinition.MOVE_FILE.getGovernanceActionTypeGUID()),

    CHECK_QUALITY_OF_DATA("2a5763d0-c540-4a59-8268-db7c88342269",
                          SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                          "Governance Action Process Step",
                          "Check Quality of Data",
                          "Validate that the data contains valid values. The quality validation survey will add a certification to the file asset if the data contains valid values.",
                          "V1.0",
                          null,
                          null,
                          null,
                          CocoRequestTypeDefinition.CHECK_DATA.getGovernanceActionTypeGUID()),

    REPORT_QUALITY_ISSUES("b1fd8336-45ca-4e2e-bd79-ce6601c0b68f",
                          SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                          "Governance Action Process Step",
                          "Report Quality Issues",
                          "Create alerts to interested parties if quality issues are detected.",
                          "V1.0",
                          null,
                          null,
                          null,
                          RequestTypeDefinition.EVALUATE_ANNOTATIONS.getGovernanceActionTypeGUID()),

    DETERMINE_ORIGIN_OF_DATA("7bcf573b-0ad8-4ca8-82c0-8d8f50fac4cd",
                             SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                             "Governance Action Process Step",
                             "Determine Origin of Data",
                             "Add details of the originating hospital.",
                             "V1.0",
                             null,
                             null,
                             null,
                             RequestTypeDefinition.SEEK_ORIGIN.getGovernanceActionTypeGUID()),

    SET_RETENTION_PERIOD("bed0ec86-45ce-4350-8c68-9596299c843a",
                         SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                         "Governance Action Process Step",
                         "Set Retention Period",
                         "Define when this file can be archived and then deleted.",
                         "V1.0",
                         null,
                         null,
                         null,
                         RequestTypeDefinition.RETENTION_PERIOD.getGovernanceActionTypeGUID()),

    PUBLISH_ASSET("22e35eff-fcc8-4baa-804e-8363989cf6f1",
                  SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                  "Governance Action Process Step",
                  "Publish Asset",
                  "Make the new file visible in the data lake catalog.",
                  "V1.0",
                  null,
                  null,
                  null,
                  RequestTypeDefinition.ZONE_MEMBER.getGovernanceActionTypeGUID()),

    WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE(ClinicalTrialSolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE.getGUID(),
                                            SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                                            "Governance Action Process",
                                            "Weekly Measurements Onboarding Pipeline",
                                            "Move landing area files to data lake, catalog files in data lake with lineage from the landing area and validate/certify that the data contains valid values.  The cataloguing includes lineage, retention, origin, governance zones.  The quality validation survey will add a certification to the file asset if the data contains valid values.",
                                            "V1.0",
                                            new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                                            new SolutionComponent[]{
                                                    MOVE_FILE_TO_DATA_LAKE,
                                                    CHECK_QUALITY_OF_DATA,
                                                    REPORT_QUALITY_ISSUES,
                                                    DETERMINE_ORIGIN_OF_DATA,
                                                    SET_RETENTION_PERIOD,
                                                    PUBLISH_ASSET},
                                            new InformationSupplyChainSegment[]{
                                                    InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE},
                                            null),

    WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER(ClinicalTrialSolutionComponent.WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER.getGUID(),
                                         SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                         "Unity Catalog Volume (File Directory)",
                                         "Weekly Measurements Data Lake Folder",
                                         "The directory where the files from the hospitals are assembled for sharing.",
                                         "V1.0",
                                         new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                                         null,
                                         new InformationSupplyChainSegment[]{
                                                 InformationSupplyChainSegment.DATA_LAKE_TO_SANDBOX,
                                                 InformationSupplyChainSegment.LANDING_AREA_TO_DATA_LAKE},
                                         null),

    POPULATE_SANDBOX(ClinicalTrialSolutionComponent.POPULATE_SANDBOX.getGUID(),
                     SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                     "Airflow DAG",
                     "Populate Sandbox",
                     "A process that copies certified files from the hospitals into the research team's sandbox for processing.",
                     "V1.0",
                     new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                     null,
                     new InformationSupplyChainSegment[]{
                             InformationSupplyChainSegment.DATA_LAKE_TO_SANDBOX},
                     null),

    TREATMENT_VALIDATION_SANDBOX(ClinicalTrialSolutionComponent.TREATMENT_VALIDATION_SANDBOX.getGUID(),
                                 SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                 "PostgreSQL Database",
                                 "Treatment Validation Sandbox",
                                 "A postgreSQL database schema for assembling data needed to validate the clinical trial.",
                                 "V1.0",
                                 new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                                 null,
                                 new InformationSupplyChainSegment[]{
                                         InformationSupplyChainSegment.DATA_LAKE_TO_SANDBOX,
                                         InformationSupplyChainSegment.ASSESS_TREATMENT},
                                 null),

    ANALYSE_PATIENT_DATA("b5c8da4c-f925-4cf1-8294-e43cd2c1a584",
                         SolutionComponentType.INSIGHT_MODEL.getSolutionComponentType(),
                         "AI Workflow",
                         "Analyse Patient Data",
                         "ML Flow based analytics pipeline to run the variety of models over the patient data.",
                         "V1.0",
                         new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                         null,
                         new InformationSupplyChainSegment[]{
                                 InformationSupplyChainSegment.ASSESS_TREATMENT},
                         null),

    TREATMENT_EFFICACY_EVIDENCE("48bc201e-3d4e-4beb-bdb2-0fd9d134f6d5",
                                SolutionComponentType.DATA_STORAGE.getSolutionComponentType(),
                                "Content Manager",
                                "Treatment Efficacy Evidence",
                                "Assembly of the data required by the regulator.",
                                "V1.0",
                                new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                                null,
                                new InformationSupplyChainSegment[]{
                                        InformationSupplyChainSegment.DELIVER_REPORT,
                                        InformationSupplyChainSegment.ASSESS_TREATMENT},
                                null),

    ASSEMBLE_REPORT("72a86eec-9734-4bc0-babb-4fec0aa7c9ff",
                    SolutionComponentType.MANUAL_PROCESS.getSolutionComponentType(),
                    "Manual Process",
                    "Assemble Treatment Assessment Report",
                    "A process of creating a filing report for the regulators detailing the findings from the clinical trial.",
                    "V1.0",
                    new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                    null,
                    new InformationSupplyChainSegment[]{
                            InformationSupplyChainSegment.DELIVER_REPORT},
                    null),

    REPORT_VALIDATION_AND_DELIVERY("0bf2547c-937c-41b6-814f-6284849271a1",
                                   SolutionComponentType.DOCUMENT_PUBLISHING.getSolutionComponentType(),
                                   "Manual Process",
                                   "Treatment Assessment Report Validation and Delivery",
                                   "An expert review, presentation, discussions relating to the results of the clinical trial.",
                                   "V1.0",
                                   new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                                   null,
                                   new InformationSupplyChainSegment[]{InformationSupplyChainSegment.DELIVER_REPORT},
                                   null),

    NOMINATE_HOSPITAL(ClinicalTrialSolutionComponent.NOMINATE_HOSPITAL.getGUID(),
                      SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                      "Governance Action Process",
                      "Nominate Hospital",
                      "Add details of a hospital as a candidate for participation in the clinical trials.",
                      "V1.0",
                      new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                      null,
                      null,
                      CocoRequestTypeDefinition.NOMINATE_HOSPITAL.getGovernanceActionTypeGUID()),

    CERTIFY_HOSPITAL(ClinicalTrialSolutionComponent.CERTIFY_HOSPITAL.getGUID(),
                     SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                     "Governance Action Process",
                     "Certify Hospital",
                     "Confirm that a hospital has met all of the criteria to participate in the clinical trial.",
                     "V1.0",
                     new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                     null,
                     null,
                     CocoRequestTypeDefinition.CERTIFY_HOSPITAL.getGovernanceActionTypeGUID()),

    ONBOARD_HOSPITAL(ClinicalTrialSolutionComponent.ONBOARD_HOSPITAL.getGUID(),
                     SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                     "Governance Action Process",
                     "Onboard Hospital",
                     "Set up the onboarding pipeline for a participating hospital.  This fails if the hospital is not certified.",
                     "V1.0",
                     new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                     null,
                     null,
                     CocoRequestTypeDefinition.ONBOARD_HOSPITAL.getGovernanceActionTypeGUID()),

    SET_UP_DATA_LAKE(ClinicalTrialSolutionComponent.SET_UP_DATA_LAKE.getGUID(),
                     SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                     "Governance Action Process",
                     "Set up Data Lake",
                     "Set up the data stores for receiving data from the hospitals - this includes the file system directory and Unity Catalog Volume for incoming patient measurements, along with the data set collection for certified measurement files.",
                     "V1.0",
                     new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                     new SolutionComponent[]{
                                    WEEKLY_MEASUREMENTS_DATA_LAKE_FOLDER
                            },
                     null,
                     CocoRequestTypeDefinition.SET_UP_DATA_LAKE.getGovernanceActionTypeGUID()),

    SET_UP_CLINICAL_TRIAL("849b0b42-f465-452b-813c-477d6398e082",
                          SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                          "Governance Action Process",
                          "Set up clinical trial",
                          "Generates the project, and governance action processes needed to drive a clinical trial.",
                          "V2.0",
                          new SolutionBlueprint[]{SolutionBlueprint.CLINICAL_TRIAL_MANAGEMENT},
                          null,
                          null,
                          CocoRequestTypeDefinition.SET_UP_CLINICAL_TRIAL.getGovernanceActionTypeGUID()),

    SUSTAINABILITY_ODS("fc55ef2d-a88d-44ee-94cb-3fca9b9af8b4",
                       SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                       "PostgreSQL Database",
                       "Sustainability Operational Data Store (ODS)",
                       "A store for both the raw data needed for the sustainability calculations and the results.",
                       "V1.0",
                       new SolutionBlueprint[]{SolutionBlueprint.SUSTAINABILITY_REPORTING},
                       null,
                       new InformationSupplyChainSegment[]{
                               InformationSupplyChainSegment.SUSTAINABILITY_DATA_GATHERING,
                               InformationSupplyChainSegment.SUSTAINABILITY_ASSESSMENT},
                       null),

    SUSTAINABILITY_CALCULATORS("06edd666-06fd-43ef-b7bd-22e2651c334f",
                               SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                               "Analytics Application",
                               "Sustainability Calculators",
                               "Algorithms that calculate the impact of Coco Pharmaceuticals' operation and the changes that are making a difference.",
                               "V1.0",
                               new SolutionBlueprint[]{SolutionBlueprint.SUSTAINABILITY_REPORTING},
                               null,
                               new InformationSupplyChainSegment[]{
                                       InformationSupplyChainSegment.SUSTAINABILITY_ASSESSMENT},
                               null),

    SUSTAINABILITY_DASHBOARDS("d50a6f1f-49d2-47c3-a55e-5844464bd26f",
                               SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                               "SuperSet Application",
                               "Sustainability Dashboards",
                               "Dashboards that illustrate Coco Pharmaceuticals' sustainability position.",
                               "V1.0",
                              new SolutionBlueprint[]{SolutionBlueprint.SUSTAINABILITY_REPORTING},
                               null,
                               new InformationSupplyChainSegment[]{
                                       InformationSupplyChainSegment.DELIVER_SUSTAINABILITY_REPORT},
                              null),

    EMPLOYEE_EXPENSE_TOOL("02cdce9a-7630-479a-90de-fd7698d098f1",
                          SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                          "Cloud Application",
                          "Employee Expense Tool",
                          "Application for recording and categorizing employee expenses, and authorizing the repayment.",
                          "V1.0",
                          new SolutionBlueprint[]{
                                  SolutionBlueprint.SUSTAINABILITY_REPORTING,
                                  SolutionBlueprint.EMPLOYEE_MANAGEMENT},
                          null,
                          new InformationSupplyChainSegment[]{
                                  InformationSupplyChainSegment.SUSTAINABILITY_DATA_GATHERING},
                          null),
    HAZMAT_INVENTORY("25fd5be7-692d-4752-9dc7-30068a7d665e",
                     SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                     "COTS Application",
                     "Hazardous Materials (HazMat) Inventory",
                     "Application for recording and tracing hazardous materials.  This includes greenhouse gasses such as CO2 and Hydro-fluorocarbons.",
                     "V1.0",
                     new SolutionBlueprint[]{
                             SolutionBlueprint.SUSTAINABILITY_REPORTING,
                             SolutionBlueprint.HAZARDOUS_MATERIAL_MANAGEMENT},
                     null,
                     new InformationSupplyChainSegment[]{
                             InformationSupplyChainSegment.SUSTAINABILITY_DATA_GATHERING},
                     null),

    ACCOUNTING_LEDGER("2c0f5a4e-bb02-4081-a80e-3072ca99a1aa",
                      SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                      "Cloud Application",
                      "Accounting ledgers",
                      "Application for recording and tracing the income and spending of Coco Pharmaceuticals.  This can help to identify how much the company is spending on particular materials and activities.",
                      "V1.0",
                      new SolutionBlueprint[]{
                              SolutionBlueprint.SUSTAINABILITY_REPORTING,
                              SolutionBlueprint.PERSONALIZED_MEDICINE_ORDER_FULFILLMENT},
                      null,
                      new InformationSupplyChainSegment[]{
                              InformationSupplyChainSegment.SUSTAINABILITY_DATA_GATHERING},
                      null),

    GOODS_INVENTORY("50768e61-43b6-4241-96a3-4c413582ec1f",
                    SolutionComponentType.AUTOMATED_PROCESS.getSolutionComponentType(),
                    "COTS Application",
                    "Goods Inventory",
                    "Application for recording and tracing physical materials as they are acquired, stored, distributed and used.",
                    "V1.0",
                    new SolutionBlueprint[]{
                            SolutionBlueprint.SUSTAINABILITY_REPORTING,
                            SolutionBlueprint.INVENTORY_MANAGEMENT},
                    null,
                    new InformationSupplyChainSegment[]{
                            InformationSupplyChainSegment.SUSTAINABILITY_DATA_GATHERING},
                    null),


    ;


    private final String                          guid;
    private final String                          componentType;
    private final String                          implementationType;
    private final String                          displayName;
    private final String                          description;
    private final String                          versionIdentifier;
    private final SolutionBlueprint[]             consumingBlueprints;
    private final SolutionComponent[]             subComponents;
    private final InformationSupplyChainSegment[] linkedFromSegment;
    private final String                          implementedBy;


    /**
     * Construct an enum instance.
     *
     * @param guid unique identifier
     * @param componentType   type of solution component - ege automated process
     * @param implementationType   type of software component - for example, is it a process, of file or database.
     * @param displayName display name of solution component
     * @param description description of solution component
     * @param versionIdentifier version identifier of the solution component
     * @param consumingBlueprints the blueprint that this belongs to
     * @param subComponents optional subcomponents of the solution
     * @param linkedFromSegment array of segments that are implemented by this component
     */
    SolutionComponent(String                          guid,
                      String                          componentType,
                      String                          implementationType,
                      String                          displayName,
                      String                          description,
                      String                          versionIdentifier,
                      SolutionBlueprint[]             consumingBlueprints,
                      SolutionComponent[]             subComponents,
                      InformationSupplyChainSegment[] linkedFromSegment,
                      String                          implementedBy)
    {
        this.guid                = guid;
        this.componentType       = componentType;
        this.implementationType  = implementationType;
        this.displayName         = displayName;
        this.description         = description;
        this.versionIdentifier   = versionIdentifier;
        this.consumingBlueprints = consumingBlueprints;
        this.subComponents       = subComponents;
        this.linkedFromSegment   = linkedFromSegment;
        this.implementedBy       = implementedBy;
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
     * Return the type of solution component - for example, is it a process, of file or database.
     *
     * @return string
     */
    public String getComponentType()
    {
        return componentType;
    }


    /**
     * Return which type of software component is likely to be deployed to implement this solution component.
     *
     * @return string
     */
    public String getImplementationType()
    {
        return implementationType;
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
     * Return the description of the solution blueprint
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the version identifier of the solution blueprint.
     *
     * @return string
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    public List<SolutionBlueprint> getConsumingBlueprints()
    {
        if (consumingBlueprints == null)
        {
            return null;
        }

        return Arrays.asList(consumingBlueprints);
    }


    /**
     * Return the optional list of subcomponents.
     *
     * @return null or list
     */
    public List<SolutionComponent> getSubComponents()
    {
        if (subComponents == null)
        {
            return null;
        }

        return Arrays.asList(subComponents);
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
     * Return the GUID of the implementation element (or null)
     *
     * @return guid
     */
    public String getImplementedBy()
    {
        return implementedBy;
    }

    /**
     * Return the unique name of the solution blueprint.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "SolutionComponent::" + displayName + "::" + versionIdentifier;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SolutionComponent{" + displayName + '}';
    }
}
