/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.server;


import org.odpi.openmetadata.accessservices.digitalservice.handlers.DigitalServiceEntityHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ReferenceableRequestBody;
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
     * Create a new digital service.
     *
     * @param serverName                        the server name
     * @param userId                            the user id
     * @param requestBody the digital service request body
     * @return the guid response
     */
    public GUIDResponse createDigitalService( String                   userId,
                                              String                   serverName,
                                              ReferenceableRequestBody requestBody)
    {
        final String methodName = "createDigitalService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DigitalServiceEntityHandler handler = instanceHandler.getDigitalServiceEntityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalServiceProperties)
                {
                    DigitalServiceProperties digitalServiceProperties = (DigitalServiceProperties) requestBody.getProperties();
                    response.setGUID(handler.createDigitalServiceEntity(userId, serverName, digitalServiceProperties));
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


}