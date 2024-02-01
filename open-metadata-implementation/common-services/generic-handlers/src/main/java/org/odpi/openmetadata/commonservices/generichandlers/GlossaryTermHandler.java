/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryIteratorForEntities;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * GlossaryTermHandler retrieves Glossary Term objects from the property server.  It runs server-side
 * and retrieves Glossary Term entities through the OMRSRepositoryConnector.
 *
 * @param <B> class for the glossary term bean
 */
public class GlossaryTermHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the glossary term handler caching the objects needed to operate within a single server instance.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public GlossaryTermHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Return the list of term-to-term relationship names.
     *
     * @return list of type names that are subtypes of asset
     */
    public List<String> getTermRelationshipTypeNames()
    {
        List<TypeDef>  knownTypeDefs = repositoryHelper.getKnownTypeDefs();
        List<String>   relationshipNames = new ArrayList<>();
        if (knownTypeDefs != null)
        {
            for (TypeDef typeDef : knownTypeDefs)
            {
                if ((typeDef != null) && (typeDef.getStatus() == TypeDefStatus.ACTIVE_TYPEDEF) && (typeDef instanceof RelationshipDef relationshipDef))
                {
                    if ((relationshipDef.getEndDef1().getEntityType().getName().equals(OpenMetadataType.GLOSSARY_TERM_TYPE_NAME)) &&
                        (relationshipDef.getEndDef2().getEntityType().getName().equals(OpenMetadataType.GLOSSARY_TERM_TYPE_NAME)))
                    {
                        relationshipNames.add(relationshipDef.getName());
                    }
                }
            }
        }

       return relationshipNames;
    }


    /**
     * Create a new metadata element to represent a glossary term (or a subtype).
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of the owning glossary
     * @param glossaryGUIDParameterName parameter supplying glossaryGUID
     * @param qualifiedName unique name for the category - used in other configuration
     * @param displayName  display name for the term
     * @param summary short description
     * @param description description of the term
     * @param examples examples of this term
     * @param abbreviation abbreviation used for the term
     * @param usage illustrations of how the term is used
     * @param publishVersionIdentifier user control version identifier
     * @param additionalProperties additional properties for a term
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a term subtype
     * @param initialStatus glossary term status to use when the object is created
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryTerm(String              userId,
                                     String              externalSourceGUID,
                                     String              externalSourceName,
                                     String              glossaryGUID,
                                     String              glossaryGUIDParameterName,
                                     String              qualifiedName,
                                     String              displayName,
                                     String              summary,
                                     String              description,
                                     String              examples,
                                     String              abbreviation,
                                     String              usage,
                                     String              publishVersionIdentifier,
                                     Map<String, String> additionalProperties,
                                     String              suppliedTypeName,
                                     Map<String, Object> extendedProperties,
                                     InstanceStatus      initialStatus,
                                     Date                effectiveFrom,
                                     Date                effectiveTo,
                                     Date                effectiveTime,
                                     String              methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }
        else if ((initialStatus == null) || (initialStatus == InstanceStatus.ACTIVE))
        {
            typeName = OpenMetadataType.GLOSSARY_TERM_TYPE_NAME;
        }
        else
        {
            typeName = OpenMetadataType.CONTROLLED_GLOSSARY_TERM_TYPE_NAME;
        }

        TypeDef glossaryTypeDef = invalidParameterHandler.validateTypeDefName(typeName,
                                                                              OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                              serviceName,
                                                                              methodName,
                                                                              repositoryHelper);

        InstanceStatus instanceStatus;

        if (initialStatus != null)
        {
            instanceStatus = initialStatus;
        }
        else
        {
            instanceStatus = glossaryTypeDef.getInitialStatus();
        }

        GlossaryTermBuilder builder = new GlossaryTermBuilder(qualifiedName,
                                                              displayName,
                                                              summary,
                                                              description,
                                                              examples,
                                                              abbreviation,
                                                              usage,
                                                              publishVersionIdentifier,
                                                              additionalProperties,
                                                              extendedProperties,
                                                              instanceStatus,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    glossaryGUID,
                                    glossaryGUIDParameterName,
                                    false,
                                    false,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        String glossaryTermGUID = this.createBeanInRepository(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              glossaryTypeDef.getGUID(),
                                                              glossaryTypeDef.getName(),
                                                              builder,
                                                              effectiveTime,
                                                              methodName);

        if (glossaryTermGUID != null)
        {
            /*
             * Link the term to its glossary.  This relationship is always effective.
             */
            final String glossaryTermGUIDParameterName = "glossaryTermGUID";

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               glossaryGUID,
                                               glossaryGUIDParameterName,
                                               OpenMetadataType.GLOSSARY_TYPE_NAME,
                                               glossaryTermGUID,
                                               glossaryTermGUIDParameterName,
                                               OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                               false,
                                               false,
                                               supportedZones,
                                               OpenMetadataType.TERM_ANCHOR_TYPE_GUID,
                                               OpenMetadataType.TERM_ANCHOR_TYPE_NAME,
                                               null,
                                               effectiveTime,
                                               methodName);
        }

        return glossaryTermGUID;
    }


    /**
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryGUID unique identifier of the owning glossary
     * @param glossaryGUIDParameterName parameter supplying glossaryGUID
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the term - used in other configuration
     * @param displayName short display name for the term
     * @param description description of the  term
     * @param publishVersionIdentifier author controlled version identifier
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
    public String createGlossaryTermFromTemplate(String         userId,
                                                 String         externalSourceGUID,
                                                 String         externalSourceName,
                                                 String         glossaryGUID,
                                                 String         glossaryGUIDParameterName,
                                                 String         templateGUID,
                                                 String         qualifiedName,
                                                 String         displayName,
                                                 String         description,
                                                 String         publishVersionIdentifier,
                                                 InstanceStatus initialStatus,
                                                 boolean        deepCopy,
                                                 boolean        templateSubstitute,
                                                 String         methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        GlossaryTermBuilder builder = new GlossaryTermBuilder(qualifiedName,
                                                              displayName,
                                                              description,
                                                              publishVersionIdentifier,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    glossaryGUID,
                                    glossaryGUIDParameterName,
                                    false,
                                    false,
                                    null,
                                    supportedZones,
                                    builder,
                                    methodName);

        String glossaryTermGUID = this.createBeanFromTemplate(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              templateGUID,
                                                              templateGUIDParameterName,
                                                              OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
                                                              OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                              qualifiedName,
                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                              builder,
                                                              supportedZones,
                                                              deepCopy,
                                                              templateSubstitute,
                                                              methodName);

        if (glossaryTermGUID != null)
        {
            final String glossaryTermGUIDParameterName = "glossaryTermGUID";

            if (initialStatus != null)
            {
                this.updateBeanStatusInRepository(userId,
                                                  externalSourceGUID,
                                                  externalSourceName,
                                                  glossaryTermGUID,
                                                  glossaryTermGUIDParameterName,
                                                  OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
                                                  OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                  false,
                                                  false,
                                                  initialStatus,
                                                  "initialStatus",
                                                  null,
                                                  methodName);
            }

            /*
             * Link the term to its glossary.  This relationship is always effective.
             */
            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               glossaryGUID,
                                               glossaryGUIDParameterName,
                                               OpenMetadataType.GLOSSARY_TYPE_NAME,
                                               glossaryTermGUID,
                                               glossaryTermGUIDParameterName,
                                               OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                               false,
                                               false,
                                               supportedZones,
                                               OpenMetadataType.TERM_ANCHOR_TYPE_GUID,
                                               OpenMetadataType.TERM_ANCHOR_TYPE_NAME,
                                               null,
                                               null,
                                               methodName);
        }

        return glossaryTermGUID;
    }


    /**
     * Update the properties of the metadata element representing a glossary term.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryGUID
     * @param qualifiedName unique name for the category - used in other configuration
     * @param displayName short display name for the term
     * @param summary string text
     * @param description description of the  term
     * @param examples string text
     * @param abbreviation string text
     * @param usage string text
     * @param publishVersionIdentifier user-controlled version identifier
     * @param additionalProperties additional properties for a term
     * @param typeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a term subtype
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTerm(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              glossaryTermGUID,
                                   String              glossaryTermGUIDParameterName,
                                   String              qualifiedName,
                                   String              displayName,
                                   String              summary,
                                   String              description,
                                   String              examples,
                                   String              abbreviation,
                                   String              usage,
                                   String              publishVersionIdentifier,
                                   Map<String, String> additionalProperties,
                                   String              typeName,
                                   Map<String, Object> extendedProperties,
                                   Date                effectiveFrom,
                                   Date                effectiveTo,
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
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);
        if (!isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GlossaryTermBuilder builder = new GlossaryTermBuilder(qualifiedName,
                                                              displayName,
                                                              summary,
                                                              description,
                                                              examples,
                                                              abbreviation,
                                                              usage,
                                                              publishVersionIdentifier,
                                                              additionalProperties,
                                                              extendedProperties,
                                                              InstanceStatus.ACTIVE,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    glossaryTermGUID,
                                    glossaryTermGUIDParameterName,
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
     * Update the status of the metadata element representing a glossary term.  This is only valid on
     * a controlled glossary term.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermGUIDParameterName parameter name for glossaryTermGUID
     * @param glossaryTermStatus new status value for the glossary term
     * @param glossaryTermStatusParameterName parameter name for the status value
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTermStatus(String         userId,
                                         String         externalSourceGUID,
                                         String         externalSourceName,
                                         String         glossaryTermGUID,
                                         String         glossaryTermGUIDParameterName,
                                         InstanceStatus glossaryTermStatus,
                                         String         glossaryTermStatusParameterName,
                                         Date           effectiveTime,
                                         boolean        forLineage,
                                         boolean        forDuplicateProcessing,
                                         String         methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        this.updateBeanStatusInRepository(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          glossaryTermGUID,
                                          glossaryTermGUIDParameterName,
                                          OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
                                          OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          glossaryTermStatus,
                                          glossaryTermStatusParameterName,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Update the glossary term using the properties and classifications from the parentGUID stored in the request body.
     * This may be turned into a method for generic types in the future.  At the moment it does not traverse the associated anchored elements
     * and so only works for glossary workflow cases.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermGUIDParameterName parameter name for glossaryTermGUID
     * @param templateGUID identifier for the new glossary
     * @param templateGUIDParameterName parameter name for the templateGUID value
     * @param isMergeClassifications should the classification be merged or replace the target entity?
     * @param isMergeProperties should the properties be merged with the existing ones or replace them
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTermFromTemplate(String         userId,
                                               String         externalSourceGUID,
                                               String         externalSourceName,
                                               String         glossaryTermGUID,
                                               String         glossaryTermGUIDParameterName,
                                               String         templateGUID,
                                               String         templateGUIDParameterName,
                                               boolean        isMergeClassifications,
                                               boolean        isMergeProperties,
                                               Date           effectiveTime,
                                               boolean        forLineage,
                                               boolean        forDuplicateProcessing,
                                               String         methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        EntityDetail templateEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        templateGUID,
                                                                        templateGUIDParameterName,
                                                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        EntityDetail termEntity = repositoryHandler.getEntityByGUID(userId,
                                                                    glossaryTermGUID,
                                                                    glossaryTermGUIDParameterName,
                                                                    OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);

        if (templateEntity != null)
        {
            InstanceProperties templateProperties = null;
            if (templateEntity.getProperties() != null)
            {
                templateProperties = new InstanceProperties();

                templateProperties.setEffectiveFromTime(templateEntity.getProperties().getEffectiveFromTime());
                templateProperties.setEffectiveToTime(templateEntity.getProperties().getEffectiveToTime());

                if (templateEntity.getProperties().getPropertyCount() > 0)
                {
                    Iterator<String> propertyNames = templateEntity.getProperties().getPropertyNames();

                    while (propertyNames.hasNext())
                    {
                        String propertyName = propertyNames.next();

                        /*
                         * Ignore qualified name.
                         */
                        if (! OpenMetadataProperty.QUALIFIED_NAME.name.equals(propertyName))
                        {
                            templateProperties.setProperty(propertyName, templateEntity.getProperties().getPropertyValue(propertyName));
                        }
                    }
                }
            }

            this.updateBeanInRepository(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        termEntity,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        templateProperties,
                                        isMergeProperties,
                                        effectiveTime,
                                        methodName);

            List<String> updatedClassifications = new ArrayList<>();

            if (templateEntity.getClassifications() != null)
            {
                for (Classification classification : templateEntity.getClassifications())
                {
                    if (! OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(classification.getName()))
                    {
                        this.setClassificationInRepository(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           termEntity,
                                                           glossaryTermGUIDParameterName,
                                                           OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                           classification.getType().getTypeDefGUID(),
                                                           classification.getType().getTypeDefName(),
                                                           classification.getProperties(),
                                                           isMergeProperties,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           supportedZones,
                                                           effectiveTime,
                                                           methodName);

                        updatedClassifications.add(classification.getName());
                    }
                }
            }

            if ((! isMergeClassifications) && (termEntity.getClassifications() != null))
            {
                for (Classification classification : termEntity.getClassifications())
                {
                    if ((! OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(classification.getName())) &&
                                (! updatedClassifications.contains(classification.getName())))
                    {
                        this.removeClassificationFromRepository(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                termEntity.getGUID(),
                                                                glossaryTermGUIDParameterName,
                                                                OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                classification.getType().getTypeDefGUID(),
                                                                classification.getType().getTypeDefName(),
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                supportedZones,
                                                                effectiveTime,
                                                                methodName);
                    }
                }
            }
        }
    }


    /**
     * Move a glossary term from one glossary to another.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermGUIDParameterName parameter name for glossaryTermGUID
     * @param newGlossaryGUID identifier for the new glossary
     * @param newGlossaryGUIDParameterName parameter name for the newGlossaryGUID value
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void moveGlossaryTerm(String         userId,
                                 String         externalSourceGUID,
                                 String         externalSourceName,
                                 String         glossaryTermGUID,
                                 String         glossaryTermGUIDParameterName,
                                 String         newGlossaryGUID,
                                 String         newGlossaryGUIDParameterName,
                                 Date           effectiveTime,
                                 boolean        forLineage,
                                 boolean        forDuplicateProcessing,
                                 String         methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        EntityDetail termEntity = repositoryHandler.getEntityByGUID(userId,
                                                                    glossaryTermGUID,
                                                                    glossaryTermGUIDParameterName,
                                                                    OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);

        List<Relationship> termAnchors = this.getAttachmentLinks(userId,
                                                                 termEntity.getGUID(),
                                                                 glossaryTermGUIDParameterName,
                                                                 OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                 OpenMetadataType.TERM_ANCHOR_TYPE_GUID,
                                                                 OpenMetadataType.TERM_ANCHOR_TYPE_NAME,
                                                                 null,
                                                                 OpenMetadataType.GLOSSARY_TYPE_NAME,
                                                                 1,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 0,
                                                                 0,
                                                                 effectiveTime,
                                                                 methodName);

        /*
         * Remove the current term anchor (should only be one but this cleans up any extra relationships).
         */
        if (termAnchors != null)
        {
            for (Relationship termAnchor : termAnchors)
            {
                if (termAnchor != null)
                {
                    repositoryHandler.removeRelationship(userId, externalSourceGUID, externalSourceName, termAnchor, methodName);
                }
            }
        }

        /*
         * Remove the original anchor.
         */
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                termEntity.getGUID(),
                                                glossaryTermGUIDParameterName,
                                                OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                OpenMetadataType.ANCHORS_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);

        /*
         * This will set up the correct anchor
         */
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  newGlossaryGUID,
                                  newGlossaryGUIDParameterName,
                                  OpenMetadataType.GLOSSARY_TYPE_NAME,
                                  termEntity.getGUID(),
                                  glossaryTermGUIDParameterName,
                                  OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.TERM_ANCHOR_TYPE_GUID,
                                  OpenMetadataType.TERM_ANCHOR_TYPE_NAME,
                                  null,
                                  null,
                                  null,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Link a term to a category.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryCategoryGUIDParameterName parameter supplying glossaryCategoryGUID
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param description description of the categorization
     * @param relationshipStatus ordinal for the relationship status enum
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupTermCategory(String  userId,
                                  String  externalSourceGUID,
                                  String  externalSourceName,
                                  String  glossaryCategoryGUID,
                                  String  glossaryCategoryGUIDParameterName,
                                  String  glossaryTermGUID,
                                  String  glossaryTermGUIDParameterName,
                                  String  description,
                                  int     relationshipStatus,
                                  Date    effectiveFrom,
                                  Date    effectiveTo,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryCategoryGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        GlossaryTermBuilder builder = new GlossaryTermBuilder(repositoryHelper, serviceName, serverName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  glossaryCategoryGUID,
                                  glossaryCategoryGUIDParameterName,
                                  OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                  glossaryTermGUID,
                                  glossaryTermGUIDParameterName,
                                  OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.TERM_CATEGORIZATION_TYPE_GUID,
                                  OpenMetadataType.TERM_CATEGORIZATION_TYPE_NAME,
                                  builder.getTermCategorizationProperties(description, relationshipStatus, methodName),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Unlink a term from a category.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryCategoryGUIDParameterName parameter supplying glossaryCategoryGUID
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermCategory(String  userId,
                                  String  externalSourceGUID,
                                  String  externalSourceName,
                                  String  glossaryCategoryGUID,
                                  String  glossaryCategoryGUIDParameterName,
                                  String  glossaryTermGUID,
                                  String  glossaryTermGUIDParameterName,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryCategoryGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      glossaryCategoryGUID,
                                      glossaryCategoryGUIDParameterName,
                                      OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                      glossaryTermGUID,
                                      glossaryTermGUIDParameterName,
                                      OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
                                      OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.TERM_CATEGORIZATION_TYPE_GUID,
                                      OpenMetadataType.TERM_CATEGORIZATION_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Link two terms together using a specialist relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermOneGUIDParameterName parameter supplying glossaryTermOneGUID
     * @param relationshipTypeName name of the type of relationship to create
     * @param relationshipTypeParameterName name of parameter passing the relationship
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param glossaryTermTwoGUIDParameterName parameter supplying glossaryTermTwoGUID
     * @param description description of the relationship
     * @param expression expression that describes the relationship
     * @param relationshipStatus ordinal for the relationship status enum (draft, active, deprecated, obsolete, other)
     * @param steward user id or name of steward id who assigned the relationship (or approved the discovered value).
     * @param source id of the source of the knowledge of the relationship
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupTermRelationship(String userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  glossaryTermOneGUID,
                                      String  glossaryTermOneGUIDParameterName,
                                      String  relationshipTypeName,
                                      String  relationshipTypeParameterName,
                                      String  glossaryTermTwoGUID,
                                      String  glossaryTermTwoGUIDParameterName,
                                      String  expression,
                                      String  description,
                                      int     relationshipStatus,
                                      String  steward,
                                      String  source,
                                      Date    effectiveFrom,
                                      Date    effectiveTo,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermOneGUID, glossaryTermOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermTwoGUID, glossaryTermTwoGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeParameterName, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                               relationshipTypeName,
                                                                               serviceName,
                                                                               methodName,
                                                                               repositoryHelper);

        GlossaryTermBuilder builder = new GlossaryTermBuilder(repositoryHelper, serviceName, serverName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  glossaryTermOneGUID,
                                  glossaryTermOneGUIDParameterName,
                                  OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                  glossaryTermTwoGUID,
                                  glossaryTermTwoGUIDParameterName,
                                  OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  relationshipTypeGUID,
                                  relationshipTypeName,
                                  builder.getTermRelationshipProperties(expression,
                                                                        description,
                                                                        relationshipStatus,
                                                                        steward,
                                                                        source,
                                                                        methodName),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermOneGUIDParameterName parameter supplying glossaryTermOneGUID
     * @param relationshipTypeName name of the type of relationship to create
     * @param relationshipTypeParameterName name of parameter passing the relationship
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param glossaryTermTwoGUIDParameterName parameter supplying glossaryTermTwoGUID
     * @param description description of the relationship
     * @param expression expression that describes the relationship
     * @param relationshipStatus ordinal for the relationship status enum (draft, active, deprecated, obsolete, other)
     * @param steward user id or name of steward id who assigned the relationship (or approved the discovered value).
     * @param source id of the source of the knowledge of the relationship
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateTermRelationship(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  glossaryTermOneGUID,
                                       String  glossaryTermOneGUIDParameterName,
                                       String  relationshipTypeName,
                                       String  relationshipTypeParameterName,
                                       String  glossaryTermTwoGUID,
                                       String  glossaryTermTwoGUIDParameterName,
                                       String  expression,
                                       String  description,
                                       int     relationshipStatus,
                                       String  steward,
                                       String  source,
                                       Date    effectiveFrom,
                                       Date    effectiveTo,
                                       boolean isMergeUpdate,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermOneGUID, glossaryTermOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermTwoGUID, glossaryTermTwoGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeParameterName, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                               relationshipTypeName,
                                                                               serviceName,
                                                                               methodName,
                                                                               repositoryHelper);

        GlossaryTermBuilder builder = new GlossaryTermBuilder(repositoryHelper, serviceName, serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateElementToElementLink(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        glossaryTermOneGUID,
                                        glossaryTermOneGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        glossaryTermTwoGUID,
                                        glossaryTermTwoGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        relationshipTypeGUID,
                                        relationshipTypeName,
                                        isMergeUpdate,
                                        builder.getTermRelationshipProperties(expression,
                                                                              description,
                                                                              relationshipStatus,
                                                                              steward,
                                                                              source,
                                                                              methodName),
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermOneGUIDParameterName parameter supplying glossaryTermOneGUID
     * @param relationshipTypeName name of the type of relationship to create
     * @param relationshipTypeParameterName name of parameter passing the relationship
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param glossaryTermTwoGUIDParameterName parameter supplying glossaryTermTwoGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermRelationship(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  glossaryTermOneGUID,
                                      String  glossaryTermOneGUIDParameterName,
                                      String  relationshipTypeName,
                                      String  relationshipTypeParameterName,
                                      String  glossaryTermTwoGUID,
                                      String  glossaryTermTwoGUIDParameterName,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermOneGUID, glossaryTermOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermTwoGUID, glossaryTermTwoGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeParameterName, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                               relationshipTypeName,
                                                                               serviceName,
                                                                               methodName,
                                                                               repositoryHelper);

        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      glossaryTermOneGUID,
                                      glossaryTermOneGUIDParameterName,
                                      OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                      glossaryTermTwoGUID,
                                      glossaryTermTwoGUIDParameterName,
                                      OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
                                      OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      relationshipTypeGUID,
                                      relationshipTypeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsAbstractConcept(String  userId,
                                         String  externalSourceGUID,
                                         String  externalSourceName,
                                         String  glossaryTermGUID,
                                         String  glossaryTermGUIDParameterName,
                                         Date    effectiveFrom,
                                         Date    effectiveTo,
                                         boolean isMergeUpdate,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryTermGUID,
                                           glossaryTermGUIDParameterName,
                                           OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                           OpenMetadataType.ABSTRACT_CONCEPT_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.ABSTRACT_CONCEPT_CLASSIFICATION_TYPE_NAME,
                                           this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsAbstractConcept(String  userId,
                                           String  externalSourceGUID,
                                           String  externalSourceName,
                                           String  glossaryTermGUID,
                                           String  glossaryTermGUIDParameterName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryTermGUID,
                                                glossaryTermGUIDParameterName,
                                                OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                OpenMetadataType.ABSTRACT_CONCEPT_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.ABSTRACT_CONCEPT_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsDataValue(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  glossaryTermGUID,
                                   String  glossaryTermGUIDParameterName,
                                   Date    effectiveFrom,
                                   Date    effectiveTo,
                                   boolean isMergeUpdate,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryTermGUID,
                                           glossaryTermGUIDParameterName,
                                           OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                           OpenMetadataType.DATA_VALUE_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.DATA_VALUE_CLASSIFICATION_TYPE_NAME,
                                           this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsDataValue(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  glossaryTermGUID,
                                     String  glossaryTermGUIDParameterName,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryTermGUID,
                                                glossaryTermGUIDParameterName,
                                                OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                OpenMetadataType.DATA_VALUE_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.DATA_VALUE_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param activityType ordinal for type of activity
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsActivity(String  userId,
                                  String  externalSourceGUID,
                                  String  externalSourceName,
                                  String  glossaryTermGUID,
                                  String  glossaryTermGUIDParameterName,
                                  int     activityType,
                                  Date    effectiveFrom,
                                  Date    effectiveTo,
                                  boolean isMergeUpdate,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        GlossaryTermBuilder builder = new GlossaryTermBuilder(repositoryHelper, serviceName, serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryTermGUID,
                                           glossaryTermGUIDParameterName,
                                           OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                           OpenMetadataType.ACTIVITY_DESC_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.ACTIVITY_DESC_CLASSIFICATION_TYPE_NAME,
                                           builder.getActivityTypeProperties(activityType, methodName),
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsActivity(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  glossaryTermGUID,
                                    String  glossaryTermGUIDParameterName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryTermGUID,
                                                glossaryTermGUIDParameterName,
                                                OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                OpenMetadataType.ACTIVITY_DESC_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.ACTIVITY_DESC_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param description description of the context
     * @param scope the scope of where the context applies
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsContext(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  glossaryTermGUID,
                                 String  glossaryTermGUIDParameterName,
                                 String  description,
                                 String  scope,
                                 Date    effectiveFrom,
                                 Date    effectiveTo,
                                 boolean isMergeUpdate,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        GlossaryTermBuilder builder = new GlossaryTermBuilder(repositoryHelper, serviceName, serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryTermGUID,
                                           glossaryTermGUIDParameterName,
                                           OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                           OpenMetadataType.CONTEXT_DEFINITION_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.CONTEXT_DEFINITION_CLASSIFICATION_TYPE_NAME,
                                           builder.getContextDescriptionProperties(description, scope, methodName),
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsContext(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  glossaryTermGUID,
                                   String  glossaryTermGUIDParameterName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryTermGUID,
                                                glossaryTermGUIDParameterName,
                                                OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                OpenMetadataType.CONTEXT_DEFINITION_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.CONTEXT_DEFINITION_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine object.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsSpineObject(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  glossaryTermGUID,
                                     String  glossaryTermGUIDParameterName,
                                     Date    effectiveFrom,
                                     Date    effectiveTo,
                                     boolean isMergeUpdate,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryTermGUID,
                                           glossaryTermGUIDParameterName,
                                           OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                           OpenMetadataType.SPINE_OBJECT_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.SPINE_OBJECT_CLASSIFICATION_TYPE_NAME,
                                           this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the spine object designation from the glossary term.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsSpineObject(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  glossaryTermGUID,
                                       String  glossaryTermGUIDParameterName,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryTermGUID,
                                                glossaryTermGUIDParameterName,
                                                OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                OpenMetadataType.SPINE_OBJECT_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.SPINE_OBJECT_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes a spine attribute.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsSpineAttribute(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  glossaryTermGUID,
                                        String  glossaryTermGUIDParameterName,
                                        Date    effectiveFrom,
                                        Date    effectiveTo,
                                        boolean isMergeUpdate,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryTermGUID,
                                           glossaryTermGUIDParameterName,
                                           OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                           OpenMetadataType.SPINE_ATTRIBUTE_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.SPINE_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                           this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the spine attribute designation from the glossary term.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsSpineAttribute(String  userId,
                                          String  externalSourceGUID,
                                          String  externalSourceName,
                                          String  glossaryTermGUID,
                                          String  glossaryTermGUIDParameterName,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryTermGUID,
                                                glossaryTermGUIDParameterName,
                                                OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                OpenMetadataType.SPINE_ATTRIBUTE_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.SPINE_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Classify the glossary term to indicate that it describes an object identifier.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsObjectIdentifier(String  userId,
                                          String  externalSourceGUID,
                                          String  externalSourceName,
                                          String  glossaryTermGUID,
                                          String  glossaryTermGUIDParameterName,
                                          Date    effectiveFrom,
                                          Date    effectiveTo,
                                          boolean isMergeUpdate,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           glossaryTermGUID,
                                           glossaryTermGUIDParameterName,
                                           OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                           OpenMetadataType.OBJECT_IDENTIFIER_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.OBJECT_IDENTIFIER_CLASSIFICATION_TYPE_NAME,
                                           this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the object identifier designation from the glossary term.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsObjectIdentifier(String  userId,
                                            String  externalSourceGUID,
                                            String  externalSourceName,
                                            String  glossaryTermGUID,
                                            String  glossaryTermGUIDParameterName,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, glossaryTermGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                glossaryTermGUID,
                                                glossaryTermGUIDParameterName,
                                                OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                OpenMetadataType.OBJECT_IDENTIFIER_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.OBJECT_IDENTIFIER_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Classify the glossary term in the repository to show that it has been archived and is only needed for lineage.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param archiveDate date that the file was archived or discovered to have been archived.  Null means now.
     * @param archiveProcess name of archiving process
     * @param archiveProperties properties to help locate the archive copy
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void archiveGlossaryTerm(String              userId,
                                    String              assetManagerGUID,
                                    String              assetManagerName,
                                    String              glossaryTermGUID,
                                    String              glossaryTermGUIDParameterName,
                                    Date                archiveDate,
                                    String              archiveProcess,
                                    Map<String, String> archiveProperties,
                                    boolean             forDuplicateProcessing,
                                    Date                effectiveTime,
                                    String              methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           glossaryTermGUID,
                                                           glossaryTermGUIDParameterName,
                                                           OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                           null,
                                                           null,
                                                           true,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        ReferenceableBuilder builder = new ReferenceableBuilder(repositoryHelper, serviceName, serverName);

        repositoryHandler.classifyEntity(userId,
                                         assetManagerGUID,
                                         assetManagerName,
                                         entity.getGUID(),
                                         entity,
                                         glossaryTermGUIDParameterName,
                                         OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                         OpenMetadataType.MEMENTO_CLASSIFICATION.typeGUID,
                                         OpenMetadataType.MEMENTO_CLASSIFICATION.typeName,
                                         ClassificationOrigin.ASSIGNED,
                                         entity.getGUID(),
                                         builder.getMementoProperties(archiveDate,
                                                                      userId,
                                                                      archiveProcess,
                                                                      archiveProperties,
                                                                      methodName),
                                         true,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Remove the metadata element representing a glossary term.
     *
     * @param userId calling user
     * @param externalSourceGUID     unique identifier of software capability representing the caller
     * @param externalSourceName     unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermGUIDParameterName parameter supplying glossaryTermGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossaryTerm(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  glossaryTermGUID,
                                   String  glossaryTermGUIDParameterName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    glossaryTermGUID,
                                    glossaryTermGUIDParameterName,
                                    OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
                                    OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Returns the glossary term object corresponding to the supplied term name.
     *
     * @param userId  String - userId of user making request.
     * @param glossaryGUID unique identifier of the glossary to query
     * @param name  this may be the qualifiedName or displayName of the term.
     * @param limitResultsByStatus By default, terms in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return List of glossary terms retrieved from property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getTermsByName(String               userId,
                                  String               glossaryGUID,
                                  String               name,
                                  List<InstanceStatus> limitResultsByStatus,
                                  int                  startFrom,
                                  int                  pageSize,
                                  boolean              forLineage,
                                  boolean              forDuplicateProcessing,
                                  Date                 effectiveTime,
                                  String               methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String entityGUIDParameterName = "termEntity.getGUID";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        /*
         * Need to filter results for glossary.
         */
        RepositoryIteratorForEntities iterator = getEntitySearchIterator(userId,
                                                                         name,
                                                                         OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
                                                                         OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                         specificMatchPropertyNames,
                                                                         true,
                                                                         false,
                                                                         null,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         0,
                                                                         queryPageSize,
                                                                         effectiveTime,
                                                                         methodName);

        List<B> results = new ArrayList<>();

        while ((iterator.moreToReceive()) && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            EntityDetail entity = iterator.getNext();

            int matchCount = 0;

            if (entity != null)
            {
                try
                {
                    this.validateAnchorEntity(userId,
                                              entity.getGUID(),
                                              entity.getType().getTypeDefName(),
                                              entity,
                                              entityGUIDParameterName,
                                              false,
                                              false,
                                              forLineage,
                                              forDuplicateProcessing,
                                              supportedZones,
                                              effectiveTime,
                                              methodName);

                    AnchorIdentifiers anchorIdentifiers = this.getAnchorGUIDFromAnchorsClassification(entity, methodName);

                    if (((glossaryGUID == null) || (glossaryGUID.equals(anchorIdentifiers.anchorGUID))) &&
                        ((limitResultsByStatus == null) || (limitResultsByStatus.contains(entity.getStatus()))))
                    {
                        matchCount ++;
                        if (matchCount > startFrom)
                        {
                            results.add(converter.getNewBean(beanClass, entity, methodName));
                        }
                    }
                }
                catch (Exception notVisible)
                {
                    // ignore entity
                }
            }
        }

        if (! results.isEmpty())
        {
            return results;
        }

        return null;
    }


    /**
     * Returns the glossary term object containing the supplied term name.  This may include wildcard characters
     *
     * @param userId  String - userId of user making request.
     * @param glossaryGUID unique identifier of the glossary to query
     * @param searchString string to find in the properties
     * @param limitResultsByStatus By default, terms in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return List of glossary terms retrieved from property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findTerms(String               userId,
                             String               glossaryGUID,
                             String               searchString,
                             List<InstanceStatus> limitResultsByStatus,
                             int                  startFrom,
                             int                  pageSize,
                             boolean              forLineage,
                             boolean              forDuplicateProcessing,
                             Date                 effectiveTime,
                             String               methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String entityGUIDParameterName = "termEntity.getGUID";

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        /*
         * Need to filter results for glossary.
         */
        RepositoryIteratorForEntities iterator = getEntitySearchIterator(userId,
                                                                         searchString,
                                                                         OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
                                                                         OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                         null,
                                                                         false,
                                                                         false,
                                                                         null,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         0,
                                                                         queryPageSize,
                                                                         effectiveTime,
                                                                         methodName);

        List<B> results = new ArrayList<>();

        while ((iterator.moreToReceive()) && ((queryPageSize == 0) || (results.size() < queryPageSize)))
        {
            EntityDetail termEntity = iterator.getNext();

            int matchCount = 0;

            if (termEntity != null)
            {
                try
                {
                    this.validateAnchorEntity(userId,
                                              termEntity.getGUID(),
                                              OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                              termEntity,
                                              entityGUIDParameterName,
                                              false,
                                              false,
                                              forLineage,
                                              forDuplicateProcessing,
                                              supportedZones,
                                              effectiveTime,
                                              methodName);

                    AnchorIdentifiers termAnchorIdentifiers = this.getAnchorGUIDFromAnchorsClassification(termEntity, methodName);

                    if (((glossaryGUID == null) || (glossaryGUID.equals(termAnchorIdentifiers.anchorGUID))) &&
                        ((limitResultsByStatus == null) || (limitResultsByStatus.contains(termEntity.getStatus()))))
                    {
                        matchCount ++;
                        if (matchCount > startFrom)
                        {
                            results.add(converter.getNewBean(beanClass, termEntity, methodName));
                        }
                    }
                    else if (glossaryGUID != null)
                    {
                        /*
                         * The term is not anchored in the requested glossary.  Maybe it is linked to a category in the requested glossary?
                         */
                        List<EntityDetail> categoryEntities = this.getAttachedEntities(userId,
                                                                                       termEntity.getGUID(),
                                                                                       entityGUIDParameterName,
                                                                                       OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                                                                       OpenMetadataType.TERM_CATEGORIZATION_TYPE_GUID,
                                                                                       OpenMetadataType.TERM_CATEGORIZATION_TYPE_NAME,
                                                                                       OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                                                                       null,
                                                                                       null,
                                                                                       1,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       supportedZones,
                                                                                       0,
                                                                                       0,
                                                                                       effectiveTime,
                                                                                       methodName);

                        if (categoryEntities != null)
                        {
                            for (EntityDetail categoryEntity : categoryEntities)
                            {
                                if (categoryEntity != null)
                                {
                                    AnchorIdentifiers categoryAnchorIdentifiers = this.getAnchorGUIDFromAnchorsClassification(categoryEntity, methodName);

                                    if ((glossaryGUID.equals(categoryAnchorIdentifiers.anchorGUID)) &&
                                        ((limitResultsByStatus == null) || (limitResultsByStatus.contains(termEntity.getStatus()))))
                                    {
                                        matchCount ++;
                                        if (matchCount > startFrom)
                                        {
                                            results.add(converter.getNewBean(beanClass, termEntity, methodName));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception notVisible)
                {
                    // ignore entity
                }
            }
        }

        if (! results.isEmpty())
        {
            return results;
        }

        return null;
    }


    /**
     * Returns the glossary term object corresponding to the supplied glossary term GUID.
     *
     * @param userId  String - userId of user making request
     * @param guid  the unique id for the glossary term within the property server
     * @param guidParameter name of parameter supplying the guid
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return Glossary Term retrieved from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getTerm(String    userId,
                     String    guid,
                     String    guidParameter,
                     boolean   forLineage,
                     boolean   forDuplicateProcessing,
                     Date      effectiveTime,
                     String    methodName) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameter,
                                          OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary of interest
     * @param glossaryGUIDParameterName property supplying the glossaryGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>  getTermsForGlossary(String               userId,
                                        String               glossaryGUID,
                                        String               glossaryGUIDParameterName,
                                        int                  startFrom,
                                        int                  pageSize,
                                        boolean              forLineage,
                                        boolean              forDuplicateProcessing,
                                        Date                 effectiveTime,
                                        String               methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TYPE_NAME,
                                        OpenMetadataType.TERM_ANCHOR_TYPE_GUID,
                                        OpenMetadataType.TERM_ANCHOR_TYPE_NAME,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);

    }


    /**
     * Retrieve the list of glossary terms associated with a glossary category.
     *
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the glossary category of interest
     * @param glossaryCategoryGUIDParameterName property supplying the glossaryCategoryGUID
     * @param limitResultsByStatus By default, term relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>    getTermsForGlossaryCategory(String        userId,
                                                  String        glossaryCategoryGUID,
                                                  String        glossaryCategoryGUIDParameterName,
                                                  List<Integer> limitResultsByStatus,
                                                  int           startFrom,
                                                  int           pageSize,
                                                  boolean       forLineage,
                                                  boolean       forDuplicateProcessing,
                                                  Date          effectiveTime,
                                                  String        methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        if ((limitResultsByStatus == null) || (limitResultsByStatus.isEmpty()))
        {
            return this.getAttachedElements(userId,
                                            glossaryCategoryGUID,
                                            glossaryCategoryGUIDParameterName,
                                            OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                            OpenMetadataType.TERM_CATEGORIZATION_TYPE_GUID,
                                            OpenMetadataType.TERM_CATEGORIZATION_TYPE_NAME,
                                            OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                            null,
                                            null,
                                            2,
                                            forLineage,
                                            forDuplicateProcessing,
                                            startFrom,
                                            pageSize,
                                            effectiveTime,
                                            methodName);
        }
        else
        {
            return this.getAttachedElements(userId,
                                            glossaryCategoryGUID,
                                            glossaryCategoryGUIDParameterName,
                                            OpenMetadataType.GLOSSARY_CATEGORY_TYPE_NAME,
                                            OpenMetadataType.TERM_CATEGORIZATION_TYPE_GUID,
                                            OpenMetadataType.TERM_CATEGORIZATION_TYPE_NAME,
                                            OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                            null,
                                            null,
                                            limitResultsByStatus,
                                            OpenMetadataType.STATUS_PROPERTY_NAME,
                                            2,
                                            forLineage,
                                            forDuplicateProcessing,
                                            startFrom,
                                            pageSize,
                                            effectiveTime,
                                            methodName);
        }
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary of interest
     * @param glossaryGUIDParameterName property supplying the glossaryGUID
     * @param relationshipTypeName optional name of relationship
     * @param limitResultsByStatus By default, term relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>  getRelatedTerms(String        userId,
                                    String        glossaryGUID,
                                    String        glossaryGUIDParameterName,
                                    String        relationshipTypeName,
                                    List<Integer> limitResultsByStatus,
                                    int           startFrom,
                                    int           pageSize,
                                    boolean       forLineage,
                                    boolean       forDuplicateProcessing,
                                    Date          effectiveTime,
                                    String        methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        String relationshipTypeGUID = null;

        if (relationshipTypeName != null)
        {
            relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                            relationshipTypeName,
                                                                            serviceName,
                                                                            methodName,
                                                                            repositoryHelper);
        }

        if ((limitResultsByStatus == null) || (limitResultsByStatus.isEmpty()))
        {
            return this.getAttachedElements(userId,
                                            glossaryGUID,
                                            glossaryGUIDParameterName,
                                            OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                            relationshipTypeGUID,
                                            relationshipTypeName,
                                            OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                            null,
                                            null,
                                            0,
                                            forLineage,
                                            forDuplicateProcessing,
                                            startFrom,
                                            pageSize,
                                            effectiveTime,
                                            methodName);
        }
        else
        {
            return this.getAttachedElements(userId,
                                            glossaryGUID,
                                            glossaryGUIDParameterName,
                                            OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                            relationshipTypeGUID,
                                            relationshipTypeName,
                                            OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                            null,
                                            null,
                                            limitResultsByStatus,
                                            OpenMetadataType.STATUS_PROPERTY_NAME,
                                            0,
                                            forLineage,
                                            forDuplicateProcessing,
                                            startFrom,
                                            pageSize,
                                            effectiveTime,
                                            methodName);
        }
    }


    /**
     * Return the glossary terms attached to a supplied entity through the semantic assignment.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the feedback is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getAttachedMeanings(String       userId,
                                        String       elementGUID,
                                        String       elementGUIDParameterName,
                                        String       elementTypeName,
                                        List<String> serviceSupportedZones,
                                        int          startingFrom,
                                        int          pageSize,
                                        boolean      forLineage,
                                        boolean      forDuplicateProcessing,
                                        Date         effectiveTime,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataType.REFERENCEABLE_TO_MEANING_TYPE_GUID,
                                        OpenMetadataType.REFERENCEABLE_TO_MEANING_TYPE_NAME,
                                        OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }

}
