/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionProperties provides a structure for carrying the properties for a governance action.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionProperties implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String                 qualifiedName          = null;
    private int                    domainIdentifier       = 0;
    private String                 displayName            = null;
    private String                 description            = null;

    private List<String>           receivedGuards         = null;

    private String                 governanceEngineGUID   = null;
    private String                 requestType            = null;
    private Map<String, String>    requestProperties      = null;
    private List<String>           requestSourceGUIDs     = null;
    private List<String>           actionTargetGUIDs      = null;

    private GovernanceActionStatus actionStatus           = null;

    private Date                   startTime              = null;
    private String                 processingEngineUserId = null;

    private Date                   completionTime         = null;
    private List<String>           completionGuards       = null;

    private Map<String, String>    additionalProperties   = null;


    /**
     * Default constructor
     */
    public GovernanceActionProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProperties(GovernanceActionProperties template)
    {
        if (template != null)
        {
            qualifiedName = template.getQualifiedName();
            domainIdentifier = template.getDomainIdentifier();
            displayName = template.getDisplayName();
            description = template.getDescription();

            receivedGuards = template.getReceivedGuards();

            governanceEngineGUID = template.getGovernanceEngineGUID();
            requestType = template.getRequestType();
            requestProperties = template.getRequestProperties();
            requestSourceGUIDs = template.getRequestSourceGUIDs();
            actionTargetGUIDs = template.getActionTargetGUIDs();

            actionStatus = template.getActionStatus();

            startTime = template.getStartTime();
            processingEngineUserId = template.getProcessingEngineUserId();

            completionTime = template.getCompletionTime();
            completionGuards = template.getCompletionGuards();

            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the qualified name for this new governance action.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the qualified name of this new governance action.
     *
     * @param qualifiedName string name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return the identifier of the governance domain that this action belongs to (0=ALL/ANY).
     *
     * @return int
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this action belongs to (0=ALL/ANY).
     *
     * @param domainIdentifier int
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Return the display name for the governance action.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for the governance action.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the governance action.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the governance action.
     *
     * @param description string text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the list of guards provided by the previous governance service.
     *
     * @return list of guards
     */
    public List<String> getReceivedGuards()
    {
        return receivedGuards;
    }


    /**
     * Set up the list of guards provided by the previous governance service.
     *
     * @param receivedGuards list of guards
     */
    public void setReceivedGuards(List<String> receivedGuards)
    {
        this.receivedGuards = receivedGuards;
    }


    /**
     * Return the unique identifier of governance engine that is processing the governance action.
     *
     * @return string guid
     */
    public String getGovernanceEngineGUID()
    {
        return governanceEngineGUID;
    }


    /**
     * Set up the unique identifier of governance engine that is processing the governance action.
     *
     * @param governanceEngineGUID string guid
     */
    public void setGovernanceEngineGUID(String governanceEngineGUID)
    {
        this.governanceEngineGUID = governanceEngineGUID;
    }


    /**
     * Return the current status of the governance action.
     *
     * @return status enum
     */
    public GovernanceActionStatus getActionStatus()
    {
        return actionStatus;
    }


    /**
     * Set up the current status of the governance action.
     *
     * @param actionStatus status enum
     */
    public void setActionStatus(GovernanceActionStatus actionStatus)
    {
        this.actionStatus = actionStatus;
    }


    /**
     * Return the userId of the governance engine that is responsible for running the governance service for this governance action.
     *
     * @return string userId
     */
    public String getProcessingEngineUserId()
    {
        return processingEngineUserId;
    }


    /**
     * Set up the userId of the governance engine that is responsible for running the governance service for this governance action.
     *
     * @param processingEngineUserId string userId
     */
    public void setProcessingEngineUserId(String processingEngineUserId)
    {
        this.processingEngineUserId = processingEngineUserId;
    }


    /**
     * Return the list of elements that triggered this request.
     *
     * @return list of string guids
     */
    public List<String> getRequestSourceGUIDs()
    {
        if (requestSourceGUIDs == null)
        {
            return null;
        }

        if (requestSourceGUIDs.isEmpty())
        {
            return null;
        }

        return requestSourceGUIDs;
    }


    /**
     * Set up the list of elements that triggered this request.
     *
     * @param requestSourceGUIDs list of string guids
     */
    public void setRequestSourceGUIDs(List<String> requestSourceGUIDs)
    {
        this.requestSourceGUIDs = requestSourceGUIDs;
    }


    /**
     * Return the list of elements that the governance action will work on.
     *
     * @return list of string guids
     */
    public List<String> getActionTargetGUIDs()
    {
        if (actionTargetGUIDs == null)
        {
            return null;
        }

        if (actionTargetGUIDs.isEmpty())
        {
            return null;
        }

        return actionTargetGUIDs;
    }


    /**
     * Set up the list of elements that the governance action will work on.
     *
     * @param actionTargetGUIDs list of string guids
     */
    public void setActionTargetGUIDs(List<String> actionTargetGUIDs)
    {
        this.actionTargetGUIDs = actionTargetGUIDs;
    }


    /**
     * Return the time that this governance action should start (null means as soon as possible).
     *
     * @return date object
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the time that this governance action should start (null means as soon as possible).
     *
     * @param startTime date object
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    /**
     * Return the request type associated with this governance action.
     *
     * @return string name
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Set up the request type associated with this governance action, used to identify ths governance service to run.
     *
     * @param requestType string name
     */
    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }


    /**
     * Return the parameters to pass onto the governance service.
     *
     * @return map of properties
     */
    public Map<String, String> getRequestProperties()
    {
        if (requestProperties == null)
        {
            return null;
        }

        if (requestProperties.isEmpty())
        {
            return null;
        }

        return requestProperties;
    }


    /**
     * Set up the parameters to pass onto the governance service.
     *
     * @param requestProperties map of properties
     */
    public void setRequestProperties(Map<String, String> requestProperties)
    {
        this.requestProperties = requestProperties;
    }


    /**
     * Return the date/time that the governance service completed (or null if not yet completed).
     *
     * @return date object
     */
    public Date getCompletionTime()
    {
        return completionTime;
    }


    /**
     * Set up the date/time that the governance service completed (or null if not yet completed).
     *
     * @param completionTime date object
     */
    public void setCompletionTime(Date completionTime)
    {
        this.completionTime = completionTime;
    }


    /**
     * Return the list of completion guards supplied by the governance service.
     *
     * @return list of guards
     */
    public List<String> getCompletionGuards()
    {
        if (completionGuards == null)
        {
            return null;
        }

        if (completionGuards.isEmpty())
        {
            return null;
        }

        return completionGuards;
    }


    /**
     * Set up the list of completion guards supplied by the governance service.
     *
     * @param completionGuards list of guards
     */
    public void setCompletionGuards(List<String> completionGuards)
    {
        this.completionGuards = completionGuards;
    }


    /**
     * Return any additional properties.
     *
     * @return map of properties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }

        if (additionalProperties.isEmpty())
        {
            return null;
        }

        return additionalProperties;
    }


    /**
     * Set up any additional properties.
     *
     * @param additionalProperties map of properties
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceActionProperties{" +
                       "qualifiedName='" + qualifiedName + '\'' +
                       ", domainIdentifier=" + domainIdentifier +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", receivedGuards=" + receivedGuards +
                       ", governanceEngineGUID='" + governanceEngineGUID + '\'' +
                       ", requestType='" + requestType + '\'' +
                       ", requestProperties=" + requestProperties +
                       ", requestSourceGUIDs=" + requestSourceGUIDs +
                       ", actionTargetGUIDs=" + actionTargetGUIDs +
                       ", actionStatus=" + actionStatus +
                       ", startTime=" + startTime +
                       ", processingEngineUserId='" + processingEngineUserId + '\'' +
                       ", completionTime=" + completionTime +
                       ", completionGuards=" + completionGuards +
                       ", additionalProperties=" + additionalProperties +
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
        GovernanceActionProperties that = (GovernanceActionProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(qualifiedName, that.qualifiedName) &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(receivedGuards, that.receivedGuards) &&
                       Objects.equals(governanceEngineGUID, that.governanceEngineGUID) &&
                       Objects.equals(requestType, that.requestType) &&
                       Objects.equals(requestProperties, that.requestProperties) &&
                       Objects.equals(requestSourceGUIDs, that.requestSourceGUIDs) &&
                       Objects.equals(actionTargetGUIDs, that.actionTargetGUIDs) &&
                       actionStatus == that.actionStatus &&
                       Objects.equals(startTime, that.startTime) &&
                       Objects.equals(processingEngineUserId, that.processingEngineUserId) &&
                       Objects.equals(completionTime, that.completionTime) &&
                       Objects.equals(completionGuards, that.completionGuards) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(qualifiedName, domainIdentifier, displayName, description, receivedGuards, governanceEngineGUID, requestType,
                            requestProperties, requestSourceGUIDs, actionTargetGUIDs, actionStatus, startTime, processingEngineUserId, completionTime,
                            completionGuards, additionalProperties);
    }
}
