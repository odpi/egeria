/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.threads;

import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceServerEventClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.EngineActionStatus;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.listener.GovernanceServerOutTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * EngineConfigurationRefreshThread is the class responsible for establishing the listener for configuration
 * updates.  It runs as a separate thread until the listener is registered with the Governance Engine OMAS.
 * At that point, the listener is able to process incoming configuration updates and this thread can end.
 */
public class EngineConfigurationRefreshThread implements Runnable
{
    private final GovernanceEngineMap         engineHandlers;
    private final GovernanceServerEventClient eventClient;
    private final AuditLog                    auditLog;
    private final String                      localServerUserId;
    private final String                      localServerName;
    private final String                      accessServiceServerName;
    private final String                      accessServiceRootURL;

    private volatile boolean                  keepTrying = true;

    private static final Logger log = LoggerFactory.getLogger(EngineConfigurationRefreshThread.class);


    /**
     * The constructor takes details of the governance engine handlers needed by the listener and the information
     * needed to log errors if the metadata server is not available.
     *
     * @param engineHandlers list of governance engine handlers running locally mapped to their names
     * @param eventClient client for accessing the Governance Server OMAS OutTopic
     * @param auditLog logging destination
     * @param localServerUserId userId for configuration requests
     * @param localServerName this server's name
     * @param accessServiceServerName metadata server's name
     * @param accessServiceRootURL platform location for metadata server
     */
    public EngineConfigurationRefreshThread(GovernanceEngineMap                  engineHandlers,
                                            GovernanceServerEventClient          eventClient,
                                            AuditLog                             auditLog,
                                            String                               localServerUserId,
                                            String                               localServerName,
                                            String                               accessServiceServerName,
                                            String                               accessServiceRootURL)
    {
        this.engineHandlers          = engineHandlers;
        this.eventClient             = eventClient;
        this.auditLog                = auditLog;
        this.localServerUserId       = localServerUserId;
        this.localServerName         = localServerName;
        this.accessServiceServerName = accessServiceServerName;
        this.accessServiceRootURL    = accessServiceRootURL;
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
                    eventClient.registerListener(localServerUserId, new GovernanceServerOutTopicListener(engineHandlers, auditLog));
                    listenerRegistered = true;

                    auditLog.logMessage(actionDescription,
                                        EngineHostServicesAuditCode.CONFIGURATION_LISTENER_REGISTERED.getMessageDefinition(localServerName,
                                                                                                                           accessServiceServerName));
                }
                catch (UserNotAuthorizedException error)
                {
                    auditLog.logException(actionDescription,
                                          EngineHostServicesAuditCode.SERVER_NOT_AUTHORIZED.getMessageDefinition(localServerName,
                                                                                                                 accessServiceServerName,
                                                                                                                 accessServiceRootURL,
                                                                                                                 localServerUserId,
                                                                                                                 error.getReportedErrorMessage()),
                                          error);
                    waitToRetry();
                }
                catch (Exception error)
                {
                    auditLog.logException(actionDescription,
                                          EngineHostServicesAuditCode.NO_CONFIGURATION_LISTENER.getMessageDefinition(localServerName,
                                                                                                                     accessServiceServerName,
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
                            GovernanceEngineHandler governanceEngineHandler = engineHandlers.getGovernanceEngineHandler(governanceEngineName);

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
