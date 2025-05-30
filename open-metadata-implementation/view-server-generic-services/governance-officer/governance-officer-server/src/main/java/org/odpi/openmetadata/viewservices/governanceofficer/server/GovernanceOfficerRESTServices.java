/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceofficer.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The GovernanceOfficerRESTServices provides the server-side implementation of the Governance Officer Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class GovernanceOfficerRESTServices extends TokenController
{
    private static final GovernanceOfficerInstanceHandler instanceHandler = new GovernanceOfficerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceOfficerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public GovernanceOfficerRESTServices()
    {
    }


    /**
     * Create a governance definition.
     *
     * @param serverName                 name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody             properties for the governance definition.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createGovernanceDefinition(String                             serverName,
                                                   String                             viewServiceURLMarker,
                                                   NewGovernanceDefinitionRequestBody requestBody)
    {
        final String methodName = "createGovernanceDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.createGovernanceDefinition(userId,
                                                                    requestBody.getExternalSourceGUID(),
                                                                    requestBody.getExternalSourceName(),
                                                                    requestBody.getAnchorGUID(),
                                                                    requestBody.getIsOwnAnchor(),
                                                                    requestBody.getAnchorScopeGUID(),
                                                                    requestBody.getProperties(),
                                                                    requestBody.getInitialStatus(),
                                                                    requestBody.getParentGUID(),
                                                                    requestBody.getParentRelationshipTypeName(),
                                                                    requestBody.getParentRelationshipProperties(),
                                                                    requestBody.getParentAtEnd1(),
                                                                    requestBody.getForLineage(),
                                                                    requestBody.getForDuplicateProcessing(),
                                                                    requestBody.getEffectiveTime()));
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
     * Create a new metadata element to represent a governance definition using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGovernanceDefinitionFromTemplate(String              serverName,
                                                               String              viewServiceURLMarker,
                                                               TemplateRequestBody requestBody)
    {
        final String methodName = "createGovernanceDefinitionFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.createGovernanceDefinitionFromTemplate(userId,
                                                                                requestBody.getExternalSourceGUID(),
                                                                                requestBody.getExternalSourceName(),
                                                                                requestBody.getAnchorGUID(),
                                                                                requestBody.getIsOwnAnchor(),
                                                                                requestBody.getAnchorScopeGUID(),
                                                                                null,
                                                                                null,
                                                                                requestBody.getTemplateGUID(),
                                                                                requestBody.getReplacementProperties(),
                                                                                requestBody.getPlaceholderPropertyValues(),
                                                                                requestBody.getParentGUID(),
                                                                                requestBody.getParentRelationshipTypeName(),
                                                                                requestBody.getParentRelationshipProperties(),
                                                                                requestBody.getParentAtEnd1(),
                                                                                requestBody.getForLineage(),
                                                                                requestBody.getForDuplicateProcessing(),
                                                                                requestBody.getEffectiveTime()));
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
     * Update the properties of a governance definition.
     *
     * @param serverName         name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateGovernanceDefinition(String                                serverName,
                                                   String                                viewServiceURLMarker,
                                                   String                                governanceDefinitionGUID,
                                                   boolean                               replaceAllProperties,
                                                   UpdateGovernanceDefinitionRequestBody requestBody)
    {
        final String methodName = "updateGovernanceDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

                handler.updateGovernanceDefinition(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   governanceDefinitionGUID,
                                                   replaceAllProperties,
                                                   requestBody.getProperties(),
                                                   requestBody.getForLineage(),
                                                   requestBody.getForDuplicateProcessing(),
                                                   requestBody.getEffectiveTime());
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
     * Update the properties of a governance definition.
     *
     * @param serverName         name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateGovernanceDefinitionStatus(String                                serverName,
                                                         String                                viewServiceURLMarker,
                                                         String                                governanceDefinitionGUID,
                                                         GovernanceDefinitionStatusRequestBody requestBody)
    {
        final String methodName = "updateGovernanceDefinitionStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

                handler.updateGovernanceDefinitionStatus(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         governanceDefinitionGUID,
                                                         requestBody.getStatus(),
                                                         requestBody.getForLineage(),
                                                         requestBody.getForDuplicateProcessing(),
                                                         requestBody.getEffectiveTime());
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
     * Attach two peer governance definitions.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkPeerDefinitions(String                    serverName,
                                            String                    viewServiceURLMarker,
                                            String                    governanceDefinitionOneGUID,
                                            String                    governanceDefinitionTwoGUID,
                                            String                    relationshipTypeName,
                                            PeerDefinitionRequestBody requestBody)
    {
        final String methodName = "linkPeerDefinitions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.linkPeerDefinitions(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            governanceDefinitionOneGUID,
                                            governanceDefinitionTwoGUID,
                                            relationshipTypeName,
                                            requestBody.getProperties(),
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
            }
            else
            {
                handler.linkPeerDefinitions(userId,
                                            null,
                                            null,
                                            governanceDefinitionOneGUID,
                                            governanceDefinitionTwoGUID,
                                            relationshipTypeName,
                                            null,
                                            false,
                                            false,
                                            new Date());
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
     * Detach a governance definition from one of its peers.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachPeerDefinitions(String                    serverName,
                                              String                    viewServiceURLMarker,
                                              String                    governanceDefinitionOneGUID,
                                              String                    governanceDefinitionTwoGUID,
                                              String                    relationshipTypeName,
                                              MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachPeerDefinitions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.detachPeerDefinitions(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              governanceDefinitionOneGUID,
                                              governanceDefinitionTwoGUID,
                                              relationshipTypeName,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachPeerDefinitions(userId,
                                              null,
                                              null,
                                              governanceDefinitionOneGUID,
                                              governanceDefinitionTwoGUID,
                                              relationshipTypeName,
                                              false,
                                              false,
                                              new Date());
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
     * Attach a supporting governance definition.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse attachSupportingDefinition(String                          serverName,
                                                   String                          viewServiceURLMarker,
                                                   String                          governanceDefinitionOneGUID,
                                                   String                          governanceDefinitionTwoGUID,
                                                   String                          relationshipTypeName,
                                                   SupportingDefinitionRequestBody requestBody)
    {
        final String methodName = "attachSupportingDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.attachSupportingDefinition(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   governanceDefinitionOneGUID,
                                                   governanceDefinitionTwoGUID,
                                                   relationshipTypeName,
                                                   requestBody.getProperties(),
                                                   requestBody.getForLineage(),
                                                   requestBody.getForDuplicateProcessing(),
                                                   requestBody.getEffectiveTime());
            }
            else
            {
                handler.attachSupportingDefinition(userId,
                                                   null,
                                                   null,
                                                   governanceDefinitionOneGUID,
                                                   governanceDefinitionTwoGUID,
                                                   relationshipTypeName,
                                                   null,
                                                   false,
                                                   false,
                                                   new Date());
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
     * Detach a governance definition from a supporting governance definition.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSupportingDefinition(String                    serverName,
                                                   String                    viewServiceURLMarker,
                                                   String                    governanceDefinitionOneGUID,
                                                   String                    governanceDefinitionTwoGUID,
                                                   String                    relationshipTypeName,
                                                   MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachSupportingDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.detachSupportingDefinition(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   governanceDefinitionOneGUID,
                                                   governanceDefinitionTwoGUID,
                                                   relationshipTypeName,
                                                   requestBody.getForLineage(),
                                                   requestBody.getForDuplicateProcessing(),
                                                   requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachSupportingDefinition(userId,
                                                   null,
                                                   null,
                                                   governanceDefinitionOneGUID,
                                                   governanceDefinitionTwoGUID,
                                                   relationshipTypeName,
                                                   false,
                                                   false,
                                                   new Date());
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
     * Attach a governance definition to its implementation.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param technicalControlGUID unique identifier of the first governance definition
     * @param implementationGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDefinitionImplementation(String                    serverName,
                                                     String                    viewServiceURLMarker,
                                                     String                    technicalControlGUID,
                                                     String                    implementationGUID,
                                                     String                    relationshipTypeName,
                                                     GovernanceImplementationRequestBody requestBody)
    {
        final String methodName = "linkDefinitionImplementation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.linkDefinitionImplementation(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     technicalControlGUID,
                                                     implementationGUID,
                                                     relationshipTypeName,
                                                     requestBody.getProperties(),
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getEffectiveTime());
            }
            else
            {
                handler.linkDefinitionImplementation(userId,
                                                     null,
                                                     null,
                                                     technicalControlGUID,
                                                     implementationGUID,
                                                     relationshipTypeName,
                                                     null,
                                                     false,
                                                     false,
                                                     new Date());
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
     * Detach a governance definition from its implementation.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDefinitionImplementation(String                    serverName,
                                                       String                    viewServiceURLMarker,
                                                       String                    governanceDefinitionOneGUID,
                                                       String                    governanceDefinitionTwoGUID,
                                                       String                    relationshipTypeName,
                                                       MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachDefinitionImplementation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.detachDefinitionImplementation(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       governanceDefinitionOneGUID,
                                                       governanceDefinitionTwoGUID,
                                                       relationshipTypeName,
                                                       requestBody.getForLineage(),
                                                       requestBody.getForDuplicateProcessing(),
                                                       requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachDefinitionImplementation(userId,
                                                       null,
                                                       null,
                                                       governanceDefinitionOneGUID,
                                                       governanceDefinitionTwoGUID,
                                                       relationshipTypeName,
                                                       false,
                                                       false,
                                                       new Date());
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
     * Delete a governance definition.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionGUID  unique identifier of the element to delete
     * @param cascadedDelete can governance definitions be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteGovernanceDefinition(String                    serverName,
                                                   String                    viewServiceURLMarker,
                                                   String                    governanceDefinitionGUID,
                                                   boolean                   cascadedDelete,
                                                   MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteGovernanceDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.deleteGovernanceDefinition(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   governanceDefinitionGUID,
                                                   cascadedDelete,
                                                   requestBody.getForLineage(),
                                                   requestBody.getForDuplicateProcessing(),
                                                   requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteGovernanceDefinition(userId,
                                                   null,
                                                   null,
                                                   governanceDefinitionGUID,
                                                   cascadedDelete,
                                                   false,
                                                   false,
                                                   new Date());
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
     * Retrieve the list of governance definition metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDefinitionsResponse getGovernanceDefinitionsByName(String            serverName,
                                                                        String            viewServiceURLMarker,
                                                                        int               startFrom,
                                                                        int               pageSize,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getGovernanceDefinitionsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceDefinitionsResponse response = new GovernanceDefinitionsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getGovernanceDefinitionsByName(userId,
                                                                            requestBody.getFilter(),
                                                                            requestBody.getTemplateFilter(),
                                                                            requestBody.getLimitResultsByStatus(),
                                                                            requestBody.getAsOfTime(),
                                                                            requestBody.getSequencingOrder(),
                                                                            requestBody.getSequencingProperty(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getForLineage(),
                                                                            requestBody.getForDuplicateProcessing(),
                                                                            requestBody.getEffectiveTime()));
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
     * Retrieve the list of governance definition metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDefinitionResponse getGovernanceDefinitionByGUID(String             serverName,
                                                                      String             viewServiceURLMarker,
                                                                      String             governanceDefinitionGUID,
                                                                      AnyTimeRequestBody requestBody)
    {
        final String methodName = "getGovernanceDefinitionByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceDefinitionResponse response = new GovernanceDefinitionResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGovernanceDefinitionByGUID(userId,
                                                                          governanceDefinitionGUID,
                                                                          requestBody.getAsOfTime(),
                                                                          requestBody.getForLineage(),
                                                                          requestBody.getForDuplicateProcessing(),
                                                                          requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getGovernanceDefinitionByGUID(userId,
                                                                          governanceDefinitionGUID,
                                                                          null,
                                                                          false,
                                                                          false,
                                                                          new Date()));
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
     * Retrieve the list of governance definition metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDefinitionsResponse findGovernanceDefinitions(String            serverName,
                                                                   String            viewServiceURLMarker,
                                                                   boolean           startsWith,
                                                                   boolean           endsWith,
                                                                   boolean           ignoreCase,
                                                                   int               startFrom,
                                                                   int               pageSize,
                                                                   FilterRequestBody requestBody)
    {
        final String methodName = "findGovernanceDefinitions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceDefinitionsResponse response = new GovernanceDefinitionsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findGovernanceDefinitions(userId,
                                                                       instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                       requestBody.getTemplateFilter(),
                                                                       requestBody.getLimitResultsByStatus(),
                                                                       requestBody.getAsOfTime(),
                                                                       requestBody.getSequencingOrder(),
                                                                       requestBody.getSequencingProperty(),
                                                                       startFrom,
                                                                       pageSize,
                                                                       requestBody.getForLineage(),
                                                                       requestBody.getForDuplicateProcessing(),
                                                                       requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findGovernanceDefinitions(userId,
                                                                       instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                                       TemplateFilter.ALL,
                                                                       null,
                                                                       null,
                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                       null,
                                                                       startFrom,
                                                                       pageSize,
                                                                       false,
                                                                       false,
                                                                       new Date()));
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
     * Return the governance definition associated with a unique identifier and the other governance definitions linked to it.
     *
     * @param serverName name of the server instance to connect to
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param startFrom paging start
     * @param pageSize max elements that can be returned
     * @param requestBody additional query parameters
     *
     * @return governance definition and its linked elements or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public GovernanceDefinitionGraphResponse getGovernanceDefinitionInContext(String             serverName,
                                                                              String             viewServiceURLMarker,
                                                                              String             governanceDefinitionGUID,
                                                                              int                startFrom,
                                                                              int                pageSize,
                                                                              ResultsRequestBody requestBody)
    {
        final String methodName = "getGovernanceDefinitionInContext";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceDefinitionGraphResponse response = new GovernanceDefinitionGraphResponse();
        AuditLog                          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGovernanceDefinitionInContext(userId,
                                                                             governanceDefinitionGUID,
                                                                             requestBody.getLimitResultsByStatus(),
                                                                             requestBody.getAsOfTime(),
                                                                             requestBody.getSequencingProperty(),
                                                                             requestBody.getSequencingOrder(),
                                                                             requestBody.getForLineage(),
                                                                             requestBody.getForDuplicateProcessing(),
                                                                             requestBody.getEffectiveTime(),
                                                                             startFrom,
                                                                             pageSize));
            }
            else
            {
                response.setElement(handler.getGovernanceDefinitionInContext(userId,
                                                                             governanceDefinitionGUID,
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                                             false,
                                                                             false,
                                                                             new Date(),
                                                                             startFrom,
                                                                             pageSize));
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
