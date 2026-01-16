/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.client;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.Map;

/**
 * EgeriaOpenMetadataStoreClient provides an interface to the open metadata store.  This is part of the Open Metadata Framework (OMF)
 * and provides a comprehensive interface for working with all types of metadata, subject to the user's (and this OMAS's) security permissions.
 * The interface supports search, maintenance of metadata elements, classifications and relationships plus the ability to raise incident reports
 * and todos along with the ability to work with metadata valid values and translations.
 */
public class EgeriaOpenMetadataStoreClient extends OpenMetadataClientBase
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum value allowed for page size
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public EgeriaOpenMetadataStoreClient(String   serverName,
                                         String   serverPlatformURLRoot,
                                         String   secretsStoreProvider,
                                         String   secretsStoreLocation,
                                         String   secretsStoreCollection,
                                         int      maxPageSize,
                                         AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, maxPageSize, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param maxPageSize maximum value allowed for page size
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public EgeriaOpenMetadataStoreClient(String                             serverName,
                                         String                             serverPlatformURLRoot,
                                         Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                         int                                maxPageSize,
                                         AuditLog                           auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, secretsStoreConnectorMap, maxPageSize, auditLog);
    }
}
