/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GlossaryHandler provides the exchange of metadata about glossaries between the repository and the OMAS.
 * Note glossaries are governance metadata and are always defined with LOCAL-COHORT provenance.
 *
 * @param <B> class that represents the glossary
 */
public class GlossaryHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from
     * @param defaultZones list of zones that the access service should set in all new B instances
     * @param publishZones list of zones that the access service sets up in published B instances
     * @param auditLog destination for audit log events
     */
    public GlossaryHandler(OpenMetadataAPIGenericConverter<B> converter,
                           Class<B>                           beanClass,
                           String                             serviceName,
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
        super(converter,
              beanClass,
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


    /**
     * Create the anchor object that all elements in a glossary (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param qualifiedName unique name for the glossary - used in other configuration
     * @param displayName short display name for the glossary
     * @param description description of the governance glossary
     * @param language the language used in the glossary definitions
     * @param usage intended usage of the glossary
     * @param additionalProperties additional properties for a glossary
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance glossary subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new glossary object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createGlossary(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              qualifiedName,
                                 String              displayName,
                                 String              description,
                                 String              language,
                                 String              usage,
                                 Map<String, String> additionalProperties,
                                 String              suppliedTypeName,
                                 Map<String, Object> extendedProperties,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.GLOSSARY.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GLOSSARY.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GlossaryBuilder builder = new GlossaryBuilder(qualifiedName,
                                                      displayName,
                                                      description,
                                                      language,
                                                      usage,
                                                      additionalProperties,
                                                      typeGUID,
                                                      typeName,
                                                      extendedProperties,
                                                      repositoryHelper,
                                                      serviceName,
                                                      serverName);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           null,
                                           methodName);
    }


    /**
     * Create a new metadata element to represent a glossary using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is used as a template, any linked terms and categories are created as well.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the new element - used in other configuration
     * @param displayName short display name for the new element
     * @param description description of the new element
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryFromTemplate(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  templateGUID,
                                             String  qualifiedName,
                                             String  displayName,
                                             String  description,
                                             boolean deepCopy,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        GlossaryBuilder builder = new GlossaryBuilder(qualifiedName,
                                                      displayName,
                                                      description,
                                                      repositoryHelper,
                                                      serviceName,
                                                      serverName);

        builder.setAnchors(userId,
                           null,
                           OpenMetadataType.GLOSSARY.typeName,
                           OpenMetadataType.GLOSSARY.typeName,
                           null,
                           methodName);

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataType.GLOSSARY.typeGUID,
                                           OpenMetadataType.GLOSSARY.typeName,
                                           qualifiedName,
                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                           builder,
                                           supportedZones,
                                           deepCopy,
                                           false,
                                           null,
                                           methodName);
    }


    /**
     * Update the anchor object that all elements in a glossary (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of the glossary to update
     * @param glossaryGUIDParameterName parameter passing the glossaryGUID
     * @param qualifiedName unique name for the glossary - used in other configuration
     * @param displayName short display name for the glossary
     * @param description description of the governance glossary
     * @param language the language used in the glossary definitions
     * @param usage intended usage of the glossary
     * @param additionalProperties additional properties for a governance glossary
     * @param suppliedTypeName type of glossary
     * @param extendedProperties  properties for a governance glossary subtype
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateGlossary(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              glossaryGUID,
                                 String              glossaryGUIDParameterName,
                                 String              qualifiedName,
                                 String              displayName,
                                 String              description,
                                 String              language,
                                 String              usage,
                                 Map<String, String> additionalProperties,
                                 String              suppliedTypeName,
                                 Map<String, Object> extendedProperties,
                                 boolean             isMergeUpdate,
                                 boolean             forLineage,
                                 boolean             forDuplicateProcessing,
                                 Date                effectiveTime,
                                 String              methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        String typeName = OpenMetadataType.GLOSSARY.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GLOSSARY.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GlossaryBuilder builder = new GlossaryBuilder(qualifiedName,
                                                      displayName,
                                                      description,
                                                      language,
                                                      usage,
                                                      additionalProperties,
                                                      typeGUID,
                                                      typeName,
                                                      extendedProperties,
                                                      repositoryHelper,
                                                      serviceName,
                                                      serverName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    glossaryGUID,
                                    glossaryGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Mark the glossary as an editing glossary.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of asset
     * @param glossaryGUIDParameterName parameter name supplying glossaryGUID
     * @param description how the category hierarchy is organized
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addEditingGlossaryClassificationToGlossary(String  userId,
                                                            String  externalSourceGUID,
                                                            String  externalSourceName,
                                                            String  glossaryGUID,
                                                            String  glossaryGUIDParameterName,
                                                            String  description,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        GlossaryBuilder builder = new GlossaryBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryGUID,
                                           glossaryGUIDParameterName,
                                           OpenMetadataType.GLOSSARY.typeName,
                                           OpenMetadataType.EDITING_GLOSSARY_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.EDITING_GLOSSARY_CLASSIFICATION.typeName,
                                           builder.getEditingGlossaryProperties(description, methodName),
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the editing glossary designation from a glossary.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of asset
     * @param glossaryGUIDParameterName parameter name supplying glossaryGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeEditingGlossaryClassificationFromGlossary(String  userId,
                                                                 String  externalSourceGUID,
                                                                 String  externalSourceName,
                                                                 String  glossaryGUID,
                                                                 String  glossaryGUIDParameterName,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing,
                                                                 Date    effectiveTime,
                                                                 String  methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryGUID,
                                                glossaryGUIDParameterName,
                                                OpenMetadataType.GLOSSARY.typeName,
                                                OpenMetadataType.EDITING_GLOSSARY_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.EDITING_GLOSSARY_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Mark the glossary as a staging glossary.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of asset
     * @param glossaryGUIDParameterName parameter name supplying glossaryGUID
     * @param description how the category hierarchy is organized
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addStagingGlossaryClassificationToGlossary(String  userId,
                                                            String  externalSourceGUID,
                                                            String  externalSourceName,
                                                            String  glossaryGUID,
                                                            String  glossaryGUIDParameterName,
                                                            String  description,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        GlossaryBuilder builder = new GlossaryBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryGUID,
                                           glossaryGUIDParameterName,
                                           OpenMetadataType.GLOSSARY.typeName,
                                           OpenMetadataType.STAGING_GLOSSARY_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.STAGING_GLOSSARY_CLASSIFICATION.typeName,
                                           builder.getStagingGlossaryProperties(description, methodName),
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the staging glossary designation from a glossary.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of asset
     * @param glossaryGUIDParameterName parameter name supplying glossaryGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeStagingGlossaryClassificationFromGlossary(String  userId,
                                                                 String  externalSourceGUID,
                                                                 String  externalSourceName,
                                                                 String  glossaryGUID,
                                                                 String  glossaryGUIDParameterName,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing,
                                                                 Date    effectiveTime,
                                                                 String  methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryGUID,
                                                glossaryGUIDParameterName,
                                                OpenMetadataType.GLOSSARY.typeName,
                                                OpenMetadataType.STAGING_GLOSSARY_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.STAGING_GLOSSARY_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Mark the glossary as a taxonomy.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of asset
     * @param glossaryGUIDParameterName parameter name supplying glossaryGUID
     * @param organizingPrinciple how the category hierarchy is organized
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addTaxonomyClassificationToGlossary(String  userId,
                                                     String  externalSourceGUID,
                                                     String  externalSourceName,
                                                     String  glossaryGUID,
                                                     String  glossaryGUIDParameterName,
                                                     String  organizingPrinciple,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime,
                                                     String  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        GlossaryBuilder builder = new GlossaryBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryGUID,
                                           glossaryGUIDParameterName,
                                           OpenMetadataType.GLOSSARY.typeName,
                                           OpenMetadataType.TAXONOMY_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.TAXONOMY_CLASSIFICATION.typeName,
                                           builder.getTaxonomyProperties(organizingPrinciple, methodName),
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the taxonomy designation from a glossary.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of asset
     * @param glossaryGUIDParameterName parameter name supplying glossaryGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeTaxonomyClassificationFromGlossary(String  userId,
                                                          String  externalSourceGUID,
                                                          String  externalSourceName,
                                                          String  glossaryGUID,
                                                          String  glossaryGUIDParameterName,
                                                          boolean forLineage,
                                                          boolean forDuplicateProcessing,
                                                          Date    effectiveTime,
                                                          String  methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryGUID,
                                                glossaryGUIDParameterName,
                                                OpenMetadataType.GLOSSARY.typeName,
                                                OpenMetadataType.TAXONOMY_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.TAXONOMY_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Mark the glossary as a canonical vocabulary.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of asset
     * @param glossaryGUIDParameterName parameter name supplying glossaryGUID
     * @param scope how the category hierarchy is organized
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addCanonicalVocabClassificationToGlossary(String  userId,
                                                           String  externalSourceGUID,
                                                           String  externalSourceName,
                                                           String  glossaryGUID,
                                                           String  glossaryGUIDParameterName,
                                                           String  scope,
                                                           boolean forLineage,
                                                           boolean forDuplicateProcessing,
                                                           Date    effectiveTime,
                                                           String  methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        GlossaryBuilder builder = new GlossaryBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryGUID,
                                           glossaryGUIDParameterName,
                                           OpenMetadataType.GLOSSARY.typeName,
                                           OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName,
                                           builder.getCanonicalVocabularyProperties(scope, methodName),
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the taxonomy designation from a glossary.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of asset
     * @param glossaryGUIDParameterName parameter name supplying glossaryGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCanonicalVocabClassificationFromGlossary(String  userId,
                                                                String  externalSourceGUID,
                                                                String  externalSourceName,
                                                                String  glossaryGUID,
                                                                String  glossaryGUIDParameterName,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime,
                                                                String  methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryGUID,
                                                glossaryGUIDParameterName,
                                                OpenMetadataType.GLOSSARY.typeName,
                                                OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories and terms because
     * the Anchors classifications are set up in these elements.  The external source values are passed in case they are needed
     * for the removal of nested terms/categories.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryGUIDParameterName parameter supplying the glossaryGUID
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossary(String  userId,
                               String  externalSourceGUID,
                               String  externalSourceName,
                               String  glossaryGUID,
                               String  glossaryGUIDParameterName,
                               boolean cascadedDelete,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        EntityDetail glossaryEntity = this.getEntityFromRepository(userId,
                                                                   glossaryGUID,
                                                                   glossaryGUIDParameterName,
                                                                   OpenMetadataType.GLOSSARY.typeName,
                                                                   null,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);

        if (glossaryEntity != null)
        {
            this.deleteBeanInRepository(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY.typeGUID,
                                        OpenMetadataType.GLOSSARY.typeName,
                                        cascadedDelete,
                                        null,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
        }
    }


    /**
     * Retrieve the list of glossary metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findGlossaries(String  userId,
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
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EntityDetail> glossaryEntities = repositoryHandler.getEntitiesByValue(userId,
                                                                                   searchString,
                                                                                   OpenMetadataType.GLOSSARY.typeGUID,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   SequencingOrder.CREATION_DATE_RECENT,
                                                                                   null,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   startFrom,
                                                                                   queryPageSize,
                                                                                   effectiveTime,
                                                                                   methodName);

        return getGlossaryBeans(userId,
                                glossaryEntities,
                                forLineage,
                                forDuplicateProcessing,
                                effectiveTime);
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getGlossariesByName(String  userId,
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
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        InstanceProperties matchProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          null,
                                                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                          name,
                                                                                          methodName);
        matchProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                       matchProperties,
                                                                       OpenMetadataProperty.DISPLAY_NAME.name,
                                                                       name,
                                                                       methodName);

        List<EntityDetail> glossaryEntities = repositoryHandler.getEntitiesByName(userId,
                                                                                  matchProperties,
                                                                                  OpenMetadataType.GLOSSARY.typeGUID,
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  startFrom,
                                                                                  queryPageSize,
                                                                                  effectiveTime,
                                                                                  methodName);

        return getGlossaryBeans(userId,
                                glossaryEntities,
                                forLineage,
                                forDuplicateProcessing,
                                effectiveTime);
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGlossaryByGUID(String  userId,
                               String  guid,
                               String  guidParameterName,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           guid,
                                                           guidParameterName,
                                                           OpenMetadataType.GLOSSARY.typeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        return this.getGlossaryBean(userId, entity, null, forLineage, forDuplicateProcessing, effectiveTime);
    }




    /**
     * Convert the returned glossary entities into an element.
     *
     * @param userId calling user
     * @param glossaryEntities entities returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return list of glossary term elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private List<B> getGlossaryBeans(String             userId,
                                     List<EntityDetail> glossaryEntities,
                                     boolean            forLineage,
                                     boolean            forDuplicateProcessing,
                                     Date               effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        if (glossaryEntities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entityDetail : glossaryEntities)
            {
                if (entityDetail != null)
                {
                    results.add(this.getGlossaryBean(userId, entityDetail, null, forLineage, forDuplicateProcessing, effectiveTime));
                }
            }

            return results;
        }

        return null;
    }



    /**
     * Convert the returned glossary  entity into an element.
     *
     * @param userId calling user
     * @param categoryEntity entity returned
     * @param relatedByRelationship optional relationship used to find entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return list of glossary term elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private B getGlossaryBean(String       userId,
                              EntityDetail categoryEntity,
                              Relationship relatedByRelationship,
                              boolean      forLineage,
                              boolean      forDuplicateProcessing,
                              Date         effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "getGlossaryBean";
        final String startingGUIDParameterName = "glossaryEntity.guid";

        if (categoryEntity != null)
        {
            List<RelatedEntity> relatedEntities = this.getAllRelatedEntities(userId,
                                                                             categoryEntity,
                                                                             startingGUIDParameterName,
                                                                             OpenMetadataType.GLOSSARY.typeName,
                                                                             null,
                                                                             null,
                                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                                             null,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName);

            if (relatedEntities != null)
            {
                List<RelatedEntity> categoryLinks = new ArrayList<>();

                final String categoryGUIDParameterName = "ownedCategory.guid";
                for (RelatedEntity relatedEntity : relatedEntities)
                {
                    /*
                     * In order to build the category hierarchy it is necessary to retrieve the CategoryHierarchyLink
                     * relationships for each category.  It is done by retrieving the super category relationship for
                     * all retrieved categories.
                     */
                    if ((relatedEntity != null) &&
                            (relatedEntity.relationship() != null) &&
                            (relatedEntity.entityDetail() != null) &&
                            (repositoryHelper.isTypeOf(serviceName,
                                                       relatedEntity.relationship().getType().getTypeDefName(),
                                                       OpenMetadataType.CATEGORY_ANCHOR_RELATIONSHIP.typeName)))
                    {
                        List<RelatedEntity> relatedCategories = this.getRelatedEntities(userId,
                                                                                        relatedEntity.entityDetail(),
                                                                                        categoryGUIDParameterName,
                                                                                        OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                                                                        OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeGUID,
                                                                                        OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeName,
                                                                                        null,
                                                                                        OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                                                                        1,
                                                                                        null,
                                                                                        null,
                                                                                        SequencingOrder.CREATION_DATE_RECENT,
                                                                                        null,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        supportedZones,
                                                                                        0,
                                                                                        invalidParameterHandler.getMaxPagingSize(),
                                                                                        effectiveTime,
                                                                                        methodName);

                        if (relatedCategories != null)
                        {
                            categoryLinks.addAll(relatedCategories);
                        }
                    }
                }

                relatedEntities.addAll(categoryLinks);
            }

            return converter.getNewComplexBean(beanClass, categoryEntity, relatedByRelationship, relatedEntities, methodName);
        }

        return null;
    }


    /**
     * Retrieve the glossary metadata element for the requested category.
     *
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param guidParameterName parameter name of glossaryCategoryGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGlossaryForCategory(String  userId,
                                    String  glossaryCategoryGUID,
                                    String  guidParameterName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        return this.getAttachedElement(userId,
                                       glossaryCategoryGUID,
                                       guidParameterName,
                                       OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                       OpenMetadataType.CATEGORY_ANCHOR_RELATIONSHIP.typeGUID,
                                       OpenMetadataType.CATEGORY_ANCHOR_RELATIONSHIP.typeName,
                                       OpenMetadataType.GLOSSARY.typeName,
                                       1,
                                       null,
                                       null,
                                       SequencingOrder.CREATION_DATE_RECENT,
                                       null,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Retrieve the glossary metadata element for the requested term.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the requested metadata element
     * @param guidParameterName parameter name of glossaryTermGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGlossaryForTerm(String  userId,
                                String  glossaryTermGUID,
                                String  guidParameterName,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return this.getAttachedElement(userId,
                                       glossaryTermGUID,
                                       guidParameterName,
                                       OpenMetadataType.GLOSSARY_TERM.typeName,
                                       OpenMetadataType.TERM_ANCHOR_RELATIONSHIP.typeGUID,
                                       OpenMetadataType.TERM_ANCHOR_RELATIONSHIP.typeName,
                                       OpenMetadataType.GLOSSARY.typeName,
                                       1,
                                       null,
                                       null,
                                       SequencingOrder.CREATION_DATE_RECENT,
                                       null,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       effectiveTime,
                                       methodName);
    }
}
