/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.EngineActionStatus;

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
    private String             actionTargetRelationshipGUID = null;
    private EngineActionStatus status                       = null;
    private Date               startDate                    = null;
    private Date               completionDate               = null;
    private String             completionMessage            = null;

    private OpenMetadataElement targetElement = null;


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
            actionTargetRelationshipGUID = template.getActionTargetRelationshipGUID();
            status = template.getStatus();
            startDate = template.getStartDate();
            completionDate = template.getCompletionDate();
            completionMessage = template.getCompletionMessage();
            targetElement = template.getTargetElement();
        }
    }


    /**
     * Return the unique identifier of the ActionTarget relationship.
     *
     * @return guid
     */
    public String getActionTargetRelationshipGUID()
    {
        return actionTargetRelationshipGUID;
    }


    /**
     * Set up the unique identifier of the ActionTarget relationship.
     *
     * @param actionTargetRelationshipGUID guid
     */
    public void setActionTargetRelationshipGUID(String actionTargetRelationshipGUID)
    {
        this.actionTargetRelationshipGUID = actionTargetRelationshipGUID;
    }

    /**
     * Return the current status of the action target.  The default value is the status is derived from
     * the engine action.  However, if it has to process many target elements, then these values can
     * be used to show progress.
     *
     * @return status enum
     */
    public EngineActionStatus getStatus()
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
    public void setStatus(EngineActionStatus status)
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
                       ", relationshipGUID=" + actionTargetRelationshipGUID +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ActionTargetElement that = (ActionTargetElement) objectToCompare;
        return Objects.equals(actionTargetRelationshipGUID, that.actionTargetRelationshipGUID) &&
                status == that.status &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(completionDate, that.completionDate) &&
                Objects.equals(completionMessage, that.completionMessage) &&
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
        return Objects.hash(super.hashCode(), actionTargetRelationshipGUID, status, startDate, completionDate, completionMessage, targetElement);
    }
}
