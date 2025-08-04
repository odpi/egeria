/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceofficer.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SupportingDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementationResourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
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
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody             properties for the governance definition.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createGovernanceDefinition(String                serverName,
                                                   String                viewServiceURLMarker,
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
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateGovernanceDefinition(String                   serverName,
                                                   String                   viewServiceURLMarker,
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
                GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
    public VoidResponse linkPeerDefinitions(String                  serverName,
                                            String                  viewServiceURLMarker,
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
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
    public VoidResponse detachPeerDefinitions(String                   serverName,
                                              String                   viewServiceURLMarker,
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
    public VoidResponse attachSupportingDefinition(String                  serverName,
                                                   String                  viewServiceURLMarker,
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
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
    public VoidResponse detachSupportingDefinition(String                   serverName,
                                                   String                   viewServiceURLMarker,
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * Delete a governance definition.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteGovernanceDefinition(String                   serverName,
                                                   String                   viewServiceURLMarker,
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getGovernanceDefinitionsByName(String            serverName,
                                                                           String            viewServiceURLMarker,
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getGovernanceDefinitionByGUID(String             serverName,
                                                                         String             viewServiceURLMarker,
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findGovernanceDefinitions(String                  serverName,
                                                                      String                  viewServiceURLMarker,
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * @param viewServiceURLMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param requestBody additional query parameters
     *
     * @return governance definition and its linked elements or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public OpenMetadataRootElementResponse getGovernanceDefinitionInContext(String             serverName,
                                                                            String             viewServiceURLMarker,
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * @param viewServiceURLMarker  view service URL marker
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
                                                   String                  viewServiceURLMarker,
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
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * @param viewServiceURLMarker  view service URL marker
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
                                                       String                   viewServiceURLMarker,
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * @param viewServiceURLMarker  view service URL marker
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
                                                   String                  viewServiceURLMarker,
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
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * @param viewServiceURLMarker  view service URL marker
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
                                                     String                   viewServiceURLMarker,
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

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
