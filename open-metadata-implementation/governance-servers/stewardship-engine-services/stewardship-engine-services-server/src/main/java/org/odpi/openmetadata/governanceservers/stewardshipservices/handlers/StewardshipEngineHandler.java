/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipservices.handlers;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

import org.odpi.openmetadata.governanceservers.stewardshipengineservices.properties.StewardshipEngineSummary;


/**
 * The StewardshipEngineHandler is responsible for running stewardship services on demand.  It is initialized
 * with the configuration for the stewardship services it supports along with the clients to the
 * asset properties store and annotations store.
 */
public class StewardshipEngineHandler
{
    private String                       serverName;               /* Initialized in constructor */
    private String                       serverUserId;             /* Initialized in constructor */
    private AuditLog                     auditLog;                 /* Initialized in constructor */
    private int                          maxPageSize;              /* Initialized in constructor */

    private String                    stewardshipEngineName;         /* Initialized in constructor */
    private String                    stewardshipEngineGUID         = null;


    /**
     * Create a client-side object for calling a stewardship engine.
     *
     * @param stewardshipEngineName the unique identifier of the stewardship engine.
     * @param serverName the name of the stewardship server where the stewardship engine is running
     * @param serverUserId user id for the server to use
     * @param auditLog logging destination
     * @param maxPageSize maximum number of results that can be returned in a single request
     */
    public StewardshipEngineHandler(String                       stewardshipEngineName,
                                    String                       serverName,
                                    String                       serverUserId,
                                    AuditLog                     auditLog,
                                    int                          maxPageSize)
    {
        this.stewardshipEngineName = stewardshipEngineName;
        this.serverName = serverName;
        this.serverUserId = serverUserId;

        this.auditLog = auditLog;
        this.maxPageSize = maxPageSize;
    }


    /**
     * Return the stewardship Engine name - used for error logging.
     *
     * @return stewardship engine name
     */
    String getStewardshipEngineName()
    {
        return stewardshipEngineName;
    }


    /**
     * Return a summary of the stewardship engine
     *
     * @return stewardship engine summary
     */
    public StewardshipEngineSummary getSummary()
    {
        StewardshipEngineSummary mySummary = new StewardshipEngineSummary();

        mySummary.setStewardshipEngineName(stewardshipEngineName);
        mySummary.setStewardshipEngineGUID(stewardshipEngineGUID);


        return mySummary;
    }

    /**
     * Request that the stewardship engine refresh its configuration by calling the metadata server.
     * This request ensures that the latest configuration is in use.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    public void refreshConfig() throws InvalidParameterException,
                                       UserNotAuthorizedException,
                                       PropertyServerException
    {
        final String methodName = "refreshConfig";

        // todo
    }


    /**
     * Request that the stewardship engine refreshes its configuration for all stewardship services
     * by calling the metadata server. This request just ensures that the latest configuration
     * is in use.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    private void refreshAllServiceConfig() throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException
    {
        // todo
    }


    /**
     * Request that the stewardship engine refreshes its configuration for a single stewardship service
     * by calling the metadata server. This request just ensures that the latest configuration
     * is in use.
     *
     * @param registeredStewardshipServiceGUID unique identifier of the SupportedStewardshipService relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user id not allowed to access configuration
     * @throws PropertyServerException problem in configuration server
     */
    public void refreshServiceConfig(String   registeredStewardshipServiceGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        // todo
    }

}
