/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DesignPatternHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.InformationSupplyChainHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SolutionComponentHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.DesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.NestedDesignPatternProperties;
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createInformationSupplyChain(String                serverName,
                                                     NewElementRequestBody requestBody)
    {
        final String methodName = "createInformationSupplyChain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    response.setGUID(handler.createCollection(userId,
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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createInformationSupplyChainFromTemplate(String              serverName,
                                                                 TemplateRequestBody requestBody)
    {
        final String methodName = "createInformationSupplyChainFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

                response.setGUID(handler.createCollectionFromTemplate(userId,
                                                                      requestBody,
                                                                      requestBody.getTemplateGUID(),
                                                                      requestBody.getReplacementProperties(),
                                                                      requestBody.getReplacementClassifications(),
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of an information supply chain.
     *
     * @param serverName         name of called server.
     * @param informationSupplyChainGUID unique identifier of the information supply chain (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateInformationSupplyChain(String                   serverName,
                                                        String                   informationSupplyChainGUID,
                                                        UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateInformationSupplyChain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

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
                    response.setFlag(handler.updateCollection(userId,
                                                              informationSupplyChainGUID,
                                                              requestBody,
                                                              informationSupplyChainProperties));
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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkPeersInInformationSupplyChain(String                     serverName,
                                                          String                     peerOneGUID,
                                                          String                     peerTwoGUID,
                                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkPeersInInformationSupplyChain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse unlinkPeersInInformationSupplyChain(String                        serverName,
                                                            String                        peerOneGUID,
                                                            String                        peerTwoGUID,
                                                            DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "unlinkPeersInInformationSupplyChain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteInformationSupplyChain(String                   serverName,
                                                     String                   informationSupplyChainGUID,
                                                     DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteInformationSupplyChain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            InformationSupplyChainHandler handler = instanceHandler.getInformationSupplyChainHandler(userId, serverName, methodName);

            handler.deleteCollection(userId, informationSupplyChainGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getInformationSupplyChainsByName(String            serverName,
                                                                            boolean           addImplementation,
                                                                            FilterRequestBody requestBody)
    {
        final String methodName = "getInformationSupplyChainsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findInformationSupplyChains(String                  serverName,
                                                                        boolean                 addImplementation,
                                                                        SearchStringRequestBody requestBody)
    {
        final String methodName = "findInformationSupplyChains";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getInformationSupplyChainByGUID(String         serverName,
                                                                           String         informationSupplyChainGUID,
                                                                           boolean        addImplementation,
                                                                           GetRequestBody requestBody)
    {
        final String methodName = "getInformationSupplyChainByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createSolutionBlueprint(String                serverName,
                                                NewElementRequestBody requestBody)
    {
        final String methodName = "createSolutionBlueprint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollectionHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof SolutionBlueprintProperties properties)
                {
                    response.setGUID(handler.createCollection(userId,
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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSolutionBlueprintFromTemplate(String              serverName,
                                                            TemplateRequestBody requestBody)
    {
        final String methodName = "createSolutionBlueprintFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollectionHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

                response.setGUID(handler.createCollectionFromTemplate(userId,
                                                                      requestBody,
                                                                      requestBody.getTemplateGUID(),
                                                                      requestBody.getReplacementProperties(),
                                                                      requestBody.getReplacementClassifications(),
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a solution blueprint.
     *
     * @param serverName         name of called server.
     * @param solutionBlueprintGUID unique identifier of the solution blueprint (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateSolutionBlueprint(String                   serverName,
                                                   String                   solutionBlueprintGUID,
                                                   UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSolutionBlueprint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollectionHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof SolutionBlueprintProperties properties)
                {
                    response.setFlag(handler.updateCollection(userId, solutionBlueprintGUID, requestBody, properties));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a solution blueprint to the element that it describes.
     *
     * @param serverName         name of called server
     * @param parentGUID  unique identifier of the element being described
     * @param solutionBlueprintGUID      unique identifier of the  solution blueprint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSolutionDesign(String                     serverName,
                                           String                     parentGUID,
                                           String                     solutionBlueprintGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSolutionDesign";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSolutionDesign(String                        serverName,
                                             String                        parentGUID,
                                             String                        solutionBlueprintGUID,
                                             DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSolutionDesign";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            handler.detachSolutionDesign(userId,
                                         parentGUID,
                                         solutionBlueprintGUID,
                                         requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSolutionBlueprint(String                   serverName,
                                                String                   solutionBlueprintGUID,
                                                DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteSolutionBlueprint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            handler.deleteCollection(userId, solutionBlueprintGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getSolutionBlueprintsByName(String            serverName,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getSolutionBlueprintsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getCollectionsByName(userId,
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of solution blueprint metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param solutionBlueprintGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getSolutionBlueprintByGUID(String         serverName,
                                                                      String         solutionBlueprintGUID,
                                                                      GetRequestBody requestBody)
    {
        final String methodName = "getSolutionBlueprintByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            response.setElement(handler.getCollectionByGUID(userId,
                                                            solutionBlueprintGUID,
                                                            requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findSolutionBlueprints(String                  serverName,
                                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findSolutionBlueprints";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollectionHandler handler = instanceHandler.getSolutionBlueprintHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findCollections(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findCollections(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSolutionComponentActor(String                     serverName,
                                                   String                     solutionRoleGUID,
                                                   String                     solutionComponentGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSolutionComponentActor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSolutionComponentActor(String                        serverName,
                                                     String                        solutionRoleGUID,
                                                     String                        solutionComponentGUID,
                                                     DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSolutionComponentActor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createSolutionComponent(String                serverName,
                                                NewElementRequestBody requestBody)
    {
        final String methodName = "createSolutionComponent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSolutionComponentFromTemplate(String              serverName,
                                                            TemplateRequestBody requestBody)
    {
        final String methodName = "createSolutionComponentFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                                                                             requestBody.getReplacementClassifications(),
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a solution component.
     *
     * @param serverName         name of called server.
     * @param solutionComponentGUID unique identifier of the solution component (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateSolutionComponent(String                   serverName,
                                                   String                   solutionComponentGUID,
                                                   UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSolutionComponent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

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
                    response.setFlag(handler.updateSolutionComponent(userId,
                                                                     solutionComponentGUID,
                                                                     requestBody,
                                                                     properties));
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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSubcomponent(String                     serverName,
                                         String                     parentSolutionComponentGUID,
                                         String                     nestedSolutionComponentGUID,
                                         NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSubcomponent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSubcomponent(String                        serverName,
                                           String                        parentSolutionComponentGUID,
                                           String                        nestedSolutionComponentGUID,
                                           DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSubcomponent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSolutionLinkingWire(String                     serverName,
                                                String                     solutionComponentOneGUID,
                                                String                     solutionComponentTwoGUID,
                                                NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSolutionLinkingWire";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSolutionLinkingWire(String                        serverName,
                                                  String                        solutionComponentOneGUID,
                                                  String                        solutionComponentTwoGUID,
                                                  DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSolutionLinkingWire";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSolutionComponent(String                   serverName,
                                                String                   solutionComponentGUID,
                                                DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteSolutionComponent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getSolutionComponentsByName(String            serverName,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getSolutionComponentsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getSolutionComponentByGUID(String         serverName,
                                                                      String         solutionComponentGUID,
                                                                      GetRequestBody requestBody)
    {
        final String methodName = "getSolutionComponentByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findSolutionComponents(String                  serverName,
                                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findSolutionComponents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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

        restCallLogger.logRESTCallReturn(token, response);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementsResponse getSolutionComponentImplementations(String             serverName,
                                                                               String             solutionComponentGUID,
                                                                               ResultsRequestBody requestBody)
    {
        final String methodName = "getSolutionComponentImplementations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        RelatedMetadataElementsResponse response = new RelatedMetadataElementsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionComponentHandler handler = instanceHandler.getSolutionComponentHandler(userId, serverName, methodName);

            response.setElements(handler.getSolutionComponentImplementations(userId,
                                                                             solutionComponentGUID,
                                                                             requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a new design pattern.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody details of the design pattern
     *
     * @return unique identifier of the design pattern or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDesignPattern(String                serverName,
                                            NewElementRequestBody requestBody)
    {
        final String methodName = "createDesignPattern";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            DesignPatternProperties properties = null;

            if (requestBody.getProperties() instanceof DesignPatternProperties designPatternProperties)
            {
                properties = designPatternProperties;
            }

            response.setGUID(handler.createDesignPattern(userId,
                                                         requestBody,
                                                         requestBody.getInitialClassifications(),
                                                         properties,
                                                         requestBody.getParentRelationshipProperties()));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a new design pattern using a template.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody details of the template and overrides
     *
     * @return unique identifier of the design pattern or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createDesignPatternFromTemplate(String              serverName,
                                                        TemplateRequestBody requestBody)
    {
        final String methodName = "createDesignPatternFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            response.setGUID(handler.createDesignPatternFromTemplate(userId,
                                                                     requestBody,
                                                                     requestBody.getTemplateGUID(),
                                                                     requestBody.getReplacementProperties(),
                                                                     requestBody.getReplacementClassifications(),
                                                                     requestBody.getPlaceholderPropertyValues(),
                                                                     requestBody.getParentRelationshipProperties()));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update an existing design pattern.
     *
     * @param serverName name of the service to route the request to
     * @param designPatternGUID unique identifier of the design pattern to update
     * @param requestBody details of the design pattern
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public BooleanResponse updateDesignPattern(String                   serverName,
                                               String                   designPatternGUID,
                                               UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateDesignPattern";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            DesignPatternProperties properties = null;

            if (requestBody.getProperties() instanceof DesignPatternProperties designPatternProperties)
            {
                properties = designPatternProperties;
            }

            response.setFlag(handler.updateDesignPattern(userId, designPatternGUID, requestBody, properties));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Link two design patterns together as parent and child.
     *
     * @param serverName name of the service to route the request to
     * @param parentDesignPatternGUID unique identifier of the parent design pattern
     * @param nestedDesignPatternGUID unique identifier of the child design pattern
     * @param requestBody properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkNestedDesignPatterns(String                   serverName,
                                                 String                   parentDesignPatternGUID,
                                                 String                   nestedDesignPatternGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkNestedDesignPatterns";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            NestedDesignPatternProperties properties = null;

            if (requestBody.getProperties() instanceof NestedDesignPatternProperties relationshipProperties)
            {
                properties = relationshipProperties;
            }

            handler.linkNestedDesignPatterns(userId,
                                             parentDesignPatternGUID,
                                             nestedDesignPatternGUID,
                                             requestBody,
                                             properties);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the link between two design patterns.
     *
     * @param serverName name of the service to route the request to
     * @param parentDesignPatternGUID unique identifier of the parent design pattern
     * @param nestedDesignPatternGUID unique identifier of the child design pattern
     * @param requestBody options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachNestedDesignPatterns(String                      serverName,
                                                   String                      parentDesignPatternGUID,
                                                   String                      nestedDesignPatternGUID,
                                                   DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachNestedDesignPatterns";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            handler.detachNestedDesignPatterns(userId, parentDesignPatternGUID, nestedDesignPatternGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Link two design patterns together as general and specialized.
     *
     * @param serverName name of the service to route the request to
     * @param generalizedDesignPatternGUID unique identifier of the generalized design pattern
     * @param specializedDesignPatternGUID unique identifier of the specialized design pattern
     * @param requestBody properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkSpecializedDesignPatterns(String                   serverName,
                                                      String                   generalizedDesignPatternGUID,
                                                      String                   specializedDesignPatternGUID,
                                                      NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkSpecializedDesignPatterns";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            NestedDesignPatternProperties properties = null;

            if (requestBody.getProperties() instanceof NestedDesignPatternProperties relationshipProperties)
            {
                properties = relationshipProperties;
            }

            handler.linkSpecializedDesignPatterns(userId,
                                                  generalizedDesignPatternGUID,
                                                  specializedDesignPatternGUID,
                                                  requestBody,
                                                  properties);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the link between two design patterns.
     *
     * @param serverName name of the service to route the request to
     * @param generalizedDesignPatternGUID unique identifier of the generalized design pattern
     * @param specializedDesignPatternGUID unique identifier of the specialized design pattern
     * @param requestBody options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachSpecializedDesignPatterns(String                      serverName,
                                                        String                      generalizedDesignPatternGUID,
                                                        String                      specializedDesignPatternGUID,
                                                        DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachSpecializedDesignPatterns";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            handler.detachSpecializedDesignPatterns(userId, generalizedDesignPatternGUID, specializedDesignPatternGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Link two design patterns together as related.
     *
     * @param serverName name of the service to route the request to
     * @param designPatternOneGUID unique identifier of the first design pattern
     * @param designPatternTwoGUID unique identifier of the second design pattern
     * @param requestBody properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse linkRelatedDesignPatterns(String                   serverName,
                                                  String                   designPatternOneGUID,
                                                  String                   designPatternTwoGUID,
                                                  NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkRelatedDesignPatterns";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            NestedDesignPatternProperties properties = null;

            if (requestBody.getProperties() instanceof NestedDesignPatternProperties relationshipProperties)
            {
                properties = relationshipProperties;
            }

            handler.linkRelatedDesignPatterns(userId,
                                              designPatternOneGUID,
                                              designPatternTwoGUID,
                                              requestBody,
                                              properties);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the link between two design patterns.
     *
     * @param serverName name of the service to route the request to
     * @param designPatternOneGUID unique identifier of the first design pattern
     * @param designPatternTwoGUID unique identifier of the second design pattern
     * @param requestBody options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse detachRelatedDesignPatterns(String                      serverName,
                                                    String                      designPatternOneGUID,
                                                    String                      designPatternTwoGUID,
                                                    DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachRelatedDesignPatterns";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            handler.detachRelatedDesignPatterns(userId, designPatternOneGUID, designPatternTwoGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Delete an existing design pattern.
     *
     * @param serverName name of the service to route the request to
     * @param designPatternGUID unique identifier of the design pattern to delete
     * @param requestBody options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public VoidResponse deleteDesignPattern(String                  serverName,
                                            String                  designPatternGUID,
                                            DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteDesignPattern";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            handler.deleteDesignPattern(userId, designPatternGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of design patterns with a matching name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody search string and paging options
     *
     * @return list of design patterns or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getDesignPatternsByName(String            serverName,
                                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getDesignPatternsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDesignPatternsByName(userId, requestBody.getFilter(), requestBody));
            }
            else
            {
                response.setElements(handler.getDesignPatternsByName(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the design pattern with the matching unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param designPatternGUID unique identifier of the design pattern to retrieve
     * @param requestBody options for the get
     *
     * @return design pattern or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getDesignPatternByGUID(String         serverName,
                                                                  String         designPatternGUID,
                                                                  GetRequestBody requestBody)
    {
        final String methodName = "getDesignPatternByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            response.setElement(handler.getDesignPatternByGUID(userId, designPatternGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of design patterns that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody search string and paging options
     *
     * @return list of design patterns or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findDesignPatterns(String                  serverName,
                                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findDesignPatterns";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DesignPatternHandler handler = instanceHandler.getDesignPatternHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findDesignPatterns(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findDesignPatterns(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
