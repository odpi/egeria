/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Process properties defines the properties of a process.  A process is a series of steps and decisions in operation
 * in the organization.  It is implemented by one or more digital resources.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeployedSoftwareComponentProperties.class, name = "DeployedSoftwareComponentProperties"),
        @JsonSubTypes.Type(value = ActionProperties.class, name = "ActionProperties"),
})
public class ProcessProperties extends AssetProperties
{
    private Date           requestedTime             = null;
    private Date           requestedStartTime        = null;
    private Date           startTime                 = null;
    private Date           dueTime                   = null;
    private Date           lastReviewTime            = null;
    private Date           lastPauseTime             = null;
    private Date           lastResumeTime            = null;
    private Date           completionTime            = null;
    private int            priority                  = 0;
    private String         formula                   = null;
    private String         formulaType               = null;
    private ActivityStatus activityStatus            = ActivityStatus.REQUESTED;
    private String         userDefinedActivityStatus = null;


    /**
     * Default constructor
     */
    public ProcessProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.PROCESS.typeName);;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public ProcessProperties(ProcessProperties template)
    {
        super(template);

        if (template != null)
        {
            requestedTime             = template.getRequestedTime();
            requestedStartTime        = template.getRequestedStartTime();
            startTime                 = template.getStartTime();
            lastReviewTime            = template.getLastReviewTime();
            dueTime                   = template.getDueTime();
            lastPauseTime             = template.getLastPauseTime();
            lastResumeTime            = template.getLastResumeTime();
            completionTime            = template.getCompletionTime();
            priority                  = template.getPriority();
            formula                   = template.getFormula();
            formulaType               = template.getFormulaType();
            activityStatus            = template.getActivityStatus();
            userDefinedActivityStatus = template.getUserDefinedActivityStatus();
        }
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public ProcessProperties(AssetProperties template)
    {
        super(template);
    }


    /**
     * Return the description of the processing performed by this process.
     *
     * @return string description
     */
    public String getFormula() { return formula; }


    /**
     * Set up the description of the processing performed by this process.
     *
     * @param formula string description
     */
    public void setFormula(String formula)
    {
        this.formula = formula;
    }


    /**
     * Return the specification language for the formula.
     *
     * @return string description
     */
    public String getFormulaType()
    {
        return formulaType;
    }


    /**
     * Set up  the specification language for the formula.
     *
     * @param formulaType string description
     */
    public void setFormulaType(String formulaType)
    {
        this.formulaType = formulaType;
    }


    /**
     * Return the time that this action was created.
     *
     * @return date
     */
    public Date getRequestedTime()
    {
        return requestedTime;
    }


    /**
     * Set up the time that the action was created.
     *
     * @param requestedTime data
     */
    public void setRequestedTime(Date requestedTime)
    {
        this.requestedTime = requestedTime;
    }


    /**
     * Return the time that the process should start.
     *
     * @return date/time
     */
    public Date getRequestedStartTime()
    {
        return requestedStartTime;
    }

    /**
     * Set up the time that the process should start.
     *
     * @param requestedStartTime date/time
     */
    public void setRequestedStartTime(Date requestedStartTime)
    {
        this.requestedStartTime = requestedStartTime;
    }


    /**
     * Return the date/time that this process started (instance only).
     *
     * @return date
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the the date/time that this process started (instance only).
     *
     * @param startTime date
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }



    /**
     * Return when this needs to be completed by.
     *
     * @return date
     */
    public Date getDueTime()
    {
        return dueTime;
    }


    /**
     * Set up when this needs to be completed by.
     *
     * @param dueTime date
     */
    public void setDueTime(Date dueTime)
    {
        this.dueTime = dueTime;
    }


    /**
     * Return the last time the action was reviewed for priority, ownership etc.
     *
     * @return date
     */
    public Date getLastReviewTime()
    {
        return lastReviewTime;
    }


    /**
     * Set up the last time the action was reviewed for priority, ownership etc.
     *
     * @param lastReviewTime data
     */
    public void setLastReviewTime(Date lastReviewTime)
    {
        this.lastReviewTime = lastReviewTime;
    }



    /**
     * Return when this was completed - or null if still outstanding.
     *
     * @return date
     */
    public Date getLastPauseTime()
    {
        return lastPauseTime;
    }


    /**
     * Set up when this was completed - or null if still outstanding.
     *
     * @param lastPauseTime date
     */
    public void setLastPauseTime(Date lastPauseTime)
    {
        this.lastPauseTime = lastPauseTime;
    }


    /**
     * Return the last time the process was resumed.
     *
     * @return date
     */
    public Date getLastResumeTime()
    {
        return lastResumeTime;
    }


    /**
     * Set up the last time the process was resumed.
     *
     * @param lastResumeTime date
     */
    public void setLastResumeTime(Date lastResumeTime)
    {
        this.lastResumeTime = lastResumeTime;
    }

    /**
     * Return the date/time that this process ended (instance only).
     *
     * @return date
     */
    public Date getCompletionTime()
    {
        return completionTime;
    }


    /**
     * Set up the date/time that this process ended (instance only).
     *
     * @param completionTime date
     */
    public void setCompletionTime(Date completionTime)
    {
        this.completionTime = completionTime;
    }

    /**
     * Return how important this is.
     *
     * @return int
     */
    public int getPriority()
    {
        return priority;
    }


    /**
     * Set up how important this is.
     *
     * @param priority integer
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }




    /**
     * Return the status of the action/to do.
     *
     * @return status enum
     */
    public ActivityStatus getActivityStatus()
    {
        return activityStatus;
    }


    /**
     * Set up the status of the action/to do.
     *
     * @param activityStatus status enum
     */
    public void setActivityStatus(ActivityStatus activityStatus)
    {
        this.activityStatus = activityStatus;
    }


    /**
     * Return additionally defined activity statuses.
     *
     * @return string
     */
    public String getUserDefinedActivityStatus()
    {
        return userDefinedActivityStatus;
    }


    /**
     * Set up additionally defined activity statuses.
     *
     * @param userDefinedActivityStatus string
     */
    public void setUserDefinedActivityStatus(String userDefinedActivityStatus)
    {
        this.userDefinedActivityStatus = userDefinedActivityStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ProcessProperties{" +
                "requestedTime=" + requestedTime +
                ", startTime=" + startTime +
                ", dueTime=" + dueTime +
                ", lastReviewTime=" + lastReviewTime +
                ", lastPauseTime=" + lastPauseTime +
                ", lastResumeTime=" + lastResumeTime +
                ", completionTime=" + completionTime +
                ", priority=" + priority +
                ", formula='" + formula + '\'' +
                ", formulaType='" + formulaType + '\'' +
                ", activityStatus=" + activityStatus +
                ", userDefinedActivityStatus='" + userDefinedActivityStatus + '\'' +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ProcessProperties that = (ProcessProperties) objectToCompare;
        return priority == that.priority && Objects.equals(requestedTime, that.requestedTime) && Objects.equals(startTime, that.startTime) && Objects.equals(dueTime, that.dueTime) && Objects.equals(lastReviewTime, that.lastReviewTime) && Objects.equals(lastPauseTime, that.lastPauseTime) && Objects.equals(lastResumeTime, that.lastResumeTime) && Objects.equals(completionTime, that.completionTime) && Objects.equals(formula, that.formula) && Objects.equals(formulaType, that.formulaType) && activityStatus == that.activityStatus && Objects.equals(userDefinedActivityStatus, that.userDefinedActivityStatus);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), requestedTime, startTime, dueTime, lastReviewTime, lastPauseTime, lastResumeTime, completionTime, priority, formula, formulaType, activityStatus, userDefinedActivityStatus);
    }
}
