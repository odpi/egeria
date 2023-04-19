/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
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
public class GovernanceActionProperties extends ReferenceableProperties
{
    @Serial
    private static final long serialVersionUID = 1L;

    private int                    domainIdentifier         = 0;
    private String                 displayName              = null;
    private String                 description              = null;

    private List<String>           mandatoryGuards          = null;
    private List<String>           receivedGuards           = null;

    private String                 governanceEngineGUID     = null;
    private String                 governanceEngineName     = null;
    private String                 processName              = null;
    private String                 governanceActionTypeGUID = null;
    private String                 governanceActionTypeName = null;
    private String                 requestType              = null;
    private Map<String, String>    requestParameters        = null;


    private GovernanceActionStatus actionStatus             = null;

    private Date                   requestedTime            = null;
    private Date                   startTime                = null;
    private String                 processingEngineUserId   = null;

    private Date                   completionTime           = null;
    private List<String>           completionGuards         = null;

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
        super(template);

        if (template != null)
        {
            domainIdentifier = template.getDomainIdentifier();
            displayName = template.getDisplayName();
            description = template.getDescription();

            mandatoryGuards = template.getMandatoryGuards();
            receivedGuards = template.getReceivedGuards();

            governanceEngineGUID = template.getGovernanceEngineGUID();
            governanceEngineName = template.getGovernanceEngineName();
            processName = template.getProcessName();
            governanceActionTypeGUID = template.getGovernanceActionTypeGUID();
            governanceActionTypeName = template.getGovernanceActionTypeName();
            requestType = template.getRequestType();
            requestParameters = template.getRequestParameters();

            actionStatus = template.getActionStatus();

            requestedTime = template.getRequestedTime();
            startTime = template.getStartTime();
            processingEngineUserId = template.getProcessingEngineUserId();

            completionTime = template.getCompletionTime();
            completionGuards = template.getCompletionGuards();
        }
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
     * Return the list of guards that must be received before this governance action can proceed.
     *
     * @return list of guards
     */
    public List<String> getMandatoryGuards()
    {
        if (mandatoryGuards == null)
        {
            return null;
        }
        else if (mandatoryGuards.isEmpty())
        {
            return null;
        }
        return mandatoryGuards;
    }


    /**
     * Set up the list of guards that must be received before this governance action can proceed.
     *
     * @param mandatoryGuards list of guards
     */
    public void setMandatoryGuards(List<String> mandatoryGuards)
    {
        this.mandatoryGuards = mandatoryGuards;
    }


    /**
     * Return the list of guards provided by the previous governance service(s).
     *
     * @return list of guards
     */
    public List<String> getReceivedGuards()
    {
        if (receivedGuards == null)
        {
            return null;
        }
        else if (receivedGuards.isEmpty())
        {
            return null;
        }
        return receivedGuards;
    }


    /**
     * Set up the list of guards provided by the previous governance service(s).
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
     * Return the unique name of governance engine that is processing the governance action.
     *
     * @return string name
     */
    public String getGovernanceEngineName()
    {
        return governanceEngineName;
    }


    /**
     * Set up the unique name of governance engine that is processing the governance action.
     *
     * @param governanceEngineName string name
     */
    public void setGovernanceEngineName(String governanceEngineName)
    {
        this.governanceEngineName = governanceEngineName;
    }


    /**
     * Return the name of the governance action process - or similar request source.
     *
     * @return name
     */
    public String getProcessName()
    {
        return processName;
    }


    /**
     * Set up the name of the governance action process - or similar request source.
     *
     * @param processName name
     */
    public void setProcessName(String processName)
    {
        this.processName = processName;
    }


    /**
     * Return the unique identifier for the governance action type that acted as a template for this governance action (if appropriate).
     *
     * @return guid
     */
    public String getGovernanceActionTypeGUID()
    {
        return governanceActionTypeGUID;
    }


