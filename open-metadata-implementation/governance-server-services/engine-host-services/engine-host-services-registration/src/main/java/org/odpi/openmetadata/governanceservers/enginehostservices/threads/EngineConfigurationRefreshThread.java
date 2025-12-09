/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.threads;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.listener.OpenMetadataOutTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * EngineConfigurationRefreshThread is the class responsible for establishing the listener for configuration
 * updates for a specific governance engine.  It runs as a separate thread in a number of phases,
 * both listening for metadata changes and periodically issuing calls to the metadata server.
 * Firstly, it needs to retrieve the governance engine definition from the metadata store.  Then it can
 * create the handler for the governance engine.  This means that incoming engine actions for this engine will be
 * processed.  Then it is listening for changes to the engine definition and actions to run.
 */
public class EngineConfigurationRefreshThread implements Runnable
{
    private final EngineConfig            engineConfig;
    private final GovernanceEngineMap     engineHandlers;
    private final OpenMetadataEventClient eventClient;
    private final AuditLog                auditLog;
    private final String                  localServerName;
    private volatile boolean              keepTrying = true;

    private static final Logger log = LoggerFactory.getLogger(EngineConfigurationRefreshThread.class);


    /**
     * The constructor takes details of the governance engine handlers needed by the listener and the information
     * needed to log errors if the metadata server is not available.
     *
     * @param engineHandlers list of governance engine handlers running locally mapped to their names
     * @param eventClient client for accessing the Governance Server OMAS OutTopic
     * @param auditLog logging destination
     * @param localServerName this server's name
     */
    public EngineConfigurationRefreshThread(EngineConfig             engineConfig,
                                            GovernanceEngineMap      engineHandlers,
                                            OpenMetadataEventClient  eventClient,
                                            AuditLog                 auditLog,
                                            String                   localServerName)
    {
        this.engineConfig            = engineConfig;
        this.engineHandlers          = engineHandlers;
        this.eventClient             = eventClient;
        this.auditLog                = auditLog;
        this.localServerName         = localServerName;
    }


    /**
     * Method that runs when the thread is started.
     */
    @Override
    public void run()
    {
        final String actionDescription = "Register configuration listener";

        boolean       listenerRegistered = false;
        List<String>  configToRetrieve;

        while (keepTrying)
        {
            configToRetrieve = new ArrayList<>(engineHandlers.getGovernanceEngineNames());

            while ((! listenerRegistered) && (keepTrying))
            {
                try
                {
                    eventClient.registerListener(engineConfig.getEngineUserId(), new OpenMetadataOutTopicListener(engineConfig, engineHandlers, auditLog));
                    listenerRegistered = true;

                    auditLog.logMessage(actionDescription,
                                        EngineHostServicesAuditCode.CONFIGURATION_LISTENER_REGISTERED.getMessageDefinition(localServerName,
                                                                                                                           engineConfig.getEngineQualifiedName(),
                                                                                                                           engineConfig.getOMAGServerName()));
                }
                catch (UserNotAuthorizedException error)
                {
                    auditLog.logException(actionDescription,
                                          EngineHostServicesAuditCode.SERVER_NOT_AUTHORIZED.getMessageDefinition(localServerName,
                                                                                                                 engineConfig.getOMAGServerName(),
                                                                                                                 engineConfig.getOMAGServerPlatformRootURL(),
                                                                                                                 engineConfig.getEngineUserId(),
                                                                                                                 error.getReportedErrorMessage()),
                                          error);
                    waitToRetry();
                }
                catch (Exception error)
                {
                    auditLog.logException(actionDescription,
                                          EngineHostServicesAuditCode.NO_CONFIGURATION_LISTENER.getMessageDefinition(localServerName,
                                                                                                                     engineConfig.getOMAGServerName(),
                                                                                                                     error.getClass().getName(),
                                                                                                                     error.getMessage()),
                                          error);

                    waitToRetry();
                }
            }

            while ((!configToRetrieve.isEmpty()) && (keepTrying))
            {
                List<String>  configFailed = new ArrayList<>();

                for (String governanceEngineName : configToRetrieve)
                {
                    if (governanceEngineName != null)
                    {
                        /*
                         * Request the configuration for the governance engine.  If it fails just log the error but let the
                         * engine host server continue to start.  It is probably a temporary outage with the metadata server
                         * which can be resolved later.
                         */
                        try
                        {
                            GovernanceEngineHandler governanceEngineHandler = engineHandlers.getGovernanceEngineHandler(engineConfig);

                            if (governanceEngineHandler != null)
                            {
                                governanceEngineHandler.refreshConfig();

                                /*
                                 * Restart any services that were incomplete when the engine host shutdown.
                                 */
                                governanceEngineHandler.restartServices(governanceEngineHandler.getGovernanceEngineElement());

                                /*
                                 * Claim any approved engine actions
                                 */
                                governanceEngineHandler.startMissedEngineActions();
                            }
                        }
                        catch (Exception error)
                        {
                            auditLog.logException(actionDescription,
                                                  EngineHostServicesAuditCode.GOVERNANCE_ENGINE_NO_CONFIG.getMessageDefinition(governanceEngineName,
                                                                                                                               error.getClass().getName(),
                                                                                                                               error.getMessage()),
                                                  error.toString(),
                                                  error);

                            configFailed.add(governanceEngineName);
                        }
                    }
                }

                configToRetrieve = configFailed;

                waitToRetry();
            }

            waitToRetry();
        }
    }


    /**
     * Wait before retrying ...
     */
    private void waitToRetry()
    {
        final int  sleepTime = 2000000;

        if (keepTrying)
        {
            try
            {
                Thread.sleep(sleepTime);
            }
            catch (Exception error)
            {
                log.error("Ignored exception from sleep - probably ok", error);
            }
        }
    }


    /**
     * Stop the thread
     */
    public void stop()
    {
        keepTrying = false;
    }
}
