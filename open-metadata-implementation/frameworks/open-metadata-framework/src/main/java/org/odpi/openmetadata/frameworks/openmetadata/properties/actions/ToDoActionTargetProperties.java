/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.actions;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ToDoStatus;

import java.util.Date;
import java.util.Objects;

public class ToDoActionTargetProperties
{
    private String     actionTargetName = null;
    private ToDoStatus status           = null;
    private Date       startDate        = null;
    private Date       completionDate    = null;
    private String     completionMessage = null;


    /**
     * Typical Constructor
     */
    public ToDoActionTargetProperties()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public ToDoActionTargetProperties(ToDoActionTargetProperties template)
    {
        if (template != null)
        {
            actionTargetName  = template.getActionTargetName();
            status            = template.getStatus();
            startDate         = template.getStartDate();
            completionDate    = template.getCompletionDate();
            completionMessage = template.getCompletionMessage();
        }
    }


    /**
     * Return the name assigned to this action target.  This name helps to guide the governance service in its processing of this action target.
     *
     * @return string name
     */
    public String getActionTargetName()
    {
        return actionTargetName;
    }


    /**
     * Set up the name assigned to this action target.  This name helps to guide the governance service in its processing of this action target.
     *
     * @param actionTargetName string name
     */
    public void setActionTargetName(String actionTargetName)
    {
        this.actionTargetName = actionTargetName;
    }


    /**
     * Return the current status of the action target.  The default value is the status is derived from
     * the engine action.  However, if it has to process many target elements, then these values can
     * be used to show progress.
     *
     * @return status enum
     */
    public ToDoStatus getStatus()
    {
        return status;
    }


    /**
     * Set up current status of the action target.  The default value is the status is derived from
     * the "To Do".  However, if the assigned person has to process many target elements, then these values can
     * be used to show progress.
     *
     * @param status enum
     */
    public void setStatus(ToDoStatus status)
    {
        this.status = status;
    }


    /**
     * Return the date/time when the governance action service started processing this target element. By default,
     * this value is derived from the startDate for the governance action service.  However, if it has to process many target elements, then these values can
     * be used to show progress.
     *
     * @return date object
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set up the date/time when the governance action service started processing this target element. By default,
     * this value is derived from the startDate for the governance action service.  However, if it has to process many target
     * elements, then these values can be used to show progress.
     *
     * @param startDate date object
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the date/time when the governance action service stopped processing this target element. By default,
     * this value is derived from the completionDate for the governance action service.  However, if it has to process
     * many target elements, then these values can be used to show progress.
     *
     * @return date object
     */
    public Date getCompletionDate()
    {
        return completionDate;
    }


    /**
     * Set up the date/time when the governance action service stopped processing this target element. By default,
     * this value is derived from the completionDate for the governance action service.  However, if it has to process
     * many target elements, then these values can be used to show progress.
     *
     * @param completionDate date object
     */
    public void setCompletionDate(Date completionDate)
    {
        this.completionDate = completionDate;
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
        return "ToDoActionTargetProperties{" +
                "actionTargetName='" + actionTargetName + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", completionDate=" + completionDate +
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
        ToDoActionTargetProperties that = (ToDoActionTargetProperties) objectToCompare;
        return status == that.status &&
                Objects.equals(actionTargetName, that.actionTargetName) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(completionDate, that.completionDate) &&
                Objects.equals(completionMessage, that.completionMessage);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(actionTargetName, status, startDate, completionDate, completionMessage);
    }

}
