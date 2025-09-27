/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.oif.rest.MetadataOriginRequestBody;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The OpenIntegrationRESTServices provides the server-side implementation of the services used by the governance
 * engine as it is managing requests to execute open governance services in the governance server.
 * These services align with the interface definitions from the Open Integration Framework (OIF).
 */
public class OpenIntegrationRESTServices
{
    private final static OIFServicesInstanceHandler instanceHandler = new OIFServicesInstanceHandler();

    private final        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private final static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(OpenIntegrationRESTServices.class),
                                                                                        instanceHandler.getServiceName());



    /**
     * Default constructor
     */
    public OpenIntegrationRESTServices()
    {
    }


    /**
     * Retrieve the unique identifier of the external metadata source.
     *
     * @param serverName name of the server to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody unique name of the software capability
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse getMetadataSourceGUID(String          serverName,
                                              String          serviceURLMarker,
                                              String          userId,
                                              NameRequestBody requestBody)
    {
        final String methodName = "getMetadataSourceGUID";
        final String parameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<Object> handler = instanceHandler.getMetadataSourceHandler(userId, serverName, methodName);

                response.setGUID(handler.getBeanGUIDByQualifiedName(userId,
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                    requestBody.getName(),
                                                                    parameterName,
                                                                    false,
                                                                    false,
                                                                    new Date(),
                                                                    methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a software capability.  This describes the metadata source.
     *
     * @param serverName name of the server to route the request to.
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param requestBody properties of the software capability
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @SuppressWarnings(value = "unused")
    public GUIDResponse createMetadataSource(String                    serverName,
                                             String                    serviceURLMarker,
                                             String                    userId,
                                             MetadataOriginRequestBody requestBody)
    {
        final String methodName = "createMetadataSource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<Object> handler = instanceHandler.getMetadataSourceHandler(userId, serverName, methodName);

                response.setGUID(handler.createSoftwareCapability(userId,
                                                                  null,
                                                                  null,
                                                                  requestBody.getTypeName(),
                                                                  requestBody.getClassificationName(),
                                                                  requestBody.getQualifiedName(),
                                                                  null,
                                                                  null,
                                                                  requestBody.getDeployedImplementationType(),
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
