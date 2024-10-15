/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.surveyaction.ffdc.SAFAuditCode;
import org.odpi.openmetadata.frameworks.surveyaction.ffdc.SAFErrorCode;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * SurveyContext provides a survey action service with access to information about
 * the survey request along with access to the open metadata repository interfaces.
 */
public class SurveyContext
{
    private final String                    userId;
    private final String                    assetGUID;
    private final Map<String, String>       requestParameters;
    private final List<ActionTargetElement> actionTargetElements;
    private final SurveyAssetStore          assetStore;
    private final AnnotationStore           annotationStore;
    private final SurveyOpenMetadataStore   openMetadataStore;
    private final String                    surveyActionServiceName;
    private final String                    requesterUserId;
    private final AuditLog                  auditLog;
    private final FileClassifier            fileClassifier;

    private       boolean                   isActive = true;


    /*
     * Values set by the survey action service for completion.
     */
    private CompletionStatus          completionStatus            = null;
    private List<String>              completionGuards            = null;
    private AuditLogMessageDefinition completionMessage           = null;
    private Map<String, String>       completionRequestParameters = null;
    private List<NewActionTarget>     completionActionTargets     = null;


    /**
     * Constructor sets up the key parameters for using the context.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the survey action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param assetStore survey asset store for the survey action service
     * @param annotationStore annotation store for the survey action service
     * @param openMetadataStore generic metadata API from the Governance Action Framework (GAF)
     * @param surveyActionServiceName name of the running service
     * @param requesterUserId original user requesting this governance service
     * @param auditLog logging destination
     */
    public SurveyContext(String                     userId,
                         String                     assetGUID,
                         Map<String, String>        requestParameters,
                         List<ActionTargetElement>  actionTargetElements,
                         SurveyAssetStore           assetStore,
                         AnnotationStore            annotationStore,
                         SurveyOpenMetadataStore    openMetadataStore,
                         String                     surveyActionServiceName,
                         String                     requesterUserId,
                         AuditLog                   auditLog)
    {
        this.userId                  = userId;
        this.assetGUID               = assetGUID;
        this.requestParameters       = requestParameters;
        this.actionTargetElements    = actionTargetElements;
        this.assetStore              = assetStore;
        this.annotationStore         = annotationStore;
        this.openMetadataStore       = openMetadataStore;
        this.surveyActionServiceName = surveyActionServiceName;
        this.requesterUserId         = requesterUserId;
        this.auditLog                = auditLog;

        this.fileClassifier          = new FileClassifier(openMetadataStore);
    }


    /**
     * Return the unique identifier of the asset being discovered.
     *
     * @return string guid
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public String getAssetGUID() throws ConnectorCheckedException
    {
        final String methodName = "getAssetGUID";

        validateIsActive(methodName);

        return assetGUID;
    }


    /**
     * Return the properties that hold the parameters used to drive the survey action service's analysis.
     *
     * @return AdditionalProperties object storing the analysis parameters
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public Map<String, String> getRequestParameters() throws ConnectorCheckedException
    {
        final String methodName = "getRequestParameters";

        validateIsActive(methodName);

        return requestParameters;
    }


    /**
     * Return the list of elements that this governance action service should work on.
     *
     * @return cached list of action target metadata elements
     */
    public List<ActionTargetElement> getActionTargetElements()
    {
        return actionTargetElements;
    }


    /**
     * Return the requester user identifier.
     *
     * @return userId
     */
    public String getRequesterUserId()
    {
        return requesterUserId;
    }


