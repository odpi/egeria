/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.projects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProjectProperties describes a project.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectProperties extends ReferenceableProperties
{
    private String       mission               = null;
    private List<String> purposes              = null;
    private String       projectPhase          = null;
    private String       projectHealth         = null;
    private String       projectStatus         = null;
    private int          priority              = 0;
    private Date         plannedStartDate      = null;
    private Date         actualStartDate       = null;
    private Date         plannedCompletionDate = null;
    private Date         actualCompletionDate  = null;


    /**
     * Default constructor
     */
    public ProjectProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.PROJECT.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProjectProperties(ProjectProperties template)
    {
        super(template);

        if (template != null)
        {
            this.mission               = template.getMission();
            this.purposes              = template.getPurposes();
            this.projectPhase          = template.getProjectPhase();
            this.projectHealth         = template.getProjectHealth();
            this.projectStatus         = template.getProjectStatus();
            this.priority              = template.getPriority();
            this.plannedStartDate      = template.getPlannedStartDate();
            this.actualStartDate       = template.getActualStartDate();
            this.plannedCompletionDate = template.getPlannedCompletionDate();
            this.actualCompletionDate  = template.getPlannedCompletionDate();
        }
    }


    /**
     * Return the mission of the project.
     *
     * @return string
     */
    public String getMission()
    {
        return mission;
    }


    /**
     * Set up the mission of the project.
     *
     * @param mission string
     */
    public void setMission(String mission)
    {
        this.mission = mission;
    }


    /**
     * Return the processing purposes permitted on the project.
     *
     * @return list
     */
    public List<String> getPurposes()
    {
        return purposes;
    }


    /**
     * Set up the processing purposes permitted on the project.
     *
     * @param purposes list
     */
    public void setPurposes(List<String> purposes)
    {
        this.purposes = purposes;
    }


    /**
     * Return the current phase in the project lifecycle.
     *
     * @return string
     */
    public String getProjectPhase()
    {
        return projectPhase;
    }


    /**
     * Set up the current phase in the project lifecycle.
     *
     * @param projectPhase string
     */
    public void setProjectPhase(String projectPhase)
    {
        this.projectPhase = projectPhase;
    }


    /**
     * Return the current health of the project.
     *
     * @return string
     */
    public String getProjectHealth()
    {
        return projectHealth;
    }


    /**
     * Set up the current health of the project.
     *
     * @param projectHealth string
     */
    public void setProjectHealth(String projectHealth)
    {
        this.projectHealth = projectHealth;
    }


    /**
     * Return the status for this project.
     *
     * @return string
     */
    public String getProjectStatus()
    {
        return projectStatus;
    }


    /**
     * Set up the status for this project.
     *
     * @param projectStatus string
     */
    public void setProjectStatus(String projectStatus)
    {
        this.projectStatus = projectStatus;
    }


    /**
     * Return the priority of this project.
     *
     * @return int
     */
    public int getPriority()
    {
        return priority;
    }


    /**
     * Set up the priority of this project.
     *
     * @param priority int
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    /**
     * Return the planned start date for the project.
     *
     * @return date
     */
    public Date getPlannedStartDate()
    {
        return plannedStartDate;
    }


    /**
     * Set up the planned start date for the project.
     *
     * @param plannedStartDate date
     */
    public void setPlannedStartDate(Date plannedStartDate)
    {
        this.plannedStartDate = plannedStartDate;
    }


    /**
     * Return the date that the project was created.
     *
     * @return date
     */
    public Date getActualStartDate()
    {
        return actualStartDate;
    }


    /**
     * Set up the date that the project was created.
     *
     * @param actualStartDate date
     */
    public void setActualStartDate(Date actualStartDate)
    {
        this.actualStartDate = actualStartDate;
    }


    /**
     * Return the date that the project is expected to complete.
     *
     * @return date
     */
    public Date getPlannedCompletionDate()
    {
        return plannedCompletionDate;
    }


    /**
     * Set up the date that the project is expected to complete.
     *
     * @param plannedCompletionDate date
     */
    public void setPlannedCompletionDate(Date plannedCompletionDate)
    {
        this.plannedCompletionDate = plannedCompletionDate;
    }


    /**
     * Return the actual date that the project ended.
     *
     * @return date
     */
    public Date getActualCompletionDate()
    {
        return actualCompletionDate;
    }


    /**
     * Set up the actual date that the project ended.
     *
     * @param actualCompletionDate date
     */
    public void setActualCompletionDate(Date actualCompletionDate)
    {
        this.actualCompletionDate = actualCompletionDate;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProjectProperties{" +
                "mission='" + mission + '\'' +
                ", purposes=" + purposes +
                ", projectPhase='" + projectPhase + '\'' +
                ", projectHealth='" + projectHealth + '\'' +
                ", projectStatus='" + projectStatus + '\'' +
                ", priority=" + priority +
                ", plannedStartDate=" + plannedStartDate +
                ", actualStartDate=" + actualStartDate +
                ", plannedCompletionDate=" + plannedCompletionDate +
                ", actualCompletionDate=" + actualCompletionDate +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ProjectProperties that = (ProjectProperties) objectToCompare;
        return priority == that.priority &&
                Objects.equals(mission, that.mission) &&
                Objects.equals(purposes, that.purposes) &&
                Objects.equals(projectPhase, that.projectPhase) &&
                Objects.equals(projectHealth, that.projectHealth) &&
                Objects.equals(projectStatus, that.projectStatus) &&
                Objects.equals(plannedStartDate, that.plannedStartDate) &&
                Objects.equals(actualStartDate, that.actualStartDate) &&
                Objects.equals(plannedCompletionDate, that.plannedCompletionDate) &&
                Objects.equals(actualCompletionDate, that.actualCompletionDate);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mission, purposes,projectPhase, projectHealth, projectStatus, priority,
                            plannedStartDate, actualStartDate, plannedCompletionDate, actualCompletionDate);
    }
}
