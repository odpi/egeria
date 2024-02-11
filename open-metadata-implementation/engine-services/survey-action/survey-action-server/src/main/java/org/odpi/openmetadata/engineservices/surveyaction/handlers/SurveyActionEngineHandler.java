/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.handlers;

import org.odpi.openmetadata.accessservices.assetowner.client.CSVFileAssetOwner;
import org.odpi.openmetadata.accessservices.assetowner.client.FileSystemAssetOwner;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceContextClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.accessservices.assetowner.client.ConnectedAssetClient;
import org.odpi.openmetadata.accessservices.assetowner.client.SurveyAssetStoreClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyContext;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyOpenMetadataStore;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceCache;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.*;

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
                                     GovernanceEngineConfigurationClient configurationClient,
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
     * @param governanceActionGUID unique identifier of the asset to analyse
     * @param governanceRequestType governance request type to use when calling the governance engine
     * @param startDate date/time to start the governance action service
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     *
     * @return service handler for this request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the governance action engine.
     */
    @Override
    public void runGovernanceService(String                     governanceActionGUID,
                                     String                     governanceRequestType,
                                     Date                       startDate,
                                     Map<String, String>        requestParameters,
                                     List<RequestSourceElement> requestSourceElements,
                                     List<ActionTargetElement>  actionTargetElements) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "runGovernanceService";

        super.validateGovernanceEngineInitialized(OpenMetadataType.SURVEY_ACTION_ENGINE.typeName, methodName);

        GovernanceServiceCache governanceServiceCache = super.getServiceCache(governanceRequestType);

        if ((governanceServiceCache != null) && (actionTargetElements != null) && (! actionTargetElements.isEmpty()))
        {
            String assetGUID = null;

            for (ActionTargetElement actionTargetElement : actionTargetElements)
            {
                if ((actionTargetElement != null)
                            && (actionTargetElement.getTargetElement() != null)
                            && (actionTargetElement.getTargetElement().getType() != null))
                {
                    String       typeName       = actionTargetElement.getTargetElement().getType().getTypeName();
                    List<String> superTypeNames = actionTargetElement.getTargetElement().getType().getSuperTypeNames();

                    if ((OpenMetadataType.ASSET.typeName.equals(typeName)) || ((superTypeNames != null) && (superTypeNames.contains(OpenMetadataType.ASSET.typeName))))
                    {
                        assetGUID = actionTargetElement.getTargetElement().getElementGUID();
                    }
                }
            }

            SurveyActionServiceHandler surveyActionServiceHandler = this.getSurveyActionServiceHandler(assetGUID,
                                                                                                       governanceRequestType,
                                                                                                       requestParameters,
                                                                                                       governanceActionGUID,
                                                                                                       governanceServiceCache);

            Thread thread = new Thread(surveyActionServiceHandler, governanceServiceCache.getGovernanceServiceName() + assetGUID + new Date());
            thread.start();
        }
    }


    /**
     * Create an instance of a survey action service handler.
     *
     * @param assetGUID unique identifier of the asset to analyse
     * @param requestType type of survey
     * @param requestParameters parameters for the survey
     * @param engineActionGUID unique identifier of the associated engine action entity
     * @param governanceServiceCache factory for survey action services
     *
     * @return unique identifier for this request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the survey action engine.
     */
    private SurveyActionServiceHandler getSurveyActionServiceHandler(String                 assetGUID,
                                                                     String                 requestType,
                                                                     Map<String, String>    requestParameters,
                                                                     String                 engineActionGUID,
                                                                     GovernanceServiceCache governanceServiceCache) throws InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException
    {
        Date                creationTime = new Date();
        Map<String, String> analysisParameters = governanceServiceCache.getRequestParameters(requestParameters);

        String reportQualifiedName = "SurveyReport:" + requestType + ":" + assetGUID + ":" + creationTime;
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
                                                                 csvFileAssetOwner);

        SurveyOpenMetadataStore openMetadataStore = new SurveyOpenMetadataStore(openMetadataClient,
                                                                                engineUserId,
                                                                                null,
                                                                                null);

        SurveyContext surveyContext = new SurveyContext(engineUserId,
                                                           assetGUID,
                                                           analysisParameters,
                                                           assetStore,
                                                           annotationStore,
                                                           openMetadataStore);

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
                                              auditLog);
    }
}
