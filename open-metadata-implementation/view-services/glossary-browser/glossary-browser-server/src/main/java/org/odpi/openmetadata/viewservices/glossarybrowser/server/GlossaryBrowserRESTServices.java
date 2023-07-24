/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossarybrowser.server;

import org.odpi.openmetadata.accessservices.assetmanager.client.management.CollaborationManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.GlossaryManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.StewardshipManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.CommentProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.DataFieldQueryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.FeedbackProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.FindNameProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermActivityType;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermRelationshipStatus;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermStatus;
import org.odpi.openmetadata.accessservices.assetmanager.properties.LevelIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RatingProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TagProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ElementStubsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryCategoryElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryCategoryElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceDefinitionsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteLogElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteLogElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.RelatedElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.FindByPropertiesRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryNameRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossarySearchStringRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermRelationshipRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.HistoryRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.InformalTagUpdateRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.ReferenceableUpdateRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.RelationshipRequestBody;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


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
     * Return the list of glossary term activity types enum values.
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
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
    public GlossaryElementsResponse   getGlossariesByName(String          serverName,
                                                          String          userId,
                                                          int             startFrom,
                                                          int             pageSize,
                                                          boolean         forLineage,
                                                          boolean         forDuplicateProcessing,
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
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NameRequestBody.class.getName());
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
     * Retrieve the glossary metadata element for the requested category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementResponse getGlossaryForCategory(String                        serverName,
                                                          String                        userId,
                                                          String                        glossaryCategoryGUID,
                                                          boolean                       forLineage,
                                                          boolean                       forDuplicateProcessing,
                                                          EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryForCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryElementResponse response = new GlossaryElementResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryForCategory(userId,
                                                                   glossaryCategoryGUID,
                                                                   requestBody.getEffectiveTime(),
                                                                   forLineage,
                                                                   forDuplicateProcessing));
            }
            else
            {
                response.setElement(handler.getGlossaryForCategory(userId,
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
     * Retrieve the glossary metadata element for the requested term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementResponse getGlossaryForTerm(String                        serverName,
                                                          String                        userId,
                                                          String                        glossaryTermGUID,
                                                          boolean                       forLineage,
                                                          boolean                       forDuplicateProcessing,
                                                          EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryForTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryElementResponse response = new GlossaryElementResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryForTerm(userId,
                                                               glossaryTermGUID,
                                                               requestBody.getEffectiveTime(),
                                                               forLineage,
                                                               forDuplicateProcessing));
            }
            else
            {
                response.setElement(handler.getGlossaryForTerm(userId,
                                                               glossaryTermGUID,
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
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, GlossarySearchStringRequestBody.class.getName());
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
     * Return the list of categories associated with a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary to query
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
    public GlossaryCategoryElementsResponse getCategoriesForTerm(String                        serverName,
                                                                 String                        userId,
                                                                 String                        glossaryTermGUID,
                                                                 int                           startFrom,
                                                                 int                           pageSize,
                                                                 boolean                       forLineage,
                                                                 boolean                       forDuplicateProcessing,
                                                                 EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getCategoriesForTerm";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getCategoriesForTerm(userId,
                                                                     glossaryTermGUID,
                                                                     startFrom,
                                                                     pageSize,
                                                                     requestBody.getEffectiveTime(),
                                                                     forLineage,
                                                                     forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getCategoriesForTerm(userId,
                                                                     glossaryTermGUID,
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
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, GlossaryNameRequestBody.class.getName());
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
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, GlossarySearchStringRequestBody.class.getName());
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
    public GlossaryTermElementsResponse getTermsForGlossaryCategory(String                              serverName,
                                                                    String                              userId,
                                                                    String                              glossaryCategoryGUID,
                                                                    int                                 startFrom,
                                                                    int                                 pageSize,
                                                                    boolean                             forLineage,
                                                                    boolean                             forDuplicateProcessing,
                                                                    GlossaryTermRelationshipRequestBody requestBody)
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
                                                                            requestBody.getLimitResultsByStatus(),
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
                                                                            null,
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
     * Retrieve the list of glossary terms associated with the requested term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary of interest
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
    public GlossaryTermElementsResponse getRelatedTerms(String                              serverName,
                                                        String                              userId,
                                                        String                              glossaryTermGUID,
                                                        int                                 startFrom,
                                                        int                                 pageSize,
                                                        boolean                             forLineage,
                                                        boolean                             forDuplicateProcessing,
                                                        GlossaryTermRelationshipRequestBody requestBody)
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
                response.setElementList(handler.getRelatedTerms(userId,
                                                                glossaryTermGUID,
                                                                requestBody.getLimitResultsByStatus(),
                                                                startFrom,
                                                                pageSize,
                                                                requestBody.getEffectiveTime(),
                                                                forLineage,
                                                                forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getRelatedTerms(userId,
                                                                glossaryTermGUID,
                                                                null,
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
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, GlossaryNameRequestBody.class.getName());
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
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, HistoryRequestBody.class.getName());
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
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param requestBody containing the StarRating and user review of referenceable (probably element).
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addRatingToElement(String           serverName,
                                           String           userId,
                                           String           guid,
                                           boolean          isPublic,
                                           RatingProperties requestBody)
    {
        final String methodName = "addRatingToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler.addRatingToElement(userId, guid, isPublic, requestBody);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, RatingProperties.class.getName());
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
     * Removes a star rating that was added to the element by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the rating object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeRatingFromElement(String          serverName,
                                                String          userId,
                                                String          guid,
                                                NullRequestBody requestBody)
    {
        final String methodName = "removeRatingFromElement";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeRatingFromElement(userId, guid);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a "LikeProperties" to the element.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param requestBody feedback request body .
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse addLikeToElement(String          serverName,
                                         String          userId,
                                         String          guid,
                                         boolean         isPublic,
                                         NullRequestBody requestBody)
    {
        final String methodName        = "addLikeToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.addLikeToElement(userId, guid, isPublic);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a "LikeProperties" added to the element by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the like object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeLikeFromElement(String          serverName,
                                              String          userId,
                                              String          guid,
                                              NullRequestBody requestBody)
    {
        final String methodName        = "removeLikeFromElement";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeLikeFromElement(userId, guid);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a comment to the element.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentToElement(String                         serverName,
                                            String                         userId,
                                            String                         guid,
                                            boolean                        isPublic,
                                            boolean                        forLineage,
                                            boolean                        forDuplicateProcessing,
                                            ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "addCommentToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof CommentProperties commentProperties)
                {
                    CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

                    response.setGUID(handler.addCommentToElement(userId,
                                                                 guid,
                                                                 isPublic,
                                                                 commentProperties,
                                                                 requestBody.getEffectiveTime(),
                                                                 forLineage,
                                                                 forDuplicateProcessing));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ReferenceableUpdateRequestBody.class.getName());
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
     * Adds a reply to a comment.
     *
     * @param serverName name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param commentGUID  String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param isPublic is this visible to other people
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentReply(String                         serverName,
                                        String                         userId,
                                        String                         commentGUID,
                                        boolean                        isPublic,
                                        boolean                        forLineage,
                                        boolean                        forDuplicateProcessing,
                                        ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "addCommentReply";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof CommentProperties commentProperties)
                {
                    CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

                    response.setGUID(handler.addCommentReply(userId,
                                                             commentGUID,
                                                             isPublic,
                                                             commentProperties,
                                                             null,
                                                             forLineage,
                                                             forDuplicateProcessing));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ReferenceableUpdateRequestBody.class.getName());
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
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param isPublic is this visible to other people
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateComment(String                         serverName,
                                        String                         userId,
                                        String                         commentGUID,
                                        boolean                        isMergeUpdate,
                                        boolean                        isPublic,
                                        boolean                        forLineage,
                                        boolean                        forDuplicateProcessing,
                                        ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof  CommentProperties commentProperties)
                {
                    CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

                    handler.updateComment(userId,
                                          commentGUID,
                                          isMergeUpdate,
                                          isPublic,
                                          commentProperties,
                                          requestBody.getEffectiveTime(),
                                          forLineage,
                                          forDuplicateProcessing);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ReferenceableUpdateRequestBody.class.getName());
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
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupAcceptedAnswer(String                  serverName,
                                            String                  userId,
                                            String                  questionCommentGUID,
                                            String                  answerCommentGUID,
                                            boolean                 forLineage,
                                            boolean                 forDuplicateProcessing,
                                            RelationshipRequestBody requestBody)
    {
        final String methodName = "setupAcceptedAnswer";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof FeedbackProperties feedbackProperties)
                {
                    handler.setupAcceptedAnswer(userId,
                                                questionCommentGUID,
                                                answerCommentGUID,
                                                feedbackProperties,
                                                requestBody.getEffectiveTime(),
                                                forLineage,
                                                forDuplicateProcessing);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, RelationshipRequestBody.class.getName());
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
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAcceptedAnswer(String                        serverName,
                                            String                        userId,
                                            String                        questionCommentGUID,
                                            String                        answerCommentGUID,
                                            boolean                       forLineage,
                                            boolean                       forDuplicateProcessing,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearAcceptedAnswer";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearAcceptedAnswer(userId,
                                            questionCommentGUID,
                                            answerCommentGUID,
                                            requestBody.getEffectiveTime(),
                                            forLineage,
                                            forDuplicateProcessing);
            }
            else
            {
                handler.clearAcceptedAnswer(userId,
                                            questionCommentGUID,
                                            answerCommentGUID,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing);
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
     * Removes a comment added to the element by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param elementGUID  String - unique id for the element object
     * @param commentGUID  String - unique id for the comment object
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeCommentFromElement(String                         serverName,
                                                 String                         userId,
                                                 String                         elementGUID,
                                                 String                         commentGUID,
                                                 boolean                        forLineage,
                                                 boolean                        forDuplicateProcessing,
                                                 ReferenceableUpdateRequestBody requestBody)
    {
        final String guidParameterName = "commentGUID";
        final String methodName        = "removeElementComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeComment(userId,
                                      commentGUID,
                                      requestBody.getEffectiveTime(),
                                      forLineage,
                                      forDuplicateProcessing);
            }
            else
            {
                handler.removeComment(userId,
                                      commentGUID,
                                      null,
                                      forLineage,
                                      forDuplicateProcessing);
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
     * Return the requested comment.
     *
     * @param serverName name of the server instances for this request
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     * @param forLineage               return elements marked with the Memento classification?
     * @param forDuplicateProcessing   do not merge elements marked as duplicates?
     * @param requestBody effectiveTime and asset manager identifiers
     * @return comment properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public CommentElementResponse getCommentByGUID(String                        serverName,
                                                   String                        userId,
                                                   String                        commentGUID,
                                                   boolean                       forLineage,
                                                   boolean                       forDuplicateProcessing,
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CommentElementResponse response = new CommentElementResponse();
        AuditLog            auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getComment(userId,
                                                       commentGUID,
                                                       requestBody.getEffectiveTime(),
                                                       forLineage,
                                                       forDuplicateProcessing));
            }
            else
            {
                response.setElement(handler.getComment(userId,
                                                       commentGUID,
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
     * Return the comments attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage               return elements marked with the Memento classification?
     * @param forDuplicateProcessing   do not merge elements marked as duplicates?
     * @param requestBody effectiveTime and asset manager identifiers
     * @return list of comments or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public CommentElementsResponse getAttachedComments(String                        serverName,
                                                       String                        userId,
                                                       String                        elementGUID,
                                                       int                           startFrom,
                                                       int                           pageSize,
                                                       boolean                       forLineage,
                                                       boolean                       forDuplicateProcessing,
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getAttachedComments";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CommentElementsResponse response = new CommentElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getAttachedComments(userId,
                                                                    elementGUID,
                                                                    startFrom,
                                                                    pageSize,
                                                                    requestBody.getEffectiveTime(),
                                                                    forLineage,
                                                                    forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getAttachedComments(userId,
                                                                    elementGUID,
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
     * Retrieve the list of comment metadata elements that contain the search string.
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
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CommentElementsResponse findComments(String                  serverName,
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
        final String methodName = "findComments";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CommentElementsResponse response = new CommentElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.findComments(userId,
                                                             instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                             startFrom,
                                                             pageSize,
                                                             requestBody.getEffectiveTime(),
                                                             forLineage,
                                                             forDuplicateProcessing));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createInformalTag(String        serverName,
                                          String        userId,
                                          TagProperties requestBody)
    {
        final String   methodName = "createTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.createInformalTag(userId, requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, TagProperties.class.getName());
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
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param tagGUID      unique id for the tag.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateTagDescription(String                       serverName,
                                               String                       userId,
                                               String                       tagGUID,
                                               InformalTagUpdateRequestBody requestBody)
    {
        final String methodName = "updateTagDescription";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateTagDescription(userId,
                                             tagGUID,
                                             requestBody.getDescription());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, InformalTagUpdateRequestBody.class.getName());
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
     * Removes a tag from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   deleteTag(String          serverName,
                                    String          userId,
                                    String          tagGUID,
                                    NullRequestBody requestBody)
    {
        final String methodName           = "deleteTag";
        final String tagGUIDParameterName = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.deleteTag(userId, tagGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param serverName   name of the server instances for this request
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the tag.
     *
     * @return Tag object or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagResponse getTag(String serverName,
                                      String userId,
                                      String guid)
    {
        final String methodName = "getTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagResponse response = new InformalTagResponse();
        AuditLog            auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setTag(handler.getTag(userId, guid));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse getTagsByName(String          serverName,
                                              String          userId,
                                              NameRequestBody requestBody,
                                              int             startFrom,
                                              int             pageSize)
    {
        final String methodName = "getTagsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.getTagsByName(userId,
                                                       requestBody.getName(),
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
     * Return the list of the calling user's private tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse getMyTagsByName(String          serverName,
                                                String          userId,
                                                NameRequestBody requestBody,
                                                int             startFrom,
                                                int             pageSize)
    {
        final String methodName = "getMyTagsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.getMyTagsByName(userId,
                                                         requestBody.getName(),
                                                         startFrom,
                                                         pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NameRequestBody.class.getName());
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
     * Return the list of tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param requestBody string to find in the properties
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse findTags(String                  serverName,
                                         String                  userId,
                                         int                     startFrom,
                                         int                     pageSize,
                                         boolean                 startsWith,
                                         boolean                 endsWith,
                                         boolean                 ignoreCase,
                                         SearchStringRequestBody requestBody)
    {
        final String methodName = "findTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.findTags(userId,
                                                  instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                  startFrom,
                                                  pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param requestBody string to find in the properties
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse findMyTags(String                  serverName,
                                           String                  userId,
                                           int                     startFrom,
                                           int                     pageSize,
                                           boolean                 startsWith,
                                           boolean                 endsWith,
                                           boolean                 ignoreCase,
                                           SearchStringRequestBody requestBody)
    {
        final String methodName = "findMyTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.findMyTags(userId,
                                                    instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                    startFrom,
                                                    pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
     * Adds a tag (either private of public) to an element.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param elementGUID    unique id for the element.
     * @param tagGUID      unique id of the tag.
     * @param requestBody  feedback request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   addTagToElement(String              serverName,
                                          String              userId,
                                          String              elementGUID,
                                          String              tagGUID,
                                          FeedbackProperties requestBody)
    {
        final String methodName             = "addTagToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        boolean  isPublic = false;

        if (requestBody != null)
        {
            isPublic = requestBody.getIsPublic();
        }

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.addTagToElement(userId, elementGUID, tagGUID, isPublic);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a tag from the element that was added by this user.
     *
     * @param serverName   name of the server instances for this request
     * @param userId    userId of user making request.
     * @param elementGUID unique id for the element.
     * @param tagGUID   unique id for the tag.
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   removeTagFromElement(String          serverName,
                                               String          userId,
                                               String          elementGUID,
                                               String          tagGUID,
                                               NullRequestBody requestBody)
    {
        final String   methodName  = "removeTagFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.removeTagFromElement(userId, elementGUID, tagGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param serverName   name of the server instances for this request
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return element guid list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDListResponse getElementsByTag(String serverName,
                                             String userId,
                                             String tagGUID,
                                             int    startFrom,
                                             int    pageSize)
    {
        final String methodName = "getElementsByTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse  response = new GUIDListResponse();
        AuditLog          auditLog = null;

        try
        {
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getElementsByTag(userId, tagGUID, startFrom, pageSize));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * A note log maintains an ordered list of notes.  It can be used to support release note, blogs and similar
     * broadcast information.  Notelogs are typically maintained by the owners/stewards of an element.
     */


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
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
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElementsResponse findNoteLogs(String                  serverName,
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
        final String methodName = "findNoteLogs";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteLogElementsResponse response = new NoteLogElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

                response.setElementList(handler.findNoteLogs(userId,
                                                             instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                             startFrom,
                                                             pageSize,
                                                             requestBody.getEffectiveTime(),
                                                             forLineage,
                                                             forDuplicateProcessing));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody name to search for and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElementsResponse getNoteLogsByName(String          serverName,
                                                     String          userId,
                                                     int             startFrom,
                                                     int             pageSize,
                                                     boolean         forLineage,
                                                     boolean         forDuplicateProcessing,
                                                     NameRequestBody requestBody)
    {
        final String methodName = "getNoteLogsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteLogElementsResponse response = new NoteLogElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

                response.setElementList(handler.getNoteLogsByName(userId,
                                                                  requestBody.getName(),
                                                                  startFrom,
                                                                  pageSize,
                                                                  requestBody.getEffectiveTime(),
                                                                  forLineage,
                                                                  forDuplicateProcessing));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NameRequestBody.class.getName());
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
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param elementGUID unique identifier of the note log of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElementsResponse getNoteLogsForElement(String                        serverName,
                                                         String                        userId,
                                                         String                        elementGUID,
                                                         int                           startFrom,
                                                         int                           pageSize,
                                                         boolean                       forLineage,
                                                         boolean                       forDuplicateProcessing,
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNotesForNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteLogElementsResponse response = new NoteLogElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getNoteLogsForElement(userId,
                                                                      elementGUID,
                                                                      startFrom,
                                                                      pageSize,
                                                                      requestBody.getEffectiveTime(),
                                                                      forLineage,
                                                                      forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getNoteLogsForElement(userId,
                                                                      elementGUID,
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
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param noteLogGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlators
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElementResponse getNoteLogByGUID(String                        serverName,
                                                   String                        userId,
                                                   String                        noteLogGUID,
                                                   boolean                       forLineage,
                                                   boolean                       forDuplicateProcessing,
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNoteLogByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteLogElementResponse response = new NoteLogElementResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getNoteLogByGUID(userId,
                                                             noteLogGUID,
                                                             requestBody.getEffectiveTime(),
                                                             forLineage,
                                                             forDuplicateProcessing));
            }
            else
            {
                response.setElement(handler.getNoteLogByGUID(userId,
                                                             noteLogGUID,
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


    /* ===============================================================================
     * A note log typically contains many notes, linked with relationships.
     */

    /**
     * Retrieve the list of note metadata elements that contain the search string.
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
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteElementsResponse findNotes(String                  serverName,
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
        final String methodName = "findNotes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteElementsResponse response = new NoteElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

                response.setElementList(handler.findNotes(userId,
                                                          instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                          startFrom,
                                                          pageSize,
                                                          requestBody.getEffectiveTime(),
                                                          forLineage,
                                                          forDuplicateProcessing));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
     * Retrieve the list of notes associated with a note log.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param noteLogGUID unique identifier of the note log of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteElementsResponse getNotesForNoteLog(String                        serverName,
                                                   String                        userId,
                                                   String                        noteLogGUID,
                                                   int                           startFrom,
                                                   int                           pageSize,
                                                   boolean                       forLineage,
                                                   boolean                       forDuplicateProcessing,
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNotesForNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteElementsResponse response = new NoteElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getNotesForNoteLog(userId,
                                                                   noteLogGUID,
                                                                   startFrom,
                                                                   pageSize,
                                                                   requestBody.getEffectiveTime(),
                                                                   forLineage,
                                                                   forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getNotesForNoteLog(userId,
                                                                   noteLogGUID,
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
     * Retrieve the note metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param noteGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteElementResponse getNoteByGUID(String                        serverName,
                                             String                        userId,
                                             String                        noteGUID,
                                             boolean                       forLineage,
                                             boolean                       forDuplicateProcessing,
                                             EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNoteByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteElementResponse response = new NoteElementResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollaborationManagementClient handler = instanceHandler.getCollaborationManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {

                response.setElement(handler.getNoteByGUID(userId,
                                                          noteGUID,
                                                          requestBody.getEffectiveTime(),
                                                          forLineage,
                                                          forDuplicateProcessing));
            }
            else
            {
                response.setElement(handler.getNoteByGUID(userId,
                                                          noteGUID,
                                                          null,
                                                          forLineage,
                                                          forDuplicateProcessing));            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return information about the elements classified with the data field classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getDataFieldClassifiedElements(String                      serverName,
                                                               String                      userId,
                                                               int                         startFrom,
                                                               int                         pageSize,
                                                               boolean                     forLineage,
                                                               boolean                     forDuplicateProcessing,
                                                               FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getDataFieldClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataFieldQueryProperties properties)
                {
                    response.setElementList(handler.getDataFieldClassifiedElements(userId,
                                                                                    properties,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    requestBody.getEffectiveTime(),
                                                                                    forLineage,
                                                                                    forDuplicateProcessing));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElementList(handler.getDataFieldClassifiedElements(userId,
                                                                                    null,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    requestBody.getEffectiveTime(),
                                                                                    forLineage,
                                                                                    forDuplicateProcessing));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LevelIdentifierProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElementList(handler.getDataFieldClassifiedElements(userId,
                                                                               null,
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
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getConfidenceClassifiedElements(String                      serverName,
                                                                String                      userId,
                                                                int                         startFrom,
                                                                int                         pageSize,
                                                                boolean                     forLineage,
                                                                boolean                     forDuplicateProcessing,
                                                                FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getConfidenceClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierProperties properties)
                {
                    response.setElementList(handler.getConfidenceClassifiedElements(userId,
                                                                                    properties.getReturnSpecificLevel(),
                                                                                    properties.getLevelIdentifier(),
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    requestBody.getEffectiveTime(),
                                                                                    forLineage,
                                                                                    forDuplicateProcessing));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElementList(handler.getConfidenceClassifiedElements(userId,
                                                                                    false,
                                                                                    0,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    requestBody.getEffectiveTime(),
                                                                                    forLineage,
                                                                                    forDuplicateProcessing));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LevelIdentifierProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElementList(handler.getConfidenceClassifiedElements(userId,
                                                                                false,
                                                                                0,
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
     * Return information about the elements classified with the criticality classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getCriticalityClassifiedElements(String                      serverName,
                                                                 String                      userId,
                                                                 int                         startFrom,
                                                                 int                         pageSize,
                                                                 boolean                     forLineage,
                                                                 boolean                     forDuplicateProcessing,
                                                                 FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getCriticalityClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierProperties properties)
                {
                    response.setElementList(handler.getCriticalityClassifiedElements(userId,
                                                                                     properties.getReturnSpecificLevel(),
                                                                                     properties.getLevelIdentifier(),
                                                                                     startFrom,
                                                                                     pageSize,
                                                                                     requestBody.getEffectiveTime(),
                                                                                     forLineage,
                                                                                     forDuplicateProcessing));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElementList(handler.getCriticalityClassifiedElements(userId,
                                                                                     false,
                                                                                     0,
                                                                                     startFrom,
                                                                                     pageSize,
                                                                                     requestBody.getEffectiveTime(),
                                                                                     forLineage,
                                                                                     forDuplicateProcessing));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LevelIdentifierProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElementList(handler.getCriticalityClassifiedElements(userId,
                                                                                 false,
                                                                                 0,
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
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getConfidentialityClassifiedElements(String                      serverName,
                                                                     String                      userId,
                                                                     int                         startFrom,
                                                                     int                         pageSize,
                                                                     boolean                     forLineage,
                                                                     boolean                     forDuplicateProcessing,
                                                                     FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getConfidentialityClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierProperties properties)
                {
                    response.setElementList(handler.getConfidentialityClassifiedElements(userId,
                                                                                         properties.getReturnSpecificLevel(),
                                                                                         properties.getLevelIdentifier(),
                                                                                         startFrom,
                                                                                         pageSize,
                                                                                         requestBody.getEffectiveTime(),
                                                                                         forLineage,
                                                                                         forDuplicateProcessing));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElementList(handler.getConfidentialityClassifiedElements(userId,
                                                                                         false,
                                                                                         0,
                                                                                         startFrom,
                                                                                         pageSize,
                                                                                         requestBody.getEffectiveTime(),
                                                                                         forLineage,
                                                                                         forDuplicateProcessing));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LevelIdentifierProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElementList(handler.getConfidentialityClassifiedElements(userId,
                                                                                     false,
                                                                                     0,
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
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getRetentionClassifiedElements(String                      serverName,
                                                               String                      userId,
                                                               int                         startFrom,
                                                               int                         pageSize,
                                                               boolean                     forLineage,
                                                               boolean                     forDuplicateProcessing,
                                                               FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getRetentionClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierProperties properties)
                {
                    response.setElementList(handler.getRetentionClassifiedElements(userId,
                                                                                   properties.getReturnSpecificLevel(),
                                                                                   properties.getLevelIdentifier(),
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   requestBody.getEffectiveTime(),
                                                                                   forLineage,
                                                                                   forDuplicateProcessing));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElementList(handler.getRetentionClassifiedElements(userId,
                                                                                   false,
                                                                                   0,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   requestBody.getEffectiveTime(),
                                                                                   forLineage,
                                                                                   forDuplicateProcessing));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LevelIdentifierProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElementList(handler.getRetentionClassifiedElements(userId,
                                                                               false,
                                                                               0,
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
     * Return information about the elements classified with the security tags classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getSecurityTaggedElements(String                      serverName,
                                                          String                      userId,
                                                          int                         startFrom,
                                                          int                         pageSize,
                                                          boolean                     forLineage,
                                                          boolean                     forDuplicateProcessing,
                                                          FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getSecurityTaggedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getSecurityTaggedElements(userId,
                                                                          startFrom,
                                                                          pageSize,
                                                                          requestBody.getEffectiveTime(),
                                                                          forLineage,
                                                                          forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getSecurityTaggedElements(userId,
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
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getOwnersElements(String                      serverName,
                                                  String                      userId,
                                                  int                         startFrom,
                                                  int                         pageSize,
                                                  boolean                     forLineage,
                                                  boolean                     forDuplicateProcessing,
                                                  FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getOwnersElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof FindNameProperties properties)
                {
                    response.setElementList(handler.getOwnersElements(userId,
                                                                      properties.getName(),
                                                                      startFrom,
                                                                      pageSize,
                                                                      requestBody.getEffectiveTime(),
                                                                      forLineage,
                                                                      forDuplicateProcessing));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElementList(handler.getOwnersElements(userId,
                                                                      null,
                                                                      startFrom,
                                                                      pageSize,
                                                                      requestBody.getEffectiveTime(),
                                                                      forLineage,
                                                                      forDuplicateProcessing));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(FindNameProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElementList(handler.getOwnersElements(userId,
                                                                  null,
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
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getMembersOfSubjectArea(String                      serverName,
                                                        String                      userId,
                                                        int                         startFrom,
                                                        int                         pageSize,
                                                        boolean                     forLineage,
                                                        boolean                     forDuplicateProcessing,
                                                        FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getMembersOfSubjectArea";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof FindNameProperties properties)
                {
                    response.setElementList(handler.getMembersOfSubjectArea(userId,
                                                                            properties.getName(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getEffectiveTime(),
                                                                            forLineage,
                                                                            forDuplicateProcessing));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElementList(handler.getMembersOfSubjectArea(userId,
                                                                            null,
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getEffectiveTime(),
                                                                            forLineage,
                                                                            forDuplicateProcessing));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(FindNameProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElementList(handler.getMembersOfSubjectArea(userId,
                                                                        null,
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
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GlossaryTermElementsResponse getMeanings(String                        serverName,
                                                    String                        userId,
                                                    String                        elementGUID,
                                                    int                           startFrom,
                                                    int                           pageSize,
                                                    boolean                       forLineage,
                                                    boolean                       forDuplicateProcessing,
                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getMeanings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElementList(handler.getMeanings(userId,
                                                            elementGUID,
                                                            startFrom,
                                                            pageSize,
                                                            null,
                                                            forLineage,
                                                            forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getMeanings(userId,
                                                            elementGUID,
                                                            startFrom,
                                                            pageSize,
                                                            requestBody.getEffectiveTime(),
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
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public RelatedElementsResponse getSemanticAssignees(String                        serverName,
                                                        String                        userId,
                                                        String                        glossaryTermGUID,
                                                        int                           startFrom,
                                                        int                           pageSize,
                                                        boolean                       forLineage,
                                                        boolean                       forDuplicateProcessing,
                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSemanticAssignees";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElementList(handler.getSemanticAssignees(userId,
                                                                     glossaryTermGUID,
                                                                     startFrom,
                                                                     pageSize,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getSemanticAssignees(userId,
                                                                     glossaryTermGUID,
                                                                     startFrom,
                                                                     pageSize,
                                                                     requestBody.getEffectiveTime(),
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
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public RelatedElementsResponse getGovernedElements(String                        serverName,
                                                       String                        userId,
                                                       String                        governanceDefinitionGUID,
                                                       int                           startFrom,
                                                       int                           pageSize,
                                                       boolean                       forLineage,
                                                       boolean                       forDuplicateProcessing,
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGovernedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElementList(handler.getGovernedElements(userId,
                                                                    governanceDefinitionGUID,
                                                                    startFrom,
                                                                    pageSize,
                                                                    null,
                                                                    forLineage,
                                                                    forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getGovernedElements(userId,
                                                                    governanceDefinitionGUID,
                                                                    startFrom,
                                                                    pageSize,
                                                                    requestBody.getEffectiveTime(),
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
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GovernanceDefinitionsResponse getGovernedByDefinitions(String                        serverName,
                                                                  String                        userId,
                                                                  String                        elementGUID,
                                                                  int                           startFrom,
                                                                  int                           pageSize,
                                                                  boolean                       forLineage,
                                                                  boolean                       forDuplicateProcessing,
                                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGovernedByDefinitions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionsResponse response = new GovernanceDefinitionsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getGovernedByDefinitions(userId,
                                                                      elementGUID,
                                                                      startFrom,
                                                                      pageSize,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing));
            }
            else
            {
                response.setElements(handler.getGovernedByDefinitions(userId,
                                                                      elementGUID,
                                                                      startFrom,
                                                                      pageSize,
                                                                      requestBody.getEffectiveTime(),
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
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the governance definition that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public RelatedElementsResponse getSourceElements(String                        serverName,
                                                     String                        userId,
                                                     String                        elementGUID,
                                                     int                           startFrom,
                                                     int                           pageSize,
                                                     boolean                       forLineage,
                                                     boolean                       forDuplicateProcessing,
                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSourceElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElementList(handler.getSourceElements(userId,
                                                                  elementGUID,
                                                                  startFrom,
                                                                  pageSize,
                                                                  null,
                                                                  forLineage,
                                                                  forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getSourceElements(userId,
                                                                  elementGUID,
                                                                  startFrom,
                                                                  pageSize,
                                                                  requestBody.getEffectiveTime(),
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
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public RelatedElementsResponse getElementsSourceFrom(String                        serverName,
                                                         String                        userId,
                                                         String                        elementGUID,
                                                         int                           startFrom,
                                                         int                           pageSize,
                                                         boolean                       forLineage,
                                                         boolean                       forDuplicateProcessing,
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getElementsSourceFrom";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElementList(handler.getElementsSourceFrom(userId,
                                                                      elementGUID,
                                                                      startFrom,
                                                                      pageSize,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing));
            }
            else
            {
                response.setElementList(handler.getElementsSourceFrom(userId,
                                                                      elementGUID,
                                                                      startFrom,
                                                                      pageSize,
                                                                      requestBody.getEffectiveTime(),
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
}
