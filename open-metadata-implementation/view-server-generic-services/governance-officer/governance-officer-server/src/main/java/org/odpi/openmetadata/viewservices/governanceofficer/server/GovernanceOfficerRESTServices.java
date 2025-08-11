/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceofficer.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintCompositionProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


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
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the governance definition.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createGovernanceDefinition(String                serverName,
                                                   String                urlMarker,
                                                   NewElementRequestBody requestBody)
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
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof GovernanceDefinitionProperties properties)
                {
                    response.setGUID(handler.createGovernanceDefinition(userId,
                                                                        requestBody,
                                                                        requestBody.getInitialClassifications(),
                                                                        properties,
                                                                        requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceDefinitionProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a governance definition using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGovernanceDefinitionFromTemplate(String              serverName,
                                                               String              urlMarker,
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
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createGovernanceDefinitionFromTemplate(userId,
                                                                                requestBody,
                                                                                requestBody.getTemplateGUID(),
                                                                                requestBody.getReplacementProperties(),
                                                                                requestBody.getPlaceholderPropertyValues(),
                                                                                requestBody.getParentRelationshipProperties()));
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
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateGovernanceDefinition(String                   serverName,
                                                   String                   urlMarker,
                                                   String                   governanceDefinitionGUID,
                                                   UpdateElementRequestBody requestBody)
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
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof GovernanceDefinitionProperties governanceDefinitionProperties)
                {
                    handler.updateGovernanceDefinition(userId,
                                                       governanceDefinitionGUID,
                                                       requestBody,
                                                       governanceDefinitionProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateGovernanceDefinition(userId,
                                                       governanceDefinitionGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceDefinitionProperties.class.getName(), methodName);
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
     * Attach two peer governance definitions.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
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
    public VoidResponse linkPeerDefinitions(String                  serverName,
                                            String                  urlMarker,
                                            String                  governanceDefinitionOneGUID,
                                            String                  governanceDefinitionTwoGUID,
                                            String                  relationshipTypeName,
                                            NewRelationshipRequestBody requestBody)
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
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof PeerDefinitionProperties peerDefinitionProperties)
                {
                    handler.linkPeerDefinitions(userId,
                                                governanceDefinitionOneGUID,
                                                governanceDefinitionTwoGUID,
                                                relationshipTypeName,
                                                requestBody,
                                                peerDefinitionProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkPeerDefinitions(userId,
                                                governanceDefinitionOneGUID,
                                                governanceDefinitionTwoGUID,
                                                relationshipTypeName,
                                                requestBody,
                                                null);
                }
                else
                {
                    /*
                     * Wrong type of properties ...
                     */
                    restExceptionHandler.handleInvalidPropertiesObject(PeerDefinitionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkPeerDefinitions(userId,
                                            governanceDefinitionOneGUID,
                                            governanceDefinitionTwoGUID,
                                            relationshipTypeName,
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
     * Detach a governance definition from one of its peers.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
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
    public VoidResponse detachPeerDefinitions(String                   serverName,
                                              String                   urlMarker,
                                              String                   governanceDefinitionOneGUID,
                                              String                   governanceDefinitionTwoGUID,
                                              String                   relationshipTypeName,
                                              DeleteRequestBody requestBody)
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            handler.detachPeerDefinitions(userId,
                                          governanceDefinitionOneGUID,
                                          governanceDefinitionTwoGUID,
                                          relationshipTypeName,
                                          requestBody);
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
     * @param urlMarker  view service URL marker
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
    public VoidResponse attachSupportingDefinition(String                  serverName,
                                                   String                  urlMarker,
                                                   String                  governanceDefinitionOneGUID,
                                                   String                  governanceDefinitionTwoGUID,
                                                   String                  relationshipTypeName,
                                                   NewRelationshipRequestBody requestBody)
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
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SupportingDefinitionProperties supportingDefinitionProperties)
                {
                    handler.attachSupportingDefinition(userId,
                                                       governanceDefinitionOneGUID,
                                                       governanceDefinitionTwoGUID,
                                                       relationshipTypeName,
                                                       requestBody,
                                                       supportingDefinitionProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.attachSupportingDefinition(userId,
                                                       governanceDefinitionOneGUID,
                                                       governanceDefinitionTwoGUID,
                                                       relationshipTypeName,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SupportingDefinitionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.attachSupportingDefinition(userId,
                                                   governanceDefinitionOneGUID,
                                                   governanceDefinitionTwoGUID,
                                                   relationshipTypeName,
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
     * Detach a governance definition from a supporting governance definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
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
    public VoidResponse detachSupportingDefinition(String                   serverName,
                                                   String                   urlMarker,
                                                   String                   governanceDefinitionOneGUID,
                                                   String                   governanceDefinitionTwoGUID,
                                                   String                   relationshipTypeName,
                                                   DeleteRequestBody requestBody)
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            handler.detachSupportingDefinition(userId,
                                               governanceDefinitionOneGUID,
                                               governanceDefinitionTwoGUID,
                                               relationshipTypeName,
                                               requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }




    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param definitionGUID identifier of the governance definition to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addGovernanceDefinitionToElement(String                  serverName,
                                                         String                   urlMarker,
                                                         String                  elementGUID,
                                                         String                  definitionGUID,
                                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addGovernanceDefinitionToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.addGovernanceDefinitionToElement(userId,
                                                         elementGUID,
                                                         definitionGUID,
                                                         null,
                                                         null);
            }
            else
            {
                if (requestBody.getProperties() instanceof GovernedByProperties governedByProperties)
                {
                    handler.addGovernanceDefinitionToElement(userId,
                                                             elementGUID,
                                                             definitionGUID,
                                                             requestBody,
                                                             governedByProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addGovernanceDefinitionToElement(userId,
                                                             elementGUID,
                                                             definitionGUID,
                                                             requestBody,
                                                             null);
                }
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
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param definitionGUID identifier of the governance definition to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeGovernanceDefinitionFromElement(String            serverName,
                                                              String            urlMarker,
                                                              String            elementGUID,
                                                              String            definitionGUID,
                                                              DeleteRequestBody requestBody)
    {
        final String methodName = "removeGovernanceDefinitionFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            handler.removeGovernanceDefinitionFromElement(userId, elementGUID, definitionGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Attach a product manager to a digital product.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param scopeElementGUID            unique identifier of the element
     * @param actorGUID unique identifier of the actor
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkAssignmentScope(String                     serverName,
                                            String                     urlMarker,
                                            String                     scopeElementGUID,
                                            String                     actorGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAssignmentScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssignmentScopeProperties properties)
                {
                    handler.linkAssignmentScope(userId,
                                               scopeElementGUID,
                                               actorGUID,
                                               requestBody,
                                               properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkAssignmentScope(userId,
                                               scopeElementGUID,
                                               actorGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkAssignmentScope(userId,
                                           scopeElementGUID,
                                           actorGUID,
                                           requestBody,
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
     * Detach a product manager from a digital product.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param scopeElementGUID            unique identifier of the element
     * @param actorGUID unique identifier of the actor
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAssignmentScope(String            serverName,
                                              String            urlMarker,
                                              String            scopeElementGUID,
                                              String            actorGUID,
                                              DeleteRequestBody requestBody)
    {
        final String methodName = "detachAssignmentScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            handler.detachAssignmentScope(userId,
                                          scopeElementGUID,
                                          actorGUID,
                                          requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }





    /* =======================================
     * Licenses
     */

    /**
     * Link an element to a license type and include details of the license in the relationship properties.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element being licensed
     * @param licenseTypeGUID unique identifier for the license type
     * @param requestBody the properties of the license
     *
     * @return guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse licenseElement(String                     serverName,
                                       String                     urlMarker,
                                       String                     elementGUID,
                                       String                     licenseTypeGUID,
                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "licenseElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LicenseProperties properties)
                {
                    response.setGUID(handler.licenseElement(userId,
                                                            elementGUID,
                                                            licenseTypeGUID,
                                                            requestBody,
                                                            properties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LicenseTypeProperties.class.getName(), methodName);
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
     * Update the properties of a license.  Remember to include the licenseId in the properties if the element has multiple
     * licenses for the same license type.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param licenseGUID unique identifier for the license type
     * @param requestBody the properties of the license
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateLicense(String                        serverName,
                                      String                        urlMarker,
                                      String                        licenseGUID,
                                      UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateLicense";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LicenseProperties licenseProperties)
                {
                    handler.updateLicense(userId, licenseGUID, requestBody, licenseProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LicenseProperties.class.getName(), methodName);
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
     * Remove the license for an element.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param licenseGUID unique identifier for the license type
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse unlicenseElement(String            serverName,
                                         String            urlMarker,
                                         String            licenseGUID,
                                         DeleteRequestBody requestBody)
    {
        final String methodName = "unlicenseElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            handler.unlicenseElement(userId, licenseGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =======================================
     * Certifications
     */

    /**
     * Link an element to a certification type and include details of the certification in the relationship properties.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element being certified
     * @param certificationTypeGUID unique identifier for the certification type
     * @param requestBody the properties of the certification
     *
     * @return guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse certifyElement(String                     serverName,
                                       String                     urlMarker,
                                       String                     elementGUID,
                                       String                     certificationTypeGUID,
                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "certifyElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CertificationProperties certificationProperties)
                {
                    response.setGUID(handler.certifyElement(userId,
                                                            elementGUID,
                                                            certificationTypeGUID,
                                                            requestBody,
                                                            certificationProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CertificationTypeProperties.class.getName(), methodName);
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
     * Update the properties of a certification.  Remember to include the certificationId in the properties if the element has multiple
     * certifications for the same certification type.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param certificationGUID unique identifier for the certification type
     * @param requestBody the properties of the certification
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateCertification(String                        serverName,
                                            String                        urlMarker,
                                            String                        certificationGUID,
                                            UpdateRelationshipRequestBody requestBody)
    {
        final String methodName = "updateCertification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CertificationProperties properties)
                {
                    handler.updateCertification(userId,
                                                certificationGUID,
                                                requestBody,
                                                properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CertificationProperties.class.getName(), methodName);
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
     * Remove the certification for an element.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param certificationGUID unique identifier for the certification type
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse decertifyElement(String            serverName,
                                         String            urlMarker,
                                         String            certificationGUID,
                                         DeleteRequestBody requestBody)
    {
        final String methodName = "decertifyElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            handler.decertifyElement(userId, certificationGUID, requestBody);
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
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteGovernanceDefinition(String                   serverName,
                                                   String                   urlMarker,
                                                   String                   governanceDefinitionGUID,
                                                   DeleteRequestBody requestBody)
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            handler.deleteGovernanceDefinition(userId, governanceDefinitionGUID, requestBody);
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
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getGovernanceDefinitionsByName(String            serverName,
                                                                           String            urlMarker,
                                                                           FilterRequestBody requestBody)
    {
        final String methodName = "getGovernanceDefinitionsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getGovernanceDefinitionsByName(userId,
                                                                            requestBody.getFilter(),
                                                                            requestBody));
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
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getGovernanceDefinitionByGUID(String             serverName,
                                                                         String             urlMarker,
                                                                         String             governanceDefinitionGUID,
                                                                         GetRequestBody requestBody)
    {
        final String methodName = "getGovernanceDefinitionByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getGovernanceDefinitionByGUID(userId, governanceDefinitionGUID, requestBody));
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
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findGovernanceDefinitions(String                  serverName,
                                                                      String                  urlMarker,
                                                                      SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceDefinitions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findGovernanceDefinitions(userId,
                                                                       requestBody.getSearchString(),
                                                                       requestBody));
            }
            else
            {
                response.setElements(handler.findGovernanceDefinitions(userId, null, null));
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
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param requestBody additional query parameters
     *
     * @return governance definition and its linked elements or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public OpenMetadataRootElementResponse getGovernanceDefinitionInContext(String             serverName,
                                                                            String             urlMarker,
                                                                            String             governanceDefinitionGUID,
                                                                            ResultsRequestBody requestBody)
    {
        final String methodName = "getGovernanceDefinitionInContext";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getGovernanceDefinitionInContext(userId,
                                                                         governanceDefinitionGUID,
                                                                         requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a design object such as a solution component or governance definition to its implementation via the ImplementedBy relationship. Request body is optional.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID unique identifier of the design object such as a governance definition or solution component
     * @param implementationGUID unique identifier of the second governance definition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkDesignToImplementation(String                  serverName,
                                                   String                  urlMarker,
                                                   String                  designGUID,
                                                   String                  implementationGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDesignToImplementation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ImplementedByProperties implementedByProperties)
                {
                    handler.linkDesignToImplementation(userId,
                                                       designGUID,
                                                       implementationGUID,
                                                       requestBody,
                                                       implementedByProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkDesignToImplementation(userId,
                                                       designGUID,
                                                       implementationGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ImplementedByProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkDesignToImplementation(userId,
                                                   designGUID,
                                                   implementationGUID,
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
     * Detach a governance definition from its implementation.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID      unique identifier of the design object such as a governance definition or solution component
     * @param implementationGUID unique identifier of the second governance definition
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachDesignFromImplementation(String                   serverName,
                                                       String                   urlMarker,
                                                       String                   designGUID,
                                                       String                   implementationGUID,
                                                       DeleteRequestBody requestBody)
    {
        final String methodName = "detachDesignFromImplementation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            handler.detachDesignFromImplementation(userId, designGUID, implementationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Attach a design object such as a solution component or governance definition to one of its implementation resource via the ImplementationResource relationship. Request body is optional.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID unique identifier of the design object such as a governance definition or solution component
     * @param implementationResourceGUID unique identifier of the implementation resource
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkImplementationResource(String                  serverName,
                                                   String                  urlMarker,
                                                   String                  designGUID,
                                                   String                  implementationResourceGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkImplementationResource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ImplementationResourceProperties implementationResourceProperties)
                {
                    handler.linkImplementationResource(userId,
                                                       designGUID,
                                                       implementationResourceGUID,
                                                       requestBody,
                                                       implementationResourceProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkImplementationResource(userId,
                                                       designGUID,
                                                       implementationResourceGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ImplementedByProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkImplementationResource(userId,
                                                   designGUID,
                                                   implementationResourceGUID,
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
     * Detach a design object such as a governance definition or solution component from one of its implementation resources
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID      unique identifier of the design object such as a governance definition or solution component
     * @param implementationResourceGUID unique identifier of the implementation resource
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachImplementationResource(String                   serverName,
                                                     String                   urlMarker,
                                                     String                   designGUID,
                                                     String                   implementationResourceGUID,
                                                     DeleteRequestBody requestBody)
    {
        final String methodName = "detachImplementationResource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            handler.detachImplementationResource(userId, designGUID, implementationResourceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
