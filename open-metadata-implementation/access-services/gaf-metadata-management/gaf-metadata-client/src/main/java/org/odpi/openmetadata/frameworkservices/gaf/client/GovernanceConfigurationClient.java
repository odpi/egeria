/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;


/**
 * GovernanceConfigurationClient supports the configuration of Governance Server and governance services.
 */
public class GovernanceConfigurationClient extends GovernanceConfigurationClientBase
{

    /**
     * Create a new client with bearer token authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum value allowed for page size
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceConfigurationClient(String   serverName,
                                         String   serverPlatformURLRoot,
                                         String   secretsStoreProvider,
                                         String   secretsStoreLocation,
                                         String   secretsStoreCollection,
                                         int      maxPageSize,
                                         AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, maxPageSize, auditLog);
    }
}
