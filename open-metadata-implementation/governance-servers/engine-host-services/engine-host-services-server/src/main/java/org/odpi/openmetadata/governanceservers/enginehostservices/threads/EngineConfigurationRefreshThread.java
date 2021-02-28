/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.threads;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineEventClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.listener.GovernanceEngineOutTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EngineConfigurationRefreshThread is the class responsible for establishing the listener for configuration
 * updates.  It runs as a separate thread until the listener is registered with the Governance Engine OMAS.
 * At that point, the listener is able to process incoming configuration updates and this thread can end.
 */
public class EngineConfigurationRefreshThread implements Runnable
{
    private Map<String, GovernanceEngineHandler> engineHandlers;
    private GovernanceEngineConfigurationClient  configurationClient;
    private GovernanceEngineEventClient          eventClient;
    private AuditLog                             auditLog;
    private String                               localServerUserId;
    private String                               localServerName;
    private String                               accessServiceServerName;
    private String                               accessServiceRootURL;

    private volatile boolean                     keepTrying = true;

    private static final Logger log = LoggerFactory.getLogger(EngineConfigurationRefreshThread.class);


    /**
     * The constructor takes details of the governance engine handlers needed by the listener and the information
     * needed to log errors if the metadata server is not available.
     *
     * @param engineHandlers list of governance engine handlers running locally mapped to their names
     * @param configurationClient client that the listener is to be registered with
     * @param eventClient client for accessing the Governance Engine OMAS OutTopic
     * @param auditLog logging destination
     * @param localServerUserId userId for configuration requests
     * @param localServerName this server's name
     * @param accessServiceServerName metadata server's name
     * @param accessServiceRootURL platform location for metadata server
     */
    public EngineConfigurationRefreshThread(Map<String, GovernanceEngineHandler> engineHandlers,
                                            GovernanceEngineConfigurationClient  configurationClient,
                                            GovernanceEngineEventClient          eventClient,
                                            AuditLog                             auditLog,
                                            String                               localServerUserId,
                                            String                               localServerName,
                                            String                               accessServiceServerName,
                                            String                               accessServiceRootURL)
    {
        this.engineHandlers = new HashMap<>(engineHandlers);
        this.configurationClient     = configurationClient;
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

        boolean  listenerRegistered = false;
        List<GovernanceEngineHandler>  configToRetrieve;

        if (engineHandlers != null)
        {
            configToRetrieve = new ArrayList<>(engineHandlers.values());
        }
        else
        {
            configToRetrieve = new ArrayList<>();
        }

        while ((! listenerRegistered) && (configToRetrieve.size() != 0) && (keepTrying))
        {
            while ((! listenerRegistered) && (keepTrying))
            {
                try
                {
                    eventClient.registerListener(localServerUserId, new GovernanceEngineOutTopicListener(engineHandlers, auditLog));
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
                catch (Throwable error)
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

            while ((configToRetrieve.size() != 0) && (keepTrying))
            {
                List<GovernanceEngineHandler>  configFailed = new ArrayList<>();

                for (GovernanceEngineHandler engineHandler : configToRetrieve)
                {
                    if (engineHandler != null)
                    {
                        /*
                         * Request the configuration for the governance engine.  If it fails just log the error but let the
                         * engine host server continue to start.  It is probably a temporary outage with the metadata server
                         * which can be resolved later.
                         */
                        try
                        {
                            engineHandler.refreshConfig();
                        }
                        catch (Throwable error)
                        {
                            auditLog.logException(actionDescription,
                                                  EngineHostServicesAuditCode.GOVERNANCE_ENGINE_NO_CONFIG.getMessageDefinition(engineHandler.getGovernanceEngineName(),
                                                                                                                         error.getClass().getName(),
                                                                                                                         error.getMessage()),
                                                  error.toString(),
                                                  error);

                            configFailed.add(engineHandler);
                        }
                    }
                }

                configToRetrieve = configFailed;

                waitToRetry();
            }
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


    public void stop()
    {
        keepTrying = false;
    }
}
