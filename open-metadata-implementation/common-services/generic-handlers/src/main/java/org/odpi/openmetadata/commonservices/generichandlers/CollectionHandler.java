/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CollectionHandler provides the exchange of metadata about collections between the repository and the OMAS.
 *
 * @param <B> class that represents the collection
 */
public class CollectionHandler<B> extends ReferenceableHandler<B>
{
    private static final String qualifiedNameParameterName = "qualifiedName";

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
    public CollectionHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create the collection object.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this collection
     * @param externalSourceName unique name of the software capability that owns this collection
     * @param qualifiedName unique name for the collection - used in other configuration
     * @param displayName short display name for the collection
     * @param description description of the governance collection
     * @param additionalProperties additional properties for a collection
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance collection subtype
     * @param classificationName name of classification to add to the collection (assume no properties)
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new collection object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createCollection(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              qualifiedName,
                                   String              displayName,
                                   String              description,
                                   Map<String, String> additionalProperties,
                                   String              suppliedTypeName,
                                   Map<String, Object> extendedProperties,
                                   String              classificationName,
                                   Date                effectiveFrom,
                                   Date                effectiveTo,
                                   Date                effectiveTime,
                                   String              methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.COLLECTION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.COLLECTION_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        CollectionBuilder builder = new CollectionBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          additionalProperties,
                                                          typeGUID,
                                                          typeName,
                                                          extendedProperties,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        if (classificationName != null)
        {
            builder.setupCollectionClassification(userId, classificationName, methodName);
        }

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Create the folder object.  This is collection with a Folder classification attached
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this collection
     * @param externalSourceName unique name of the software capability that owns this collection
     * @param qualifiedName unique name for the collection - used in other configuration
     * @param displayName short display name for the collection
     * @param description description of the governance collection
     * @param additionalProperties additional properties for a collection
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance collection subtype
     * @param orderBy the factor used to organize the members
     * @param orderPropertyName name of property of OrderBy is 99 (OTHER)
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new collection object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createFolder(String              userId,
                               String              externalSourceGUID,
                               String              externalSourceName,
                               String              qualifiedName,
                               String              displayName,
                               String              description,
                               Map<String, String> additionalProperties,
                               String              suppliedTypeName,
                               Map<String, Object> extendedProperties,
                               int                 orderBy,
                               String              orderPropertyName,
                               Date                effectiveFrom,
                               Date                effectiveTo,
                               Date                effectiveTime,
                               String              methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.COLLECTION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.COLLECTION_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        CollectionBuilder builder = new CollectionBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          additionalProperties,
                                                          typeGUID,
                                                          typeName,
                                                          extendedProperties,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        builder.setupFolderClassification(userId, orderBy, orderPropertyName, methodName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Create a new metadata element to represent a collection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new collection.
     *
     * All categories and terms are linked to a single collection.  They are owned by this collection and if the
     * collection is deleted, any linked terms and categories are deleted as well.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this collection
     * @param externalSourceName unique name of the software capability that owns this collection
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the collection - used in other configuration
     * @param displayName short display name for the collection
     * @param description description of the governance collection
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createCollectionFromTemplate(String userId,
                                               String externalSourceGUID,
                                               String externalSourceName,
                                               String templateGUID,
                                               String qualifiedName,
                                               String displayName,
                                               String description,
                                               String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        CollectionBuilder builder = new CollectionBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataType.COLLECTION_TYPE_GUID,
                                           OpenMetadataType.COLLECTION_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                           builder,
                                           supportedZones,
                                           true,
                                           false,
                                           null,
                                           methodName);
    }


    /**
     * Update the anchor object that all elements in a collection (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this collection
     * @param externalSourceName unique name of the software capability that owns this collection
     * @param collectionGUID unique identifier of the collection to update
     * @param collectionGUIDParameterName parameter passing the collectionGUID
     * @param qualifiedName unique name for the collection - used in other configuration
     * @param displayName short display name for the collection
     * @param description description of the governance collection
     * @param additionalProperties additional properties for a governance collection
     * @param suppliedTypeName type of collection
     * @param extendedProperties  properties for a governance collection subtype
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateCollection(String              userId,
                                   String              externalSourceGUID,
                                   String              externalSourceName,
                                   String              collectionGUID,
                                   String              collectionGUIDParameterName,
                                   String              qualifiedName,
                                   String              displayName,
                                   String              description,
                                   Map<String, String> additionalProperties,
                                   String              suppliedTypeName,
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
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.COLLECTION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.COLLECTION_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        CollectionBuilder builder = new CollectionBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          additionalProperties,
                                                          typeGUID,
                                                          typeName,
                                                          extendedProperties,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    collectionGUID,
                                    collectionGUIDParameterName,
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
     * Mark the collection as a folder.
     *
     * @param userId calling user
     * @param collectionGUID unique identifier of asset
     * @param collectionGUIDParameterName parameter name supplying collectionGUID
     * @param orderBy the factor used to organize the members
     * @param orderPropertyName name of property of OrderBy is 99 (OTHER)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addFolderClassificationToCollection(String  userId,
                                                     String  collectionGUID,
                                                     String  collectionGUIDParameterName,
                                                     int     orderBy,
                                                     String  orderPropertyName,
                                                     boolean isMergeUpdate,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime,
                                                     String  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);

        CollectionBuilder builder = new CollectionBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           collectionGUID,
                                           collectionGUIDParameterName,
                                           OpenMetadataType.COLLECTION_TYPE_NAME,
                                           OpenMetadataType.FOLDER_TYPE_GUID,
                                           OpenMetadataType.FOLDER_TYPE_NAME,
                                           builder.getFolderProperties(orderBy, orderPropertyName, methodName),
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the taxonomy designation from a collection.
     *
     * @param userId calling user
     * @param collectionGUID unique identifier of asset
     * @param collectionGUIDParameterName parameter name supplying collectionGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeFolderClassificationFromCollection(String  userId,
                                                          String  collectionGUID,
                                                          String  collectionGUIDParameterName,
                                                          boolean forLineage,
                                                          boolean forDuplicateProcessing,
                                                          Date    effectiveTime,
                                                          String  methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(collectionGUID, collectionGUIDParameterName, methodName);

        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                collectionGUID,
                                                collectionGUIDParameterName,
                                                OpenMetadataType.COLLECTION_TYPE_NAME,
                                                OpenMetadataType.FOLDER_TYPE_GUID,
                                                OpenMetadataType.FOLDER_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Add a member (Referenceable) to collection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this collection
     * @param externalSourceName unique name of the software capability that owns this collection
     * @param collectionGUID unique identifier of the collection
     * @param collectionGUIDParameterName parameter supplying the collectionGUID
     * @param memberGUID unique identifier of the element that is being added to the collection
     * @param memberGUIDParameterName parameter supplying the memberGUID
     * @param membershipRationale why is the element a member? (optional)
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addMemberToCollection(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  collectionGUID,
                                      String  collectionGUIDParameterName,
                                      String  memberGUID,
                                      String  memberGUIDParameterName,
                                      String  membershipRationale,
                                      Date    effectiveFrom,
                                      Date    effectiveTo,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataType.MEMBERSHIP_RATIONALE_PROPERTY_NAME,
                                                                                     membershipRationale,
                                                                                     methodName);
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  collectionGUID,
                                  collectionGUIDParameterName,
                                  OpenMetadataType.COLLECTION_TYPE_NAME,
                                  memberGUID,
                                  memberGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.COLLECTION_MEMBERSHIP_TYPE_GUID,
                                  OpenMetadataType.COLLECTION_MEMBERSHIP_TYPE_NAME,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Add a member (Referenceable) to collection.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this collection
     * @param externalSourceName unique name of the software capability that owns this collection
     * @param collectionGUID unique identifier of the collection
     * @param collectionGUIDParameterName parameter supplying the collectionGUID
     * @param memberGUID unique identifier of the element that is being added to the collection
     * @param memberGUIDParameterName parameter supplying the memberGUID
     * @param membershipRationale why is the element a member? (optional)
     * @param confidence level of confidence that the membership is correct - from 0 to 100
     * @param membershipStatus status of member
     * @param userDefinedStatus extension to membership status values
     * @param createdBy who created the relationship
     * @param expression what is the expression used to characterize the membership
     * @param source what was the source of the membership recommendation
     * @param steward which steward is responsible for the membership
     * @param stewardTypeName type name of the element representing the steward
     * @param stewardPropertyName property name of the property providing the identifier for the steward (eg qualifiedName)
     * @param notes additional notes for/from the steward
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateCollectionMembership(String  userId,
                                           String  externalSourceGUID,
                                           String  externalSourceName,
                                           String  collectionGUID,
                                           String  collectionGUIDParameterName,
                                           String  memberGUID,
                                           String  memberGUIDParameterName,
                                           String  membershipRationale,
                                           String  expression,
                                           int     membershipStatus,
                                           String  userDefinedStatus,
                                           int     confidence,
                                           String  createdBy,
                                           String  steward,
                                           String  stewardTypeName,
                                           String  stewardPropertyName,
                                           String  source,
                                           String  notes,
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
        InstanceProperties properties = null;

        if (isMergeUpdate)
        {
            Relationship relationship = getUniqueAttachmentLink(userId,
                                                                collectionGUID,
                                                                collectionGUIDParameterName,
                                                                OpenMetadataType.COLLECTION_TYPE_NAME,
                                                                OpenMetadataType.COLLECTION_MEMBERSHIP_TYPE_GUID,
                                                                OpenMetadataType.COLLECTION_MEMBERSHIP_TYPE_NAME,
                                                                memberGUID,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                2,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                supportedZones,
                                                                effectiveTime,
                                                                methodName);

            if (relationship != null)
            {
                properties = relationship.getProperties();
            }
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.MEMBERSHIP_RATIONALE_PROPERTY_NAME,
                                                                  membershipRationale,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.EXPRESSION_PROPERTY_NAME,
                                                                  expression,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataType.STATUS_PROPERTY_NAME,
                                                                    OpenMetadataType.MEMBERSHIP_STATUS_ENUM_TYPE_GUID,
                                                                    OpenMetadataType.MEMBERSHIP_STATUS_ENUM_TYPE_NAME,
                                                                    membershipStatus,
                                                                    methodName);
        }
        catch (TypeErrorException classificationNotSupported)
        {
            throw new InvalidParameterException(classificationNotSupported, OpenMetadataType.STATUS_PROPERTY_NAME);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.USER_DEFINED_STATUS_PROPERTY_NAME,
                                                                  userDefinedStatus,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataType.CONFIDENCE_PROPERTY_NAME,
                                                               confidence,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.CREATED_BY_PROPERTY_NAME,
                                                                  createdBy,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.STEWARD_PROPERTY_NAME,
                                                                  steward,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.STEWARD_TYPE_NAME_PROPERTY_NAME,
                                                                  stewardTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.STEWARD_PROPERTY_NAME_PROPERTY_NAME,
                                                                  stewardPropertyName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.SOURCE_PROPERTY_NAME,
                                                                  source,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.NOTES_PROPERTY_NAME,
                                                                  notes,
                                                                  methodName);


        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  collectionGUID,
                                  collectionGUIDParameterName,
                                  OpenMetadataType.COLLECTION_TYPE_NAME,
                                  memberGUID,
                                  memberGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.COLLECTION_MEMBERSHIP_TYPE_GUID,
                                  OpenMetadataType.COLLECTION_MEMBERSHIP_TYPE_NAME,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a parent-child relationship between two categories.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this collection
     * @param externalSourceName unique name of the software capability that owns this collection
     * @param collectionGUID unique identifier of the collection
     * @param collectionGUIDParameterName parameter supplying the collectionGUID
     * @param memberGUID unique identifier of the element that is being added to the collection
     * @param memberGUIDParameterName parameter supplying the memberGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeMemberFromCollection(String  userId,
                                           String  externalSourceGUID,
                                           String  externalSourceName,
                                           String  collectionGUID,
                                           String  collectionGUIDParameterName,
                                           String  memberGUID,
                                           String  memberGUIDParameterName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      collectionGUID,
                                      collectionGUIDParameterName,
                                      OpenMetadataType.COLLECTION_TYPE_NAME,
                                      memberGUID,
                                      memberGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeGUID,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.COLLECTION_MEMBERSHIP_TYPE_GUID,
                                      OpenMetadataType.COLLECTION_MEMBERSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a collection.  This will delete the collection and all categories and terms because
     * the Anchors classifications are set up in these elements.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this collection
     * @param externalSourceName unique name of the software capability that owns this collection
     * @param collectionGUID unique identifier of the metadata element to remove
     * @param collectionGUIDParameterName parameter supplying the collectionGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeCollection(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  collectionGUID,
                                 String  collectionGUIDParameterName,
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
                                    collectionGUID,
                                    collectionGUIDParameterName,
                                    OpenMetadataType.COLLECTION_TYPE_GUID,
                                    OpenMetadataType.COLLECTION_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of collection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findCollections(String  userId,
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
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataType.COLLECTION_TYPE_GUID,
                              OpenMetadataType.COLLECTION_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of collection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)

     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getCollectionsByName(String  userId,
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
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.NAME.name);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataType.COLLECTION_TYPE_GUID,
                                    OpenMetadataType.COLLECTION_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the collection metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getCollectionByGUID(String  userId,
                                 String  guid,
                                 String  guidParameterName,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataType.COLLECTION_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);

    }
}
