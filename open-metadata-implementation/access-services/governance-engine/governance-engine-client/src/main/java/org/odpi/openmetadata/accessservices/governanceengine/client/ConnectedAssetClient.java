/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.client;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.ConnectedAssetClientBase;

/**
 * ConnectedAssetClient is used by applications and tools as a factory for Open
 * Connector Framework (OCF) connectors.  The configuration for the connectors is managed as open metadata in
 * a Connection definition.  The caller to the ConnectedAssetClient passes either the name, GUID or URL for the
 * connection to the appropriate method to retrieve a connector.  The ConnectedAssetClient retrieves the connection
 * from the metadata repository and creates an appropriate connector as described the connection and
 * returns it to the caller.
 *
 * The ConnectedAssetClient supports access to the asset properties through the connector.
 */
public class ConnectedAssetClient extends ConnectedAssetClientBase
{
    private static final String  serviceURLName = "governance-engine";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException null URL or server name
     */
    public ConnectedAssetClient(String   serverName,
                                String   serverPlatformURLRoot,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException null URL or server name
     */
    public ConnectedAssetClient(String serverName,
                                String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     * @throws InvalidParameterException null URL or server name
     */
    public ConnectedAssetClient(String     serverName,
                                String     serverPlatformURLRoot,
                                String     userId,
                                String     password,
                                AuditLog   auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException null URL or server name
     */
    public ConnectedAssetClient(String     serverName,
                                String     serverPlatformURLRoot,
                                String     userId,
                                String     password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, userId, password);
    }
}