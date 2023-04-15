/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossarybrowser.server;


import org.odpi.openmetadata.accessservices.assetmanager.client.management.GlossaryManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermActivityType;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermRelationshipStatus;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermStatus;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryCategoryElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryCategoryElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryNameRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossarySearchStringRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermStatusListResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.HistoryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NameRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The GlossaryBrowserRESTServices provides the implementation of the Glossary Browser Open Metadata View Service (OMVS).
 * This interface provides view interfaces for glossary UIs.
 */

public class GlossaryBrowserRESTServices
{
    private static final GlossaryBrowserInstanceHandler instanceHandler = new GlossaryBrowserInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GlossaryBrowserRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public GlossaryBrowserRESTServices()
    {
    }


    /**
     * Return the list of glossary term status enum values.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @return list of enum values
     */
    public GlossaryTermStatusListResponse getGlossaryTermStatuses(String serverName,
                                                                  String userId)
    {
        final String methodName = "getGlossaryTermStatuses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermStatusListResponse response = new GlossaryTermStatusListResponse();
        AuditLog                       auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setStatuses(Arrays.asList(GlossaryTermStatus.values()));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of glossary term relationship status enum values.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @return list of enum values
     */
    public GlossaryTermRelationshipStatusListResponse getGlossaryTermRelationshipStatuses(String serverName,
                                                                                          String userId)
    {
        final String methodName = "getGlossaryTermStatuses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermRelationshipStatusListResponse response = new GlossaryTermRelationshipStatusListResponse();
        AuditLog                       auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setStatuses(Arrays.asList(GlossaryTermRelationshipStatus.values()));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of glossary term relationship status enum values.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @return list of enum values
     */
    public GlossaryTermActivityTypeListResponse getGlossaryTermActivityTypes(String serverName,
                                                                             String userId)
    {
        final String methodName = "getGlossaryTermStatuses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermActivityTypeListResponse response = new GlossaryTermActivityTypeListResponse();
        AuditLog                             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setTypes(Arrays.asList(GlossaryTermActivityType.values()));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of glossary metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementsResponse findGlossaries(String                  serverName,
                                                   String                  userId,
                                                   int                     startFrom,
                                                   int                     pageSize,
                                                   boolean                 startsWith,
                                                   boolean                 endsWith,
                                                   boolean                 ignoreCase,
                                                   boolean                 forLineage,
                                                   boolean                 forDuplicateProcessing,
                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findGlossaries";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryElementsResponse response = new GlossaryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setElementList(handler.findGlossaries(userId,
                                                               instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                               startFrom,
                                                               pageSize,
                                                               requestBody.getEffectiveTime(),
                                                               forLineage,
                                                               forDuplicateProcessing));
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
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementsResponse   getGlossariesByName(String                  serverName,
                                                          String                  userId,
                                                          int                     startFrom,
                                                          int                     pageSize,
                                                          boolean                 forLineage,
                                                          boolean                 forDuplicateProcessing,
                                                          NameRequestBody requestBody)
    {
        final String methodName = "getGlossariesByName";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryElementsResponse response = new GlossaryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setElementList(handler.getGlossariesByName(userId,
                                                                    requestBody.getName(),
                                                                    startFrom,
                                                                    pageSize,
                                                                    requestBody.getEffectiveTime(),
                                                                    forLineage,
                                                                    forDuplicateProcessing));
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
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementResponse getGlossaryByGUID(String                        serverName,
                                                     String                        userId,
                                                     String                        glossaryGUID,
                                                     boolean                       forLineage,
                                                     boolean                       forDuplicateProcessing,
                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryElementResponse response = new GlossaryElementResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryByGUID(userId,
                                                              glossaryGUID,
                                                              requestBody.getEffectiveTime(),
                                                              forLineage,
                                                              forDuplicateProcessing));
            }
            else
            {
                response.setElement(handler.getGlossaryByGUID(userId,
                                                              glossaryGUID,
                                                              null,
                                                              forLineage,
                                                              forDuplicateProcessing));
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
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody string to find in the properties and correlators
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementsResponse findGlossaryCategories(String                          serverName,
                                                                   String                          userId,
                                                                   int                             startFrom,
                                                                   int                             pageSize,
                                                                   boolean                         startsWith,
                                                                   boolean                         endsWith,
                                                                   boolean                         ignoreCase,
                                                                   boolean                         forLineage,
                                                                   boolean                         forDuplicateProcessing,
                                                                   GlossarySearchStringRequestBody requestBody)
    {
        final String methodName = "findGlossaryCategories";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setElementList(handler.findGlossaryCategories(userId,
                                                                       requestBody.getGlossaryGUID(),
                                                                       instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                                       startFrom,
                                                                       pageSize,
                                                                       requestBody.getEffectiveTime(),
                                                                       forLineage,
                                                                       forDuplicateProcessing));
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
     * Return the list of categories associated with a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of metadata elements describing the categories associated with the requested glossary or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementsResponse getCategoriesForGlossary(String                        serverName,
                                                                     String                        userId,
                                                                     String                        glossaryGUID,
                                                                     int                           startFrom,
                                                                     int                           pageSize,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getCategoriesForGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getCategoriesForGlossary(userId,
                                                                         glossaryGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         requestBody.getEffectiveTime(),
                                                                         forLineage,
                                                                         forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getCategoriesForGlossary(userId,
                                                                         glossaryGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         null,
                                                                         forLineage,
                                                                         forDuplicateProcessing));
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
     * Retrieve the list of glossary category metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param requestBody name to search for and correlators
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementsResponse   getGlossaryCategoriesByName(String                  serverName,
                                                                          String                  userId,
                                                                          int                     startFrom,
                                                                          int                     pageSize,
                                                                          boolean                 forLineage,
                                                                          boolean                 forDuplicateProcessing,
                                                                          GlossaryNameRequestBody requestBody)
    {
        final String methodName = "getGlossaryCategoriesByName";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setElementList(handler.getGlossaryCategoriesByName(userId,
                                                                            requestBody.getGlossaryGUID(),
                                                                            requestBody.getName(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getEffectiveTime(),
                                                                            forLineage,
                                                                            forDuplicateProcessing));
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
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementResponse getGlossaryCategoryByGUID(String                        serverName,
                                                                     String                        userId,
                                                                     String                        glossaryCategoryGUID,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryCategoryByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementResponse response = new GlossaryCategoryElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryCategoryByGUID(userId,
                                                                      glossaryCategoryGUID,
                                                                      requestBody.getEffectiveTime(),
                                                                      forLineage,
                                                                      forDuplicateProcessing));
            }
            else
            {
                response.setElement(handler.getGlossaryCategoryByGUID(userId,
                                                                      glossaryCategoryGUID,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing));
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
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return parent glossary category element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementResponse getGlossaryCategoryParent(String                        serverName,
                                                                     String                        userId,
                                                                     String                        glossaryCategoryGUID,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryCategoryParent";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementResponse response = new GlossaryCategoryElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryCategoryParent(userId,
                                                                      glossaryCategoryGUID,
                                                                      requestBody.getEffectiveTime(),
                                                                      forLineage,
                                                                      forDuplicateProcessing));
            }
            else
            {
                response.setElement(handler.getGlossaryCategoryParent(userId,
                                                                      glossaryCategoryGUID,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing));
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
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of glossary category elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementsResponse getGlossarySubCategories(String                        serverName,
                                                                     String                        userId,
                                                                     String                        glossaryCategoryGUID,
                                                                     int                           startFrom,
                                                                     int                           pageSize,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossarySubCategories";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getGlossarySubCategories(userId,
                                                                         glossaryCategoryGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         requestBody.getEffectiveTime(),
                                                                         forLineage,
                                                                         forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getGlossarySubCategories(userId,
                                                                         glossaryCategoryGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         null,
                                                                         forLineage,
                                                                         forDuplicateProcessing));
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
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers and search string
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementsResponse findGlossaryTerms(String                          serverName,
                                                          String                          userId,
                                                          int                             startFrom,
                                                          int                             pageSize,
                                                          boolean                         startsWith,
                                                          boolean                         endsWith,
                                                          boolean                         ignoreCase,
                                                          boolean                         forLineage,
                                                          boolean                         forDuplicateProcessing,
                                                          GlossarySearchStringRequestBody requestBody)
    {
        final String methodName = "findGlossaryTerms";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setElementList(handler.findGlossaryTerms(userId,
                                                                  requestBody.getGlossaryGUID(),
                                                                  instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                                  requestBody.getLimitResultsByStatus(),
                                                                  startFrom,
                                                                  pageSize,
                                                                  requestBody.getEffectiveTime(),
                                                                  forLineage,
                                                                  forDuplicateProcessing));
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
     * Retrieve the list of glossary terms associated with a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementsResponse getTermsForGlossary(String                        serverName,
                                                            String                        userId,
                                                            String                        glossaryGUID,
                                                            int                           startFrom,
                                                            int                           pageSize,
                                                            boolean                       forLineage,
                                                            boolean                       forDuplicateProcessing,
                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getTermsForGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getTermsForGlossary(userId,
                                                                    glossaryGUID,
                                                                    startFrom,
                                                                    pageSize,
                                                                    requestBody.getEffectiveTime(),
                                                                    forLineage,
                                                                    forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getTermsForGlossary(userId,
                                                                    glossaryGUID,
                                                                    startFrom,
                                                                    pageSize,
                                                                    null,
                                                                    forLineage,
                                                                    forDuplicateProcessing));
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
     * Retrieve the list of glossary terms associated with a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the glossary category of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementsResponse getTermsForGlossaryCategory(String                        serverName,
                                                                    String                        userId,
                                                                    String                        glossaryCategoryGUID,
                                                                    int                           startFrom,
                                                                    int                           pageSize,
                                                                    boolean                       forLineage,
                                                                    boolean                       forDuplicateProcessing,
                                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getTermsForGlossaryCategory";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getTermsForGlossaryCategory(userId,
                                                                            glossaryCategoryGUID,
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getEffectiveTime(),
                                                                            forLineage,
                                                                            forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getTermsForGlossaryCategory(userId,
                                                                            glossaryCategoryGUID,
                                                                            startFrom,
                                                                            pageSize,
                                                                            null,
                                                                            forLineage,
                                                                            forDuplicateProcessing));
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
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers and name
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementsResponse   getGlossaryTermsByName(String                  serverName,
                                                                 String                  userId,
                                                                 int                     startFrom,
                                                                 int                     pageSize,
                                                                 boolean                 forLineage,
                                                                 boolean                 forDuplicateProcessing,
                                                                 GlossaryNameRequestBody requestBody)
    {
        final String methodName = "getGlossaryTermsByName";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setElementList(handler.getGlossaryTermsByName(userId,
                                                                       requestBody.getGlossaryGUID(),
                                                                       requestBody.getName(),
                                                                       requestBody.getLimitResultsByStatus(),
                                                                       startFrom,
                                                                       pageSize,
                                                                       requestBody.getEffectiveTime(),
                                                                       forLineage,
                                                                       forDuplicateProcessing));
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
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementResponse getGlossaryTermByGUID(String                        serverName,
                                                             String                        userId,
                                                             String                        guid,
                                                             boolean                       forLineage,
                                                             boolean                       forDuplicateProcessing,
                                                             EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryTermByGUID";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementResponse response = new GlossaryTermElementResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryTermByGUID(userId,
                                                                  guid,
                                                                  requestBody.getEffectiveTime(),
                                                                  forLineage,
                                                                  forDuplicateProcessing));
            }
            else
            {
                response.setElement(handler.getGlossaryTermByGUID(userId,
                                                                  guid,
                                                                  null,
                                                                  forLineage,
                                                                  forDuplicateProcessing));
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
     * Retrieve all the versions of a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param guid unique identifier of object to retrieve
     * @param startFrom the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param oldestFirst  defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param requestBody the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return list of beans or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GlossaryTermElementsResponse getGlossaryTermHistory(String             serverName,
                                                               String             userId,
                                                               String             guid,
                                                               int                startFrom,
                                                               int                pageSize,
                                                               boolean            oldestFirst,
                                                               boolean            forLineage,
                                                               boolean            forDuplicateProcessing,
                                                               HistoryRequestBody requestBody)
    {
        final String methodName = "getGlossaryTermHistory";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setElementList(handler.getGlossaryTermHistory(userId,
                                                                       guid,
                                                                       requestBody.getFromTime(),
                                                                       requestBody.getToTime(),
                                                                       startFrom,
                                                                       pageSize,
                                                                       oldestFirst,
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

}
