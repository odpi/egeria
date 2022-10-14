/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GovernanceActionStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

/**
 * ActionTargetElement describes an element that a governance action service should process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ActionTargetElement implements Serializable
{
    private static final long      serialVersionUID = 1L;

    private String                 actionTargetName = null;

    private String                 actionTargetGUID = null;
    private GovernanceActionStatus status           = null;
    private Date                   startDate        = null;
    private Date                   completionDate   = null;

    private ElementStub            targetElement = null;


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
        if (template != null)
        {
            actionTargetName = template.getActionTargetName();
            actionTargetGUID = template.getActionTargetGUID();
            status = template.getStatus();
            startDate = template.getStartDate();
            completionDate = template.getCompletionDate();
            targetElement = template.getTargetElement();
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
     * Return the unique identifier for this action target.
     *
     * @return string identifier
     */
    public String getActionTargetGUID()
    {
        return actionTargetGUID;
    }


    /**
     * Set up the unique identifier for this action target.
     *
     * @param actionTargetGUID string identifier
     */
    public void setActionTargetGUID(String actionTargetGUID)
    {
        this.actionTargetGUID = actionTargetGUID;
    }


    /**
     * Return the current status of the action target.  The default value is the status is derived from
     * the governance action service.  However if it has to process many target elements, then these values can
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
     * the governance action service.  However if it has to process many target elements, then these values can
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
     * this value is derived from the startDate for the governance action service.  However if it has to process many target elements, then these values can
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
     * this value is derived from the startDate for the governance action service.  However if it has to process many target
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
     * this value is derived from the completionDate for the governance action service.  However if it has to process
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
     * this value is derived from the completionDate for the governance action service.  However if it has to process
     * many target elements, then these values can be used to show progress.
     *
     * @param completionDate date object
     */
    public void setCompletionDate(Date completionDate)
    {
        this.completionDate = completionDate;
    }


    /**
     * Return details of the target element that the governance action service is to process.
     *
     * @return metadata element properties
     */
    public ElementStub getTargetElement()
    {
        return targetElement;
    }


    /**
     * Set up details of the target element that the governance action service is to process.
     *
     * @param targetElement metadata element properties
     */
    public void setTargetElement(ElementStub targetElement)
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
                       "actionTargetName='" + actionTargetName + '\'' +
                       ", actionTargetGUID='" + actionTargetGUID + '\'' +
                       ", status=" + status +
                       ", startDate=" + startDate +
                       ", completionDate=" + completionDate +
                       ", targetElement=" + targetElement +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ActionTargetElement
                that = (ActionTargetElement) objectToCompare;
        return Objects.equals(actionTargetName, that.actionTargetName) &&
                       Objects.equals(actionTargetGUID, that.actionTargetGUID) &&
                       status == that.status &&
                       Objects.equals(startDate, that.startDate) &&
                       Objects.equals(completionDate, that.completionDate) &&
                       Objects.equals(targetElement, that.targetElement);
    }



    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(actionTargetName, actionTargetGUID, status, startDate, completionDate, targetElement);
    }
}
