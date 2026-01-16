/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * EgeriaOpenGovernanceClient provides an interface to the services that build, monitor and trigger governance actions.
 * This is part of the Open Governance Framework (OGF).
 */
public class EgeriaOpenGovernanceClient extends OpenGovernanceClientBase
{
    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize           pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public EgeriaOpenGovernanceClient(String   serverName,
                                      String   serverPlatformURLRoot,
                                      String   localServerSecretsStoreProvider,
                                      String   localServerSecretsStoreLocation,
                                      String   localServerSecretsStoreCollection,
                                      int      maxPageSize,
                                      AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, maxPageSize, auditLog);
    }
}
