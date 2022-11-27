/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.assetanalysis.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.client.*;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.discovery.*;
import org.odpi.openmetadata.frameworks.discovery.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceCache;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceServiceHandler;

import java.util.*;

/**
 * The DiscoveryEngineHandler is responsible for running discovery services on demand.  It is initialized
 * with the configuration for the discovery services it supports along with the clients to the
 * asset properties store and annotations store.
 */
public class DiscoveryEngineHandler extends GovernanceEngineHandler
{
    private final DiscoveryEngineClient discoveryEngineClient;    /* Initialized in constructor */

    private static final String supportGovernanceEngineType = "OpenDiscoveryEngine";
    private static final String assetTypeName = "Asset";

    /**
     * Create a client-side object for calling a discovery engine.
     *
     * @param engineConfig the unique identifier of the discovery engine.
     * @param serverName the name of the engine host server where the discovery engine is running
     * @param serverUserId user id for the server to use
     * @param configurationClient client to retrieve the configuration
     * @param serverClient client used by the engine host services to control the execution of governance action requests
     * @param discoveryEngineClient REST client for direct REST Calls to Discovery Engine OMAS - used by discovery services
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public DiscoveryEngineHandler(EngineConfig                        engineConfig,
                                  String                              serverName,
                                  String                              serverUserId,
                                  GovernanceEngineConfigurationClient configurationClient,
                                  GovernanceEngineClient              serverClient,
                                  DiscoveryEngineClient               discoveryEngineClient,
                                  AuditLog                            auditLog,
                                  int                                 maxPageSize)
    {
        super(engineConfig,
              serverName,
              serverUserId,
              EngineServiceDescription.ASSET_ANALYSIS_OMES.getEngineServiceFullName(),
              configurationClient,
              serverClient,
              auditLog,
              maxPageSize);

        this.discoveryEngineClient = discoveryEngineClient;
    }


    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param assetGUID identifier of the asset to analyze.
     * @param discoveryRequestType identifier of the type of discovery request to run - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     * @param annotationTypes list of the types of annotations to produce (and no others)
     *
     * @return unique id for the discovery request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem with connecting to the metadata server or
     *                                 there is a problem with the setup of the discovery engine.
     */
    public  String discoverAsset(String              assetGUID,
                                 String              discoveryRequestType,
                                 Map<String, String> analysisParameters,
                                 List<String>        annotationTypes) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "discoverAsset";

        super.validateGovernanceEngineInitialized(supportGovernanceEngineType, methodName);

        GovernanceServiceCache discoveryServiceCache = super.getServiceCache(discoveryRequestType);

        if (discoveryServiceCache != null)
        {
            return runDiscoveryService(assetGUID, discoveryRequestType, analysisParameters, methodName, annotationTypes, discoveryServiceCache);
        }

