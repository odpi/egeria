/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.GlossaryCategoryConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.GlossaryConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.GlossaryTermConverter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalGlossaryLinkElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ArchiveProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.CanonicalVocabularyProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.DataFieldValuesProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.EditingGlossaryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalGlossaryElementLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalGlossaryLinkProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryCategoryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermActivityType;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermCategorization;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermContextDefinition;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermRelationship;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermRelationshipStatus;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermStatus;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.StagingGlossaryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TaxonomyProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TemplateProperties;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GlossaryCategoryHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GlossaryHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GlossaryTermHandler;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * GlossaryExchangeHandler is the server side handler for managing glossary content.
 */
public class GlossaryExchangeHandler extends ExchangeHandlerBase
{
    private final GlossaryHandler<GlossaryElement>                 glossaryHandler;
    private final GlossaryCategoryHandler<GlossaryCategoryElement> glossaryCategoryHandler;
    private final GlossaryTermHandler<GlossaryTermElement>         glossaryTermHandler;

    private final static String glossaryGUIDParameterName          = "glossaryGUID";
    private final static String glossaryCategoryGUIDParameterName  = "glossaryCategoryGUID";
    private final static String glossaryTermGUIDParameterName      = "glossaryTermGUID";

    /**
     * Construct the glossary exchange handler with information needed to work with glossary related objects
     * for Asset Manager OMAS.
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve instances from.
     * @param defaultZones list of zones that the access service should set in all new instances.
     * @param publishZones list of zones that the access service sets up in published instances.
     * @param auditLog destination for audit log events.
     */
    public GlossaryExchangeHandler(String                             serviceName,
                                   String                             serverName,
                                   InvalidParameterHandler            invalidParameterHandler,
                                   RepositoryHandler                  repositoryHandler,
                                   OMRSRepositoryHelper               repositoryHelper,
                                   String                             localServerUserId,
                                   OpenMetadataServerSecurityVerifier securityVerifier,
                                   List<String>                       supportedZones,
                                   List<String>                       defaultZones,
                                   List<String>                       publishZones,
                                   AuditLog                           auditLog)
    {
        super(serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        glossaryHandler = new GlossaryHandler<>(new GlossaryConverter<>(repositoryHelper, serviceName, serverName),
                                                GlossaryElement.class,
                                                serviceName,
                                                serverName,
                                                invalidParameterHandler,
                                                repositoryHandler,
                                                repositoryHelper,
                                                localServerUserId,
                                                securityVerifier,
                                                supportedZones,
                                                defaultZones,
                                                publishZones,
                                                auditLog);

        glossaryCategoryHandler = new GlossaryCategoryHandler<>(new GlossaryCategoryConverter<>(repositoryHelper, serviceName, serverName),
                                                                GlossaryCategoryElement.class,
                                                                serviceName,
                                                                serverName,
                                                                invalidParameterHandler,
                                                                repositoryHandler,
                                                                repositoryHelper,
                                                                localServerUserId,
                                                                securityVerifier,
                                                                supportedZones,
                                                                defaultZones,
                                                                publishZones,
                                                                auditLog);

        glossaryTermHandler = new GlossaryTermHandler<>(new GlossaryTermConverter<>(repositoryHelper, serviceName, serverName),
                                                        GlossaryTermElement.class,
                                                        serviceName,
                                                        serverName,
                                                        invalidParameterHandler,
                                                        repositoryHandler,
                                                        repositoryHelper,
                                                        localServerUserId,
                                                        securityVerifier,
                                                        supportedZones,
                                                        defaultZones,
                                                        publishZones,
                                                        auditLog);
    }



    /* ========================================================
     * Managing the externalIds and related correlation properties.
     */


    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToGlossaries(String                userId,
                                                      String                assetManagerGUID,
                                                      String                assetManagerName,
                                                      List<GlossaryElement> results,
                                                      String                methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        if (results != null)
        {
            for (MetadataElement glossary : results)
            {
                if ((glossary != null) && (glossary.getElementHeader() != null) && (glossary.getElementHeader().getGUID() != null))
                {
                    glossary.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                 glossary.getElementHeader().getGUID(),
                                                                                 glossaryGUIDParameterName,
                                                                                 OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                                 assetManagerGUID,
                                                                                 assetManagerName,
                                                                                 false,
                                                                                 false,
                                                                                 null,
                                                                                 methodName));
                }
            }
        }
    }


    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToGlossaryCategories(String                        userId,
                                                              String                        assetManagerGUID,
                                                              String                        assetManagerName,
                                                              List<GlossaryCategoryElement> results,
                                                              boolean                       forLineage,
                                                              boolean                       forDuplicateProcessing,
                                                              Date                          effectiveTime,
                                                              String                        methodName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        if (results != null)
        {
            for (MetadataElement glossaryCategory : results)
            {
                if ((glossaryCategory != null) && (glossaryCategory.getElementHeader() != null) && (glossaryCategory.getElementHeader().getGUID() != null))
                {
                    glossaryCategory.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                         glossaryCategory.getElementHeader().getGUID(),
                                                                                         glossaryCategoryGUIDParameterName,
                                                                                         OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                                                         assetManagerGUID,
                                                                                         assetManagerName,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         effectiveTime,
                                                                                         methodName));
                }
            }
        }
    }




    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToGlossaryTerms(String                    userId,
                                                         String                    assetManagerGUID,
                                                         String                    assetManagerName,
                                                         List<GlossaryTermElement> results,
                                                         boolean                   forLineage,
                                                         boolean                   forDuplicateProcessing,
                                                         Date                      effectiveTime,
                                                         String                    methodName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        if (results != null)
        {
            for (MetadataElement glossaryTerm : results)
            {
                if ((glossaryTerm != null) && (glossaryTerm.getElementHeader() != null) && (glossaryTerm.getElementHeader().getGUID() != null))
                {
                    glossaryTerm.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                     glossaryTerm.getElementHeader().getGUID(),
                                                                                     glossaryTermGUIDParameterName,
                                                                                     OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                                     assetManagerGUID,
                                                                                     assetManagerName,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     effectiveTime,
                                                                                     methodName));
                }
            }
        }
    }


    /* ========================================================
     * The Glossary entity is the top level element in a glossary.
     */


    /**
     * Create a new metadata element to represent the root of a glossary.  All categories and terms are linked
     * to a single glossary.  They are owned by this glossary and if the glossary is deleted, any linked terms and
     * categories are deleted as well.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param glossaryProperties properties to store
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossary(String                        userId,
                                 MetadataCorrelationProperties correlationProperties,
                                 boolean                       assetManagerIsHome,
                                 GlossaryProperties            glossaryProperties,
                                 String                        methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String propertiesParameterName    = "glossaryProperties";
        final String qualifiedNameParameterName = "glossaryProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);

        invalidParameterHandler.validateObject(glossaryProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String glossaryGUID = glossaryHandler.createGlossary(userId,
                                                             getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                             getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                             glossaryProperties.getQualifiedName(),
                                                             glossaryProperties.getDisplayName(),
                                                             glossaryProperties.getDescription(),
                                                             glossaryProperties.getLanguage(),
                                                             glossaryProperties.getUsage(),
                                                             glossaryProperties.getAdditionalProperties(),
                                                             glossaryProperties.getTypeName(),
                                                             glossaryProperties.getExtendedProperties(),
                                                             methodName);

        if (glossaryGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          glossaryGUID,
                                          glossaryGUIDParameterName,
                                          OpenMetadataType.GLOSSARY_TYPE_NAME,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return glossaryGUID;
    }


    /**
     * Create a new metadata element to represent a glossary using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms and categories are deleted as well.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryFromTemplate(String                        userId,
                                             MetadataCorrelationProperties correlationProperties,
                                             boolean                       assetManagerIsHome,
                                             String                        templateGUID,
                                             TemplateProperties            templateProperties,
                                             boolean                       deepCopy,
                                             String                        methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);

        String glossaryGUID = glossaryHandler.createGlossaryFromTemplate(userId,
                                                                         this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                         this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                         templateGUID,
                                                                         templateProperties.getQualifiedName(),
                                                                         templateProperties.getDisplayName(),
                                                                         templateProperties.getDescription(),
                                                                         deepCopy,
                                                                         methodName);
        if (glossaryGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          glossaryGUID,
                                          glossaryGUIDParameterName,
                                          OpenMetadataType.GLOSSARY_TYPE_NAME,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return glossaryGUID;
    }


    /**
     * Update the metadata element representing a glossary.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryGUID unique identifier of the metadata element to update
     * @param glossaryProperties new properties for this element
     * @param updateDescription description of the update for the revision history
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossary(String                        userId,
                               MetadataCorrelationProperties correlationProperties,
                               String                        glossaryGUID,
                               GlossaryProperties            glossaryProperties,
                               String                        updateDescription,
                               boolean                       isMergeUpdate,
                               boolean                       forLineage,
                               boolean                       forDuplicateProcessing,
                               Date                          effectiveTime,
                               String                        methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String propertiesParameterName    = "glossaryProperties";
        final String qualifiedNameParameterName = "glossaryProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryHandler.updateGlossary(userId,
                                       getExternalSourceGUID(correlationProperties),
                                       getExternalSourceName(correlationProperties),
                                       glossaryGUID,
                                       glossaryGUIDParameterName,
                                       glossaryProperties.getQualifiedName(),
                                       glossaryProperties.getDisplayName(),
                                       glossaryProperties.getDescription(),
                                       glossaryProperties.getLanguage(),
                                       glossaryProperties.getUsage(),
                                       glossaryProperties.getAdditionalProperties(),
                                       glossaryProperties.getTypeName(),
                                       glossaryProperties.getExtendedProperties(),
                                       isMergeUpdate,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);

        String revisionHistoryTitle = "Glossary properties updated by " + userId + " on " + new Date();

        this.updateRevisionHistory(userId,
                                   glossaryGUID,
                                   glossaryProperties.getQualifiedName(),
                                   OpenMetadataType.GLOSSARY_TYPE_NAME,
                                   revisionHistoryTitle,
                                   updateDescription);
    }


    /**
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories and terms because
     * the Anchors classifications are set up in these elements.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossary(String                        userId,
                               MetadataCorrelationProperties correlationProperties,
                               String                        glossaryGUID,
                               boolean                       forLineage,
                               boolean                       forDuplicateProcessing,
                               Date                          effectiveTime,
                               String                        methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryHandler.removeGlossary(userId,
                                       getExternalSourceGUID(correlationProperties),
                                       getExternalSourceName(correlationProperties),
                                       glossaryGUID,
                                       glossaryGUIDParameterName,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Classify the glossary to indicate that it is an editing glossary - this means it is
     * a collection of glossary updates that will be merged into its source glossary.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param properties description of how the glossary is organized
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setGlossaryAsEditingGlossary(String                        userId,
                                             MetadataCorrelationProperties correlationProperties,
                                             String                        glossaryGUID,
                                             EditingGlossaryProperties     properties,
                                             boolean                       forLineage,
                                             boolean                       forDuplicateProcessing,
                                             Date                          effectiveTime,
                                             String                        methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        if (properties != null)
        {
            glossaryHandler.addEditingGlossaryClassificationToGlossary(userId,
                                                                       getExternalSourceGUID(correlationProperties),
                                                                       getExternalSourceName(correlationProperties),
                                                                       glossaryGUID,
                                                                       glossaryGUIDParameterName,
                                                                       properties.getDescription(),
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);
        }
        else
        {
            glossaryHandler.addEditingGlossaryClassificationToGlossary(userId,
                                                                       getExternalSourceGUID(correlationProperties),
                                                                       getExternalSourceName(correlationProperties),
                                                                       glossaryGUID,
                                                                       glossaryGUIDParameterName,
                                                                       null,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);
        }
    }


    /**
     * Remove the editing glossary designation from the glossary.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearGlossaryAsEditingGlossary(String                        userId,
                                               MetadataCorrelationProperties correlationProperties,
                                               String                        glossaryGUID,
                                               boolean                       forLineage,
                                               boolean                       forDuplicateProcessing,
                                               Date                          effectiveTime,
                                               String                        methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryHandler.removeEditingGlossaryClassificationFromGlossary(userId,
                                                                        getExternalSourceGUID(correlationProperties),
                                                                        getExternalSourceName(correlationProperties),
                                                                        glossaryGUID,
                                                                        glossaryGUIDParameterName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);
    }

    /**
     * Classify the glossary to indicate that it is an editing glossary - this means it is
     * a collection of glossary updates that will be merged into another glossary.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param properties description of how the glossary is organized
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setGlossaryAsStagingGlossary(String                        userId,
                                             MetadataCorrelationProperties correlationProperties,
                                             String                        glossaryGUID,
                                             StagingGlossaryProperties     properties,
                                             boolean                       forLineage,
                                             boolean                       forDuplicateProcessing,
                                             Date                          effectiveTime,
                                             String                        methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        if (properties != null)
        {
            glossaryHandler.addStagingGlossaryClassificationToGlossary(userId,
                                                                       getExternalSourceGUID(correlationProperties),
                                                                       getExternalSourceName(correlationProperties),
                                                                       glossaryGUID,
                                                                       glossaryGUIDParameterName,
                                                                       properties.getDescription(),
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);
        }
        else
        {
            glossaryHandler.addStagingGlossaryClassificationToGlossary(userId,
                                                                       getExternalSourceGUID(correlationProperties),
                                                                       getExternalSourceName(correlationProperties),
                                                                       glossaryGUID,
                                                                       glossaryGUIDParameterName,
                                                                       null,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);
        }
    }


    /**
     * Remove the staging glossary designation from the glossary.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearGlossaryAsStagingGlossary(String                        userId,
                                               MetadataCorrelationProperties correlationProperties,
                                               String                        glossaryGUID,
                                               boolean                       forLineage,
                                               boolean                       forDuplicateProcessing,
                                               Date                          effectiveTime,
                                               String                        methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryHandler.removeStagingGlossaryClassificationFromGlossary(userId,
                                                                        getExternalSourceGUID(correlationProperties),
                                                                        getExternalSourceName(correlationProperties),
                                                                        glossaryGUID,
                                                                        glossaryGUIDParameterName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param properties description of how the glossary is organized
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setGlossaryAsTaxonomy(String                        userId,
                                      MetadataCorrelationProperties correlationProperties,
                                      String                        glossaryGUID,
                                      TaxonomyProperties            properties,
                                      boolean                       forLineage,
                                      boolean                       forDuplicateProcessing,
                                      Date                          effectiveTime,
                                      String                        methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        if (properties != null)
        {
            glossaryHandler.addTaxonomyClassificationToGlossary(userId,
                                                                getExternalSourceGUID(correlationProperties),
                                                                getExternalSourceName(correlationProperties),
                                                                glossaryGUID,
                                                                glossaryGUIDParameterName,
                                                                properties.getOrganizingPrinciple(),
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime,
                                                                methodName);
        }
        else
        {
            glossaryHandler.addTaxonomyClassificationToGlossary(userId,
                                                                getExternalSourceGUID(correlationProperties),
                                                                getExternalSourceName(correlationProperties),
                                                                glossaryGUID,
                                                                glossaryGUIDParameterName,
                                                                null,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime,
                                                                methodName);
        }
    }


    /**
     * Remove the taxonomy designation from the glossary.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearGlossaryAsTaxonomy(String                        userId,
                                        MetadataCorrelationProperties correlationProperties,
                                        String                        glossaryGUID,
                                        boolean                       forLineage,
                                        boolean                       forDuplicateProcessing,
                                        Date                          effectiveTime,
                                        String                        methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryHandler.removeTaxonomyClassificationFromGlossary(userId,
                                                                 getExternalSourceGUID(correlationProperties),
                                                                 getExternalSourceName(correlationProperties),
                                                                 glossaryGUID,
                                                                 glossaryGUIDParameterName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);
    }


    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param properties description of the situations where this glossary is relevant.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setGlossaryAsCanonical(String                        userId,
                                       MetadataCorrelationProperties correlationProperties,
                                       String                        glossaryGUID,
                                       CanonicalVocabularyProperties properties,
                                       boolean                       forLineage,
                                       boolean                       forDuplicateProcessing,
                                       Date                          effectiveTime,
                                       String                        methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        if (properties != null)
        {
            glossaryHandler.addCanonicalVocabClassificationToGlossary(userId,
                                                                      getExternalSourceGUID(correlationProperties),
                                                                      getExternalSourceName(correlationProperties),
                                                                      glossaryGUID,
                                                                      glossaryGUIDParameterName,
                                                                      properties.getScope(),
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);
        }
        else
        {
            glossaryHandler.addCanonicalVocabClassificationToGlossary(userId,
                                                                      getExternalSourceGUID(correlationProperties),
                                                                      getExternalSourceName(correlationProperties),
                                                                      glossaryGUID,
                                                                      glossaryGUIDParameterName,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);
        }

    }


    /**
     * Remove the canonical designation from the glossary.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearGlossaryAsCanonical(String                        userId,
                                         MetadataCorrelationProperties correlationProperties,
                                         String                        glossaryGUID,
                                         boolean                       forLineage,
                                         boolean                       forDuplicateProcessing,
                                         Date                          effectiveTime,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryHandler.removeCanonicalVocabClassificationFromGlossary(userId,
                                                                       getExternalSourceGUID(correlationProperties),
                                                                       getExternalSourceName(correlationProperties),
                                                                       glossaryGUID,
                                                                       glossaryGUIDParameterName,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);
    }


    /**
     * Retrieve the list of glossary metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter for search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement> findGlossaries(String  userId,
                                                String  assetManagerGUID,
                                                String  assetManagerName,
                                                String  searchString,
                                                String  searchStringParameterName,
                                                int     startFrom,
                                                int     pageSize,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        List<GlossaryElement> results = glossaryHandler.findGlossaries(userId,
                                                                       searchString,
                                                                       searchStringParameterName,
                                                                       startFrom,
                                                                       pageSize,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);

        addCorrelationPropertiesToGlossaries(userId, assetManagerGUID, assetManagerName, results , methodName);

        return results;
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param nameParameterName name of parameter supplying name value
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement>   getGlossariesByName(String  userId,
                                                       String  assetManagerGUID,
                                                       String  assetManagerName,
                                                       String  name,
                                                       String  nameParameterName,
                                                       int     startFrom,
                                                       int     pageSize,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime,
                                                       String  methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        List<GlossaryElement> results = glossaryHandler.getGlossariesByName(userId,
                                                                            name,
                                                                            nameParameterName,
                                                                            startFrom,
                                                                            pageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveTime,
                                                                            methodName);

        addCorrelationPropertiesToGlossaries(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /**
     * Retrieve the list of glossaries created by this caller.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement>   getGlossariesForAssetManager(String  userId,
                                                                String  assetManagerGUID,
                                                                String  assetManagerName,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime,
                                                                String  methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String assetManagerGUIDParameterName = "assetManagerGUID";
        final String glossaryEntityParameterName = "glossaryEntity";
        final String glossaryGUIDParameterName = "glossaryEntity.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);

        List<GlossaryElement> results = new ArrayList<>();

        List<EntityDetail> glossaryEntities = externalIdentifierHandler.getElementEntitiesForScope(userId,
                                                                                                   assetManagerGUID,
                                                                                                   assetManagerGUIDParameterName,
                                                                                                   OpenMetadataType.SOFTWARE_CAPABILITY_TYPE_NAME,
                                                                                                   OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                                                   startFrom,
                                                                                                   pageSize,
                                                                                                   effectiveTime,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   methodName);

        if (glossaryEntities != null)
        {
            for (EntityDetail glossaryEntity : glossaryEntities)
            {
                if (glossaryEntity != null)
                {
                    GlossaryElement glossaryElement = glossaryHandler.getBeanFromEntity(userId,
                                                                                        glossaryEntity,
                                                                                        glossaryEntityParameterName,
                                                                                        methodName);

                    if (glossaryElement != null)
                    {
                        glossaryElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                            glossaryEntity.getGUID(),
                                                                                            glossaryGUIDParameterName,
                                                                                            OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                                            assetManagerGUID,
                                                                                            assetManagerName,
                                                                                            forLineage,
                                                                                            forDuplicateProcessing,
                                                                                            effectiveTime,
                                                                                            methodName));

                        results.add(glossaryElement);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElement getGlossaryByGUID(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  glossaryGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String guidParameterName  = "glossaryGUID";

        GlossaryElement glossary = glossaryHandler.getGlossaryByGUID(userId,
                                                                     glossaryGUID,
                                                                     guidParameterName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

        if (glossary != null)
        {
            glossary.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                         glossaryGUID,
                                                                         guidParameterName,
                                                                         OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                         assetManagerGUID,
                                                                         assetManagerName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName));
        }

        return glossary;
    }



    /**
     * Retrieve the glossary metadata element for the requested category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElement getGlossaryForCategory(String  userId,
                                                  String  assetManagerGUID,
                                                  String  assetManagerName,
                                                  String  glossaryCategoryGUID,
                                                  boolean forLineage,
                                                  boolean forDuplicateProcessing,
                                                  Date    effectiveTime,
                                                  String  methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String guidParameterName  = "glossaryCategoryGUID";

        GlossaryElement glossary = glossaryHandler.getGlossaryForCategory(userId,
                                                                          glossaryCategoryGUID,
                                                                          guidParameterName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

        if (glossary != null)
        {
            final String glossaryGUIDParameterName  = "glossary.getElementHeader().getGUID()";

            glossary.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                         glossary.getElementHeader().getGUID(),
                                                                         glossaryGUIDParameterName,
                                                                         OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                         assetManagerGUID,
                                                                         assetManagerName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName));
        }

        return glossary;
    }


    /**
     * Retrieve the glossary metadata element for the requested term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElement getGlossaryForTerm(String  userId,
                                              String  assetManagerGUID,
                                              String  assetManagerName,
                                              String  glossaryTermGUID,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String guidParameterName  = "glossaryTermGUID";

        GlossaryElement glossary = glossaryHandler.getGlossaryForTerm(userId,
                                                                      glossaryTermGUID,
                                                                      guidParameterName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

        if (glossary != null)
        {
            final String glossaryGUIDParameterName  = "glossary.getElementHeader().getGUID()";

            glossary.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                         glossary.getElementHeader().getGUID(),
                                                                         glossaryGUIDParameterName,
                                                                         OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                         assetManagerGUID,
                                                                         assetManagerName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName));
        }

        return glossary;
    }


    /* =====================================================================================================================
     * A glossary may host one or more glossary categories depending on its capability
     */

    /**
     * Create a new metadata element to represent a glossary category.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param glossaryCategoryProperties properties about the glossary category to store
     * @param isRootCategory is this category a root category?
     * @param updateDescription description of the update for the revision history
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return unique identifier of the new glossary category
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryCategory(String                        userId,
                                         String                        glossaryGUID,
                                         MetadataCorrelationProperties correlationProperties,
                                         boolean                       assetManagerIsHome,
                                         GlossaryCategoryProperties    glossaryCategoryProperties,
                                         boolean                       isRootCategory,
                                         String                        updateDescription,
                                         boolean                       forLineage,
                                         boolean                       forDuplicateProcessing,
                                         Date                          effectiveTime,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String propertiesParameterName           = "glossaryCategoryProperties";
        final String qualifiedNameParameterName        = "glossaryCategoryProperties.qualifiedName";

        invalidParameterHandler.validateObject(glossaryCategoryProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryCategoryProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String glossaryCategoryGUID = glossaryCategoryHandler.createGlossaryCategory(userId,
                                                                                     getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                                     getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                                     glossaryGUID,
                                                                                     glossaryGUIDParameterName,
                                                                                     glossaryCategoryProperties.getQualifiedName(),
                                                                                     glossaryCategoryProperties.getDisplayName(),
                                                                                     glossaryCategoryProperties.getDescription(),
                                                                                     glossaryCategoryProperties.getAdditionalProperties(),
                                                                                     isRootCategory,
                                                                                     glossaryCategoryProperties.getTypeName(),
                                                                                     glossaryCategoryProperties.getExtendedProperties(),
                                                                                     glossaryCategoryProperties.getEffectiveFrom(),
                                                                                     glossaryCategoryProperties.getEffectiveTo(),
                                                                                     effectiveTime,
                                                                                     methodName);

        if (glossaryCategoryGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          glossaryCategoryGUID,
                                          glossaryCategoryGUIDParameterName,
                                          OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);

            String revisionHistoryTitle = "Glossary category created by " + userId + " on " + new Date();

            this.updateRevisionHistory(userId,
                                       glossaryCategoryGUID,
                                       glossaryCategoryProperties.getQualifiedName(),
                                       OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                       revisionHistoryTitle,
                                       updateDescription);
        }



        return glossaryCategoryGUID;
    }


    /**
     * Create a new metadata element to represent a glossary category using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome  ensure that only the asset manager can update this element
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param methodName calling method
     *
     * @return unique identifier of the new glossary category
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryCategoryFromTemplate(String                        userId,
                                                     MetadataCorrelationProperties correlationProperties,
                                                     boolean                       assetManagerIsHome,
                                                     String                        glossaryGUID,
                                                     String                        templateGUID,
                                                     TemplateProperties            templateProperties,
                                                     boolean                       deepCopy,
                                                     String                        methodName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String glossaryGUIDParameterName = "glossaryGUID";
        final String templateGUIDParameterName         = "templateGUID";
        final String propertiesParameterName           = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);

        String glossaryCategoryGUID = glossaryCategoryHandler.createGlossaryCategoryFromTemplate(userId,
                                                                                                 getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                                                 getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                                                 glossaryGUID,
                                                                                                 glossaryGUIDParameterName,
                                                                                                 templateGUID,
                                                                                                 templateProperties.getQualifiedName(),
                                                                                                 templateProperties.getDisplayName(),
                                                                                                 templateProperties.getDescription(),
                                                                                                 deepCopy,
                                                                                                 methodName);
        if (glossaryCategoryGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          glossaryCategoryGUID,
                                          glossaryCategoryGUIDParameterName,
                                          OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return glossaryCategoryGUID;
    }


    /**
     * Update the metadata element representing a glossary category.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryCategoryGUID unique identifier of the metadata element to update
     * @param glossaryCategoryProperties new properties for the metadata element
     * @param updateDescription description of the update for the revision history
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryCategory(String                        userId,
                                       MetadataCorrelationProperties correlationProperties,
                                       String                        glossaryCategoryGUID,
                                       GlossaryCategoryProperties    glossaryCategoryProperties,
                                       String                        updateDescription,
                                       boolean                       isMergeUpdate,
                                       boolean                       forLineage,
                                       boolean                       forDuplicateProcessing,
                                       Date                          effectiveTime,
                                       String                        methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String propertiesParameterName    = "glossaryCategoryProperties";
        final String qualifiedNameParameterName = "glossaryCategoryProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryCategoryGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryCategoryProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryCategoryProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryCategoryGUID,
                                        glossaryCategoryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryCategoryHandler.updateGlossaryCategory(userId,
                                                       getExternalSourceGUID(correlationProperties),
                                                       getExternalSourceName(correlationProperties),
                                                       glossaryCategoryGUID,
                                                       glossaryCategoryGUIDParameterName,
                                                       glossaryCategoryProperties.getQualifiedName(),
                                                       glossaryCategoryProperties.getDisplayName(),
                                                       glossaryCategoryProperties.getDescription(),
                                                       glossaryCategoryProperties.getAdditionalProperties(),
                                                       glossaryCategoryProperties.getTypeName(),
                                                       glossaryCategoryProperties.getExtendedProperties(),
                                                       glossaryCategoryProperties.getEffectiveFrom(),
                                                       glossaryCategoryProperties.getEffectiveTo(),
                                                       effectiveTime,
                                                       isMergeUpdate,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       methodName);

        String revisionHistoryTitle = "Glossary category updated by " + userId + " on " + new Date();

        this.updateRevisionHistory(userId,
                                   glossaryCategoryGUID,
                                   glossaryCategoryProperties.getQualifiedName(),
                                   OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                   revisionHistoryTitle,
                                   updateDescription);

    }


    /**
     * Create a parent-child relationship between two categories.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupCategoryParent(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  glossaryParentCategoryGUID,
                                    String  glossaryChildCategoryGUID,
                                    Date    effectiveFrom,
                                    Date    effectiveTo,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String glossaryParentCategoryGUIDParameterName = "glossaryParentCategoryGUID";
        final String glossaryChildCategoryGUIDParameterName = "glossaryChildCategoryGUID";

        glossaryCategoryHandler.setupCategoryParent(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    glossaryParentCategoryGUID,
                                                    glossaryParentCategoryGUIDParameterName,
                                                    glossaryChildCategoryGUID,
                                                    glossaryChildCategoryGUIDParameterName,
                                                    effectiveFrom,
                                                    effectiveTo,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    methodName);

        externalIdentifierHandler.logRelationshipCreation(assetManagerGUID,
                                                          assetManagerName,
                                                          OpenMetadataType.CATEGORY_HIERARCHY_TYPE_NAME,
                                                          glossaryParentCategoryGUID,
                                                          OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                          glossaryChildCategoryGUID,
                                                          OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                          methodName);
    }


    /**
     * Remove a parent-child relationship between two categories.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearCategoryParent(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  glossaryParentCategoryGUID,
                                    String  glossaryChildCategoryGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String glossaryParentCategoryGUIDParameterName = "glossaryParentCategoryGUID";
        final String glossaryChildCategoryGUIDParameterName = "glossaryChildCategoryGUID";

        glossaryCategoryHandler.clearCategoryParent(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    glossaryParentCategoryGUID,
                                                    glossaryParentCategoryGUIDParameterName,
                                                    glossaryChildCategoryGUID,
                                                    glossaryChildCategoryGUIDParameterName,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    methodName);

        externalIdentifierHandler.logRelationshipRemoval(assetManagerGUID,
                                                         assetManagerName,
                                                         OpenMetadataType.CATEGORY_HIERARCHY_TYPE_NAME,
                                                         glossaryParentCategoryGUID,
                                                         OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                         glossaryChildCategoryGUID,
                                                         OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                         methodName);
    }


    /**
     * Remove the metadata element representing a glossary category.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryCategoryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossaryCategory(String                        userId,
                                       MetadataCorrelationProperties correlationProperties,
                                       String                        glossaryCategoryGUID,
                                       boolean                       forLineage,
                                       boolean                       forDuplicateProcessing,
                                       Date                          effectiveTime,
                                       String                        methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        this.validateExternalIdentifier(userId,
                                        glossaryCategoryGUID,
                                        glossaryCategoryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryCategoryHandler.removeGlossaryCategory(userId,
                                                       getExternalSourceGUID(correlationProperties),
                                                       getExternalSourceName(correlationProperties),
                                                       glossaryCategoryGUID,
                                                       glossaryCategoryGUIDParameterName,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       methodName);
    }


    /**
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary to query
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter for searchString
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   findGlossaryCategories(String  userId,
                                                                  String  assetManagerGUID,
                                                                  String  assetManagerName,
                                                                  String  glossaryGUID,
                                                                  String  searchString,
                                                                  String  searchStringParameterName,
                                                                  int     startFrom,
                                                                  int     pageSize,
                                                                  Date    effectiveTime,
                                                                  boolean forLineage,
                                                                  boolean forDuplicateProcessing,
                                                                  String  methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        List<GlossaryCategoryElement> results = glossaryCategoryHandler.findGlossaryCategories(userId,
                                                                                               glossaryGUID,
                                                                                               searchString,
                                                                                               searchStringParameterName,
                                                                                               startFrom,
                                                                                               pageSize,
                                                                                               effectiveTime,
                                                                                               forLineage,
                                                                                               forDuplicateProcessing,
                                                                                               methodName);

        this.addCorrelationPropertiesToGlossaryCategories(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          results,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);

        return results;
    }


    /**
     * Return the list of categories associated with a glossary.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of metadata elements describing the categories associated with the requested glossary
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getCategoriesForGlossary(String  userId,
                                                                    String  assetManagerGUID,
                                                                    String  assetManagerName,
                                                                    String  glossaryGUID,
                                                                    int     startFrom,
                                                                    int     pageSize,
                                                                    boolean forLineage,
                                                                    boolean forDuplicateProcessing,
                                                                    Date    effectiveTime,
                                                                    String  methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        List<GlossaryCategoryElement> results = glossaryCategoryHandler.getCategoriesForGlossary(userId,
                                                                                                 glossaryGUID,
                                                                                                 glossaryGUIDParameterName,
                                                                                                 startFrom,
                                                                                                 pageSize,
                                                                                                 effectiveTime,
                                                                                                 forLineage,
                                                                                                 forDuplicateProcessing,
                                                                                                 methodName);

        this.addCorrelationPropertiesToGlossaryCategories(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          results,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);

        return results;
    }


    /**
     * Return the list of categories associated with a glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of metadata elements describing the categories associated with the requested term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getCategoriesForTerm(String  userId,
                                                                String  assetManagerGUID,
                                                                String  assetManagerName,
                                                                String  glossaryTermGUID,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime,
                                                                String  methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        List<GlossaryCategoryElement> results = glossaryCategoryHandler.getCategoriesForTerm(userId,
                                                                                             glossaryTermGUID,
                                                                                             glossaryTermGUIDParameterName,
                                                                                             startFrom,
                                                                                             pageSize,
                                                                                             effectiveTime,
                                                                                             forLineage,
                                                                                             forDuplicateProcessing,
                                                                                             methodName);

        this.addCorrelationPropertiesToGlossaryCategories(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          results,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);

        return results;
    }


    /**
     * Retrieve the list of glossary category metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary to query
     * @param name name to search for
     * @param nameParameterName parameter name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getGlossaryCategoriesByName(String  userId,
                                                                       String  assetManagerGUID,
                                                                       String  assetManagerName,
                                                                       String  glossaryGUID,
                                                                       String  name,
                                                                       String  nameParameterName,
                                                                       int     startFrom,
                                                                       int     pageSize,
                                                                       boolean forLineage,
                                                                       boolean forDuplicateProcessing,
                                                                       Date    effectiveTime,
                                                                       String  methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        List<GlossaryCategoryElement> results = glossaryCategoryHandler.getGlossaryCategoriesByName(userId,
                                                                                                    glossaryGUID,
                                                                                                    name,
                                                                                                    nameParameterName,
                                                                                                    startFrom,
                                                                                                    pageSize,
                                                                                                    effectiveTime,
                                                                                                    forLineage,
                                                                                                    forDuplicateProcessing,
                                                                                                    methodName);

        this.addCorrelationPropertiesToGlossaryCategories(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          results,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);

        return results;
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return parent glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElement getGlossaryCategoryParent(String  userId,
                                                             String  assetManagerGUID,
                                                             String  assetManagerName,
                                                             String  glossaryCategoryGUID,
                                                             boolean forLineage,
                                                             boolean forDuplicateProcessing,
                                                             Date    effectiveTime,
                                                             String  methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        GlossaryCategoryElement glossaryCategory = glossaryCategoryHandler.getGlossaryCategoryParent(userId,
                                                                                                     glossaryCategoryGUID,
                                                                                                     glossaryCategoryGUIDParameterName,
                                                                                                     effectiveTime,
                                                                                                     forLineage,
                                                                                                     forDuplicateProcessing,
                                                                                                     methodName);

        if (glossaryCategory != null)
        {
            glossaryCategory.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                 glossaryCategoryGUID,
                                                                                 glossaryCategoryGUIDParameterName,
                                                                                 OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                                                 assetManagerGUID,
                                                                                 assetManagerName,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 effectiveTime,
                                                                                 methodName));
        }

        return glossaryCategory;
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement> getGlossarySubCategories(String  userId,
                                                                  String  assetManagerGUID,
                                                                  String  assetManagerName,
                                                                  String  glossaryCategoryGUID,
                                                                  int     startFrom,
                                                                  int     pageSize,
                                                                  boolean forLineage,
                                                                  boolean forDuplicateProcessing,
                                                                  Date    effectiveTime,
                                                                  String  methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        List<GlossaryCategoryElement> results = glossaryCategoryHandler.getGlossarySubCategories(userId,
                                                                                                 glossaryCategoryGUID,
                                                                                                 glossaryCategoryGUIDParameterName,
                                                                                                 startFrom,
                                                                                                 pageSize,
                                                                                                 effectiveTime,
                                                                                                 forLineage,
                                                                                                 forDuplicateProcessing,
                                                                                                 methodName);

        this.addCorrelationPropertiesToGlossaryCategories(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          results,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);

        return results;
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElement getGlossaryCategoryByGUID(String  userId,
                                                             String  assetManagerGUID,
                                                             String  assetManagerName,
                                                             String  openMetadataGUID,
                                                             boolean forLineage,
                                                             boolean forDuplicateProcessing,
                                                             Date    effectiveTime,
                                                             String  methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String guidParameterName  = "openMetadataGUID";

        GlossaryCategoryElement glossaryCategory = glossaryCategoryHandler.getGlossaryCategoryByGUID(userId,
                                                                                                     openMetadataGUID,
                                                                                                     guidParameterName,
                                                                                                     effectiveTime,
                                                                                                     forLineage,
                                                                                                     forDuplicateProcessing,
                                                                                                     methodName);

        if (glossaryCategory != null)
        {
            glossaryCategory.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                 openMetadataGUID,
                                                                                 guidParameterName,
                                                                                 OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                                                 assetManagerGUID,
                                                                                 assetManagerName,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 effectiveTime,
                                                                                 methodName));
        }

        return glossaryCategory;
    }


    /* ===============================================================================
     * A glossary typically contains many glossary terms, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a glossary term.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome  ensure that only the asset manager can update this element
     * @param glossaryTermProperties properties for the glossary term
     * @param updateDescription description of the update for the revision history
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryTerm(String                        userId,
                                     String                        glossaryGUID,
                                     MetadataCorrelationProperties correlationProperties,
                                     boolean                       assetManagerIsHome,
                                     GlossaryTermProperties        glossaryTermProperties,
                                     String                        updateDescription,
                                     Date                          effectiveTime,
                                     boolean                       forLineage,
                                     boolean                       forDuplicateProcessing,
                                     String                        methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String propertiesParameterName     = "glossaryTermProperties";
        final String qualifiedNameParameterName  = "glossaryTermProperties.qualifiedName";

        invalidParameterHandler.validateObject(glossaryTermProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(glossaryTermProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String glossaryTermGUID = glossaryTermHandler.createGlossaryTerm(userId,
                                                                         getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                         getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                         glossaryGUID,
                                                                         glossaryGUIDParameterName,
                                                                         glossaryTermProperties.getQualifiedName(),
                                                                         glossaryTermProperties.getDisplayName(),
                                                                         glossaryTermProperties.getSummary(),
                                                                         glossaryTermProperties.getDescription(),
                                                                         glossaryTermProperties.getExamples(),
                                                                         glossaryTermProperties.getAbbreviation(),
                                                                         glossaryTermProperties.getUsage(),
                                                                         glossaryTermProperties.getPublishVersionIdentifier(),
                                                                         glossaryTermProperties.getAdditionalProperties(),
                                                                         glossaryTermProperties.getTypeName(),
                                                                         glossaryTermProperties.getExtendedProperties(),
                                                                         InstanceStatus.ACTIVE,
                                                                         glossaryTermProperties.getEffectiveFrom(),
                                                                         glossaryTermProperties.getEffectiveTo(),
                                                                         effectiveTime,
                                                                         methodName);

        if (glossaryTermGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          glossaryTermGUID,
                                          glossaryTermGUIDParameterName,
                                          OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);

            String revisionHistoryTitle = "Glossary term created by " + userId + " on " + new Date();

            this.updateRevisionHistory(userId,
                                       glossaryTermGUID,
                                       glossaryTermProperties.getQualifiedName(),
                                       OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                       revisionHistoryTitle,
                                       updateDescription);
        }

        return glossaryTermGUID;
    }


    /**
     * Convert the GlossaryTermStatus to an InstanceStatus understood by the repository services.
     *
     * @param status status from caller
     * @return instance status
     */
    private InstanceStatus getInstanceStatus(GlossaryTermStatus status)
    {
        if (status != null)
        {
            switch (status)
            {
                case DRAFT:
                    return InstanceStatus.DRAFT;

                case PREPARED:
                    return InstanceStatus.PREPARED;

                case PROPOSED:
                    return InstanceStatus.PROPOSED;

                case APPROVED:
                    return InstanceStatus.APPROVED;

                case REJECTED:
                    return InstanceStatus.REJECTED;

               case ACTIVE:
                    return InstanceStatus.ACTIVE;

                case DEPRECATED:
                    return InstanceStatus.DEPRECATED;

                case OTHER:
                    return InstanceStatus.OTHER;

            }
        }

        return null;
    }


    /**
     * Create a new metadata element to represent a glossary term whose lifecycle is managed through a controlled workflow.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param glossaryTermProperties properties for the glossary term
     * @param updateDescription description of the update for the revision history
     * @param initialStatus glossary term status to use when the object is created
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createControlledGlossaryTerm(String                        userId,
                                               String                        glossaryGUID,
                                               MetadataCorrelationProperties correlationProperties,
                                               boolean                       assetManagerIsHome,
                                               GlossaryTermProperties        glossaryTermProperties,
                                               GlossaryTermStatus            initialStatus,
                                               String                        updateDescription,
                                               Date                          effectiveTime,
                                               boolean                       forLineage,
                                               boolean                       forDuplicateProcessing,
                                               String                        methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String propertiesParameterName     = "glossaryTermProperties";
        final String qualifiedNameParameterName  = "glossaryTermProperties.qualifiedName";
        final String initialStatusParameterName  = "initialStatus";

        invalidParameterHandler.validateObject(glossaryTermProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateObject(initialStatus, initialStatusParameterName, methodName);
        invalidParameterHandler.validateName(glossaryTermProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.CONTROLLED_GLOSSARY_TERM_TYPE_NAME;

        if (glossaryTermProperties.getTypeName() != null)
        {
            typeName = glossaryTermProperties.getTypeName();
        }

        String glossaryTermGUID = glossaryTermHandler.createGlossaryTerm(userId,
                                                                         getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                         getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                         glossaryGUID,
                                                                         glossaryGUIDParameterName,
                                                                         glossaryTermProperties.getQualifiedName(),
                                                                         glossaryTermProperties.getDisplayName(),
                                                                         glossaryTermProperties.getSummary(),
                                                                         glossaryTermProperties.getDescription(),
                                                                         glossaryTermProperties.getExamples(),
                                                                         glossaryTermProperties.getAbbreviation(),
                                                                         glossaryTermProperties.getUsage(),
                                                                         glossaryTermProperties.getPublishVersionIdentifier(),
                                                                         glossaryTermProperties.getAdditionalProperties(),
                                                                         typeName,
                                                                         glossaryTermProperties.getExtendedProperties(),
                                                                         getInstanceStatus(initialStatus),
                                                                         glossaryTermProperties.getEffectiveFrom(),
                                                                         glossaryTermProperties.getEffectiveTo(),
                                                                         effectiveTime,
                                                                         methodName);

        if (glossaryTermGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          glossaryTermGUID,
                                          glossaryTermGUIDParameterName,
                                          OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);

            String revisionHistoryTitle = "Glossary term created by " + userId + " on " + new Date();

            this.updateRevisionHistory(userId,
                                       glossaryTermGUID,
                                       glossaryTermProperties.getQualifiedName(),
                                       OpenMetadataType.CONTROLLED_GLOSSARY_TERM_TYPE_NAME,
                                       revisionHistoryTitle,
                                       updateDescription);
        }

        return glossaryTermGUID;
    }


    /**
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param initialStatus glossary term status to use when the object is created
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param templateSubstitute is this element a template substitute (used as the "other end" of a new/updated relationship)
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryTermFromTemplate(String                        userId,
                                                 MetadataCorrelationProperties correlationProperties,
                                                 boolean                       assetManagerIsHome,
                                                 String                        glossaryGUID,
                                                 String                        templateGUID,
                                                 TemplateProperties            templateProperties,
                                                 GlossaryTermStatus            initialStatus,
                                                 boolean                       deepCopy,
                                                 boolean                       templateSubstitute,
                                                 String                        methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String templateGUIDParameterName         = "templateGUID";
        final String propertiesParameterName           = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);

        String glossaryTermGUID = glossaryTermHandler.createGlossaryTermFromTemplate(userId,
                                                                                     getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                                     getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                                     glossaryGUID,
                                                                                     glossaryGUIDParameterName,
                                                                                     templateGUID,
                                                                                     templateProperties.getQualifiedName(),
                                                                                     templateProperties.getDisplayName(),
                                                                                     templateProperties.getDescription(),
                                                                                     templateProperties.getVersionIdentifier(),
                                                                                     getInstanceStatus(initialStatus),
                                                                                     deepCopy,
                                                                                     templateSubstitute,
                                                                                     methodName);
        if (glossaryTermGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          glossaryTermGUID,
                                          glossaryTermGUIDParameterName,
                                          OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return glossaryTermGUID;
    }


    /**
     * Update the properties of the metadata element representing a glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermProperties new properties for the glossary term
     * @param updateDescription description of the update for the revision history
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTerm(String                        userId,
                                   MetadataCorrelationProperties correlationProperties,
                                   String                        glossaryTermGUID,
                                   GlossaryTermProperties        glossaryTermProperties,
                                   String                        updateDescription,
                                   boolean                       isMergeUpdate,
                                   boolean                       forLineage,
                                   boolean                       forDuplicateProcessing,
                                   Date                          effectiveTime,
                                   String                        methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String propertiesParameterName    = "glossaryTermProperties";
        final String qualifiedNameParameterName = "glossaryTermProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryTermProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(glossaryTermProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.updateGlossaryTerm(userId,
                                               getExternalSourceGUID(correlationProperties),
                                               getExternalSourceName(correlationProperties),
                                               glossaryTermGUID,
                                               glossaryTermGUIDParameterName,
                                               glossaryTermProperties.getQualifiedName(),
                                               glossaryTermProperties.getDisplayName(),
                                               glossaryTermProperties.getSummary(),
                                               glossaryTermProperties.getDescription(),
                                               glossaryTermProperties.getExamples(),
                                               glossaryTermProperties.getAbbreviation(),
                                               glossaryTermProperties.getUsage(),
                                               glossaryTermProperties.getPublishVersionIdentifier(),
                                               glossaryTermProperties.getAdditionalProperties(),
                                               OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                               glossaryTermProperties.getExtendedProperties(),
                                               glossaryTermProperties.getEffectiveFrom(),
                                               glossaryTermProperties.getEffectiveTo(),
                                               isMergeUpdate,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);

        String revisionHistoryTitle = "Glossary term updated by " + userId + " on " + new Date();

        this.updateRevisionHistory(userId,
                                   glossaryTermGUID,
                                   glossaryTermProperties.getQualifiedName(),
                                   OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                   revisionHistoryTitle,
                                   updateDescription);
    }


    /**
     * Update the status of the metadata element representing a glossary term.  This is only valid on
     * a controlled glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermStatus new properties for the glossary term
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTermStatus(String                        userId,
                                         MetadataCorrelationProperties correlationProperties,
                                         String                        glossaryTermGUID,
                                         GlossaryTermStatus            glossaryTermStatus,
                                         boolean                       forLineage,
                                         boolean                       forDuplicateProcessing,
                                         Date                          effectiveTime,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String glossaryTermStatusParameterName  = "glossaryTermStatus";

        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(glossaryTermStatus, glossaryTermStatusParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.updateGlossaryTermStatus(userId,
                                                     getExternalSourceGUID(correlationProperties),
                                                     getExternalSourceName(correlationProperties),
                                                     glossaryTermGUID,
                                                     glossaryTermGUIDParameterName,
                                                     getInstanceStatus(glossaryTermStatus),
                                                     glossaryTermStatusParameterName,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     methodName);
    }


    /**
     * Update the glossary term using the properties and classifications from the parentGUID stored in the request body.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param templateGUID identifier for the template glossary term
     * @param isMergeClassifications should the classification be merged or replace the target entity?
     * @param isMergeProperties should the properties be merged with the existing ones or replace them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTermFromTemplate(String                        userId,
                                               MetadataCorrelationProperties correlationProperties,
                                               String                        glossaryTermGUID,
                                               String                        templateGUID,
                                               boolean                       isMergeClassifications,
                                               boolean                       isMergeProperties,
                                               boolean                       forLineage,
                                               boolean                       forDuplicateProcessing,
                                               Date                          effectiveTime,
                                               String                        methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String templateGUIDParameterName  = "templateGUID";

        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.updateGlossaryTermFromTemplate(userId,
                                                           getExternalSourceGUID(correlationProperties),
                                                           getExternalSourceName(correlationProperties),
                                                           glossaryTermGUID,
                                                           glossaryTermGUIDParameterName,
                                                           templateGUID,
                                                           templateGUIDParameterName,
                                                           isMergeClassifications,
                                                           isMergeProperties,
                                                           effectiveTime,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           methodName);
    }


    /**
     * Move a glossary term from one glossary to another.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param newGlossaryGUID identifier for the new glossary
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void moveGlossaryTerm(String                        userId,
                                 MetadataCorrelationProperties correlationProperties,
                                 String                        glossaryTermGUID,
                                 String                        newGlossaryGUID,
                                 boolean                       forLineage,
                                 boolean                       forDuplicateProcessing,
                                 Date                          effectiveTime,
                                 String                        methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String newGlossaryGUIDParameterName  = "newGlossaryGUID";

        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(newGlossaryGUID, newGlossaryGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.moveGlossaryTerm(userId,
                                             getExternalSourceGUID(correlationProperties),
                                             getExternalSourceName(correlationProperties),
                                             glossaryTermGUID,
                                             glossaryTermGUIDParameterName,
                                             newGlossaryGUID,
                                             newGlossaryGUIDParameterName,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Return the ordinal for the relationship status - handling null values
     *
     * @param status supplied status
     * @return resulting ordinal
     */
    private int getTermRelationshipStatus(GlossaryTermRelationshipStatus status)
    {
        if (status != null)
        {
            return status.getOpenTypeOrdinal();
        }

        return GlossaryTermRelationshipStatus.ACTIVE.getOpenTypeOrdinal();
    }


    /**
     * Link a term to a category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param categorizationProperties properties for the categorization relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupTermCategory(String                     userId,
                                  String                     assetManagerGUID,
                                  String                     assetManagerName,
                                  String                     glossaryCategoryGUID,
                                  String                     glossaryTermGUID,
                                  GlossaryTermCategorization categorizationProperties,
                                  boolean                    forLineage,
                                  boolean                    forDuplicateProcessing,
                                  Date                       effectiveTime,
                                  String                     methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String glossaryCategoryGUIDParameterName = "glossaryCategoryGUID";
        final String glossaryTermGUIDParameterName = "glossaryTermGUID";

        if (categorizationProperties != null)
        {
            glossaryTermHandler.setupTermCategory(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  glossaryCategoryGUID,
                                                  glossaryCategoryGUIDParameterName,
                                                  glossaryTermGUID,
                                                  glossaryTermGUIDParameterName,
                                                  categorizationProperties.getDescription(),
                                                  this.getTermRelationshipStatus(categorizationProperties.getStatus()),
                                                  categorizationProperties.getEffectiveFrom(),
                                                  categorizationProperties.getEffectiveTo(),
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  effectiveTime,
                                                  methodName);
        }
        else
        {
            glossaryTermHandler.setupTermCategory(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  glossaryCategoryGUID,
                                                  glossaryCategoryGUIDParameterName,
                                                  glossaryTermGUID,
                                                  glossaryTermGUIDParameterName,
                                                  null,
                                                  GlossaryTermRelationshipStatus.ACTIVE.getOpenTypeOrdinal(),
                                                  null,
                                                  null,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  effectiveTime,
                                                  methodName);
        }

        externalIdentifierHandler.logRelationshipCreation(assetManagerGUID,
                                                          assetManagerName,
                                                          OpenMetadataType.TERM_CATEGORIZATION_TYPE_NAME,
                                                          glossaryCategoryGUID,
                                                          OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                          glossaryTermGUID,
                                                          OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                          methodName);
    }


    /**
     * Unlink a term from a category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermCategory(String  userId,
                                  String  assetManagerGUID,
                                  String  assetManagerName,
                                  String  glossaryCategoryGUID,
                                  String  glossaryTermGUID,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String glossaryCategoryGUIDParameterName = "glossaryCategoryGUID";
        final String glossaryTermGUIDParameterName = "glossaryTermGUID";

        glossaryTermHandler.clearTermCategory(userId,
                                              assetManagerGUID,
                                              assetManagerName,
                                              glossaryCategoryGUID,
                                              glossaryCategoryGUIDParameterName,
                                              glossaryTermGUID,
                                              glossaryTermGUIDParameterName,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);

        externalIdentifierHandler.logRelationshipRemoval(assetManagerGUID,
                                                         assetManagerName,
                                                         OpenMetadataType.TERM_CATEGORIZATION_TYPE_NAME,
                                                         glossaryCategoryGUID,
                                                         OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                         glossaryTermGUID,
                                                         OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                         methodName);
    }


    /**
     * Return the list of term-to-term relationship names.
     *
     * @return list of type names that are subtypes of asset
     */
    public List<String> getTermRelationshipTypeNames()
    {
        return glossaryTermHandler.getTermRelationshipTypeNames();
    }


    /**
     * Link two terms together using a specialist relationship.  If there are no relationship properties
     * then the status is set to ACTIVE and other properties are null.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param relationshipsProperties properties for the categorization relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupTermRelationship(String                   userId,
                                      String                   assetManagerGUID,
                                      String                   assetManagerName,
                                      String                   glossaryTermOneGUID,
                                      String                   relationshipTypeName,
                                      String                   glossaryTermTwoGUID,
                                      GlossaryTermRelationship relationshipsProperties,
                                      boolean                  forLineage,
                                      boolean                  forDuplicateProcessing,
                                      Date                     effectiveTime,
                                      String                   methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String glossaryTermOneGUIDParameterName = "glossaryTermOneGUID";
        final String glossaryTermTwoGUIDParameterName = "glossaryTermTwoGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        if (relationshipsProperties != null)
        {
            glossaryTermHandler.setupTermRelationship(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      glossaryTermOneGUID,
                                                      glossaryTermOneGUIDParameterName,
                                                      relationshipTypeName,
                                                      relationshipTypeNameParameterName,
                                                      glossaryTermTwoGUID,
                                                      glossaryTermTwoGUIDParameterName,
                                                      relationshipsProperties.getExpression(),
                                                      relationshipsProperties.getDescription(),
                                                      this.getTermRelationshipStatus(relationshipsProperties.getStatus()),
                                                      relationshipsProperties.getSteward(),
                                                      relationshipsProperties.getSource(),
                                                      relationshipsProperties.getEffectiveFrom(),
                                                      relationshipsProperties.getEffectiveTo(),
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
        }
        else
        {
            glossaryTermHandler.setupTermRelationship(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      glossaryTermOneGUID,
                                                      glossaryTermOneGUIDParameterName,
                                                      relationshipTypeName,
                                                      relationshipTypeNameParameterName,
                                                      glossaryTermTwoGUID,
                                                      glossaryTermTwoGUIDParameterName,
                                                      null,
                                                      null,
                                                      GlossaryTermRelationshipStatus.ACTIVE.getOpenTypeOrdinal(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
        }

        externalIdentifierHandler.logRelationshipCreation(assetManagerGUID,
                                                          assetManagerName,
                                                          relationshipTypeName,
                                                          glossaryTermOneGUID,
                                                          OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                          glossaryTermTwoGUID,
                                                          OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                          methodName);
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param relationshipsProperties properties for the categorization relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateTermRelationship(String                   userId,
                                       String                   assetManagerGUID,
                                       String                   assetManagerName,
                                       String                   glossaryTermOneGUID,
                                       String                   relationshipTypeName,
                                       String                   glossaryTermTwoGUID,
                                       GlossaryTermRelationship relationshipsProperties,
                                       boolean                  forLineage,
                                       boolean                  forDuplicateProcessing,
                                       Date                     effectiveTime,
                                       String                   methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String glossaryTermOneGUIDParameterName = "glossaryTermOneGUID";
        final String glossaryTermTwoGUIDParameterName = "glossaryTermTwoGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        if (relationshipsProperties != null)
        {
            glossaryTermHandler.updateTermRelationship(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       glossaryTermOneGUID,
                                                       glossaryTermOneGUIDParameterName,
                                                       relationshipTypeName,
                                                       relationshipTypeNameParameterName,
                                                       glossaryTermTwoGUID,
                                                       glossaryTermTwoGUIDParameterName,
                                                       relationshipsProperties.getExpression(),
                                                       relationshipsProperties.getDescription(),
                                                       this.getTermRelationshipStatus(relationshipsProperties.getStatus()),
                                                       relationshipsProperties.getSteward(),
                                                       relationshipsProperties.getSource(),
                                                       relationshipsProperties.getEffectiveFrom(),
                                                       relationshipsProperties.getEffectiveTo(),
                                                       true,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       effectiveTime,
                                                       methodName);
        }
        else
        {
            glossaryTermHandler.updateTermRelationship(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       glossaryTermOneGUID,
                                                       glossaryTermOneGUIDParameterName,
                                                       relationshipTypeName,
                                                       relationshipTypeNameParameterName,
                                                       glossaryTermTwoGUID,
                                                       glossaryTermTwoGUIDParameterName,
                                                       null,
                                                       null,
                                                       GlossaryTermRelationshipStatus.ACTIVE.getOpenTypeOrdinal(),
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       true,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       effectiveTime,
                                                       methodName);
        }
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermRelationship(String  userId,
                                      String  assetManagerGUID,
                                      String  assetManagerName,
                                      String  glossaryTermOneGUID,
                                      String  relationshipTypeName,
                                      String  glossaryTermTwoGUID,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String glossaryTermOneGUIDParameterName = "glossaryTermOneGUID";
        final String glossaryTermTwoGUIDParameterName = "glossaryTermTwoGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        glossaryTermHandler.clearTermRelationship(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  glossaryTermOneGUID,
                                                  glossaryTermOneGUIDParameterName,
                                                  relationshipTypeName,
                                                  relationshipTypeNameParameterName,
                                                  glossaryTermTwoGUID,
                                                  glossaryTermTwoGUIDParameterName,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  effectiveTime,
                                                  methodName);

        externalIdentifierHandler.logRelationshipRemoval(assetManagerGUID,
                                                         assetManagerName,
                                                         relationshipTypeName,
                                                         glossaryTermOneGUID,
                                                         OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                         glossaryTermTwoGUID,
                                                         OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                         methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsAbstractConcept(String                        userId,
                                         MetadataCorrelationProperties correlationProperties,
                                         String                        glossaryTermGUID,
                                         boolean                       forLineage,
                                         boolean                       forDuplicateProcessing,
                                         Date                          effectiveTime,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.setTermAsAbstractConcept(userId,
                                                     getExternalSourceGUID(correlationProperties),
                                                     getExternalSourceName(correlationProperties),
                                                     glossaryTermGUID,
                                                     glossaryTermGUIDParameterName,
                                                     null,
                                                     null,
                                                     true,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsAbstractConcept(String                        userId,
                                           MetadataCorrelationProperties correlationProperties,
                                           String                        glossaryTermGUID,
                                           boolean                       forLineage,
                                           boolean                       forDuplicateProcessing,
                                           Date                          effectiveTime,
                                           String                        methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.clearTermAsAbstractConcept(userId,
                                                       getExternalSourceGUID(correlationProperties),
                                                       getExternalSourceName(correlationProperties),
                                                       glossaryTermGUID,
                                                       glossaryTermGUIDParameterName,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       effectiveTime,
                                                       methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes a data field and supply
     * properties that describe the characteristics of the data values found within.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param properties characterizations of the data values stored in the data field
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsDataField(String                        userId,
                                   MetadataCorrelationProperties correlationProperties,
                                   String                        glossaryTermGUID,
                                   DataFieldValuesProperties     properties,
                                   boolean                       forLineage,
                                   boolean                       forDuplicateProcessing,
                                   Date                          effectiveTime,
                                   String                        methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        if (properties != null)
        {
            glossaryTermHandler.addDataFieldValuesClassification(userId,
                                                                 glossaryTermGUID,
                                                                 glossaryTermGUIDParameterName,
                                                                 OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                 properties.getDefaultValue(),
                                                                 properties.getSampleValues(),
                                                                 properties.getDataPattern(),
                                                                 properties.getNamePattern(),
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);
        }
        else
        {
            glossaryTermHandler.addDataFieldValuesClassification(userId,
                                                                 glossaryTermGUID,
                                                                 glossaryTermGUIDParameterName,
                                                                 OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);
        }
    }


    /**
     * Remove the data field designation from the glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsDataField(String                        userId,
                                     MetadataCorrelationProperties correlationProperties,
                                     String                        glossaryTermGUID,
                                     boolean                       forLineage,
                                     boolean                       forDuplicateProcessing,
                                     Date                          effectiveTime,
                                     String                        methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.removeDataFieldValuesClassification(userId,
                                                                glossaryTermGUID,
                                                                glossaryTermGUIDParameterName,
                                                                OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime,
                                                                methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsDataValue(String                        userId,
                                   MetadataCorrelationProperties correlationProperties,
                                   String                        glossaryTermGUID,
                                   boolean                       forLineage,
                                   boolean                       forDuplicateProcessing,
                                   Date                          effectiveTime,
                                   String                        methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.setTermAsDataValue(userId,
                                               getExternalSourceGUID(correlationProperties),
                                               getExternalSourceName(correlationProperties),
                                               glossaryTermGUID,
                                               glossaryTermGUIDParameterName,
                                               null,
                                               null,
                                               true,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsDataValue(String                        userId,
                                     MetadataCorrelationProperties correlationProperties,
                                     String                        glossaryTermGUID,
                                     boolean                       forLineage,
                                     boolean                       forDuplicateProcessing,
                                     Date                          effectiveTime,
                                     String                        methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.clearTermAsDataValue(userId,
                                                 getExternalSourceGUID(correlationProperties),
                                                 getExternalSourceName(correlationProperties),
                                                 glossaryTermGUID,
                                                 glossaryTermGUIDParameterName,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
    }


    /**
     * Return the open metadata type ordinal (handling the null condition).
     *
     * @param activityType activity type enum
     * @return open type ordinal
     */
    private int getActivityType(GlossaryTermActivityType activityType)
    {
        if (activityType != null)
        {
            return activityType.getOpenTypeOrdinal();
        }

        return GlossaryTermActivityType.ACTION.getOpenTypeOrdinal();
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param activityType type of activity
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsActivity(String                        userId,
                                  MetadataCorrelationProperties correlationProperties,
                                  String                        glossaryTermGUID,
                                  GlossaryTermActivityType      activityType,
                                  boolean                       forLineage,
                                  boolean                       forDuplicateProcessing,
                                  Date                          effectiveTime,
                                  String                        methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.setTermAsActivity(userId,
                                              getExternalSourceGUID(correlationProperties),
                                              getExternalSourceName(correlationProperties),
                                              glossaryTermGUID,
                                              glossaryTermGUIDParameterName,
                                              this.getActivityType(activityType),
                                              null,
                                              null,
                                              true,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsActivity(String                        userId,
                                    MetadataCorrelationProperties correlationProperties,
                                    String                        glossaryTermGUID,
                                    boolean                       forLineage,
                                    boolean                       forDuplicateProcessing,
                                    Date                          effectiveTime,
                                    String                        methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.clearTermAsActivity(userId,
                                                getExternalSourceGUID(correlationProperties),
                                                getExternalSourceName(correlationProperties),
                                                glossaryTermGUID,
                                                glossaryTermGUIDParameterName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param contextDefinition more details of the context
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsContext(String                        userId,
                                 MetadataCorrelationProperties correlationProperties,
                                 String                        glossaryTermGUID,
                                 GlossaryTermContextDefinition contextDefinition,
                                 boolean                       forLineage,
                                 boolean                       forDuplicateProcessing,
                                 Date                          effectiveTime,
                                 String                        methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        if (contextDefinition != null)
        {
            glossaryTermHandler.setTermAsContext(userId,
                                                 getExternalSourceGUID(correlationProperties),
                                                 getExternalSourceName(correlationProperties),
                                                 glossaryTermGUID,
                                                 glossaryTermGUIDParameterName,
                                                 contextDefinition.getDescription(),
                                                 contextDefinition.getScope(),
                                                 null,
                                                 null,
                                                 true,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
        }
        else
        {
            glossaryTermHandler.setTermAsContext(userId,
                                                 getExternalSourceGUID(correlationProperties),
                                                 getExternalSourceName(correlationProperties),
                                                 glossaryTermGUID,
                                                 glossaryTermGUIDParameterName,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 true,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
        }
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsContext(String                        userId,
                                   MetadataCorrelationProperties correlationProperties,
                                   String                        glossaryTermGUID,
                                   boolean                       forLineage,
                                   boolean                       forDuplicateProcessing,
                                   Date                          effectiveTime,
                                   String                        methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.clearTermAsContext(userId,
                                               getExternalSourceGUID(correlationProperties),
                                               getExternalSourceName(correlationProperties),
                                               glossaryTermGUID,
                                               glossaryTermGUIDParameterName,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine object.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsSpineObject(String                        userId,
                                     MetadataCorrelationProperties correlationProperties,
                                     String                        glossaryTermGUID,
                                     boolean                       forLineage,
                                     boolean                       forDuplicateProcessing,
                                     Date                          effectiveTime,
                                     String                        methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.setTermAsSpineObject(userId,
                                                 getExternalSourceGUID(correlationProperties),
                                                 getExternalSourceName(correlationProperties),
                                                 glossaryTermGUID,
                                                 glossaryTermGUIDParameterName,
                                                 null,
                                                 null,
                                                 true,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
    }


    /**
     * Remove the spine object designation from the glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsSpineObject(String                        userId,
                                       MetadataCorrelationProperties correlationProperties,
                                       String                        glossaryTermGUID,
                                       boolean                       forLineage,
                                       boolean                       forDuplicateProcessing,
                                       Date                          effectiveTime,
                                       String                        methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.clearTermAsSpineObject(userId,
                                                   getExternalSourceGUID(correlationProperties),
                                                   getExternalSourceName(correlationProperties),
                                                   glossaryTermGUID,
                                                   glossaryTermGUIDParameterName,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
    }



    /**
     * Classify the glossary term to indicate that it describes a spine attribute.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsSpineAttribute(String                        userId,
                                        MetadataCorrelationProperties correlationProperties,
                                        String                        glossaryTermGUID,
                                        boolean                       forLineage,
                                        boolean                       forDuplicateProcessing,
                                        Date                          effectiveTime,
                                        String                        methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.setTermAsSpineAttribute(userId,
                                                    getExternalSourceGUID(correlationProperties),
                                                    getExternalSourceName(correlationProperties),
                                                    glossaryTermGUID,
                                                    glossaryTermGUIDParameterName,
                                                    null,
                                                    null,
                                                    true,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
    }


    /**
     * Remove the spine attribute designation from the glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsSpineAttribute(String                        userId,
                                          MetadataCorrelationProperties correlationProperties,
                                          String                        glossaryTermGUID,
                                          boolean                       forLineage,
                                          boolean                       forDuplicateProcessing,
                                          Date                          effectiveTime,
                                          String                        methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.clearTermAsSpineAttribute(userId,
                                                      getExternalSourceGUID(correlationProperties),
                                                      getExternalSourceName(correlationProperties),
                                                      glossaryTermGUID,
                                                      glossaryTermGUIDParameterName,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes an object identifier.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsObjectIdentifier(String                        userId,
                                          MetadataCorrelationProperties correlationProperties,
                                          String                        glossaryTermGUID,
                                          boolean                       forLineage,
                                          boolean                       forDuplicateProcessing,
                                          Date                          effectiveTime,
                                          String                        methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.setTermAsObjectIdentifier(userId,
                                                      getExternalSourceGUID(correlationProperties),
                                                      getExternalSourceName(correlationProperties),
                                                      glossaryTermGUID,
                                                      glossaryTermGUIDParameterName,
                                                      null,
                                                      null,
                                                      true,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
    }


    /**
     * Remove the object identifier designation from the glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsObjectIdentifier(String                        userId,
                                            MetadataCorrelationProperties correlationProperties,
                                            String                        glossaryTermGUID,
                                            boolean                       forLineage,
                                            boolean                       forDuplicateProcessing,
                                            Date                          effectiveTime,
                                            String                        methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.clearTermAsObjectIdentifier(userId,
                                                        getExternalSourceGUID(correlationProperties),
                                                        getExternalSourceName(correlationProperties),
                                                        glossaryTermGUID,
                                                        glossaryTermGUIDParameterName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
    }


    /**
     * Undo the last update to the glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return glossary term after undo
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GlossaryTermElement undoGlossaryTermUpdate(String                        userId,
                                                      MetadataCorrelationProperties correlationProperties,
                                                      String                        glossaryTermGUID,
                                                      boolean                       forLineage,
                                                      boolean                       forDuplicateProcessing,
                                                      Date                          effectiveTime,
                                                      String                        methodName) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        GlossaryTermElement glossaryTerm = glossaryTermHandler.undoBeanUpdateInRepository(userId,
                                                                                          getExternalSourceGUID(correlationProperties),
                                                                                          getExternalSourceName(correlationProperties),
                                                                                          glossaryTermGUID,
                                                                                          glossaryTermGUIDParameterName,
                                                                                          OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                                          forLineage,
                                                                                          forDuplicateProcessing,
                                                                                          effectiveTime,
                                                                                          methodName);

        if (glossaryTerm != null)
        {
            glossaryTerm.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                             glossaryTermGUID,
                                                                             glossaryTermGUIDParameterName,
                                                                             OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                             getExternalSourceGUID(correlationProperties),
                                                                             getExternalSourceName(correlationProperties),
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName));
        }

        return glossaryTerm;
    }


    /**
     * Classify the glossary term in the repository to show that it has been archived and is only needed for lineage.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param archiveProperties properties describing the archiver
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void archiveGlossaryTerm(String                        userId,
                                    MetadataCorrelationProperties correlationProperties,
                                    String                        glossaryTermGUID,
                                    ArchiveProperties             archiveProperties,
                                    boolean                       forDuplicateProcessing,
                                    Date                          effectiveTime,
                                    String                        methodName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        true,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        if (archiveProperties == null)
        {
            glossaryTermHandler.archiveGlossaryTerm(userId,
                                                    getExternalSourceGUID(correlationProperties),
                                                    getExternalSourceName(correlationProperties),
                                                    glossaryTermGUID,
                                                    glossaryTermGUIDParameterName,
                                                    null,
                                                    null,
                                                    null,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
        }
        else
        {
            glossaryTermHandler.archiveGlossaryTerm(userId,
                                                    getExternalSourceGUID(correlationProperties),
                                                    getExternalSourceName(correlationProperties),
                                                    glossaryTermGUID,
                                                    glossaryTermGUIDParameterName,
                                                    archiveProperties.getArchiveDate(),
                                                    archiveProperties.getArchiveProcess(),
                                                    archiveProperties.getArchiveProperties(),
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
        }
    }


    /**
     * Remove the metadata element representing a glossary term.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossaryTerm(String                        userId,
                                   MetadataCorrelationProperties correlationProperties,
                                   String                        glossaryTermGUID,
                                   boolean                       forLineage,
                                   boolean                       forDuplicateProcessing,
                                   Date                          effectiveTime,
                                   String                        methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        glossaryTermHandler.removeGlossaryTerm(userId,
                                               getExternalSourceGUID(correlationProperties),
                                               getExternalSourceName(correlationProperties),
                                               glossaryTermGUID,
                                               glossaryTermGUIDParameterName,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
    }


    /**
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary to query
     * @param searchString string to find in the properties
     * @param limitResultsByStatus By default, terms in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>   findGlossaryTerms(String                   userId,
                                                         String                   assetManagerGUID,
                                                         String                   assetManagerName,
                                                         String                   glossaryGUID,
                                                         String                   searchString,
                                                         List<GlossaryTermStatus> limitResultsByStatus,
                                                         int                      startFrom,
                                                         int                      pageSize,
                                                         boolean                  forLineage,
                                                         boolean                  forDuplicateProcessing,
                                                         Date                     effectiveTime,
                                                         String                   methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        List<GlossaryTermElement> results = glossaryTermHandler.findTerms(userId,
                                                                          glossaryGUID,
                                                                          searchString,
                                                                          getInstanceStatuses(limitResultsByStatus),
                                                                          startFrom,
                                                                          pageSize,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

        this.addCorrelationPropertiesToGlossaryTerms(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }


    /**
     * Transform a list of GlossaryTermStatus into InstanceStatuses.
     *
     * @param glossaryTermStatuses supplied glossary term statuses
     * @return converted statuses
     */
    private List<InstanceStatus> getInstanceStatuses(List<GlossaryTermStatus> glossaryTermStatuses)
    {
        if (glossaryTermStatuses != null)
        {
            List<InstanceStatus> instanceStatuses = new ArrayList<>();

            for (GlossaryTermStatus glossaryTermStatus : glossaryTermStatuses)
            {
                instanceStatuses.add(getInstanceStatus(glossaryTermStatus));
            }

            if (!instanceStatuses.isEmpty())
            {
                return instanceStatuses;
            }
        }

        return null;
    }


    /**
     * Convert the list of status enums into a list of ordinals for the equivalent open metadata type.
     *
     * @param glossaryTermRelationshipStatuses relationship statuses
     * @return list of integers, or null
     */
    private List<Integer> getEnumOrdinals(List<GlossaryTermRelationshipStatus> glossaryTermRelationshipStatuses)
    {
        if (glossaryTermRelationshipStatuses != null)
        {
            List<Integer> ordinals = new ArrayList<>();

            for (GlossaryTermRelationshipStatus status : glossaryTermRelationshipStatuses)
            {
                if (status != null)
                {
                    ordinals.add(status.getOpenTypeOrdinal());
                }
            }

            if (! ordinals.isEmpty())
            {
                return ordinals;
            }
        }

        return null;
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>    getTermsForGlossary(String  userId,
                                                            String  assetManagerGUID,
                                                            String  assetManagerName,
                                                            String  glossaryGUID,
                                                            int     startFrom,
                                                            int     pageSize,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {

        List<GlossaryTermElement> results = glossaryTermHandler.getTermsForGlossary(userId,
                                                                                    glossaryGUID,
                                                                                    glossaryGUIDParameterName,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName);

        this.addCorrelationPropertiesToGlossaryTerms(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary category.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category of interest
     * @param limitResultsByStatus By default, term relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>    getTermsForGlossaryCategory(String                               userId,
                                                                    String                               assetManagerGUID,
                                                                    String                               assetManagerName,
                                                                    String                               glossaryCategoryGUID,
                                                                    List<GlossaryTermRelationshipStatus> limitResultsByStatus,
                                                                    int                                  startFrom,
                                                                    int                                  pageSize,
                                                                    boolean                              forLineage,
                                                                    boolean                              forDuplicateProcessing,
                                                                    Date                                 effectiveTime,
                                                                    String                               methodName) throws InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException
    {
        List<GlossaryTermElement> results = glossaryTermHandler.getTermsForGlossaryCategory(userId,
                                                                                            glossaryCategoryGUID,
                                                                                            glossaryCategoryGUIDParameterName,
                                                                                            getEnumOrdinals(limitResultsByStatus),
                                                                                            startFrom,
                                                                                            pageSize,
                                                                                            forLineage,
                                                                                            forDuplicateProcessing,
                                                                                            effectiveTime,
                                                                                            methodName);

        this.addCorrelationPropertiesToGlossaryTerms(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }


    /**
     * Retrieve the list of glossary terms associated with the requested glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term of interest
     * @param relationshipTypeName optional name of relationship
     * @param limitResultsByStatus By default, term relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>    getRelatedTerms(String                               userId,
                                                        String                               assetManagerGUID,
                                                        String                               assetManagerName,
                                                        String                               glossaryTermGUID,
                                                        String                               relationshipTypeName,
                                                        List<GlossaryTermRelationshipStatus> limitResultsByStatus,
                                                        int                                  startFrom,
                                                        int                                  pageSize,
                                                        boolean                              forLineage,
                                                        boolean                              forDuplicateProcessing,
                                                        Date                                 effectiveTime,
                                                        String                               methodName) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        List<GlossaryTermElement> results = glossaryTermHandler.getRelatedTerms(userId,
                                                                                glossaryTermGUID,
                                                                                glossaryTermGUIDParameterName,
                                                                                relationshipTypeName,
                                                                                getEnumOrdinals(limitResultsByStatus),
                                                                                startFrom,
                                                                                pageSize,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName);

        this.addCorrelationPropertiesToGlossaryTerms(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }


    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary to query
     * @param name name to search for
     * @param limitResultsByStatus By default, terms in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>   getGlossaryTermsByName(String                   userId,
                                                              String                   assetManagerGUID,
                                                              String                   assetManagerName,
                                                              String                   glossaryGUID,
                                                              String                   name,
                                                              List<GlossaryTermStatus> limitResultsByStatus,
                                                              int                      startFrom,
                                                              int                      pageSize,
                                                              boolean                  forLineage,
                                                              boolean                  forDuplicateProcessing,
                                                              Date                     effectiveTime,
                                                              String                   methodName) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        List<GlossaryTermElement> results = glossaryTermHandler.getTermsByName(userId,
                                                                               glossaryGUID,
                                                                               name,
                                                                               getInstanceStatuses(limitResultsByStatus),
                                                                               startFrom,
                                                                               pageSize,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               effectiveTime,
                                                                               methodName);

        this.addCorrelationPropertiesToGlossaryTerms(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }


    /**
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param guid unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElement getGlossaryTermByGUID(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  guid,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime,
                                                     String  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String guidParameterName = "guid";

        GlossaryTermElement glossaryTerm = glossaryTermHandler.getTerm(userId,
                                                                       guid,
                                                                       guidParameterName,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime, methodName);

        if (glossaryTerm != null)
        {
            glossaryTerm.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                             guid,
                                                                             guidParameterName,
                                                                             OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                             assetManagerGUID,
                                                                             assetManagerName,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName));
        }

        return glossaryTerm;
    }


    /**
     * Retrieve all the versions of a glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param guid unique identifier of object to retrieve
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFrom the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param oldestFirst  defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<GlossaryTermElement> getGlossaryTermHistory(String                 userId,
                                                            String                 assetManagerGUID,
                                                            String                 assetManagerName,
                                                            String                 guid,
                                                            Date                   fromTime,
                                                            Date                   toTime,
                                                            int                    startFrom,
                                                            int                    pageSize,
                                                            boolean                oldestFirst,
                                                            boolean                forLineage,
                                                            boolean                forDuplicateProcessing,
                                                            Date                   effectiveTime,
                                                            String                 methodName) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String guidParameterName  = "guid";

        HistorySequencingOrder sequencingOrder = HistorySequencingOrder.BACKWARDS;

        if (oldestFirst)
        {
            sequencingOrder = HistorySequencingOrder.FORWARDS;
        }
        List<GlossaryTermElement> results = glossaryTermHandler.getBeanHistory(userId,
                                                                               guid,
                                                                               guidParameterName,
                                                                               OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                               fromTime,
                                                                               toTime,
                                                                               startFrom,
                                                                               pageSize,
                                                                               sequencingOrder,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               glossaryTermHandler.getSupportedZones(),
                                                                               effectiveTime,
                                                                               methodName);

        this.addCorrelationPropertiesToGlossaryTerms(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of object to retrieve
     * @param startFrom the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<GlossaryTermElement> getMeanings(String                 userId,
                                                 String                 assetManagerGUID,
                                                 String                 assetManagerName,
                                                 String                 glossaryTermGUID,
                                                 int                    startFrom,
                                                 int                    pageSize,
                                                 boolean                forLineage,
                                                 boolean                forDuplicateProcessing,
                                                 Date                   effectiveTime,
                                                 String                 methodName) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String guidParameterName  = "glossaryTermGUID";

        List<GlossaryTermElement> results = glossaryTermHandler.getMeanings(userId,
                                                                            glossaryTermGUID,
                                                                            guidParameterName,
                                                                            OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                            startFrom,
                                                                            pageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveTime,
                                                                            methodName);

        this.addCorrelationPropertiesToGlossaryTerms(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     results,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);

        return results;
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
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param linkProperties properties of the link
     * @param methodName calling method
     *
     * @return unique identifier of the external reference
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createExternalGlossaryLink(String                         userId,
                                             String                         assetManagerGUID,
                                             String                         assetManagerName,
                                             ExternalGlossaryLinkProperties linkProperties,
                                             String                         methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        // todo
        return null;
    }


    /**
     * Update the properties of a reference to an external glossary resource.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param linkProperties properties of the link
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateExternalGlossaryLink(String                         userId,
                                           String                         assetManagerGUID,
                                           String                         assetManagerName,
                                           String                         externalLinkGUID,
                                           ExternalGlossaryLinkProperties linkProperties,
                                           boolean                        forLineage,
                                           boolean                        forDuplicateProcessing,
                                           Date                           effectiveTime,
                                           String                         methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        // todo
    }


    /**
     * Remove information about a link to an external glossary resource (and the relationships that attached it to the glossaries).
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeExternalGlossaryLink(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  externalLinkGUID,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        // todo
    }


    /**
     * Connect a glossary to a reference to an external glossary resource.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to attach
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void attachExternalLinkToGlossary(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  glossaryGUID,
                                             String  externalLinkGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        // todo
    }


    /**
     * Disconnect a glossary from a reference to an external glossary resource.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void detachExternalLinkFromGlossary(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  glossaryGUID,
                                               String  externalLinkGUID,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        // todo
    }


    /**
     * Retrieve the list of links to external glossary resources attached to a glossary.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the metadata element for the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of attached links to external glossary resources
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ExternalGlossaryLinkElement> getExternalLinksForGlossary(String  userId,
                                                                         String  glossaryGUID,
                                                                         int     startFrom,
                                                                         int     pageSize,
                                                                         boolean forLineage,
                                                                         boolean forDuplicateProcessing,
                                                                         Date    effectiveTime,
                                                                         String  methodName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        // todo
        return null;
    }


    /**
     * Return the glossaries connected to an external glossary source.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the metadata element for the external glossary link of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of glossaries
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement> getGlossariesForExternalLink(String  userId,
                                                              String  assetManagerGUID,
                                                              String  assetManagerName,
                                                              String  externalLinkGUID,
                                                              int     startFrom,
                                                              int     pageSize,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime,
                                                              String  methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        // todo
        return null;
    }


    /**
     * Create a link to an external glossary category resource.  This is associated with a category to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the glossary category
     * @param linkProperties properties of the link
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void attachExternalCategoryLink(String                                userId,
                                           String                                assetManagerGUID,
                                           String                                assetManagerName,
                                           String                                glossaryCategoryGUID,
                                           String                                externalLinkGUID,
                                           ExternalGlossaryElementLinkProperties linkProperties,
                                           boolean                               forLineage,
                                           boolean                               forDuplicateProcessing,
                                           Date                                  effectiveTime,
                                           String                                methodName) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        // todo
    }


    /**
     * Remove the link to an external glossary category resource.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the glossary category
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void detachExternalCategoryLink(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  glossaryCategoryGUID,
                                           String  externalLinkGUID,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        // todo
    }


    /**
     * Create a link to an external glossary term resource.  This is associated with a term to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the glossary category
     * @param linkProperties properties of the link
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void attachExternalTermLink(String                                userId,
                                       String                                assetManagerGUID,
                                       String                                assetManagerName,
                                       String                                glossaryTermGUID,
                                       String                                externalLinkGUID,
                                       ExternalGlossaryElementLinkProperties linkProperties,
                                       boolean                               forLineage,
                                       boolean                               forDuplicateProcessing,
                                       Date                                  effectiveTime,
                                       String                                methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        // todo
    }


    /**
     * Remove the link to an external glossary term resource.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the glossary category
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void detachExternalTermLink(String  userId,
                                       String  assetManagerGUID,
                                       String  assetManagerName,
                                       String  glossaryTermGUID,
                                       String  externalLinkGUID,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        // todo
    }
}
