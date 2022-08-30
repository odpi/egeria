/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.admin;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceEngineElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.RegisteredGovernanceServiceElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceActionProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.RegisteredGovernanceService;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogGovernanceEvent;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineStatus;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesErrorCode;

import java.util.*;

/**
 * The GovernanceEngineHandler is responsible for running governance services on demand.  It is initialized
 * with the configuration for the engine service it supports along with the clients to the metadata store where the
 * definitions of the .
 */
public abstract class GovernanceEngineHandler
{
    protected String                 serverName;        /* Initialized in constructor */
    protected String                 serverUserId;      /* Initialized in constructor */
    private   String                 engineServiceName; /* Initialized in constructor */
    protected GovernanceEngineClient serverClient;      /* Initialized in constructor */
    protected String                 engineUserId;      /* Initialized in constructor */
    protected AuditLog               auditLog;          /* Initialized in constructor */
    protected int                    maxPageSize;       /* Initialized in constructor */

    protected String                     governanceEngineName;   /* Initialized in constructor */
    protected String                     governanceEngineGUID       = null;
    protected GovernanceEngineProperties governanceEngineProperties = null;
    private   GovernanceEngineElement    governanceEngineElement    = null;

    private String        governanceEngineTypeName = null;
    private List<String>  governanceEngineSuperTypeNames = null;


    private GovernanceEngineConfigurationClient configurationClient;        /* Initialized in constructor */


    private GovernanceServiceCacheMap  governanceServiceLookupTable = new GovernanceServiceCacheMap();


    /**
     * Create a client-side object for calling a governance engine.
     *
     * @param engineConfig the properties of the governance engine.
     * @param serverName the name of the engine host server where the governance engine is running
     * @param serverUserId user id for the server to use
     * @param engineServiceName name of the OMES that is supporting this governance engine
     * @param configurationClient client to retrieve the configuration
     * @param serverClient client to control the execution of governance action requests
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public GovernanceEngineHandler(EngineConfig                        engineConfig,
                                   String                              serverName,
                                   String                              serverUserId,
                                   String                              engineServiceName,
                                   GovernanceEngineConfigurationClient configurationClient,
                                   GovernanceEngineClient              serverClient,
                                   AuditLog                            auditLog,
                                   int                                 maxPageSize)
    {
        this.engineServiceName = engineServiceName;
        this.governanceEngineName = engineConfig.getEngineQualifiedName();
        this.serverName = serverName;
        this.serverUserId = serverUserId;
        this.engineUserId = engineConfig.getEngineUserId();
        if (engineUserId == null)
        {
            engineUserId = serverUserId;
        }
        this.configurationClient = configurationClient;
        this.serverClient = serverClient;
        this.auditLog = auditLog;
        this.maxPageSize = maxPageSize;
    }




    /**
     * Return the governance Engine name - used for error logging.
     *
     * @return governance engine name
     */
    public String getGovernanceEngineName()
    {
        return governanceEngineName;
    }


    /**
     * Return a summary of the governance engine
     *
     * @return governance engine summary
     */
    public GovernanceEngineSummary getSummary()
    {
        GovernanceEngineSummary mySummary = new GovernanceEngineSummary();

        mySummary.setGovernanceEngineName(governanceEngineName);
        mySummary.setGovernanceEngineTypeName(governanceEngineTypeName);
        mySummary.setGovernanceEngineService(engineServiceName);
        mySummary.setGovernanceEngineGUID(governanceEngineGUID);

        if (governanceEngineProperties != null)
        {
            mySummary.setGovernanceEngineDescription(governanceEngineProperties.getDescription());
        }

        mySummary.setGovernanceRequestTypes(governanceServiceLookupTable.getGovernanceRequestTypes());
        mySummary.setGovernanceEngineStatus(GovernanceEngineStatus.ASSIGNED);

        if (governanceEngineGUID != null)
        {
            mySummary.setGovernanceEngineStatus(GovernanceEngineStatus.CONFIGURING);
        }

        if (governanceServiceLookupTable.getGovernanceRequestTypes() != null)
        {
            mySummary.setGovernanceEngineStatus(GovernanceEngineStatus.RUNNING);
        }

        return mySummary;
    }


    /**
     * Request that the governance engine refresh its configuration by calling the metadata server.
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
         * Begin by extracting the properties for the governance engine from the metadata server.
         * This method throws exceptions if there is a problem retrieving the governance engine properties.
         */
        this.governanceEngineElement = configurationClient.getGovernanceEngineByName(serverUserId, governanceEngineName);

