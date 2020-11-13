/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.discoveryengineservices.handlers;

import org.odpi.openmetadata.accessservices.discoveryengine.client.DiscoveryConfigurationClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.ffdc.DiscoveryEngineServicesAuditCode;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.listener.DiscoveryConfigurationRefreshListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DiscoveryConfigurationRefreshHandler is the class responsible for establishing the listener for configuration
 * updates.  It runs as a separate thread until the listener is registered with the Discovery Engine OMAS.
 * At that point, the listener is able to process incoming configuration updates and this thread can end.
 */
public class DiscoveryConfigurationRefreshHandler implements Runnable
{
    private Map<String, DiscoveryEngineHandler> discoveryEngineHandlers;
    private DiscoveryConfigurationClient        configurationClient;
    private AuditLog                            auditLog;
    private String                              localServerUserId;
    private String                              localServerName;
    private String                              accessServiceServerName;
    private String                              accessServiceRootURL;

    private static final Logger log = LoggerFactory.getLogger(DiscoveryConfigurationRefreshHandler.class);


    /**
     * The constructor takes details of the discovery engine handlers needed by the listener and the information
     * needed to log errors if the metadata server is not available.
     *
     * @param discoveryEngineHandlers list of discovery engine handlers running locally mapped to their names.
     * @param configurationClient client that the listener is to be registered with.
     * @param auditLog logging destination
     * @param localServerUserId userId for configuration requests
     * @param localServerName this server's name
     * @param accessServiceServerName metadata server's name
     * @param accessServiceRootURL platform location for metadata server.
     */
    public DiscoveryConfigurationRefreshHandler(Map<String, DiscoveryEngineHandler> discoveryEngineHandlers,
                                                DiscoveryConfigurationClient        configurationClient,
                                                AuditLog                            auditLog,
                                                String                              localServerUserId,
                                                String                              localServerName,
                                                String                              accessServiceServerName,
                                                String                              accessServiceRootURL)
    {
        this.discoveryEngineHandlers = new HashMap<>(discoveryEngineHandlers);
        this.configurationClient     = configurationClient;
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
        List<DiscoveryEngineHandler>  configToRetrieve;

        if (discoveryEngineHandlers != null)
        {
            configToRetrieve = new ArrayList<>(discoveryEngineHandlers.values());
        }
        else
        {
            configToRetrieve = new ArrayList<>();
        }

        while ((! listenerRegistered) && (configToRetrieve.size() != 0))
        {
            while (!listenerRegistered)
            {
                try
                {
                    configurationClient.registerListener(localServerUserId, new DiscoveryConfigurationRefreshListener(discoveryEngineHandlers,
                                                                                                                      auditLog));
                    listenerRegistered = true;

                    auditLog.logMessage(actionDescription,
                                        DiscoveryEngineServicesAuditCode.CONFIGURATION_LISTENER_REGISTERED.getMessageDefinition(localServerName,
                                                                                                                                accessServiceServerName));
                }
                catch (UserNotAuthorizedException error)
                {
                    auditLog.logException(actionDescription,
                                          DiscoveryEngineServicesAuditCode.SERVER_NOT_AUTHORIZED.getMessageDefinition(localServerName,
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
                                          DiscoveryEngineServicesAuditCode.NO_CONFIGURATION_LISTENER.getMessageDefinition(localServerName,
                                                                                                                          accessServiceServerName,
                                                                                                                          error.getClass().getName(),
                                                                                                                          error.getMessage()),
                                          error);

                    waitToRetry();
                }
            }

            while (configToRetrieve.size() != 0)
            {
                List<DiscoveryEngineHandler>  configFailed = new ArrayList<>();

                for (DiscoveryEngineHandler discoveryEngineHandler : configToRetrieve)
                {
                    if (discoveryEngineHandler != null)
                    {
                        /*
                         * Request the configuration for the discovery server.  If it fails just log the error but let the
                         * discovery server continue to start.  It is probably a temporary outage with the metadata server
                         * which can be resolved later.
                         */
                        try
                        {
                            discoveryEngineHandler.refreshConfig();
                        }
                        catch (Throwable error)
                        {
                            auditLog.logException(actionDescription,
                                                  DiscoveryEngineServicesAuditCode.DISCOVERY_ENGINE_NO_CONFIG.getMessageDefinition(discoveryEngineHandler.getDiscoveryEngineName(),
                                                                                                                                   error.getClass().getName(),
                                                                                                                                   error.getMessage()),
                                                  error.toString(),
                                                  error);

                            configFailed.add(discoveryEngineHandler);
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
