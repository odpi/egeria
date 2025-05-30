/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.handlers.GlossaryExchangeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworkservices.omf.rest.HistoryExternalIdentifiersRequestBody;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * GlossaryExchangeRESTServices is the server-side implementation of the Asset Manager OMAS's
 * support for glossaries.  It matches the GlossaryExchangeClient.
 */
public class GlossaryExchangeRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();
    private static final RESTCallLogger              restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(GlossaryExchangeRESTServices.class),
                                                                                    instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

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
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossary(String                   serverName,
                                       String                   userId,
                                       boolean                  assetManagerIsHome,
                                       ReferenceableRequestBody requestBody)
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
                if (requestBody.getElementProperties() instanceof GlossaryProperties glossaryProperties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);


                    response.setGUID(handler.createGlossary(userId,
                                                            requestBody.getMetadataCorrelationProperties(),
                                                            assetManagerIsHome,
                                                            glossaryProperties,
                                                            methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a glossary using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms and categories are deleted as well.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryFromTemplate(String              serverName,
                                                   String              userId,
                                                   boolean             assetManagerIsHome,
                                                   String              templateGUID,
                                                   boolean             deepCopy,
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
                                                                    assetManagerIsHome,
                                                                    templateGUID,
                                                                    requestBody.getElementProperties(),
                                                                    deepCopy,
                                                                    methodName));
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
     * Update the metadata element representing a glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new properties for this element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossary(String                         serverName,
                                       String                         userId,
                                       String                         glossaryGUID,
                                       boolean                        isMergeUpdate,
                                       boolean                        forLineage,
                                       boolean                        forDuplicateProcessing,
                                       ReferenceableUpdateRequestBody requestBody)
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
                if (requestBody.getElementProperties() instanceof GlossaryProperties glossaryProperties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    handler.updateGlossary(userId,
                                           requestBody.getMetadataCorrelationProperties(),
                                           glossaryGUID,
                                           glossaryProperties,
                                           requestBody.getUpdateDescription(),
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody.getEffectiveTime(),
                                           methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryProperties.class.getName(), methodName);
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
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories
     * and terms.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeGlossary(String                         serverName,
                                       String                         userId,
                                       String                         glossaryGUID,
                                       boolean                        cascadedDelete,
                                       boolean                        forLineage,
                                       boolean                        forDuplicateProcessing,
                                       ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "removeGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGlossary(userId,
                                       requestBody.getMetadataCorrelationProperties(),
                                       glossaryGUID,
                                       cascadedDelete,
                                       forLineage,
                                       forDuplicateProcessing,
                                       requestBody.getEffectiveTime(),
                                       methodName);
            }
            else
            {
                handler.removeGlossary(userId,
                                       null,
                                       glossaryGUID,
                                       cascadedDelete,
                                       forLineage,
                                       forDuplicateProcessing,
                                       null,
                                       methodName);
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
     * Classify the glossary to indicate that it is an editing glossary - this means it is
     * a collection of glossary updates that will be merged into its source glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setGlossaryAsEditingGlossary(String                    serverName,
                                                     String                    userId,
                                                     String                    glossaryGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     ClassificationRequestBody requestBody)
    {
        final String methodName = "setGlossaryAsEditingGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof EditingGlossaryProperties properties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    handler.setGlossaryAsEditingGlossary(userId,
                                                         requestBody.getMetadataCorrelationProperties(),
                                                         glossaryGUID,
                                                         properties,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         requestBody.getEffectiveTime(),
                                                         methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    handler.setGlossaryAsEditingGlossary(userId,
                                                         requestBody.getMetadataCorrelationProperties(),
                                                         glossaryGUID,
                                                         null,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         requestBody.getEffectiveTime(),
                                                         methodName);                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(EditingGlossaryProperties.class.getName(), methodName);
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
     * Remove the editing glossary designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGlossaryAsEditingGlossary(String                    serverName,
                                                       String                    userId,
                                                       String                    glossaryGUID,
                                                       boolean                   forLineage,
                                                       boolean                   forDuplicateProcessing,
                                                       ClassificationRequestBody requestBody)
    {
        final String methodName = "clearGlossaryAsEditingGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearGlossaryAsEditingGlossary(userId,
                                                       requestBody.getMetadataCorrelationProperties(),
                                                       glossaryGUID,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       requestBody.getEffectiveTime(),
                                                       methodName);
            }
            else
            {
                handler.clearGlossaryAsEditingGlossary(userId,
                                                       null,
                                                       glossaryGUID,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       null,
                                                       methodName);
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
     * Classify the glossary to indicate that it is an editing glossary - this means it is
     * a collection of glossary updates that will be merged into another glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setGlossaryAsStagingGlossary(String                    serverName,
                                                     String                    userId,
                                                     String                    glossaryGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     ClassificationRequestBody requestBody)
    {
        final String methodName = "setGlossaryAsEditingGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof StagingGlossaryProperties properties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    handler.setGlossaryAsStagingGlossary(userId,
                                                         requestBody.getMetadataCorrelationProperties(),
                                                         glossaryGUID,
                                                         properties,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         requestBody.getEffectiveTime(),
                                                         methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    handler.setGlossaryAsStagingGlossary(userId,
                                                         requestBody.getMetadataCorrelationProperties(),
                                                         glossaryGUID,
                                                         null,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         requestBody.getEffectiveTime(),
                                                         methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(StagingGlossaryProperties.class.getName(), methodName);
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
     * Remove the staging glossary designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGlossaryAsStagingGlossary(String                    serverName,
                                                       String                    userId,
                                                       String                    glossaryGUID,
                                                       boolean                   forLineage,
                                                       boolean                   forDuplicateProcessing,
                                                       ClassificationRequestBody requestBody)
    {
        final String methodName = "clearGlossaryAsStagingGlossary";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearGlossaryAsStagingGlossary(userId,
                                                       requestBody.getMetadataCorrelationProperties(),
                                                       glossaryGUID,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       requestBody.getEffectiveTime(),
                                                       methodName);
            }
            else
            {
                handler.clearGlossaryAsEditingGlossary(userId,
                                                       null,
                                                       glossaryGUID,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       null,
                                                       methodName);
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
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setGlossaryAsTaxonomy(String                    serverName,
                                              String                    userId,
                                              String                    glossaryGUID,
                                              boolean                   forLineage,
                                              boolean                   forDuplicateProcessing,
                                              ClassificationRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof TaxonomyProperties properties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    handler.setGlossaryAsTaxonomy(userId,
                                                  requestBody.getMetadataCorrelationProperties(),
                                                  glossaryGUID,
                                                  properties,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  requestBody.getEffectiveTime(),
                                                  methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TaxonomyProperties.class.getName(), methodName);
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
     * Remove the taxonomy designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGlossaryAsTaxonomy(String                    serverName,
                                                String                    userId,
                                                String                    glossaryGUID,
                                                boolean                   forLineage,
                                                boolean                   forDuplicateProcessing,
                                                ClassificationRequestBody requestBody)
    {
        final String methodName = "clearGlossaryAsTaxonomy";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearGlossaryAsTaxonomy(userId,
                                                requestBody.getMetadataCorrelationProperties(),
                                                glossaryGUID,
                                                forLineage,
                                                forDuplicateProcessing,
                                                requestBody.getEffectiveTime(),
                                                methodName);
            }
            else
            {
                handler.clearGlossaryAsTaxonomy(userId,
                                                null,
                                                glossaryGUID,
                                                forLineage,
                                                forDuplicateProcessing,
                                                null,
                                                methodName);
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
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody description of the situations where this glossary is relevant.
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setGlossaryAsCanonical(String                    serverName,
                                               String                    userId,
                                               String                    glossaryGUID,
                                               boolean                   forLineage,
                                               boolean                   forDuplicateProcessing,
                                               ClassificationRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof CanonicalVocabularyProperties properties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    handler.setGlossaryAsCanonical(userId,
                                                   requestBody.getMetadataCorrelationProperties(),
                                                   glossaryGUID,
                                                   properties,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   requestBody.getEffectiveTime(),
                                                   methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CanonicalVocabularyProperties.class.getName(), methodName);
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
     * Remove the canonical designation from the glossary.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGlossaryAsCanonical(String                    serverName,
                                                 String                    userId,
                                                 String                    glossaryGUID,
                                                 boolean                   forLineage,
                                                 boolean                   forDuplicateProcessing,
                                                 ClassificationRequestBody requestBody)
    {
        final String methodName = "clearGlossaryAsCanonical";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearGlossaryAsCanonical(userId,
                                                 requestBody.getMetadataCorrelationProperties(),
                                                 glossaryGUID,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime(),
                                                 methodName);
            }
            else
            {
                handler.clearGlossaryAsCanonical(userId,
                                                 null,
                                                 glossaryGUID,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 null,
                                                 methodName);
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
     * Retrieve the list of glossary metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementsResponse findGlossaries(String                          serverName,
                                                   String                          userId,
                                                   int                             startFrom,
                                                   int                             pageSize,
                                                   boolean                         forLineage,
                                                   boolean                         forDuplicateProcessing,
                                                   SearchStringRequestBody         requestBody)
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
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               requestBody.getEffectiveTime(),
                                                               methodName));
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
                                                          NameRequestBody         requestBody)
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
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    requestBody.getEffectiveTime(),
                                                                    methodName));
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
     * Retrieve the list of glossaries created by this caller.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElementsResponse   getGlossariesForAssetManager(String                        serverName,
                                                                   String                        userId,
                                                                   int                           startFrom,
                                                                   int                           pageSize,
                                                                   boolean                       forLineage,
                                                                   boolean                       forDuplicateProcessing,
                                                                   EffectiveTimeQueryRequestBody requestBody)
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
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             requestBody.getEffectiveTime(),
                                                                             methodName));
            }
            else
            {
                response.setElementList(handler.getGlossariesForAssetManager(userId,
                                                                             null,
                                                                             null,
                                                                             startFrom,
                                                                             pageSize,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             null,
                                                                             methodName));
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

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryByGUID(userId,
                                                              requestBody.getAssetManagerGUID(),
                                                              requestBody.getAssetManagerName(),
                                                              glossaryGUID,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              requestBody.getEffectiveTime(),
                                                              methodName));
            }
            else
            {
                response.setElement(handler.getGlossaryByGUID(userId,
                                                              null,
                                                              null,
                                                              glossaryGUID,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              null,
                                                              methodName));
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

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryForCategory(userId,
                                                                   requestBody.getAssetManagerGUID(),
                                                                   requestBody.getAssetManagerName(),
                                                                   glossaryCategoryGUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   requestBody.getEffectiveTime(),
                                                                   methodName));
            }
            else
            {
                response.setElement(handler.getGlossaryForCategory(userId,
                                                                   null,
                                                                   null,
                                                                   glossaryCategoryGUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   null,
                                                                   methodName));
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

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryForTerm(userId,
                                                               requestBody.getAssetManagerGUID(),
                                                               requestBody.getAssetManagerName(),
                                                               glossaryTermGUID,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               requestBody.getEffectiveTime(),
                                                               methodName));
            }
            else
            {
                response.setElement(handler.getGlossaryForTerm(userId,
                                                               null,
                                                               null,
                                                               glossaryTermGUID,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               null,
                                                               methodName));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param isRootCategory is this category a root category?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties about the glossary category to store
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryCategory(String                         serverName,
                                               String                         userId,
                                               String                         glossaryGUID,
                                               boolean                        assetManagerIsHome,
                                               boolean                        isRootCategory,
                                               boolean                        forLineage,
                                               boolean                        forDuplicateProcessing,
                                               ReferenceableUpdateRequestBody requestBody)
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
                if (requestBody.getElementProperties() instanceof GlossaryCategoryProperties properties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    response.setGUID(handler.createGlossaryCategory(userId,
                                                                    glossaryGUID,
                                                                    requestBody.getMetadataCorrelationProperties(),
                                                                    assetManagerIsHome,
                                                                    properties,
                                                                    isRootCategory,
                                                                    requestBody.getUpdateDescription(),
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    requestBody.getEffectiveTime(),
                                                                    methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryCategoryProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a glossary category using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryCategoryFromTemplate(String               serverName,
                                                           String               userId,
                                                           String               glossaryGUID,
                                                           String               templateGUID,
                                                           boolean              assetManagerIsHome,
                                                           boolean              deepCopy,
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
                                                                            assetManagerIsHome,
                                                                            glossaryGUID,
                                                                            templateGUID,
                                                                            requestBody.getElementProperties(),
                                                                            deepCopy,
                                                                            methodName));
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
     * Update the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new properties for the metadata element
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryCategory(String                         serverName,
                                               String                         userId,
                                               String                         glossaryCategoryGUID,
                                               boolean                        isMergeUpdate,
                                               boolean                        forLineage,
                                               boolean                        forDuplicateProcessing,
                                               ReferenceableUpdateRequestBody requestBody)
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
                if (requestBody.getElementProperties() instanceof GlossaryCategoryProperties properties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);


                    handler.updateGlossaryCategory(userId,
                                                   requestBody.getMetadataCorrelationProperties(),
                                                   glossaryCategoryGUID,
                                                   properties,
                                                   requestBody.getUpdateDescription(),
                                                   isMergeUpdate,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   requestBody.getEffectiveTime(),
                                                   methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryCategoryProperties.class.getName(), methodName);
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
     * Remove the metadata element representing a glossary category.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeGlossaryCategory(String                         serverName,
                                               String                         userId,
                                               String                         glossaryCategoryGUID,
                                               boolean                        forLineage,
                                               boolean                        forDuplicateProcessing,
                                               ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "removeGlossaryCategory";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGlossaryCategory(userId,
                                               requestBody.getMetadataCorrelationProperties(),
                                               glossaryCategoryGUID,
                                               forLineage,
                                               forDuplicateProcessing,
                                               requestBody.getEffectiveTime(), methodName);
            }
            else
            {
                handler.removeGlossaryCategory(userId,
                                               null,
                                               glossaryCategoryGUID,
                                               forLineage,
                                               forDuplicateProcessing,
                                               null,
                                               methodName);
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
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findGlossaryCategories(userId,
                                                                       requestBody.getAssetManagerGUID(),
                                                                       requestBody.getAssetManagerName(),
                                                                       requestBody.getGlossaryGUID(),
                                                                       requestBody.getSearchString(),
                                                                       requestBody.getSearchStringParameterName(),
                                                                       startFrom,
                                                                       pageSize,
                                                                       requestBody.getEffectiveTime(),
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       methodName));
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

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getCategoriesForGlossary(userId,
                                                                         requestBody.getAssetManagerGUID(),
                                                                         requestBody.getAssetManagerName(),
                                                                         glossaryGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         requestBody.getEffectiveTime(),
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
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         null,
                                                                         methodName));
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
     * Return the list of categories associated with a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of metadata elements describing the categories associated with the requested term or
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

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getCategoriesForTerm(userId,
                                                                     requestBody.getAssetManagerGUID(),
                                                                     requestBody.getAssetManagerName(),
                                                                     glossaryTermGUID,
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     requestBody.getEffectiveTime(),
                                                                     methodName));
            }
            else
            {
                response.setElementList(handler.getCategoriesForTerm(userId,
                                                                     null,
                                                                     null,
                                                                     glossaryTermGUID,
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     null,
                                                                     methodName));
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
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getGlossaryCategoriesByName(userId,
                                                                            requestBody.getAssetManagerGUID(),
                                                                            requestBody.getAssetManagerName(),
                                                                            requestBody.getGlossaryGUID(),
                                                                            requestBody.getName(),
                                                                            requestBody.getNameParameterName(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            requestBody.getEffectiveTime(),
                                                                            methodName));
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

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryCategoryByGUID(userId,
                                                                      requestBody.getAssetManagerGUID(),
                                                                      requestBody.getAssetManagerName(),
                                                                      glossaryCategoryGUID,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      requestBody.getEffectiveTime(),
                                                                      methodName));
            }
            else
            {
                response.setElement(handler.getGlossaryCategoryByGUID(userId,
                                                                      null,
                                                                      null,
                                                                      glossaryCategoryGUID,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      null,
                                                                      methodName));
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

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryCategoryParent(userId,
                                                                      requestBody.getAssetManagerGUID(),
                                                                      requestBody.getAssetManagerName(),
                                                                      glossaryCategoryGUID,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      requestBody.getEffectiveTime(),
                                                                      methodName));
            }
            else
            {
                response.setElement(handler.getGlossaryCategoryParent(userId,
                                                                      null,
                                                                      null,
                                                                      glossaryCategoryGUID,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      null,
                                                                      methodName));
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

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getGlossarySubCategories(userId,
                                                                         requestBody.getAssetManagerGUID(),
                                                                         requestBody.getAssetManagerName(),
                                                                         glossaryCategoryGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         requestBody.getEffectiveTime(),
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
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         null,
                                                                         methodName));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the glossary term
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryTerm(String                         serverName,
                                           String                         userId,
                                           String                         glossaryGUID,
                                           boolean                        assetManagerIsHome,
                                           boolean                        forLineage,
                                           boolean                        forDuplicateProcessing,
                                           ReferenceableUpdateRequestBody requestBody)
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
                if (requestBody.getElementProperties() instanceof GlossaryTermProperties properties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    response.setGUID(handler.createGlossaryTerm(userId,
                                                                glossaryGUID,
                                                                requestBody.getMetadataCorrelationProperties(),
                                                                assetManagerIsHome,
                                                                properties,
                                                                requestBody.getUpdateDescription(),
                                                                requestBody.getEffectiveTime(),
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryTermProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a glossary term whose lifecycle is managed through a controlled workflow.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
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
                                                     boolean                           assetManagerIsHome,
                                                     boolean                           forLineage,
                                                     boolean                           forDuplicateProcessing,
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
                                                                      assetManagerIsHome,
                                                                      requestBody.getElementProperties(),
                                                                      requestBody.getInitialStatus(),
                                                                      requestBody.getUpdateDescription(),
                                                                      requestBody.getEffectiveTime(),
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      methodName));
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
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param templateSubstitute is this element a template substitute (used as the "other end" of a new/updated relationship)
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryTermFromTemplate(String                       serverName,
                                                       String                       userId,
                                                       String                       glossaryGUID,
                                                       String                       templateGUID,
                                                       boolean                      assetManagerIsHome,
                                                       boolean                      deepCopy,
                                                       boolean                      templateSubstitute,
                                                       GlossaryTemplateRequestBody  requestBody)
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
                                                                        assetManagerIsHome,
                                                                        glossaryGUID,
                                                                        templateGUID,
                                                                        requestBody.getElementProperties(),
                                                                        requestBody.getGlossaryTermStatus(),
                                                                        deepCopy,
                                                                        templateSubstitute,
                                                                        methodName));
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
     * Update the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new properties for the glossary term
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryTerm(String                         serverName,
                                           String                         userId,
                                           String                         glossaryTermGUID,
                                           boolean                        isMergeUpdate,
                                           boolean                        forLineage,
                                           boolean                        forDuplicateProcessing,
                                           ReferenceableUpdateRequestBody requestBody)
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
                if (requestBody.getElementProperties() instanceof GlossaryTermProperties properties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    handler.updateGlossaryTerm(userId,
                                               requestBody.getMetadataCorrelationProperties(),
                                               glossaryTermGUID,
                                               properties,
                                               requestBody.getUpdateDescription(),
                                               isMergeUpdate,
                                               forLineage,
                                               forDuplicateProcessing,
                                               requestBody.getEffectiveTime(),
                                               methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryTermProperties.class.getName(), methodName);
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
     * Update the status of the metadata element representing a glossary term.  This is only valid on
     * a controlled glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
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
                                                 boolean                       forLineage,
                                                 boolean                       forDuplicateProcessing,
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
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime(),
                                                 methodName);
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
     * Update the glossary term using the properties and classifications from the parentGUID stored in the request body.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param isMergeClassifications should the classification be merged or replace the target entity?
     * @param isMergeProperties should the properties be merged with the existing ones or replace them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryTermFromTemplate(String                         serverName,
                                                       String                         userId,
                                                       String                         glossaryTermGUID,
                                                       boolean                        isMergeClassifications,
                                                       boolean                        isMergeProperties,
                                                       boolean                        forLineage,
                                                       boolean                        forDuplicateProcessing,
                                                       ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateGlossaryTermFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.updateGlossaryTermFromTemplate(userId,
                                                       requestBody.getMetadataCorrelationProperties(),
                                                       glossaryTermGUID,
                                                       requestBody.getParentGUID(),
                                                       requestBody.getUpdateDescription(),
                                                       isMergeClassifications,
                                                       isMergeProperties,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       requestBody.getEffectiveTime(),
                                                       methodName);
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
     * Move a glossary term from one glossary to another.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse moveGlossaryTerm(String                         serverName,
                                         String                         userId,
                                         String                         glossaryTermGUID,
                                         boolean                        forLineage,
                                         boolean                        forDuplicateProcessing,
                                         ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "moveGlossaryTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                handler.moveGlossaryTerm(userId,
                                         requestBody.getMetadataCorrelationProperties(),
                                         glossaryTermGUID,
                                         requestBody.getParentGUID(),
                                         forLineage,
                                         forDuplicateProcessing,
                                         requestBody.getEffectiveTime(),
                                         methodName);
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
     * Return the list of term-to-term relationship names.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @return list of type names that are subtypes of asset or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    public NameListResponse getTermRelationshipTypeNames(String serverName,
                                                         String userId)
    {
        final String   methodName = "getTermRelationshipTypeNames";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NameListResponse response = new NameListResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            response.setNames(handler.getTermRelationshipTypeNames());
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsAbstractConcept(String                    serverName,
                                                 String                    userId,
                                                 String                    glossaryTermGUID,
                                                 boolean                   forLineage,
                                                 boolean                   forDuplicateProcessing,
                                                 ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsAbstractConcept";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setTermAsAbstractConcept(userId,
                                                 requestBody.getMetadataCorrelationProperties(),
                                                 glossaryTermGUID,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime(),
                                                 methodName);
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
     * Remove the abstract concept designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsAbstractConcept(String                    serverName,
                                                   String                    userId,
                                                   String                    glossaryTermGUID,
                                                   boolean                   forLineage,
                                                   boolean                   forDuplicateProcessing,
                                                   ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsAbstractConcept";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            handler.clearTermAsAbstractConcept(userId,
                                               requestBody.getMetadataCorrelationProperties(),
                                               glossaryTermGUID,
                                               forLineage,
                                               forDuplicateProcessing,
                                               requestBody.getEffectiveTime(),
                                               methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsDataValue(String                    serverName,
                                           String                    userId,
                                           String                    glossaryTermGUID,
                                           boolean                   forLineage,
                                           boolean                   forDuplicateProcessing,
                                           ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setTermAsDataValue(userId,
                                           requestBody.getMetadataCorrelationProperties(),
                                           glossaryTermGUID,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody.getEffectiveTime(),
                                           methodName);
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
     * Remove the data value designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsDataValue(String                    serverName,
                                             String                    userId,
                                             String                    glossaryTermGUID,
                                             boolean                   forLineage,
                                             boolean                   forDuplicateProcessing,
                                             ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsDataValue(userId,
                                             requestBody.getMetadataCorrelationProperties(),
                                             glossaryTermGUID,
                                             forLineage,
                                             forDuplicateProcessing,
                                             requestBody.getEffectiveTime(), methodName);
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
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody type of activity and correlators
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsActivity(String                    serverName,
                                          String                    userId,
                                          String                    glossaryTermGUID,
                                          boolean                   forLineage,
                                          boolean                   forDuplicateProcessing,
                                          ClassificationRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof ActivityDescriptionProperties properties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    handler.setTermAsActivity(userId,
                                              requestBody.getMetadataCorrelationProperties(),
                                              glossaryTermGUID,
                                              properties.getActivityType(),
                                              forLineage,
                                              forDuplicateProcessing,
                                              requestBody.getEffectiveTime(),
                                              methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ActivityDescriptionProperties.class.getName(), methodName);
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
     * Remove the activity designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsActivity(String                    serverName,
                                            String                    userId,
                                            String                    glossaryTermGUID,
                                            boolean                   forLineage,
                                            boolean                   forDuplicateProcessing,
                                            ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsActivity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsActivity(userId,
                                            requestBody.getMetadataCorrelationProperties(),
                                            glossaryTermGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            requestBody.getEffectiveTime(),
                                            methodName);
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
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody more details of the context
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsContext(String                    serverName,
                                         String                    userId,
                                         String                    glossaryTermGUID,
                                         boolean                   forLineage,
                                         boolean                   forDuplicateProcessing,
                                         ClassificationRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof GlossaryTermContextDefinition properties)
                {
                    GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                    handler.setTermAsContext(userId,
                                             requestBody.getMetadataCorrelationProperties(),
                                             glossaryTermGUID,
                                             properties,
                                             forLineage,
                                             forDuplicateProcessing,
                                             requestBody.getEffectiveTime(),
                                             methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryTermContextDefinition.class.getName(), methodName);
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
     * Remove the context definition designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsContext(String                    serverName,
                                           String                    userId,
                                           String                    glossaryTermGUID,
                                           boolean                   forLineage,
                                           boolean                   forDuplicateProcessing,
                                           ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsContext";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsContext(userId,
                                           requestBody.getMetadataCorrelationProperties(),
                                           glossaryTermGUID,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody.getEffectiveTime(),
                                           methodName);
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
     * Classify the glossary term to indicate that it describes a spine object.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsSpineObject(String                    serverName,
                                             String                    userId,
                                             String                    glossaryTermGUID,
                                             boolean                   forLineage,
                                             boolean                   forDuplicateProcessing,
                                             ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsSpineObject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setTermAsSpineObject(userId,
                                             requestBody.getMetadataCorrelationProperties(),
                                             glossaryTermGUID,
                                             forLineage,
                                             forDuplicateProcessing,
                                             requestBody.getEffectiveTime(),
                                             methodName);
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
     * Remove the spine object designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsSpineObject(String                    serverName,
                                               String                    userId,
                                               String                    glossaryTermGUID,
                                               boolean                   forLineage,
                                               boolean                   forDuplicateProcessing,
                                               ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsSpineObject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsSpineObject(userId,
                                               requestBody.getMetadataCorrelationProperties(),
                                               glossaryTermGUID,
                                               forLineage,
                                               forDuplicateProcessing,
                                               requestBody.getEffectiveTime(),
                                               methodName);
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
     * Classify the glossary term to indicate that it describes a spine attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsSpineAttribute(String                    serverName,
                                                String                    userId,
                                                String                    glossaryTermGUID,
                                                boolean                   forLineage,
                                                boolean                   forDuplicateProcessing,
                                                ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsSpineAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setTermAsSpineAttribute(userId,
                                                requestBody.getMetadataCorrelationProperties(),
                                                glossaryTermGUID,
                                                forLineage,
                                                forDuplicateProcessing,
                                                requestBody.getEffectiveTime(),
                                                methodName);
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
     * Remove the spine attribute designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsSpineAttribute(String                    serverName,
                                                  String                    userId,
                                                  String                    glossaryTermGUID,
                                                  boolean                   forLineage,
                                                  boolean                   forDuplicateProcessing,
                                                  ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsSpineAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsSpineAttribute(userId,
                                                  requestBody.getMetadataCorrelationProperties(),
                                                  glossaryTermGUID,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  requestBody.getEffectiveTime(), methodName);
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
     * Classify the glossary term to indicate that it describes an object identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsObjectIdentifier(String                    serverName,
                                                  String                    userId,
                                                  String                    glossaryTermGUID,
                                                  boolean                   forLineage,
                                                  boolean                   forDuplicateProcessing,
                                                  ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsObjectIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setTermAsObjectIdentifier(userId,
                                                  requestBody.getMetadataCorrelationProperties(),
                                                  glossaryTermGUID,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  requestBody.getEffectiveTime(),
                                                  methodName);
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
     * Remove the object identifier designation from the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsObjectIdentifier(String                    serverName,
                                                    String                    userId,
                                                    String                    glossaryTermGUID,
                                                    boolean                   forLineage,
                                                    boolean                   forDuplicateProcessing,
                                                    ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsObjectIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsObjectIdentifier(userId,
                                                    requestBody.getMetadataCorrelationProperties(),
                                                    glossaryTermGUID,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    requestBody.getEffectiveTime(),
                                                    methodName);
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
     * Undo the last update to the glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElementResponse undoGlossaryTermUpdate(String            serverName,
                                                              String            userId,
                                                              String            glossaryTermGUID,
                                                              boolean           forLineage,
                                                              boolean           forDuplicateProcessing,
                                                              UpdateRequestBody requestBody)
    {
        final String methodName = "undoGlossaryTermUpdate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementResponse response = new GlossaryTermElementResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.undoGlossaryTermUpdate(userId,
                                                                   requestBody.getMetadataCorrelationProperties(),
                                                                   glossaryTermGUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   requestBody.getEffectiveTime(),
                                                                   methodName));
            }
            else
            {
                response.setElement(handler.undoGlossaryTermUpdate(userId,
                                                                   null,
                                                                   glossaryTermGUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   new Date(),
                                                                   methodName));
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
     * Archive the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to archive
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse archiveGlossaryTerm(String             serverName,
                                            String             userId,
                                            String             glossaryTermGUID,
                                            boolean            forDuplicateProcessing,
                                            ArchiveRequestBody requestBody)
    {
        final String methodName = "archiveGlossaryTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.archiveGlossaryTerm(userId,
                                           requestBody.getMetadataCorrelationProperties(),
                                           glossaryTermGUID,
                                           requestBody.getElementProperties(),
                                           forDuplicateProcessing,
                                           requestBody.getEffectiveTime(),
                                           methodName);
            }
            else
            {
                handler.archiveGlossaryTerm(userId,
                                            null,
                                            glossaryTermGUID,
                                            null,
                                            forDuplicateProcessing,
                                            null,
                                            methodName);
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
     * Remove the metadata element representing a glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeGlossaryTerm(String                         serverName,
                                           String                         userId,
                                           String                         glossaryTermGUID,
                                           boolean                        forLineage,
                                           boolean                        forDuplicateProcessing,
                                           ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "removeGlossaryTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGlossaryTerm(userId,
                                           requestBody.getMetadataCorrelationProperties(),
                                           glossaryTermGUID,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody.getEffectiveTime(),
                                           methodName);
            }
            else
            {
                handler.removeGlossaryTerm(userId,
                                           null,
                                           glossaryTermGUID,
                                           forLineage,
                                           forDuplicateProcessing,
                                           null,
                                           methodName);
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
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findGlossaryTerms(userId,
                                                                  requestBody.getAssetManagerGUID(),
                                                                  requestBody.getAssetManagerName(),
                                                                  requestBody.getGlossaryGUID(),
                                                                  requestBody.getSearchString(),
                                                                  requestBody.getLimitResultsByStatus(),
                                                                  startFrom,
                                                                  pageSize,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
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
    public GlossaryTermElementsResponse getTermsForGlossary(String                              serverName,
                                                            String                              userId,
                                                            String                              glossaryGUID,
                                                            int                                 startFrom,
                                                            int                                 pageSize,
                                                            boolean                             forLineage,
                                                            boolean                             forDuplicateProcessing,
                                                            EffectiveTimeQueryRequestBody       requestBody)
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
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    requestBody.getEffectiveTime(),
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
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    null,
                                                                    methodName));
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

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getTermsForGlossaryCategory(userId,
                                                                            requestBody.getAssetManagerGUID(),
                                                                            requestBody.getAssetManagerName(),
                                                                            glossaryCategoryGUID,
                                                                            requestBody.getLimitResultsByStatus(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            requestBody.getEffectiveTime(),
                                                                            methodName));
            }
            else
            {
                response.setElementList(handler.getTermsForGlossaryCategory(userId,
                                                                            null,
                                                                            null,
                                                                            glossaryCategoryGUID,
                                                                            null,
                                                                            startFrom,
                                                                            pageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            null,
                                                                            methodName));
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
     * Retrieve the list of glossary terms associated with the requested glossary term.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term of interest
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
        final String methodName = "getRelatedTerms";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getRelatedTerms(userId,
                                                                requestBody.getAssetManagerGUID(),
                                                                requestBody.getAssetManagerName(),
                                                                glossaryTermGUID,
                                                                requestBody.getRelationshipTypeName(),
                                                                requestBody.getLimitResultsByStatus(),
                                                                startFrom,
                                                                pageSize,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                requestBody.getEffectiveTime(),
                                                                methodName));
            }
            else
            {
                response.setElementList(handler.getRelatedTerms(userId,
                                                                null,
                                                                null,
                                                                glossaryTermGUID,
                                                                null,
                                                                null,
                                                                startFrom,
                                                                pageSize,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                null,
                                                                methodName));
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
                GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getGlossaryTermsByName(userId,
                                                                       requestBody.getAssetManagerGUID(),
                                                                       requestBody.getAssetManagerName(),
                                                                       requestBody.getGlossaryGUID(),
                                                                       requestBody.getName(),
                                                                       requestBody.getLimitResultsByStatus(),
                                                                       startFrom,
                                                                       pageSize,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       requestBody.getEffectiveTime(),
                                                                       methodName));
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

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGlossaryTermByGUID(userId,
                                                                  requestBody.getAssetManagerGUID(),
                                                                  requestBody.getAssetManagerName(),
                                                                  guid,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
            }
            else
            {
                response.setElement(handler.getGlossaryTermByGUID(userId,
                                                                  null,
                                                                  null,
                                                                  guid,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  null,
                                                                  methodName));
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
    public GlossaryTermElementsResponse getGlossaryTermHistory(String                                serverName,
                                                               String                                userId,
                                                               String                                guid,
                                                               int                                   startFrom,
                                                               int                                   pageSize,
                                                               boolean                               oldestFirst,
                                                               boolean                               forLineage,
                                                               boolean                               forDuplicateProcessing,
                                                               HistoryExternalIdentifiersRequestBody requestBody)
    {
        final String methodName = "getGlossaryTermHistory";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getGlossaryTermHistory(userId,
                                                                       requestBody.getExternalScopeGUID(),
                                                                       requestBody.getExternalScopeName(),
                                                                       guid,
                                                                       requestBody.getFromTime(),
                                                                       requestBody.getToTime(),
                                                                       startFrom,
                                                                       pageSize,
                                                                       oldestFirst,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       requestBody.getEffectiveTime(),
                                                                       methodName));
            }
            else
            {
                response.setElementList(handler.getGlossaryTermHistory(userId,
                                                                       null,
                                                                       null,
                                                                       guid,
                                                                       null,
                                                                       null,
                                                                       startFrom,
                                                                       pageSize,
                                                                       oldestFirst,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       null,
                                                                       methodName));
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
