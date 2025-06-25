/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.client;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.CollectionHandler;

/**
 * The CollectionsClient supports requests related to collections.
 */
public class CollectionsClient extends CollectionHandler
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param localServerName       name of this server (view server)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog              logging destination
     * @param maxPageSize           number of elements that can be returned on a call
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public CollectionsClient(String localServerName,
                             String serverName,
                             String serverPlatformURLRoot,
                             AuditLog auditLog,
                             int maxPageSize) throws InvalidParameterException
    {
        super(localServerName,
              serverName,
              serverPlatformURLRoot,
              auditLog,
              AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceURLMarker(),
              AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceName(),
              maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param localServerName       name of this server (view server)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param auditLog              logging destination
     * @param maxPageSize           number of elements that can be returned on a call
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public CollectionsClient(String localServerName,
                             String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password,
                             AuditLog auditLog,
                             int maxPageSize) throws InvalidParameterException
    {
        super(localServerName,
              serverName,
              serverPlatformURLRoot,
              userId,
              password,
              auditLog,
              AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceURLMarker(),
              AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceName(),
              maxPageSize);
    }
}