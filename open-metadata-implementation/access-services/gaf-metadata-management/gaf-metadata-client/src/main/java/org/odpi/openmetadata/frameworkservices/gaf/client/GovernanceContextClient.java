/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;

/**
 * GovernanceContextClient sits in the governance context of a governance action service when it is running in the engine host OMAG server.
 * It is however shared by all the governance action services running in an engine service so that we only need one connector to the topic
 * listener for the watchdog governance services.
 */
public class GovernanceContextClient extends GovernanceContextClientBase
{

    /**
     * Create a new client with no authentication embedded in the HTTP request.
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
    public GovernanceContextClient(String   serverName,
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
