/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;



import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServiceResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The GovernanceProgramRESTServices provides the server-side implementation of the GovernanceProgram Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class GovernanceProgramRESTServices
{
    static final private GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private final static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceProgramRESTServices.class),
                                                                      instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public GovernanceProgramRESTServices()
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
                                                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceCode(),
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
