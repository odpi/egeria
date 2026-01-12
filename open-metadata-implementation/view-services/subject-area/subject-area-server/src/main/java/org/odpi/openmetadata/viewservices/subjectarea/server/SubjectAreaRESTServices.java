/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SubjectAreaHierarchyProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The SubjectAreaRESTServices provides the server-side implementation of the Subject Area Open Metadata
 * View Service (OMVS).  This interface provides access to data fields, data structures and data classes.
 */
public class SubjectAreaRESTServices extends TokenController
{
    private static final SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SubjectAreaRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public SubjectAreaRESTServices()
    {
    }


    /**
     * Link subject area definitions in a hierarchy.
     *
     * @param serverName         name of called server
     * @param subjectAreaGUID    unique identifier of the parent subject area.
     * @param nestedSubjectAreaGUID    unique identifier of the nested subject area.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSubjectAreas(String                  serverName,
                                         String                  subjectAreaGUID,
                                         String                  nestedSubjectAreaGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSubjectAreas";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SubjectAreaHierarchyProperties subjectAreaHierarchyProperties)
                {
                    handler.linkSubjectAreas(userId,
                                             subjectAreaGUID,
                                             nestedSubjectAreaGUID,
                                             requestBody,
                                             subjectAreaHierarchyProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSubjectAreas(userId,
                                             subjectAreaGUID,
                                             nestedSubjectAreaGUID,
                                             requestBody,
                                             null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SubjectAreaHierarchyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSubjectAreas(userId,
                                         subjectAreaGUID,
                                         nestedSubjectAreaGUID,
                                         null,
                                         null);
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
     * Detach subject area definitions from their hierarchical relationship.
     *
     * @param serverName         name of called server
     * @param subjectAreaGUID    unique identifier of the parent subject area.
     * @param nestedSubjectAreaGUID    unique identifier of the nested subject area.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSubjectAreas(String                        serverName,
                                           String                        subjectAreaGUID,
                                           String                        nestedSubjectAreaGUID,
                                           DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSubjectAreas";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            handler.detachSubjectAreas(userId, subjectAreaGUID, nestedSubjectAreaGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
