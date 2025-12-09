/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.enginemap;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceEngineElement;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceContextClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.GovernanceEngineHandlerFactory;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.OMAGEngineServiceRegistration;
import org.odpi.openmetadata.governanceservers.enginehostservices.threads.EngineConfigurationRefreshThread;

import java.util.*;

/**
 * GovernanceEngineMap provides a thread safe mapping of governance engine names to governance engine handlers.
 * This class allows the governance engine handler to be added after the engine host has started processing requests.
 * It is not possible to create the governance handler until the governance engine definition has been retrieved from
 * the metadata access server.  So, unless the engine definition is linked explicitly to an engine service,
 * the type of the governance engine handler is unknown.  Retrieval of a governance engine definition may be
 * triggered by an event, or by a refresh sweep of the metadata.  Therefore, it is possible that two threads
 * are responding to the creation of a new governance engine definition.  Hence, the synchronized methods
 */
public class GovernanceEngineMap
{
    private final Map<String, GovernanceEngineHandlerFactory>    governanceEngineHandlerFactoryMap;
    private final Map<String, GovernanceEngineHandlerProperties> governanceEngineHandlerMap = new HashMap<>();

    private final String                        localServerName;
    private final String                        localServerUserId;
    private final AuditLog                      auditLog;
    private final int                           maxPageSize;


    /**
     * Create a client-side object for calling a survey action engine.
     *
     * @param localServerName the name of the engine host server where the survey action engine is running
     * @param localServerUserId user id for the engine host server to use
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public GovernanceEngineMap(String                        localServerName,
                               String                        localServerUserId,
                               AuditLog                      auditLog,
                               int                           maxPageSize)
    {
        governanceEngineHandlerFactoryMap = OMAGEngineServiceRegistration.getGovernanceEngineHandlerFactoryMap();

        this.localServerName     = localServerName;
        this.localServerUserId   = localServerUserId;
        this.auditLog            = auditLog;
        this.maxPageSize         = maxPageSize;
    }


    /**
     * Return the governance engine handler for a specific governance engine name if known.
     * If the handler does not yet exist, an attempt is made to create it. If this fails, null is returned
     *
     * @param governanceEngineName engine to lookup
     * @return handler or null
     */
    public synchronized GovernanceEngineHandler getGovernanceEngineHandler(String governanceEngineName)
    {
        GovernanceEngineHandlerProperties engineHandlerProperties = governanceEngineHandlerMap.get(governanceEngineName);

        if (engineHandlerProperties != null)
        {
            /*
             * The engine is configured in this server.  Is it active?
             */
            return engineHandlerProperties.getGovernanceEngineHandler();
        }

        return null;
    }



    /**
     * Return the governance engine handler for a specific governance engine name if known.
     * If the handler does not yet exist, an attempt is made to create it. If this fails, null is returned
     *
     * @param governanceEngine engine to lookup
     * @return handler or null
     */
    public synchronized GovernanceEngineHandler getGovernanceEngineHandler(EngineConfig governanceEngine)
    {
        final String methodName = "getGovernanceEngineHandler(name)";

        GovernanceEngineHandlerProperties engineHandlerProperties = governanceEngineHandlerMap.get(governanceEngine.getEngineQualifiedName());

        if (engineHandlerProperties != null)
        {
            /*
             * The engine is configured in this server.  Is it active?
             */
            GovernanceEngineHandler governanceEngineHandler = engineHandlerProperties.getGovernanceEngineHandler();

            if (governanceEngineHandler == null)
            {
                try
                {
                    GovernanceConfigurationClient configurationClient = engineHandlerProperties.getGovernanceConfigurationClient();

                    GovernanceEngineElement governanceEngineElement = configurationClient.getGovernanceEngineByName(governanceEngine.getEngineUserId(),
                                                                                                                    governanceEngine.getEngineQualifiedName());

                    if (governanceEngineElement != null)
                    {
                        governanceEngineHandler = this.createGovernanceEngineHandler(governanceEngine, governanceEngineElement);
                    }
                }
                catch (Exception error)
                {
                    auditLog.logException(methodName,
                                          EngineHostServicesAuditCode.GOVERNANCE_ENGINE_NO_CONFIG.getMessageDefinition(governanceEngine.getEngineQualifiedName(),
                                                                                                                       error.getClass().getName(),
                                                                                                                       error.getMessage()),
                                          error);
                }
            }

            return governanceEngineHandler;
        }

        return null;
    }



