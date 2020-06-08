/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.client;

import org.odpi.openmetadata.accessservices.dataplatform.properties.DataPlatform;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DeployedDatabaseSchema;
import org.odpi.openmetadata.accessservices.dataplatform.responses.DataPlatformRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataplatform.responses.DeployedDatabaseSchemaRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * DataPlatformClient provides the client implementation for the Data Platform OMAS.
 */

@Deprecated
public class DataPlatformClient extends OCFRESTClient {

    private static final String QUALIFIED_NAME_PARAMETER = "qualifiedName";
    private static final String DATA_PLATFORM_REGISTRATION_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-platform/users/{1}/registration";
    private static final String DEPLOYED_DATABASE_SCHEMA_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-platform/users/{1}/deployed-database-schema";
    private String externalSourceName;

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Constructor for no authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public DataPlatformClient(String serverName, String serverPlatformURLRoot) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId                user id for the HTTP request
     * @param password              password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public DataPlatformClient(String serverName, String serverPlatformURLRoot, String userId, String password) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userId, password);
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }

    /**
     * Create the software server capability entity for registering external data platforms.
     *
     * @param userId                   the name of the calling user
     * @param dataPlatformProperties the software server capability bean
     * @return unique identifier of the server in the repository
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public GUIDResponse createExternalDataPlatform(String userId, DataPlatform dataPlatformProperties) throws
            InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        final String methodName = "createExternalDataPlatform";

        invalidParameterHandler.validateUserId(userId, methodName);

        DataPlatformRegistrationRequestBody requestBody = new DataPlatformRegistrationRequestBody();
        requestBody.setDataPlatformProperties(dataPlatformProperties);
        requestBody.setExternalSourceName(dataPlatformProperties.getQualifiedName());
        setExternalSourceName(dataPlatformProperties.getQualifiedName());

        return callGUIDPostRESTCall(methodName,
                serverPlatformURLRoot + DATA_PLATFORM_REGISTRATION_URL_TEMPLATE,
                requestBody,
                serverName,
                userId);
    }

    /**
     * Create deployed database schema asset from external source.
     *
     * @param userId                 the user id
     * @param deployedDatabaseSchema the deployed database schema
     * @return the string
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    the property server exception
     */
    public GUIDResponse createDeployedDatabaseSchema(String userId, DeployedDatabaseSchema deployedDatabaseSchema) throws
            InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        final String methodName = "createDeployedDatabaseSchema";

        invalidParameterHandler.validateUserId(userId, methodName);

        DeployedDatabaseSchemaRequestBody requestBody = new DeployedDatabaseSchemaRequestBody();
        requestBody.setDeployedDatabaseSchema(deployedDatabaseSchema);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(methodName,
                serverPlatformURLRoot + DEPLOYED_DATABASE_SCHEMA_URL_TEMPLATE,
                requestBody,
                serverName,
                userId);
    }

    public String getExternalSourceName() {
        return externalSourceName;
    }

    public void setExternalSourceName(String externalSourceName) {
        this.externalSourceName = externalSourceName;
    }
}
