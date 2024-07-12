/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.enginemap;

import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceContextClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceEngineElement;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.GovernanceEngineHandlerFactory;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.OMAGEngineServiceRegistration;

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

    private final String                              localServerName;
    private final String                              localServerUserId;
    private final String                        localServerPassword;
    private final GovernanceConfigurationClient configurationClient;
    private final GovernanceContextClient       engineActionClient;
    private final AuditLog                            auditLog;
    private final int                                 maxPageSize;


    /**
     * Create a client-side object for calling a survey action engine.
     *
     * @param localServerName the name of the engine host server where the survey action engine is running
     * @param localServerUserId user id for the engine host server to use
     * @param localServerPassword optional password for the engine host server to use
     * @param configurationClient client to retrieve the configuration
     * @param engineActionClient client used by the engine host services to control the execution of engine action requests
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public GovernanceEngineMap(String                              localServerName,
                               String                              localServerUserId,
                               String                              localServerPassword,
                               GovernanceConfigurationClient configurationClient,
                               GovernanceContextClient engineActionClient,
                               AuditLog                            auditLog,
                               int                                 maxPageSize)
    {
        governanceEngineHandlerFactoryMap = OMAGEngineServiceRegistration.getGovernanceEngineHandlerFactoryMap();

        this.localServerName     = localServerName;
        this.localServerUserId   = localServerUserId;
        this.localServerPassword = localServerPassword;
        this.configurationClient = configurationClient;
        this.engineActionClient  = engineActionClient;
        this.auditLog            = auditLog;
        this.maxPageSize         = maxPageSize;
    }


    /**
     * Retrieve, or create the governance handler for a governance engine.
     *
     * @param governanceEngineGUID unique identifier of the governance engine definition in open metadata
     * @param governanceEngineName unique name of the governance engine definition in open metadata
     * @return governance engine handler
     */
    public synchronized GovernanceEngineHandler getGovernanceEngineHandler(String governanceEngineGUID,
                                                                           String governanceEngineName)
    {
        final String methodName = "getGovernanceEngineHandler(guid, name)";

        GovernanceEngineHandlerProperties engineHandlerProperties = governanceEngineHandlerMap.get(governanceEngineName);

        if (engineHandlerProperties != null)
        {
            GovernanceEngineHandler governanceEngineHandler = engineHandlerProperties.getGovernanceEngineHandler();

            if (governanceEngineHandler == null)
            {
                try
                {
                    GovernanceEngineElement governanceEngineElement = configurationClient.getGovernanceEngineByGUID(localServerUserId,
                                                                                                                        governanceEngineGUID);

                    governanceEngineHandler = this.createGovernanceEngineHandler(governanceEngineElement);
                }
                catch (Exception error)
                {
                    auditLog.logException(methodName,
                                          EngineHostServicesAuditCode.GOVERNANCE_ENGINE_NO_CONFIG.getMessageDefinition(governanceEngineGUID,
                                                                                                                       governanceEngineName,
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
     * Return the governance engine handler for a specific governance engine name if known.
     * If the handler does not yet exist, null is returned
     *
     * @param governanceEngineName name to lookup
     * @return handler or null
     */
    public synchronized GovernanceEngineHandler getGovernanceEngineHandler(String governanceEngineName)
    {
        final String methodName = "getGovernanceEngineHandler(name)";

        GovernanceEngineHandlerProperties engineHandlerProperties = governanceEngineHandlerMap.get(governanceEngineName);

        if (engineHandlerProperties != null)
        {
            GovernanceEngineHandler governanceEngineHandler = engineHandlerProperties.getGovernanceEngineHandler();

            if (governanceEngineHandler == null)
            {
                try
                {
                    GovernanceEngineElement governanceEngineElement = configurationClient.getGovernanceEngineByName(localServerUserId,
                                                                                                                    governanceEngineName);

                    governanceEngineHandler = this.createGovernanceEngineHandler(governanceEngineElement);
                }
                catch (Exception error)
                {
                    auditLog.logException(methodName,
                                          EngineHostServicesAuditCode.GOVERNANCE_ENGINE_NO_CONFIG.getMessageDefinition(governanceEngineName,
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
    private GovernanceEngineHandler createGovernanceEngineHandler(GovernanceEngineElement governanceEngineElement) throws InvalidParameterException
    {
        GovernanceEngineHandler governanceEngineHandler = null;

        if (governanceEngineElement != null)
        {
            /*
             * The engine exists in open metadata.
             */
            GovernanceEngineHandlerProperties engineHandlerProperties = governanceEngineHandlerMap.get(governanceEngineElement.getProperties().getQualifiedName());

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
                     * The type of governance engine is supported by this engine host. so create the governance engine.
                     */
                    governanceEngineHandler = governanceEngineHandlerFactory.createGovernanceEngineHandler(engineHandlerProperties.getEngineConfig(),
                                                                                                           localServerName,
                                                                                                           localServerUserId,
                                                                                                           localServerPassword,
                                                                                                           engineHandlerProperties.getPartnerServerName(),
                                                                                                           engineHandlerProperties.getPartnerURLRoot(),
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
     * @param engineConfigs list of engine descriptions
     * @param partnerServerName name of the server where the engine definition is located
     * @param partnerURLRoot url of the platform where the server is located
     */
    public synchronized void setGovernanceEngineProperties(List<EngineConfig> engineConfigs,
                                                           String             partnerServerName,
                                                           String             partnerURLRoot)
    {
        if (engineConfigs != null)
        {
           for (EngineConfig engineConfig : engineConfigs)
           {
               this.setGovernanceEngineProperties(engineConfig,
                                                  partnerServerName,
                                                  partnerURLRoot);
           }
        }
    }


    /**
     * Set up a placeholder entry in the governance handler map for a governance engine.
     *
     * @param engineConfig description of the engine
     * @param partnerServerName name of the server where the engine definition is located
     * @param partnerURLRoot url of the platform where the server is located
     */
    public synchronized void setGovernanceEngineProperties(EngineConfig engineConfig,
                                                           String       partnerServerName,
                                                           String       partnerURLRoot)
    {
        if ((engineConfig != null) &&
                (engineConfig.getEngineQualifiedName() != null) &&
                (governanceEngineHandlerMap.get(engineConfig.getEngineQualifiedName()) == null))
        {
            GovernanceEngineHandlerProperties engineHandlerProperties = new GovernanceEngineHandlerProperties(engineConfig,
                                                                                                              partnerServerName,
                                                                                                              partnerURLRoot);

            governanceEngineHandlerMap.put(engineConfig.getEngineQualifiedName(),
                                           engineHandlerProperties);
        }
    }


    /**
     * GovernanceEngineHandlerProperties manages the properties of a governance engine.
     */
    static class GovernanceEngineHandlerProperties
    {
        private final EngineConfig engineConfig;
        private final String partnerServerName;
        private final String partnerURLRoot;
        private       String governanceEngineTypeName = null;

        GovernanceEngineHandler governanceEngineHandler = null;

        public GovernanceEngineHandlerProperties(EngineConfig engineConfig,
                                                 String       partnerServerName,
                                                 String       partnerURLRoot)
        {
            this.engineConfig        = engineConfig;
            this.partnerServerName   = partnerServerName;
            this.partnerURLRoot      = partnerURLRoot;
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
         * Return the name of the metadata access service that is used by this engine.
         *
         * @return server name
         */
        public String getPartnerServerName()
        {
            return partnerServerName;
        }

        /**
         * Return the platform URL root where the metadata access service is running.
         *
         * @return url root
         */
        public String getPartnerURLRoot()
        {
            return partnerURLRoot;
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
    }
}
