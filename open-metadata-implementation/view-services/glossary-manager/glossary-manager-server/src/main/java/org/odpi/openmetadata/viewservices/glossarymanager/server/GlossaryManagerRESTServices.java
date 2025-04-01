/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossarymanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.client.management.GlossaryManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermActivityType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermRelationshipStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.ArchiveRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.ClassificationRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.ControlledGlossaryTermRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTemplateRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermStatusRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.ReferenceableUpdateRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.RelationshipRequestBody;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.TemplateRequestBody;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;


/**
 * The GlossaryManagerRESTServices provides the implementation of the Glossary ManagerOpen Metadata View Service (OMVS).
 * This interface provides view interfaces for infrastructure and ops users.
 */

public class GlossaryManagerRESTServices extends TokenController
{
    private static final GlossaryManagerInstanceHandler instanceHandler = new GlossaryManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GlossaryManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public GlossaryManagerRESTServices()
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
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossary(String                   serverName,
                                       ReferenceableRequestBody requestBody)
    {
        final String   methodName = "createGlossary";

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
                if (requestBody.getElementProperties() instanceof GlossaryProperties glossaryProperties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    response.setGUID(handler.createGlossary(userId, glossaryProperties));
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
                                                   String              templateGUID,
                                                   boolean             deepCopy,
                                                   TemplateRequestBody requestBody)
    {
        final String methodName = "createGlossaryFromTemplate";

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
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setGUID(handler.createGlossaryFromTemplate(userId, templateGUID, requestBody.getElementProperties(), deepCopy));
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
                                       String                         glossaryGUID,
                                       boolean                        isMergeUpdate,
                                       boolean                        forLineage,
                                       boolean                        forDuplicateProcessing,
                                       ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateGlossary";

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
                if (requestBody.getElementProperties() instanceof GlossaryProperties glossaryProperties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.updateGlossary(userId,
                                           glossaryGUID,
                                           isMergeUpdate,
                                           glossaryProperties,
                                           requestBody.getEffectiveTime(),
                                           forLineage,
                                           forDuplicateProcessing);
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
    public VoidResponse removeGlossary(String                         serverName,
                                       String                         glossaryGUID,
                                       boolean                        forLineage,
                                       boolean                        forDuplicateProcessing,
                                       ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "removeGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGlossary(userId,
                                       glossaryGUID,
                                       requestBody.getEffectiveTime(),
                                       forLineage,
                                       forDuplicateProcessing);
            }
            else
            {
                handler.removeGlossary(userId,
                                       glossaryGUID,
                                       null,
                                       forLineage,
                                       forDuplicateProcessing);
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
                                                     String                    glossaryGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     ClassificationRequestBody requestBody)
    {
        final String methodName = "setGlossaryAsEditingGlossary";

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
                if (requestBody.getProperties() instanceof EditingGlossaryProperties properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.setGlossaryAsEditingGlossary(userId,
                                                         glossaryGUID,
                                                         properties,
                                                         requestBody.getEffectiveTime(),
                                                         forLineage,
                                                         forDuplicateProcessing);
                }
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
                                                       String                    glossaryGUID,
                                                       boolean                   forLineage,
                                                       boolean                   forDuplicateProcessing,
                                                       ClassificationRequestBody requestBody)
    {
        final String methodName = "clearGlossaryAsEditingGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearGlossaryAsEditingGlossary(userId,
                                                       glossaryGUID,
                                                       requestBody.getEffectiveTime(),
                                                       forLineage,
                                                       forDuplicateProcessing);
            }
            else
            {
                handler.clearGlossaryAsEditingGlossary(userId,
                                                       glossaryGUID,
                                                       null,
                                                       forLineage,
                                                       forDuplicateProcessing);
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
     * Classify the glossary to indicate that it is a staging glossary - this means it is
     * a collection of glossary updates that will be transferred into another glossary.
     *
     * @param serverName name of the server to route the request to
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
                                                     String                    glossaryGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     ClassificationRequestBody requestBody)
    {
        final String methodName = "setGlossaryAsStagingGlossary";

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
                if (requestBody.getProperties() instanceof StagingGlossaryProperties properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.setGlossaryAsStagingGlossary(userId,
                                                         glossaryGUID,
                                                         properties,
                                                         requestBody.getEffectiveTime(),
                                                         forLineage,
                                                         forDuplicateProcessing);
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
                                                       String                    glossaryGUID,
                                                       boolean                   forLineage,
                                                       boolean                   forDuplicateProcessing,
                                                       ClassificationRequestBody requestBody)
    {
        final String methodName = "clearGlossaryAsStagingGlossary";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearGlossaryAsStagingGlossary(userId,
                                                       glossaryGUID,
                                                       requestBody.getEffectiveTime(),
                                                       forLineage,
                                                       forDuplicateProcessing);
            }
            else
            {
                handler.clearGlossaryAsStagingGlossary(userId,
                                                       glossaryGUID,
                                                       null,
                                                       forLineage,
                                                       forDuplicateProcessing);
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
                                              String                    glossaryGUID,
                                              boolean                   forLineage,
                                              boolean                   forDuplicateProcessing,
                                              ClassificationRequestBody requestBody)
    {
        final String methodName = "setGlossaryAsTaxonomy";

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
                if (requestBody.getProperties() instanceof TaxonomyProperties properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.setGlossaryAsTaxonomy(userId,
                                                  glossaryGUID,
                                                  properties,
                                                  requestBody.getEffectiveTime(),
                                                  forLineage,
                                                  forDuplicateProcessing);
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
                                                String                    glossaryGUID,
                                                boolean                   forLineage,
                                                boolean                   forDuplicateProcessing,
                                                ClassificationRequestBody requestBody)
    {
        final String methodName = "clearGlossaryAsTaxonomy";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearGlossaryAsTaxonomy(userId,
                                                glossaryGUID,
                                                requestBody.getEffectiveTime(),
                                                forLineage,
                                                forDuplicateProcessing);
            }
            else
            {
                handler.clearGlossaryAsTaxonomy(userId,
                                                glossaryGUID,
                                                null,
                                                forLineage,
                                                forDuplicateProcessing);
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
                                               String                    glossaryGUID,
                                               boolean                   forLineage,
                                               boolean                   forDuplicateProcessing,
                                               ClassificationRequestBody requestBody)
    {
        final String methodName = "setGlossaryAsCanonical";

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
                if (requestBody.getProperties() instanceof CanonicalVocabularyProperties properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.setGlossaryAsCanonical(userId,
                                                   glossaryGUID,
                                                   properties,
                                                   requestBody.getEffectiveTime(),
                                                   forLineage,
                                                   forDuplicateProcessing);
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
                                                 String                    glossaryGUID,
                                                 boolean                   forLineage,
                                                 boolean                   forDuplicateProcessing,
                                                 ClassificationRequestBody requestBody)
    {
        final String methodName = "clearGlossaryAsCanonical";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearGlossaryAsCanonical(userId,
                                                 glossaryGUID,
                                                 requestBody.getEffectiveTime(),
                                                 forLineage,
                                                 forDuplicateProcessing);
            }
            else
            {
                handler.clearGlossaryAsCanonical(userId,
                                                 glossaryGUID,
                                                 null,
                                                 forLineage,
                                                 forDuplicateProcessing);
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
     * @param glossaryGUID unique identifier of the glossary where the category is located
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
                                               String                         glossaryGUID,
                                               boolean                        isRootCategory,
                                               boolean                        forLineage,
                                               boolean                        forDuplicateProcessing,
                                               ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "createGlossaryCategory";

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
                if (requestBody.getElementProperties() instanceof GlossaryCategoryProperties properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    response.setGUID(handler.createGlossaryCategory(userId,
                                                                    glossaryGUID,
                                                                    properties,
                                                                    isRootCategory,
                                                                    requestBody.getEffectiveTime(),
                                                                    forLineage,
                                                                    forDuplicateProcessing));
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
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new glossary category or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryCategoryFromTemplate(String               serverName,
                                                           String               glossaryGUID,
                                                           String               templateGUID,
                                                           TemplateRequestBody  requestBody)
    {
        final String methodName = "createGlossaryCategoryFromTemplate";

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
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setGUID(handler.createGlossaryCategoryFromTemplate(userId,
                                                                            glossaryGUID,
                                                                            templateGUID,
                                                                            requestBody.getElementProperties(),
                                                                            true));
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
                                               String                         glossaryCategoryGUID,
                                               boolean                        isMergeUpdate,
                                               boolean                        forLineage,
                                               boolean                        forDuplicateProcessing,
                                               ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateGlossaryCategory";

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
                if (requestBody.getElementProperties() instanceof GlossaryCategoryProperties properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.updateGlossaryCategory(userId,
                                                   glossaryCategoryGUID,
                                                   isMergeUpdate,
                                                   properties,
                                                   requestBody.getEffectiveTime(),
                                                   forLineage,
                                                   forDuplicateProcessing);
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
     * Create a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupCategoryParent(String                  serverName,
                                            String                  glossaryParentCategoryGUID,
                                            String                  glossaryChildCategoryGUID,
                                            boolean                 forLineage,
                                            boolean                 forDuplicateProcessing,
                                            RelationshipRequestBody requestBody)
    {
        final String methodName = "setupCategoryParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.setupCategoryParent(userId,
                                                glossaryParentCategoryGUID,
                                                glossaryChildCategoryGUID,
                                                requestBody.getEffectiveTime(),
                                                forLineage,
                                                forDuplicateProcessing);
                }
                else
                {
                    handler.setupCategoryParent(userId,
                                                glossaryParentCategoryGUID,
                                                glossaryChildCategoryGUID,
                                                requestBody.getEffectiveTime(),
                                                forLineage,
                                                forDuplicateProcessing);
                }
            }
            else
            {
                handler.setupCategoryParent(userId,
                                            glossaryParentCategoryGUID,
                                            glossaryChildCategoryGUID,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing);
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
     * Remove a parent-child relationship between two categories.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearCategoryParent(String                        serverName,
                                            String                        glossaryParentCategoryGUID,
                                            String                        glossaryChildCategoryGUID,
                                            boolean                       forLineage,
                                            boolean                       forDuplicateProcessing,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearCategoryParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearCategoryParent(userId,
                                            glossaryParentCategoryGUID,
                                            glossaryChildCategoryGUID,
                                            requestBody.getEffectiveTime(),
                                            forLineage,
                                            forDuplicateProcessing);
            }
            else
            {
                handler.clearCategoryParent(userId,
                                            glossaryParentCategoryGUID,
                                            glossaryChildCategoryGUID,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing);
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
                                               String                         glossaryCategoryGUID,
                                               boolean                        forLineage,
                                               boolean                        forDuplicateProcessing,
                                               ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "removeGlossaryCategory";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGlossaryCategory(userId,
                                               glossaryCategoryGUID,
                                               requestBody.getEffectiveTime(),
                                               forLineage,
                                               forDuplicateProcessing);
            }
            else
            {
                handler.removeGlossaryCategory(userId,
                                               glossaryCategoryGUID,
                                               null,
                                               forLineage,
                                               forDuplicateProcessing);
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
        final String methodName = "getGlossaryTermRelationshipStatuses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GlossaryTermRelationshipStatusListResponse response = new GlossaryTermRelationshipStatusListResponse();
        AuditLog                                   auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setStatuses(Arrays.asList(GlossaryTermRelationshipStatus.values()));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
    public GlossaryTermActivityTypeListResponse getGlossaryTermActivityTypes(String serverName)
    {
        final String methodName = "getGlossaryTermActivityTypes";

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
     * @param glossaryGUID unique identifier of the glossary where the term is to be located
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
                                                     String                            glossaryGUID,
                                                     boolean                           forLineage,
                                                     boolean                           forDuplicateProcessing,
                                                     ControlledGlossaryTermRequestBody requestBody)
    {
        final String methodName = "createControlledGlossaryTerm";

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
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setGUID(handler.createControlledGlossaryTerm(userId,
                                                                      glossaryGUID,
                                                                      requestBody.getElementProperties(),
                                                                      requestBody.getInitialStatus(),
                                                                      requestBody.getEffectiveTime(),
                                                                      forLineage,
                                                                      forDuplicateProcessing));
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
     * @param glossaryGUID unique identifier of the glossary where the term is to be located
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
    public GUIDResponse createGlossaryTermFromTemplate(String                      serverName,
                                                       String                      glossaryGUID,
                                                       String                      templateGUID,
                                                       boolean                     deepCopy,
                                                       boolean                     templateSubstitute,
                                                       GlossaryTemplateRequestBody requestBody)
    {
        final String methodName = "createGlossaryTermFromTemplate";

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
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                response.setGUID(handler.createGlossaryTermFromTemplate(userId,
                                                                        glossaryGUID,
                                                                        templateGUID,
                                                                        requestBody.getElementProperties(),
                                                                        deepCopy,
                                                                        templateSubstitute,
                                                                        requestBody.getGlossaryTermStatus()));
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
                                           String                         glossaryTermGUID,
                                           boolean                        isMergeUpdate,
                                           boolean                        forLineage,
                                           boolean                        forDuplicateProcessing,
                                           ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateGlossaryTerm";

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
                if (requestBody.getElementProperties() instanceof GlossaryTermProperties properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);


                    handler.updateGlossaryTerm(userId,
                                               glossaryTermGUID,
                                               isMergeUpdate,
                                               properties,
                                               requestBody.getEffectiveTime(),
                                               forLineage,
                                               forDuplicateProcessing);
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
                                                 String                        glossaryTermGUID,
                                                 boolean                       forLineage,
                                                 boolean                       forDuplicateProcessing,
                                                 GlossaryTermStatusRequestBody requestBody)
    {
        final String methodName = "updateGlossaryTermStatus";

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
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                handler.updateGlossaryTermStatus(userId,
                                                 glossaryTermGUID,
                                                 requestBody.getGlossaryTermStatus(),
                                                 requestBody.getEffectiveTime(),
                                                 forLineage,
                                                 forDuplicateProcessing);
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
                                                       String                         glossaryTermGUID,
                                                       boolean                        isMergeClassifications,
                                                       boolean                        isMergeProperties,
                                                       boolean                        forLineage,
                                                       boolean                        forDuplicateProcessing,
                                                       ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateGlossaryTermFromTemplate";

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
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                handler.updateGlossaryTermFromTemplate(userId,
                                                       glossaryTermGUID,
                                                       requestBody.getParentGUID(),
                                                       isMergeClassifications,
                                                       isMergeProperties,
                                                       requestBody.getEffectiveTime(),
                                                       forLineage,
                                                       forDuplicateProcessing);
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
                                         String                         glossaryTermGUID,
                                         boolean                        forLineage,
                                         boolean                        forDuplicateProcessing,
                                         ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "moveGlossaryTerm";

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
                GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                handler.moveGlossaryTerm(userId,
                                         glossaryTermGUID,
                                         requestBody.getParentGUID(),
                                         requestBody.getEffectiveTime(),
                                         forLineage,
                                         forDuplicateProcessing);
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
     * Link a term to a category.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the categorization relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupTermCategory(String                   serverName,
                                          String                   glossaryCategoryGUID,
                                          String                   glossaryTermGUID,
                                          boolean                  forLineage,
                                          boolean                  forDuplicateProcessing,
                                          RelationshipRequestBody  requestBody)
    {
        final String methodName = "setupTermCategory";

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
                if (requestBody.getProperties() instanceof GlossaryTermCategorization properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.setupTermCategory(userId,
                                              glossaryCategoryGUID,
                                              glossaryTermGUID,
                                              properties,
                                              requestBody.getEffectiveTime(),
                                              forLineage,
                                              forDuplicateProcessing);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryTermCategorization.class.getName(), methodName);
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
     * Unlink a term from a category.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermCategory(String                        serverName,
                                          String                        glossaryCategoryGUID,
                                          String                        glossaryTermGUID,
                                          boolean                       forLineage,
                                          boolean                       forDuplicateProcessing,
                                          EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearTermCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermCategory(userId,
                                          glossaryCategoryGUID,
                                          glossaryTermGUID,
                                          requestBody.getEffectiveTime(),
                                          forLineage,
                                          forDuplicateProcessing);
            }
            else
            {
                handler.clearTermCategory(userId,
                                          glossaryCategoryGUID,
                                          glossaryTermGUID,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing);
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
     * @return list of type names that are subtypes of asset or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    public NameListResponse getTermRelationshipTypeNames(String serverName)
    {
        final String   methodName = "getTermRelationshipTypeNames";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        NameListResponse response = new NameListResponse();
        AuditLog         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            response.setNames(handler.getTermRelationshipTypeNames(userId));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link two terms together using a specialist relationship.
     *
     * @param serverName name of the server to route the request to
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupTermRelationship(String                  serverName,
                                              String                  glossaryTermOneGUID,
                                              String                  relationshipTypeName,
                                              String                  glossaryTermTwoGUID,
                                              boolean                 forLineage,
                                              boolean                 forDuplicateProcessing,
                                              RelationshipRequestBody requestBody)
    {
        final String methodName = "setupTermRelationship";

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
                if (requestBody.getProperties() instanceof GlossaryTermRelationship properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.setupTermRelationship(userId,
                                                  relationshipTypeName,
                                                  glossaryTermOneGUID,
                                                  glossaryTermTwoGUID,
                                                  properties,
                                                  requestBody.getEffectiveTime(),
                                                  forLineage,
                                                  forDuplicateProcessing);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryTermRelationship.class.getName(), methodName);
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
     * Update the relationship properties for the two terms.
     *
     * @param serverName name of the server to route the request to
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateTermRelationship(String                  serverName,
                                               String                  glossaryTermOneGUID,
                                               String                  relationshipTypeName,
                                               String                  glossaryTermTwoGUID,
                                               boolean                 forLineage,
                                               boolean                 forDuplicateProcessing,
                                               RelationshipRequestBody requestBody)
    {
        final String methodName = "updateTermRelationship";

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
                if (requestBody.getProperties() instanceof GlossaryTermRelationship properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.updateTermRelationship(userId,
                                                   relationshipTypeName,
                                                   glossaryTermOneGUID,
                                                   glossaryTermTwoGUID,
                                                   properties,
                                                   requestBody.getEffectiveTime(),
                                                   forLineage,
                                                   forDuplicateProcessing);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryTermRelationship.class.getName(), methodName);
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
     * Remove the relationship between two terms.
     *
     * @param serverName name of the server to route the request to
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties of the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermRelationship(String                        serverName,
                                              String                        glossaryTermOneGUID,
                                              String                        relationshipTypeName,
                                              String                        glossaryTermTwoGUID,
                                              boolean                       forLineage,
                                              boolean                       forDuplicateProcessing,
                                              EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearTermRelationship";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermRelationship(userId,
                                              relationshipTypeName,
                                              glossaryTermOneGUID,
                                              glossaryTermTwoGUID,
                                              requestBody.getEffectiveTime(),
                                              forLineage,
                                              forDuplicateProcessing);
            }
            else
            {
                handler.clearTermRelationship(userId,
                                              relationshipTypeName,
                                              glossaryTermOneGUID,
                                              glossaryTermTwoGUID,
                                              null,
                                              forLineage,
                                              forDuplicateProcessing);
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
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param serverName name of the server to route the request to
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
                                                 String                    glossaryTermGUID,
                                                 boolean                   forLineage,
                                                 boolean                   forDuplicateProcessing,
                                                 ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsAbstractConcept";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setTermAsAbstractConcept(userId,
                                                 glossaryTermGUID,
                                                 requestBody.getEffectiveTime(),
                                                 forLineage,
                                                 forDuplicateProcessing);
            }
            else
            {
                handler.setTermAsAbstractConcept(userId,
                                                 glossaryTermGUID,
                                                 null,
                                                 forLineage,
                                                 forDuplicateProcessing);
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
                                                   String                    glossaryTermGUID,
                                                   boolean                   forLineage,
                                                   boolean                   forDuplicateProcessing,
                                                   ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsAbstractConcept";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsAbstractConcept(userId,
                                                   glossaryTermGUID,
                                                   requestBody.getEffectiveTime(),
                                                   forLineage,
                                                   forDuplicateProcessing);
            }
            else
            {
                handler.clearTermAsAbstractConcept(userId,
                                                   glossaryTermGUID,
                                                   null,
                                                   forLineage,
                                                   forDuplicateProcessing);
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
                                           String                    glossaryTermGUID,
                                           boolean                   forLineage,
                                           boolean                   forDuplicateProcessing,
                                           ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setTermAsDataValue(userId,
                                           glossaryTermGUID,
                                           requestBody.getEffectiveTime(),
                                           forLineage,
                                           forDuplicateProcessing);
            }
            else
            {
                handler.setTermAsDataValue(userId,
                                           glossaryTermGUID,
                                           null,
                                           forLineage,
                                           forDuplicateProcessing);
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
                                             String                    glossaryTermGUID,
                                             boolean                   forLineage,
                                             boolean                   forDuplicateProcessing,
                                             ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsDataValue(userId,
                                             glossaryTermGUID,
                                             requestBody.getEffectiveTime(),
                                             forLineage,
                                             forDuplicateProcessing);
            }
            else
            {
                handler.clearTermAsDataValue(userId,
                                             glossaryTermGUID,
                                             null,
                                             forLineage,
                                             forDuplicateProcessing);
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
                                          String                    glossaryTermGUID,
                                          boolean                   forLineage,
                                          boolean                   forDuplicateProcessing,
                                          ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsActivity";

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
                if (requestBody.getProperties() instanceof ActivityDescriptionProperties properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.setTermAsActivity(userId,
                                              glossaryTermGUID,
                                              properties,
                                              requestBody.getEffectiveTime(),
                                              forLineage,
                                              forDuplicateProcessing);
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
                                            String                    glossaryTermGUID,
                                            boolean                   forLineage,
                                            boolean                   forDuplicateProcessing,
                                            ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsActivity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsActivity(userId,
                                            glossaryTermGUID,
                                            requestBody.getEffectiveTime(),
                                            forLineage,
                                            forDuplicateProcessing);
            }
            else
            {
                handler.clearTermAsActivity(userId,
                                            glossaryTermGUID,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing);
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
                                         String                    glossaryTermGUID,
                                         boolean                   forLineage,
                                         boolean                   forDuplicateProcessing,
                                         ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsContext";

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
                if (requestBody.getProperties() instanceof GlossaryTermContextDefinition properties)
                {
                    GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

                    handler.setTermAsContext(userId,
                                             glossaryTermGUID,
                                             properties,
                                             requestBody.getEffectiveTime(),
                                             forLineage,
                                             forDuplicateProcessing);
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
                                           String                    glossaryTermGUID,
                                           boolean                   forLineage,
                                           boolean                   forDuplicateProcessing,
                                           ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsContext";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsContext(userId,
                                           glossaryTermGUID,
                                           requestBody.getEffectiveTime(),
                                           forLineage,
                                           forDuplicateProcessing);
            }
            else
            {
                handler.clearTermAsContext(userId,
                                           glossaryTermGUID,
                                           null,
                                           forLineage,
                                           forDuplicateProcessing);
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
                                             String                    glossaryTermGUID,
                                             boolean                   forLineage,
                                             boolean                   forDuplicateProcessing,
                                             ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsSpineObject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setTermAsSpineObject(userId,
                                             glossaryTermGUID,
                                             requestBody.getEffectiveTime(),
                                             forLineage,
                                             forDuplicateProcessing);
            }
            else
            {
                handler.setTermAsSpineObject(userId,
                                             glossaryTermGUID,
                                             null,
                                             forLineage,
                                             forDuplicateProcessing);
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
                                               String                    glossaryTermGUID,
                                               boolean                   forLineage,
                                               boolean                   forDuplicateProcessing,
                                               ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsSpineObject";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsSpineObject(userId,
                                               glossaryTermGUID,
                                               requestBody.getEffectiveTime(),
                                               forLineage,
                                               forDuplicateProcessing);
            }
            else
            {
                handler.clearTermAsSpineObject(userId,
                                               glossaryTermGUID,
                                               null,
                                               forLineage,
                                               forDuplicateProcessing);
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
                                                String                    glossaryTermGUID,
                                                boolean                   forLineage,
                                                boolean                   forDuplicateProcessing,
                                                ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsSpineAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setTermAsSpineAttribute(userId,
                                                glossaryTermGUID,
                                                requestBody.getEffectiveTime(),
                                                forLineage,
                                                forDuplicateProcessing);
            }
            else
            {
                handler.setTermAsSpineAttribute(userId,
                                                glossaryTermGUID,
                                                null,
                                                forLineage,
                                                forDuplicateProcessing);
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
                                                  String                    glossaryTermGUID,
                                                  boolean                   forLineage,
                                                  boolean                   forDuplicateProcessing,
                                                  ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsSpineAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsSpineAttribute(userId,
                                                  glossaryTermGUID,
                                                  requestBody.getEffectiveTime(),
                                                  forLineage,
                                                  forDuplicateProcessing);
            }
            else
            {
                handler.clearTermAsSpineAttribute(userId,
                                                  glossaryTermGUID,
                                                  null,
                                                  forLineage,
                                                  forDuplicateProcessing);
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
                                                  String                    glossaryTermGUID,
                                                  boolean                   forLineage,
                                                  boolean                   forDuplicateProcessing,
                                                  ClassificationRequestBody requestBody)
    {
        final String methodName = "setTermAsObjectIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setTermAsObjectIdentifier(userId,
                                                  glossaryTermGUID,
                                                  requestBody.getEffectiveTime(),
                                                  forLineage,
                                                  forDuplicateProcessing);
            }
            else
            {
                handler.setTermAsObjectIdentifier(userId,
                                                  glossaryTermGUID,
                                                  null,
                                                  forLineage,
                                                  forDuplicateProcessing);
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
                                                    String                    glossaryTermGUID,
                                                    boolean                   forLineage,
                                                    boolean                   forDuplicateProcessing,
                                                    ClassificationRequestBody requestBody)
    {
        final String methodName = "clearTermAsObjectIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearTermAsObjectIdentifier(userId,
                                                    glossaryTermGUID,
                                                    requestBody.getEffectiveTime(),
                                                    forLineage,
                                                    forDuplicateProcessing);
            }
            else
            {
                handler.clearTermAsObjectIdentifier(userId,
                                                    glossaryTermGUID,
                                                    null,
                                                    forLineage,
                                                    forDuplicateProcessing);
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
    public GlossaryTermElementResponse undoGlossaryTermUpdate(String                        serverName,
                                                              String                        glossaryTermGUID,
                                                              boolean                       forLineage,
                                                              boolean                       forDuplicateProcessing,
                                                              EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "undoGlossaryTermUpdate";

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
                response.setElement(handler.undoGlossaryTermUpdate(userId,
                                                                   glossaryTermGUID,
                                                                   requestBody.getEffectiveTime(),
                                                                   forLineage,
                                                                   forDuplicateProcessing));
            }
            else
            {
                response.setElement(handler.undoGlossaryTermUpdate(userId,
                                                                   glossaryTermGUID,
                                                                   new Date(),
                                                                   forLineage,
                                                                   forDuplicateProcessing));
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
                                            String             glossaryTermGUID,
                                            boolean            forDuplicateProcessing,
                                            ArchiveRequestBody requestBody)
    {
        final String methodName = "archiveGlossaryTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.archiveGlossaryTerm(userId,
                                            glossaryTermGUID,
                                            requestBody.getElementProperties(),
                                            requestBody.getEffectiveTime(),
                                            forDuplicateProcessing);
            }
            else
            {
                handler.archiveGlossaryTerm(userId,
                                            glossaryTermGUID,
                                            null,
                                            null,
                                            forDuplicateProcessing);
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
                                           String                         glossaryTermGUID,
                                           boolean                        forLineage,
                                           boolean                        forDuplicateProcessing,
                                           ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "removeGlossaryTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryManagementClient handler = instanceHandler.getGlossaryManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGlossaryTerm(userId,
                                           glossaryTermGUID,
                                           requestBody.getEffectiveTime(),
                                           forLineage,
                                           forDuplicateProcessing);
            }
            else
            {
                handler.removeGlossaryTerm(userId,
                                           glossaryTermGUID,
                                           null,
                                           forLineage,
                                           forDuplicateProcessing);
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
