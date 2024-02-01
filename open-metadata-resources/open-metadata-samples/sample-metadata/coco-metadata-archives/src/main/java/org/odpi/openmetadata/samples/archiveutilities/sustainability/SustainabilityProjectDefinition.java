/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;

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
            "Sustainability Campaign",
            "Campaign coordinating the sustainability initiatives in Coco Pharmaceuticals.",
            true,
            false,
            null,
            null,
            null),

    /**
     * Project to establish a sustainability focus and practices in Coco Pharmaceuticals.
     */
    SUS_BOOTSTRAP("Project:Sustainability Bootstrap",
                  "Sustainability Bootstrap Project",
                  "Project to establish a sustainability focus and practices in Coco Pharmaceuticals.",
                  false,
                  false,
                  SustainabilityProjectDefinition.SUS_GOV,
                  null,
                  null),


    ;

    private final String                          qualifiedName;
    private final String                          displayName;
    private final String                          description;
    private final boolean                         isCampaign;
    private final boolean                         isTask;
    private final SustainabilityProjectDefinition controllingProject;
    private final PersonDefinition[]              leaders;
    private final PersonDefinition[]              members;

    /**
     * The constructor creates an instance of the enum
     *
     * @param qualifiedName   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     * @param isCampaign is this a collection of related projects
     * @param isTask is this a task within a project
     * @param controllingProject team that this team reports to - can be null if the entry is an organization
     * @param leaders person to link into the leadership role
     * @param members list of people who are members of the team or organization
     */
    SustainabilityProjectDefinition(String                          qualifiedName,
                                    String                          displayName,
                                    String                          description,
                                    boolean                         isCampaign,
                                    boolean                         isTask,
                                    SustainabilityProjectDefinition controllingProject,
                                    PersonDefinition[]              leaders,
                                    PersonDefinition[]              members)
    {
        this.qualifiedName = qualifiedName;
        this.displayName = displayName;
        this.description = description;
        this.isCampaign = isCampaign;
        this.isTask = isTask;
        this.controllingProject = controllingProject;
        this.leaders = leaders;
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
     * Which project provides direction?
     *
     * @return project
     */
    public SustainabilityProjectDefinition getControllingProject()
    {
        return controllingProject;
    }


    /**
     * Who is the leader for this project?
     *
     * @return person
     */
    public List<PersonDefinition> getLeader()
    {
        if (leaders != null)
        {
            return Arrays.asList(leaders);
        }

        return null;
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
