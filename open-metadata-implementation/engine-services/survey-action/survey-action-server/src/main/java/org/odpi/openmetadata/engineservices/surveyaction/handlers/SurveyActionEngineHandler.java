/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.handlers;

import org.odpi.openmetadata.accessservices.assetowner.client.CSVFileAssetOwner;
import org.odpi.openmetadata.accessservices.assetowner.client.ConnectedAssetClient;
import org.odpi.openmetadata.accessservices.assetowner.client.FileSystemAssetOwner;
import org.odpi.openmetadata.accessservices.assetowner.client.SurveyAssetStoreClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceContextClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionAuditCode;
import org.odpi.openmetadata.engineservices.surveyaction.ffdc.SurveyActionErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.enums.EngineActionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyContext;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyOpenMetadataStore;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceCache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The SurveyActionEngineHandler is responsible for running survey action services on demand.  It is initialized
 * with the configuration for the survey action services it supports along with the clients to the
 * asset properties store and annotations store.
 */
public class SurveyActionEngineHandler extends GovernanceEngineHandler
{
    private final ConnectedAssetClient connectedAssetClient;    /* Initialized in constructor */
    private final FileSystemAssetOwner fileSystemAssetOwner;    /* Initialized in constructor */
    private final CSVFileAssetOwner    csvFileAssetOwner;    /* Initialized in constructor */
    private final OpenMetadataClient   openMetadataClient;      /* Initialized in constructor */

    private final PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * Create a client-side object for calling a survey action engine.
     *
     * @param engineConfig the unique identifier of the survey action engine.
     * @param serverName the name of the engine host server where the survey action engine is running
     * @param serverUserId user id for the server to use
     * @param configurationClient client to retrieve the configuration
     * @param engineActionClient client used by the engine host services to control the execution of governance action requests
     * @param connectedAssetClient REST client from the OCF that is linked to the Asset Owner OMAS
     * @param fileSystemAssetOwner REST client that is linked to the Asset Owner OMAS
     * @param csvFileAssetOwner REST client that is linked to the Asset Owner OMAS
     * @param openMetadataClient REST Client from the GAF that is linked to the Asset Owner OMAS
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public SurveyActionEngineHandler(EngineConfig                        engineConfig,
                                     String                              serverName,
                                     String                              serverUserId,
                                     GovernanceConfigurationClient configurationClient,
                                     GovernanceContextClient             engineActionClient,
                                     ConnectedAssetClient                connectedAssetClient,
                                     FileSystemAssetOwner                fileSystemAssetOwner,
                                     CSVFileAssetOwner                   csvFileAssetOwner,
                                     OpenMetadataClient                  openMetadataClient,
                                     AuditLog                            auditLog,
                                     int                                 maxPageSize)
    {
        super(engineConfig,
              serverName,
              serverUserId,
              EngineServiceDescription.SURVEY_ACTION_OMES.getEngineServiceFullName(),
              configurationClient,
              engineActionClient,
              auditLog,
              maxPageSize);

        this.connectedAssetClient = connectedAssetClient;
        this.fileSystemAssetOwner = fileSystemAssetOwner;
        this.csvFileAssetOwner    = csvFileAssetOwner;
        this.openMetadataClient   = openMetadataClient;
    }


    /**
     * Run an instance of a survey action service in its own thread and return the handler (for disconnect processing).
     *
     * @param engineActionGUID unique identifier of engine action to activate
     * @param governanceRequestType governance request type to use when calling the governance engine
     * @param requesterUserId original user requesting this governance service
     * @param requestedStartDate date/time to start the governance service
     * @param requestParameters name-value properties to control the governance service
     * @param requestSourceElements metadata elements associated with the request to the governance service
     * @param actionTargetElements metadata elements that need to be worked on by the governance service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the governance engine.
     */
    @Override
    public void runGovernanceService(String                     engineActionGUID,
                                     String                     governanceRequestType,
                                     String                     requesterUserId,
                                     Date                       requestedStartDate,
                                     Map<String, String>        requestParameters,
                                     List<RequestSourceElement> requestSourceElements,
                                     List<ActionTargetElement>  actionTargetElements) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "runGovernanceService";

