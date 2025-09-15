/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceengines;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;

/**
 * The SolutionRoleDefinition is used to feed the definition of the solution roles for
 * Coco Pharmaceuticals' solution blueprints.
 */
public enum SolutionRoleDefinition
{
    PARTICIPATING_HOSPITAL("30adaab5-8870-47a8-8ae9-facbf84cb05a",
                           "ClinicalTrialParticipatingHospital",
                           "Clinical Trial Participating Hospital",
                           "A hospital that is participating in a clinical trial."),

    PARTICIPATING_HOSPITAL_COORDINATOR("a8bd84ca-0aae-4534-b0e8-87e8659467a6",
                                       "ClinicalTrialParticipatingHospitalCoordinator",
                                       "Clinical Trial Participating Hospital Coordinator",
                                       "A contact person from a hospital that is participating in a clinical trial that is responsible for ensuring the hospital's participation is operating according to the data agreements."),

    CLINICAL_TRIALS_EXECUTIVE("2a404b2e-c13b-4971-af57-4a29afc116bf",
                           "ClinicalTrialsExecutive",
                           "Clinical Trial Executive",
                           "An executive accountable for the compliant operation of all clinical trials."),

    CLINICAL_TRIAL_SPONSOR("f6bc847b-868d-43cc-b767-41f5fe3e47d1",
                           "ClinicalTrialSponsor",
                           "Clinical Trial Sponsor",
                           "An executive accountable for the compliant operation of a clinical trial."),

    CLINICAL_TRIAL_MANAGER("f37f3735-28a1-4e03-9ff5-3fe2f137f661",
                           "ClinicalTrialManager",
                           "Clinical Trial Manager",
                           "A person responsible for the smooth and compliant operating of the clinical trial."),

    CLINICAL_TRIAL_DATA_ENGINEER("b0290339-c96c-4b05-904f-12fc98e54e14",
                                 "CertifiedDataEngineer",
                                 "Certified Data Engineer",
                                 "A data engineer that is certified to build pipelines that work with patient and research data."),

    CLINICAL_TRIAL_DATA_SCIENTIST("ece17806-836c-4756-b3a2-2d12dde215f6",
                                  "NewTreatmentDataScientist",
                                  "New Treatment Data Scientist",
                                  "A Coco Pharmaceutical's data scientist working with the research team developing a new treatment."),

    CLINICAL_TRIAL_RESEARCHER("0c757e35-8a42-4d5f-b01b-c72a6cea65cc",
                              "NewTreatmentResearcher",
                              "New Treatment Researcher",
                              "A member of Coco Pharmaceutical's Trial Research Team responsible for the development of the new treatment under trial."),

    SUSTAINABILITY_LEADER("3d6e9e6e-6dda-4586-a114-d8e7a1f47972",
                              "SustainabilityLeader",
                              "Sustainability Leader",
                              "A Coco Pharmaceutical's leader responsible for the measurement and improvement of the company's sustainability position."),

    SUSTAINABILITY_EXECUTIVE("2942cd61-d8ad-427d-847a-dae4a1a6f32f",
                             "SustainabilityExecutive",
                             "Sustainability Executive",
                             "A Coco Pharmaceuticals Board Member accountable for the company's sustainability."),

    SUSTAINABILITY_CHAMPION("ac0e0d11-6523-499f-bcdb-5db15fc49ac8",
                             "SustainabilityChampion",
                             "Sustainability Champion",
                             "A Coco Pharmaceuticals employee focused on improving sustainability in their work environment."),
    ;

    private final String                 guid;
    private final String                 identifier;
    private final String                 displayName;
    private final String                 description;


    /**
     * SolutionRoleDefinition constructor creates an instance of the enum
     *
     * @param guid         unique identifier for the role
     * @param identifier   unique Id for the role
     * @param displayName   text for the role
     * @param description   description of the assets in the role
     */
    SolutionRoleDefinition(String                 guid,
                           String                 identifier,
                           String                 displayName,
                           String                 description)
    {
        this.guid       = guid;
        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Return the name of the PersonRole type to use.
     *
     * @return type name
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Returns the unique name for the role entity.
     *
     * @return identifier
     */
    public String getQualifiedName()
    {
        return "SolutionActorRole::" + guid + "::" + identifier;
    }


    /**
     * Returns the unique name for the role.
     *
     * @return identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Returns a descriptive name of the role.
     *
     * @return display name
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Returns a detailed description of the role.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns the breadth of responsibility for the role.
     *
     * @return scope
     */
    public ScopeDefinition getScope()
    {
        return ScopeDefinition.WITHIN_SOLUTION;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SolutionRoleDefinition{" + "identifier='" + identifier + '}';
    }
}
