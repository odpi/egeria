/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.threads;


import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataEventClient;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationGroupHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.listener.OpenMetadataOutTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * GroupConfigurationRefreshThread is the class responsible for establishing the listener for configuration
 * updates.  It runs as a separate thread until the listener is registered with the Open Metadata Services.
 * At that point, the listener is able to process incoming configuration updates and this thread can end.
 */
public class GroupConfigurationRefreshThread implements Runnable
{
    private final IntegrationGroupConfig        group;
    private final String                        groupName;
    private final IntegrationGroupHandler       groupHandler;
    private final EgeriaOpenMetadataEventClient eventClient;
    private final AuditLog                      auditLog;
    private final String                        localServerUserId;
    private final String                        localServerName;
    private final String                        accessServiceServerName;
    private final String                        accessServiceRootURL;
    private final GovernanceConfigurationClient configurationClient;

    private volatile boolean keepTrying = true;

    private static final Logger log = LoggerFactory.getLogger(GroupConfigurationRefreshThread.class);


    /**
     * The constructor takes details of the integration group handlers needed by the listener and the information
     * needed to log errors if the metadata server is not available.
     *
     * @param group configuration for the group
     * @param groupHandler integration group handler
     * @param eventClient client for accessing the Governance Group OMAS OutTopic
     * @param auditLog logging destination
     * @param localServerUserId userId for configuration requests
     * @param localServerName this server's name
     * @param accessServiceServerName metadata server's name
     * @param accessServiceRootURL platform location for metadata server
     * @param maxPageSize max results
     */
    public GroupConfigurationRefreshThread(IntegrationGroupConfig        group,
                                           IntegrationGroupHandler       groupHandler,
                                           EgeriaOpenMetadataEventClient eventClient,
                                           AuditLog                      auditLog,
                                           String                        localServerUserId,
                                           String                        localServerName,
                                           String                        accessServiceServerName,
                                           String                        accessServiceRootURL,
                                           int                           maxPageSize) throws InvalidParameterException
    {
        this.group                   = group;
        this.groupName               = group.getIntegrationGroupQualifiedName();
        this.groupHandler            = groupHandler;
        this.eventClient             = eventClient;
        this.auditLog                = auditLog;
        this.localServerUserId       = localServerUserId;
        this.localServerName         = localServerName;
        this.accessServiceServerName = accessServiceServerName;
        this.accessServiceRootURL    = accessServiceRootURL;

        this.configurationClient = new GovernanceConfigurationClient(group.getOMAGServerName(),
                                                                     group.getOMAGServerPlatformRootURL(),
                                                                     group.getSecretsStoreProvider(),
                                                                     group.getSecretsStoreLocation(),
                                                                     group.getSecretsStoreCollection(),
                                                                     maxPageSize,
                                                                     auditLog);
    }


    /**
     * Method that runs when the thread is started.
     */
    @Override
    public void run()
    {
        final String actionDescription = "Register configuration listener";

        boolean  listenerRegistered = false;


        while (keepTrying)
        {
            /*
             * First register a listener for the group's configuration.
             */
            while ((! listenerRegistered) && (keepTrying))
            {
                try
                {
                    eventClient.registerListener(localServerUserId,
                                                 new OpenMetadataOutTopicListener(groupName,
                                                                                  groupHandler,
                                                                                  configurationClient,
                                                                                  localServerUserId,
                                                                                  auditLog));
                    listenerRegistered = true;

                    auditLog.logMessage(actionDescription,
                                        IntegrationDaemonServicesAuditCode.CONFIGURATION_LISTENER_REGISTERED.getMessageDefinition(localServerName,
                                                                                                                           accessServiceServerName));
                }
                catch (UserNotAuthorizedException error)
                {
                    auditLog.logException(actionDescription,
                                          IntegrationDaemonServicesAuditCode.SERVER_NOT_AUTHORIZED.getMessageDefinition(localServerName,
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
                                          IntegrationDaemonServicesAuditCode.NO_CONFIGURATION_LISTENER.getMessageDefinition(localServerName,
                                                                                                                            accessServiceServerName,
                                                                                                                            error.getClass().getName(),
                                                                                                                            error.getMessage()),
                                          error);

                    waitToRetry();
                }
            }

            while (keepTrying)
            {
                /*
                 * Request the configuration for the governance group.  If it fails just log the error but let the
                 * integration daemon server continue to start.  It is probably a temporary outage with the metadata server
                 * which can be resolved later.
                 */
                try
                {
                    groupHandler.refreshConfig();
                }
                catch (InvalidParameterException error)
                {
                    auditLog.logMessage(actionDescription,
                                        IntegrationDaemonServicesAuditCode.INTEGRATION_GROUP_NO_CONFIG.getMessageDefinition(groupHandler.getIntegrationGroupName(),
                                                                                                                            error.getClass().getName(),
                                                                                                                            error.getMessage()),
                                        error.toString());
                }
                catch (Exception error)
                {
                    auditLog.logException(actionDescription,
                                          IntegrationDaemonServicesAuditCode.INTEGRATION_GROUP_NO_CONFIG.getMessageDefinition(groupHandler.getIntegrationGroupName(),
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                          error.toString(),
                                          error);
                }

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
     * Strop the thread
     */
    public void stop()
    {
        keepTrying = false;
    }
}
