/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ActionTargetElement describes an element that a governance action service should process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActionTargetElement extends NewActionTarget
{
    private static final long      serialVersionUID  = 1L;

    private GovernanceActionStatus status            = null;
    private Date                   startDate         = null;
    private Date                   completionDate    = null;
    private String                 completionMessage = null;

    private OpenMetadataElement    targetElement     = null;


    /**
     * Typical Constructor
     */
    public ActionTargetElement()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public ActionTargetElement(ActionTargetElement template)
    {
        super(template);

        if (template != null)
        {
            status = template.getStatus();
            startDate = template.getStartDate();
            completionDate = template.getCompletionDate();
            completionMessage = template.getCompletionMessage();
            targetElement = template.getTargetElement();
        }
    }


    /**
     * Return the current status of the action target.  The default value is the status is derived from
     * the governance action service.  However, if it has to process many target elements, then these values can
     * be used to show progress.
     *
     * @return status enum
     */
    public GovernanceActionStatus getStatus()
    {
        return status;
    }


    /**
     * Set up current status of the action target.  The default value is the status is derived from
     * the governance action service.  However, if it has to process many target elements, then these values can
     * be used to show progress.
     *
     * @param status enum
     */
    public void setStatus(GovernanceActionStatus status)
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
     * Return details of the target element that the governance action service is to process.
     *
     * @return metadata element properties
     */
    public OpenMetadataElement getTargetElement()
    {
        return targetElement;
    }


    /**
     * Set up details of the target element that the governance action service is to process.
     *
     * @param targetElement metadata element properties
     */
    public void setTargetElement(OpenMetadataElement targetElement)
    {
        this.targetElement = targetElement;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ActionTargetElement{" +
                       "status=" + status +
                       ", startDate=" + startDate +
                       ", completionDate=" + completionDate +
                       ", completionMessage='" + completionMessage + '\'' +
                       ", targetElement=" + targetElement +
                       ", actionTargetName='" + getActionTargetName() + '\'' +
                       ", actionTargetGUID='" + getActionTargetGUID() + '\'' +
                       '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof ActionTargetElement))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        ActionTargetElement that = (ActionTargetElement) objectToCompare;

        if (status != that.status)
        {
            return false;
        }
        if (startDate != null ? ! startDate.equals(that.startDate) : that.startDate != null)
        {
            return false;
        }
        if (completionDate != null ? ! completionDate.equals(that.completionDate) : that.completionDate != null)
        {
            return false;
        }
        if (completionMessage != null ? ! completionMessage.equals(that.completionMessage) : that.completionMessage != null)
        {
            return false;
        }
        return targetElement != null ? targetElement.equals(that.targetElement) : that.targetElement == null;
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (completionDate != null ? completionDate.hashCode() : 0);
        result = 31 * result + (completionMessage != null ? completionMessage.hashCode() : 0);
        result = 31 * result + (targetElement != null ? targetElement.hashCode() : 0);
        return result;
    }
}
