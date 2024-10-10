/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.controls.GuardType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.EngineActionStatus;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EngineActionElement contains the properties and header for a EngineAction entity retrieved from the metadata
 * repository that represents the execution of a call to a governance engine. This may be triggered by directly
 * creating the engine action, or using a governance action type as a template or as a step in a governance action
 * process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionProcessStepExecution extends GovernanceActionProcessStepProperties
{
    private List<String>                         mandatoryGuards          = null;
    private List<String>                         receivedGuards           = null;
    private String                               governanceEngineName     = null;
    private String                               governanceActionTypeGUID = null;
    private String                               governanceActionTypeName = null;
    private String                               processName              = null;
    private String                               processStepGUID          = null;
    private String                               processStepName          = null;
    private String                               requesterUserId          = null;
    private String                               requestType              = null;
    private Map<String, String>                  requestParameters        = null;
    private List<RequestSourceElement>           requestSourceElements    = null;
    private List<ActionTargetElement>            actionTargetElements     = null;
    private EngineActionStatus                   actionStatus             = null;
    private Date                                 requestedTime            = null;
    private Date                                 requestedStartTime       = null;
    private Date                                 startTime                = null;
    private String                               processingEngineUserId   = null;
    private Date                                 completionTime           = null;
    private List<String>                         completionGuards         = null;
    private String                               completionMessage        = null;
    private List<RelatedEngineActionElement>     previousActions          = null;
    private List<RelatedEngineActionElement>     followOnActions          = null;


    /**
     * Default constructor
     */
    public GovernanceActionProcessStepExecution()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessStepExecution(GovernanceActionProcessStepExecution template)
    {
        super(template);
        
        if (template != null)
        {
            mandatoryGuards = template.getMandatoryGuards();
            receivedGuards = template.getReceivedGuards();

            governanceEngineName = template.getGovernanceEngineName();

            governanceActionTypeGUID = template.getGovernanceActionTypeGUID();
            governanceActionTypeName = template.getGovernanceActionTypeName();

            processName = template.getProcessName();
            processStepGUID = template.getProcessStepGUID();
            processStepName = template.getProcessStepName();

            requesterUserId = template.getRequesterUserId();
            requestType = template.getRequestType();
            requestParameters = template.getRequestParameters();
            requestSourceElements = template.getRequestSourceElements();
            actionTargetElements = template.getActionTargetElements();

            actionStatus = template.getActionStatus();

            requestedTime = template.getRequestedTime();
            requestedStartTime = template.getRequestedStartTime();
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
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessStepExecution(GovernanceActionProcessStepProperties template)
    {
        super(template);

    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessStepExecution(EngineActionElement template)
    {
        if (template != null)
        {
            super.setEffectiveFrom(template.getEffectiveFrom());
            super.setEffectiveTo(template.getEffectiveTo());
            super.setTypeName(template.getTypeName());
            super.setExtendedProperties(template.getExtendedProperties());
            super.setQualifiedName(template.getQualifiedName());
            super.setAdditionalProperties(template.getAdditionalProperties());
            super.setDomainIdentifier(template.getDomainIdentifier());
            super.setDisplayName(template.getDisplayName());
            super.setDescription(template.getDescription());

            /*
             * Add the received guards to produced guards to simplify the logic of process graph display logic.
             */
            if (template.getReceivedGuards() != null)
            {
                List<GuardType> guardTypes = new ArrayList<>();

                for (String receivedGuard : template.getReceivedGuards())
                {
                    if (receivedGuard != null)
                    {
                        GuardType guardType = new GuardType();

                        guardType.setGuard(receivedGuard);

                        guardTypes.add(guardType);
                    }
                }

                super.setProducedGuards(guardTypes);
            }

            super.setGovernanceEngineGUID(template.getGovernanceEngineGUID());

            mandatoryGuards = template.getMandatoryGuards();
            receivedGuards = template.getReceivedGuards();

            governanceEngineName = template.getGovernanceEngineName();

            governanceActionTypeGUID = template.getGovernanceActionTypeGUID();
            governanceActionTypeName = template.getGovernanceActionTypeName();

            processName = template.getProcessName();
            processStepGUID = template.getProcessStepGUID();
            processStepName = template.getProcessStepName();

            requesterUserId = template.getRequesterUserId();
            requestType = template.getRequestType();
            requestParameters = template.getRequestParameters();
            requestSourceElements = template.getRequestSourceElements();
            actionTargetElements = template.getActionTargetElements();

            actionStatus = template.getActionStatus();

            requestedTime = template.getRequestedTime();
            requestedStartTime = template.getRequestedStartTime();
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
     * Return the list of guards that must be received before this engine action can proceed.
     *
     * @return list of guards
     */
    public List<String> getMandatoryGuards()
    {
        return mandatoryGuards;
    }


    /**
     * Set up the list of guards that must be received before this engine action can proceed.
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
     * Return the unique name of governance engine that is processing the engine action.
     *
     * @return string name
     */
    public String getGovernanceEngineName()
    {
        return governanceEngineName;
    }


    /**
     * Set up the unique name of governance engine that is processing the engine action.
     *
     * @param governanceEngineName string name
     */
    public void setGovernanceEngineName(String governanceEngineName)
    {
        this.governanceEngineName = governanceEngineName;
    }


    /**
     * Return the unique identifier for the governance action type that acted as a template for this engine action (if appropriate).
     *
     * @return guid
     */
    public String getGovernanceActionTypeGUID()
    {
        return governanceActionTypeGUID;
    }


    /**
     * Set up the unique identifier for the governance action type that acted as a template for this engine action (if appropriate).
     *
     * @param governanceActionTypeGUID guid
     */
    public void setGovernanceActionTypeGUID(String governanceActionTypeGUID)
    {
        this.governanceActionTypeGUID = governanceActionTypeGUID;
    }


    /**
     * Return the unique name for the governance action type that acted as a template for this engine action (if appropriate).
     *
     * @return name
     */
    public String getGovernanceActionTypeName()
    {
        return governanceActionTypeName;
    }


    /**
     * Set up the unique name for the governance action type that acted as a template for this engine action (if appropriate).
     *
     * @param governanceActionTypeName name
     */
    public void setGovernanceActionTypeName(String governanceActionTypeName)
    {
        this.governanceActionTypeName = governanceActionTypeName;
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
     * Return the unique identifier for the governance action process step that acted as a template for this engine action (if appropriate).
     *
     * @return guid
     */
    public String getProcessStepGUID()
    {
        return processStepGUID;
    }


    /**
     * Set up the unique identifier for the governance action process step that acted as a template for this engine action (if appropriate).
     *
     * @param processStepGUID guid
     */
    public void setProcessStepGUID(String processStepGUID)
    {
        this.processStepGUID = processStepGUID;
    }


    /**
     * Return the unique name for the governance action process step that acted as a template for this engine action (if appropriate).
     *
     * @return name
     */
    public String getProcessStepName()
    {
        return processStepName;
    }


    /**
     * Set up the unique name for the governance action process step that acted as a template for this engine action (if appropriate).
     *
     * @param processStepName name
     */
    public void setProcessStepName(String processStepName)
    {
        this.processStepName = processStepName;
    }


    /**
     * Return the current status of the engine action.
     *
     * @return status enum
     */
    public EngineActionStatus getActionStatus()
    {
        return actionStatus;
    }


    /**
     * Set up the current status of the engine action.
     *
     * @param actionStatus status enum
     */
    public void setActionStatus(EngineActionStatus actionStatus)
    {
        this.actionStatus = actionStatus;
    }


    /**
     * Return the userId of the governance engine that is responsible for running the governance service for this engine action.
     *
     * @return string userId
     */
    public String getProcessingEngineUserId()
    {
        return processingEngineUserId;
    }


    /**
     * Set up the userId of the governance engine that is responsible for running the governance service for this engine action.
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
     * Return the list of elements that the engine action will work on.
     *
     * @return list of elements
     */
    public List<ActionTargetElement> getActionTargetElements()
    {
        return actionTargetElements;
    }


    /**
     * Set up the list of elements that the engine action will work on.
     *
     * @param actionTargetElements list of elements
     */
    public void setActionTargetElements(List<ActionTargetElement> actionTargetElements)
    {
        this.actionTargetElements = actionTargetElements;
    }


    /**
     * Return the time that the engine action was created.
     *
     * @return date/time
     */
    public Date getRequestedTime()
    {
        return requestedTime;
    }


    /**
     * Set up the time that the engine action was created.
     *
     * @param requestedTime date/time
     */
    public void setRequestedTime(Date requestedTime)
    {
        this.requestedTime = requestedTime;
    }


    /**
     * Return the time that the engine action should start.
     *
     * @return date/time
     */
    public Date getRequestedStartTime()
    {
        return requestedStartTime;
    }

    /**
     * Set up the time that the engine action should start.
     *
     * @param requestedStartTime date/time
     */
    public void setRequestedStartTime(Date requestedStartTime)
    {
        this.requestedStartTime = requestedStartTime;
    }


    /**
     * Return the time that this engine action should start (null means as soon as possible).
     *
     * @return date object
     */
    public Date getStartTime()
    {
        return startTime;
    }


    /**
     * Set up the time that this engine action should start (null means as soon as possible).
     *
     * @param startTime date object
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    /**
     * Return the requesting user
     *
     * @return string
     */
    public String getRequesterUserId()
    {
        return requesterUserId;
    }


    /**
     * Set up the requesting user.
     *
     * @param requesterUserId string
     */
    public void setRequesterUserId(String requesterUserId)
    {
        this.requesterUserId = requesterUserId;
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
     * Return the list of engine actions that preceded this engine action.
     *
     * @return list of element stubs
     */
    public List<RelatedEngineActionElement> getPreviousActions()
    {
        return previousActions;
    }


    /**
     * Set up the list of engine actions that preceded this engine action.
     *
     * @param previousActions list of element stubs
     */
    public void setPreviousActions(List<RelatedEngineActionElement> previousActions)
    {
        this.previousActions = previousActions;
    }


    /**
     * Return the list of engine actions that will run after this engine action has completed.
     *
     * @return list of element stubs
     */
    public List<RelatedEngineActionElement> getFollowOnActions()
    {
        return followOnActions;
    }


    /**
     * Set up the list of engine actions that will run after this engine action has completed.
     *
     * @param followOnActions list of element stubs
     */
    public void setFollowOnActions(List<RelatedEngineActionElement> followOnActions)
    {
        this.followOnActions = followOnActions;
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        GovernanceActionProcessStepExecution that = (GovernanceActionProcessStepExecution) objectToCompare;
        return Objects.equals(mandatoryGuards, that.mandatoryGuards) &&
                Objects.equals(receivedGuards, that.receivedGuards) &&
                Objects.equals(governanceEngineName, that.governanceEngineName) &&
                Objects.equals(governanceActionTypeGUID, that.governanceActionTypeGUID) &&
                Objects.equals(governanceActionTypeName, that.governanceActionTypeName) &&
                Objects.equals(processName, that.processName) &&
                Objects.equals(processStepGUID, that.processStepGUID) &&
                Objects.equals(processStepName, that.processStepName) &&
                Objects.equals(requesterUserId, that.requesterUserId) &&
                Objects.equals(requestType, that.requestType) &&
                Objects.equals(requestParameters, that.requestParameters) &&
                Objects.equals(requestSourceElements, that.requestSourceElements) &&
                Objects.equals(actionTargetElements, that.actionTargetElements) &&
                actionStatus == that.actionStatus &&
                Objects.equals(requestedTime, that.requestedTime) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(processingEngineUserId, that.processingEngineUserId) &&
                Objects.equals(completionTime, that.completionTime) &&
                Objects.equals(completionGuards, that.completionGuards) &&
                Objects.equals(completionMessage, that.completionMessage) &&
                Objects.equals(previousActions, that.previousActions) &&
                Objects.equals(followOnActions, that.followOnActions);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mandatoryGuards, receivedGuards, governanceEngineName,
                            governanceActionTypeGUID, governanceActionTypeName, processName, processStepGUID,
                            processStepName, requesterUserId, requestType, requestParameters, requestSourceElements,
                            actionTargetElements, actionStatus, requestedTime, startTime, processingEngineUserId,
                            completionTime, completionGuards, completionMessage, previousActions, followOnActions);
    }
}
