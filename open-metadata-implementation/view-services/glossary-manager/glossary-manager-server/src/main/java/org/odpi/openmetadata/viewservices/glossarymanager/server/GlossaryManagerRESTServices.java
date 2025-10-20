/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.glossarymanager.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.TermRelationshipStatus;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GlossaryTermHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermActivityTypeListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermRelationshipStatusListResponse;
import org.odpi.openmetadata.viewservices.glossarymanager.rest.GlossaryTermStatusListResponse;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


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
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setGlossaryAsTaxonomy(String                    serverName,
                                              String                    glossaryGUID,
                                              NewClassificationRequestBody requestBody)
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

            CollectionHandler handler = instanceHandler.getGlossaryHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof TaxonomyProperties properties)
                {
                    handler.setGlossaryAsTaxonomy(userId, glossaryGUID, properties, requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setGlossaryAsTaxonomy(userId, glossaryGUID, null, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TaxonomyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.setGlossaryAsTaxonomy(userId, glossaryGUID, null, null);
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
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGlossaryAsTaxonomy(String                    serverName,
                                                String                    glossaryGUID,
                                                MetadataSourceRequestBody requestBody)
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

            CollectionHandler handler = instanceHandler.getGlossaryHandler(userId, serverName, methodName);

            handler.clearGlossaryAsTaxonomy(userId, glossaryGUID, requestBody);
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
     * @param requestBody description of the situations where this glossary is relevant.
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setGlossaryAsCanonical(String                    serverName,
                                               String                    glossaryGUID,
                                               NewClassificationRequestBody requestBody)
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

            CollectionHandler handler = instanceHandler.getGlossaryHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CanonicalVocabularyProperties properties)
                {
                    handler.setGlossaryAsCanonical(userId, glossaryGUID, properties, requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setGlossaryAsCanonical(userId, glossaryGUID, null, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CanonicalVocabularyProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.setGlossaryAsCanonical(userId, glossaryGUID, null, null);
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
     * @param requestBody correlation properties for the external asset manager
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearGlossaryAsCanonical(String                    serverName,
                                                 String                    glossaryGUID,
                                                 MetadataSourceRequestBody requestBody)
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

            CollectionHandler handler = instanceHandler.getGlossaryHandler(userId, serverName, methodName);

            handler.clearGlossaryAsCanonical(userId, glossaryGUID, requestBody);
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

            response.setStatuses(Arrays.asList(ElementStatus.DRAFT,
                                               ElementStatus.PREPARED,
                                               ElementStatus.PROPOSED,
                                               ElementStatus.APPROVED,
                                               ElementStatus.REJECTED,
                                               ElementStatus.ACTIVE,
                                               ElementStatus.DEPRECATED,
                                               ElementStatus.OTHER));
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

            response.setStatuses(Arrays.asList(TermRelationshipStatus.values()));
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

            response.setTypes(Arrays.asList(ActivityType.values()));
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
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryTerm(String              serverName,
                                           NewElementRequestBody requestBody)
    {
        final String methodName = "createGlossaryTerm";

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
                GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof GlossaryTermProperties glossaryTermProperties)
                {
                    response.setGUID(handler.createGlossaryTerm(userId,
                                                                requestBody,
                                                                requestBody.getInitialClassifications(),
                                                                glossaryTermProperties,
                                                                requestBody.getParentRelationshipProperties()));
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
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGlossaryTermFromTemplate(String              serverName,
                                                       String              templateGUID,
                                                       TemplateRequestBody requestBody)
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
                GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

                response.setGUID(handler.createGlossaryTermFromTemplate(userId,
                                                                        requestBody,
                                                                        templateGUID,
                                                                        requestBody.getReplacementProperties(),
                                                                        requestBody.getPlaceholderPropertyValues(),
                                                                        requestBody.getParentRelationshipProperties()));
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
     * @param requestBody new properties for the glossary term
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryTerm(String                         serverName,
                                           String                    glossaryTermGUID,
                                           UpdateElementRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof GlossaryTermProperties properties)
                {
                    GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

                    handler.updateGlossaryTerm(userId, glossaryTermGUID, requestBody, properties);
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
     * Update the glossary term using the properties and classifications from the parentGUID stored in the request body.
     *
     * @param serverName name of the server to route the request to
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param templateGUID template to use
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGlossaryTermFromTemplate(String                        serverName,
                                                       String                        glossaryTermGUID,
                                                       String                        templateGUID,
                                                       UpdateWithTemplateRequestBody requestBody)
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
                GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

                handler.updateGlossaryTermFromTemplate(userId, glossaryTermGUID, templateGUID, requestBody);
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
     * @param requestBody status and correlation properties
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse moveGlossaryTerm(String                        serverName,
                                         String                        glossaryTermGUID,
                                         String                        glossaryGUID,
                                         DeleteRelationshipRequestBody requestBody)
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
                GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

                handler.moveGlossaryTerm(userId, glossaryTermGUID, glossaryGUID, requestBody);
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
            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

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
     * Link two terms together using a specialist relationship.
     *
     * @param serverName name of the server to route the request to
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
    public VoidResponse setupTermRelationship(String                  serverName,
                                              String                  glossaryTermOneGUID,
                                              String                  relationshipTypeName,
                                              String                  glossaryTermTwoGUID,
                                              NewRelationshipRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof GlossaryTermRelationshipProperties properties)
                {
                    GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

                    handler.setupTermRelationship(userId,
                                                  relationshipTypeName,
                                                  glossaryTermOneGUID,
                                                  glossaryTermTwoGUID,
                                                  requestBody,
                                                  properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryTermRelationshipProperties.class.getName(), methodName);
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
     * @param requestBody properties for the relationship
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateTermRelationship(String                        serverName,
                                               String                        glossaryTermOneGUID,
                                               String                        relationshipTypeName,
                                               String                        glossaryTermTwoGUID,
                                               UpdateRelationshipRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof GlossaryTermRelationshipProperties properties)
                {
                    GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

                    handler.updateTermRelationship(userId,
                                                   relationshipTypeName,
                                                   glossaryTermOneGUID,
                                                   glossaryTermTwoGUID,
                                                   requestBody,
                                                   properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GlossaryTermRelationshipProperties.class.getName(), methodName);
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
                                              DeleteRelationshipRequestBody requestBody)
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

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            handler.clearTermRelationship(userId,
                                          relationshipTypeName,
                                          glossaryTermOneGUID,
                                          glossaryTermTwoGUID,
                                          requestBody);
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
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsAbstractConcept(String                    serverName,
                                                 String                    glossaryTermGUID,
                                                 NewClassificationRequestBody requestBody)
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

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AbstractConceptProperties properties)
                {
                    handler.setTermAsAbstractConcept(userId, glossaryTermGUID, properties, requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setTermAsAbstractConcept(userId, glossaryTermGUID, null, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AbstractConceptProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.setTermAsAbstractConcept(userId, glossaryTermGUID, null, null);
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
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsAbstractConcept(String                    serverName,
                                                   String                    glossaryTermGUID,
                                                   MetadataSourceRequestBody requestBody)
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

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            handler.clearTermAsAbstractConcept(userId, glossaryTermGUID, requestBody);
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
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsDataValue(String                    serverName,
                                           String                    glossaryTermGUID,
                                           NewClassificationRequestBody requestBody)
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

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataValueProperties properties)
                {
                    handler.setTermAsDataValue(userId, glossaryTermGUID, properties, requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setTermAsDataValue(userId, glossaryTermGUID, null, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataValueProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.setTermAsDataValue(userId, glossaryTermGUID, null, null);
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
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsDataValue(String                    serverName,
                                             String                    glossaryTermGUID,
                                             MetadataSourceRequestBody requestBody)
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

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            handler.clearTermAsDataValue(userId, glossaryTermGUID, requestBody);
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
     * @param requestBody type of activity and correlators
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsActivity(String                    serverName,
                                          String                    glossaryTermGUID,
                                          NewClassificationRequestBody requestBody)
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

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ActivityDescriptionProperties properties)
                {
                    handler.setTermAsActivity(userId, glossaryTermGUID, properties, requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setTermAsActivity(userId, glossaryTermGUID, null, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ActivityDescriptionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.setTermAsActivity(userId, glossaryTermGUID, null, null);
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
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsActivity(String                    serverName,
                                            String                    glossaryTermGUID,
                                            MetadataSourceRequestBody requestBody)
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

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            handler.clearTermAsActivity(userId, glossaryTermGUID, requestBody);
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
     * @param requestBody more details of the context
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setTermAsContext(String                    serverName,
                                         String                    glossaryTermGUID,
                                         NewClassificationRequestBody requestBody)
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

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ContextDefinitionProperties properties)
                {
                    handler.setTermAsContext(userId, glossaryTermGUID, properties, requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setTermAsContext(userId, glossaryTermGUID, null, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ContextDefinitionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.setTermAsContext(userId, glossaryTermGUID, null, null);
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
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearTermAsContext(String                    serverName,
                                           String                    glossaryTermGUID,
                                           MetadataSourceRequestBody requestBody)
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

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            handler.clearTermAsContext(userId, glossaryTermGUID, requestBody);
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
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse deleteGlossaryTerm(String                   serverName,
                                           String                   glossaryTermGUID,
                                           DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteGlossaryTerm";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            handler.deleteGlossaryTerm(userId, glossaryTermGUID, requestBody);
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
     * @param requestBody asset manager identifiers and search string
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findGlossaryTerms(String                  serverName,
                                                              SearchStringRequestBody requestBody)
    {
        final String methodName = "findGlossaryTerms";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findGlossaryTerms(userId,
                                                               requestBody.getSearchString(),
                                                               requestBody));
            }
            else
            {
                response.setElements(handler.findGlossaryTerms(userId,
                                                               null,
                                                               null));
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
     * @param requestBody asset manager identifiers and name
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse   getGlossaryTermsByName(String            serverName,
                                                                     FilterRequestBody requestBody)
    {
        final String methodName = "getGlossaryTermsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

                response.setElements(handler.getGlossaryTermsByName(userId, requestBody.getFilter(), requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, FilterRequestBody.class.getName());
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
     * @param guid unique identifier of the requested metadata element
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getGlossaryTermByGUID(String             serverName,
                                                                 String             guid,
                                                                 GetRequestBody requestBody)
    {
        final String methodName = "getGlossaryTermByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GlossaryTermHandler handler = instanceHandler.getGlossaryTermHandler(userId, serverName, methodName);

            response.setElement(handler.getGlossaryTermByGUID(userId, guid, requestBody));

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

}
