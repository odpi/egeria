/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
public class EngineActionProperties extends ActionProperties
{
    private int                 domainIdentifier         = 0;
    private List<String>        mandatoryGuards          = null;
    private List<String>        receivedGuards           = null;
    private String              executorEngineGUID       = null;
    private String              executorEngineName       = null;
    private String              governanceActionTypeGUID = null;
    private String              governanceActionTypeName = null;
    private String              processName              = null;
    private String              processStepGUID          = null;
    private String              processStepName          = null;
    private String              requesterUserId          = null;
    private String              requestType              = null;
    private Map<String, String> requestParameters        = null;
    private String              processingEngineUserId   = null;
    private List<String>        completionGuards         = null;
    private String              completionMessage        = null;


    /**
     * Default constructor
     */
    public EngineActionProperties()
    {
        super();
        super.typeName = OpenMetadataType.ENGINE_ACTION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EngineActionProperties(EngineActionProperties template)
    {
        super(template);
        
        if (template != null)
        {
            domainIdentifier = template.getDomainIdentifier();

            mandatoryGuards = template.getMandatoryGuards();
            receivedGuards = template.getReceivedGuards();

            executorEngineGUID = template.getExecutorEngineGUID();
            executorEngineName = template.getExecutorEngineName();

            governanceActionTypeGUID = template.getGovernanceActionTypeGUID();
            governanceActionTypeName = template.getGovernanceActionTypeName();

            processName = template.getProcessName();
            processStepGUID = template.getProcessStepGUID();
            processStepName = template.getProcessStepName();

            requesterUserId = template.getRequesterUserId();
            requestType = template.getRequestType();
            requestParameters = template.getRequestParameters();

            processingEngineUserId = template.getProcessingEngineUserId();

            completionGuards = template.getCompletionGuards();
            completionMessage = template.getCompletionMessage();
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
     * Return the unique identifier of governance engine that is processing the engine action.
     *
     * @return string guid
     */
    public String getExecutorEngineGUID()
    {
        return executorEngineGUID;
    }


    /**
     * Set up the unique identifier of governance engine that is processing the engine action.
     *
     * @param executorEngineGUID string guid
     */
    public void setExecutorEngineGUID(String executorEngineGUID)
    {
        this.executorEngineGUID = executorEngineGUID;
    }


    /**
     * Return the unique name of governance engine that is processing the engine action.
     *
     * @return string name
     */
    public String getExecutorEngineName()
    {
        return executorEngineName;
    }


    /**
     * Set up the unique name of governance engine that is processing the engine action.
     *
     * @param executorEngineName string name
     */
    public void setExecutorEngineName(String executorEngineName)
    {
        this.executorEngineName = executorEngineName;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "EngineActionElement{" +
                "domainIdentifier=" + domainIdentifier +
                ", mandatoryGuards=" + mandatoryGuards +
                ", receivedGuards=" + receivedGuards +
                ", governanceEngineGUID='" + executorEngineGUID + '\'' +
                ", governanceEngineName='" + executorEngineName + '\'' +
                ", governanceActionTypeGUID='" + governanceActionTypeGUID + '\'' +
                ", governanceActionTypeName='" + governanceActionTypeName + '\'' +
                ", processName='" + processName + '\'' +
                ", processStepGUID='" + processStepGUID + '\'' +
                ", processStepName='" + processStepName + '\'' +
                ", requesterUserId='" + requesterUserId + '\'' +
                ", requestType='" + requestType + '\'' +
                ", requestParameters=" + requestParameters +
                ", processingEngineUserId='" + processingEngineUserId + '\'' +
                ", completionGuards=" + completionGuards +
                ", completionMessage='" + completionMessage + '\'' +
                "} " + super.toString();
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
        EngineActionProperties that = (EngineActionProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier && Objects.equals(mandatoryGuards, that.mandatoryGuards) && Objects.equals(receivedGuards, that.receivedGuards) && Objects.equals(executorEngineGUID, that.executorEngineGUID) && Objects.equals(executorEngineName, that.executorEngineName) && Objects.equals(governanceActionTypeGUID, that.governanceActionTypeGUID) && Objects.equals(governanceActionTypeName, that.governanceActionTypeName) && Objects.equals(processName, that.processName) && Objects.equals(processStepGUID, that.processStepGUID) && Objects.equals(processStepName, that.processStepName) && Objects.equals(requesterUserId, that.requesterUserId) && Objects.equals(requestType, that.requestType) && Objects.equals(requestParameters, that.requestParameters) && Objects.equals(processingEngineUserId, that.processingEngineUserId) && Objects.equals(completionGuards, that.completionGuards) && Objects.equals(completionMessage, that.completionMessage);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), domainIdentifier, mandatoryGuards, receivedGuards, executorEngineGUID, executorEngineName, governanceActionTypeGUID, governanceActionTypeName, processName, processStepGUID, processStepName, requesterUserId, requestType, requestParameters, processingEngineUserId, completionGuards, completionMessage);
    }
}
