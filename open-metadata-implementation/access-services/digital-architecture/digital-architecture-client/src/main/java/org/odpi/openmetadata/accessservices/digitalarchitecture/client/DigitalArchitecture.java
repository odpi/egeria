/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;


/**
 * AssetOwner provides the generic client-side interface for the Asset Owner Open Metadata Access Service (OMAS).
 * There are other clients that provide specialized methods for specific types of Asset.
 *
 * This client is initialized with the URL and name of the server that is running the Asset Owner OMAS.
 * This server is responsible for locating and managing the asset owner's definitions exchanged with this client.
 */
public class DigitalArchitecture

{
    DigitalArchitectureRESTClient restClient;               /* Initialized in constructor */

    private static final String  serviceURLName = "digital-architecture";

    protected String   serverName;               /* Initialized in constructor */
    protected String   serverPlatformRootURL;    /* Initialized in constructor */
    protected AuditLog auditLog;                 /* Initialized in constructor */

    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    protected static NullRequestBody nullRequestBody = new NullRequestBody();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DigitalArchitecture(String   serverName,
                               String   serverPlatformRootURL,
                               AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.auditLog = auditLog;

        this.restClient = new DigitalArchitectureRESTClient(serverName, serverPlatformRootURL, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DigitalArchitecture(String serverName,
                               String serverPlatformRootURL) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;

        this.restClient = new DigitalArchitectureRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DigitalArchitecture(String   serverName,
                               String   serverPlatformRootURL,
                               String   userId,
                               String   password,
                               AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.auditLog = auditLog;

        this.restClient = new DigitalArchitectureRESTClient(serverName, serverPlatformRootURL, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DigitalArchitecture(String serverName,
                               String serverPlatformRootURL,
                               String userId,
                               String password) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformRootURL, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;

        this.restClient = new DigitalArchitectureRESTClient(serverName, serverPlatformRootURL, userId, password);
    }
}
