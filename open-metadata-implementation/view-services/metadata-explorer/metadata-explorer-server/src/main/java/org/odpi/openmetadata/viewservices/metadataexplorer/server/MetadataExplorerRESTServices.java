/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexplorer.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.metadataexplorer.handlers.OpenMetadataHandler;
import org.slf4j.LoggerFactory;



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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementResponse getMetadataElementByGUID(String                   serverName,
                                                                String                   elementGUID,
                                                                String                   viewServiceURLMarker,
                                                                String                   accessServiceURLMarker,
                                                                boolean                  forLineage,
                                                                boolean                  forDuplicateProcessing,
                                                                EffectiveTimeRequestBody requestBody)
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

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getMetadataElementByGUID(userId,
                                                                     elementGUID,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getMetadataElementByGUID(userId,
                                                                     elementGUID,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     null));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementResponse getMetadataElementByUniqueName(String          serverName,
                                                                      String          viewServiceURLMarker,
                                                                      String          accessServiceURLMarker,
                                                                      boolean         forLineage,
                                                                      boolean         forDuplicateProcessing,
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
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                response.setElement(handler.getMetadataElementByUniqueName(userId,
                                                                           requestBody.getName(),
                                                                           requestBody.getNamePropertyName(),
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           requestBody.getEffectiveTime()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element unique identifier (guid) or
     *  InvalidParameterException the unique identifier is null or not known or
     *  UserNotAuthorizedException the governance action service is not able to access the element or
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public GUIDResponse getMetadataElementGUIDByUniqueName(String          serverName,
                                                           String          viewServiceURLMarker,
                                                           String          accessServiceURLMarker,
                                                           boolean         forLineage,
                                                           boolean         forDuplicateProcessing,
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
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                response.setGUID(handler.getMetadataElementGUIDByUniqueName(userId,
                                                                            requestBody.getName(),
                                                                            requestBody.getNamePropertyName(),
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            requestBody.getEffectiveTime()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve all the versions of an element.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the metadata element
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
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
                                                                  String             accessServiceURLMarker,
                                                                  boolean            forLineage,
                                                                  boolean            forDuplicateProcessing,
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

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getMetadataElementHistory(userId,
                                                                          elementGUID,
                                                                          requestBody.getFromTime(),
                                                                          requestBody.getToTime(),
                                                                          oldestFirst,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
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
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          null,
                                                                          startFrom,
                                                                          pageSize));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
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
                                                                       String                  accessServiceURLMarker,
                                                                       boolean                 forLineage,
                                                                       boolean                 forDuplicateProcessing,
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
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                response.setElementList(handler.findMetadataElementsWithString(userId,
                                                                               requestBody.getSearchString(),
                                                                               requestBody.getTypeName(),
                                                                               requestBody.getLimitResultsByStatus(),
                                                                               requestBody.getAsOfTime(),
                                                                               requestBody.getSequencingProperty(),
                                                                               requestBody.getSequencingOrder(),
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               requestBody.getEffectiveTime(),
                                                                               startFrom,
                                                                               pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
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
                                                                         String             accessServiceURLMarker,
                                                                         boolean            forLineage,
                                                                         boolean            forDuplicateProcessing,
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

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getRelatedMetadataElements(userId,
                                                                           elementGUID,
                                                                           startingAtEnd,
                                                                           relationshipTypeName,
                                                                           requestBody.getLimitResultsByStatus(),
                                                                           requestBody.getAsOfTime(),
                                                                           requestBody.getSequencingProperty(),
                                                                           requestBody.getSequencingOrder(),
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           requestBody.getEffectiveTime(),
                                                                           startFrom,
                                                                           pageSize));
            }
            else
            {
                response.setElementList(handler.getRelatedMetadataElements(userId,
                                                                           elementGUID,
                                                                           startingAtEnd,
                                                                           relationshipTypeName,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           null,
                                                                           startFrom,
                                                                           pageSize));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
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
                                                                                String             accessServiceURLMarker,
                                                                                boolean            forLineage,
                                                                                boolean            forDuplicateProcessing,
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

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getMetadataElementRelationships(userId,
                                                                                metadataElementAtEnd1GUID,
                                                                                relationshipTypeName,
                                                                                metadataElementAtEnd2GUID,
                                                                                requestBody.getLimitResultsByStatus(),
                                                                                requestBody.getAsOfTime(),
                                                                                requestBody.getSequencingProperty(),
                                                                                requestBody.getSequencingOrder(),
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                requestBody.getEffectiveTime(),
                                                                                startFrom,
                                                                                pageSize));
            }
            else
            {
                response.setElementList(handler.getMetadataElementRelationships(userId,
                                                                                metadataElementAtEnd1GUID,
                                                                                relationshipTypeName,
                                                                                metadataElementAtEnd2GUID,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                null,
                                                                                startFrom,
                                                                                pageSize));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
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
                                                             String          accessServiceURLMarker,
                                                             boolean         forLineage,
                                                             boolean         forDuplicateProcessing,
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
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                response.setElementList(handler.findMetadataElements(userId,
                                                                     requestBody.getMetadataElementTypeName(),
                                                                     requestBody.getMetadataElementSubtypeNames(),
                                                                     requestBody.getSearchProperties(),
                                                                     requestBody.getLimitResultsByStatus(),
                                                                     requestBody.getAsOfTime(),
                                                                     requestBody.getMatchClassifications(),
                                                                     requestBody.getSequencingProperty(),
                                                                     requestBody.getSequencingOrder(),
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     requestBody.getEffectiveTime(),
                                                                     startFrom,
                                                                     pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
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
                                                                                         String                      accessServiceURLMarker,
                                                                                         boolean                     forLineage,
                                                                                         boolean                     forDuplicateProcessing,
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
                OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                response.setElementList(handler.findRelationshipsBetweenMetadataElements(userId,
                                                                                         requestBody.getRelationshipTypeName(),
                                                                                         requestBody.getSearchProperties(),
                                                                                         requestBody.getLimitResultsByStatus(),
                                                                                         requestBody.getAsOfTime(),
                                                                                         requestBody.getSequencingProperty(),
                                                                                         requestBody.getSequencingOrder(),
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         requestBody.getEffectiveTime(),
                                                                                         startFrom,
                                                                                         pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param relationshipGUID unique identifier for the relationship
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public OpenMetadataRelationshipResponse getRelationshipByGUID(String                   serverName,
                                                                  String                   relationshipGUID,
                                                                  String                   viewServiceURLMarker,
                                                                  String                   accessServiceURLMarker,
                                                                  boolean                  forLineage,
                                                                  boolean                  forDuplicateProcessing,
                                                                  EffectiveTimeRequestBody requestBody)
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

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.getRelationshipByGUID(userId,
                                              relationshipGUID,
                                              forLineage,
                                              forDuplicateProcessing,
                                              requestBody.getEffectiveTime());
            }
            else
            {
                handler.getRelationshipByGUID(userId,
                                              relationshipGUID,
                                              forLineage,
                                              forDuplicateProcessing,
                                              null);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Retrieve all the versions of an element.
     *
     * @param serverName     name of server instance to route request to
     * @param relationshipGUID unique identifier for the metadata element
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
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
                                                                       String             accessServiceURLMarker,
                                                                       boolean            forLineage,
                                                                       boolean            forDuplicateProcessing,
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

            OpenMetadataHandler handler = instanceHandler.getOpenMetadataHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getRelationshipHistory(userId,
                                                                       relationshipGUID,
                                                                       requestBody.getFromTime(),
                                                                       requestBody.getToTime(),
                                                                       oldestFirst,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       requestBody.getEffectiveTime(),
                                                                       startFrom,
                                                                       pageSize));
            }
            else
            {
                response.setElementList(handler.getRelationshipHistory(userId,
                                                                       relationshipGUID,
                                                                       null,
                                                                       null,
                                                                       oldestFirst,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       null,
                                                                       startFrom,
                                                                       pageSize));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

}
