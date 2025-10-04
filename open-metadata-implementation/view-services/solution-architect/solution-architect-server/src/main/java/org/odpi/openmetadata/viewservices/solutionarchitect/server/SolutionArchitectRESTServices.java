/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.InformationSupplyChainHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SolutionBlueprintHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SolutionComponentHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.RelatedMetadataElementsResponse;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The SolutionArchitectRESTServices provides the server-side implementation of the Solution Architect Open Metadata
 * View Service (OMVS).  This interface provides access to information supply chains, solution blueprints,
 * solution components and solution roles.
 */
public class SolutionArchitectRESTServices extends TokenController
{
    private static final SolutionArchitectInstanceHandler instanceHandler = new SolutionArchitectInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SolutionArchitectRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public SolutionArchitectRESTServices()
    {
    }


    /**
     * Create an information supply chain.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the information supply chain.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createInformationSupplyChain(String                serverName,
                                                     NewElementRequestBody requestBody)
    {
        final String methodName = "createInformationSupplyChain";

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
                InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof InformationSupplyChainProperties informationSupplyChainProperties)
                {
                    response.setGUID(handler.createInformationSupplyChain(userId,
                                                                          requestBody,
                                                                          requestBody.getInitialClassifications(),
                                                                          informationSupplyChainProperties,
                                                                          requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(InformationSupplyChainProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent an information supply chain using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createInformationSupplyChainFromTemplate(String              serverName,
                                                                 TemplateRequestBody requestBody)
    {
        final String methodName = "createInformationSupplyChainFromTemplate";

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
                InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

                response.setGUID(handler.createInformationSupplyChainFromTemplate(userId,
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
     * Update the properties of an information supply chain.
     *
     * @param serverName         name of called server.
     * @param informationSupplyChainGUID unique identifier of the information supply chain (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateInformationSupplyChain(String                   serverName,
                                                       String                   informationSupplyChainGUID,
                                                       UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateInformationSupplyChain";

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
                InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof InformationSupplyChainProperties informationSupplyChainProperties)
                {
                    handler.updateInformationSupplyChain(userId,
                                                         informationSupplyChainGUID,
                                                         requestBody,
                                                         informationSupplyChainProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateInformationSupplyChain(userId,
                                                         informationSupplyChainGUID,
                                                         requestBody,
                                                         null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(InformationSupplyChainProperties.class.getName(), methodName);
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
     * Connect two peers in an information supply chains.  The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.
     *
     * @param serverName   name of called server
     * @param peerOneGUID  unique identifier of the end one element in the relationship
     * @param peerTwoGUID  unique identifier of the end two element in the relationship
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkPeersInInformationSupplyChain(String                  serverName,
                                                          String                  peerOneGUID,
                                                          String                  peerTwoGUID,
                                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkPeersInInformationSupplyChain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

            if (requestBody != null)
            {

                if (requestBody.getProperties() instanceof InformationSupplyChainLinkProperties properties)
                {
                    handler.linkPeersInInformationSupplyChain(userId,
                                                              peerOneGUID,
                                                              peerTwoGUID,
                                                              requestBody,
                                                              properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkPeersInInformationSupplyChain(userId,
                                                              peerOneGUID,
                                                              peerTwoGUID,
                                                              requestBody,
                                                              null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(InformationSupplyChainLinkProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkPeersInInformationSupplyChain(userId,
                                                          peerOneGUID,
                                                          peerTwoGUID,
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
     * Detach two peers in an information supply chain from one another.    The linked elements are of type 'Referenceable' to allow significant data stores to be included in the definition of the information supply chain.
     *
     * @param serverName         name of called server
     * @param peerOneGUID  unique identifier of the end one element in the relationship
     * @param peerTwoGUID  unique identifier of the end two element in the relationship
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse unlinkPeersInInformationSupplyChain(String                   serverName,
                                                            String                   peerOneGUID,
                                                            String                   peerTwoGUID,
                                                            DeleteRequestBody requestBody)
    {
        final String methodName = "unlinkPeersInInformationSupplyChain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

            handler.unlinkPeersInInformationSupplyChain(userId,
                                                        peerOneGUID,
                                                        peerTwoGUID,
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
     * Connect a nested information supply chain to its parent.
     *
     * @param serverName         name of called server
     * @param informationSupplyChainGUID  unique identifier of the parent information supply chain
     * @param nestedInformationSupplyChainGUID      unique identifier of the child information supply chain
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse composeInformationSupplyChains(String                  serverName,
                                                       String                  informationSupplyChainGUID,
                                                       String                  nestedInformationSupplyChainGUID,
                                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "composeInformationSupplyChains";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CollectionMembershipProperties properties)
                {
                    handler.composeInformationSupplyChains(userId,
                                                           informationSupplyChainGUID,
                                                           nestedInformationSupplyChainGUID,
                                                           requestBody,
                                                           properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.composeInformationSupplyChains(userId,
                                                           informationSupplyChainGUID,
                                                           nestedInformationSupplyChainGUID,
                                                           requestBody,
                                                           null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.composeInformationSupplyChains(userId,
                                                       informationSupplyChainGUID,
                                                       nestedInformationSupplyChainGUID,
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
     * Detach a nested information supply chain from its parent.
     *
     * @param serverName         name of called server
     * @param informationSupplyChainGUID  unique identifier of the parent information supply chain
     * @param nestedInformationSupplyChainGUID      unique identifier of the child information supply chain
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse decomposeInformationSupplyChains(String                   serverName,
                                                         String                   informationSupplyChainGUID,
                                                         String                   nestedInformationSupplyChainGUID,
                                                         DeleteRequestBody requestBody)
    {
        final String methodName = "decomposeInformationSupplyChains";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

            handler.decomposeInformationSupplyChains(userId,
                                                     informationSupplyChainGUID,
                                                     nestedInformationSupplyChainGUID,
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
     * Delete an information supply chain.
     *
     * @param serverName         name of called server
     * @param informationSupplyChainGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteInformationSupplyChain(String                   serverName,
                                                     String                   informationSupplyChainGUID,
                                                     DeleteRequestBody requestBody)
    {
        final String methodName = "deleteInformationSupplyChain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

            handler.deleteInformationSupplyChain(userId, informationSupplyChainGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of information supply chain metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public InformationSupplyChainsResponse getInformationSupplyChainsByName(String            serverName,
                                                                            boolean           addImplementation,
                                                                            FilterRequestBody requestBody)
    {
        final String methodName = "getInformationSupplyChainsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformationSupplyChainsResponse response = new InformationSupplyChainsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getInformationSupplyChainsByName(userId,
                                                                              requestBody.getFilter(),
                                                                              requestBody,
                                                                              addImplementation));
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
     * Retrieve the list of information supply chain metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public InformationSupplyChainsResponse findInformationSupplyChains(String                  serverName,
                                                                       boolean                 addImplementation,
                                                                       SearchStringRequestBody requestBody)
    {
        final String methodName = "findInformationSupplyChains";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformationSupplyChainsResponse response = new InformationSupplyChainsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findInformationSupplyChains(userId,
                                                                         requestBody.getSearchString(),
                                                                         addImplementation,
                                                                         requestBody));
            }
            else
            {
                response.setElements(handler.findInformationSupplyChains(userId,
                                                                         null,
                                                                         addImplementation,
                                                                         null));
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
     * Retrieve the list of information supply chain metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param informationSupplyChainGUID    unique identifier of the required element
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param requestBody time values for the query
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public InformationSupplyChainResponse getInformationSupplyChainByGUID(String             serverName,
                                                                          String             informationSupplyChainGUID,
                                                                          boolean            addImplementation,
                                                                          GetRequestBody requestBody)
    {
        final String methodName = "getInformationSupplyChainByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformationSupplyChainResponse response = new InformationSupplyChainResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

            response.setElement(handler.getInformationSupplyChainByGUID(userId,
                                                                        informationSupplyChainGUID,
                                                                        addImplementation,
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
     * Create a solution blueprint.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the solution blueprint.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createSolutionBlueprint(String                serverName,
                                                NewElementRequestBody requestBody)
    {
        final String methodName = "createSolutionBlueprint";

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
                SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof SolutionBlueprintProperties properties)
                {
                    response.setGUID(handler.createSolutionBlueprint(userId,
                                                                     requestBody,
                                                                     requestBody.getInitialClassifications(),
                                                                     properties,
                                                                     requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a solution blueprint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSolutionBlueprintFromTemplate(String              serverName,
                                                            TemplateRequestBody requestBody)
    {
        final String methodName = "createSolutionBlueprintFromTemplate";

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
                SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

                response.setGUID(handler.createSolutionBlueprintFromTemplate(userId,
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
     * Update the properties of a solution blueprint.
     *
     * @param serverName         name of called server.
     * @param solutionBlueprintGUID unique identifier of the solution blueprint (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSolutionBlueprint(String                   serverName,
                                                String                   solutionBlueprintGUID,
                                                UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSolutionBlueprint";

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
                SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof SolutionBlueprintProperties properties)
                {
                    handler.updateSolutionBlueprint(userId,
                                                    solutionBlueprintGUID,
                                                    requestBody,
                                                    properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateSolutionBlueprint(userId,
                                                    solutionBlueprintGUID,
                                                    requestBody,
                                                    null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionBlueprintProperties.class.getName(), methodName);
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
     * Attach a solution component to a solution blueprint.
     *
     * @param serverName         name of called server
     * @param parentSolutionBlueprintGUID    unique identifier of the parent solution blueprint.
     * @param nestedSolutionComponentGUID    unique identifier of the nested solution component.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSolutionComponentToBlueprint(String                     serverName,
                                                         String                     parentSolutionBlueprintGUID,
                                                         String                     nestedSolutionComponentGUID,
                                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSolutionComponentToBlueprint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CollectionMembershipProperties properties)
                {
                    handler.linkSolutionComponentToBlueprint(userId,
                                                             parentSolutionBlueprintGUID,
                                                             nestedSolutionComponentGUID,
                                                             requestBody,
                                                             properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSolutionComponentToBlueprint(userId,
                                                             parentSolutionBlueprintGUID,
                                                             nestedSolutionComponentGUID,
                                                             requestBody,
                                                             null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CollectionMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSolutionComponentToBlueprint(userId,
                                                         parentSolutionBlueprintGUID,
                                                         nestedSolutionComponentGUID,
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
     * Detach a solution component from a solution blueprint.
     *
     * @param serverName         name of called server
     * @param parentSolutionBlueprintGUID    unique identifier of the parent solution blueprint.
     * @param nestedSolutionComponentGUID    unique identifier of the nested solution component.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSolutionComponentFromBlueprint(String                   serverName,
                                                             String                   parentSolutionBlueprintGUID,
                                                             String                   nestedSolutionComponentGUID,
                                                             DeleteRequestBody requestBody)
    {
        final String methodName = "detachSolutionComponentFromBlueprint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            handler.detachSolutionComponentFromBlueprint(userId,
                                                         parentSolutionBlueprintGUID,
                                                         nestedSolutionComponentGUID,
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
     * Attach a solution blueprint to the element that is describes.
     *
     * @param serverName         name of called server
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the  solution blueprint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSolutionDesign(String                  serverName,
                                           String                  parentGUID,
                                           String                  solutionBlueprintGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSolutionDesign";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SolutionDesignProperties solutionDesignProperties)
                {
                    handler.linkSolutionDesign(userId,
                                               parentGUID,
                                               solutionBlueprintGUID,
                                               requestBody,
                                               solutionDesignProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSolutionDesign(userId,
                                               parentGUID,
                                               solutionBlueprintGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionDesignProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSolutionDesign(userId,
                                           parentGUID,
                                           solutionBlueprintGUID,
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
     * Detach a solution blueprint from the element it describes.
     *
     * @param serverName         name of called server
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the  solution blueprint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSolutionDesign(String                   serverName,
                                             String                   parentGUID,
                                             String                   solutionBlueprintGUID,
                                             DeleteRequestBody requestBody)
    {
        final String methodName = "detachSolutionDesign";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            handler.detachSolutionDesign(userId,
                                         parentGUID,
                                         solutionBlueprintGUID,
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
     * Delete a solution blueprint.
     *
     * @param serverName         name of called server
     * @param solutionBlueprintGUID  unique identifier of the element to delete
     * @param requestBody  description of the delete method.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSolutionBlueprint(String                   serverName,
                                                String                   solutionBlueprintGUID,
                                                DeleteRequestBody requestBody)
    {
        final String methodName = "deleteSolutionBlueprint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            handler.deleteSolutionBlueprint(userId, solutionBlueprintGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of solution blueprint metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getSolutionBlueprintsByName(String            serverName,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getSolutionBlueprintsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSolutionBlueprintsByName(userId,
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
     * Retrieve the list of solution blueprint metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param solutionComponentGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getSolutionBlueprintByGUID(String         serverName,
                                                                      String         solutionComponentGUID,
                                                                      GetRequestBody requestBody)
    {
        final String methodName = "getSolutionBlueprintByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            response.setElement(handler.getSolutionBlueprintByGUID(userId,
                                                                   solutionComponentGUID,
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
     * Retrieve the list of solution blueprint metadata elements that contain the search string.  The returned blueprints include a list of the components that are associated with it.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findSolutionBlueprints(String                  serverName,
                                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findSolutionBlueprints";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionBlueprintHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSolutionBlueprints(userId,
                                                                    requestBody.getSearchString(),
                                                                    requestBody));
            }
            else
            {
                response.setElements(handler.findSolutionBlueprints(userId,
                                                                    null,
                                                                    null));
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
     * Attach a solution component to a solution role.
     *
     * @param serverName         name of called server
     * @param solutionRoleGUID    unique identifier of the parent solution role.
     * @param solutionComponentGUID    unique identifier of the nested solution component.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSolutionComponentActor(String                  serverName,
                                                   String                  solutionRoleGUID,
                                                   String                  solutionComponentGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSolutionComponentActor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SolutionComponentActorProperties properties)
                {
                    handler.linkSolutionComponentActor(userId,
                                                       solutionRoleGUID,
                                                       solutionComponentGUID,
                                                       requestBody,
                                                       properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSolutionComponentActor(userId,
                                                       solutionRoleGUID,
                                                       solutionComponentGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionComponentActorProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSolutionComponentActor(userId,
                                                   solutionRoleGUID,
                                                   solutionComponentGUID,
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
     * Detach a solution component from a solution role.
     *
     * @param serverName         name of called server
     * @param solutionRoleGUID    unique identifier of the parent solution role.
     * @param solutionComponentGUID    unique identifier of the nested solution component.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSolutionComponentActor(String                   serverName,
                                                     String                   solutionRoleGUID,
                                                     String                   solutionComponentGUID,
                                                     DeleteRequestBody requestBody)
    {
        final String methodName = "detachSolutionComponentActor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            handler.detachSolutionComponentActor(userId,
                                                 solutionRoleGUID,
                                                 solutionComponentGUID,
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
     * Create a solution component.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the solution component.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createSolutionComponent(String                serverName,
                                                NewElementRequestBody requestBody)
    {
        final String methodName = "createSolutionComponent";

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
                SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof SolutionComponentProperties properties)
                {
                    response.setGUID(handler.createSolutionComponent(userId,
                                                                     requestBody,
                                                                     requestBody.getInitialClassifications(),
                                                                     properties,
                                                                     requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionComponentProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a solution component using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSolutionComponentFromTemplate(String              serverName,
                                                            TemplateRequestBody requestBody)
    {
        final String methodName = "createSolutionComponentFromTemplate";

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
                SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

                response.setGUID(handler.createSolutionComponentFromTemplate(userId,
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
     * Update the properties of a solution component.
     *
     * @param serverName         name of called server.
     * @param solutionComponentGUID unique identifier of the solution component (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSolutionComponent(String                   serverName,
                                                String                   solutionComponentGUID,
                                                UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSolutionComponent";

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
                SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof SolutionComponentProperties properties)
                {
                    handler.updateSolutionComponent(userId,
                                                    solutionComponentGUID,
                                                    requestBody,
                                                    properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateSolutionComponent(userId,
                                                    solutionComponentGUID,
                                                    requestBody,
                                                    null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionComponentProperties.class.getName(), methodName);
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
     * Attach a solution component to a solution component.
     *
     * @param serverName         name of called server
     * @param parentSolutionComponentGUID    unique identifier of the parent solution component.
     * @param nestedSolutionComponentGUID    unique identifier of the nested solution component.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSubcomponent(String                  serverName,
                                         String                  parentSolutionComponentGUID,
                                         String                  nestedSolutionComponentGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSubcomponent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SolutionCompositionProperties solutionCompositionProperties)
                {
                    handler.linkSubcomponent(userId,
                                             parentSolutionComponentGUID,
                                             nestedSolutionComponentGUID,
                                             requestBody,
                                             solutionCompositionProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSubcomponent(userId,
                                             parentSolutionComponentGUID,
                                             nestedSolutionComponentGUID,
                                             requestBody,
                                             null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionCompositionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSubcomponent(userId,
                                         parentSolutionComponentGUID,
                                         nestedSolutionComponentGUID,
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
     * Detach a solution component from a solution component.
     *
     * @param serverName         name of called server
     * @param parentSolutionComponentGUID    unique identifier of the parent solution component.
     * @param nestedSolutionComponentGUID    unique identifier of the nested solution component.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSubcomponent(String                   serverName,
                                           String                   parentSolutionComponentGUID,
                                           String                   nestedSolutionComponentGUID,
                                           DeleteRequestBody requestBody)
    {
        final String methodName = "detachSubcomponent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            handler.detachSubcomponent(userId,
                                       parentSolutionComponentGUID,
                                       nestedSolutionComponentGUID,
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
     * Attach a solution component to a solution component.
     *
     * @param serverName         name of called server
     * @param solutionComponentOneGUID unique identifier of the solution component at end 1
     * @param solutionComponentTwoGUID unique identifier of the solution component at end 2
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSolutionLinkingWire(String                  serverName,
                                                String                  solutionComponentOneGUID,
                                                String                  solutionComponentTwoGUID,
                                                NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSolutionLinkingWire";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SolutionLinkingWireProperties solutionLinkingWireProperties)
                {
                    handler.linkSolutionLinkingWire(userId,
                                                    solutionComponentOneGUID,
                                                    solutionComponentTwoGUID,
                                                    requestBody,
                                                    solutionLinkingWireProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkSolutionLinkingWire(userId,
                                                    solutionComponentOneGUID,
                                                    solutionComponentTwoGUID,
                                                    requestBody,
                                                    null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SolutionLinkingWireProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkSolutionLinkingWire(userId,
                                                solutionComponentOneGUID,
                                                solutionComponentTwoGUID,
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
     * Detach a solution component from a solution component.
     *
     * @param serverName         name of called server
     * @param solutionComponentOneGUID unique identifier of the solution component at end 1
     * @param solutionComponentTwoGUID unique identifier of the solution component at end 2
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSolutionLinkingWire(String                   serverName,
                                                  String                   solutionComponentOneGUID,
                                                  String                   solutionComponentTwoGUID,
                                                  DeleteRequestBody requestBody)
    {
        final String methodName = "detachSolutionLinkingWire";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            handler.detachSolutionLinkingWire(userId,
                                              solutionComponentOneGUID,
                                              solutionComponentTwoGUID,
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
     * Delete a solution component.
     *
     * @param serverName         name of called server
     * @param solutionComponentGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSolutionComponent(String                   serverName,
                                                String                   solutionComponentGUID,
                                                DeleteRequestBody requestBody)
    {
        final String methodName = "deleteSolutionComponent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            handler.deleteSolutionComponent(userId, solutionComponentGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of solution component metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getSolutionComponentsByName(String            serverName,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getSolutionComponentsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSolutionComponentsByName(userId,
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
     * Retrieve the list of solution component metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param solutionComponentGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getSolutionComponentByGUID(String         serverName,
                                                                      String         solutionComponentGUID,
                                                                      GetRequestBody requestBody)
    {
        final String methodName = "getSolutionComponentByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            response.setElement(handler.getSolutionComponentByGUID(userId, solutionComponentGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Retrieve the list of solution component metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findSolutionComponents(String                  serverName,
                                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findSolutionComponents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSolutionComponents(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findSolutionComponents(userId, null, null));
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
     * Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.
     *
     * @param serverName name of the service to route the request to
     * @param solutionComponentGUID unique identifier of the solution component to query
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementsResponse getSolutionComponentImplementations(String             serverName,
                                                                               String             solutionComponentGUID,
                                                                               ResultsRequestBody requestBody)
    {
        final String methodName = "getSolutionComponentImplementations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementsResponse response = new RelatedMetadataElementsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            response.setElementList(handler.getSolutionComponentImplementations(userId,
                                                                                solutionComponentGUID,
                                                                                requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
