/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.server;


import org.odpi.openmetadata.accessservices.digitalservice.handlers.DigitalServiceEntityHandler;
import org.odpi.openmetadata.accessservices.digitalservice.properties.DigitalService;
import org.odpi.openmetadata.accessservices.digitalservice.rest.DigitalServiceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The DigitalServiceRESTServices provides the server-side implementation of the Stewardship Action Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class DigitalServiceRESTServices
{
    private static DigitalServiceInstanceHandler instanceHandler = new DigitalServiceInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(DigitalServiceRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DigitalServiceRESTServices()
    {
    }


    /**
     * Create deployed database schema guid response.
     *
     * @param serverName                        the server name
     * @param userId                            the user id
     * @param digitalServiceRequestBody the deployed database schema request body
     * @return the guid response
     */
    public GUIDResponse createDigitalService(String serverName, String userId,
                                                     DigitalServiceRequestBody digitalServiceRequestBody) {

        final String methodName = "create DigitalService";

        GUIDResponse response = new GUIDResponse();

        try {
            if (digitalServiceRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }
            DigitalServiceEntityHandler handler = instanceHandler.getDigitalServiceEntityHandler(userId, serverName, methodName);
            DigitalService digitalService = digitalServiceRequestBody.getDigitalService();
            response.setGUID(handler.createDigitalServiceEntity(digitalService));

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