/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.handlers.GlossaryExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;



/**
 * GlossaryExchangeRESTServices is the server-side implementation of the Asset Manager OMAS's
 * support for glossaries.  It matches the GlossaryExchangeClient.
 */
public class GlossaryExchangeRESTServices
{
    private static AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();
    private static RESTCallLogger              restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(GlossaryExchangeRESTServices.class),
                                                                                    instanceHandler.getServiceName());

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public GlossaryExchangeRESTServices()
    {
    }


    /* ========================================================
     * The Glossary entity is the top level element in a glossary.
     */


    /**
     * Create a new metadata element to represent the root of a glossary.  All categories and terms are linked
     * to a single glossary.  They are owned by this glossary and if the glossary is deleted, any linked terms and
     * categories are deleted as well.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossary(String              serverName,
                                       String              userId,
                                       GlossaryRequestBody requestBody)
    {
        final String   methodName = "createGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createGlossary(userId,
                                                        requestBody.getMetadataCorrelationProperties(),
                                                        requestBody.getElementProperties(),
                                                        methodName));
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
     * Create a new metadata element to represent a glossary using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     *
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms and categories are deleted as well.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryFromTemplate(String              serverName,
                                                   String              userId,
                                                   String              templateGUID,
                                                   TemplateRequestBody requestBody)
    {
        final String methodName = "createGlossaryFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createGlossaryFromTemplate(userId,
                                                                    requestBody.getMetadataCorrelationProperties(),
                                                                    templateGUID,
                                                                    requestBody.getElementProperties(),
                                                                    methodName));
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
     * Update the metadata element representing a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to update
     * @param requestBody new properties for this element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossary(String              serverName,
                                       String              userId,
                                       String              glossaryGUID,
                                       GlossaryRequestBody requestBody)
    {
        final String methodName = "updateGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.updateGlossary(userId,
                                       requestBody.getMetadataCorrelationProperties(),
                                       glossaryGUID,
                                       requestBody.getElementProperties(),
                                       methodName);
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
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories
     * and terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeGlossary(String                        serverName,
                                       String                        userId,
                                       String                        glossaryGUID,
                                       MetadataCorrelationProperties requestBody)
    {
        final String methodName = "removeGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.removeGlossary(userId, requestBody, glossaryGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     *
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc and as such they are logically categorized by the linked category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setGlossaryAsTaxonomy(String                            serverName,
                                              String                            userId,
                                              String                            glossaryGUID,
                                              TaxonomyClassificationRequestBody requestBody)
    {
        final String methodName = "setGlossaryAsTaxonomy";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.setGlossaryAsTaxonomy(userId,
                                              requestBody.getMetadataCorrelationProperties(),
                                              glossaryGUID,
                                              requestBody.getOrganizingPrinciple(),
                                              methodName);
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
     * Remove the taxonomy designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGlossaryAsTaxonomy(String                        serverName,
                                                String                        userId,
                                                String                        glossaryGUID,
                                                MetadataCorrelationProperties requestBody)
    {
        final String methodName = "clearGlossaryAsTaxonomy";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.clearGlossaryAsTaxonomy(userId, requestBody, glossaryGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     *
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody description of the situations where this glossary is relevant.
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setGlossaryAsCanonical(String                                       serverName,
                                               String                                       userId,
                                               String                                       glossaryGUID,
                                               CanonicalVocabularyClassificationRequestBody requestBody)
    {
        final String methodName = "setGlossaryAsCanonical";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.setGlossaryAsCanonical(userId,
                                               requestBody.getMetadataCorrelationProperties(),
                                               glossaryGUID,
                                               requestBody.getScope(),
                                               methodName);
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
     * Remove the canonical designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGlossaryAsCanonical(String                        serverName,
                                                 String                        userId,
                                                 String                        glossaryGUID,
                                                 MetadataCorrelationProperties requestBody)
    {
        final String methodName = "clearGlossaryAsCanonical";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.clearGlossaryAsCanonical(userId, requestBody, glossaryGUID, methodName);
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
                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findGlossaries";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryElementsResponse response = new GlossaryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findGlossaries(userId,
                                                               requestBody.getAssetManagerGUID(),
                                                               requestBody.getAssetManagerName(),
                                                               requestBody.getSearchString(),
                                                               requestBody.getSearchStringParameterName(),
                                                               startFrom,
                                                               pageSize,
                                                               methodName));
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
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getGlossariesByName(userId,
                                                                    requestBody.getAssetManagerGUID(),
                                                                    requestBody.getAssetManagerName(),
                                                                    requestBody.getName(),
                                                                    requestBody.getNameParameterName(),
                                                                    startFrom,
                                                                    pageSize,
                                                                    methodName));
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
     * Retrieve the list of glossaries created by this caller.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody asset manager identifiers
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementsResponse   getGlossariesForAssetManager(String                             serverName,
                                                                   String                             userId,
                                                                   int                                startFrom,
                                                                   int                                pageSize,
                                                                   AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "getGlossariesForAssetManager";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryElementsResponse response = new GlossaryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getGlossariesForAssetManager(userId,
                                                                             requestBody.getAssetManagerGUID(),
                                                                             requestBody.getAssetManagerName(),
                                                                             startFrom,
                                                                             pageSize,
                                                                             methodName));
            }
            else
            {
                response.setElementList(handler.getGlossariesForAssetManager(userId,
                                                                             null,
                                                                             null,
                                                                             startFrom,
                                                                             pageSize,
                                                                             methodName));
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
     * @param openMetadataGUID unique identifier of the requested metadata element
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementResponse getGlossaryByGUID(String                             serverName,
                                                     String                             userId,
                                                     String                             openMetadataGUID,
                                                     AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "getGlossaryByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryElementResponse response = new GlossaryElementResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryByGUID(userId,
                                                              requestBody.getAssetManagerGUID(),
                                                              requestBody.getAssetManagerName(),
                                                              openMetadataGUID,
                                                              methodName));
            }
            else
            {
                response.setElement(handler.getGlossaryByGUID(userId,
                                                              null,
                                                              null,
                                                              openMetadataGUID,
                                                              methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /* =====================================================================================================================
     * A glossary may host one or more glossary categories depending on its capability
     */

    /**
     * Create a new metadata element to represent a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param requestBody properties about the glossary category to store
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryCategory(String                      serverName,
                                               String                      userId,
                                               String                      glossaryGUID,
                                               GlossaryCategoryRequestBody requestBody)
    {
        final String methodName = "createGlossaryCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createGlossaryCategory(userId,
                                                                glossaryGUID,
                                                                requestBody.getMetadataCorrelationProperties(),
                                                                requestBody.getElementProperties(),
                                                                methodName));
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
     * Create a new metadata element to represent a glossary category using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryCategoryFromTemplate(String               serverName,
                                                           String               userId,
                                                           String               templateGUID,
                                                           TemplateRequestBody  requestBody)
    {
        final String methodName = "createGlossaryCategoryFromTemplate";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createGlossaryCategoryFromTemplate(userId,
                                                                            requestBody.getMetadataCorrelationProperties(),
                                                                            templateGUID,
                                                                            requestBody.getElementProperties(),
                                                                            methodName));
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
     * Update the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryCategory(String                      serverName,
                                               String                      userId,
                                               String                      glossaryCategoryGUID,
                                               GlossaryCategoryRequestBody requestBody)
    {
        final String methodName = "updateGlossaryCategory";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.updateGlossaryCategory(userId,
                                               requestBody.getMetadataCorrelationProperties(),
                                               glossaryCategoryGUID,
                                               requestBody.getElementProperties(),
                                               methodName);
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
     * Create a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupCategoryParent(String                             serverName,
                                            String                             userId,
                                            String                             glossaryParentCategoryGUID,
                                            String                             glossaryChildCategoryGUID,
                                            AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "setupCategoryParent";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setupCategoryParent(userId,
                                            requestBody.getAssetManagerGUID(),
                                            requestBody.getAssetManagerName(),
                                            glossaryParentCategoryGUID,
                                            glossaryChildCategoryGUID,
                                            methodName);
            }
            else
            {
                handler.setupCategoryParent(userId,
                                            null,
                                            null,
                                            glossaryParentCategoryGUID,
                                            glossaryChildCategoryGUID,
                                            methodName);
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
     * Remove a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearCategoryParent(String                             serverName,
                                            String                             userId,
                                            String                             glossaryParentCategoryGUID,
                                            String                             glossaryChildCategoryGUID,
                                            AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "clearCategoryParent";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearCategoryParent(userId,
                                            requestBody.getAssetManagerGUID(),
                                            requestBody.getAssetManagerName(),
                                            glossaryParentCategoryGUID,
                                            glossaryChildCategoryGUID,
                                            methodName);
            }
            else
            {
                handler.clearCategoryParent(userId,
                                            null,
                                            null,
                                            glossaryParentCategoryGUID,
                                            glossaryChildCategoryGUID,
                                            methodName);
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
     * Remove the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeGlossaryCategory(String                        serverName,
                                               String                        userId,
                                               String                        glossaryCategoryGUID,
                                               MetadataCorrelationProperties requestBody)
    {
        final String methodName = "removeGlossaryCategory";
        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.removeGlossaryCategory(userId, requestBody, glossaryCategoryGUID, methodName);
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
     * @param requestBody string to find in the properties and correlators
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementsResponse findGlossaryCategories(String                  serverName,
                                                                   String                  userId,
                                                                   int                     startFrom,
                                                                   int                     pageSize,
                                                                   SearchStringRequestBody requestBody)
    {
        final String methodName = "findGlossaryCategories";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findGlossaryCategories(userId,
                                                                       requestBody.getAssetManagerGUID(),
                                                                       requestBody.getAssetManagerName(),
                                                                       requestBody.getSearchString(),
                                                                       requestBody.getSearchStringParameterName(),
                                                                       startFrom,
                                                                       pageSize,
                                                                       methodName));
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
     * @param requestBody asset manager identifiers
     *
     * @return list of metadata elements describing the categories associated with the requested glossary or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementsResponse getCategoriesForGlossary(String                             serverName,
                                                                     String                             userId,
                                                                     String                             glossaryGUID,
                                                                     int                                startFrom,
                                                                     int                                pageSize,
                                                                     AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "getCategoriesForGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getCategoriesForGlossary(userId,
                                                                         requestBody.getAssetManagerGUID(),
                                                                         requestBody.getAssetManagerName(),
                                                                         glossaryGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         methodName));
            }
            else
            {
                response.setElementList(handler.getCategoriesForGlossary(userId,
                                                                         null,
                                                                         null,
                                                                         glossaryGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         methodName));
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
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementsResponse   getGlossaryCategoriesByName(String          serverName,
                                                                          String          userId,
                                                                          int             startFrom,
                                                                          int             pageSize,
                                                                          NameRequestBody requestBody)
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
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getGlossaryCategoriesByName(userId,
                                                                            requestBody.getAssetManagerGUID(),
                                                                            requestBody.getAssetManagerName(),
                                                                            requestBody.getName(),
                                                                            requestBody.getNameParameterName(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            methodName));
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
     * @param requestBody asset manager identifiers
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementResponse getGlossaryCategoryByGUID(String                             serverName,
                                                                     String                             userId,
                                                                     String                             glossaryCategoryGUID,
                                                                     AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "getGlossaryCategoryByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementResponse response = new GlossaryCategoryElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryCategoryByGUID(userId,
                                                                      requestBody.getAssetManagerGUID(),
                                                                      requestBody.getAssetManagerName(),
                                                                      glossaryCategoryGUID,
                                                                      methodName));
            }
            else
            {
                response.setElement(handler.getGlossaryCategoryByGUID(userId,
                                                                      null,
                                                                      null,
                                                                      glossaryCategoryGUID,
                                                                      methodName));
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
     * @param requestBody asset manager identifiers
     *
     * @return parent glossary category element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementResponse getGlossaryCategoryParent(String                             serverName,
                                                                     String                             userId,
                                                                     String                             glossaryCategoryGUID,
                                                                     AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "getGlossaryCategoryParent";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementResponse response = new GlossaryCategoryElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryCategoryParent(userId,
                                                                      requestBody.getAssetManagerGUID(),
                                                                      requestBody.getAssetManagerName(),
                                                                      glossaryCategoryGUID,
                                                                      methodName));
            }
            else
            {
                response.setElement(handler.getGlossaryCategoryParent(userId,
                                                                      null,
                                                                      null,
                                                                      glossaryCategoryGUID,
                                                                      methodName));
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
     * @param requestBody asset manager identifiers
     *
     * @return list of glossary category elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElementsResponse getGlossarySubCategories(String                             serverName,
                                                                     String                             userId,
                                                                     String                             glossaryCategoryGUID,
                                                                     int                                startFrom,
                                                                     int                                pageSize,
                                                                     AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "getGlossarySubCategories";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryCategoryElementsResponse response = new GlossaryCategoryElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getGlossarySubCategories(userId,
                                                                         requestBody.getAssetManagerGUID(),
                                                                         requestBody.getAssetManagerName(),
                                                                         glossaryCategoryGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         methodName));
            }
            else
            {
                response.setElementList(handler.getGlossarySubCategories(userId,
                                                                         null,
                                                                         null,
                                                                         glossaryCategoryGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         methodName));
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
     * A glossary typically contains many glossary terms, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param requestBody properties for the glossary term
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryTerm(String                  serverName,
                                           String                  userId,
                                           String                  glossaryGUID,
                                           GlossaryTermRequestBody requestBody)
    {
        final String methodName = "createGlossaryTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createGlossaryTerm(userId,
                                                            glossaryGUID,
                                                            requestBody.getMetadataCorrelationProperties(),
                                                            requestBody.getElementProperties(),
                                                            methodName));
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
     * Create a new metadata element to represent a glossary term whose lifecycle is managed through a controlled workflow.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param requestBody properties for the glossary term
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createControlledGlossaryTerm(String                            serverName,
                                                     String                            userId,
                                                     String                            glossaryGUID,
                                                     ControlledGlossaryTermRequestBody requestBody)
    {
        final String methodName = "createControlledGlossaryTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createControlledGlossaryTerm(userId,
                                                                      glossaryGUID,
                                                                      requestBody.getMetadataCorrelationProperties(),
                                                                      requestBody.getElementProperties(),
                                                                      requestBody.getInitialStatus(),
                                                                      methodName));
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
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryTermFromTemplate(String               serverName,
                                                       String               userId,
                                                       String               templateGUID,
                                                       TemplateRequestBody  requestBody)
    {
        final String methodName = "createGlossaryTermFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createGlossaryTermFromTemplate(userId,
                                                                        requestBody.getMetadataCorrelationProperties(),
                                                                        templateGUID,
                                                                        requestBody.getElementProperties(),
                                                                        methodName));
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
     * Update the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param requestBody new properties for the glossary term
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryTerm(String                  serverName,
                                           String                  userId,
                                           String                  glossaryTermGUID,
                                           GlossaryTermRequestBody requestBody)
    {
        final String methodName = "updateGlossaryTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.updateGlossaryTerm(userId,
                                           requestBody.getMetadataCorrelationProperties(),
                                           glossaryTermGUID,
                                           requestBody.getElementProperties(),
                                           methodName);
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
     * Update the status of the metadata element representing a glossary term.  This is only valid on
     * a controlled glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryTermStatus(String                        serverName,
                                                 String                        userId,
                                                 String                        glossaryTermGUID,
                                                 GlossaryTermStatusRequestBody requestBody)
    {
        final String methodName = "updateGlossaryTermStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.updateGlossaryTermStatus(userId,
                                                 requestBody.getMetadataCorrelationProperties(),
                                                 glossaryTermGUID,
                                                 requestBody.getGlossaryTermStatus(),
                                                 methodName);
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
     * Link a term to a category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param requestBody properties for the categorization relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupTermCategory(String                    serverName,
                                          String                    userId,
                                          String                    glossaryCategoryGUID,
                                          String                    glossaryTermGUID,
                                          CategorizationRequestBody requestBody)
    {
        final String methodName = "setupTermCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.setupTermCategory(userId,
                                          requestBody.getAssetManagerGUID(),
                                          requestBody.getAssetManagerName(),
                                          glossaryCategoryGUID,
                                          glossaryTermGUID,
                                          requestBody.getCategorizationProperties(),
                                          methodName);
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
     * Unlink a term from a category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermCategory(String                             serverName,
                                          String                             userId,
                                          String                             glossaryCategoryGUID,
                                          String                             glossaryTermGUID,
                                          AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "clearTermCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermCategory(userId,
                                          requestBody.getAssetManagerGUID(),
                                          requestBody.getAssetManagerName(),
                                          glossaryCategoryGUID,
                                          glossaryTermGUID,
                                          methodName);
            }
            else
            {
                handler.clearTermCategory(userId,
                                          null,
                                          null,
                                          glossaryCategoryGUID,
                                          glossaryTermGUID,
                                          methodName);
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
     * Link two terms together using a specialist relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupTermRelationship(String                      serverName,
                                              String                      userId,
                                              String                      glossaryTermOneGUID,
                                              String                      relationshipTypeName,
                                              String                      glossaryTermTwoGUID,
                                              TermRelationshipRequestBody requestBody)
    {
        final String methodName = "setupTermRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.setupTermRelationship(userId,
                                               requestBody.getAssetManagerGUID(),
                                               requestBody.getAssetManagerName(),
                                               glossaryTermOneGUID,
                                               relationshipTypeName,
                                               glossaryTermTwoGUID,
                                               requestBody.getProperties(),
                                               methodName);
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
     * Update the relationship properties for the two terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateTermRelationship(String                      serverName,
                                               String                      userId,
                                               String                      glossaryTermOneGUID,
                                               String                      relationshipTypeName,
                                               String                      glossaryTermTwoGUID,
                                               TermRelationshipRequestBody requestBody)
    {
        final String methodName = "updateTermRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.updateTermRelationship(userId,
                                               glossaryTermOneGUID,
                                               relationshipTypeName,
                                               glossaryTermTwoGUID,
                                               requestBody.getProperties(),
                                               methodName);
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
     * Remove the relationship between two terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param requestBody properties of the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermRelationship(String                             serverName,
                                              String                             userId,
                                              String                             glossaryTermOneGUID,
                                              String                             relationshipTypeName,
                                              String                             glossaryTermTwoGUID,
                                              AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "clearTermRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermRelationship(userId,
                                              requestBody.getAssetManagerGUID(),
                                              requestBody.getAssetManagerName(),
                                              glossaryTermOneGUID,
                                              relationshipTypeName,
                                              glossaryTermTwoGUID,
                                              methodName);
            }
            else
            {
                handler.clearTermRelationship(userId,
                                              null,
                                              null,
                                              glossaryTermOneGUID,
                                              relationshipTypeName,
                                              glossaryTermTwoGUID,
                                              methodName);
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
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsAbstractConcept(String                        serverName,
                                                 String                        userId,
                                                 String                        glossaryTermGUID,
                                                 MetadataCorrelationProperties requestBody)
    {
        final String methodName = "setTermAsAbstractConcept";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.setTermAsAbstractConcept(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsAbstractConcept(String                        serverName,
                                                   String                        userId,
                                                   String                        glossaryTermGUID,
                                                   MetadataCorrelationProperties requestBody)
    {
        final String methodName = "clearTermAsAbstractConcept";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.clearTermAsAbstractConcept(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsDataValue(String                        serverName,
                                           String                        userId,
                                           String                        glossaryTermGUID,
                                           MetadataCorrelationProperties requestBody)
    {
        final String methodName = "setTermAsDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.setTermAsDataValue(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsDataValue(String                        serverName,
                                             String                        userId,
                                             String                        glossaryTermGUID,
                                             MetadataCorrelationProperties requestBody)
    {
        final String methodName = "clearTermAsDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.clearTermAsDataValue(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody type of activity and correlators
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsActivity(String                                serverName,
                                          String                                userId,
                                          String                                glossaryTermGUID,
                                          ActivityTermClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsActivity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.setTermAsActivity(userId,
                                          requestBody.getMetadataCorrelationProperties(),
                                          glossaryTermGUID,
                                          requestBody.getActivityType(),
                                          methodName);
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
     * Remove the activity designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsActivity(String                        serverName,
                                            String                        userId,
                                            String                        glossaryTermGUID,
                                            MetadataCorrelationProperties requestBody)
    {
        final String methodName = "clearTermAsActivity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.clearTermAsActivity(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody more details of the context
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsContext(String                                     serverName,
                                         String                                     userId,
                                         String                                     glossaryTermGUID,
                                         ContextDefinitionClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsContext";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.setTermAsContext(userId,
                                         requestBody.getMetadataCorrelationProperties(),
                                         glossaryTermGUID,
                                         requestBody.getContextDefinition(),
                                         methodName);
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
     * Remove the context definition designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsContext(String                        serverName,
                                           String                        userId,
                                           String                        glossaryTermGUID,
                                           MetadataCorrelationProperties requestBody)
    {
        final String methodName = "clearTermAsContext";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.clearTermAsContext(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Classify the glossary term to indicate that it describes a spine object.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsSpineObject(String                        serverName,
                                             String                        userId,
                                             String                        glossaryTermGUID,
                                             MetadataCorrelationProperties requestBody)
    {
        final String methodName = "setTermAsSpineObject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.setTermAsSpineObject(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the spine object designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsSpineObject(String                        serverName,
                                               String                        userId,
                                               String                        glossaryTermGUID,
                                               MetadataCorrelationProperties requestBody)
    {
        final String methodName = "clearTermAsSpineObject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.clearTermAsSpineObject(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Classify the glossary term to indicate that it describes a spine attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsSpineAttribute(String                        serverName,
                                                String                        userId,
                                                String                        glossaryTermGUID,
                                                MetadataCorrelationProperties requestBody)
    {
        final String methodName = "setTermAsSpineAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.setTermAsSpineAttribute(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the spine attribute designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsSpineAttribute(String                        serverName,
                                                  String                        userId,
                                                  String                        glossaryTermGUID,
                                                  MetadataCorrelationProperties requestBody)
    {
        final String methodName = "clearTermAsSpineAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.clearTermAsSpineAttribute(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Classify the glossary term to indicate that it describes an object identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsObjectIdentifier(String                        serverName,
                                                  String                        userId,
                                                  String                        glossaryTermGUID,
                                                  MetadataCorrelationProperties requestBody)
    {
        final String methodName = "setTermAsObjectIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.setTermAsObjectIdentifier(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the object identifier designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsObjectIdentifier(String                        serverName,
                                                    String                        userId,
                                                    String                        glossaryTermGUID,
                                                    MetadataCorrelationProperties requestBody)
    {
        final String methodName = "clearTermAsObjectIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.clearTermAsObjectIdentifier(userId, requestBody, glossaryTermGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeGlossaryTerm(String                        serverName,
                                           String                        userId,
                                           String                        glossaryTermGUID,
                                           MetadataCorrelationProperties requestBody)
    {
        final String methodName = "removeGlossaryTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.removeGlossaryTerm(userId, requestBody, glossaryTermGUID, methodName);
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
     * @param requestBody asset manager identifiers and search string
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementsResponse findGlossaryTerms(String                  serverName,
                                                          String                  userId,
                                                          int                     startFrom,
                                                          int                     pageSize,
                                                          SearchStringRequestBody requestBody)
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
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findGlossaryTerms(userId,
                                                                  requestBody.getAssetManagerGUID(),
                                                                  requestBody.getAssetManagerName(),
                                                                  requestBody.getSearchString(),
                                                                  requestBody.getSearchStringParameterName(),
                                                                  startFrom,
                                                                  pageSize,
                                                                  methodName));
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
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementsResponse getTermsForGlossary(String                             serverName,
                                                            String                             userId,
                                                            String                             glossaryGUID,
                                                            int                                startFrom,
                                                            int                                pageSize,
                                                            AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "getTermsForGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getTermsForGlossary(userId,
                                                                    requestBody.getAssetManagerGUID(),
                                                                    requestBody.getAssetManagerName(),
                                                                    glossaryGUID,
                                                                    startFrom,
                                                                    pageSize,
                                                                    methodName));
            }
            else
            {
                response.setElementList(handler.getTermsForGlossary(userId,
                                                                    null,
                                                                    null,
                                                                    glossaryGUID,
                                                                    startFrom,
                                                                    pageSize,
                                                                    methodName));
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
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementsResponse getTermsForGlossaryCategory(String                             serverName,
                                                                    String                             userId,
                                                                    String                             glossaryCategoryGUID,
                                                                    int                                startFrom,
                                                                    int                                pageSize,
                                                                    AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "getTermsForGlossaryCategory";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getTermsForGlossaryCategory(userId,
                                                                            requestBody.getAssetManagerGUID(),
                                                                            requestBody.getAssetManagerName(),
                                                                            glossaryCategoryGUID,
                                                                            startFrom,
                                                                            pageSize,
                                                                            methodName));
            }
            else
            {
                response.setElementList(handler.getTermsForGlossaryCategory(userId,
                                                                            null,
                                                                            null,
                                                                            glossaryCategoryGUID,
                                                                            startFrom,
                                                                            pageSize,
                                                                            methodName));
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
     * @param requestBody asset manager identifiers and name
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementsResponse   getGlossaryTermsByName(String          serverName,
                                                                 String          userId,
                                                                 int             startFrom,
                                                                 int             pageSize,
                                                                 NameRequestBody requestBody)
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
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getGlossaryTermsByName(userId,
                                                                       requestBody.getAssetManagerGUID(),
                                                                       requestBody.getAssetManagerName(),
                                                                       requestBody.getName(),
                                                                       requestBody.getNameParameterName(),
                                                                       startFrom,
                                                                       pageSize,
                                                                       methodName));
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
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementResponse getGlossaryTermByGUID(String                             serverName,
                                                             String                             userId,
                                                             String                             guid,
                                                             AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "getGlossaryTermByGUID";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementResponse response = new GlossaryTermElementResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryTermByGUID(userId,
                                                                  requestBody.getAssetManagerGUID(),
                                                                  requestBody.getAssetManagerName(),
                                                                  guid,
                                                                  methodName));
            }
            else
            {
                response.setElement(handler.getGlossaryTermByGUID(userId,
                                                                  null,
                                                                  null,
                                                                  guid,
                                                                  methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* =========================================================================================
     * Support for linkage to external glossary resources.  These glossary resources are not
     * stored as metadata - they could be web pages, ontologies or some other format.
     * It is possible that the external glossary resource may have been generated by the metadata
     * representation or vice versa.
     */


    /**
     * Create a link to an external glossary resource.  This is associated with a glossary to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param requestBody properties of the link
     *
     * @return unique identifier of the external reference or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createExternalGlossaryLink(String                          serverName,
                                                   String                          userId,
                                                   ExternalGlossaryLinkRequestBody requestBody)
    {
        final String methodName = "createExternalGlossaryLink";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createExternalGlossaryLink(userId,
                                                                    requestBody.getAssetManagerGUID(),
                                                                    requestBody.getAssetManagerName(),
                                                                    requestBody.getElementProperties(),
                                                                    methodName));
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
     * Update the properties of a reference to an external glossary resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param requestBody properties of the link
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateExternalGlossaryLink(String                          serverName,
                                                   String                          userId,
                                                   String                          externalLinkGUID,
                                                   ExternalGlossaryLinkRequestBody requestBody)
    {
        final String methodName = "updateExternalGlossaryLink";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.updateExternalGlossaryLink(userId,
                                                   requestBody.getAssetManagerGUID(),
                                                   requestBody.getAssetManagerName(),
                                                   externalLinkGUID,
                                                   requestBody.getElementProperties(),
                                                   methodName);
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
     * Remove information about a link to an external glossary resource (and the relationships that attached it to the glossaries).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeExternalGlossaryLink(String                             serverName,
                                                   String                             userId,
                                                   String                             externalLinkGUID,
                                                   AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "removeExternalGlossaryLink";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeExternalGlossaryLink(userId,
                                                   requestBody.getAssetManagerGUID(),
                                                   requestBody.getAssetManagerName(),
                                                   externalLinkGUID,
                                                   methodName);
            }
            else
            {
                handler.removeExternalGlossaryLink(userId,
                                                   null,
                                                   null,
                                                   externalLinkGUID,
                                                   methodName);
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
     * Connect a glossary to a reference to an external glossary resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to attach
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse attachExternalLinkToGlossary(String                             serverName,
                                                     String                             userId,
                                                     String                             glossaryGUID,
                                                     String                             externalLinkGUID,
                                                     AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "attachExternalLinkToGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.attachExternalLinkToGlossary(userId,
                                                   requestBody.getAssetManagerGUID(),
                                                   requestBody.getAssetManagerName(),
                                                   glossaryGUID,
                                                   externalLinkGUID,
                                                   methodName);
            }
            else
            {
                handler.attachExternalLinkToGlossary(userId,
                                                     null,
                                                     null,
                                                     glossaryGUID,
                                                     externalLinkGUID,
                                                     methodName);
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
     * Disconnect a glossary from a reference to an external glossary resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse detachExternalLinkFromGlossary(String                             serverName,
                                                       String                             userId,
                                                       String                             externalLinkGUID,
                                                       String                             glossaryGUID,
                                                       AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "detachExternalLinkFromGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachExternalLinkFromGlossary(userId,
                                                       requestBody.getAssetManagerGUID(),
                                                       requestBody.getAssetManagerName(),
                                                       glossaryGUID,
                                                       externalLinkGUID,
                                                       methodName);
            }
            else
            {
                handler.detachExternalLinkFromGlossary(userId,
                                                       null,
                                                       null,
                                                       glossaryGUID,
                                                       externalLinkGUID,
                                                       methodName);
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
     * Retrieve the list of links to external glossary resources attached to a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element for the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of attached links to external glossary resources or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ExternalGlossaryLinkElementsResponse getExternalLinksForGlossary(String serverName,
                                                                            String userId,
                                                                            String glossaryGUID,
                                                                            int    startFrom,
                                                                            int    pageSize)
    {
        final String methodName = "getExternalLinksForGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalGlossaryLinkElementsResponse response = new ExternalGlossaryLinkElementsResponse();
        AuditLog                             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            response.setElementList(handler.getExternalLinksForGlossary(userId, glossaryGUID, startFrom, pageSize, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the glossaries connected to an external glossary source.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the metadata element for the external glossary link of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody asset manager identifiers
     *
     * @return list of glossaries or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementsResponse getGlossariesForExternalLink(String                             serverName,
                                                                 String                             userId,
                                                                 String                             externalLinkGUID,
                                                                 int                                startFrom,
                                                                 int                                pageSize,
                                                                 AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "getGlossariesForExternalLink";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryElementsResponse response = new GlossaryElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getGlossariesForExternalLink(userId,
                                                                             requestBody.getAssetManagerGUID(),
                                                                             requestBody.getAssetManagerName(),
                                                                             externalLinkGUID,
                                                                             startFrom,
                                                                             pageSize,
                                                                             methodName));
            }
            else
            {
                response.setElementList(handler.getGlossariesForExternalLink(userId,
                                                                             null,
                                                                             null,
                                                                             externalLinkGUID,
                                                                             startFrom,
                                                                             pageSize,
                                                                             methodName));
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
     * Create a link to an external glossary category resource.  This is associated with a category to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the the glossary category
     * @param requestBody properties of the link
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse attachExternalCategoryLink(String                                 serverName,
                                                   String                                 userId,
                                                   String                                 glossaryCategoryGUID,
                                                   String                                 externalLinkGUID,
                                                   ExternalGlossaryElementLinkRequestBody requestBody)
    {
        final String methodName = "attachExternalCategoryLink";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.attachExternalCategoryLink(userId,
                                                   requestBody.getAssetManagerGUID(),
                                                   requestBody.getAssetManagerName(),
                                                   glossaryCategoryGUID,
                                                   externalLinkGUID,
                                                   requestBody.getElementProperties(),
                                                   methodName);
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
     * Remove the link to an external glossary category resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the the glossary category
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse detachExternalCategoryLink(String                             serverName,
                                                   String                             userId,
                                                   String                             externalLinkGUID,
                                                   String                             glossaryCategoryGUID,
                                                   AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "detachExternalCategoryLink";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachExternalCategoryLink(userId,
                                                   requestBody.getAssetManagerGUID(),
                                                   requestBody.getAssetManagerName(),
                                                   glossaryCategoryGUID,
                                                   externalLinkGUID,
                                                   methodName);
            }
            else
            {
                handler.detachExternalCategoryLink(userId,
                                                   null,
                                                   null,
                                                   glossaryCategoryGUID,
                                                   externalLinkGUID,
                                                   methodName);
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
     * Create a link to an external glossary term resource.  This is associated with a term to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the the glossary category
     * @param requestBody properties of the link
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse attachExternalTermLink(String                                 serverName,
                                               String                                 userId,
                                               String                                 externalLinkGUID,
                                               String                                 glossaryTermGUID,
                                               ExternalGlossaryElementLinkRequestBody requestBody)
    {
        final String methodName = "attachExternalTermLink";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.attachExternalTermLink(userId,
                                               requestBody.getAssetManagerGUID(),
                                               requestBody.getAssetManagerName(),
                                               glossaryTermGUID,
                                               externalLinkGUID,
                                               requestBody.getElementProperties(),
                                               methodName);
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
     * Remove the link to an external glossary term resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the the glossary category
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse detachExternalTermLink(String                             serverName,
                                               String                             userId,
                                               String                             externalLinkGUID,
                                               String                             glossaryTermGUID,
                                               AssetManagerIdentifiersRequestBody requestBody)
    {
        final String methodName = "detachExternalTermLink";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.detachExternalTermLink(userId,
                                               requestBody.getAssetManagerGUID(),
                                               requestBody.getAssetManagerName(),
                                               glossaryTermGUID,
                                               externalLinkGUID,
                                               methodName);
            }
            else
            {
                handler.detachExternalTermLink(userId,
                                               null,
                                               null,
                                               glossaryTermGUID,
                                               externalLinkGUID,
                                               methodName);
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
