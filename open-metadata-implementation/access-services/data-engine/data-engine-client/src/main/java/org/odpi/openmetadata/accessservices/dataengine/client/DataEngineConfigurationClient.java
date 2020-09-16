/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * DataEngineConfigurationClient provides the client-side interface for obtaining Data Engine access service configuration specifics using OCF REST client.
 */
public class DataEngineConfigurationClient extends OCFRESTClient {

    private static final String IN_TOPIC_CONNECTION_PATH = "/servers/{0}/open-metadata/access-services/data-engine/users/{1}/topics/in-topic-connection";
    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     *  Create DataEngineConfigurationClient with unauthenticated access to the DE OMAS server.
     * @param serverName
     * @param serverPlatformRootURL
     * @throws InvalidParameterException
     */
    public DataEngineConfigurationClient(String serverName, String serverPlatformRootURL) throws InvalidParameterException {
        super(serverName, serverPlatformRootURL);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformRootURL;
    }

    /**
     *
     * @param serverName
     * @param serverPlatformRootURL
     * @param userId
     * @param password
     * @throws InvalidParameterException
     */
    public DataEngineConfigurationClient(String serverName, String serverPlatformRootURL, String userId, String password) throws InvalidParameterException {
        super(serverName, serverPlatformRootURL, userId, password);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformRootURL;
    }

    /**
     * Retrieve input topic connection details from remote DE OMAS instance.
     *
     * @param serverName name of the server hosting DE access service
     * @param userId user accessing the server hosting DE access service
     * @return ConnectionResponse OCF object
     *
     * @throws InvalidParameterException
     * @throws PropertyServerException
     * @throws UserNotAuthorizedException
     */
    public ConnectionResponse getInTopicConnection(String serverName, String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        String methodName = "getInTopicConnection";

        invalidParameterHandler.validateUserId(serverName, methodName);
        invalidParameterHandler.validateUserId(userId, methodName);

        ConnectionResponse restResult = callConnectionGetRESTCall(methodName, serverPlatformURLRoot + IN_TOPIC_CONNECTION_PATH, serverName, userId);

        return restResult;
    }


}