        super.validateGovernanceEngineInitialized(OpenMetadataType.SURVEY_ACTION_ENGINE.typeName, methodName);

        GovernanceServiceCache governanceServiceCache = super.getServiceCache(governanceRequestType);

        if (governanceServiceCache != null)
        {
            if ((actionTargetElements != null) && (! actionTargetElements.isEmpty()))
            {
                String assetGUID = getAssetGUIDFromActionTargets(actionTargetElements,
                                                                 governanceServiceCache.getGovernanceServiceName(),
                                                                 governanceRequestType,
                                                                 engineActionGUID);

                if (assetGUID != null)
                {
                    SurveyActionServiceHandler surveyActionServiceHandler = this.getSurveyActionServiceHandler(assetGUID,
                                                                                                               governanceRequestType,
                                                                                                               requestParameters,
                                                                                                               actionTargetElements,
                                                                                                               engineActionGUID,
                                                                                                               requesterUserId,
                                                                                                               requestedStartDate,
                                                                                                               governanceServiceCache);

                    super.startServiceExecutionThread(engineActionGUID,
                                                      surveyActionServiceHandler,
                                                      governanceServiceCache.getGovernanceServiceName() + assetGUID + new Date());
                }
                else
                {
                    throw new InvalidParameterException(SurveyActionErrorCode.NO_TARGET_ASSET.getMessageDefinition(governanceServiceCache.getGovernanceServiceName(),
                                                                                                                   governanceRequestType,
                                                                                                                   engineActionGUID),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        "assetGUID");
                }
            }
            else
            {
                throw new InvalidParameterException(SurveyActionErrorCode.NO_TARGET_ASSET.getMessageDefinition(governanceServiceCache.getGovernanceServiceName(),
                                                                                                               governanceRequestType,
                                                                                                               engineActionGUID),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    "assetGUID");
            }
        }
        else
        {
            throw new InvalidParameterException(SurveyActionErrorCode.NULL_REQUEST.getMessageDefinition(engineActionGUID),
                                                this.getClass().getName(),
                                                methodName,
                                                "governanceServiceCache");
        }
    }


    /**
     * Extract the asset to survey from the action targets.  If there are multiple
     * action targets that are assets then the method picks one and logs a message to
     * say the others are being ignored.
     *
     * @param actionTargetElements action target elements
     * @param governanceServiceName name of the selected governance service
     * @param governanceRequestType calling request type
     * @param engineActionGUID unique identifier of the engine action
     * @return assetGUID or null
     * @throws InvalidParameterException problem updating action target status
     * @throws UserNotAuthorizedException problem updating action target status
     * @throws PropertyServerException problem updating action target status
     */
    private String getAssetGUIDFromActionTargets(List<ActionTargetElement> actionTargetElements,
                                                 String                    governanceServiceName,
                                                 String                    governanceRequestType,
                                                 String                    engineActionGUID) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getAssetGUIDFromActionTargets";

        String assetGUID = null;
        List<String> ignoredAssets = new ArrayList<>();

        /*
         * First pick out all the assets ...
         */
        List<ActionTargetElement> assetTargetElements = new ArrayList<>();

        for (ActionTargetElement actionTargetElement : actionTargetElements)
        {
            if (actionTargetElement != null)
            {
                if (propertyHelper.isTypeOf(actionTargetElement.getTargetElement(), OpenMetadataType.ASSET.typeName))
                {
                    assetTargetElements.add(actionTargetElement);
                }
            }
        }

        if (assetTargetElements.size() == 1)
        {
            assetGUID = assetTargetElements.get(0).getTargetElement().getElementGUID();
        }
        else
        {
            /*
             * Since there are multiple assets, only pick out the ones with an action target name of "newAsset".
             */
            for (ActionTargetElement actionTargetElement : assetTargetElements)
            {
                if (ActionTarget.NEW_ASSET.getName().equals(actionTargetElement.getActionTargetName()))
                {
                    if (assetGUID == null)
                    {
                        assetGUID = actionTargetElement.getTargetElement().getElementGUID();
                        engineActionClient.updateActionTargetStatus(serverUserId,
                                                                    actionTargetElement.getActionTargetRelationshipGUID(),
                                                                    EngineActionStatus.IN_PROGRESS,
                                                                    new Date(),
                                                                    null,
                                                                    null);
                    }
                    else
                    {
                        ignoredAssets.add(actionTargetElement.getTargetElement().getElementGUID());
                    }
                }
            }

            if (! ignoredAssets.isEmpty())
            {
                auditLog.logMessage(methodName,
                                    SurveyActionAuditCode.IGNORING_ASSETS.getMessageDefinition(governanceServiceName,
                                                                                               governanceRequestType,
                                                                                               engineActionGUID,
                                                                                               assetGUID,
                                                                                               ignoredAssets.toString()));
            }
        }



        return assetGUID;
    }

    /**
     * Create an instance of a survey action service handler.
     *
     * @param assetGUID unique identifier of the asset to analyse
     * @param requestType type of survey
     * @param requestParameters parameters for the survey
     * @param actionTargetElements the elements for the service to work on
     * @param engineActionGUID unique identifier of the associated engine action entity
     * @param requesterUserId original user requesting this governance service
     * @param requestedStartDate date/time that the governance service should start executing
     * @param governanceServiceCache factory for survey action services
     *
     * @return unique identifier for this request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the survey action engine.
     */
    private SurveyActionServiceHandler getSurveyActionServiceHandler(String                    assetGUID,
                                                                     String                    requestType,
                                                                     Map<String, String>       requestParameters,
                                                                     List<ActionTargetElement> actionTargetElements,
                                                                     String                    engineActionGUID,
                                                                     String                    requesterUserId,
                                                                     Date                      requestedStartDate,
                                                                     GovernanceServiceCache    governanceServiceCache) throws InvalidParameterException,
                                                                                                                              UserNotAuthorizedException,
                                                                                                                              PropertyServerException
    {
        Date                creationTime = new Date();
        Map<String, String> analysisParameters = governanceServiceCache.getRequestParameters(requestParameters);

        String reportQualifiedName = "SurveyReport::" + requestType + "::" + assetGUID + "::" + creationTime;
        String reportDisplayName   = "Survey Report for " + assetGUID;
        String reportDescription   = "This is the " + requestType + " survey report for asset " + assetGUID + " generated at " +
                                             creationTime +
                                             " by the " + governanceServiceCache.getGovernanceServiceName() + " survey action service running on survey action engine " +
                                             governanceEngineProperties.getDisplayName() + " (" + governanceEngineName + ").";

        AnnotationStore annotationStore = new AnnotationStore(engineUserId,
                                                              assetGUID,
                                                              openMetadataClient,
                                                              null,
                                                              null,
                                                              reportQualifiedName,
                                                              reportDisplayName,
                                                              reportDescription,
                                                              requestType,
                                                              engineActionGUID);

        SurveyAssetStore assetStore = new SurveyAssetStoreClient(assetGUID,
                                                                 engineUserId,
                                                                 connectedAssetClient,
                                                                 fileSystemAssetOwner,
                                                                 csvFileAssetOwner,
                                                                 auditLog);

        SurveyOpenMetadataStore openMetadataStore = new SurveyOpenMetadataStore(openMetadataClient,
                                                                                engineUserId,
                                                                                null,
                                                                                null,
                                                                                engineActionGUID);

        SurveyContext surveyContext = new SurveyContext(engineUserId,
                                                        assetGUID,
                                                        analysisParameters,
                                                        actionTargetElements,
                                                        assetStore,
                                                        annotationStore,
                                                        openMetadataStore,
                                                        governanceServiceCache.getGovernanceServiceName(),
                                                        requesterUserId,
                                                        auditLog);

        return new SurveyActionServiceHandler(governanceEngineProperties,
                                              governanceEngineGUID,
                                              serverUserId,
                                              engineActionGUID,
                                              engineActionClient,
                                              governanceServiceCache.getServiceRequestType(),
                                              governanceServiceCache.getGovernanceServiceGUID(),
                                              governanceServiceCache.getGovernanceServiceName(),
                                              governanceServiceCache.getNextGovernanceService(),
                                              surveyContext,
                                              requestedStartDate,
                                              auditLog);
    }
}
