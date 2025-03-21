/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.admin;

import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceContextClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.EngineActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineStatus;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesErrorCode;

import java.util.*;

/**
 * The GovernanceEngineHandler is responsible for running governance services on demand.  It is initialized
 * with the configuration for the engine service it supports along with the clients to the metadata store where the
 * definitions of the governance engine are managed.
 */
public abstract class GovernanceEngineHandler
{
    protected String                  serverName;         /* Initialized in constructor */
    protected String                  serverUserId;       /* Initialized in constructor */
    protected GovernanceContextClient engineActionClient; /* Initialized in constructor */
    protected String                  engineUserId;      /* Initialized in constructor */
    protected AuditLog                auditLog;          /* Initialized in constructor */
    protected int                     maxPageSize;       /* Initialized in constructor */

    private final String                 engineServiceName;      /* Initialized in constructor */
    protected String                     governanceEngineName;   /* Initialized in constructor */
    protected String                     governanceEngineGUID       = null;
    protected GovernanceEngineProperties governanceEngineProperties = null;
    private   GovernanceEngineElement governanceEngineElement = null;
    private   boolean                 servicesToRestart       = true;

    private String        governanceEngineTypeName = null;
    private List<String>  governanceEngineSuperTypeNames = null;

    private final Map<String, EngineActionExecution> engineActionThreadMap = new HashMap<>();

    protected final GovernanceConfigurationClient configurationClient;        /* Initialized in constructor */

    private final GovernanceServiceCacheMap  governanceServiceLookupTable = new GovernanceServiceCacheMap();


    /**
     * Create a client-side object for calling a governance engine.
     *
     * @param engineConfig the properties of the governance engine.
     * @param serverName the name of the engine host server where the governance engine is running
     * @param serverUserId user id for the server to use
     * @param engineServiceName name of the OMES that is supporting this governance engine
     * @param configurationClient client to retrieve the configuration
     * @param engineActionClient client to control the execution of engine action requests
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public GovernanceEngineHandler(EngineConfig                        engineConfig,
                                   String                              serverName,
                                   String                              serverUserId,
                                   String                              engineServiceName,
                                   GovernanceConfigurationClient configurationClient,
                                   GovernanceContextClient             engineActionClient,
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
        this.engineActionClient  = engineActionClient;
        this.auditLog            = auditLog;
        this.maxPageSize         = maxPageSize;
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
     * Return the governance engine element - used to determine if the governance
     * engine is configured.
     *
     * @return governance engine element
     */
    public GovernanceEngineElement getGovernanceEngineElement()
    {
        return governanceEngineElement;
    }


