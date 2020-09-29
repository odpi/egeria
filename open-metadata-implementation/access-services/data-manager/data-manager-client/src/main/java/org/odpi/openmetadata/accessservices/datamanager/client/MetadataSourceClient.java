/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.client;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerIntegratorInterface;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.ConnectedAssetClientBase;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * DatabaseManagerClient is the client for managing resources from a relational database server.
 */
public class DataManagerIntegratorClient extends ConnectedAssetClientBase implements DataManagerIntegratorInterface
{
    private DataManagerRESTClient restClient;               /* Initialized in constructor */

     private final String urlTemplatePrefix = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/integrators";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DataManagerIntegratorClient(String   serverName,
                                       String   serverPlatformRootURL,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, auditLog);

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public DataManagerIntegratorClient(String serverName,
                                       String serverPlatformRootURL) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL);

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL);
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
    public DataManagerIntegratorClient(String   serverName,
                                       String   serverPlatformRootURL,
                                       String   userId,
                                       String   password,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, auditLog);

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL, userId, password, auditLog);
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
    public DataManagerIntegratorClient(String serverName,
                                       String serverPlatformRootURL,
                                       String userId,
                                       String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL);

        this.restClient = new DataManagerRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Create a new client that is to be used within an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public DataManagerIntegratorClient(String                serverName, 
                                       String                serverPlatformURLRoot, 
                                       DataManagerRESTClient restClient, 
                                       int                   maxPageSize, 
                                       AuditLog              auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
        
        this.restClient = restClient;
    }


    /* ========================================================
     * The integrator represents this integration processing that is calling the data manager OMAS
     */

    /**
     * Create information about the integration daemon that is managing the acquisition of metadata from the
     * data manager.  Typically this is Egeria's data manager integrator OMIS.
     *
     * @param userId calling user
     * @param integratorCapabilities description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String  createDataManagerIntegrator(String                                userId,
                                               SoftwareServerCapabilitiesProperties  integratorCapabilities) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        final String methodName                  = "createDataManagerIntegrator";
        final String propertiesParameterName     = "integratorCapabilities";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(integratorCapabilities, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(integratorCapabilities.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformRootURL + urlTemplatePrefix;

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  integratorCapabilities,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Retrieve the unique identifier of the integration daemon.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String  getDataManagerIntegratorGUID(String  userId,
                                                String  qualifiedName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName                  = "getDataManagerIntegratorGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformRootURL + urlTemplatePrefix + "/by-name/{2}";

        GUIDResponse restResult = restClient.callGUIDGetRESTCall(methodName,
                                                                  urlTemplate,
                                                                  serverName,
                                                                  userId,
                                                                  qualifiedName);

        return restResult.getGUID();
    }
}
