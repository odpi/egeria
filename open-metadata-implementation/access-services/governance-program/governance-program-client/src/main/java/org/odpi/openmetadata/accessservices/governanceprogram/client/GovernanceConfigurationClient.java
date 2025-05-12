/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClientBase;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;


/**
 * GovernanceConfigurationClient supports the configuration of Governance Server and governance services.
 */
public class GovernanceConfigurationClient extends GovernanceConfigurationClientBase
{
    private final static String serviceURLMarker = AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceURLMarker();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceConfigurationClient(String serverName,
                                         String serverPlatformURLRoot,
                                         int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize           pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceConfigurationClient(String serverName,
                                         String serverPlatformURLRoot,
                                         String userId,
                                         String password,
                                         int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker, userId, password, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public GovernanceConfigurationClient(String        serverName,
                                         String        serverPlatformURLRoot,
                                         GAFRESTClient restClient,
                                         int           maxPageSize,
                                         AuditLog      auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker, restClient, maxPageSize, auditLog);
    }
}
