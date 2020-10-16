/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipengineservices.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.StewardshipEngine;


/**
 * StewardshipEngineClient is a client-side library for calling a specific stewardship engine with a stewardship server.
 */
public class StewardshipEngineClient extends StewardshipEngine
{
    private String        serverName;               /* Initialized in constructor */
    private String        serverPlatformRootURL;    /* Initialized in constructor */
    private String        stewardshipEngineName;    /* Initialized in constructor */
    private OCFRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();


    /**
     * Create a client-side object for calling a stewardship engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the stewardship engine is running.
     * @param serverName the name of the stewardship server where the stewardship engine is running
     * @param stewardshipEngineName the unique name of the stewardship engine.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public StewardshipEngineClient(String serverPlatformRootURL,
                                   String serverName,
                                   String stewardshipEngineName) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;
        this.stewardshipEngineName   = stewardshipEngineName;

        this.restClient = new OCFRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a client-side object for calling a stewardship engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the stewardship engine is running.
     * @param serverName the name of the stewardship server where the stewardship engine is running
     * @param stewardshipEngineName the unique name of the stewardship engine.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public StewardshipEngineClient(String serverPlatformRootURL,
                                   String serverName,
                                   String stewardshipEngineName,
                                   String userId,
                                   String password) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;
        this.stewardshipEngineName   = stewardshipEngineName;

        this.restClient = new OCFRESTClient(serverName, serverPlatformRootURL, userId, password);
    }

}
