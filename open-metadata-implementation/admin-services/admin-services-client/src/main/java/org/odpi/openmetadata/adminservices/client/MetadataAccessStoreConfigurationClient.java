/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

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
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataAccessStoreConfigurationClient(String adminUserId,
                                                  String serverName,
                                                  String serverPlatformRootURL) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL);
    }


    /**
     * Create a new client that passes a connection userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is passed as the admin userId.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param connectionUserId      caller's system userId embedded in all HTTP requests
     * @param connectionPassword    caller's system password embedded in all HTTP requests
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataAccessStoreConfigurationClient(String adminUserId,
                                                  String serverName,
                                                  String serverPlatformRootURL,
                                                  String connectionUserId,
                                                  String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
    }


    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setInMemLocalRepository() throws OMAGNotAuthorizedException,
                                                 OMAGConfigurationErrorException,
                                                 OMAGInvalidParameterException
    {
        final String methodName  = "setInMemLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/in-memory-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        adminUserId,
                                        serverName);
    }



    /**
     * Set up a graph store as the local repository.  This graph store uses JanusGraph.  It is scalable with
     * different back ends and can be run in a HA context with multiple versions of the same server deployed
     * to the same repository.
     *
     * @param storageProperties  properties used to configure the back end storage for the graph
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setGraphLocalRepository(Map<String, Object> storageProperties) throws OMAGNotAuthorizedException,
                                                                                      OMAGConfigurationErrorException,
                                                                                      OMAGInvalidParameterException
    {
        final String methodName  = "setGraphLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/local-graph-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        storageProperties,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Set up an XTDB store as the local repository.  This store uses XTDB to provide a historical metadata repository that operates in memory.
     * This version of the XTDB repository is designed for testing.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setXTDBInMemRepository() throws OMAGNotAuthorizedException,
                                                OMAGConfigurationErrorException,
                                                OMAGInvalidParameterException
    {
        final String methodName  = "setXTDBInMemRepository";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/xtdb-in-memory-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Set up an XTDB store as the local repository.  This store uses XTDB with RocksDB KV store to provide a high performance historical
     * metadata repository.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setXTDBLocalKVRepository() throws OMAGNotAuthorizedException,
                                                  OMAGConfigurationErrorException,
                                                  OMAGInvalidParameterException
    {
        final String methodName  = "setXTDBLocalKVRepository";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/xtdb-local-kv-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Set up an XTDB store as the local repository.  This store uses XTDB to provide a high performance historical
     * metadata repository.  It is scalable with different back ends and can be run in a HA context with multiple versions of the
     * same server deployed to the same repository.
     *
     * @param storageProperties  properties used to configure the back end storage for the repository.  They are stored in the
     *                           configuration properties of the local repository's connection.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setXTDBLocalRepository(Map<String, Object> storageProperties) throws OMAGNotAuthorizedException,
                                                                                     OMAGConfigurationErrorException,
                                                                                     OMAGInvalidParameterException
    {
        final String methodName  = "setXTDBLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/xtdb-local-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        storageProperties,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Set up an read only local repository.  This repository manages metadata in memory but does not
     * support the ability to store new metadata.  This means it can safely be used to host read only content
     * from an open metadata archive within a production cohort.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setReadOnlyLocalRepository() throws OMAGNotAuthorizedException,
                                                 OMAGConfigurationErrorException,
                                                 OMAGInvalidParameterException
    {
        final String methodName  = "setReadOnlyLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/read-only-repository";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        adminUserId,
                                        serverName);
    }




    /**
     * Provide the connection to the local repository connector that maps
     * the OMRS RepositoryConnector API to the third party persistence API.
     * The persistence layer is only called through the open metadata APIs.
     *
     * @param connection  connection to the OMRS repository connector.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setPluginRepositoryConnection(Connection connection) throws OMAGNotAuthorizedException,
                                                                            OMAGInvalidParameterException,
                                                                            OMAGConfigurationErrorException
    {
        final String methodName    = "setRepositoryConnection";
        final String parameterName = "connection";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/plugin-repository/connection";

        try
        {
            invalidParameterHandler.validateConnection(connection, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connection,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Provide the connection to the local repository connector that maps
     * the OMRS RepositoryConnector API to the third party persistence API.
     * The persistence layer is only called through the open metadata APIs.
     *
     * @param connectorProvider    connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setPluginRepositoryConnection(String              connectorProvider,
                                              Map<String, Object> additionalProperties) throws OMAGNotAuthorizedException,
                                                                                               OMAGInvalidParameterException,
                                                                                               OMAGConfigurationErrorException
    {
        final String methodName    = "setNativeRepositoryConnection";
        final String parameterName = "connectorProvider";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/mode/plugin-repository/details" +
                                             "?connectorProvider={2}";

        try
        {
            invalidParameterHandler.validateName(connectorProvider, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        additionalProperties,
                                        adminUserId,
                                        serverName,
                                        connectorProvider);
    }


}
