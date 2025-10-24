/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.ProjectStatus;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * The ProjectDefinition is used to feed the definition of the projects and teams for Coco Pharmaceuticals scenarios.
 */
public enum SustainabilityProjectDefinition
{
    /**
     * Campaign coordinating the sustainability initiatives in Coco Pharmaceuticals.
     */
    SUS_GOV("Campaign:Sustainability",
            "CAMP-SUS",
            "Sustainability Campaign",
            "Campaign coordinating the sustainability initiatives in Coco Pharmaceuticals.",
            true,
            false,
            null,
            ProjectStatus.ACTIVE,
            null,
            null,
            PersonDefinition.TOM_TALLY,
            null),

    /**
     * Project to establish a sustainability focus and practices in Coco Pharmaceuticals.
     */
    SUS_BOOTSTRAP("Project:Sustainability Bootstrap",
                  "PROJ-SUS-BOOTSTRAP",
                  "Sustainability Bootstrap Project",
                  "Project to establish a sustainability focus and practices in Coco Pharmaceuticals.",
                  false,
                  false,
                  null,
                  ProjectStatus.ACTIVATING,
                  SustainabilityProjectDefinition.SUS_GOV,
                  null,
                  PersonDefinition.TOM_TALLY,
                  null),


    ;


    private final String                  qualifiedName;
    private final String                  identifier;
    private final String                  displayName;
    private final String                  description;
    private final boolean                 isCampaign;
    private final boolean                 isTask;
    private final String              projectTypeClassification;
    private final ProjectStatus       projectStatus;
    private final SustainabilityProjectDefinition   controllingProject;
    private final SustainabilityProjectDefinition[] dependentOn;
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
    SustainabilityProjectDefinition(String                     qualifiedName,
                                    String                     identifier,
                                    String                     displayName,
                                    String                     description,
                                    boolean                    isCampaign,
                                    boolean                    isTask,
                                    String                     projectTypeClassification,
                                    ProjectStatus                     projectStatus,
                                    SustainabilityProjectDefinition   controllingProject,
                                    SustainabilityProjectDefinition[] dependentOn,
                                    PersonDefinition                  leader,
                                    PersonDefinition[]                members)
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
    public ProjectStatus getProjectStatus()
    {
        return projectStatus;
    }


    /**
     * Which project provides direction?
     *
     * @return project
     */
    public SustainabilityProjectDefinition getControllingProject()
    {
        return controllingProject;
    }


    /**
     * Which projects is this project dependent on?
     *
     * @return list of projects
     */
    public List<SustainabilityProjectDefinition> getDependentOn()
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
        return "SustainabilityProjectDefinition{" + displayName + '}';
    }
}