    /**
     * Return a summary of the governance engine
     *
     * @return governance engine summary
     */
    public synchronized GovernanceEngineSummary getSummary()
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
     * @throws UserNotAuthorizedException user id is not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    public synchronized void refreshConfig() throws InvalidParameterException,
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
     * @throws UserNotAuthorizedException user id is not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    private void refreshAllServiceConfig() throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException
    {
        final String methodName = "refreshAllServiceConfig";

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
            List<RegisteredGovernanceServiceElement> registeredGovernanceServices = configurationClient.getRegisteredGovernanceServices(serverUserId,
                                                                                                                                        governanceEngineGUID,
                                                                                                                                        startingFrom,
                                                                                                                                        maxPageSize);

            if ((registeredGovernanceServices != null) && (! registeredGovernanceServices.isEmpty()))
            {
                for (RegisteredGovernanceServiceElement registeredGovernanceService : registeredGovernanceServices)
                {
                    refreshRegisteredGovernanceService(registeredGovernanceService, null, methodName);
                }

                startingFrom = startingFrom + maxPageSize;
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
     * @throws UserNotAuthorizedException user id is not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    public synchronized void refreshServiceConfig(String  registeredGovernanceServiceGUID,
                                                  String  specificRequestType) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "refreshServiceConfig";

        if (registeredGovernanceServiceGUID != null)
        {
            RegisteredGovernanceServiceElement registeredGovernanceServiceElement = configurationClient.getRegisteredGovernanceService(serverUserId,
                                                                                                                                       governanceEngineGUID,
                                                                                                                                       registeredGovernanceServiceGUID);

            this.refreshRegisteredGovernanceService(registeredGovernanceServiceElement,
                                                    specificRequestType,
                                                    methodName);
        }
    }


    /**
     * Set up the service cache for each registered service.  An entry is added for each governance request type.
     *
     * @param registeredGovernanceServiceElement description of the governance service and its governance request types
     * @param specificRequestType refresh all request types (null) or a specific one?
     * @param methodName calling method for message
     * @throws InvalidParameterException invalid configuration elements
     * @throws PropertyServerException error detected in remote server
     */
    private void refreshRegisteredGovernanceService(RegisteredGovernanceServiceElement registeredGovernanceServiceElement,
                                                    String                             specificRequestType,
                                                    String                             methodName) throws InvalidParameterException,
                                                                                                          PropertyServerException
    {
        if ((registeredGovernanceServiceElement != null) &&
                    (registeredGovernanceServiceElement.getElementHeader() != null) &&
                    (registeredGovernanceServiceElement.getProperties() != null))
        {
            RegisteredGovernanceService registeredGovernanceService = registeredGovernanceServiceElement.getProperties();

            if (registeredGovernanceService.getRequestTypes() != null)
            {
                Set<String> governanceRequestTypes;

                if (specificRequestType == null)
                {
                    governanceRequestTypes = registeredGovernanceService.getRequestTypes().keySet();
                }
                else
                {
                    governanceRequestTypes = new HashSet<>();

                    governanceRequestTypes.add(specificRequestType);
                }

                for (String governanceRequestType : governanceRequestTypes)
                {
                    GovernanceServiceCache governanceServiceCache = new GovernanceServiceCache(serverName,
                                                                                               governanceEngineName,
                                                                                               registeredGovernanceServiceElement,
                                                                                               governanceRequestType,
                                                                                               auditLog);
                    governanceServiceLookupTable.put(governanceRequestType, governanceServiceCache);

                    auditLog.logMessage(methodName,
                                        EngineHostServicesAuditCode.SUPPORTED_REQUEST_TYPE.getMessageDefinition(governanceEngineName,
                                                                                                                serverName,
                                                                                                                governanceRequestType));
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
     * Restart any services that were running when the engine host shut down.
     *
     * @param governanceEngineElement governance engine description
     */
    public void restartServices(GovernanceEngineElement governanceEngineElement)
    {
        final String methodName = "restartServices";

        if ((servicesToRestart) && (governanceEngineElement != null))
        {
            try
            {
                int startFrom = 0;

                List<EngineActionElement> engineActionElements = engineActionClient.getActiveClaimedEngineActions(serverUserId,
                                                                                                                  governanceEngineElement.getElementHeader().getGUID(),
                                                                                                                  startFrom,
                                                                                                                  maxPageSize);

                servicesToRestart = false;
                while (engineActionElements != null)
                {
                    for (EngineActionElement engineActionElement : engineActionElements)
                    {
                        if (engineActionElement != null)
                        {
                            restartGovernanceService(engineActionElement);
                        }
                    }

                    startFrom            = startFrom + maxPageSize;
                    engineActionElements = engineActionClient.getActiveClaimedEngineActions(serverUserId,
                                                                                            governanceEngineElement.getElementHeader().getGUID(),
                                                                                            startFrom,
                                                                                            maxPageSize);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      EngineHostServicesAuditCode.UNEXPECTED_EXCEPTION_DURING_RESTART.getMessageDefinition(methodName,
                                                                                                                           error.getClass().getName(),
                                                                                                                           error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Look for engine actions that were skipped - typically because the events were missed.
     */
    public void startMissedEngineActions()
    {
        final String methodName = "startMissedEngineActions";

        int startFrom = 0;
        int pageSize  = 10;

        try
        {
            List<EngineActionElement> activeEngineActions = engineActionClient.getActiveEngineActions(serverUserId,
                                                                                                      startFrom,
                                                                                                      pageSize);

            while (activeEngineActions != null)
            {
                for (EngineActionElement engineActionElement : activeEngineActions)
                {
                    if ((engineActionElement != null) &&
                            (engineActionElement.getActionStatus() == EngineActionStatus.APPROVED) &&
                            (governanceEngineGUID.equals(engineActionElement.getGovernanceEngineGUID())))
                    {

                        try
                        {
                            this.executeEngineAction(engineActionElement.getElementHeader().getGUID());
                        }
                        catch (Exception error)
                        {
                            auditLog.logException(methodName,
                                                  EngineHostServicesAuditCode.ENGINE_ACTION_FAILED.getMessageDefinition(engineActionElement.getGovernanceEngineName(),
                                                                                                                        error.getClass().getName(),
                                                                                                                        error.getMessage()),
                                                  engineActionElement.toString(),
                                                  error);
                        }
                    }
                }

                startFrom           = startFrom + pageSize;
                activeEngineActions = engineActionClient.getActiveEngineActions(serverUserId,
                                                                                startFrom,
                                                                                pageSize);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  EngineHostServicesAuditCode.UNEXPECTED_EXCEPTION_DURING_RESTART.getMessageDefinition(methodName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       error.getMessage()),
                                  error);
        }
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
     * Execute the requested engine action on or after the start time.
     *
     * @param engineActionGUID unique identifier of potential governance action to run.
     */
    public void executeEngineAction(String engineActionGUID)
    {
        final String methodName = "executeEngineAction";

        try
        {
            EngineActionElement latestEngineActionElement = engineActionClient.getEngineAction(serverUserId, engineActionGUID);

            if (latestEngineActionElement.getActionStatus() == EngineActionStatus.APPROVED)
            {
                engineActionClient.claimEngineAction(serverUserId, engineActionGUID);

                runGovernanceService(engineActionGUID,
                                     latestEngineActionElement.getRequestType(),
                                     latestEngineActionElement.getRequesterUserId(),
                                     latestEngineActionElement.getRequestedStartTime(),
                                     latestEngineActionElement.getRequestParameters(),
                                     latestEngineActionElement.getRequestSourceElements(),
                                     latestEngineActionElement.getActionTargetElements());
            }
            else if ((latestEngineActionElement.getActionStatus() == EngineActionStatus.CANCELLED) &&
                    (serverUserId.equals(latestEngineActionElement.getProcessingEngineUserId())))
            {
                cancelGovernanceService(engineActionGUID);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  EngineHostServicesAuditCode.ACTION_PROCESSING_ERROR.getMessageDefinition(methodName,
                                                                                                           error.getClass().getName(),
                                                                                                           engineActionGUID,
                                                                                                           error.getMessage()),
                                  error);
        }
    }


    /**
     * Run an instance of a governance service in its own thread and register the handler (for disconnect processing).
     *
     * @param engineActionGUID unique identifier of the engine action
     * @param governanceRequestType governance request type to use when calling the governance engine
     * @param requestedStartDate date/time to start the governance service
     * @param requesterUserId original user requesting this governance service
     * @param requestParameters name-value properties to control the governance service
     * @param requestSourceElements metadata elements associated with the request to the governance service
     * @param actionTargetElements metadata elements that need to be worked on by the governance service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the governance engine.
     */
    public abstract void runGovernanceService(String                     engineActionGUID,
                                              String                     governanceRequestType,
                                              String                     requesterUserId,
                                              Date                       requestedStartDate,
                                              Map<String, String>        requestParameters,
                                              List<RequestSourceElement> requestSourceElements,
                                              List<ActionTargetElement>  actionTargetElements) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException;

    /**
     * Restart an instance of a governance service in its own thread and register the handler (for disconnect processing).
     * This can be overridden by an engine service if restart needs different logic to the first time
     * a governance service starts for an engine action.
     *
     * @param engineActionElement details of the engine action
     */
    public  void restartGovernanceService(EngineActionElement engineActionElement)
    {
        final String methodName = "restartGovernanceService";

        try
        {
            runGovernanceService(engineActionElement.getElementHeader().getGUID(),
                                 engineActionElement.getRequestType(),
                                 engineActionElement.getRequesterUserId(),
                                 engineActionElement.getRequestedStartTime(),
                                 engineActionElement.getRequestParameters(),
                                 engineActionElement.getRequestSourceElements(),
                                 engineActionElement.getActionTargetElements());
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  EngineHostServicesAuditCode.ACTION_PROCESSING_ERROR.getMessageDefinition(methodName,
                                                                                                           error.getClass().getName(),
                                                                                                           engineActionElement.getElementHeader().getGUID(),
                                                                                                           error.getMessage()),
                                  error);
        }
    }


    /**
     * Start the execution of the governance service on a new thread.
     *
     * @param engineActionGUID unique identifier of the engine action that initiated this request.
     * @param serviceToRun runnable packed with details of the governance service
     * @param threadName name of the thread for diagnostic purposes
     * @throws InvalidParameterException error updating engine action status
     * @throws PropertyServerException error updating engine action status
     * @throws UserNotAuthorizedException error updating engine action status
     */
    protected synchronized void startServiceExecutionThread(String                    engineActionGUID,
                                                            GovernanceServiceHandler  serviceToRun,
                                                            String                    threadName) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        Thread thread = new Thread(serviceToRun, threadName);

        EngineActionExecution engineActionExecution = new EngineActionExecution();

        engineActionExecution.executionThread = thread;
        engineActionExecution.governanceServiceHandler = serviceToRun;

        engineActionThreadMap.put(engineActionGUID, engineActionExecution);

        thread.start();
    }


    /**
     * Cancelling a running governance service.  The ability to stop the running service is not guaranteed and
     * the engine host's platform may need to be restarted.
     *
     * @param engineActionGUID unique identifier of the engine action
     */
    private synchronized void cancelGovernanceService(String engineActionGUID) throws ConnectorCheckedException
    {
        final String methodName = "cancelGovernanceService";

        EngineActionExecution engineActionExecution = engineActionThreadMap.get(engineActionGUID);

        if (engineActionExecution.executionThread != null)
        {
            auditLog.logMessage(methodName,
                                EngineHostServicesAuditCode.ENGINE_ACTION_CANCELLED.getMessageDefinition(governanceEngineName,
                                                                                                         engineActionGUID,
                                                                                                         engineActionExecution.executionThread.getName()));

            /*
             * Tell the service to shut down.  This will cause exceptions if the service accesses its context.
             */
            engineActionExecution.governanceServiceHandler.disconnect();

            /*
             * This interrupt should cause an exception to be received by the governance service thread.
             * However, it is highly unreliable unless the survey service is performing IO like writing to a file.
             */
            engineActionExecution.executionThread.interrupt();
        }
    }


    /**
     * A class to capture the execution environment.
     */
    static class EngineActionExecution
    {
        GovernanceServiceHandler governanceServiceHandler;
        Thread                   executionThread;
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
