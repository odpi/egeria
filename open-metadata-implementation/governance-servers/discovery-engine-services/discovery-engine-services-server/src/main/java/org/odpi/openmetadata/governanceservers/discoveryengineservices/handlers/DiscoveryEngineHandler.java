/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.discoveryengineservices.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.client.*;
import org.odpi.openmetadata.adapters.connectors.discoveryservices.CSVDiscoveryServiceProvider;
import org.odpi.openmetadata.adapters.connectors.discoveryservices.DuplicateSuspectDiscoveryProvider;
import org.odpi.openmetadata.adapters.connectors.discoveryservices.SequentialDiscoveryPipelineProvider;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.ffdc.DiscoveryEngineServicesAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.discovery.*;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryEngineException;
import org.odpi.openmetadata.frameworks.discovery.properties.*;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.ffdc.DiscoveryEngineServicesErrorCode;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.properties.DiscoveryEngineStatus;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.properties.DiscoveryEngineSummary;

import java.util.*;

/**
 * The DiscoveryEngineHandler is responsible for running discovery services on demand.  It is initialized
 * with the configuration for the discovery services it supports along with the clients to the
 * asset properties store and annotations store.
 */
public class DiscoveryEngineHandler
{
    private String                       serverName;               /* Initialized in constructor */
    private String                       serverUserId;             /* Initialized in constructor */
    private AuditLog                     auditLog;                 /* Initialized in constructor */
    private DiscoveryEngineClient        discoveryEngineClient;    /* Initialized in constructor */
    private DiscoveryConfigurationClient configurationClient;      /* Initialized in constructor */
    private int                          maxPageSize;              /* Initialized in constructor */

    private String                    discoveryEngineName;         /* Initialized in constructor */
    private String                    discoveryEngineGUID         = null;
    private DiscoveryEngineProperties discoveryEngineProperties   = null;
    private DiscoveryServiceCacheMap  discoveryServiceLookupTable = new DiscoveryServiceCacheMap();

    /*
     * Ensure standard discovery services are available to the discovery engines.
     */
    private CSVDiscoveryServiceProvider         csvDiscoveryServiceProvider;
    private DuplicateSuspectDiscoveryProvider   duplicateSuspectDiscoveryProvider;
    private SequentialDiscoveryPipelineProvider sequentialDiscoveryPipelineProvider;


    /**
     * Create a client-side object for calling a discovery engine.
     *
     * @param discoveryEngineName the unique identifier of the discovery engine.
     * @param serverName the name of the discovery server where the discovery engine is running
     * @param serverUserId user id for the server to use
     * @param configurationClient client to retrieve the configuration
     * @param discoveryEngineClient REST client for direct REST Calls
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public DiscoveryEngineHandler(String                       discoveryEngineName,
                                  String                       serverName,
                                  String                       serverUserId,
                                  DiscoveryConfigurationClient configurationClient,
                                  DiscoveryEngineClient        discoveryEngineClient,
                                  AuditLog                     auditLog,
                                  int                          maxPageSize)
    {
        this.discoveryEngineName = discoveryEngineName;
        this.serverName = serverName;
        this.serverUserId = serverUserId;
        this.configurationClient = configurationClient;
        this.discoveryEngineClient = discoveryEngineClient;
        this.auditLog = auditLog;
        this.maxPageSize = maxPageSize;
    }


    /**
     * Return the discovery Engine name - used for error logging.
     *
     * @return discovery engine name
     */
    String getDiscoveryEngineName()
    {
        return discoveryEngineName;
    }


