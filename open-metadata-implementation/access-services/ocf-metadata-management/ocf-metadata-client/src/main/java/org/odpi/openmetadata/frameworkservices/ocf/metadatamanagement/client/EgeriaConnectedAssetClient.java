/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * EgeriaConnectedAssetClient is used by applications and tools as a factory for Open
 * Connector Framework (OCF) connectors.  The configuration for the connectors is managed as open metadata in
 * a Connection definition.  The caller to the EgeriaConnectedAssetClient passes either the name, GUID or URL for the
 * connection to the appropriate method to retrieve a connector.  The EgeriaConnectedAssetClient retrieves the connection
 * from the metadata repository and creates an appropriate connector as described the connection and
 * returns it to the caller.
 */
public class EgeriaConnectedAssetClient extends ConnectedAssetClientBase
{
    private final static String serviceURLMarker = AccessServiceDescription.OCF_METADATA_MANAGEMENT.getServiceURLMarker();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OCF REST services
     * @param maxPageSize maximum page size for this process
     * @param auditLog destination for log messages
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EgeriaConnectedAssetClient(String   serverName,
                                      String   serverPlatformURLRoot,
                                      int      maxPageSize,
                                      AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker, maxPageSize, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OCF REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum page size for this process
     * @param auditLog destination for log messages
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public EgeriaConnectedAssetClient(String   serverName,
                                      String   serverPlatformURLRoot,
                                      String   userId,
                                      String   password,
                                      int      maxPageSize,
                                      AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker, userId, password, maxPageSize, auditLog);
    }
}