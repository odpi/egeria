/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;

import java.util.List;
import java.util.Map;


/**
 * SurveyContext provides a survey action service with access to information about
 * the survey request along with access to the open metadata repository interfaces.
 */
public class SurveyContext extends ConnectorContextBase
{
    private final String                    assetGUID;
    private final Map<String, String>       requestParameters;
    private final List<ActionTargetElement> actionTargetElements;
    private final SurveyAssetStore          assetStore;
    private final AnnotationStore           annotationStore;
    private final String                    surveyActionServiceName;
    private final String                    requesterUserId;
    private final AuditLog                  auditLog;



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
     * @param localServerName name of local server
     * @param localServiceName name of the service to call
     * @param externalSourceGUID metadata collection unique id
     * @param externalSourceName metadata collection unique name
     * @param connectorId id of this connector instance
     * @param connectorName name of this connector instance
     * @param connectorUserId userId to use when issuing open metadata requests
     * @param connectorGUID unique identifier of the connector element that describes this connector in the open metadata store(s)
     * @param generateIntegrationReport should the context generate an integration report?
     * @param openMetadataClient client to access open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of results
     * @param deleteMethod default delete method
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the survey action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param annotationStore annotation store for the survey action service
     * @param surveyActionServiceName name of the running service
     * @param requesterUserId original user requesting this governance service
     */
    public SurveyContext(String                     localServerName,
                         String                     localServiceName,
                         String                     externalSourceGUID,
                         String                     externalSourceName,
                         String                     connectorId,
                         String                     connectorName,
                         String                     connectorUserId,
                         String                     connectorGUID,
                         boolean                    generateIntegrationReport,
                         OpenMetadataClient         openMetadataClient,
                         AuditLog                   auditLog,
                         int                        maxPageSize,
                         DeleteMethod               deleteMethod,
                         String                     assetGUID,
                         Map<String, String>        requestParameters,
                         List<ActionTargetElement>  actionTargetElements,
                         SurveyAssetStore           assetStore,
                         AnnotationStore            annotationStore,
                         String                     surveyActionServiceName,
                         String                     requesterUserId)
    {
        super(localServerName,
              localServiceName,
              externalSourceGUID,
              externalSourceName,
              connectorId,
              connectorName,
              connectorUserId,
              connectorGUID,
              generateIntegrationReport,
              openMetadataClient,
              auditLog,
              maxPageSize,
              deleteMethod);

        this.assetGUID               = assetGUID;
        this.requestParameters       = requestParameters;
        this.actionTargetElements    = actionTargetElements;
        this.assetStore              = assetStore;
        this.annotationStore         = annotationStore;
        this.surveyActionServiceName = surveyActionServiceName;
        this.requesterUserId         = requesterUserId;
        this.auditLog                = auditLog;
    }


    /**
     * Return the unique identifier of the asset being discovered.
     *
     * @return string guid
     * @throws UserNotAuthorizedException exception thrown if connector is no longer active
     */
    public String getAssetGUID() throws UserNotAuthorizedException
    {
        final String methodName = "getAssetGUID";

        validateIsActive(methodName);

        return assetGUID;
    }


    /**
     * Return the properties that hold the parameters used to drive the survey action service's analysis.
     *
     * @return AdditionalProperties object storing the analysis parameters
     * @throws UserNotAuthorizedException exception thrown if connector is no longer active
     */
    public Map<String, String> getRequestParameters() throws UserNotAuthorizedException
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
     * @throws UserNotAuthorizedException exception thrown if connector is no longer active
     */
    public SurveyAssetStore getAssetStore() throws UserNotAuthorizedException
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
     * @throws UserNotAuthorizedException exception thrown if connector is no longer active
     */
    public AnnotationStore getAnnotationStore() throws UserNotAuthorizedException
    {
        final String methodName = "getAnnotationStore";

        validateIsActive(methodName);

        return annotationStore;
    }


    /**
     * Determine whether a particular element should be surveyed.  The include list takes precedent over
     * the exclude list.
     *
     * @param elementName name of the element
     * @param excludedNames list of names to exclude (null means ignore value)
     * @param includedNames list of names to include (null means ignore value)
     * @return flag indicating whether to work with the database
     */
    public boolean elementShouldBeSurveyed(String       elementName,
                                           List<String> excludedNames,
                                           List<String> includedNames)
    {
        if (elementName == null)
        {
            return false;
        }

        if (includedNames != null)
        {
            return includedNames.contains(elementName);
        }
        else if (excludedNames != null)
        {
            return ! excludedNames.contains(elementName);
        }

        return true;
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
     * Return the survey action service.
     *
     * @return qualified name
     */
    public String getSurveyActionServiceName()
    {
        return surveyActionServiceName;
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
                "assetGUID='" + assetGUID + '\'' +
                ", requestParameters=" + requestParameters +
                ", actionTargetElements=" + actionTargetElements +
                ", assetStore=" + assetStore +
                ", annotationStore=" + annotationStore +
                ", surveyActionServiceName='" + surveyActionServiceName + '\'' +
                ", requesterUserId='" + requesterUserId + '\'' +
                ", auditLog=" + auditLog +
                ", completionStatus=" + completionStatus +
                ", completionGuards=" + completionGuards +
                ", completionMessage=" + completionMessage +
                ", completionRequestParameters=" + completionRequestParameters +
                ", completionActionTargets=" + completionActionTargets +
                "} " + super.toString();
    }
}
