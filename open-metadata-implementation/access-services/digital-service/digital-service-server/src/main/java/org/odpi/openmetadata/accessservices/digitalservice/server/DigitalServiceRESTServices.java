/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.server;


import org.odpi.openmetadata.accessservices.digitalservice.handlers.DigitalServiceEntityHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServiceResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalServiceProperties;
import org.slf4j.LoggerFactory;



/**
 * The DigitalServiceRESTServices provides the server-side implementation of the Digital Service Open Metadata
 * Assess Service (OMAS).  This interface provides supports for managing digital assets.
 */
public class DigitalServiceRESTServices
{
    private static final DigitalServiceInstanceHandler instanceHandler = new DigitalServiceInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(DigitalServiceRESTServices.class),
                                                                                        instanceHandler.getServiceName());
    private final        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();



    /**
     * Default constructor
     */
    public DigitalServiceRESTServices()
    {
    }

    /**
     * Return service description method.  This method is used to ensure Spring loads this module.
     *
     * @param serverName called server
     * @param userId calling user
     * @return service description
     */
    public RegisteredOMAGServiceResponse getServiceDescription(String serverName,
                                                               String userId)
    {
        final String methodName = "getServiceDescription";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServiceResponse response = new RegisteredOMAGServiceResponse();
        AuditLog                      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setService(instanceHandler.getRegisteredOMAGService(userId,
                                                                         serverName,
                                                                         AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceCode(),
                                                                         methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


}