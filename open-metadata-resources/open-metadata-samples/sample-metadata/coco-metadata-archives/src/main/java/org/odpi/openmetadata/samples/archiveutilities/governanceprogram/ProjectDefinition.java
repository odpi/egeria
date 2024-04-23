/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * The ProjectDefinition is used to feed the definition of the projects for Coco Pharmaceuticals scenarios.
 */
public enum ProjectDefinition
{
    /**
     * CAMP-MM - Project to upgrade manufacturing process to support personalized medicine.
     */
    MANUFACTURING_MOD("Campaign:Manufacturing Modernization",
                      "CAMP-MM",
                      "Manufacturing Modernization Project",
                      "Project to upgrade manufacturing process to support personalized medicine.",
                      true,
                      false,
                      null,
                      ProjectStatusDefinition.ACTIVATING,
                      null,
                      null,
                      PersonDefinition.STEW_FASTER,
                      null),

    /**
     * CAMP-CT - Developing efficient ways to manage each of the clinical trials.
     */
    CLINICAL_TRIALS("Campaign:Clinical Trials Management",
                    "CAMP-CT",
                    "Clinical Trials Management",
                    "Developing efficient ways to manage each of the clinical trials.",
                    true,
                    false,
                    OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION_TYPE_NAME,
                    ProjectStatusDefinition.ACTIVE,
                    null,
                    null,
                    PersonDefinition.TANYA_TIDIE,
                    null),

    /**
     * PROJ-CT-TBDF - Clinical trial related to the new treatment for Teddy Bear Drop Foot.
     */
    DROP_FOOT_CLINICAL_TRIAL("Clinical Trial:Teddy Bear Drop Foot",
                             "PROJ-CT-TBDF",
                             "Teddy Bear Drop Foot Clinical Trial",
                             "Clinical trial related to the new treatment for Teddy Bear Drop Foot.",
                             false,
                             false,
                             OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION_TYPE_NAME,
                             ProjectStatusDefinition.ACTIVE,
                             CLINICAL_TRIALS,
                             null,
                             PersonDefinition.TANYA_TIDIE,
                             null),

    /**
     * PROJ-CT-TBDF-001 - Setting up the systems that will support the clinical trial related to the new treatment for Teddy Bear Drop Foot.
     */
    DROP_FOOT_CLINICAL_TRIAL_IT("Clinical Trial:Teddy Bear Drop Foot:IT Setup",
                                "PROJ-CT-TBDF-001",
                                "Teddy Bear Drop Foot Clinical Trial IT Setup",
                                "Setting up the systems that will support the clinical trial related to the new treatment for Teddy Bear Drop Foot.",
                                false,
                                false,
                                null,
                                ProjectStatusDefinition.COMPLETED,
                                DROP_FOOT_CLINICAL_TRIAL,
                                null,
                                PersonDefinition.ERIN_OVERVIEW,
                                null),

    /**
     * PROJ-CT-TBDF-002 - Templates for onboarding of Teddy Bear Drop Foot related data.
     */
    DROP_FOOT_TEMPLATES("Clinical Trial:Teddy Bear Drop Foot:Templates",
                        "PROJ-CT-TBDF-002",
                        "Teddy Bear Drop Foot Clinical Trial Templates",
                        "Templates for onboarding of Teddy Bear Drop Foot related data.",
                        false,
                        true,
                        null,
                        ProjectStatusDefinition.ACTIVATING,
                        DROP_FOOT_CLINICAL_TRIAL_IT,
                        null,
                        PersonDefinition.PETER_PROFILE,
                        null),

    /**
     * PROJ-CT-TBDF-003 - Pipelines for onboarding of Teddy Bear Drop Foot related data.
     */
    DROP_FOOT_DATA_PIPELINES("Clinical Trial:Teddy Bear Drop Foot:Data Pipelines",
                             "PROJ-CT-TBDF-003",
                             "Teddy Bear Drop Foot Clinical Trial Data Pipelines",
                             "Pipelines for onboarding of Teddy Bear Drop Foot related data.",
                             false,
                             true,
                             null,
                             ProjectStatusDefinition.PLANNED,
                             DROP_FOOT_CLINICAL_TRIAL_IT,
                             new ProjectDefinition[]{ProjectDefinition.DROP_FOOT_TEMPLATES},
                             PersonDefinition.BOB_NITTER,
                             null),
    ;

    private final String                  qualifiedName;
    private final String                  identifier;
    private final String                  displayName;
    private final String                  description;
    private final boolean                 isCampaign;
    private final boolean                 isTask;
    private final String                  projectTypeClassification;
    private final ProjectStatusDefinition projectStatus;
    private final ProjectDefinition       controllingProject;
    private final ProjectDefinition[]     dependentOn;
    private final PersonDefinition        leader;
    private final PersonDefinition[]      members;

    /**
     * The constructor creates an instance of the enum
     *
     * @param qualifiedName   unique id for the  entity
     * @param identifier   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     * @param isCampaign is this a collection of related projects
     * @param isTask is this a task within a project
     * @param projectTypeClassification should a classification be added to describe the type of project
     * @param projectStatus what is the status of the project
     * @param controllingProject project that this project reports to - can be null if the project is standalone or a campaign
     * @param dependentOn projects that this project is dependent on
     * @param leader person to link into the leadership role
     * @param members list of people who are members of the team or organization
     */
    ProjectDefinition(String                     qualifiedName,
                      String                     identifier,
                      String                     displayName,
                      String                     description,
                      boolean                    isCampaign,
                      boolean                    isTask,
                      String                     projectTypeClassification,
                      ProjectStatusDefinition    projectStatus,
                      ProjectDefinition          controllingProject,
                      ProjectDefinition[]        dependentOn,
                      PersonDefinition           leader,
                      PersonDefinition[]         members)
    {
        this.qualifiedName = qualifiedName;
        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
        this.isCampaign = isCampaign;
        this.isTask = isTask;
        this.projectTypeClassification = projectTypeClassification;
        this.projectStatus = projectStatus;
        this.dependentOn = dependentOn;
        this.controllingProject = controllingProject;
        this.leader = leader;
        this.members = members;
    }


    /**
     * Return the qualified name for the project.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the short identifier for the project.
     *
     * @return string name
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the display name for the project.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description for the project.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Is this project a campaign?
     *
     * @return boolean
     */
    public boolean isCampaign() { return isCampaign; }


    /**
     * Is the project a task?
     *
     * @return boolean
     */
    public boolean isTask() { return isTask; }


    /**
     * How should this project be classified?
     *
     * @return classification
     */
    public String getProjectTypeClassification()
    {
        return projectTypeClassification;
    }


    /**
     * What is the project's current status?
     *
     * @return status
     */
    public ProjectStatusDefinition getProjectStatus()
    {
        return projectStatus;
    }


    /**
     * Which project provides direction?
     *
     * @return project
     */
    public ProjectDefinition getControllingProject()
    {
        return controllingProject;
    }


    /**
     * Which projects is this project dependent on?
     *
     * @return list of projects
     */
    public List<ProjectDefinition> getDependentOn()
    {
        if (dependentOn != null)
        {
            return Arrays.asList(dependentOn);
        }

        return null;
    }


    /**
     * Who is the leader for this project?
     *
     * @return person
     */
    public PersonDefinition getLeader()
    {
        return leader;
    }


    /**
     * Return the team members.
     *
     * @return list of people
     */
    public List<PersonDefinition> getMembers()
    {
        if (members != null)
        {
            return Arrays.asList(members);
        }

        return null;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProjectDefinition{" + displayName + '}';
    }
}
