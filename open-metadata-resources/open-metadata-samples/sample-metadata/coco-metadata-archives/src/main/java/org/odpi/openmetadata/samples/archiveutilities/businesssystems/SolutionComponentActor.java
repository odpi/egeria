/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.businesssystems;

/**
 * Define the relationship between the solution roles and the solution components
 */
public enum SolutionComponentActor
{
    COORDINATOR_TO_PARTICIPATING_HOSPITAL(SolutionRoleDefinition.PARTICIPATING_HOSPITAL_COORDINATOR,
                                          SolutionComponent.HOSPITAL,
                                          "Coordinator on behalf of hospital",
                                          "Coordinate data agreements, data collection and sharing"),
    PARTICIPATING_HOSPITAL_TO_HOSPITAL(SolutionRoleDefinition.PARTICIPATING_HOSPITAL,
                                       SolutionComponent.HOSPITAL,
                                       "Owner",
                                       "Owns the hospital processes that supports the clinical trial."),

    DATA_ENGINEER_TO_SET_UP_DATA_LAKE(SolutionRoleDefinition.CLINICAL_TRIAL_DATA_ENGINEER,
                                      SolutionComponent.SET_UP_DATA_LAKE_FOLDER,
                                      "Initiator",
                                      "Chooses where the data will go in the data lake and initiates the process to set up the data lake folder and Unity Catalog Volume."),
    DATA_ENGINEER_TO_ONBOARD_HOSPITAL(SolutionRoleDefinition.CLINICAL_TRIAL_DATA_ENGINEER,
                                      SolutionComponent.ONBOARD_HOSPITAL,
                                      "Initiator",
                                      "Calls the process to set up the onboarding pipeline for each participating hospital."),
    DATA_ENGINEER_TO_ONBOARDING_PIPELINE(SolutionRoleDefinition.CLINICAL_TRIAL_DATA_ENGINEER,
                                         SolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE,
                                         "Steward",
                                         "Notified by the onboarding pipeline if data from a hospital contains invalid values."),

    DATA_SCIENTIST_TO_ANALYSE_PATIENT_DATA(SolutionRoleDefinition.CLINICAL_TRIAL_DATA_SCIENTIST,
                                           SolutionComponent.ANALYSE_PATIENT_DATA,
                                           "Data Analyser",
                                           "Runs the analysis model workflow to analyse the weekly measurements, and validates results."),
    RESEARCHER_TO_ANALYSE_PATIENT_DATA(SolutionRoleDefinition.CLINICAL_TRIAL_RESEARCHER,
                                       SolutionComponent.ANALYSE_PATIENT_DATA,
                                       "Results Interpreter",
                                       "Interprets the results of the weekly measurements analysis."),

    MANAGER_TO_SET_UP_TRIAL(SolutionRoleDefinition.CLINICAL_TRIAL_MANAGER,
                            SolutionComponent.SET_UP_CLINICAL_TRIAL,
                            "Initiator",
                            "Based on agreement with the clinical trial sponsor, initiates the set up of a new clinical trial."),
    MANAGER_TO_NOMINATE_HOSPITAL(SolutionRoleDefinition.CLINICAL_TRIAL_MANAGER,
                                 SolutionComponent.NOMINATE_HOSPITAL,
                                 "Initiator",
                                 "Initiates the set up of a new data sharing agreement and certification process with a hospital."),
    MANAGER_TO_CERTIFY_HOSPITAL(SolutionRoleDefinition.CLINICAL_TRIAL_MANAGER,
                                SolutionComponent.CERTIFY_HOSPITAL,
                                "Certifier",
                                "Certified that a hospital is certified to participate in the clinical trial."),
    MANAGER_TO_ONBOARDING_PIPELINE(SolutionRoleDefinition.CLINICAL_TRIAL_MANAGER,
                                   SolutionComponent.WEEKLY_MEASUREMENTS_ONBOARDING_PIPELINE,
                                   "Steward",
                                   "Notified by the onboarding pipeline if data from a hospital contains invalid values."),
    MANAGER_TO_ASSEMBLE_REPORT(SolutionRoleDefinition.CLINICAL_TRIAL_MANAGER,
                               SolutionComponent.ASSEMBLE_REPORT,
                               "Author",
                               "Assembles the treatment efficacy evidence and process descriptions into the report."),
    SPONSOR_TO_VALIDATION_AND_DELIVERY(SolutionRoleDefinition.CLINICAL_TRIAL_SPONSOR,
                                       SolutionComponent.REPORT_VALIDATION_AND_DELIVERY,
                                       "Reviewer",
                                       "Reviews report and discussed findings with regulator."),

    SUSTAINABILITY_LEADER_TO_DASHBOARD(SolutionRoleDefinition.SUSTAINABILITY_LEADER,
                                       SolutionComponent.SUSTAINABILITY_DASHBOARDS,
                                       "Review Status",
                                       "Reviews current status of company's sustainability position."),

    SUSTAINABILITY_EXECUTIVE_TO_DASHBOARD(SolutionRoleDefinition.SUSTAINABILITY_EXECUTIVE,
                                          SolutionComponent.SUSTAINABILITY_DASHBOARDS,
                                          "Review Status",
                                          "Reviews current status of company's sustainability position."),

    SUSTAINABILITY_CHAMPION_TO_DASHBOARD(SolutionRoleDefinition.SUSTAINABILITY_CHAMPION,
                                         SolutionComponent.SUSTAINABILITY_DASHBOARDS,
                                         "Review Status",
                                         "Reviews current status of company's sustainability position."),
    ;

    final SolutionRoleDefinition solutionRole;
    final SolutionComponent      solutionComponent;
    final String                 role;
    final String                 description;

    SolutionComponentActor(SolutionRoleDefinition solutionRole,
                           SolutionComponent      solutionComponent,
                           String                 role,
                           String                 description)
    {
        this.solutionRole      = solutionRole;
        this.solutionComponent = solutionComponent;
        this.role              = role;
        this.description       = description;
    }


    public SolutionRoleDefinition getSolutionRole()
    {
        return solutionRole;
    }

    public SolutionComponent getSolutionComponent()
    {
        return solutionComponent;
    }

    public String getRole()
    {
        return role;
    }

    public String getDescription()
    {
        return description;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SolutionComponentActor{" +
                "solutionRole=" + solutionRole +
                ", solutionComponent=" + solutionComponent +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                "}";
    }
}