        if ((governanceEngineElement == null) ||
                    (governanceEngineElement.getElementHeader() == null) ||
                    (governanceEngineElement.getElementHeader().getType() == null) ||
                    (governanceEngineElement.getProperties() == null))
        {
            this.governanceEngineGUID = null;
            this.governanceEngineTypeName = null;
            this.governanceEngineSuperTypeNames = null;
            this.governanceEngineProperties = null;

            throw new PropertyServerException(EngineHostServicesErrorCode.UNKNOWN_GOVERNANCE_ENGINE_CONFIG.getMessageDefinition(governanceEngineName,
                                                                                                                                configurationClient.getConfigurationServerName(),
                                                                                                                                serverName),
                                              this.getClass().getName(),
                                              methodName);
        }
        else
        {
            this.governanceEngineGUID = governanceEngineElement.getElementHeader().getGUID();
            this.governanceEngineTypeName = governanceEngineElement.getElementHeader().getType().getTypeName();
            this.governanceEngineSuperTypeNames = governanceEngineElement.getElementHeader().getType().getSuperTypeNames();
            this.governanceEngineProperties = governanceEngineElement.getProperties();

            refreshAllServiceConfig();
        }
    }


    /**
     * Request that the governance engine refreshes its configuration for all governance services
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
         * Clear the lookup table - this will cause temporary failures in governance requests if services were already
         * configured.
         */
        final String actionDescription = "Retrieve all governance service configuration";

        auditLog.logMessage(actionDescription,
                            EngineHostServicesAuditCode.CLEARING_ALL_GOVERNANCE_SERVICE_CONFIG.getMessageDefinition(governanceEngineName));

        governanceServiceLookupTable.clear();

        int      startingFrom = 0;
        boolean  moreToReceive = true;

        while (moreToReceive)
        {
            List<String> registeredGovernanceServices = configurationClient.getRegisteredGovernanceServices(serverUserId,
                                                                                                            governanceEngineGUID,
                                                                                                            startingFrom,
                                                                                                            maxPageSize);

            if ((registeredGovernanceServices != null) && (! registeredGovernanceServices.isEmpty()))
            {
                for (String registeredGovernanceServiceGUID : registeredGovernanceServices)
                {
                    refreshServiceConfig(registeredGovernanceServiceGUID, null);
                }

                if (registeredGovernanceServices.size() < maxPageSize)
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
                            EngineHostServicesAuditCode.FINISHED_ALL_GOVERNANCE_SERVICE_CONFIG.getMessageDefinition(governanceEngineName));
    }


    /**
     * Request that the governance engine refreshes its configuration for a single governance service
     * by calling the metadata server. This request just ensures that the latest configuration
     * is in use.
     *
     * @param registeredGovernanceServiceGUID unique identifier of the GovernanceService entity
     * @param specificRequestType specific request type to refresh
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    public void refreshServiceConfig(String  registeredGovernanceServiceGUID,
                                     String  specificRequestType) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "refreshServiceConfig";

        if (registeredGovernanceServiceGUID != null)
        {
            RegisteredGovernanceServiceElement governanceServiceElement = configurationClient.getRegisteredGovernanceService(serverUserId,
                                                                                                                             governanceEngineGUID,
                                                                                                                             registeredGovernanceServiceGUID);

            if ((governanceServiceElement != null) &&
                        (governanceServiceElement.getElementHeader() != null) &&
                        (governanceServiceElement.getProperties() != null))
            {
                RegisteredGovernanceService governanceService = governanceServiceElement.getProperties();

                if (governanceService.getRequestTypes() != null)
                {
                    Set<String> governanceRequestTypes;

                    if (specificRequestType == null)
                    {
                        governanceRequestTypes = governanceService.getRequestTypes().keySet();
                    }
                    else
                    {
                        governanceRequestTypes = new HashSet<>();

                        governanceRequestTypes.add(specificRequestType);
                    }

                    for (String governanceRequestType : governanceRequestTypes)
                    {
                        GovernanceServiceCache governanceServiceCache = new GovernanceServiceCache(serverName, governanceEngineName, governanceServiceElement, auditLog);
                        governanceServiceLookupTable.put(governanceRequestType, governanceServiceCache);

                        auditLog.logMessage(methodName,
                                            EngineHostServicesAuditCode.SUPPORTED_REQUEST_TYPE.getMessageDefinition(governanceEngineName,
                                                                                                                    serverName,
                                                                                                                    governanceRequestType));
                    }
                }
            }
        }
    }


    /**
     * Validate that the governance engine is initialized.  This is signified by the setting of the governance engine properties.
     *
     * @param expectedTypeName type of governance engine supported by the calling engine service
     * @param methodName calling method
     * @throws PropertyServerException governance engine is not initialized
     */
    protected void validateGovernanceEngineInitialized(String expectedTypeName,
                                                       String methodName) throws PropertyServerException
    {
        if (governanceEngineProperties == null)
        {
            throw new PropertyServerException(EngineHostServicesErrorCode.GOVERNANCE_ENGINE_NOT_INITIALIZED.getMessageDefinition(serverName,
                                                                                                                            governanceEngineName),
                                               this.getClass().getName(),
                                               methodName);
        }

        if (expectedTypeName.equals(governanceEngineTypeName))
        {
            return;
        }

        if ((governanceEngineSuperTypeNames != null) && (governanceEngineSuperTypeNames.contains(expectedTypeName)))
        {
            return;
        }

        throw new PropertyServerException(EngineHostServicesErrorCode.WRONG_TYPE_OF_GOVERNANCE_ENGINE.getMessageDefinition(governanceEngineName,
                                                                                                                           configurationClient.getConfigurationServerName(),
                                                                                                                           expectedTypeName,
                                                                                                                           governanceEngineTypeName,
                                                                                                                           serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Retrieve the governance service for the requested type.
     *
     * @param governanceRequestType governance request type.
     * @return governance service
     */
    synchronized protected GovernanceServiceCache getServiceCache(String governanceRequestType)
    {
        return governanceServiceLookupTable.get(governanceRequestType);
    }


    /**
     * GovernanceServiceCacheMap maintains the map of governance request types to governance services.
     * It is synchronized because the map is being rebuilt periodically.
     */
    static private class  GovernanceServiceCacheMap
    {
        private volatile Map<String, GovernanceServiceCache>  governanceServiceLookupTable = new HashMap<>();

        /**
         * Remove all governance services from the hash map
         */
        synchronized void clear()
        {
            governanceServiceLookupTable = new HashMap<>();
        }

        /**
         * Add a new governance service to the map.
         *
         * @param governanceRequestType governance request type
         * @param governanceServiceCache mapped governance service
         */
        synchronized void put(String                 governanceRequestType,
                              GovernanceServiceCache governanceServiceCache)
        {
            governanceServiceLookupTable.put(governanceRequestType, governanceServiceCache);
        }


        /**
         * Retrieve the governance service for the requested type.
         *
         * @param governanceRequestType governance request type.
         * @return governance service
         */
        synchronized protected GovernanceServiceCache get(String governanceRequestType)
        {
            return governanceServiceLookupTable.get(governanceRequestType);
        }


        /**
         * Return the list of governance request types registered with a governance service.
         *
         * @return list of governance request types.
         */
        synchronized List<String>  getGovernanceRequestTypes()
        {
            if (governanceServiceLookupTable.isEmpty())
            {
                return null;
            }

            return new ArrayList<>(governanceServiceLookupTable.keySet());
        }
    }


    /**
     * Execute the requested governance action on or after the start time.
     *
     * @param governanceActionGUID unique identifier of potential governance action to run.
     */
    public void executeGovernanceAction(String governanceActionGUID)
    {
        final String methodName = "executeGovernanceAction";

        try
        {
            GovernanceActionElement    latestGovernanceActionElement = serverClient.getGovernanceAction(serverUserId, governanceActionGUID);
            GovernanceActionProperties properties                    = latestGovernanceActionElement.getProperties();

            if (properties.getActionStatus() == GovernanceActionStatus.APPROVED)
            {
                serverClient.claimGovernanceAction(serverUserId, governanceActionGUID);

                // todo if the start date is in the future then the governance action should be given to the scheduler

                serverClient.updateGovernanceActionStatus(serverUserId, governanceActionGUID, GovernanceActionStatus.IN_PROGRESS);

                runGovernanceService(governanceActionGUID,
                                     properties.getRequestType(),
                                     properties.getRequestParameters(),
                                     properties.getRequestSourceElements(),
                                     properties.getActionTargetElements());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  EngineHostServicesAuditCode.ACTION_PROCESSING_ERROR.getMessageDefinition(methodName,
                                                                                                           error.getClass().getName(),
                                                                                                           governanceActionGUID,
                                                                                                           error.getMessage()),
                                  error);
        }
    }


    /**
     * Run an instance of a governance action service in its own thread and return the handler (for disconnect processing).
     *
     * @param governanceActionGUID unique identifier of the asset to analyse
     * @param requestType unique identifier of the asset that the annotations should be attached to
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
    public abstract GovernanceServiceHandler runGovernanceService(String                     governanceActionGUID,
                                                                  String                     requestType,
                                                                  Map<String, String>        requestParameters,
                                                                  List<RequestSourceElement> requestSourceElements,
                                                                  List<ActionTargetElement>  actionTargetElements) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException;


    /**
     * Pass on the watchdog event to any governance service that supports them.
     *
     * @param watchdogGovernanceEvent element describing the changing metadata data.
     *
     * @throws InvalidParameterException Vital fields of the governance action are not filled out
     * @throws UserNotAuthorizedException the governance service is not permitted to execute the governance action
     * @throws PropertyServerException there is a problem communicating with the open metadata stores
     */
    public void publishWatchdogEvent(WatchdogGovernanceEvent watchdogGovernanceEvent) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        /*
         * This method is overridden by subclasses where applicable
         */
    }


    /**
     * Confirms termination of the governance engine.
     */
    public void terminate()
    {
        final String actionDescription = "terminate";

        auditLog.logMessage(actionDescription,
                            EngineHostServicesAuditCode.ENGINE_SHUTDOWN.getMessageDefinition(governanceEngineName, serverName));

        governanceEngineElement = null;
        governanceEngineGUID = null;
        governanceEngineProperties = null;
        governanceServiceLookupTable.clear();
    }
}
