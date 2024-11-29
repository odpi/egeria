/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossarybrowser.server;

import org.odpi.openmetadata.accessservices.assetmanager.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.CollaborationManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.GlossaryManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.StewardshipManagementClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermActivityType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermRelationshipStatus;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryCategoryElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryCategoryElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermRelationshipRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceDefinitionsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteLogElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteLogElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindNameProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LevelIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DataFieldQueryProperties;
import org.odpi.openmetadata.frameworkservices.gaf.rest.OpenMetadataElementResponse;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.FindByPropertiesRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryNameRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossarySearchStringRequestBody;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.GlossaryTermStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarybrowser.rest.HistoryRequestBody;

import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;


/**
 * The GlossaryBrowserRESTServices provides the implementation of the Glossary Browser Open Metadata View Service (OMVS).
 * This interface provides view interfaces for glossary UIs.
 */

public class GlossaryBrowserRESTServices extends TokenController
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
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementResponse getMetadataElementByGUID(String  serverName,
                                                                String  elementGUID,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime)
    {
        final String methodName = "getMetadataElementByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        OpenMetadataElementResponse response = new OpenMetadataElementResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            response.setElement(handler.getMetadataElementByGUID(userId,
                                                                 elementGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of glossary term status enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    public GlossaryTermStatusListResponse getGlossaryTermStatuses(String serverName)
    {
        final String methodName = "getGlossaryTermStatuses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermStatusListResponse response = new GlossaryTermStatusListResponse();
        AuditLog                       auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
     * @return list of enum values
     */
    public GlossaryTermRelationshipStatusListResponse getGlossaryTermRelationshipStatuses(String serverName)
    {
        final String methodName = "getGlossaryTermStatuses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermRelationshipStatusListResponse response = new GlossaryTermRelationshipStatusListResponse();
        AuditLog                       auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
     * @return list of enum values
     */
    public GlossaryTermActivityTypeListResponse getGlossaryTermActivityTypes(String serverName)
    {
        final String methodName = "getGlossaryTermStatuses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermActivityTypeListResponse response = new GlossaryTermActivityTypeListResponse();
        AuditLog                             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryElementsResponse response = new GlossaryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                          int             startFrom,
                                                          int             pageSize,
                                                          boolean         forLineage,
                                                          boolean         forDuplicateProcessing,
                                                          NameRequestBody requestBody)
    {
        final String methodName = "getGlossariesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryElementsResponse response = new GlossaryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                     String                        glossaryGUID,
                                                     boolean                       forLineage,
                                                     boolean                       forDuplicateProcessing,
                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryElementResponse response = new GlossaryElementResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                          String                        glossaryCategoryGUID,
                                                          boolean                       forLineage,
                                                          boolean                       forDuplicateProcessing,
                                                          EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryForCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryElementResponse response = new GlossaryElementResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                      String                        glossaryTermGUID,
                                                      boolean                       forLineage,
                                                      boolean                       forDuplicateProcessing,
                                                      EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryForTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryElementResponse response = new GlossaryElementResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                                     String                        glossaryGUID,
                                                                     int                           startFrom,
                                                                     int                           pageSize,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getCategoriesForGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                                 String                        glossaryTermGUID,
                                                                 int                           startFrom,
                                                                 int                           pageSize,
                                                                 boolean                       forLineage,
                                                                 boolean                       forDuplicateProcessing,
                                                                 EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getCategoriesForTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                                          int                     startFrom,
                                                                          int                     pageSize,
                                                                          boolean                 forLineage,
                                                                          boolean                 forDuplicateProcessing,
                                                                          GlossaryNameRequestBody requestBody)
    {
        final String methodName = "getGlossaryCategoriesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                                     String                        glossaryCategoryGUID,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryCategoryByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryCategoryElementResponse response = new GlossaryCategoryElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                                     String                        glossaryCategoryGUID,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryCategoryParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryCategoryElementResponse response = new GlossaryCategoryElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                                     String                        glossaryCategoryGUID,
                                                                     int                           startFrom,
                                                                     int                           pageSize,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossarySubCategories";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                            String                        glossaryGUID,
                                                            int                           startFrom,
                                                            int                           pageSize,
                                                            boolean                       forLineage,
                                                            boolean                       forDuplicateProcessing,
                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getTermsForGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                                    String                              glossaryCategoryGUID,
                                                                    int                                 startFrom,
                                                                    int                                 pageSize,
                                                                    boolean                             forLineage,
                                                                    boolean                             forDuplicateProcessing,
                                                                    GlossaryTermRelationshipRequestBody requestBody)
    {
        final String methodName = "getTermsForGlossaryCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                        String                              glossaryTermGUID,
                                                        int                                 startFrom,
                                                        int                                 pageSize,
                                                        boolean                             forLineage,
                                                        boolean                             forDuplicateProcessing,
                                                        GlossaryTermRelationshipRequestBody requestBody)
    {
        final String methodName = "getTermsForGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getRelatedTerms(userId,
                                                                glossaryTermGUID,
                                                                requestBody.getRelationshipTypeName(),
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
                                                                 int                     startFrom,
                                                                 int                     pageSize,
                                                                 boolean                 forLineage,
                                                                 boolean                 forDuplicateProcessing,
                                                                 GlossaryNameRequestBody requestBody)
    {
        final String methodName = "getGlossaryTermsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                             String                        guid,
                                                             boolean                       forLineage,
                                                             boolean                       forDuplicateProcessing,
                                                             EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGlossaryTermByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermElementResponse response = new GlossaryTermElementResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                               String             guid,
                                                               int                startFrom,
                                                               int                pageSize,
                                                               boolean            oldestFirst,
                                                               boolean            forLineage,
                                                               boolean            forDuplicateProcessing,
                                                               HistoryRequestBody requestBody)
    {
        final String methodName = "getGlossaryTermHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
     * Return the list of tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
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
                                              NameRequestBody requestBody,
                                              int             startFrom,
                                              int             pageSize)
    {
        final String methodName = "getTagsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                                NameRequestBody requestBody,
                                                int             startFrom,
                                                int             pageSize)
    {
        final String methodName = "getMyTagsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                         int                     startFrom,
                                         int                     pageSize,
                                         boolean                 startsWith,
                                         boolean                 endsWith,
                                         boolean                 ignoreCase,
                                         SearchStringRequestBody requestBody)
    {
        final String methodName = "findTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
                                           int                     startFrom,
                                           int                     pageSize,
                                           boolean                 startsWith,
                                           boolean                 endsWith,
                                           boolean                 ignoreCase,
                                           SearchStringRequestBody requestBody)
    {
        final String methodName = "findMyTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
     * Return information about the elements classified with the data field classification.
     *
     * @param serverName  name of the server instance to connect to
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
                                                               int                         startFrom,
                                                               int                         pageSize,
                                                               boolean                     forLineage,
                                                               boolean                     forDuplicateProcessing,
                                                               FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getDataFieldClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataFieldQueryProperties properties)
                {
                    response.setElements(handler.getDataFieldClassifiedElements(userId,
                                                                                properties,
                                                                                startFrom,
                                                                                pageSize,
                                                                                requestBody.getEffectiveTime(),
                                                                                forLineage,
                                                                                forDuplicateProcessing));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElements(handler.getDataFieldClassifiedElements(userId,
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
                response.setElements(handler.getDataFieldClassifiedElements(userId,
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
    public MetadataElementSummariesResponse getConfidenceClassifiedElements(String                      serverName,
                                                                            int                         startFrom,
                                                                            int                         pageSize,
                                                                            boolean                     forLineage,
                                                                            boolean                     forDuplicateProcessing,
                                                                            FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getConfidenceClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierProperties properties)
                {
                    response.setElements(handler.getConfidenceClassifiedElements(userId,
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
                    response.setElements(handler.getConfidenceClassifiedElements(userId,
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
                response.setElements(handler.getConfidenceClassifiedElements(userId,
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
    public MetadataElementSummariesResponse getCriticalityClassifiedElements(String                      serverName,
                                                                             int                         startFrom,
                                                                             int                         pageSize,
                                                                             boolean                     forLineage,
                                                                             boolean                     forDuplicateProcessing,
                                                                             FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getCriticalityClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierProperties properties)
                {
                    response.setElements(handler.getCriticalityClassifiedElements(userId,
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
                    response.setElements(handler.getCriticalityClassifiedElements(userId,
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
                response.setElements(handler.getCriticalityClassifiedElements(userId,
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
    public MetadataElementSummariesResponse getConfidentialityClassifiedElements(String                      serverName,
                                                                                 int                         startFrom,
                                                                                 int                         pageSize,
                                                                                 boolean                     forLineage,
                                                                                 boolean                     forDuplicateProcessing,
                                                                                 FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getConfidentialityClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierProperties properties)
                {
                    response.setElements(handler.getConfidentialityClassifiedElements(userId,
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
                    response.setElements(handler.getConfidentialityClassifiedElements(userId,
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
                response.setElements(handler.getConfidentialityClassifiedElements(userId,
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
    public MetadataElementSummariesResponse getRetentionClassifiedElements(String                      serverName,
                                                                           int                         startFrom,
                                                                           int                         pageSize,
                                                                           boolean                     forLineage,
                                                                           boolean                     forDuplicateProcessing,
                                                                           FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getRetentionClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierProperties properties)
                {
                    response.setElements(handler.getRetentionClassifiedElements(userId,
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
                    response.setElements(handler.getRetentionClassifiedElements(userId,
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
                response.setElements(handler.getRetentionClassifiedElements(userId,
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
    public MetadataElementSummariesResponse getSecurityTaggedElements(String                      serverName,
                                                                      int                         startFrom,
                                                                      int                         pageSize,
                                                                      boolean                     forLineage,
                                                                      boolean                     forDuplicateProcessing,
                                                                      FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getSecurityTaggedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSecurityTaggedElements(userId,
                                                                       startFrom,
                                                                       pageSize,
                                                                       requestBody.getEffectiveTime(),
                                                                       forLineage,
                                                                       forDuplicateProcessing));
            }
            else
            {
                response.setElements(handler.getSecurityTaggedElements(userId,
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
    public MetadataElementSummariesResponse getOwnersElements(String                      serverName,
                                                              int                         startFrom,
                                                              int                         pageSize,
                                                              boolean                     forLineage,
                                                              boolean                     forDuplicateProcessing,
                                                              FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getOwnersElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof FindNameProperties properties)
                {
                    response.setElements(handler.getOwnersElements(userId,
                                                                   properties.getName(),
                                                                   startFrom,
                                                                   pageSize,
                                                                   requestBody.getEffectiveTime(),
                                                                   forLineage,
                                                                   forDuplicateProcessing));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElements(handler.getOwnersElements(userId,
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
                response.setElements(handler.getOwnersElements(userId,
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
    public MetadataElementSummariesResponse getMembersOfSubjectArea(String                      serverName,
                                                                    int                         startFrom,
                                                                    int                         pageSize,
                                                                    boolean                     forLineage,
                                                                    boolean                     forDuplicateProcessing,
                                                                    FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getMembersOfSubjectArea";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof FindNameProperties properties)
                {
                    response.setElements(handler.getMembersOfSubjectArea(userId,
                                                                         properties.getName(),
                                                                         startFrom,
                                                                         pageSize,
                                                                         requestBody.getEffectiveTime(),
                                                                         forLineage,
                                                                         forDuplicateProcessing));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElements(handler.getMembersOfSubjectArea(userId,
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
                response.setElements(handler.getMembersOfSubjectArea(userId,
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
                                                    String                        elementGUID,
                                                    int                           startFrom,
                                                    int                           pageSize,
                                                    boolean                       forLineage,
                                                    boolean                       forDuplicateProcessing,
                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getMeanings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
    public RelatedMetadataElementSummariesResponse getSemanticAssignees(String                        serverName,
                                                                        String                        glossaryTermGUID,
                                                                        int                           startFrom,
                                                                        int                           pageSize,
                                                                        boolean                       forLineage,
                                                                        boolean                       forDuplicateProcessing,
                                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSemanticAssignees";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getSemanticAssignees(userId,
                                                                  glossaryTermGUID,
                                                                  startFrom,
                                                                  pageSize,
                                                                  null,
                                                                  forLineage,
                                                                  forDuplicateProcessing));
            }
            else
            {
                response.setElements(handler.getSemanticAssignees(userId,
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
    public RelatedMetadataElementSummariesResponse getGovernedElements(String                        serverName,
                                                                       String                        governanceDefinitionGUID,
                                                                       int                           startFrom,
                                                                       int                           pageSize,
                                                                       boolean                       forLineage,
                                                                       boolean                       forDuplicateProcessing,
                                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGovernedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getGovernedElements(userId,
                                                                 governanceDefinitionGUID,
                                                                 startFrom,
                                                                 pageSize,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing));
            }
            else
            {
                response.setElements(handler.getGovernedElements(userId,
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
                                                                  String                        elementGUID,
                                                                  int                           startFrom,
                                                                  int                           pageSize,
                                                                  boolean                       forLineage,
                                                                  boolean                       forDuplicateProcessing,
                                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGovernedByDefinitions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceDefinitionsResponse response = new GovernanceDefinitionsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

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
    public RelatedMetadataElementSummariesResponse getSourceElements(String                        serverName,
                                                                     String                        elementGUID,
                                                                     int                           startFrom,
                                                                     int                           pageSize,
                                                                     boolean                       forLineage,
                                                                     boolean                       forDuplicateProcessing,
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSourceElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getSourceElements(userId,
                                                               elementGUID,
                                                               startFrom,
                                                               pageSize,
                                                               null,
                                                               forLineage,
                                                               forDuplicateProcessing));
            }
            else
            {
                response.setElements(handler.getSourceElements(userId,
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
    public RelatedMetadataElementSummariesResponse getElementsSourceFrom(String                        serverName,
                                                                         String                        elementGUID,
                                                                         int                           startFrom,
                                                                         int                           pageSize,
                                                                         boolean                       forLineage,
                                                                         boolean                       forDuplicateProcessing,
                                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getElementsSourceFrom";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getElementsSourceFrom(userId,
                                                                   elementGUID,
                                                                   startFrom,
                                                                   pageSize,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing));
            }
            else
            {
                response.setElements(handler.getElementsSourceFrom(userId,
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