    /**
     * Return a summary of the discovery engine
     *
     * @return discovery engine summary
     */
    public DiscoveryEngineSummary getSummary()
    {
        DiscoveryEngineSummary mySummary = new DiscoveryEngineSummary();

        mySummary.setDiscoveryEngineName(discoveryEngineName);
        mySummary.setDiscoveryEngineGUID(discoveryEngineGUID);

        if (discoveryEngineProperties != null)
        {
            mySummary.setDiscoveryEngineDescription(discoveryEngineProperties.getDescription());
        }

        mySummary.setDiscoveryRequestTypes(discoveryServiceLookupTable.getDiscoveryRequestTypes());

        mySummary.setDiscoveryEngineStatus(DiscoveryEngineStatus.ASSIGNED);
        if (discoveryEngineGUID != null)
        {
            mySummary.setDiscoveryEngineStatus(DiscoveryEngineStatus.CONFIGURING);
        }
        if (discoveryServiceLookupTable.getDiscoveryRequestTypes() != null)
        {
            mySummary.setDiscoveryEngineStatus(DiscoveryEngineStatus.RUNNING);
        }

        return mySummary;
    }

    /**
     * Request that the discovery engine refresh its configuration by calling the metadata server.
     * This request ensures that the latest configuration is in use.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    public void refreshConfig() throws InvalidParameterException,
                                       UserNotAuthorizedException,
                                       PropertyServerException
    {
        final String methodName = "refreshConfig";

        /*
         * Begin by extracting the properties for the discovery engine from the metadata server.
         * This method throws exceptions if there is a problem retrieving the discovery engine properties.
         */
        this.discoveryEngineProperties = configurationClient.getDiscoveryEngineByName(serverUserId, discoveryEngineName);

