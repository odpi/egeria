/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActionTargetProperties identifies the properties of an ActionTarget relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActionTargetProperties extends RelationshipBeanProperties
{
    private String         actionTargetName  = null;
    private ActivityStatus status            = null;
    private Date           startTime         = null;
    private Date           completionTime    = null;
    private String         completionMessage = null;

    /**
     * Typical Constructor
     */
    public ActionTargetProperties()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public ActionTargetProperties(ActionTargetProperties template)
    {
        if (template != null)
        {
            actionTargetName  = template.getActionTargetName();
            status            = template.getStatus();
            startTime         = template.getStartTime();
            completionTime    = template.getCompletionTime();
            completionMessage = template.getCompletionMessage();
        }
    }


    /**
     * Return the name assigned to this action target.  This name helps to guide the actor in its processing of this action target.
     *
     * @return string name
     */
    public String getActionTargetName()
    {
        return actionTargetName;
    }


    /**
     * Set up the name assigned to this action target.  This name helps to guide the actor in its processing of this action target.
     *
     * @param actionTargetName string name
     */
    public void setActionTargetName(String actionTargetName)
    {
        this.actionTargetName = actionTargetName;
    }



    /**
     * Return the current status of the action target.  The default value is the status is derived from
     * the  action.  However, if the actor has to process many target elements, then these values can
     * be used to show progress.
     *
     * @return status enum
     */
    public ActivityStatus getStatus()
    {
        return status;
    }


    /**
     * Set up current status of the action target.  The default value is the status is derived from
     * the action.  However, if the actor has to process many target elements, then these values can
     * be used to show progress.
     *
     * @param status enum
     */
    public void setStatus(ActivityStatus status)
    {
        this.status = status;
    }


    /**
     * Return the date/time when the actor started processing this target element. By default,
     * this value is derived from the startDate for the action.  However, if the actor has to process many target elements, then these values can
     * be used to show progress.
     *
     * @return date object
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the date/time when the actor started processing this target element. By default,
     * this value is derived from the startDate for the action.  However, if the actor has to process many target
     * elements, then these values can be used to show progress.
     *
     * @param startTime date object
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    /**
     * Return the date/time when the actor stopped processing this target element. By default,
     * this value is derived from the completionDate for the action.  However, if it has to process
     * many target elements, then these values can be used to show progress.
     *
     * @return date object
     */
    public Date getCompletionTime()
    {
        return completionTime;
    }


    /**
     * Set up the date/time when the governance action service stopped processing this target element. By default,
     * this value is derived from the completionDate for the governance action service.  However, if it has to process
     * many target elements, then these values can be used to show progress.
     *
     * @param completionTime date object
     */
    public void setCompletionTime(Date completionTime)
    {
        this.completionTime = completionTime;
    }


    /**
     * Return the optional message from the running governance service supplied on its completion.
     *
     * @return string message
     */
    public String getCompletionMessage()
    {
        return completionMessage;
    }


    /**
     * Set up optional message from the running governance service supplied on its completion.
     *
     * @param completionMessage string message
     */
    public void setCompletionMessage(String completionMessage)
    {
        this.completionMessage = completionMessage;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ActionTargetProperties{" +
                "actionTargetName='" + actionTargetName + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", completionTime=" + completionTime +
                ", completionMessage='" + completionMessage + '\'' +
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
        ActionTargetProperties that = (ActionTargetProperties) objectToCompare;
        return Objects.equals(actionTargetName, that.actionTargetName) && status == that.status && Objects.equals(startTime, that.startTime) && Objects.equals(completionTime, that.completionTime) && Objects.equals(completionMessage, that.completionMessage);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), actionTargetName, status, startTime, completionTime, completionMessage);
    }
}
