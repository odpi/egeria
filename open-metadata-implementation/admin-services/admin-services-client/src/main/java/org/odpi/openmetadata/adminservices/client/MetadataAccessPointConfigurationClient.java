/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.Map;

/**
 * MetadataAccessPointConfigurationClient configures a MetadataAccessPoint OMAG Server.  This server
 * can become a cohort member and, through the access services, offers a wide range of specialist APIs
 * and event streams to access and store metadata.
 */
public class MetadataAccessPointConfigurationClient extends MetadataAccessServerConfigurationClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretStoreProvider class name of the secrets store
     * @param secretStoreLocation location (networkAddress) of the secrets store
     * @param secretStoreCollection name of the collection of secrets to use to connect to the remote server
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataAccessPointConfigurationClient(String   serverName,
                                                  String   serverPlatformRootURL,
                                                  String   secretStoreProvider,
                                                  String   secretStoreLocation,
                                                  String   secretStoreCollection,
                                                  String   delegatingUserId,
                                                  AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, delegatingUserId, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataAccessPointConfigurationClient(String                             serverPlatformRootURL,
                                                  Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                                  String                             delegatingUserId,
                                                  AuditLog                           auditLog) throws InvalidParameterException
    {
        super(serverPlatformRootURL, secretsStoreConnectorMap, delegatingUserId, auditLog);
    }
}