        return null;
    }


    /**
     * Request the execution of a discovery service for each asset that is found (limited by the supported zones of the
     * Discovery Engine OMAS).
     *
     * @param discoveryRequestType identifier of the type of discovery to run - this determines which discovery service to run.
     * @param analysisParameters name value properties to control the analysis
     * @param annotationTypes list of the types of annotations to produce (and no others)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem with connecting to the metadata server or
     *                                 there is a problem with the setup of the discovery engine.
     */
    public  void scanAllAssets(String              discoveryRequestType,
                               Map<String, String> analysisParameters,
                               List<String>        annotationTypes) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "scanAllAssets";

        super.validateGovernanceEngineInitialized(supportGovernanceEngineType, methodName);

        GovernanceServiceCache   discoveryServiceCache = super.getServiceCache(discoveryRequestType);

        if (discoveryServiceCache != null)
        {
            int      startingFrom = 0;
            boolean  moreToReceive = true;

            while (moreToReceive)
            {
                List<String> assets = discoveryEngineClient.getAssets(serverUserId, startingFrom, maxPageSize);

                if ((assets != null) && (! assets.isEmpty()))
                {
                    for (String assetGUID : assets)
                    {
                        if (assetGUID != null)
                        {
                            runDiscoveryService(assetGUID,
                                                discoveryRequestType,
                                                analysisParameters,
                                                methodName + ": " + assetGUID,
                                                annotationTypes,
                                                discoveryServiceCache);
                        }
                    }

                    if (assets.size() < maxPageSize)
                    {
                        moreToReceive = false;
                    }
                    else
                    {
                        startingFrom = startingFrom + maxPageSize;
                    }
                }
                else
                {
                    moreToReceive = false;
                }
            }
        }
    }


    /**
     * Run an instance of a governance action service in its own thread and return the handler (for disconnect processing).
     *
     * @param governanceActionGUID unique identifier of the asset to analyse
     * @param governanceRequestType governance request type to use when calling the governance engine
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
    public GovernanceServiceHandler runGovernanceService(String                     governanceActionGUID,
                                                         String                     governanceRequestType,
                                                         Map<String, String>        requestParameters,
                                                         List<RequestSourceElement> requestSourceElements,
                                                         List<ActionTargetElement>  actionTargetElements) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        final String methodName = "runGovernanceService";

        super.validateGovernanceEngineInitialized(supportGovernanceEngineType, methodName);

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

                    if ((assetTypeName.equals(typeName)) || ((superTypeNames != null) && (superTypeNames.contains(assetTypeName))))
                    {
                        assetGUID = actionTargetElement.getTargetElement().getElementGUID();
                    }
                }
            }

            DiscoveryServiceHandler discoveryServiceHandler = this.getDiscoveryServiceHandler(assetGUID,
                                                                                              governanceRequestType,
                                                                                              requestParameters,
                                                                                              null,
                                                                                              null,
                                                                                              governanceActionGUID,
                                                                                              governanceServiceCache);

            Thread thread = new Thread(discoveryServiceHandler, governanceServiceCache.getGovernanceServiceName() + assetGUID + new Date());
            thread.start();

            return discoveryServiceHandler;
        }

        return null;
    }


    /**
     * Run an instance of a discovery service in its own thread.
     *
     * @param assetGUID unique identifier of the asset to analyse
     * @param discoveryRequestType type of discovery
     * @param suppliedAnalysisParameters parameters for the discovery
     * @param annotationTypes types of annotations that can be returned
     * @param governanceServiceCache factory for discovery services.
     *
     * @return unique identifier for this request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the discovery engine.
     */
    private String runDiscoveryService(String                 assetGUID,
                                       String                 discoveryRequestType,
                                       Map<String, String>    suppliedAnalysisParameters,
                                       String                 firstAnalysisStep,
                                       List<String>           annotationTypes,
                                       GovernanceServiceCache governanceServiceCache) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        DiscoveryServiceHandler discoveryServiceHandler = this.getDiscoveryServiceHandler(assetGUID,
                                                                                          discoveryRequestType,
                                                                                          suppliedAnalysisParameters,
                                                                                          firstAnalysisStep,
                                                                                          annotationTypes,
                                                                                          null,
                                                                                          governanceServiceCache);

        Thread thread = new Thread(discoveryServiceHandler, governanceServiceCache.getGovernanceServiceName() + assetGUID + new Date());
        thread.start();

        return discoveryServiceHandler.getDiscoveryReportGUID();
    }



    /**
     * Create an instance of a discovery service handler.
     *
     * @param assetGUID unique identifier of the asset to analyse
     * @param discoveryRequestType type of discovery
     * @param suppliedAnalysisParameters parameters for the discovery
     * @param firstAnalysisStepName name of the first analysis step for the discovery service
     * @param annotationTypes types of annotations that can be returned
     * @param governanceActionGUID unique identifier of the associated governance action entity
     * @param governanceServiceCache factory for discovery services
     *
     * @return unique identifier for this request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the discovery engine.
     */
    private DiscoveryServiceHandler getDiscoveryServiceHandler(String                 assetGUID,
                                                               String                 discoveryRequestType,
                                                               Map<String, String>    suppliedAnalysisParameters,
                                                               String                 firstAnalysisStepName,
                                                               List<String>           annotationTypes,
                                                               String                 governanceActionGUID,
                                                               GovernanceServiceCache governanceServiceCache) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        Date                creationTime = new Date();
        Map<String, String> analysisParameters = governanceServiceCache.getRequestParameters(suppliedAnalysisParameters);

        String reportQualifiedName = "DiscoveryAnalysisReport:" + discoveryRequestType + ":" + assetGUID + ":" + creationTime;
        String reportDisplayName   = "Discovery Analysis Report for " + assetGUID;
        String reportDescription   = "This is the " + discoveryRequestType + " discovery analysis report for asset " + assetGUID + " generated at " +
                                             creationTime +
                                             " by the " + governanceServiceCache.getGovernanceServiceName() + " discovery service running on discovery engine " +
                                             governanceEngineProperties.getDisplayName() + " (" + governanceEngineName + ").";

        DiscoveryAnalysisReportClient discoveryAnalysisReportClient = new DiscoveryAnalysisReportClient(engineUserId,
                                                                                                        DiscoveryRequestStatus.WAITING,
                                                                                                        assetGUID,
                                                                                                        analysisParameters,
                                                                                                        firstAnalysisStepName,
                                                                                                        reportQualifiedName,
                                                                                                        reportDisplayName,
                                                                                                        reportDescription,
                                                                                                        governanceEngineGUID,
                                                                                                        governanceServiceCache.getGovernanceServiceGUID(),
                                                                                                        discoveryEngineClient);

        discoveryAnalysisReportClient.setDiscoveryRequestStatus(DiscoveryRequestStatus.ACTIVATING);

        DiscoveryAnnotationStore annotationStore = new DiscoveryAnnotationStoreClient(engineUserId,
                                                                                      assetGUID,
                                                                                      discoveryAnalysisReportClient,
                                                                                      discoveryEngineClient);
        DiscoveryAssetStore assetStore = new DiscoveryAssetStoreClient(assetGUID,
                                                                       engineUserId,
                                                                       discoveryEngineClient);

        DiscoveryAssetCatalogStore assetCatalogStore = new DiscoveryAssetCatalogStoreClient(engineUserId,
                                                                                            discoveryEngineClient,
                                                                                            maxPageSize);
        DiscoveryContext discoveryContext = new DiscoveryContext(engineUserId,
                                                                 assetGUID,
                                                                 analysisParameters,
                                                                 annotationTypes,
                                                                 assetStore,
                                                                 annotationStore,
                                                                 assetCatalogStore);

        return new DiscoveryServiceHandler(governanceEngineProperties,
                                           governanceEngineGUID,
                                           serverUserId,
                                           governanceActionGUID,
                                           serverClient,
                                           governanceServiceCache.getServiceRequestType(),
                                           governanceServiceCache.getGovernanceServiceGUID(),
                                           governanceServiceCache.getGovernanceServiceName(),
                                           governanceServiceCache.getNextGovernanceService(),
                                           discoveryContext,
                                           discoveryAnalysisReportClient.getDiscoveryReportGUID(),
                                           auditLog);

    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return discovery report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem connecting to the metadata server.
     */
    public DiscoveryAnalysisReport getDiscoveryReport(String   discoveryRequestGUID) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return discoveryEngineClient.getDiscoveryAnalysisReport(engineUserId, discoveryRequestGUID);
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem connecting to the metadata server.
     */
    public  List<Annotation> getDiscoveryReportAnnotations(String   discoveryRequestGUID,
                                                           int      startingFrom,
                                                           int      maximumResults) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return discoveryEngineClient.getDiscoveryReportAnnotations(engineUserId,
                                                                   discoveryRequestGUID,
                                                                   startingFrom,
                                                                   maximumResults);
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem connecting to the metadata server.
     */
    public  List<Annotation>  getExtendedAnnotations(String   annotationGUID,
                                                     int      startingFrom,
                                                     int      maximumResults) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return discoveryEngineClient.getExtendedAnnotations(engineUserId,
                                                            annotationGUID,
                                                            startingFrom,
                                                            maximumResults);
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem connecting to the metadata server.
     */
    public  Annotation getAnnotation(String annotationGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return discoveryEngineClient.getAnnotation(engineUserId, annotationGUID);
    }
}
