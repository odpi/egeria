/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Map;

/**
 * MetadataAccessStoreConfigurationClient provides the configuration client for a MetadataAccessServer OMAG server.
 * A metadata access server is a metadata access point with a native open metadata repository.
 */
public class MetadataAccessStoreConfigurationClient extends MetadataAccessServerConfigurationClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretStoreProvider class name of the secrets store
     * @param secretStoreLocation location (networkAddress) of the secrets store
     * @param secretStoreCollection name of the collection of secrets to use to connect to the remote server
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataAccessStoreConfigurationClient(String   serverName,
                                                  String   serverPlatformRootURL,
                                                  String   secretStoreProvider,
                                                  String   secretStoreLocation,
                                                  String   secretStoreCollection,
                                                  AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, auditLog);
    }


    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setInMemLocalRepository() throws UserNotAuthorizedException,
                                                 OMAGConfigurationErrorException,
                                                 InvalidParameterException
    {
        final String methodName  = "setInMemLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/local-repository/mode/in-memory-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName);
    }


    /**
     * Set up a read only local repository.  This repository manages metadata in memory but does not
     * support the ability to store new metadata.  This means it can safely be used to host read only content
     * from an open metadata archive within a production cohort.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setReadOnlyLocalRepository() throws UserNotAuthorizedException,
                                                    OMAGConfigurationErrorException,
                                                    InvalidParameterException
    {
        final String methodName  = "setReadOnlyLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/local-repository/mode/read-only-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName);
    }



    /**
     * Set up a  local repository that stores metadata in PostgreSQL tables.  This repository manages metadata in memory but does not
     * support the ability to store new metadata.  This means it can safely be used to host read only content
     * from an open metadata archive within a production cohort.
     *
     * @param storageProperties  properties used to configure the back end storage for the repository
     * Each repository is stored in its own database schema.  The storage properties should include databaseURL, databaseSchema, secretsStore and secretsCollectionName
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setPostgreSQLLocalRepository(Map<String, Object> storageProperties) throws UserNotAuthorizedException,
                                                                                           OMAGConfigurationErrorException,
                                                                                           InvalidParameterException
    {
        final String methodName  = "setPostgreSQLLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/local-repository/mode/postgres-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        storageProperties,
                                        serverName);
    }



    /**
     * Provide the connection to the local repository connector that maps
     * the OMRS RepositoryConnector API to the third party persistence API.
     * The persistence layer is only called through the open metadata APIs.
     *
     * @param connection  connection to the OMRS repository connector.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setPluginRepositoryConnection(Connection connection) throws UserNotAuthorizedException,
                                                                            InvalidParameterException,
                                                                            OMAGConfigurationErrorException
    {
        final String methodName    = "setRepositoryConnection";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/local-repository/mode/plugin-repository/connection";

        invalidParameterHandler.validateConnection(connection, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        serverName);
    }


    /**
     * Provide the connection to the local repository connector that maps
     * the OMRS RepositoryConnector API to the third party persistence API.
     * The persistence layer is only called through the open metadata APIs.
     *
     * @param connectorProvider    connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setPluginRepositoryConnection(String              connectorProvider,
                                              Map<String, Object> additionalProperties) throws UserNotAuthorizedException,
                                                                                               InvalidParameterException,
                                                                                               OMAGConfigurationErrorException
    {
        final String methodName    = "setNativeRepositoryConnection";
        final String parameterName = "connectorProvider";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/local-repository/mode/plugin-repository/details" +
                                             "?connectorProvider={1}";

        invalidParameterHandler.validateName(connectorProvider, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        additionalProperties,
                                        serverName,
                                        connectorProvider);
    }


}
