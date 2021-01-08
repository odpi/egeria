/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActionTargetStatusRequestBody provides a structure for passing the properties that sit in the ActionTarget relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActionTargetStatusRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String                 actionTargetGUID = null;
    private GovernanceActionStatus status = null;
    private Date                   startDate = null;
    private Date                   completionDate = null;


    /**
     * Default constructor
     */
    public ActionTargetStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ActionTargetStatusRequestBody(ActionTargetStatusRequestBody template)
    {
        if (template != null)
        {
            actionTargetGUID = template.getActionTargetGUID();
            status = template.getStatus();
            startDate = template.getStartDate();
            completionDate = template.getCompletionDate();
        }
    }


    /**
     * Return the unique identifier of the action target metadata element.
     *
     * @return string guid
     */
    public String getActionTargetGUID()
    {
        return actionTargetGUID;
    }


    /**
     * Set up the unique identifier of the action target metadata element.
     *
     * @param actionTargetGUID string guid
     */
    public void setActionTargetGUID(String actionTargetGUID)
    {
        this.actionTargetGUID = actionTargetGUID;
    }


    /**
     * Return the status of the work on this actionTarget.
     *
     * @return status enum
     */
    public GovernanceActionStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the status of the work on this actionTarget.
     *
     * @param status status enum
     */
    public void setStatus(GovernanceActionStatus status)
    {
        this.status = status;
    }


    /**
     * Return the date/time that work on this action target started.
     *
     * @return date object
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set up the date/time that work on this action target started.
     *
     * @param startDate date object
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the date/time that work on this actionTarget completed.
     *
     * @return date object
     */
    public Date getCompletionDate()
    {
        return completionDate;
    }


    /**
     * Set up the date/time that work on this actionTarget completed.
     *
     * @param completionDate date object
     */
    public void setCompletionDate(Date completionDate)
    {
        this.completionDate = completionDate;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ActionTargetStatusRequestBody{" +
                       "actionTargetGUID='" + actionTargetGUID + '\'' +
                       ", status=" + status +
                       ", startDate=" + startDate +
                       ", completionDate=" + completionDate +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ActionTargetStatusRequestBody that = (ActionTargetStatusRequestBody) objectToCompare;
        return Objects.equals(actionTargetGUID, that.actionTargetGUID) &&
                       status == that.status &&
                       Objects.equals(startDate, that.startDate) &&
                       Objects.equals(completionDate, that.completionDate);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(actionTargetGUID, status, startDate, completionDate);
    }
}
