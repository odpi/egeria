/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataplatform.handlers.RegistrationHandler;
import org.odpi.openmetadata.accessservices.dataplatform.responses.RegistrationRequestBody;
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
     * Create the software server capability entity
     *
     * @param serverName              name of server instance to call
     * @param userId                  the name of the calling user
     * @param registrationRequestBody properties of the server
     * @return the unique identifier (guid) of the created server
     */
    public GUIDResponse createSoftwareServer(String serverName, String userId,
                                             RegistrationRequestBody registrationRequestBody) {

        final String methodName = "createSoftwareServer";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            if (registrationRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }
            RegistrationHandler handler = dataPlatformInstanceHandler.getRegistrationHandler(userId, serverName, methodName);
            SoftwareServerCapability softwareServerCapability = registrationRequestBody.getSoftwareServerCapability();
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
     * Get the unique identifier from a software server capability definition
     *
     * @param serverName    name of the service to route the request to
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the server
     * @return the unique identifier from a software server capability definition
     */
    public GUIDResponse getSoftwareServerGuidByQualifiedName(String serverName, String userId, String qualifiedName) {

        final String methodName = "getSoftwareServerGuidByQualifiedName";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            RegistrationHandler handler = dataPlatformInstanceHandler.getRegistrationHandler(userId, serverName,
                    methodName);

            response.setGUID(handler.getSoftwareServerCapabilityGuidByQualifiedName(userId, qualifiedName));

        } catch (InvalidParameterException error) {
            dataPlatformInstanceHandler.getExceptionHandler().captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            dataPlatformInstanceHandler.getExceptionHandler().capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            dataPlatformInstanceHandler.getExceptionHandler().captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }


}