        if (discoveryEngineProperties == null)
        {
            throw new PropertyServerException(DiscoveryEngineServicesErrorCode.UNKNOWN_DISCOVERY_ENGINE_CONFIG.getMessageDefinition(discoveryEngineName,
                                                                                                                                    configurationClient.getConfigurationServerName(),
                                                                                                                                    serverName),
                                              this.getClass().getName(),
                                              methodName);
        }
        else
        {
            this.discoveryEngineGUID = discoveryEngineProperties.getGUID();
            refreshAllServiceConfig();
        }
    }


    /**
     * Request that the discovery engine refreshes its configuration for all discovery services
     * by calling the metadata server. This request just ensures that the latest configuration
     * is in use.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    private void refreshAllServiceConfig() throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException
    {
        /*
         * Clear the lookup table - this will cause temporary failures in discovery requests if services were already
         * configured.
         */
        final String actionDescription = "Retrieve all discovery service configuration";

        auditLog.logMessage(actionDescription,
                            DiscoveryEngineServicesAuditCode.CLEARING_ALL_DISCOVERY_SERVICE_CONFIG.getMessageDefinition(discoveryEngineName));

        discoveryServiceLookupTable.clear();

        int      startingFrom = 0;
        boolean  moreToReceive = true;

        while (moreToReceive)
        {
            List<String> registeredDiscoveryServices = configurationClient.getRegisteredDiscoveryServices(serverUserId,
                                                                                                          discoveryEngineGUID,
                                                                                                          startingFrom,
                                                                                                          maxPageSize);

            if ((registeredDiscoveryServices != null) && (! registeredDiscoveryServices.isEmpty()))
            {
                for (String registeredDiscoveryServiceGUID : registeredDiscoveryServices)
                {
                    refreshServiceConfig(registeredDiscoveryServiceGUID);
                }

                if (registeredDiscoveryServices.size() < maxPageSize)
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

        auditLog.logMessage(actionDescription,
                            DiscoveryEngineServicesAuditCode.FINISHED_ALL_DISCOVERY_SERVICE_CONFIG.getMessageDefinition(discoveryEngineName));
    }


    /**
     * Request that the discovery engine refreshes its configuration for a single discovery service
     * by calling the metadata server. This request just ensures that the latest configuration
     * is in use.
     *
     * @param registeredDiscoveryServiceGUID unique identifier of the SupportedDiscoveryService relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    public void refreshServiceConfig(String   registeredDiscoveryServiceGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        if (registeredDiscoveryServiceGUID != null)
        {
            RegisteredDiscoveryService discoveryService = configurationClient.getRegisteredDiscoveryService(serverUserId,
                                                                                                            discoveryEngineGUID,
                                                                                                            registeredDiscoveryServiceGUID);

            if (discoveryService != null)
            {
                if (discoveryService.getDiscoveryRequestTypes() != null)
                {
                    for (String discoveryRequestType : discoveryService.getDiscoveryRequestTypes())
                    {
                        DiscoveryServiceCache discoveryServiceCache = new DiscoveryServiceCache(serverName,
                                                                                                discoveryEngineName,
                                                                                                discoveryService);
                        discoveryServiceLookupTable.put(discoveryRequestType, discoveryServiceCache);
                    }
                }
            }
        }
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
     * @throws PropertyServerException there was a problem detected by the discovery engine.
     * @throws DiscoveryEngineException there is a problem with the set up of the discovery engine.
     */
    public  String discoverAsset(String              assetGUID,
                                 String              discoveryRequestType,
                                 Map<String, String> analysisParameters,
                                 List<String>        annotationTypes) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException,
                                                                             DiscoveryEngineException
    {
        final String methodName = "discoverAsset";

        validateDiscoveryEngineInitialized(methodName);

        DiscoveryServiceCache   discoveryServiceCache = discoveryServiceLookupTable.get(discoveryRequestType);

        if (discoveryServiceCache != null)
        {
            return runDiscoveryService(assetGUID, discoveryRequestType, analysisParameters, annotationTypes, discoveryServiceCache);
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
     * @throws PropertyServerException there was a problem with connecting to the metadata server
     * @throws DiscoveryEngineException there is a problem with the set up of the discovery engine.
     */
    public  void scanAllAssets(String              discoveryRequestType,
                               Map<String, String> analysisParameters,
                               List<String>        annotationTypes) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException,
                                                                           DiscoveryEngineException
    {
        final String methodName = "scanAllAssets";

        validateDiscoveryEngineInitialized(methodName);

        DiscoveryServiceCache   discoveryServiceCache = discoveryServiceLookupTable.get(discoveryRequestType);

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
     * Run an instance of a discovery service in its own thread.
     *
     * @param assetGUID unique identifier of the asset to analyse
     * @param discoveryRequestType type of discovery
     * @param suppliedAnalysisParameters parameters for the discovery
     * @param annotationTypes types of annotations that can be returned
     * @param discoveryServiceCache factory for discovery services.
     *
     * @return unique identifier for this request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the discovery engine.
     */
    private String runDiscoveryService(String                assetGUID,
                                       String                discoveryRequestType,
                                       Map<String, String>   suppliedAnalysisParameters,
                                       List<String>          annotationTypes,
                                       DiscoveryServiceCache discoveryServiceCache) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        Date                creationTime = new Date();
        Map<String, String> analysisParameters = suppliedAnalysisParameters;

        if (analysisParameters == null)
        {
            analysisParameters = discoveryServiceCache.getDefaultAnalysisParameters();
        }

        String reportQualifiedName = "DiscoveryAnalysisReport:" + discoveryRequestType + ":" + assetGUID + ":" + creationTime.toString();
        String reportDisplayName   = "Discovery Analysis Report for " + assetGUID;
        String reportDescription   = "This is the " + discoveryRequestType + " discovery analysis report for asset " + assetGUID + " generated at " +
                creationTime.toString() +
                " by the " + discoveryServiceCache.getDiscoveryServiceName() + " discovery service running on discovery engine " +
                discoveryEngineProperties.getDisplayName() + " (" + discoveryEngineName + ").";

        DiscoveryAnalysisReportClient discoveryAnalysisReportClient = new DiscoveryAnalysisReportClient(serverUserId,
                                                                                                        DiscoveryRequestStatus.WAITING,
                                                                                                        assetGUID,
                                                                                                        analysisParameters,
                                                                                                        reportQualifiedName,
                                                                                                        reportDisplayName,
                                                                                                        reportDescription,
                                                                                                        discoveryEngineGUID,
                                                                                                        discoveryServiceCache.getDiscoveryServiceGUID(),
                                                                                                        discoveryEngineClient);

        discoveryAnalysisReportClient.setDiscoveryRequestStatus(DiscoveryRequestStatus.ACTIVATING);

        DiscoveryAnnotationStore annotationStore = new DiscoveryAnnotationStoreClient(serverUserId,
                                                                                      assetGUID,
                                                                                      discoveryAnalysisReportClient,
                                                                                      discoveryEngineClient);
        DiscoveryAssetStore assetStore = new DiscoveryAssetStoreClient(assetGUID,
                                                                       serverUserId,
                                                                       discoveryEngineClient);

        DiscoveryAssetCatalogStore assetCatalogStore = new DiscoveryAssetCatalogStoreClient(serverUserId,
                                                                                            discoveryEngineClient,
                                                                                            maxPageSize);
        DiscoveryContext discoveryContext = new DiscoveryContext(serverUserId,
                                                                 assetGUID,
                                                                 analysisParameters,
                                                                 annotationTypes,
                                                                 assetStore,
                                                                 annotationStore,
                                                                 assetCatalogStore);

        DiscoveryServiceHandler discoveryServiceHandler = new DiscoveryServiceHandler(discoveryEngineProperties,
                                                                                      discoveryRequestType,
                                                                                      discoveryServiceCache.getDiscoveryServiceName(),
                                                                                      discoveryServiceCache.getNextDiscoveryService(),
                                                                                      discoveryContext,
                                                                                      auditLog);
        Thread thread = new Thread(discoveryServiceHandler, discoveryServiceCache.getDiscoveryServiceName() + assetGUID + new Date().toString());
        thread.start();

        return discoveryAnalysisReportClient.getDiscoveryReportGUID();
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
        return discoveryEngineClient.getDiscoveryAnalysisReport(serverUserId, discoveryRequestGUID);
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
        return discoveryEngineClient.getDiscoveryReportAnnotations(serverUserId,
                                                                   discoveryRequestGUID,
                                                                   startingFrom,
                                                                   maximumResults);
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
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
    public  List<Annotation>  getExtendedAnnotations(String   discoveryRequestGUID,
                                                     String   annotationGUID,
                                                     int      startingFrom,
                                                     int      maximumResults) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return discoveryEngineClient.getExtendedAnnotations(serverUserId,
                                                                discoveryRequestGUID,
                                                                annotationGUID,
                                                                startingFrom,
                                                                maximumResults);
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem connecting to the metadata server.
     */
    public  Annotation        getAnnotation(String   discoveryRequestGUID,
                                            String   annotationGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return discoveryEngineClient.getAnnotation(serverUserId, discoveryRequestGUID, annotationGUID);
    }


    /**
     * Confirms termination of the discovery engine.
     */
    public void terminate()
    {
        final String                     actionDescription = "terminate";

        auditLog.logMessage(actionDescription,
                            DiscoveryEngineServicesAuditCode.ENGINE_SHUTDOWN.getMessageDefinition(discoveryEngineName, serverName));

        discoveryEngineProperties = null;
        discoveryServiceLookupTable.clear();
    }


    /**
     * Validate that the discovery engine is initialized.  This is signified by the setting of the discovery engine properties.
     *
     * @param methodName calling method
     * @throws DiscoveryEngineException discovery engine is not initialized
     */
    private void validateDiscoveryEngineInitialized(String methodName) throws DiscoveryEngineException
    {
        if (discoveryEngineProperties == null)
        {
            throw new DiscoveryEngineException(DiscoveryEngineServicesErrorCode.DISCOVERY_ENGINE_NOT_INITIALIZED.getMessageDefinition(serverName,
                                                                                                                                      discoveryEngineName),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * DiscoveryServiceCacheMap maintains the map of discovery request types to discovery services.
     * It is synchronized because the map is being rebuilt periodically.
     */
    static private class  DiscoveryServiceCacheMap
    {
        private volatile Map<String, DiscoveryServiceCache>  discoveryServiceLookupTable = new HashMap<>();

        /**
         * Remove all discovery services from the hash map
         */
        synchronized void clear()
        {
            discoveryServiceLookupTable = new HashMap<>();
        }

        /**
         * Add a new discovery service to the map.
         *
         * @param discoveryRequestType discovery request type
         * @param discoveryServiceCache mapped discovery service
         */
        synchronized void put(String                discoveryRequestType,
                              DiscoveryServiceCache discoveryServiceCache)
        {
            discoveryServiceLookupTable.put(discoveryRequestType, discoveryServiceCache);
        }


        /**
         * Retrieve the discovery service for the requested type.
         *
         * @param discoveryRequestType discovery request type.
         * @return discovery service
         */
        synchronized DiscoveryServiceCache get(String discoveryRequestType)
        {
            return discoveryServiceLookupTable.get(discoveryRequestType);
        }


        /**
         * Return the list of discovery request types registered with a discovery service.
         *
         * @return list of discovery request types.
         */
        synchronized List<String>  getDiscoveryRequestTypes()
        {
            if (discoveryServiceLookupTable.isEmpty())
            {
                return null;
            }

            return new ArrayList<>(discoveryServiceLookupTable.keySet());
        }
    }



    /**
     * DiscoveryServiceCache maintains the information about a registered discovery service.
     */
    static private class  DiscoveryServiceCache
    {
        private DiscoveryService            nextDiscoveryService;
        private DiscoveryServiceProperties  properties;
        private Map<String, String>         defaultAnalysisParameters;


        /**
         * Sets up the cache
         *
         * @param discoveryServerName name of this server.
         * @param discoveryEngineName name of this engine.
         * @param properties registered properties of the discovery services
         * @throws InvalidParameterException there is a problem with the connection used to create the
         * discovery service instance or the discovery service properties are null
         * @throws PropertyServerException problem with the discovery service connector or related config
         */
        DiscoveryServiceCache(String                      discoveryServerName,
                              String                      discoveryEngineName,
                              RegisteredDiscoveryService  properties) throws InvalidParameterException,
                                                                             PropertyServerException
        {
            final String methodName = "DiscoveryServiceCache constructor";

            if (properties != null)
            {
                this.properties                = properties;
                this.defaultAnalysisParameters = properties.getDefaultAnalysisParameters();

                getNextDiscoveryService(); /* validate that the connection works */
            }
            else
            {
                throw new PropertyServerException(DiscoveryEngineServicesErrorCode.NULL_DISCOVERY_SERVICE.getMessageDefinition(methodName,
                                                                                                                               discoveryEngineName,
                                                                                                                               discoveryServerName),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }


        /**
         * Simple getter for the discovery service name - used in messages.
         *
         * @return name
         */
        String  getDiscoveryServiceName()
        {
            return properties.getQualifiedName();
        }


        /**
         * Simple getter for the discovery service's unique identifier (GUID).
         *
         * @return string guid
         */
        String getDiscoveryServiceGUID()
        {
            return properties.getGUID();
        }


        /**
         * Return the analysis parameters to use if none supplied from the caller - these can be null too.
         *
         * @return map of string property name to string property value
         */
        Map<String, String> getDefaultAnalysisParameters()
        {
            return defaultAnalysisParameters;
        }


        /**
         * Return a discovery service connector instance using the registered properties for the discovery service.
         *
         * @return connector
         * @throws InvalidParameterException bad connection
         * @throws PropertyServerException problem with the discovery service connector
         */
        synchronized DiscoveryService  getNextDiscoveryService() throws InvalidParameterException,
                                                                        PropertyServerException
        {
            DiscoveryService  returnValue = nextDiscoveryService;

            try
            {
                ConnectorBroker  connectorBroker = new ConnectorBroker();

                nextDiscoveryService = (DiscoveryService)connectorBroker.getConnector(properties.getConnection());
            }
            catch (ConnectionCheckedException  error)
            {
                throw new InvalidParameterException(error.getReportedErrorMessage(), error, properties.getQualifiedName() + "DiscoveryService Connection");
            }
            catch (ConnectorCheckedException error)
            {
                throw new PropertyServerException(error);
            }

            return returnValue;
        }
    }
}
