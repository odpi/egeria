/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.handlers.GlossaryExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;


/**
 * GlossaryExchangeRESTServices is the server-side implementation of the Asset Manager OMAS's
 * support for relational databases.  It matches the GlossaryExchangeClient.
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

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryManagerHandler(userId, serverName, methodName);

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
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryManagerHandler(userId, serverName, methodName);

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
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryManagerHandler(userId, serverName, methodName);

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
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryManagerHandler(userId, serverName, methodName);

                handler.removeGlossary(userId, requestBody, glossaryGUID, methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureThrowable(response, error, methodName, auditLog);
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
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param organizingPrinciple description of how the glossary is organized
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setGlossaryAsTaxonomy(String serverName,
                                              String userId,
                                              String assetManagerGUID,
                                              String assetManagerName,
                                              String glossaryGUID,
                                              String glossaryExternalIdentifier,
                                              String organizingPrinciple)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the taxonomy designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGlossaryAsTaxonomy(String serverName,
                                                String userId,
                                                String assetManagerGUID,
                                                String assetManagerName,
                                                String glossaryGUID,
                                                String glossaryExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
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
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param scope description of the situations where this glossary is relevant.
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setGlossaryAsCanonical(String serverName,
                                               String userId,
                                               String assetManagerGUID,
                                               String assetManagerName,
                                               String glossaryGUID,
                                               String glossaryExternalIdentifier,
                                               String scope)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the canonical designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGlossaryAsCanonical(String serverName,
                                                 String userId,
                                                 String assetManagerGUID,
                                                 String assetManagerName,
                                                 String glossaryGUID,
                                                 String glossaryExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the list of glossary metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementsResponse findGlossaries(String serverName,
                                                   String userId,
                                                   String assetManagerGUID,
                                                   String assetManagerName,
                                                   String searchString,
                                                   int    startFrom,
                                                   int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement>   getGlossariesByName(String serverName,
                                                       String userId,
                                                       String assetManagerGUID,
                                                       String assetManagerName,
                                                       String name,
                                                       int    startFrom,
                                                       int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the list of glossaries created by this caller.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement>   getGlossariesForAssetManager(String serverName,
                                                                String userId,
                                                                String assetManagerGUID,
                                                                String assetManagerName,
                                                                int    startFrom,
                                                                int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElement getGlossaryByGUID(String serverName,
                                             String userId,
                                             String assetManagerGUID,
                                             String assetManagerName,
                                             String openMetadataGUID)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryExternalIdentifier unique identifier of the requested metadata element from the asset manager
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElement getGlossaryByExternalIdentifier(String serverName,
                                                           String userId,
                                                           String assetManagerGUID,
                                                           String assetManagerName,
                                                           String glossaryExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /* =====================================================================================================================
     * A glossary may host one or more glossary categories depending on its capability
     */

    /**
     * Create a new metadata element to represent a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param glossaryCategoryProperties properties about the glossary category to store
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryCategory(String                     serverName,
                                               String                     userId,
                                               String                     assetManagerGUID,
                                               String                     assetManagerName,
                                               String                     glossaryGUID,
                                               String                     glossaryCategoryExternalIdentifier,
                                               Map<String, String>        mappingProperties,
                                               GlossaryCategoryProperties glossaryCategoryProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Create a new metadata element to represent a glossary category using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryCategoryFromTemplate(String              serverName,
                                                           String              userId,
                                                           String              assetManagerGUID,
                                                           String              assetManagerName,
                                                           String              templateGUID,
                                                           String              glossaryGUID,
                                                           String              glossaryCategoryExternalIdentifier,
                                                           Map<String, String> mappingProperties,
                                                           TemplateProperties  templateProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Update the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the metadata element to update
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param glossaryCategoryProperties new properties for the metadata element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryCategory(String                     serverName,
                                               String                     userId,
                                               String                     assetManagerGUID,
                                               String                     assetManagerName,
                                               String                     glossaryCategoryGUID,
                                               String                     glossaryCategoryExternalIdentifier,
                                               GlossaryCategoryProperties glossaryCategoryProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Create a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupCategoryParent(String serverName,
                                            String userId,
                                            String assetManagerGUID,
                                            String assetManagerName,
                                            String glossaryParentCategoryGUID,
                                            String glossaryChildCategoryGUID)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearCategoryParent(String serverName,
                                            String userId,
                                            String assetManagerGUID,
                                            String assetManagerName,
                                            String glossaryParentCategoryGUID,
                                            String glossaryChildCategoryGUID)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the metadata element to remove
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeGlossaryCategory(String serverName,
                                               String userId,
                                               String assetManagerGUID,
                                               String assetManagerName,
                                               String glossaryCategoryGUID,
                                               String glossaryCategoryExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   findGlossaryCategories(String serverName,
                                                                  String userId,
                                                                  String assetManagerGUID,
                                                                  String assetManagerName,
                                                                  String searchString,
                                                                  int    startFrom,
                                                                  int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Return the list of categories associated with a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the categories associated with the requested glossary or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getCategoriesForGlossary(String serverName,
                                                                    String userId,
                                                                    String assetManagerGUID,
                                                                    String assetManagerName,
                                                                    String glossaryGUID,
                                                                    int    startFrom,
                                                                    int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the list of glossary category metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getGlossaryCategoriesByName(String serverName,
                                                                       String userId,
                                                                       String assetManagerGUID,
                                                                       String assetManagerName,
                                                                       String name,
                                                                       int    startFrom,
                                                                       int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElement getGlossaryCategoryByGUID(String serverName,
                                                             String userId,
                                                             String assetManagerGUID,
                                                             String assetManagerName,
                                                             String glossaryCategoryGUID)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }



    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     *
     * @return parent glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElement getGlossaryCategoryParent(String serverName,
                                                             String userId,
                                                             String assetManagerGUID,
                                                             String assetManagerName,
                                                             String glossaryCategoryGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement> getGlossarySubCategories(String userId,
                                                                  String assetManagerGUID,
                                                                  String assetManagerName,
                                                                  String glossaryCategoryGUID,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /* ===============================================================================
     * A glossary typically contains many glossary terms, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param glossaryTermProperties properties for the glossary term
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryTerm(String                 serverName,
                                           String                 userId,
                                           String                 assetManagerGUID,
                                           String                 assetManagerName,
                                           String                 glossaryGUID,
                                           String                 glossaryTermExternalIdentifier,
                                           Map<String, String>    mappingProperties,
                                           GlossaryTermProperties glossaryTermProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Create a new metadata element to represent a glossary term whose lifecycle is managed through a controlled workflow.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param glossaryTermProperties properties for the glossary term
     * @param initialStatus glossary term status to use when the object is created
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createControlledGlossaryTerm(String                 serverName,
                                                     String                 userId,
                                                     String                 assetManagerGUID,
                                                     String                 assetManagerName,
                                                     String                 glossaryGUID,
                                                     String                 glossaryTermExternalIdentifier,
                                                     Map<String, String>    mappingProperties,
                                                     GlossaryTermProperties glossaryTermProperties,
                                                     GlossaryTermStatus     initialStatus)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param glossaryGUID unique identifier of the glossary where the glossary term is located.
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryTermFromTemplate(String              serverName,
                                                       String              userId,
                                                       String              assetManagerGUID,
                                                       String              assetManagerName,
                                                       String              templateGUID,
                                                       String              glossaryGUID,
                                                       String              glossaryTermExternalIdentifier,
                                                       Map<String, String> mappingProperties,
                                                       TemplateProperties  templateProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Update the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param glossaryTermProperties new properties for the glossary term
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryTerm(String                 serverName,
                                           String                 userId,
                                           String                 assetManagerGUID,
                                           String                 assetManagerName,
                                           String                 glossaryTermGUID,
                                           String                 glossaryTermExternalIdentifier,
                                           GlossaryTermProperties glossaryTermProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Update the status of the metadata element representing a glossary term.  This is only valid on
     * a controlled glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param glossaryTermStatus new properties for the glossary term
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryTermStatus(String             serverName,
                                                 String             userId,
                                                 String             assetManagerGUID,
                                                 String             assetManagerName,
                                                 String             glossaryTermGUID,
                                                 String             glossaryTermExternalIdentifier,
                                                 GlossaryTermStatus glossaryTermStatus)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Link a term to a category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param categorizationProperties properties for the categorization relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupTermCategory(String                     serverName,
                                          String                     userId,
                                          String                     assetManagerGUID,
                                          String                     assetManagerName,
                                          String                     glossaryCategoryGUID,
                                          String                     glossaryTermGUID,
                                          GlossaryTermCategorization categorizationProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Unlink a term from a category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermCategory(String serverName,
                                          String userId,
                                          String assetManagerGUID,
                                          String assetManagerName,
                                          String glossaryCategoryGUID,
                                          String glossaryTermGUID)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Link two terms together using a specialist relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param relationshipsProperties properties for the categorization relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupTermRelationship(String                   serverName,
                                              String                   userId,
                                              String                   assetManagerGUID,
                                              String                   assetManagerName,
                                              String                   relationshipTypeName,
                                              String                   glossaryTermOneGUID,
                                              String                   glossaryTermTwoGUID,
                                              GlossaryTermRelationship relationshipsProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param relationshipsProperties properties for the categorization relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateTermRelationship(String                   serverName,
                                               String                   userId,
                                               String                   assetManagerGUID,
                                               String                   assetManagerName,
                                               String                   relationshipTypeName,
                                               String                   glossaryTermOneGUID,
                                               String                   glossaryTermTwoGUID,
                                               GlossaryTermRelationship relationshipsProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermRelationship(String serverName,
                                              String userId,
                                              String assetManagerGUID,
                                              String assetManagerName,
                                              String relationshipTypeName,
                                              String glossaryTermOneGUID,
                                              String glossaryTermTwoGUID)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }



    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsAbstractConcept(String serverName,
                                                 String userId,
                                                 String assetManagerGUID,
                                                 String assetManagerName,
                                                 String glossaryTermGUID,
                                                 String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsAbstractConcept(String serverName,
                                                   String userId,
                                                   String assetManagerGUID,
                                                   String assetManagerName,
                                                   String glossaryTermGUID,
                                                   String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsDataValue(String serverName,
                                           String userId,
                                           String assetManagerGUID,
                                           String assetManagerName,
                                           String glossaryTermGUID,
                                           String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsDataValue(String serverName,
                                             String userId,
                                             String assetManagerGUID,
                                             String assetManagerName,
                                             String glossaryTermGUID,
                                             String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param activityType type of activity
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsActivity(String                   serverName,
                                          String                   userId,
                                          String                   assetManagerGUID,
                                          String                   assetManagerName,
                                          String                   glossaryTermGUID,
                                          String                   glossaryTermExternalIdentifier,
                                          GlossaryTermActivityType activityType)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsActivity(String serverName,
                                            String userId,
                                            String assetManagerGUID,
                                            String assetManagerName,
                                            String glossaryTermGUID,
                                            String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param contextDefinition more details of the context
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsContext(String                        serverName,
                                         String                        userId,
                                         String                        assetManagerGUID,
                                         String                        assetManagerName,
                                         String                        glossaryTermGUID,
                                         String                        glossaryTermExternalIdentifier,
                                         GlossaryTermContextDefinition contextDefinition)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsContext(String serverName,
                                           String userId,
                                           String assetManagerGUID,
                                           String assetManagerName,
                                           String glossaryTermGUID,
                                           String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Classify the glossary term to indicate that it describes a spine object.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsSpineObject(String serverName,
                                             String userId,
                                             String assetManagerGUID,
                                             String assetManagerName,
                                             String glossaryTermGUID,
                                             String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the spine object designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsSpineObject(String serverName,
                                               String userId,
                                               String assetManagerGUID,
                                               String assetManagerName,
                                               String glossaryTermGUID,
                                               String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }



    /**
     * Classify the glossary term to indicate that it describes a spine attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsSpineAttribute(String serverName,
                                                String userId,
                                                String assetManagerGUID,
                                                String assetManagerName,
                                                String glossaryTermGUID,
                                                String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the spine attribute designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsSpineAttribute(String serverName,
                                                  String userId,
                                                  String assetManagerGUID,
                                                  String assetManagerName,
                                                  String glossaryTermGUID,
                                                  String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Classify the glossary term to indicate that it describes an object identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsObjectIdentifier(String serverName,
                                                  String userId,
                                                  String assetManagerGUID,
                                                  String assetManagerName,
                                                  String glossaryTermGUID,
                                                  String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the object identifier designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsObjectIdentifier(String serverName,
                                                    String userId,
                                                    String assetManagerGUID,
                                                    String assetManagerName,
                                                    String glossaryTermGUID,
                                                    String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to remove
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeGlossaryTerm(String serverName,
                                           String userId,
                                           String assetManagerGUID,
                                           String assetManagerName,
                                           String glossaryTermGUID,
                                           String glossaryTermExternalIdentifier)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>   findGlossaryTerms(String serverName,
                                                         String userId,
                                                         String assetManagerGUID,
                                                         String assetManagerName,
                                                         String searchString,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>    getTermsForGlossaryCategory(String serverName,
                                                                    String userId,
                                                                    String assetManagerGUID,
                                                                    String assetManagerName,
                                                                    String glossaryCategoryGUID,
                                                                    int    startFrom,
                                                                    int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>   getGlossaryTermsByName(String serverName,
                                                              String userId,
                                                              String assetManagerGUID,
                                                              String assetManagerName,
                                                              String name,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElement getGlossaryTermByGUID(String serverName,
                                                     String userId,
                                                     String assetManagerGUID,
                                                     String assetManagerName,
                                                     String guid)
    {
        final String methodName = "XXX";
        // todo
        return null;
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
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param linkProperties properties of the link
     *
     * @return unique identifier of the external reference or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createExternalGlossaryLink(String                         serverName,
                                                   String                         userId,
                                                   String                         assetManagerGUID,
                                                   String                         assetManagerName,
                                                   ExternalGlossaryLinkProperties linkProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Update the properties of a reference to an external glossary resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param linkProperties properties of the link
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateExternalGlossaryLink(String                         serverName,
                                                   String                         userId,
                                                   String                         assetManagerGUID,
                                                   String                         assetManagerName,
                                                   String                         externalLinkGUID,
                                                   ExternalGlossaryLinkProperties linkProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove information about a link to an external glossary resource (and the relationships that attached it to the glossaries).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeExternalGlossaryLink(String serverName,
                                                   String userId,
                                                   String assetManagerGUID,
                                                   String assetManagerName,
                                                   String externalLinkGUID)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Connect a glossary to a reference to an external glossary resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to attach
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse attachExternalLinkToGlossary(String serverName,
                                                     String userId,
                                                     String assetManagerGUID,
                                                     String assetManagerName,
                                                     String externalLinkGUID,
                                                     String glossaryGUID)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Disconnect a glossary from a reference to an external glossary resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to remove
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse detachExternalLinkFromGlossary(String serverName,
                                                       String userId,
                                                       String assetManagerGUID,
                                                       String assetManagerName,
                                                       String externalLinkGUID,
                                                       String glossaryGUID)
    {
        final String methodName = "XXX";
        // todo
        return null;
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
    public List<ExternalGlossaryLinkElement> getExternalLinksForGlossary(String serverName,
                                                                         String userId,
                                                                         String glossaryGUID,
                                                                         int    startFrom,
                                                                         int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Return the glossaries connected to an external glossary source.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalLinkGUID unique identifier of the metadata element for the external glossary link of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of glossaries or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement> getGlossariesForExternalLink(String serverName,
                                                              String userId,
                                                              String externalLinkGUID,
                                                              int    startFrom,
                                                              int    pageSize)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Create a link to an external glossary category resource.  This is associated with a category to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the the glossary category
     * @param linkProperties properties of the link
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse attachExternalCategoryLink(String                                serverName,
                                                   String                                userId,
                                                   String                                assetManagerGUID,
                                                   String                                assetManagerName,
                                                   String                                externalLinkGUID,
                                                   String                                glossaryCategoryGUID,
                                                   ExternalGlossaryElementLinkProperties linkProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the link to an external glossary category resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the the glossary category
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse detachExternalCategoryLink(String serverName,
                                                   String userId,
                                                   String assetManagerGUID,
                                                   String assetManagerName,
                                                   String externalLinkGUID,
                                                   String glossaryCategoryGUID)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Create a link to an external glossary term resource.  This is associated with a term to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the the glossary category
     * @param linkProperties properties of the link
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse attachExternalTermLink(String                                serverName,
                                               String                                userId,
                                               String                                assetManagerGUID,
                                               String                                assetManagerName,
                                               String                                externalLinkGUID,
                                               String                                glossaryTermGUID,
                                               ExternalGlossaryElementLinkProperties linkProperties)
    {
        final String methodName = "XXX";
        // todo
        return null;
    }


    /**
     * Remove the link to an external glossary term resource.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the the glossary category
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse detachExternalTermLink(String serverName,
                                               String userId,
                                               String assetManagerGUID,
                                               String assetManagerName,
                                               String externalLinkGUID,
                                               String glossaryTermGUID)
    {
        final String detachExternalTermLink = "XXX";
        // todo
        return null;
    }
}
