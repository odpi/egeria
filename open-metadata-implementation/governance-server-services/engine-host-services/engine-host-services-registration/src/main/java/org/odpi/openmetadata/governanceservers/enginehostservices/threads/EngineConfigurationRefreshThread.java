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
import org.odpi.openmetadata.governanceservers.enginehostservices.listener.OpenGovernanceOutTopicListener;
import org.odpi.openmetadata.governanceservers.enginehostservices.listener.OpenMetadataOutTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    /*
     * How long to wait after something failed - typically the metadata access server not being reachable yet.
     */
    private static final long retryIntervalMS = 10000L;

    /*
     * How often this engine checks for work it has not started, and for configuration that has changed.
     *
     * The engine normally hears about a new engine action on the out topic, and that is what makes it prompt.
     * This cycle is what makes it reliable, and what makes the out topic optional.
     *
     * Reliable, because an event that is missed - because the engine's subscription was being established at
     * the moment it was published, or because the action was read in the instant before it was approved -
     * leaves an engine action sitting at APPROVED with nothing scheduled to look at it again.  Nothing else in
     * the engine host retries, so however rare that is, without this cycle it is permanent.
     *
     * Optional, because an engine host in a deployment with no event bus hears nothing at all.  Everything it
     * would have been told is found by this cycle instead, so the engine host runs without Kafka; it just
     * reacts a little later.
     *
     * A pass costs one paged query per engine for the waiting work, and the configuration reload it can lead
     * to is throttled separately inside refreshConfig - so running this often is cheap.
     */
    private static final long engineActionSweepIntervalMS = 5000L;

    private final EngineConfig            engineConfig;
    private final GovernanceEngineMap     engineHandlers;
    private final OpenMetadataEventClient omfEventClient;
    private final OpenMetadataEventClient gafEventClient;
    private final AuditLog                auditLog;
    private final String                  localServerName;
    private volatile boolean              keepTrying = true;

    private static final Logger log = LoggerFactory.getLogger(EngineConfigurationRefreshThread.class);


    /**
     * The constructor takes details of the governance engine handlers needed by the listener and the information
     * needed to log errors if the metadata server is not available.
     *
     * @param engineHandlers list of governance engine handlers running locally mapped to their names
     * @param omfEventClient client for accessing the Open Metadata OutTopic
     * @param gafEventClient client for accessing the Open Governance OutTopic
     * @param auditLog logging destination
     * @param localServerName this server's name
     */
    public EngineConfigurationRefreshThread(EngineConfig             engineConfig,
                                            GovernanceEngineMap      engineHandlers,
                                            OpenMetadataEventClient  omfEventClient,
                                            OpenMetadataEventClient  gafEventClient,
                                            AuditLog                 auditLog,
                                            String                   localServerName)
    {
        this.engineConfig    = engineConfig;
        this.engineHandlers  = engineHandlers;
        this.omfEventClient  = omfEventClient;
        this.gafEventClient  = gafEventClient;
        this.auditLog        = auditLog;
        this.localServerName = localServerName;
    }


    /**
     * Method that runs when the thread is started.
     */
    @Override
    public void run()
    {
        final String actionDescription = "Retrieve governance engine configuration";

        while (keepTrying)
        {
            /*
             * Listening for events is attempted, but the engine does not wait for it to succeed.
             *
             * This used to loop until the listener registered, which made an event bus a requirement: in a
             * deployment with no Kafka there is no out topic to register against, so registration failed
             * every time and this thread never reached the configuration retrieval below.  The engines stayed
             * in CONFIGURING for ever and the engine host, though running, could do nothing at all.
             *
             * Events are an optimisation - they are what makes the engine react within a second rather than
             * within a sweep.  Everything the engine needs to know it can also find by asking, which is what
             * the cycle at the end of this method does.  So registration is tried, its outcome is recorded,
             * and the engine gets on with its work either way; if it did not succeed it is tried again on
             * each pass of that cycle, so an event bus that appears later is picked up without a restart.
             */
            boolean listenerRegistered = registerListeners(true);

            /*
             * There is one of these threads per governance engine configured on this server, and it is
             * responsible for its own engine only - the one named by engineConfig.  This used to loop over
             * every engine name on the server while refreshing engineConfig on each pass, which refreshed
             * this thread's engine once per configured engine and, when the refresh failed, reported the
             * failure against whichever other engine the loop happened to be on.  An operator reading the
             * audit log was sent to an engine that had nothing wrong with it.
             */
            boolean configRetrieved = false;

            while ((! configRetrieved) && (keepTrying))
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

                    configRetrieved = true;
                }
                catch (Exception error)
                {
                    auditLog.logException(actionDescription,
                                          EngineHostServicesAuditCode.GOVERNANCE_ENGINE_NO_CONFIG.getMessageDefinition(engineConfig.getEngineQualifiedName(),
                                                                                                                       error.getClass().getName(),
                                                                                                                       error.getMessage()),
                                          error.toString(),
                                          error);

                    waitToRetry();
                }
            }

            /*
             * The engine is configured and any services interrupted by a previous shutdown have been
             * restarted.  From here on this thread keeps the engine current on a short cycle, asking for two
             * things on each pass:
             *
             *   - refreshConfig, which throttles itself, so on most passes it does nothing and once the
             *     configuration is old enough it reloads the engine definition and its governance services;
             *   - the sweep for engine actions that were requested for this engine and have not been started,
             *     which is wanted on every pass and so is asked for separately.
             *
             * The reason this cycle is short is that it is what the engine host falls back on when it does not
             * hear an event.  An engine action normally arrives on the out topic, which is prompt; this is
             * what makes it reliable.  It also means an engine host does not need an event bus at all - with
             * no Kafka in the deployment, nothing arrives on a topic and every engine action and configuration
             * change is picked up by this cycle instead.  The engine host is then slower to react, by at most
             * the interval below, but it works.
             *
             * restartServices is deliberately not called again here: interrupted services are restarted once,
             * when this thread first retrieves the engine's configuration above.
             */
            while (keepTrying)
            {
                if (! listenerRegistered)
                {
                    /*
                     * Quietly, because this runs continuously: the first attempt has already been reported.
                     */
                    listenerRegistered = registerListeners(false);
                }

                try
                {
                    GovernanceEngineHandler governanceEngineHandler = engineHandlers.getGovernanceEngineHandler(engineConfig);

                    if (governanceEngineHandler != null)
                    {
                        governanceEngineHandler.refreshConfig();

                        /*
                         * The sweep is asked for explicitly, because refreshConfig is configuration only and
                         * throttles itself - so on most passes it does nothing at all, and the sweep would be
                         * throttled along with it.
                         */
                        governanceEngineHandler.startMissedEngineActions();
                    }
                }
                catch (Exception error)
                {
                    /*
                     * Logged at debug rather than as an audit log entry: this runs continuously, so a
                     * problem that persisted would fill the audit log with the same message.  Anything
                     * missed is still waiting and the next pass tries again.
                     */
                    log.debug("Keeping governance engine " + engineConfig.getEngineQualifiedName() + " current failed: " +
                                      error.getClass().getName() + " " + error.getMessage(), error);
                }

                waitForNextSweep();
            }
        }
    }


    /**
     * Try to register this engine's listeners on the metadata access server's out topics.
     * <br>
     * Failure is not fatal - see the note in {@link #run()} for why the engine carries on without them.
     *
     * @param reportFailure should a failure be recorded in the audit log?  True for the first attempt, so an
     *                      operator can see that this engine is working without events and why; false for the
     *                      retries that follow, which would otherwise repeat the same message indefinitely
     * @return true if the listeners are registered
     */
    private boolean registerListeners(boolean reportFailure)
    {
        final String actionDescription = "Register configuration listener";

        try
        {
            omfEventClient.registerListener(engineConfig.getEngineUserId(), new OpenMetadataOutTopicListener(engineConfig, engineHandlers, auditLog));
            gafEventClient.registerListener(engineConfig.getEngineUserId(), new OpenGovernanceOutTopicListener(engineConfig, engineHandlers, auditLog));

            auditLog.logMessage(actionDescription,
                                EngineHostServicesAuditCode.CONFIGURATION_LISTENER_REGISTERED.getMessageDefinition(localServerName,
                                                                                                                   engineConfig.getEngineQualifiedName(),
                                                                                                                   engineConfig.getOMAGServerName()));
            return true;
        }
        catch (UserNotAuthorizedException error)
        {
            if (reportFailure)
            {
                auditLog.logException(actionDescription,
                                      EngineHostServicesAuditCode.SERVER_NOT_AUTHORIZED.getMessageDefinition(localServerName,
                                                                                                             engineConfig.getOMAGServerName(),
                                                                                                             engineConfig.getOMAGServerPlatformRootURL(),
                                                                                                             engineConfig.getEngineUserId(),
                                                                                                             error.getReportedErrorMessage()),
                                      error);
            }
            else
            {
                log.debug("Still unable to register the out topic listeners for " + engineConfig.getEngineQualifiedName(), error);
            }
        }
        catch (Exception error)
        {
            if (reportFailure)
            {
                auditLog.logException(actionDescription,
                                      EngineHostServicesAuditCode.NO_CONFIGURATION_LISTENER.getMessageDefinition(localServerName,
                                                                                                                 engineConfig.getOMAGServerName(),
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage()),
                                      error);
            }
            else
            {
                log.debug("Still unable to register the out topic listeners for " + engineConfig.getEngineQualifiedName(), error);
            }
        }

        return false;
    }


    /**
     * Wait before retrying something that failed - reaching the metadata access server, most often, which is
     * worth leaving a gap before trying again.
     */
    private void waitToRetry()
    {
        this.sleep(retryIntervalMS);
    }


    /**
     * Wait between sweeps for engine actions this engine has not started.
     */
    private void waitForNextSweep()
    {
        this.sleep(engineActionSweepIntervalMS);
    }


    /**
     * Pause this thread, unless it is stopping.
     *
     * @param sleepTimeMS how long to pause for
     */
    private void sleep(long sleepTimeMS)
    {
        if (keepTrying)
        {
            try
            {
                Thread.sleep(sleepTimeMS);
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
