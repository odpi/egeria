/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipservices.admin;

import org.odpi.openmetadata.adminservices.configuration.properties.StewardshipEngineServicesConfig;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * StewardshipOperationalServices is responsible for controlling the startup and shutdown of
 * of the stewardship services.
 */
public class StewardshipOperationalServices
{
    private static final Logger log = LoggerFactory.getLogger(StewardshipOperationalServices.class);

    private String                          localServerName;               /* Initialized in constructor */
    private String                          localServerUserId;             /* Initialized in constructor */
    private int                             maxPageSize;                   /* Initialized in constructor */
    private OMRSAuditLog                    auditLog                        = null;
    private StewardshipEngineServicesConfig stewardshipEngineServicesConfig = null;



    /**
     * Constructor used at server startup.
     *
     * @param localServerName name of the local server
     * @param localServerUserId user id for this server to use if processing inbound messages.
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     */
    public StewardshipOperationalServices(String                   localServerName,
                                          String                   localServerUserId,
                                          int                      maxPageSize)
    {
        this.localServerName       = localServerName;
        this.localServerUserId     = localServerUserId;
        this.maxPageSize           = maxPageSize;
    }


    /**
     * Initialize the service.
     *
     * @param configuration config properties
     * @param auditLog destination for audit log messages.
     */
    public void initialize(StewardshipEngineServicesConfig configuration,
                           OMRSAuditLog                 auditLog)
    {
        this.stewardshipEngineServicesConfig = configuration;
        this.auditLog                        = auditLog;
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
