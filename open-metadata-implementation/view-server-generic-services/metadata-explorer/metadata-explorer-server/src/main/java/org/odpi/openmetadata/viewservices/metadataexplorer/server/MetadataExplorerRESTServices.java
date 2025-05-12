/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexplorer.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.metadataexplorer.handlers.OpenMetadataHandler;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The MetadataExplorerRESTServices provides the server-side implementation of the Metadata Explorer Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class MetadataExplorerRESTServices extends TokenController
{
    private static final MetadataExplorerInstanceHandler instanceHandler = new MetadataExplorerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(MetadataExplorerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public MetadataExplorerRESTServices()
    {
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the metadata element
     * @param viewServiceURLMarker  view service URL marker

     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementResponse getMetadataElementByGUID(String             serverName,
                                                                String             elementGUID,
                                                                String             viewServiceURLMarker,
                                                                AnyTimeRequestBody requestBody)
    {
        final String methodName = "getMetadataElementByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog                    auditLog = null;
        OpenMetadataElementResponse response = new OpenMetadataElementResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getMetadataElementByGUID(userId,
                                                                     elementGUID,
                                                                     requestBody.getForLineage(),
                                                                     requestBody.getForDuplicateProcessing(),
                                                                     requestBody.getAsOfTime(),
                                                                     requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getMetadataElementByGUID(userId,
                                                                     elementGUID,
                                                                     false,
                                                                     false,
                                                                     null,
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
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementResponse getMetadataElementByUniqueName(String          serverName,
                                                                      String          viewServiceURLMarker,
                                                                      NameRequestBody requestBody)
    {
        final String methodName = "getMetadataElementByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        OpenMetadataElementResponse response = new OpenMetadataElementResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setElement(handler.getMetadataElementByUniqueName(userId,
                                                                           requestBody.getName(),
                                                                           requestBody.getNamePropertyName(),
                                                                           requestBody.getForLineage(),
                                                                           requestBody.getForDuplicateProcessing(),
                                                                           requestBody.getAsOfTime(),
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
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element unique identifier (guid) or
     *  InvalidParameterException the unique identifier is null or not known or
     *  UserNotAuthorizedException the governance action service is not able to access the element or
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public GUIDResponse getMetadataElementGUIDByUniqueName(String          serverName,
                                                           String          viewServiceURLMarker,
                                                           NameRequestBody requestBody)
    {
        final String methodName = "getMetadataElementGUIDByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.getMetadataElementGUIDByUniqueName(userId,
                                                                            requestBody.getName(),
                                                                            requestBody.getNamePropertyName(),
                                                                            requestBody.getForLineage(),
                                                                            requestBody.getForDuplicateProcessing(),
                                                                            requestBody.getAsOfTime(),
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
     * Retrieve all the versions of an element.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the metadata element
     * @param viewServiceURLMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param oldestFirst  defining how the results should be ordered.
     * @param requestBody the time window required
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementsResponse getMetadataElementHistory(String             serverName,
                                                                  String             elementGUID,
                                                                  String             viewServiceURLMarker,
                                                                  int                startFrom,
                                                                  int                pageSize,
                                                                  boolean            oldestFirst,
                                                                  HistoryRequestBody requestBody)
    {
        final String methodName = "getMetadataElementHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        OpenMetadataElementsResponse response = new OpenMetadataElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getMetadataElementHistory(userId,
                                                                          elementGUID,
                                                                          requestBody.getFromTime(),
                                                                          requestBody.getToTime(),
                                                                          oldestFirst,
                                                                          requestBody.getForLineage(),
                                                                          requestBody.getForDuplicateProcessing(),
                                                                          requestBody.getEffectiveTime(),
                                                                          startFrom,
                                                                          pageSize));
            }
            else
            {
                response.setElementList(handler.getMetadataElementHistory(userId,
                                                                          elementGUID,
                                                                          null,
                                                                          null,
                                                                          oldestFirst,
                                                                          false,
                                                                          false,
                                                                          null,
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


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementsResponse findMetadataElementsWithString(String                  serverName,
                                                                       String                  viewServiceURLMarker,
                                                                       int                     startFrom,
                                                                       int                     pageSize,
                                                                       SearchStringRequestBody requestBody)
    {
        final String methodName = "findMetadataElementsWithString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        OpenMetadataElementsResponse response = new OpenMetadataElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setElementList(handler.findMetadataElementsWithString(userId,
                                                                               requestBody.getSearchString(),
                                                                               requestBody.getTypeName(),
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
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied anchorGUID.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker  view service URL marker
     * @param anchorGUID unique identifier of anchor
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public AnchorSearchMatchesResponse findElementsForAnchor(String                  serverName,
                                                             String                  viewServiceURLMarker,
                                                             String                  anchorGUID,
                                                             int                     startFrom,
                                                             int                     pageSize,
                                                             SearchStringRequestBody requestBody)
    {
        final String methodName = "findElementsForAnchor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        AnchorSearchMatchesResponse response = new AnchorSearchMatchesResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setElement(handler.findElementsForAnchor(userId,
                                                                  requestBody.getSearchString(),
                                                                  anchorGUID,
                                                                  requestBody.getTypeName(),
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
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied domain name. The results are organized by anchor element.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker  view service URL marker
     * @param anchorDomainName name of open metadata type for the domain
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public AnchorSearchMatchesListResponse findElementsInAnchorDomain(String                  serverName,
                                                                      String                  viewServiceURLMarker,
                                                                      String                  anchorDomainName,
                                                                      int                     startFrom,
                                                                      int                     pageSize,
                                                                      SearchStringRequestBody requestBody)
    {
        final String methodName = "findElementsInAnchorDomain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        AnchorSearchMatchesListResponse response = new AnchorSearchMatchesListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setElements(handler.findElementsInAnchorDomain(userId,
                                                                        requestBody.getSearchString(),
                                                                        anchorDomainName,
                                                                        requestBody.getTypeName(),
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
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied scope guid. The results are organized by anchor element.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker  view service URL marker
     * @param anchorScopeGUID unique identifier of the scope to use
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public AnchorSearchMatchesListResponse findElementsInAnchorScope(String                  serverName,
                                                                     String                  viewServiceURLMarker,
                                                                     String                  anchorScopeGUID,
                                                                     int                     startFrom,
                                                                     int                     pageSize,
                                                                     SearchStringRequestBody requestBody)
    {
        final String methodName = "findElementsInAnchorScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        AnchorSearchMatchesListResponse response = new AnchorSearchMatchesListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setElements(handler.findElementsInAnchorScope(userId,
                                                                       requestBody.getSearchString(),
                                                                       anchorScopeGUID,
                                                                       requestBody.getTypeName(),
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
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the starting metadata element
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param viewServiceURLMarker  view service URL marker
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public RelatedMetadataElementListResponse getRelatedMetadataElements(String             serverName,
                                                                         String             elementGUID,
                                                                         String             relationshipTypeName,
                                                                         String             viewServiceURLMarker,
                                                                         int                startingAtEnd,
                                                                         int                startFrom,
                                                                         int                pageSize,
                                                                         ResultsRequestBody requestBody)
    {
        final String methodName = "getRelatedMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog                           auditLog = null;
        RelatedMetadataElementListResponse response = new RelatedMetadataElementListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setRelatedElementList(handler.getRelatedMetadataElements(userId,
                                                                                  elementGUID,
                                                                                  startingAtEnd,
                                                                                  relationshipTypeName,
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
                response.setRelatedElementList(handler.getRelatedMetadataElements(userId,
                                                                                  elementGUID,
                                                                                  startingAtEnd,
                                                                                  relationshipTypeName,
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  false,
                                                                                  false,
                                                                                  null,
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



    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     *
     * @param serverName name of the server instances for this request
     * @param viewServiceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param elementGUID  unique identifier for the element
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param requestBody effective time and asOfTime
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public OpenMetadataGraphResponse getAnchoredElementsGraph(String             serverName,
                                                              String             viewServiceURLMarker,
                                                              String             elementGUID,
                                                              int                startFrom,
                                                              int                pageSize,
                                                              AnyTimeRequestBody requestBody)
    {
        final String methodName    = "getAnchoredElementsGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataGraphResponse response = new OpenMetadataGraphResponse();
        AuditLog                  auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElementGraph(handler.getAnchoredElementsGraph(userId,
                                                                          elementGUID,
                                                                          requestBody.getForLineage(),
                                                                          requestBody.getForDuplicateProcessing(),
                                                                          startFrom,
                                                                          pageSize,
                                                                          requestBody.getAsOfTime(),
                                                                          requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElementGraph(handler.getAnchoredElementsGraph(userId,
                                                                          elementGUID,
                                                                          false,
                                                                          false,
                                                                          startFrom,
                                                                          pageSize,
                                                                          null,
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
     * Retrieve the relationships linking to the supplied elements.
     *
     * @param serverName     name of server instance to route request to
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataRelationshipListResponse getMetadataElementRelationships(String             serverName,
                                                                                String             metadataElementAtEnd1GUID,
                                                                                String             relationshipTypeName,
                                                                                String             metadataElementAtEnd2GUID,
                                                                                String             viewServiceURLMarker,
                                                                                int                startFrom,
                                                                                int                pageSize,
                                                                                ResultsRequestBody requestBody)
    {
        final String methodName = "getMetadataElementRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog                             auditLog = null;
        OpenMetadataRelationshipListResponse response = new OpenMetadataRelationshipListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setRelationshipList(handler.getMetadataElementRelationships(userId,
                                                                                     metadataElementAtEnd1GUID,
                                                                                     metadataElementAtEnd2GUID,
                                                                                     relationshipTypeName,
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
                response.setRelationshipList(handler.getMetadataElementRelationships(userId,
                                                                                     metadataElementAtEnd1GUID,
                                                                                     metadataElementAtEnd2GUID,
                                                                                     relationshipTypeName,
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     false,
                                                                                     false,
                                                                                     null,
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


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody properties defining the search criteria
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementsResponse findMetadataElements(String          serverName,
                                                             String          viewServiceURLMarker,
                                                             int             startFrom,
                                                             int             pageSize,
                                                             FindRequestBody requestBody)
    {
        final String methodName = "findMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        OpenMetadataElementsResponse response = new OpenMetadataElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setElementList(handler.findMetadataElements(userId,
                                                                     requestBody.getMetadataElementTypeName(),
                                                                     requestBody.getMetadataElementSubtypeNames(),
                                                                     requestBody.getSearchProperties(),
                                                                     requestBody.getLimitResultsByStatus(),
                                                                     requestBody.getAsOfTime(),
                                                                     requestBody.getMatchClassifications(),
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
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody properties defining the search criteria
     *
     * @return a list of relationships - null means no matching relationships - or
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataRelationshipListResponse findRelationshipsBetweenMetadataElements(String                      serverName,
                                                                                         String                      viewServiceURLMarker,
                                                                                         int                         startFrom,
                                                                                         int                         pageSize,
                                                                                         FindRelationshipRequestBody requestBody)
    {
        final String methodName = "findRelationshipsBetweenMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog                             auditLog = null;
        OpenMetadataRelationshipListResponse response = new OpenMetadataRelationshipListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setRelationshipList(handler.findRelationshipsBetweenMetadataElements(userId,
                                                                                              requestBody.getRelationshipTypeName(),
                                                                                              requestBody.getSearchProperties(),
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
     * Retrieve the relationship using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param relationshipGUID unique identifier for the relationship
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public OpenMetadataRelationshipResponse getRelationshipByGUID(String             serverName,
                                                                  String             relationshipGUID,
                                                                  String             viewServiceURLMarker,
                                                                  AnyTimeRequestBody requestBody)
    {
        final String methodName = "getRelationshipByGUID";
        final String guidParameterName = "relationshipGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog                         auditLog = null;
        OpenMetadataRelationshipResponse response = new OpenMetadataRelationshipResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getRelationshipByGUID(userId,
                                                                  relationshipGUID,
                                                                  requestBody.getForLineage(),
                                                                  requestBody.getForDuplicateProcessing(),
                                                                  requestBody.getAsOfTime(),
                                                                  requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getRelationshipByGUID(userId,
                                                                  relationshipGUID,
                                                                  false,
                                                                  false,
                                                                  null,
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
     * Retrieve all the versions of an element.
     *
     * @param serverName     name of server instance to route request to
     * @param relationshipGUID unique identifier for the metadata element
     * @param viewServiceURLMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param oldestFirst  defining how the results should be ordered.
     * @param requestBody the time window required
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataRelationshipListResponse getRelationshipHistory(String             serverName,
                                                                       String             relationshipGUID,
                                                                       String             viewServiceURLMarker,
                                                                       int                startFrom,
                                                                       int                pageSize,
                                                                       boolean            oldestFirst,
                                                                       HistoryRequestBody requestBody)
    {
        final String methodName = "getRelationshipHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        OpenMetadataRelationshipListResponse response = new OpenMetadataRelationshipListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setRelationshipList(handler.getRelationshipHistory(userId,
                                                                            relationshipGUID,
                                                                            requestBody.getFromTime(),
                                                                            requestBody.getToTime(),
                                                                            oldestFirst,
                                                                            requestBody.getForLineage(),
                                                                            requestBody.getForDuplicateProcessing(),
                                                                            requestBody.getEffectiveTime(),
                                                                            startFrom,
                                                                            pageSize));
            }
            else
            {
                response.setRelationshipList(handler.getRelationshipHistory(userId,
                                                                            relationshipGUID,
                                                                            null,
                                                                            null,
                                                                            oldestFirst,
                                                                            false,
                                                                            false,
                                                                            null,
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
