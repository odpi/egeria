/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceserver.client;


import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.integration.client.IntegrationGroupConfiguration;
import org.odpi.openmetadata.frameworks.integration.properties.CatalogTarget;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.frameworkservices.oif.client.IntegrationGroupConfigurationClientBase;
import org.odpi.openmetadata.frameworkservices.oif.client.rest.OpenIntegrationRESTClient;

import java.util.List;


/**
 * IntegrationGroupConfigurationClient supports the configuration of integration group and integration connectors.
 */
public class IntegrationGroupConfigurationClient extends IntegrationGroupConfigurationClientBase
{
    private final static String serviceURLMarker = AccessServiceDescription.GOVERNANCE_SERVER_OMAS.getAccessServiceURLMarker();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public IntegrationGroupConfigurationClient(String serverName,
                                               String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public IntegrationGroupConfigurationClient(String serverName,
                                               String serverPlatformURLRoot,
                                               String userId,
                                               String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker, userId, password);
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
    public IntegrationGroupConfigurationClient(String                    serverName,
                                               String                    serverPlatformURLRoot,
                                               OpenIntegrationRESTClient restClient,
                                               int                       maxPageSize,
                                               AuditLog                  auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker, restClient, maxPageSize, auditLog);
    }
}
