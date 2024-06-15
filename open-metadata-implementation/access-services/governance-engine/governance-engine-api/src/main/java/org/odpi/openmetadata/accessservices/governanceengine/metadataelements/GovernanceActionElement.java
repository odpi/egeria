/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceengine.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedEngineActionElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionTypeElement contains the properties and header for a GovernanceAction entity retrieved from the metadata
 * repository that represents a governance action type (plus relevant relationships and properties).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionElement extends ReferenceableProperties implements MetadataElement
{
    private ElementHeader                        elementHeader            = null;
    private int                                  domainIdentifier         = 0;
    private String                               displayName              = null;
    private String                               description              = null;
    private List<String>                         mandatoryGuards          = null;
    private List<String>                         receivedGuards           = null;
    private String                               governanceEngineGUID     = null;
    private String                               governanceEngineName     = null;
    private String                               processName              = null;
    private String                               governanceActionTypeGUID = null;
    private String                               governanceActionTypeName = null;
    private String                               requestType              = null;
    private Map<String, String>                  requestParameters        = null;
    private List<RequestSourceElement>           requestSourceElements    = null;
    private List<ActionTargetElement>            actionTargetElements     = null;
    @SuppressWarnings(value = "deprecation")
    private GovernanceActionStatus               actionStatus             = null;
    private Date                                 requestedTime            = null;
    private Date                                 startTime                = null;
    private String                               processingEngineUserId   = null;
    private Date                                 completionTime           = null;
    private List<String>                         completionGuards         = null;
    private String                           completionMessage = null;
    private List<RelatedEngineActionElement> previousActions   = null;
    private List<RelatedEngineActionElement> followOnActions   = null;


    /**
     * Default constructor
     */
    public GovernanceActionElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionElement(GovernanceActionElement template)
    {
        super(template);
        
        if (template != null)
        {
            elementHeader = template.getElementHeader();

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
            requestSourceElements = template.getRequestSourceElements();
            actionTargetElements = template.getActionTargetElements();

            actionStatus = template.getActionStatus();

            requestedTime = template.getRequestedTime();
            startTime = template.getStartTime();
            processingEngineUserId = template.getProcessingEngineUserId();

            completionTime = template.getCompletionTime();
            completionGuards = template.getCompletionGuards();
            completionMessage = template.getCompletionMessage();

            previousActions = template.getPreviousActions();
            followOnActions = template.getFollowOnActions();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
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
    @SuppressWarnings(value = "deprecation")
    public GovernanceActionStatus getActionStatus()
    {
        return actionStatus;
    }


    /**
     * Set up the current status of the governance action.
     *
     * @param actionStatus status enum
     */
    @SuppressWarnings(value = "deprecation")
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
     * @return list of elements
     */
    public List<RequestSourceElement> getRequestSourceElements()
    {
        if (requestSourceElements == null)
        {
            return null;
        }

        if (requestSourceElements.isEmpty())
        {
            return null;
        }

        return requestSourceElements;
    }


    /**
     * Set up the list of elements that triggered this request.
     *
     * @param requestSourceElements list of elements
     */
    public void setRequestSourceElements(List<RequestSourceElement> requestSourceElements)
    {
        this.requestSourceElements = requestSourceElements;
    }


    /**
     * Return the list of elements that the governance action will work on.
     *
     * @return list of elements
     */
    public List<ActionTargetElement> getActionTargetElements()
    {
        if (actionTargetElements == null)
        {
            return null;
        }

        if (actionTargetElements.isEmpty())
        {
            return null;
        }

        return actionTargetElements;
    }


    /**
     * Set up the list of elements that the governance action will work on.
     *
     * @param actionTargetElements list of elements
     */
    public void setActionTargetElements(List<ActionTargetElement> actionTargetElements)
    {
        this.actionTargetElements = actionTargetElements;
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
     * Return the governance request type used to call the governance service via this governance engine.
     *
     * @return name of the request type
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Set up the governance request type used to call the governance service via this governance engine.
     *
     * @param requestType name of the request type passed to the governance service
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
     * Return the list of governance actions that preceded this governance action.
     *
     * @return list of element stubs
     */
    public List<RelatedEngineActionElement> getPreviousActions()
    {
        return previousActions;
    }


    /**
     * Set up the list of governance actions that preceded this governance action.
     *
     * @param previousActions list of element stubs
     */
    public void setPreviousActions(List<RelatedEngineActionElement> previousActions)
    {
        this.previousActions = previousActions;
    }


    /**
     * Return the list of governance actions that will run after this governance action has completed.
     *
     * @return list of element stubs
     */
    public List<RelatedEngineActionElement> getFollowOnActions()
    {
        return followOnActions;
    }


    /**
     * Set up the list of governance actions that will run after this governance action has completed.
     *
     * @param followOnActions list of element stubs
     */
    public void setFollowOnActions(List<RelatedEngineActionElement> followOnActions)
    {
        this.followOnActions = followOnActions;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceActionElement{" +
                       "elementHeader=" + elementHeader +
                       ", domainIdentifier=" + domainIdentifier +
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
                       ", requestSourceElements=" + requestSourceElements +
                       ", actionTargetElements=" + actionTargetElements +
                       ", actionStatus=" + actionStatus +
                       ", requestedTime=" + requestedTime +
                       ", startTime=" + startTime +
                       ", processingEngineUserId='" + processingEngineUserId + '\'' +
                       ", completionTime=" + completionTime +
                       ", completionGuards=" + completionGuards +
                       ", completionMessage='" + completionMessage + '\'' +
                       ", previousActions=" + previousActions +
                       ", followOnActions=" + followOnActions +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof GovernanceActionElement))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        GovernanceActionElement that = (GovernanceActionElement) objectToCompare;

        if (domainIdentifier != that.domainIdentifier)
        {
            return false;
        }
        if (elementHeader != null ? ! elementHeader.equals(that.elementHeader) : that.elementHeader != null)
        {
            return false;
        }
        if (displayName != null ? ! displayName.equals(that.displayName) : that.displayName != null)
        {
            return false;
        }
        if (description != null ? ! description.equals(that.description) : that.description != null)
        {
            return false;
        }
        if (mandatoryGuards != null ? ! mandatoryGuards.equals(that.mandatoryGuards) : that.mandatoryGuards != null)
        {
            return false;
        }
        if (receivedGuards != null ? ! receivedGuards.equals(that.receivedGuards) : that.receivedGuards != null)
        {
            return false;
        }
        if (governanceEngineGUID != null ? ! governanceEngineGUID.equals(that.governanceEngineGUID) : that.governanceEngineGUID != null)
        {
            return false;
        }
        if (governanceEngineName != null ? ! governanceEngineName.equals(that.governanceEngineName) : that.governanceEngineName != null)
        {
            return false;
        }
        if (processName != null ? ! processName.equals(that.processName) : that.processName != null)
        {
            return false;
        }
        if (governanceActionTypeGUID != null ? ! governanceActionTypeGUID.equals(
                that.governanceActionTypeGUID) : that.governanceActionTypeGUID != null)
        {
            return false;
        }
        if (governanceActionTypeName != null ? ! governanceActionTypeName.equals(
                that.governanceActionTypeName) : that.governanceActionTypeName != null)
        {
            return false;
        }
        if (requestType != null ? ! requestType.equals(that.requestType) : that.requestType != null)
        {
            return false;
        }
        if (requestParameters != null ? ! requestParameters.equals(that.requestParameters) : that.requestParameters != null)
        {
            return false;
        }
        if (requestSourceElements != null ? ! requestSourceElements.equals(that.requestSourceElements) : that.requestSourceElements != null)
        {
            return false;
        }
        if (actionTargetElements != null ? ! actionTargetElements.equals(that.actionTargetElements) : that.actionTargetElements != null)
        {
            return false;
        }
        if (actionStatus != that.actionStatus)
        {
            return false;
        }
        if (requestedTime != null ? ! requestedTime.equals(that.requestedTime) : that.requestedTime != null)
        {
            return false;
        }
        if (startTime != null ? ! startTime.equals(that.startTime) : that.startTime != null)
        {
            return false;
        }
        if (processingEngineUserId != null ? ! processingEngineUserId.equals(that.processingEngineUserId) : that.processingEngineUserId != null)
        {
            return false;
        }
        if (completionTime != null ? ! completionTime.equals(that.completionTime) : that.completionTime != null)
        {
            return false;
        }
        if (completionGuards != null ? ! completionGuards.equals(that.completionGuards) : that.completionGuards != null)
        {
            return false;
        }
        if (completionMessage != null ? ! completionMessage.equals(that.completionMessage) : that.completionMessage != null)
        {
            return false;
        }
        if (previousActions != null ? ! previousActions.equals(that.previousActions) : that.previousActions != null)
        {
            return false;
        }
        return followOnActions != null ? followOnActions.equals(that.followOnActions) : that.followOnActions == null;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (elementHeader != null ? elementHeader.hashCode() : 0);
        result = 31 * result + domainIdentifier;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (mandatoryGuards != null ? mandatoryGuards.hashCode() : 0);
        result = 31 * result + (receivedGuards != null ? receivedGuards.hashCode() : 0);
        result = 31 * result + (governanceEngineGUID != null ? governanceEngineGUID.hashCode() : 0);
        result = 31 * result + (governanceEngineName != null ? governanceEngineName.hashCode() : 0);
        result = 31 * result + (processName != null ? processName.hashCode() : 0);
        result = 31 * result + (governanceActionTypeGUID != null ? governanceActionTypeGUID.hashCode() : 0);
        result = 31 * result + (governanceActionTypeName != null ? governanceActionTypeName.hashCode() : 0);
        result = 31 * result + (requestType != null ? requestType.hashCode() : 0);
        result = 31 * result + (requestParameters != null ? requestParameters.hashCode() : 0);
        result = 31 * result + (requestSourceElements != null ? requestSourceElements.hashCode() : 0);
        result = 31 * result + (actionTargetElements != null ? actionTargetElements.hashCode() : 0);
        result = 31 * result + (actionStatus != null ? actionStatus.hashCode() : 0);
        result = 31 * result + (requestedTime != null ? requestedTime.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (processingEngineUserId != null ? processingEngineUserId.hashCode() : 0);
        result = 31 * result + (completionTime != null ? completionTime.hashCode() : 0);
        result = 31 * result + (completionGuards != null ? completionGuards.hashCode() : 0);
        result = 31 * result + (completionMessage != null ? completionMessage.hashCode() : 0);
        result = 31 * result + (previousActions != null ? previousActions.hashCode() : 0);
        result = 31 * result + (followOnActions != null ? followOnActions.hashCode() : 0);
        return result;
    }
}
