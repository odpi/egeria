/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * DataEngineRESTConfigurationClient provides an extension to the client-side REST interface for obtaining Data Engine access service configuration
 * specifics.
 */
public class DataEngineRESTConfigurationClient extends DataEngineRESTClient {

    private static final String IN_TOPIC_CONNECTION_PATH =
            "/servers/{0}/open-metadata/access-services/data-engine/users/{1}/topics/in-topic-connection";
    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Create DataEngineRESTConfigurationClient with unauthenticated access to the DE OMAS server.
     *
     * @param serverName            the server name
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException null URL or server name
     */
    public DataEngineRESTConfigurationClient(String serverName, String serverPlatformRootURL) throws InvalidParameterException {
        super(serverName, serverPlatformRootURL);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformRootURL;
    }

    /**
     * Instantiates a new Data Engine REST configuration client.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException null URL or server name
     */
    public DataEngineRESTConfigurationClient(String serverName, String serverPlatformRootURL, String userId, String password) throws
                                                                                                                              InvalidParameterException {
        super(serverName, serverPlatformRootURL, userId, password);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformRootURL;
    }

    /**
     * Retrieve input topic connection details from remote DE OMAS instance.
     *
     * @param serverName name of the server hosting DE access service
     * @param userId     user accessing the server hosting DE access service
     *
     * @return ConnectionResponse OCF object
     *
     * @throws InvalidParameterException  input parameter(s) invalid
     * @throws PropertyServerException    something went wrong with the REST call stack
     * @throws UserNotAuthorizedException user is not authorized
     */
    public ConnectionResponse getInTopicConnection(String serverName, String userId) throws InvalidParameterException, PropertyServerException,
                                                                                            UserNotAuthorizedException {

        String methodName = "getInTopicConnection";

        invalidParameterHandler.validateUserId(serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        return callOCFConnectionGetRESTCall(methodName, serverPlatformURLRoot + IN_TOPIC_CONNECTION_PATH, serverName, userId);
    }

}