    /**
     * Create a governance engine handler for a specific governance engine and add it to the governance engine properties.
     *
     * @param governanceEngineElement governance engine element retrieved from the open metadata repositories.
     * @return governance engine handler
     * @throws InvalidParameterException problem creating handler
     */
    private GovernanceEngineHandler createGovernanceEngineHandler(EngineConfig            governanceEngine,
                                                                  GovernanceEngineElement governanceEngineElement) throws InvalidParameterException
    {
        GovernanceEngineHandler governanceEngineHandler = null;

        if (governanceEngine != null)
        {
            /*
             * The engine exists in open metadata - find out what type of engine this is.
             */
            GovernanceEngineHandlerProperties engineHandlerProperties = governanceEngineHandlerMap.get(governanceEngine.getEngineQualifiedName());

            if (engineHandlerProperties != null)
            {
                /*
                 * The engine is configured for this engine host
                 */
                String governanceEngineTypeName = governanceEngineElement.getElementHeader().getType().getTypeName();

                GovernanceEngineHandlerFactory governanceEngineHandlerFactory = governanceEngineHandlerFactoryMap.get(governanceEngineTypeName);

                if (governanceEngineHandlerFactory != null)
                {
                    /*
                     * The type of governance engine is supported by this engine host. So create the governance engine.
                     */
                    GovernanceConfigurationClient configurationClient = engineHandlerProperties.getGovernanceConfigurationClient();

                    GovernanceContextClient engineActionClient = new GovernanceContextClient(governanceEngine.getOMAGServerName(),
                                                                                             governanceEngine.getOMAGServerPlatformRootURL(),
                                                                                             governanceEngine.getSecretsStoreProvider(),
                                                                                             governanceEngine.getSecretsStoreLocation(),
                                                                                             governanceEngine.getSecretsStoreCollection(),
                                                                                             maxPageSize,
                                                                                             auditLog);

                    governanceEngineHandler = governanceEngineHandlerFactory.createGovernanceEngineHandler(engineHandlerProperties.getEngineConfig(),
                                                                                                           localServerName,
                                                                                                           localServerUserId,
                                                                                                           configurationClient,
                                                                                                           engineActionClient,
                                                                                                           auditLog,
                                                                                                           maxPageSize);


                    /*
                     * Save the engine handler.
                     */
                    engineHandlerProperties.setGovernanceEngineHandler(governanceEngineHandler,
                                                                       governanceEngineTypeName);
                }
            }
        }

        return governanceEngineHandler;
    }


    /**
     * Return the list of active governance engine handlers.
     *
     * @return list of governance engine handlers or an empty list
     */
    public synchronized List<GovernanceEngineHandler> getGovernanceEngineHandlers()
    {
        List<GovernanceEngineHandler> governanceEngineHandlers = new ArrayList<>();

        for (String governanceEngineName : governanceEngineHandlerMap.keySet())
        {
            GovernanceEngineHandler governanceEngineHandler = governanceEngineHandlerMap.get(governanceEngineName).getGovernanceEngineHandler();

            if (governanceEngineHandler != null)
            {
                governanceEngineHandlers.add(governanceEngineHandler);
            }
        }

        return governanceEngineHandlers;
    }


    /**
     * Return the list of the configured engines.
     *
     * @return list of engine names
     */
    public synchronized List<String> getGovernanceEngineNames()
    {
        return new ArrayList<>(governanceEngineHandlerMap.keySet());
    }


    /**
     * Return the list of the configured engines for a particular engine service.
     *
     * @param serviceURLMarker marker that determines which engine service is calling.
     * @return list of engine names
     */
    public synchronized List<String> getGovernanceEngineNames(String serviceURLMarker)
    {
        String governanceEngineTypeName = OMAGEngineServiceRegistration.getGovernanceEngineTypeName(serviceURLMarker);

        if (governanceEngineTypeName != null)
        {
            List<String> governanceEngineNames = new ArrayList<>();

            for (GovernanceEngineHandlerProperties engineHandlerProperties : governanceEngineHandlerMap.values())
            {
                if (governanceEngineTypeName.equals(engineHandlerProperties.getGovernanceEngineTypeName()))
                {
                    governanceEngineNames.add(engineHandlerProperties.getEngineConfig().getEngineQualifiedName());
                }
            }

            if (!governanceEngineNames.isEmpty())
            {
                return governanceEngineNames;
            }
        }

        return null;
    }


