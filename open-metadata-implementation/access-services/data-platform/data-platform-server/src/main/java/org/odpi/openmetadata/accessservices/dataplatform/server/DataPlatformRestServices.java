/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.accessservices.dataplatform.handlers.DeployedDatabaseSchemaAssetHandler;
import org.odpi.openmetadata.accessservices.dataplatform.handlers.RegistrationHandler;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DeployedDatabaseSchema;
import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataplatform.responses.DataPlatformOMASAPIResponse;
import org.odpi.openmetadata.accessservices.dataplatform.responses.DataPlatformRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataplatform.responses.DeployedDatabaseSchemaRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DataPlatformRestServices provides the server-side implementation of the Data Platform Open Metadata Assess Service
 * (OMAS). This service provides the functionality to register data platforms as software server capabilities for publishing
 * metadata from them.
 */
public class DataPlatformRestServices {

    private static final Logger log = LoggerFactory.getLogger(DataPlatformRestServices.class);

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private DataPlatformInstanceHandler dataPlatformInstanceHandler = new DataPlatformInstanceHandler();

    public DataPlatformRestServices() {
    }

    public DataPlatformRestServices(DataPlatformInstanceHandler dataPlatformInstanceHandler) {
        this.dataPlatformInstanceHandler = dataPlatformInstanceHandler;
    }


    /**
     * Create the software server capability entity from an external data platforms
     *
     * @param serverName                          name of server instance to call
     * @param userId                              the name of the calling user
     * @param dataPlatformRegistrationRequestBody properties of the server
     * @return the unique identifier (guid) of the created server
     */
    public GUIDResponse createExternalDataPlatform(String serverName,
                                                   String userId,
                                                   DataPlatformRegistrationRequestBody dataPlatformRegistrationRequestBody) {

        final String methodName = "createExternalDataPlatform";

        GUIDResponse response = new GUIDResponse();

        try {
            if (dataPlatformRegistrationRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }
            RegistrationHandler handler = dataPlatformInstanceHandler.getRegistrationHandler(userId, serverName, methodName);
            SoftwareServerCapability softwareServerCapability = dataPlatformRegistrationRequestBody.getSoftwareServerCapability();
            response.setGUID(handler.createSoftwareServerCapability(softwareServerCapability));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }


    /**
     * Return the software server capability definition from an external data platform
     *
     * @param serverName    name of the service to route the request to
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the server
     * @return the unique identifier from a software server capability definition
     */
    public DataPlatformOMASAPIResponse getExternalDataPlatformByQualifiedName(String serverName, String userId, String qualifiedName) {

        final String methodName = "getExternalDataPlatformByQualifiedName";
        DataPlatformOMASAPIResponse response = new DataPlatformOMASAPIResponse();

        try {
            RegistrationHandler handler = dataPlatformInstanceHandler.getRegistrationHandler(userId, serverName, methodName);
            response.setSoftwareServerCapability(handler.getSoftwareServerCapabilityByQualifiedName(userId, qualifiedName));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);

        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());
        return response;
    }

    /**
     * Create deployed database schema guid response.
     *
     * @param serverName                        the server name
     * @param userId                            the user id
     * @param deployedDatabaseSchemaRequestBody the deployed database schema request body
     * @return the guid response
     */
    public GUIDResponse createDeployedDatabaseSchema(String serverName, String userId,
                                                     DeployedDatabaseSchemaRequestBody deployedDatabaseSchemaRequestBody) {

        final String methodName = "createDeployedDatabaseSchema";

        GUIDResponse response = new GUIDResponse();

        try {
            if (deployedDatabaseSchemaRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }
            DeployedDatabaseSchemaAssetHandler handler = dataPlatformInstanceHandler.getDeployedDatabaseSchemaAssetHandler(userId, serverName, methodName);
            DeployedDatabaseSchema deployedDatabaseSchema = deployedDatabaseSchemaRequestBody.getDeployedDatabaseSchema();
            response.setGUID(handler.createDeployedDatabaseSchemaAsset(deployedDatabaseSchema));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }
}