    /**
     * Set up the unique identifier for the governance action type that acted as a template for this governance action (if appropriate).
     *
     * @param governanceActionTypeGUID guid
     */
    public void setGovernanceActionTypeGUID(String governanceActionTypeGUID)
    {
        this.governanceActionTypeGUID = governanceActionTypeGUID;
    }


    /**
     * Return the unique name for the governance action type that acted as a template for this governance action (if appropriate).
     *
     * @return name
     */
    public String getGovernanceActionTypeName()
    {
        return governanceActionTypeName;
    }


    /**
     * Set up the unique name for the governance action type that acted as a template for this governance action (if appropriate).
     *
     * @param governanceActionTypeName name
     */
    public void setGovernanceActionTypeName(String governanceActionTypeName)
    {
        this.governanceActionTypeName = governanceActionTypeName;
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
     * Return the time that the governance action was created.
     *
     * @return date/time
     */
    public Date getRequestedTime()
    {
        return requestedTime;
    }


    /**
     * Set up the time that the governance action was created.
     *
     * @param requestedTime date/time
     */
    public void setRequestedTime(Date requestedTime)
    {
        this.requestedTime = requestedTime;
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
    public Map<String, String> getRequestParameters()
    {
        if (requestParameters == null)
        {
            return null;
        }

        if (requestParameters.isEmpty())
        {
            return null;
        }

        return requestParameters;
    }


    /**
     * Set up the parameters to pass onto the governance service.
     *
     * @param requestParameters map of properties
     */
    public void setRequestParameters(Map<String, String> requestParameters)
    {
        this.requestParameters = requestParameters;
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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceActionProperties{" +
                       "domainIdentifier=" + domainIdentifier +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", mandatoryGuards=" + mandatoryGuards +
                       ", receivedGuards=" + receivedGuards +
                       ", governanceEngineGUID='" + governanceEngineGUID + '\'' +
                       ", governanceEngineName='" + governanceEngineName + '\'' +
                       ", processName='" + processName + '\'' +
                       ", governanceActionTypeGUID='" + governanceActionTypeGUID + '\'' +
                       ", governanceActionTypeName='" + governanceActionTypeName + '\'' +
                       ", requestType='" + requestType + '\'' +
                       ", requestParameters=" + requestParameters +
                       ", actionStatus=" + actionStatus +
                       ", requestedTime=" + requestedTime +
                       ", startTime=" + startTime +
                       ", processingEngineUserId='" + processingEngineUserId + '\'' +
                       ", completionTime=" + completionTime +
                       ", completionGuards=" + completionGuards +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceActionProperties that = (GovernanceActionProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(mandatoryGuards, that.mandatoryGuards) &&
                       Objects.equals(receivedGuards, that.receivedGuards) &&
                       Objects.equals(governanceEngineGUID, that.governanceEngineGUID) &&
                       Objects.equals(governanceEngineName, that.governanceEngineName) &&
                       Objects.equals(processName, that.processName) &&
                       Objects.equals(governanceActionTypeGUID, that.governanceActionTypeGUID) &&
                       Objects.equals(governanceActionTypeName, that.governanceActionTypeName) &&
                       Objects.equals(requestType, that.requestType) &&
                       Objects.equals(requestParameters, that.requestParameters) &&
                       actionStatus == that.actionStatus &&
                       Objects.equals(requestedTime, that.requestedTime) &&
                       Objects.equals(startTime, that.startTime) &&
                       Objects.equals(processingEngineUserId, that.processingEngineUserId) &&
                       Objects.equals(completionTime, that.completionTime) &&
                       Objects.equals(completionGuards, that.completionGuards);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), domainIdentifier, displayName, description, mandatoryGuards, receivedGuards, governanceEngineGUID,
                            governanceEngineName, processName, governanceActionTypeGUID, governanceActionTypeName, requestType, requestParameters,
                            actionStatus, requestedTime, startTime, processingEngineUserId, completionTime, completionGuards);
    }
}