    /**
     * Return the asset store for the survey action service.  This is able to provide a connector to the asset
     * configured with the properties of the asset from a property server.
     *
     * @return asset store
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public SurveyAssetStore getAssetStore() throws ConnectorCheckedException
    {
        final String methodName = "getAssetStore";

        validateIsActive(methodName);

        return assetStore;
    }


    /**
     * Return the annotation store for the survey action service.  This is where the annotations are stored and
     * retrieved from.
     *
     * @return annotation store
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public AnnotationStore getAnnotationStore() throws ConnectorCheckedException
    {
        final String methodName = "getAnnotationStore";

        validateIsActive(methodName);

        return annotationStore;
    }


    /**
     * Return a generic interface for accessing and updating open metadata elements, classifications and relationships.
     *
     * @return open metadata store
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public SurveyOpenMetadataStore getOpenMetadataStore() throws ConnectorCheckedException
    {
        final String methodName = "getOpenMetadataStore";

        validateIsActive(methodName);

        return openMetadataStore;
    }


    /**
     * Return the file classifier that retrieves file reference data from the open metadata repositories.
     *
     * @return file classifier
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    public FileClassifier getFileClassifier() throws ConnectorCheckedException
    {
        final String methodName = "getFileClassifier";

        validateIsActive(methodName);

        return fileClassifier;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        isActive = false;
    }


    /**
     * Verify that the connector is still active.
     *
     * @param methodName calling method
     * @throws ConnectorCheckedException exception thrown if no longer active
     */
    private void validateIsActive(String methodName) throws ConnectorCheckedException
    {
        if (! isActive)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    SAFAuditCode.DISCONNECT_DETECTED.getMessageDefinition(surveyActionServiceName));
            }

            throw new ConnectorCheckedException(SAFErrorCode.DISCONNECT_DETECTED.getMessageDefinition(surveyActionServiceName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param requestParameters properties to pass to the next governance action service
     * @param newActionTargets map of action target names to GUIDs for the resulting governance action service
     * @param completionMessage message to describe completion results or reasons for failure
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void recordCompletionStatus(CompletionStatus          status,
                                       List<String>              outputGuards,
                                       Map<String, String>       requestParameters,
                                       List<NewActionTarget>     newActionTargets,
                                       AuditLogMessageDefinition completionMessage) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        this.completionStatus = status;
        this.completionGuards = outputGuards;
        this.completionRequestParameters = requestParameters;
        this.completionActionTargets = newActionTargets;
        this.completionMessage = completionMessage;
    }


    /**
     * Return the completion status provided by the survey action service.
     *
     * @return enum
     */
    public CompletionStatus getCompletionStatus()
    {
        return completionStatus;
    }


    /**
     * Return the guards provided by the survey action service.  If these are null then a standard
     * guard (eg SURVEY_INVALID, SURVEY_COMPLETE, SURVEY_FAILED) is used
     *
     * @return list of strings
     */
    public List<String> getCompletionGuards()
    {
        return completionGuards;
    }


    /**
     * Return the populated message to act as the completion message.
     *
     * @return audit log message definition
     */
    public AuditLogMessageDefinition getCompletionMessage()
    {
        return completionMessage;
    }


    /**
     * Return any new request parameters for downstream governance actions.
     *
     * @return map of request parameter name to request parameter value
     */
    public Map<String, String> getCompletionRequestParameters()
    {
        return completionRequestParameters;
    }


    /**
     * Return any additional action targets that should be made available to downstream governance actions.
     * This is in addition to the actual survey report.
     *
     * @return list of new action targets
     */
    public List<NewActionTarget> getCompletionActionTargets()
    {
        return completionActionTargets;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SurveyContext{" +
                "userId='" + userId + '\'' +
                ", assetGUID='" + assetGUID + '\'' +
                ", requestParameters=" + requestParameters +
                ", actionTargetElements=" + actionTargetElements +
                ", assetStore=" + assetStore +
                ", annotationStore=" + annotationStore +
                ", openMetadataStore=" + openMetadataStore +
                ", surveyActionServiceName='" + surveyActionServiceName + '\'' +
                ", requesterUserId='" + requesterUserId + '\'' +
                ", fileClassifier=" + fileClassifier +
                ", isActive=" + isActive +
                ", completionStatus=" + completionStatus +
                ", completionGuards=" + completionGuards +
                ", completionMessage=" + completionMessage +
                ", completionRequestParameters=" + completionRequestParameters +
                ", completionActionTargets=" + completionActionTargets +
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
        SurveyContext that = (SurveyContext) objectToCompare;
        return isActive == that.isActive &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(assetGUID, that.assetGUID) &&
                Objects.equals(requestParameters, that.requestParameters) &&
                Objects.equals(actionTargetElements, that.actionTargetElements) &&
                Objects.equals(assetStore, that.assetStore) &&
                Objects.equals(annotationStore, that.annotationStore) &&
                Objects.equals(openMetadataStore, that.openMetadataStore) &&
                Objects.equals(surveyActionServiceName, that.surveyActionServiceName) &&
                Objects.equals(requesterUserId, that.requesterUserId) &&
                Objects.equals(auditLog, that.auditLog) &&
                Objects.equals(fileClassifier, that.fileClassifier) &&
                completionStatus == that.completionStatus &&
                Objects.equals(completionGuards, that.completionGuards) &&
                Objects.equals(completionMessage, that.completionMessage) &&
                Objects.equals(completionRequestParameters, that.completionRequestParameters) &&
                Objects.equals(completionActionTargets, that.completionActionTargets);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(userId, assetGUID, requestParameters, actionTargetElements, assetStore, annotationStore,
                            openMetadataStore, surveyActionServiceName, requesterUserId, auditLog, fileClassifier,
                            isActive, completionStatus, completionGuards, completionMessage,
                            completionRequestParameters, completionActionTargets);
    }
}
