/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server;


import org.odpi.openmetadata.accessservices.digitalarchitecture.client.SolutionManager;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


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
    public GUIDResponse createInformationSupplyChain(String                               serverName,
                                                     NewInformationSupplyChainRequestBody requestBody)
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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createInformationSupplyChain(userId,
                                                                      requestBody.getExternalSourceGUID(),
                                                                      requestBody.getExternalSourceName(),
                                                                      requestBody.getAnchorGUID(),
                                                                      requestBody.getIsOwnAnchor(),
                                                                      requestBody.getAnchorScopeGUID(),
                                                                      requestBody.getProperties(),
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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createInformationSupplyChainFromTemplate(userId,
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
     * Update the properties of an information supply chain.
     *
     * @param serverName         name of called server.
     * @param informationSupplyChainGUID unique identifier of the information supply chain (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateInformationSupplyChain(String                                  serverName,
                                                       String                                  informationSupplyChainGUID,
                                                       boolean                                 replaceAllProperties,
                                                       UpdateInformationSupplyChainRequestBody requestBody)
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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                handler.updateInformationSupplyChain(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     informationSupplyChainGUID,
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
     * Create an information supply chain segment.
     *
     * @param serverName                 name of called server.
     * @param informationSupplyChainGUID unique identifier of optional parent information supply chain
     * @param requestBody             properties for the information supply chain.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createInformationSupplyChainSegment(String                                   serverName,
                                                            String                                   informationSupplyChainGUID,
                                                            InformationSupplyChainSegmentRequestBody requestBody)
    {
        final String methodName = "createInformationSupplyChainSegment";

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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createInformationSupplyChainSegment(userId,
                                                                             requestBody.getExternalSourceGUID(),
                                                                             requestBody.getExternalSourceName(),
                                                                             requestBody.getProperties(),
                                                                             informationSupplyChainGUID,
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
     * Update the properties of an information supply chain segment.
     *
     * @param serverName         name of called server.
     * @param segmentGUID unique identifier of the information supply chain segment (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateInformationSupplyChainSegment(String                                         serverName,
                                                              String                                         segmentGUID,
                                                              boolean                                        replaceAllProperties,
                                                              InformationSupplyChainSegmentRequestBody requestBody)
    {
        final String methodName = "updateInformationSupplyChainSegment";

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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                handler.updateInformationSupplyChainSegment(userId,
                                                            requestBody.getExternalSourceGUID(),
                                                            requestBody.getExternalSourceName(),
                                                            segmentGUID,
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
     * Connect two information supply chain segments.
     *
     * @param serverName         name of called server
     * @param segment1GUID  unique identifier of the first segment
     * @param segment2GUID      unique identifier of the second segment
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkSegments(String                                serverName,
                                     String                                segment1GUID,
                                     String                                segment2GUID,
                                     InformationSupplyChainLinkRequestBody requestBody)
    {
        final String methodName = "linkSegments";

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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                handler.linkSegments(userId,
                                     requestBody.getExternalSourceGUID(),
                                     requestBody.getExternalSourceName(),
                                     segment1GUID,
                                     segment2GUID,
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
     * Detach two information supply chain segments from one another.
     *
     * @param serverName         name of called server
     * @param segment1GUID  unique identifier of the first segment
     * @param segment2GUID      unique identifier of the second segment
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachSegments(String                    serverName,
                                       String                    segment1GUID,
                                       String                    segment2GUID,
                                       MetadataSourceRequestBody requestBody)
    {
        final String methodName = "detachSegments";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachSegments(userId,
                                       requestBody.getExternalSourceGUID(),
                                       requestBody.getExternalSourceName(),
                                       segment1GUID,
                                       segment2GUID,
                                       requestBody.getForLineage(),
                                       requestBody.getForDuplicateProcessing(),
                                       requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachSegments(userId,
                                       null,
                                       null,
                                       segment1GUID,
                                       segment2GUID,
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
     * Delete an information supply chain segment.
     *
     * @param serverName         name of called server
     * @param segmentGUID  unique identifier of the  segment
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteInformationSupplyChainSegment(String                    serverName,
                                                            String                    segmentGUID,
                                                            MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteInformationSupplyChainSegment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteInformationSupplyChainSegment(userId,
                                                            requestBody.getExternalSourceGUID(),
                                                            requestBody.getExternalSourceName(),
                                                            segmentGUID,
                                                            requestBody.getForLineage(),
                                                            requestBody.getForDuplicateProcessing(),
                                                            requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteInformationSupplyChainSegment(userId,
                                                            null,
                                                            null,
                                                            segmentGUID,
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
     * Delete an information supply chain.
     *
     * @param serverName         name of called server
     * @param informationSupplyChainGUID  unique identifier of the element to delete
     * @param cascadedDelete can information supply chains be deleted if segments are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteInformationSupplyChain(String                    serverName,
                                                     String                    informationSupplyChainGUID,
                                                     boolean                   cascadedDelete,
                                                     MetadataSourceRequestBody requestBody)
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

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteInformationSupplyChain(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     informationSupplyChainGUID,
                                                     cascadedDelete,
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteInformationSupplyChain(userId,
                                                     null,
                                                     null,
                                                     informationSupplyChainGUID,
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
     * Retrieve the list of information supply chain metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public InformationSupplyChainsResponse getInformationSupplyChainsByName(String            serverName,
                                                                            boolean           addImplementation,
                                                                            int               startFrom,
                                                                            int               pageSize,
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

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getInformationSupplyChainsByName(userId,
                                                                              requestBody.getFilter(),
                                                                              addImplementation,
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
     * Retrieve the list of information supply chain metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param addImplementation should details of the implementation of the information supply chain be extracted too?
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
    public InformationSupplyChainsResponse findInformationSupplyChains(String            serverName,
                                                                       boolean           addImplementation,
                                                                       boolean           startsWith,
                                                                       boolean           endsWith,
                                                                       boolean           ignoreCase,
                                                                       int               startFrom,
                                                                       int               pageSize,
                                                                       FilterRequestBody requestBody)
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

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findInformationSupplyChains(userId,
                                                                         instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                         addImplementation,
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
                response.setElements(handler.findInformationSupplyChains(userId,
                                                                         instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                                         addImplementation,
                                                                         null,
                                                                         null,
                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                         null,
                                                                         startFrom,
                                                                         pageSize,
                                                                         true,
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
                                                                          AnyTimeRequestBody requestBody)
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

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getInformationSupplyChainByGUID(userId,
                                                                            informationSupplyChainGUID,
                                                                            addImplementation,
                                                                            requestBody.getAsOfTime(),
                                                                            requestBody.getForLineage(),
                                                                            requestBody.getForDuplicateProcessing(),
                                                                            requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getInformationSupplyChainByGUID(userId,
                                                                            informationSupplyChainGUID,
                                                                            addImplementation,
                                                                            null,
                                                                            true,
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
    public GUIDResponse createSolutionBlueprint(String                      serverName,
                                                NewSolutionBlueprintRequestBody requestBody)
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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createSolutionBlueprint(userId,
                                                                 requestBody.getExternalSourceGUID(),
                                                                 requestBody.getExternalSourceName(),
                                                                 requestBody.getAnchorGUID(),
                                                                 requestBody.getIsOwnAnchor(),
                                                                 requestBody.getAnchorScopeGUID(),
                                                                 requestBody.getProperties(),
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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createSolutionBlueprintFromTemplate(userId,
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
     * Update the properties of a solution blueprint.
     *
     * @param serverName         name of called server.
     * @param solutionBlueprintGUID unique identifier of the solution blueprint (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSolutionBlueprint(String                             serverName,
                                                String                             solutionBlueprintGUID,
                                                boolean                            replaceAllProperties,
                                                UpdateSolutionBlueprintRequestBody requestBody)
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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                handler.updateSolutionBlueprint(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                solutionBlueprintGUID,
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
    public VoidResponse linkSolutionComponentToBlueprint(String                                  serverName,
                                                         String                                  parentSolutionBlueprintGUID,
                                                         String                                  nestedSolutionComponentGUID,
                                                         SolutionBlueprintCompositionRequestBody requestBody)
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
            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.linkSolutionComponentToBlueprint(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         parentSolutionBlueprintGUID,
                                                         nestedSolutionComponentGUID,
                                                         requestBody.getProperties(),
                                                         requestBody.getForLineage(),
                                                         requestBody.getForDuplicateProcessing(),
                                                         requestBody.getEffectiveTime());
            }
            else
            {
                handler.linkSolutionComponentToBlueprint(userId,
                                                         null,
                                                         null,
                                                         parentSolutionBlueprintGUID,
                                                         nestedSolutionComponentGUID,
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
    public VoidResponse detachSolutionComponentFromBlueprint(String                    serverName,
                                                             String                    parentSolutionBlueprintGUID,
                                                             String                    nestedSolutionComponentGUID,
                                                             MetadataSourceRequestBody requestBody)
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

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachSolutionComponentFromBlueprint(userId,
                                                             requestBody.getExternalSourceGUID(),
                                                             requestBody.getExternalSourceName(),
                                                             parentSolutionBlueprintGUID,
                                                             nestedSolutionComponentGUID,
                                                             requestBody.getForLineage(),
                                                             requestBody.getForDuplicateProcessing(),
                                                             requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachSolutionComponentFromBlueprint(userId,
                                                             null,
                                                             null,
                                                             parentSolutionBlueprintGUID,
                                                             nestedSolutionComponentGUID,
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
     * Delete a solution blueprint.
     *
     * @param serverName         name of called server
     * @param solutionBlueprintGUID  unique identifier of the element to delete
     * @param cascadedDelete can solution blueprints be deleted if solution components are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSolutionBlueprint(String                    serverName,
                                                String                    solutionBlueprintGUID,
                                                boolean                   cascadedDelete,
                                                MetadataSourceRequestBody requestBody)
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

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteSolutionBlueprint(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                solutionBlueprintGUID,
                                                cascadedDelete,
                                                requestBody.getForLineage(),
                                                requestBody.getForDuplicateProcessing(),
                                                requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteSolutionBlueprint(userId,
                                                null,
                                                null,
                                                solutionBlueprintGUID,
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
     * Retrieve the list of solution blueprint metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SolutionBlueprintsResponse getSolutionBlueprintsByName(String            serverName,
                                                                  int               startFrom,
                                                                  int               pageSize,
                                                                  FilterRequestBody requestBody)
    {
        final String methodName = "getSolutionBlueprintsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionBlueprintsResponse response = new SolutionBlueprintsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSolutionBlueprintsByName(userId,
                                                                         requestBody.getFilter(),
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
    public SolutionBlueprintResponse getSolutionBlueprintByGUID(String             serverName,
                                                                String             solutionComponentGUID,
                                                                AnyTimeRequestBody requestBody)
    {
        final String methodName = "getSolutionBlueprintByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionBlueprintResponse response = new SolutionBlueprintResponse();
        AuditLog                  auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getSolutionBlueprintByGUID(userId,
                                                                       solutionComponentGUID,
                                                                       requestBody.getAsOfTime(),
                                                                       requestBody.getForLineage(),
                                                                       requestBody.getForDuplicateProcessing(),
                                                                       requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getSolutionBlueprintByGUID(userId,
                                                                       solutionComponentGUID,
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
     * Retrieve the list of solution blueprint metadata elements that contain the search string.  The returned blueprints include a list of the components that are associated with it.
     *
     * @param serverName name of the service to route the request to
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
    public SolutionBlueprintsResponse findSolutionBlueprints(String            serverName,
                                                             boolean           startsWith,
                                                             boolean           endsWith,
                                                             boolean           ignoreCase,
                                                             int               startFrom,
                                                             int               pageSize,
                                                             FilterRequestBody requestBody)
    {
        final String methodName = "findSolutionBlueprints";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionBlueprintsResponse response = new SolutionBlueprintsResponse();
        AuditLog                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSolutionBlueprints(userId,
                                                                    instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
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
                response.setElements(handler.findSolutionBlueprints(userId,
                                                                    instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
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
     * Create a solution role.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the solution role.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createSolutionRole(String                  serverName,
                                           NewActorRoleRequestBody requestBody)
    {
        final String methodName = "createSolutionRole";

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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createSolutionRole(userId,
                                                            requestBody.getExternalSourceGUID(),
                                                            requestBody.getExternalSourceName(),
                                                            requestBody.getAnchorGUID(),
                                                            requestBody.getIsOwnAnchor(),
                                                            requestBody.getAnchorScopeGUID(),
                                                            requestBody.getProperties(),
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
     * Create a new metadata element to represent a solution role using an existing metadata element as a template.
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
    public GUIDResponse createSolutionRoleFromTemplate(String              serverName,
                                                       TemplateRequestBody requestBody)
    {
        final String methodName = "createSolutionRoleFromTemplate";

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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createSolutionRoleFromTemplate(userId,
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
     * Update the properties of a solution role.
     *
     * @param serverName         name of called server.
     * @param solutionRoleGUID unique identifier of the solution role (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSolutionRole(String                      serverName,
                                           String                     solutionRoleGUID,
                                           boolean                    replaceAllProperties,
                                           UpdateActorRoleRequestBody requestBody)
    {
        final String methodName = "updateSolutionRole";

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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                handler.updateSolutionRole(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           solutionRoleGUID,
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
    public VoidResponse linkSolutionComponentActor(String                            serverName,
                                                   String                            solutionRoleGUID,
                                                   String                            solutionComponentGUID,
                                                   SolutionComponentActorRequestBody requestBody)
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
            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.linkSolutionComponentActor(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   solutionRoleGUID,
                                                   solutionComponentGUID,
                                                   requestBody.getProperties(),
                                                   requestBody.getForLineage(),
                                                   requestBody.getForDuplicateProcessing(),
                                                   requestBody.getEffectiveTime());
            }
            else
            {
                handler.linkSolutionComponentActor(userId,
                                                   null,
                                                   null,
                                                   solutionRoleGUID,
                                                   solutionComponentGUID,
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
    public VoidResponse detachSolutionComponentActor(String                    serverName,
                                                     String                    solutionRoleGUID,
                                                     String                    solutionComponentGUID,
                                                     MetadataSourceRequestBody requestBody)
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

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachSolutionComponentActor(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     solutionRoleGUID,
                                                     solutionComponentGUID,
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachSolutionComponentActor(userId,
                                                     null,
                                                     null,
                                                     solutionRoleGUID,
                                                     solutionComponentGUID,
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
     * Delete a solution role.
     *
     * @param serverName         name of called server
     * @param solutionRoleGUID  unique identifier of the element to delete
     * @param cascadedDelete can solution roles be deleted if solution components are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSolutionRole(String                    serverName,
                                           String                    solutionRoleGUID,
                                           boolean                   cascadedDelete,
                                           MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteSolutionRole";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteSolutionRole(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           solutionRoleGUID,
                                           cascadedDelete,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteSolutionRole(userId,
                                           null,
                                           null,
                                           solutionRoleGUID,
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
     * Retrieve the list of solution role metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SolutionRolesResponse getSolutionRolesByName(String            serverName,
                                                        int               startFrom,
                                                        int               pageSize,
                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getSolutionRolesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionRolesResponse response = new SolutionRolesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSolutionRolesByName(userId,
                                                                    requestBody.getFilter(),
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
     * Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     *
     * @param serverName name of the service to route the request to
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
    public SolutionRolesResponse findSolutionRoles(String            serverName,
                                                   boolean           startsWith,
                                                   boolean           endsWith,
                                                   boolean           ignoreCase,
                                                   int               startFrom,
                                                   int               pageSize,
                                                   FilterRequestBody requestBody)
    {
        final String methodName = "findSolutionRoles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionRolesResponse response = new SolutionRolesResponse();
        AuditLog              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSolutionRoles(userId,
                                                               instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
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
                response.setElements(handler.findSolutionRoles(userId,
                                                               instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
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
     * Retrieve the list of solution role metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param solutionRoleGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SolutionRoleResponse getSolutionRoleByGUID(String             serverName,
                                                        String             solutionRoleGUID,
                                                        AnyTimeRequestBody requestBody)
    {
        final String methodName = "getSolutionRoleByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionRoleResponse response = new SolutionRoleResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getSolutionRoleByGUID(userId,
                                                                   solutionRoleGUID,
                                                                   requestBody.getAsOfTime(),
                                                                   requestBody.getForLineage(),
                                                                   requestBody.getForDuplicateProcessing(),
                                                                   requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getSolutionRoleByGUID(userId,
                                                                   solutionRoleGUID,
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
    public GUIDResponse createSolutionComponent(String                      serverName,
                                                NewSolutionComponentRequestBody requestBody)
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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createSolutionComponent(userId,
                                                                 requestBody.getExternalSourceGUID(),
                                                                 requestBody.getExternalSourceName(),
                                                                 requestBody.getAnchorGUID(),
                                                                 requestBody.getIsOwnAnchor(),
                                                                 requestBody.getAnchorScopeGUID(),
                                                                 requestBody.getProperties(),
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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                response.setGUID(handler.createSolutionComponentFromTemplate(userId,
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
     * Update the properties of a solution component.
     *
     * @param serverName         name of called server.
     * @param solutionComponentGUID unique identifier of the solution component (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSolutionComponent(String                         serverName,
                                                String                         solutionComponentGUID,
                                                boolean                        replaceAllProperties,
                                                UpdateSolutionComponentRequestBody requestBody)
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
                SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

                handler.updateSolutionComponent(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                solutionComponentGUID,
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
                                         RelationshipRequestBody requestBody)
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
            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.linkSubcomponent(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         parentSolutionComponentGUID,
                                         nestedSolutionComponentGUID,
                                         requestBody.getProperties(),
                                         requestBody.getForLineage(),
                                         requestBody.getForDuplicateProcessing(),
                                         requestBody.getEffectiveTime());
            }
            else
            {
                handler.linkSubcomponent(userId,
                                         null,
                                         null,
                                         parentSolutionComponentGUID,
                                         nestedSolutionComponentGUID,
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
    public VoidResponse detachSubcomponent(String                    serverName,
                                           String                    parentSolutionComponentGUID,
                                           String                    nestedSolutionComponentGUID,
                                           MetadataSourceRequestBody requestBody)
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

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachSubcomponent(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           parentSolutionComponentGUID,
                                           nestedSolutionComponentGUID,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachSubcomponent(userId,
                                           null,
                                           null,
                                           parentSolutionComponentGUID,
                                           nestedSolutionComponentGUID,
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
     * Delete a solution component.
     *
     * @param serverName         name of called server
     * @param solutionComponentGUID  unique identifier of the element to delete
     * @param cascadedDelete can solution components be deleted if solution components are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSolutionComponent(String                    serverName,
                                                String                    solutionComponentGUID,
                                                boolean                   cascadedDelete,
                                                MetadataSourceRequestBody requestBody)
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

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteSolutionComponent(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                solutionComponentGUID,
                                                cascadedDelete,
                                                requestBody.getForLineage(),
                                                requestBody.getForDuplicateProcessing(),
                                                requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteSolutionComponent(userId,
                                                null,
                                                null,
                                                solutionComponentGUID,
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
     * Retrieve the list of solution component metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SolutionComponentsResponse getSolutionComponentsByName(String            serverName,
                                                                  int               startFrom,
                                                                  int               pageSize,
                                                                  FilterRequestBody requestBody)
    {
        final String methodName = "getSolutionComponentsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionComponentsResponse response = new SolutionComponentsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSolutionComponentsByName(userId,
                                                                         requestBody.getFilter(),
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
    public SolutionComponentResponse getSolutionComponentByGUID(String             serverName,
                                                                String             solutionComponentGUID,
                                                                AnyTimeRequestBody requestBody)
    {
        final String methodName = "getSolutionComponentByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionComponentResponse response = new SolutionComponentResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getSolutionComponentByGUID(userId,
                                                                       solutionComponentGUID,
                                                                       requestBody.getAsOfTime(),
                                                                       requestBody.getForLineage(),
                                                                       requestBody.getForDuplicateProcessing(),
                                                                       requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getSolutionComponentByGUID(userId,
                                                                       solutionComponentGUID,
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
     * Retrieve the list of solution component metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.
     *
     * @param serverName name of the service to route the request to
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
    public SolutionComponentsResponse findSolutionComponents(String            serverName,
                                                             boolean           startsWith,
                                                             boolean           endsWith,
                                                             boolean           ignoreCase,
                                                             int               startFrom,
                                                             int               pageSize,
                                                             FilterRequestBody requestBody)
    {
        final String methodName = "findSolutionComponents";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SolutionComponentsResponse response = new SolutionComponentsResponse();
        AuditLog                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSolutionComponents(userId,
                                                                    instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
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
                response.setElements(handler.findSolutionComponents(userId,
                                                                    instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
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
     * Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.
     *
     * @param serverName name of the service to route the request to
     * @param solutionComponentGUID unique identifier of the solution component to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementsResponse getSolutionComponentImplementations(String             serverName,
                                                                               String             solutionComponentGUID,
                                                                               int                startFrom,
                                                                               int                pageSize,
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

            SolutionManager handler = instanceHandler.getSolutionManagerClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElementList(handler.getSolutionComponentImplementations(userId,
                                                                                    solutionComponentGUID,
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
            else
            {
                response.setElementList(handler.getSolutionComponentImplementations(userId,
                                                                                    solutionComponentGUID,
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
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
