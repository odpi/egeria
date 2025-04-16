/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server;


import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServiceResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;



/**
 * The DigitalArchitectureRESTServices provides the server-side implementation of the Digital Architecture Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class DigitalArchitectureRESTServices
{
    private static final DigitalArchitectureInstanceHandler   instanceHandler     = new DigitalArchitectureInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(DigitalArchitectureRESTServices.class),
                                                                                        instanceHandler.getServiceName());
    private   final      RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DigitalArchitectureRESTServices()
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
                                                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceCode(),
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