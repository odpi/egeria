/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.lineagelinker.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.LineageHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.LineageRelationshipProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The LineageLinkerRESTServices provides the server-side implementation of the Lineage Linker Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class LineageLinkerRESTServices extends TokenController
{
    private static final LineageLinkerInstanceHandler instanceHandler = new LineageLinkerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(LineageLinkerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public LineageLinkerRESTServices()
    {
    }

    /* =======================================
     * Lineages
     */

    /**
     * Create a lineage relationship between two elements that indicates that data is flowing from one element to another.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementOneGUID unique identifier of the element
     * @param elementTwoGUID unique identifier of the lineage type
     * @param requestBody the properties of the lineage
     *
     * @return guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse linkLineage(String                     serverName,
                                    String                     urlMarker,
                                    String                     elementOneGUID,
                                    String                     relationshipTypeName,
                                    String                     elementTwoGUID,
                                    NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkLineage";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            LineageHandler handler = instanceHandler.getLineageHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LineageRelationshipProperties properties)
                {
                    response.setGUID(handler.linkLineage(userId,
                                                         elementOneGUID,
                                                         elementTwoGUID,
                                                         relationshipTypeName,
                                                         requestBody,
                                                         properties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.linkLineage(userId,
                                                         elementOneGUID,
                                                         elementTwoGUID,
                                                         relationshipTypeName,
                                                         requestBody,
                                                         null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LineageRelationshipProperties.class.getName(), methodName);
                }
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
     * Update the lineage relationship.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param lineageRelationshipGUID unique identifier for the lineage relationship
     * @param requestBody the properties of the lineage
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateLineage(String                        serverName,
                                      String                        urlMarker,
                                      String                        lineageRelationshipGUID,
                                      UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateLineage";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            LineageHandler handler = instanceHandler.getLineageHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LineageRelationshipProperties properties)
                {
                    handler.updateLineage(userId, lineageRelationshipGUID, requestBody, properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LineageRelationshipProperties.class.getName(), methodName);
                }
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
     * Remove the lineage for an element.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param lineageRelationshipGUID unique identifier for the lineage type
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse detachLineage(String                        serverName,
                                      String                        urlMarker,
                                      String                        lineageRelationshipGUID,
                                      DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachLineage";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            LineageHandler handler = instanceHandler.getLineageHandler(userId, serverName, urlMarker, methodName);

            handler.detachLineage(userId, lineageRelationshipGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