    /**
     * Set up a placeholder entry in the governance handler map for a governance engine.
     *
     * @param engineConfig description of the engine
     * @param eventClient client for receiving events
     * @param configurationRefreshThread thread for controlling the receipt of events.
     * @throws InvalidParameterException Problem create the client
     */
    public synchronized void setGovernanceEngineProperties(EngineConfig                     engineConfig,
                                                           OpenMetadataEventClient          eventClient,
                                                           EngineConfigurationRefreshThread configurationRefreshThread) throws InvalidParameterException
    {
        if ((engineConfig != null) &&
                (engineConfig.getEngineQualifiedName() != null) &&
                (governanceEngineHandlerMap.get(engineConfig.getEngineQualifiedName()) == null))
        {
            GovernanceConfigurationClient configurationClient = new GovernanceConfigurationClient(engineConfig.getOMAGServerName(),
                                                                                                  engineConfig.getOMAGServerPlatformRootURL(),
                                                                                                  engineConfig.getSecretsStoreProvider(),
                                                                                                  engineConfig.getSecretsStoreLocation(),
                                                                                                  engineConfig.getSecretsStoreCollection(),
                                                                                                  maxPageSize,
                                                                                                  auditLog);

            GovernanceEngineHandlerProperties engineHandlerProperties = new GovernanceEngineHandlerProperties(engineConfig,
                                                                                                              eventClient,
                                                                                                              configurationRefreshThread,
                                                                                                              configurationClient);

            governanceEngineHandlerMap.put(engineConfig.getEngineQualifiedName(), engineHandlerProperties);
        }
    }


    /**
     * Shutdown all engines ...
     */
    public synchronized void shutdown()
    {
        for (GovernanceEngineHandlerProperties handler : governanceEngineHandlerMap.values())
        {
            if (handler != null)
            {
                handler.shutdown();
            }
        }
    }


    /**
     * GovernanceEngineHandlerProperties manages the properties of a governance engine.
     */
    static class GovernanceEngineHandlerProperties
    {
        private final EngineConfig engineConfig;
        private final OpenMetadataEventClient          eventClient;
        private final EngineConfigurationRefreshThread configurationRefreshThread;
        private       String governanceEngineTypeName = null;
        private final GovernanceConfigurationClient governanceConfigurationClient;

        GovernanceEngineHandler governanceEngineHandler = null;

        /**
         *
         * @param engineConfig description of the engine
         * @param eventClient client for receiving events
         * @param configurationRefreshThread thread for controlling the receipt of events
         * @param governanceConfigurationClient client for accessing open metadata definitions
         */
        public GovernanceEngineHandlerProperties(EngineConfig                     engineConfig,
                                                 OpenMetadataEventClient          eventClient,
                                                 EngineConfigurationRefreshThread configurationRefreshThread,
                                                 GovernanceConfigurationClient    governanceConfigurationClient)
        {
            this.engineConfig                  = engineConfig;
            this.eventClient                   = eventClient;
            this.configurationRefreshThread    = configurationRefreshThread;
            this.governanceConfigurationClient = governanceConfigurationClient;
        }


        /**
         * Return the engine config.
         *
         * @return engine config
         */
        public EngineConfig getEngineConfig()
        {
            return engineConfig;
        }


        /**
         * Set ip the governance action handler and type name ofr this governance engine.
         * This happens once the governance engine definition is retrieve from the open metadata repositories.
         *
         * @param governanceEngineHandler new handler
         * @param governanceEngineTypeName extracted type name
         */
        public void setGovernanceEngineHandler(GovernanceEngineHandler governanceEngineHandler,
                                               String                  governanceEngineTypeName)
        {
            this.governanceEngineHandler = governanceEngineHandler;
            this.governanceEngineTypeName = governanceEngineTypeName;
        }

        /**
         * Return the governance engine handler.
         *
         * @return handler
         */
        public GovernanceEngineHandler getGovernanceEngineHandler()
        {
            return governanceEngineHandler;
        }


        /**
         * Return the open metadata type name for this governance engine.
         *
         * @return type name
         */
        public String getGovernanceEngineTypeName()
        {
            return governanceEngineTypeName;
        }


        /**
         * Return the client for open metadata describing the engine.
         *
         * @return client
         */
        public GovernanceConfigurationClient getGovernanceConfigurationClient()
        {
            return governanceConfigurationClient;
        }


        /**
         * Perform shutdown of the engine.
         */
        public void shutdown()
        {
            if (eventClient != null)
            {
                eventClient.disconnect();
            }

            if (configurationRefreshThread != null)
            {
                configurationRefreshThread.stop();
            }

            if (governanceEngineHandler != null)
            {
                governanceEngineHandler.terminate();
            }
        }
    }
}
