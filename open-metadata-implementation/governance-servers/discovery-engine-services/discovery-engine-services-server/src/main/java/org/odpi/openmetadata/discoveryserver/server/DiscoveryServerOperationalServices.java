/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.server;

import org.odpi.openmetadata.accessservices.discoveryengine.client.DiscoveryConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.DiscoveryServerConfig;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryConfigurationServer;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DiscoveryServerOperationalServices is responsible for controlling the startup and shutdown of
 * a discovery server instance.
 */
public class DiscoveryServerOperationalServices
{
    private static final Logger log = LoggerFactory.getLogger(DiscoveryServerOperationalServices.class);

    private String                localServerName;               /* Initialized in constructor */
    private String                localServerUserId;             /* Initialized in constructor */
    private String                localServerPassword;           /* Initialized in constructor */
    private int                   maxPageSize;                   /* Initialized in constructor */
    private OMRSAuditLog          auditLog              = null;
    private DiscoveryServerConfig discoveryServerConfig = null;



    /**
     * Constructor used at server startup.
     *
     * @param localServerName name of the local server
     * @param localServerUserId user id for this server to use if sending REST requests and
     *                          processing inbound messages.
     * @param localServerPassword password for this server to use if sending REST requests.
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     */
    public DiscoveryServerOperationalServices(String                   localServerName,
                                              String                   localServerUserId,
                                              String                   localServerPassword,
                                              int                      maxPageSize)
    {
        this.localServerName       = localServerName;
        this.localServerUserId     = localServerUserId;
        this.localServerPassword   = localServerPassword;
        this.maxPageSize           = maxPageSize;
    }


    /**
     * Initialize the service.
     *
     * @param configuration config properties
     * @param auditLog destination for audit log messages.
     */
    public void initialize(DiscoveryServerConfig configuration,
                           OMRSAuditLog          auditLog)
    {
        this.discoveryServerConfig = configuration;
        this.auditLog              = auditLog;

        DiscoveryConfigurationClient configurationClient;
        if ((localServerName != null) && (localServerPassword != null))
        {
            configurationClient = new DiscoveryConfigurationClient(configuration.getAccessServiceServerName(),
                                                                   configuration.getAccessServiceRootURL(),
                                                                   localServerUserId,
                                                                   localServerPassword);
        }
        else
        {
             configurationClient = new DiscoveryConfigurationClient(configuration.getAccessServiceServerName(),
                                                                    configuration.getAccessServiceRootURL());
        }

        // TODO
    }


    /**
     * Shutdown the service.
     *
     * @param permanent is the service going away?
     */
    public void terminate(boolean  permanent)
    {

    }
}
