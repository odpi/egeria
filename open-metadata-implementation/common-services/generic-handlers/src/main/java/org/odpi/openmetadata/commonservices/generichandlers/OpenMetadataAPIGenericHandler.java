/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersAuditCode;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.LatestChangeAction;
import org.odpi.openmetadata.frameworks.openmetadata.enums.LatestChangeTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadataobservability.ffdc.OpenMetadataObservabilityAuditCode;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * OpenMetadataAPIGenericHandler manages the exchange of Open Metadata API Bean content with the repository services
 * (via the repository handler).
 *
 * @param <B> bean class
 */
public class OpenMetadataAPIGenericHandler<B> extends OpenMetadataAPIAnchorHandler<B>
{
    private static final Logger log = LoggerFactory.getLogger(OpenMetadataAPIGenericHandler.class);

    /**
     * Construct the handler information needed to interact with the repository services
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
     * @param auditLog destination for audit log events.
     */
    public OpenMetadataAPIGenericHandler(OpenMetadataAPIGenericConverter<B> converter,
                                         Class<B>                           beanClass,
                                         String                             serviceName,
                                         String                             serverName,
                                         InvalidParameterHandler            invalidParameterHandler,
                                         RepositoryHandler                  repositoryHandler,
                                         OMRSRepositoryHelper               repositoryHelper,
                                         String                             localServerUserId,
                                         OpenMetadataServerSecurityVerifier securityVerifier,
                                         AuditLog                           auditLog)
    {
        super(converter, beanClass, serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId, securityVerifier, auditLog);
    }


    /**
     * Validate the anchor guid and add its type and identifier to the builder.
     *
     * @param userId calling user
     * @param anchorGUID unique identifier of the anchor
     * @param anchorGUIDParameterName parameter used to pass anchorGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param builder builder to receive the anchor (if appropriate).
     * @param methodName calling method
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addAnchorGUIDToBuilder(String                        userId,
                                       String                        anchorGUID,
                                       String                        anchorGUIDParameterName,
                                       boolean                       forLineage,
                                       boolean                       forDuplicateProcessing,
                                       Date                          effectiveTime,
                                       OpenMetadataAPIGenericBuilder builder,
                                       String                        methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        if (anchorGUID != null)
        {
            EntityDetail anchorEntity = this.setUpAnchorsClassificationFromAnchor(userId,
                                                                     anchorGUID,
                                                                     anchorGUIDParameterName,
                                                                     null,
                                                                     builder,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

            if (anchorEntity != null)
            {

                /*
                 * This method will throw an exception if the element is not in the supported zones - it will look like
                 * the element is not known.
                 */
                invalidParameterHandler.validateElementInSupportedZone(anchorEntity.getGUID(),
                                                                       anchorGUIDParameterName,
                                                                       this.getEntityZones(anchorEntity),
                                                                       securityVerifier.getSupportedZones(userId, anchorEntity.getType().getTypeDefName(), methodName),
                                                                       serviceName,
                                                                       methodName);

            }
        }
    }


    /**
     * Return the zone membership of an element.
     *
     * @param entity entity
     * @return list of zones or null
     */
    protected List<String> getEntityZones(EntityDetail entity)
    {
        final String methodName = "getEntityZones";

        Classification zoneMembershipClassification = this.getExistingClassification(entity, OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName);

        if (zoneMembershipClassification != null)
        {
            return repositoryHelper.getStringArrayProperty(serviceName,
                                                           OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                           zoneMembershipClassification.getProperties(),
                                                           methodName);
        }

        return null;
    }


    /**
     * Check that a retrieved entity is readable.  This is always the first check and include checks
     * for private feedback, connection selection, asset zones and security tags on glossaries
     *
     * @param userId calling user
     * @param connectToEntity entity retrieved
     * @param connectToGUIDParameterName parameter used to pass the entity to retrieve.
     * @param connectToType type of retrieved entity
     * @param isExplicitGetRequest is this entity requested explicitly
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void validateRetrievedEntityIsVisible(String       userId,
                                                  EntityDetail connectToEntity,
                                                  String       connectToGUIDParameterName,
                                                  String       connectToType,
                                                  boolean      isExplicitGetRequest,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        /*
         * This first processing looks at the retrieved entity itself to ensure it is visible.
         */
        invalidParameterHandler.validateElementInSupportedZone(connectToEntity.getGUID(),
                                                               connectToGUIDParameterName,
                                                               this.getEntityZones(connectToEntity),
                                                               securityVerifier.getSupportedZones(userId, connectToEntity.getType().getTypeDefName(), methodName),
                                                               serviceName,
                                                               methodName);

        /*
         * Even if the request is an update request, the security module is first called for read - the update
         * is validated once the properties have been updated.
         */
        securityVerifier.validateUserForElementRead(userId,
                                                    connectToEntity,
                                                    repositoryHelper,
                                                    serviceName,
                                                    methodName);

        /*
         * Log asset retrieval to identify where popular asset are.
         */
        if (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataType.ASSET.typeName))
        {
            if (isExplicitGetRequest)
            {
                auditLog.logMessage(assetActionDescription,
                                    OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_READ.getMessageDefinition(userId,
                                                                                                                connectToEntity.getType().getTypeDefName(),
                                                                                                                connectToEntity.getGUID(),
                                                                                                                methodName,
                                                                                                                serviceName));
            }
            else
            {
                auditLog.logMessage(assetActionDescription,
                                    OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_SEARCH.getMessageDefinition(userId,
                                                                                                                  connectToEntity.getType().getTypeDefName(),
                                                                                                                  connectToEntity.getGUID(),
                                                                                                                  methodName,
                                                                                                                  serviceName));
            }
        }
    }


    /**
     * Validates whether an operation is valid based on the type of entity it is connecting to, who the user is and whether it is a read or an update.
     *
     * @param userId           userId of user making request.
     * @param connectToGUID       unique id for the object to connect the attachment to.
     * @param connectToGUIDParameterName  name of the parameter that passed the connectTo guid
     * @param connectToType       type of the connectToElement.
     * @param isExplicitGetRequest Is this request an explicit get request for the asset or a find request.
     * @param isUpdate         is this an update request?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime    the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     * @return anchor entity or null.  The anchor entity is used by the caller to set the LatestChange classification
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail validateEntityAndAnchorForRead(String       userId,
                                                       String       connectToGUID,
                                                       String       connectToGUIDParameterName,
                                                       String       connectToType,
                                                       boolean      isExplicitGetRequest,
                                                       boolean      isUpdate,
                                                       boolean      forLineage,
                                                       boolean      forDuplicateProcessing,
                                                       Date         effectiveTime,
                                                       String       methodName) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectToGUID, connectToGUIDParameterName, methodName);

        /*
         * This returns the entity for the connectTo element and validates it is of the correct type.
         */
        EntityDetail  connectToEntity = repositoryHandler.getEntityByGUID(userId,
                                                                          connectToGUID,
                                                                          connectToGUIDParameterName,
                                                                          connectToType,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

        return this.validateEntityAndAnchorForRead(userId,
                                                   connectToType,
                                                   connectToEntity,
                                                   connectToGUIDParameterName,
                                                   isExplicitGetRequest,
                                                   isUpdate,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
    }


    /**
     * Validates whether an operation is valid based on the type of entity it is connecting to, who the user is and whether it is a read or an
     * update.
     * <br><br>
     * The first part of this method is looking to see if the connectToEntity is an anchor entity. In which case it calls any specific validation
     * for that entity and returns the connectToEntity, assuming all is ok - exceptions are thrown if the entity is not valid or the user does not
     * have access to it.
     * <br><br>
     * If the connectToEntity is of a type that has a lifecycle that is linked to the lifecycle of another entity - typically a referenceable -
     * then that other entity is its anchor (examples are schema elements, comments, connections).  The anchor entity needs to be retrieved and
     * validated.
     * <br><br>
     * Some anchor entities have specific validation to perform.
     *
     * @param userId           userId of user making request.
     * @param connectToType    name of type of connectToEntity
     * @param connectToEntity  entity retrieved from the repository
     * @param connectToGUIDParameterName  name of the parameter that passed the connectTo guid
     * @param isExplicitGetRequest Is this request an explicit get request for the asset or a find request.
     * @param isUpdate         is this an update request?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     *
     * @return anchor entity or null if this entity is an anchor or does not have an anchor.  The anchor entity is used by the
     * caller to set the LatestChange classification
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem accessing the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail validateEntityAndAnchorForRead(String        userId,
                                                       String        connectToType,
                                                       EntityDetail  connectToEntity,
                                                       String        connectToGUIDParameterName,
                                                       boolean       isExplicitGetRequest,
                                                       boolean       isUpdate,
                                                       boolean       forLineage,
                                                       boolean       forDuplicateProcessing,
                                                       Date          effectiveTime,
                                                       String        methodName) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        invalidParameterHandler.validateObject(connectToEntity, connectToGUIDParameterName, methodName);

        /*
         * This first processing looks at the retrieved entity itself to ensure it is visible.
         */
        validateRetrievedEntityIsVisible(userId,
                                         connectToEntity,
                                         connectToGUIDParameterName,
                                         connectToType,
                                         isExplicitGetRequest,
                                         methodName);

        /*
         * If an entity has an anchor, the unique identifier of the anchor should be in the Anchors classifications.
         * The exception occurs where the entity is not being managed by this handler, or something equivalent that
         * maintains the Anchors classification.  Therefore, if the Anchors classification is missing, a new one is
         * derived and added to the connectToEntity.
         */
        AnchorIdentifiers anchorIdentifiers = this.getAnchorGUIDForEntity(connectToEntity,
                                                                          connectToGUIDParameterName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);


        /*
         * If an anchor GUID has been found then validate it by retrieving the identified entity.
         * Note - anchorIdentifiers may or may not be null if the connectToEntity is actually an anchor.
         */
        return validateAnchorGUID(userId,
                                  connectToEntity,
                                  anchorIdentifiers,
                                  isExplicitGetRequest,
                                  isUpdate,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Validates a list of entities retrieved from the repository as a result of a query.
     * It validates supported zones read security and the anchor GUID.  It does not handle Mementos, effective dates
     * and element status, since they are the responsibility of the repository handler.
     *
     * @param userId           userId of user making request.
     * @param retrievedEntities  entities retrieved from the repository
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     *
     * @return list of validated entities
     */
    public List<EntityDetail> validateEntitiesAndAnchorsForRead(String             userId,
                                                                List<EntityDetail> retrievedEntities,
                                                                boolean            forLineage,
                                                                boolean            forDuplicateProcessing,
                                                                Date               effectiveTime,
                                                                String             methodName)
    {
        if (retrievedEntities != null)
        {
            final String connectToGUIDParameterName = "retrievedEntity.getGUID";

            Map<String, EntityDetail>      visibleEntities = new HashMap<>();

            List<String>                   extractedAnchorGUIDs = new ArrayList<>();
            Map<String, AnchorIdentifiers> extractedAnchors = new HashMap<>();

            List<PropertyCondition> anchorRetrievalQuery = new ArrayList<>();

            /*
             * Identify the anchors that need to be retrieved.  The aim is to build a query that retrieves all of the
             * required Anchor entities in a single retrieve to minimize the calls to the repositories.
             */
            for (EntityDetail connectToEntity : retrievedEntities)
            {
                if (connectToEntity != null)
                {
                    try
                    {
                        /*
                         * This first processing looks at the retrieved entity itself to ensure it is visible.
                         */
                        validateRetrievedEntityIsVisible(userId,
                                                         connectToEntity,
                                                         connectToGUIDParameterName,
                                                         connectToEntity.getType().getTypeDefName(),
                                                         false,
                                                         methodName);

                        visibleEntities.put(connectToEntity.getGUID(), connectToEntity);

                        /*
                         * If an entity has an anchor, the unique identifier of the anchor should be in the Anchors classifications.
                         * The exception occurs where the entity is not being managed by this handler, or something equivalent that
                         * maintains the Anchors classification.  Therefore, if the Anchors classification is missing, a new one is
                         * derived and added to the connectToEntity.
                         */
                        AnchorIdentifiers anchorIdentifiers = this.getAnchorGUIDForEntity(connectToEntity,
                                                                                          connectToGUIDParameterName,
                                                                                          forLineage,
                                                                                          forDuplicateProcessing,
                                                                                          effectiveTime,
                                                                                          methodName);

                        if (anchorIdentifiers != null)
                        {
                            extractedAnchors.put(connectToEntity.getGUID(), anchorIdentifiers);

                            if (anchorIdentifiers.anchorGUID != null)
                            {
                                if (! extractedAnchorGUIDs.contains(anchorIdentifiers.anchorGUID))
                                {
                                    /*
                                     * Need to retrieve the anchor entity
                                     */
                                    PropertyCondition propertyCondition = new PropertyCondition();

                                    propertyCondition.setProperty(OpenMetadataProperty.GUID.name);
                                    propertyCondition.setOperator(PropertyComparisonOperator.EQ);

                                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

                                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
                                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                                    primitivePropertyValue.setPrimitiveValue(anchorIdentifiers.anchorGUID);

                                    propertyCondition.setValue(primitivePropertyValue);

                                    anchorRetrievalQuery.add(propertyCondition);

                                    extractedAnchorGUIDs.add(anchorIdentifiers.anchorGUID);
                                }
                            }
                            else
                            {
                                if (! extractedAnchorGUIDs.contains(connectToEntity.getGUID()))
                                {
                                    extractedAnchorGUIDs.add(connectToEntity.getGUID());
                                }
                            }
                        }
                    }
                    catch (Exception unauthorizedEntity)
                    {
                        // ignore this entity since the user is not allowed to see it
                    }
                }
            }

            /*
             * Retrieve the anchor entities
             */
            Map<String, EntityDetail> retrievedAnchors = this.getEntityList(userId,
                                                                            OpenMetadataType.OPEN_METADATA_ROOT.typeGUID,
                                                                            anchorRetrievalQuery,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveTime,
                                                                            methodName);

            /*
             * Validate the anchors.
             */
            List<String>       validatedAnchors  = new ArrayList<>();
            List<EntityDetail> validatedEntities = new ArrayList<>();

            for (EntityDetail connectToEntity : visibleEntities.values())
            {
                if (connectToEntity != null)
                {
                    try
                    {
                        /*
                         * If an entity has an anchor, the unique identifier of the anchor should be in the Anchors classifications.
                         * The exception occurs where the entity is not being managed by this handler, or something equivalent that
                         * maintains the Anchors classification.  Therefore, if the Anchors classification is missing, a new one is
                         * derived and added to the connectToEntity.
                         */
                        AnchorIdentifiers anchorIdentifiers = extractedAnchors.get(connectToEntity.getGUID());

                        if (anchorIdentifiers != null)
                        {
                            if (anchorIdentifiers.anchorGUID != null)
                            {
                                EntityDetail anchorEntity = retrievedAnchors.get(anchorIdentifiers.anchorGUID);

                                if (anchorEntity == null)
                                {
                                    /*
                                     * Unexpectedly the Anchor GUID was not already retrieved.
                                     */
                                    validateAnchorGUID(userId,
                                                       connectToEntity,
                                                       anchorIdentifiers,
                                                       false,
                                                       false,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       effectiveTime,
                                                       methodName);

                                    validatedAnchors.add(anchorIdentifiers.anchorGUID);
                                }
                                else if (! validatedAnchors.contains(anchorEntity.getGUID()))
                                {
                                    validateAnchorEntity(userId,
                                                         connectToEntity,
                                                         anchorEntity,
                                                         false,
                                                         false,
                                                         methodName);

                                    validatedAnchors.add(anchorEntity.getGUID());
                                }
                            }
                        }

                        validatedEntities.add(connectToEntity);
                    }
                    catch (Exception unauthorizedEntity)
                    {
                        // ignore this entity since the user is not allowed to see it
                    }
                }
            }

            return validatedEntities;
        }

        return null;
    }


    /**
     * Check that the anchor attached to an entity allows the entity to be either returned to the caller or acted upon.
     *
     * @param userId userId of user making request.
     * @param connectToEntity entity retrieved from the repository
     * @param anchorIdentifiers retrieved anchors information
     * @param isExplicitGetRequest Is this request an explicit get request for the asset or a find request.
     * @param isUpdate         is this an update request?
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     * @return anchor entity (this may be the connectToEntity if it is its own anchor or null if the element is unanchored)
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem accessing the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail validateAnchorGUID(String            userId,
                                           EntityDetail      connectToEntity,
                                           AnchorIdentifiers anchorIdentifiers,
                                           boolean           isExplicitGetRequest,
                                           boolean           isUpdate,
                                           boolean           forLineage,
                                           boolean           forDuplicateProcessing,
                                           Date              effectiveTime,
                                           String            methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        if (anchorIdentifiers == null)
        {
            /*
             * The element is unanchored.
             */
            return null;
        }

        /*
         * The element has an anchor.  If anchorGUID is null it means it is its own anchor.
         */
        if (anchorIdentifiers.anchorGUID != null)
        {
            final String anchorGUIDParameterName = "anchorIdentifiers";

            if (! anchorIdentifiers.anchorGUID.equals(connectToEntity.getGUID()))
            {
                EntityDetail anchorEntity = repositoryHandler.getEntityByGUID(userId,
                                                                              anchorIdentifiers.anchorGUID,
                                                                              anchorGUIDParameterName,
                                                                              OpenMetadataType.REFERENCEABLE.typeName,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              effectiveTime,
                                                                              methodName);

                /*
                 * Perform any special processing on the anchor entity
                 */
                validateAnchorEntity(userId,
                                     connectToEntity,
                                     anchorEntity,
                                     isExplicitGetRequest,
                                     isUpdate,
                                     methodName);

                return anchorEntity;
            }
            else
            {
                return connectToEntity;
            }
        }
        else
        {
            return connectToEntity;
        }
    }


    /**
     * Check that the anchor attached to an entity allows the entity to be either returned to the caller or acted upon.
     *
     * @param userId userId of user making request.
     * @param connectToEntity entity retrieved from the repository
     * @param anchorEntity retrieved anchors entity
     * @param isExplicitGetRequest Is this request an explicit get request for the asset or a find request.
     * @param isUpdate         is this an update request?
     * @param methodName       calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem accessing the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void validateAnchorEntity(String       userId,
                                     EntityDetail connectToEntity,
                                     EntityDetail anchorEntity,
                                     boolean      isExplicitGetRequest,
                                     boolean      isUpdate,
                                     String       methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String anchorGUIDParameterName = "anchorIdentifiers";

        /*
         * Perform any special processing on the anchor entity
         */
        if (anchorEntity != null)
        {
            InstanceType anchorEntityType = anchorEntity.getType();

            if (anchorEntityType != null)
            {
                String connectToType = connectToEntity.getType().getTypeDefName();
                boolean isFeedbackEntity =
                        (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataType.INFORMAL_TAG.typeName)) ||
                                (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataType.COMMENT.typeName)) ||
                                (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataType.RATING.typeName)) ||
                                (repositoryHelper.isTypeOf(serviceName, connectToType, OpenMetadataType.LIKE.typeName));


                /*
                 * This method will throw an exception if the element is not in the supported zones - it will look like
                 * the asset is not known.
                 */
                invalidParameterHandler.validateElementInSupportedZone(anchorEntity.getGUID(),
                                                                       anchorGUIDParameterName,
                                                                       this.getEntityZones(anchorEntity),
                                                                       securityVerifier.getSupportedZones(userId, anchorEntity.getType().getTypeDefName(), methodName),
                                                                       serviceName,
                                                                       methodName);


                if (isFeedbackEntity)
                {
                    securityVerifier.validateUserForAnchorAddFeedback(userId,
                                                                      anchorEntity,
                                                                      connectToEntity,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      methodName);
                }
                else if (isUpdate)
                {
                    securityVerifier.validateUserForAnchorMemberUpdate(userId,
                                                                       anchorEntity,
                                                                       repositoryHelper,
                                                                       serviceName,
                                                                       methodName);
                }
                else
                {
                    securityVerifier.validateUserForAnchorMemberRead(userId,
                                                                     anchorEntity,
                                                                     connectToEntity,
                                                                     repositoryHelper,
                                                                     serviceName,
                                                                     methodName);
                }


                if (OpenMetadataType.ASSET.typeName.equals(anchorEntityType.getTypeDefName()))
                {
                    if (isUpdate)
                    {
                        if (isFeedbackEntity)
                        {
                            auditLog.logMessage(assetActionDescription,
                                                OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE_FEEDBACK.getMessageDefinition(userId,
                                                                                                                                       anchorEntityType.getTypeDefName(),
                                                                                                                                       anchorEntity.getGUID(),
                                                                                                                                       methodName,
                                                                                                                                       serviceName));
                        }
                        else
                        {
                            auditLog.logMessage(assetActionDescription,
                                                OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE_ATTACHMENT.getMessageDefinition(userId,
                                                                                                                                         anchorEntityType.getTypeDefName(),
                                                                                                                                         anchorEntity.getGUID(),
                                                                                                                                         methodName,
                                                                                                                                         serviceName));
                        }
                    }
                    else if (isExplicitGetRequest)
                    {
                        auditLog.logMessage(assetActionDescription,
                                            OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_READ_ATTACHMENT.getMessageDefinition(userId,
                                                                                                                                   anchorEntityType.getTypeDefName(),
                                                                                                                                   anchorEntity.getGUID(),
                                                                                                                                   methodName,
                                                                                                                                   serviceName));
                    }
                    else
                    {
                        auditLog.logMessage(assetActionDescription,
                                            OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_SEARCH_ATTACHMENT.getMessageDefinition(userId,
                                                                                                                                     anchorEntityType.getTypeDefName(),
                                                                                                                                     anchorEntity.getGUID(),
                                                                                                                                     methodName,
                                                                                                                                     serviceName));
                    }
                }
            }
        }
    }


    /**
     * Add the requested classification to the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param classificationProperties properties to save in the classification
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     */
    public void setClassificationInRepository(String             userId,
                                              String             externalSourceGUID,
                                              String             externalSourceName,
                                              String             beanGUID,
                                              String             beanGUIDParameterName,
                                              String             beanGUIDTypeName,
                                              String             classificationTypeGUID,
                                              String             classificationTypeName,
                                              InstanceProperties classificationProperties,
                                              boolean            isMergeUpdate,
                                              boolean            forLineage,
                                              boolean            forDuplicateProcessing,
                                              Date               effectiveTime,
                                              String             methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail beanEntity = getEntityFromRepository(userId,
                                                          beanGUID,
                                                          beanGUIDParameterName,
                                                          beanGUIDTypeName,
                                                          null,
                                                          null,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);

        setClassificationInRepository(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      beanEntity,
                                      beanGUIDParameterName,
                                      beanGUIDTypeName,
                                      classificationTypeGUID,
                                      classificationTypeName,
                                      classificationProperties,
                                      isMergeUpdate,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Add the requested classification to the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanEntity entity that the classification is for
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param classificationProperties properties to save in the classification
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param forLineage the query is for lineage so ignore Memento classifications
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     */
    public void setClassificationInRepository(String             userId,
                                              String             externalSourceGUID,
                                              String             externalSourceName,
                                              EntityDetail       beanEntity,
                                              String             beanGUIDParameterName,
                                              String             beanGUIDTypeName,
                                              String             classificationTypeGUID,
                                              String             classificationTypeName,
                                              InstanceProperties classificationProperties,
                                              boolean            isMergeUpdate,
                                              boolean            forLineage,
                                              boolean            forDuplicateProcessing,
                                              Date               effectiveTime,
                                              String             methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        if (beanEntity != null)
        {
            EntityDetail anchorEntity = validateEntityAndAnchorForRead(userId,
                                                                       beanGUIDTypeName,
                                                                       beanEntity,
                                                                       beanGUIDParameterName,
                                                                       true,
                                                                       true,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);

            /*
             * Check that the user is able to classify the element.
             */
            if (anchorEntity != null)
            {
                securityVerifier.validateUserForAnchorClassify(userId,
                                                               anchorEntity,
                                                               classificationTypeName,
                                                               repositoryHelper,
                                                               serviceName,
                                                               methodName);
            }
            else
            {
                securityVerifier.validateUserForElementClassify(userId,
                                                                beanEntity,
                                                                classificationTypeName,
                                                                repositoryHelper,
                                                                serviceName,
                                                                methodName);
            }

            Classification existingClassification = this.getExistingClassification(beanEntity, classificationTypeName);

            /*
             * Classify the asset
             */
            LatestChangeAction latestChangeActionOrdinal;
            if (existingClassification == null)
            {
                latestChangeActionOrdinal = LatestChangeAction.CREATED;

                repositoryHandler.classifyEntity(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 beanEntity.getGUID(),
                                                 beanEntity,
                                                 beanGUIDParameterName,
                                                 beanGUIDTypeName,
                                                 classificationTypeGUID,
                                                 classificationTypeName,
                                                 ClassificationOrigin.ASSIGNED,
                                                 null,
                                                 classificationProperties,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
            }
            else
            {
                latestChangeActionOrdinal = LatestChangeAction.UPDATED;

                InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                      classificationProperties,
                                                                      existingClassification.getProperties());

                /*
                 * If there are no properties to change then nothing more to do
                 */
                if ((newProperties == null) && (existingClassification.getProperties() == null))
                {
                    auditLog.logMessage(methodName,
                                        GenericHandlersAuditCode.IGNORING_UNNECESSARY_CLASSIFICATION_UPDATE.getMessageDefinition(classificationTypeName,
                                                                                                                         beanEntity.getGUID(),
                                                                                                                         methodName,
                                                                                                                         userId));
                    return;
                }

                /*
                 * If nothing has changed in the properties then nothing to do
                 */
                if ((newProperties != null) && (newProperties.equals(existingClassification.getProperties())))
                {
                    auditLog.logMessage(methodName,
                                        GenericHandlersAuditCode.IGNORING_UNNECESSARY_CLASSIFICATION_UPDATE.getMessageDefinition(classificationTypeName,
                                                                                                                                 beanEntity.getGUID(),
                                                                                                                                 methodName,
                                                                                                                                 userId));
                    return;
                }

                repositoryHandler.reclassifyEntity(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   beanEntity.getGUID(),
                                                   beanGUIDParameterName,
                                                   beanGUIDTypeName,
                                                   classificationTypeGUID,
                                                   classificationTypeName,
                                                   existingClassification,
                                                   newProperties,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
            }

            final String actionDescriptionTemplate = "Adding %s classification to %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, classificationTypeName, beanGUIDTypeName, beanEntity.getGUID());

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             LatestChangeTarget.ATTACHMENT_CLASSIFICATION,
                                             latestChangeActionOrdinal,
                                             classificationTypeName,
                                             beanEntity.getGUID(),
                                             beanGUIDTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(methodName, beanEntity.getType().getTypeDefName(), OpenMetadataType.REFERENCEABLE.typeName))
            {
                this.addLatestChangeToAnchor(beanEntity,
                                             LatestChangeTarget.ENTITY_CLASSIFICATION,
                                             latestChangeActionOrdinal,
                                             classificationTypeName,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
    }


    /**
     * Locate the requested classification in the supplied entity.
     *
     * @param beanEntity entity potentially containing the classification
     * @param classificationTypeName unique name of classification type
     */
    Classification getExistingClassification(EntitySummary beanEntity,
                                             String        classificationTypeName)
    {
        Classification  existingClassification  = null;

        if (beanEntity != null)
        {
            /*
             * Look to see if there is an existing classification
             */
            List<Classification> existingClassifications = beanEntity.getClassifications();
            if (existingClassifications != null)
            {
                for (Classification classification : existingClassifications)
                {
                    if (classification != null)
                    {
                        if (classificationTypeName.equals(classification.getName()))
                        {
                            existingClassification = classification;
                        }
                    }
                }
            }
        }

        return existingClassification;
    }


    /**
     * Remove the requested classification from the matching entity in the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the classification name is null
     * @throws PropertyServerException there is a problem with the repositories
     * @throws UserNotAuthorizedException the user is not allowed to update the security tags
     */
    public void removeClassificationFromRepository(String       userId,
                                                   String       externalSourceGUID,
                                                   String       externalSourceName,
                                                   String       beanGUID,
                                                   String       beanGUIDParameterName,
                                                   String       beanGUIDTypeName,
                                                   String       classificationTypeGUID,
                                                   String       classificationTypeName,
                                                   boolean      forLineage,
                                                   boolean      forDuplicateProcessing,
                                                   Date         effectiveTime,
                                                   String       methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail  beanEntity = getEntityFromRepository(userId,
                                                           beanGUID,
                                                           beanGUIDParameterName,
                                                           beanGUIDTypeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        if (beanEntity != null)
        {
            EntityDetail  anchorEntity = validateEntityAndAnchorForRead(userId,
                                                                        beanGUIDTypeName,
                                                                        beanEntity,
                                                                        beanGUIDParameterName,
                                                                        true,
                                                                        true,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

            if (anchorEntity != null)
            {
                securityVerifier.validateUserForAnchorDeclassify(userId,
                                                                 anchorEntity,
                                                                 classificationTypeName,
                                                                 repositoryHelper,
                                                                 serviceName,
                                                                 methodName);
            }
            else
            {
                securityVerifier.validateUserForElementDeclassify(userId,
                                                                  beanEntity,
                                                                  classificationTypeName,
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  methodName);
            }


            /*
             * Look to see if there is an existing classification
             */
            InstanceAuditHeader existingClassification = this.getExistingClassification(beanEntity, classificationTypeName);

            if (existingClassification != null)
            {
                repositoryHandler.declassifyEntity(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   beanEntity.getGUID(),
                                                   beanEntity,
                                                   beanGUIDParameterName,
                                                   beanGUIDTypeName,
                                                   classificationTypeGUID,
                                                   classificationTypeName,
                                                   existingClassification,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);

                final String actionDescriptionTemplate = "Removing %s classification from %s %s";
                String actionDescription = String.format(actionDescriptionTemplate, classificationTypeName, beanGUIDTypeName, beanGUID);

                if (anchorEntity != null)
                {
                    this.addLatestChangeToAnchor(anchorEntity,
                                                 LatestChangeTarget.ATTACHMENT_CLASSIFICATION,
                                                 LatestChangeAction.DELETED,
                                                 classificationTypeName,
                                                 beanGUID,
                                                 beanGUIDTypeName,
                                                 null,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }
                else if (repositoryHelper.isTypeOf(methodName, beanEntity.getType().getTypeDefName(), OpenMetadataType.REFERENCEABLE.typeName))
                {
                    this.addLatestChangeToAnchor(beanEntity,
                                                 LatestChangeTarget.ENTITY_CLASSIFICATION,
                                                 LatestChangeAction.DELETED,
                                                 classificationTypeName,
                                                 null,
                                                 null,
                                                 null,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }
            }
        }
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID unique identifier of classification type
     * @param classificationTypeName unique name of classification type
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateClassificationEffectivityDates(String       userId,
                                                     String       externalSourceGUID,
                                                     String       externalSourceName,
                                                     String       beanGUID,
                                                     String       beanGUIDParameterName,
                                                     String       beanGUIDTypeName,
                                                     String       classificationTypeGUID,
                                                     String       classificationTypeName,
                                                     boolean      forLineage,
                                                     boolean      forDuplicateProcessing,
                                                     Date         effectiveFrom,
                                                     Date         effectiveTo,
                                                     Date         effectiveTime,
                                                     String       methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail  beanEntity = getEntityFromRepository(userId,
                                                           beanGUID,
                                                           beanGUIDParameterName,
                                                           beanGUIDTypeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        EntityDetail  anchorEntity = validateEntityAndAnchorForRead(userId,
                                                                    beanGUIDTypeName,
                                                                    beanEntity,
                                                                    beanGUIDParameterName,
                                                                    true,
                                                                    true,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);

        if (beanEntity != null)
        {
            /*
             * Check that the user is able to change the classification attached to the element.
             */
            if (anchorEntity != null)
            {
                securityVerifier.validateUserForAnchorClassify(userId,
                                                               anchorEntity,
                                                               classificationTypeName,
                                                               repositoryHelper,
                                                               serviceName,
                                                               methodName);
            }
            else
            {
                securityVerifier.validateUserForElementClassify(userId,
                                                                beanEntity,
                                                                classificationTypeName,
                                                                repositoryHelper,
                                                                serviceName,
                                                                methodName);
            }

            Classification existingClassification = this.getExistingClassification(beanEntity, classificationTypeName);

            invalidParameterHandler.validateObject(existingClassification, classificationTypeName, methodName);

            LatestChangeAction latestChangeActionOrdinal = LatestChangeAction.UPDATED;

            InstanceProperties newProperties = existingClassification.getProperties();

            if (newProperties == null)
            {
                newProperties = new InstanceProperties();
            }

            repositoryHandler.reclassifyEntity(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               beanEntity.getGUID(),
                                               beanGUIDParameterName,
                                               beanGUIDTypeName,
                                               classificationTypeGUID,
                                               classificationTypeName,
                                               existingClassification,
                                               this.setUpEffectiveDates(newProperties, effectiveFrom, effectiveTo),
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);

            final String actionDescriptionTemplate = "Updating effectivity dates for %s classification to %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, classificationTypeName, beanGUIDTypeName, beanGUID);

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             LatestChangeTarget.ATTACHMENT_CLASSIFICATION,
                                             latestChangeActionOrdinal,
                                             classificationTypeName,
                                             beanGUID,
                                             beanGUIDTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(methodName, beanEntity.getType().getTypeDefName(), OpenMetadataType.REFERENCEABLE.typeName))
            {
                this.addLatestChangeToAnchor(beanEntity,
                                             LatestChangeTarget.ENTITY_CLASSIFICATION,
                                             latestChangeActionOrdinal,
                                             classificationTypeName,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
    }


    /**
     * Update the effectivity dates of a specific entity .
     * The effectivity dates control the visibility of the entity through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the entity in the repositories
     * @param beanGUIDParameterName parameter name that passed the beanGUID
     * @param beanGUIDTypeGUID type of bean
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateBeanEffectivityDates(String       userId,
                                           String       externalSourceGUID,
                                           String       externalSourceName,
                                           String       beanGUID,
                                           String       beanGUIDParameterName,
                                           String       beanGUIDTypeGUID,
                                           String       beanGUIDTypeName,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           Date         effectiveFrom,
                                           Date         effectiveTo,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(beanGUID, beanGUIDParameterName, methodName);

        EntityDetail  beanEntity = getEntityFromRepository(userId,
                                                           beanGUID,
                                                           beanGUIDParameterName,
                                                           beanGUIDTypeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        EntityDetail  anchorEntity = validateEntityAndAnchorForRead(userId,
                                                                    beanGUIDTypeName,
                                                                    beanEntity,
                                                                    beanGUIDParameterName,
                                                                    true,
                                                                    true,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);

        if (beanEntity != null)
        {
            InstanceProperties newProperties = this.setUpEffectiveDates(null, effectiveFrom, effectiveTo);

            /*
             * Check that the user is able to classify the element.
             */
            if (anchorEntity != null)
            {
                securityVerifier.validateUserForAnchorMemberUpdate(userId,
                                                                   anchorEntity,
                                                                   repositoryHelper,
                                                                   serviceName,
                                                                   methodName);
            }
            else
            {
                securityVerifier.validateUserForElementDetailUpdate(userId,
                                                                    beanEntity,
                                                                    newProperties,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    methodName);
            }


            repositoryHandler.updateEntityProperties(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     beanEntity.getGUID(),
                                                     beanEntity,
                                                     beanGUIDTypeGUID,
                                                     beanGUIDTypeName,
                                                     newProperties,
                                                     methodName);

            final String actionDescriptionTemplate = "Updating effectivity dates for %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, beanGUIDTypeName, beanGUID);

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             LatestChangeTarget.ATTACHMENT_PROPERTY,
                                             LatestChangeAction.UPDATED,
                                             null,
                                             beanGUID,
                                             beanGUIDTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(methodName, beanEntity.getType().getTypeDefName(), OpenMetadataType.REFERENCEABLE.typeName))
            {
                this.addLatestChangeToAnchor(beanEntity,
                                             LatestChangeTarget.ENTITY_PROPERTY,
                                             LatestChangeAction.UPDATED,
                                             null,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
    }


    /**
     * Create the new properties to hand to the repository helper based on the supplied properties,
     * existing properties and whether this is a merge update or not.
     * The effectivity dates are always preserved unless they are over-written by the caller.  If they need clearing then use the separate call.
     *
     * @param isMergeUpdate should the supplied updateProperties be merged with existing properties (true) by replacing just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param suppliedProperties properties from the caller
     * @param existingProperties properties from the repository
     * @return properties to store
     */
    private InstanceProperties setUpNewProperties(boolean            isMergeUpdate,
                                                  InstanceProperties suppliedProperties,
                                                  InstanceProperties existingProperties)
    {
        /*
         * Sort out the properties
         */
        InstanceProperties newProperties;

        if (isMergeUpdate)
        {
            newProperties = existingProperties;

            if (newProperties == null)
            {
                newProperties = suppliedProperties;
            }
            else if (suppliedProperties != null)
            {
                Map<String, InstancePropertyValue>  propertyMap = suppliedProperties.getInstanceProperties();

                for (String propertyName : propertyMap.keySet())
                {
                    if (propertyName != null)
                    {
                        newProperties.setProperty(propertyName, propertyMap.get(propertyName));
                    }
                }

                if (suppliedProperties.getEffectiveFromTime() != null)
                {
                    newProperties.setEffectiveFromTime(suppliedProperties.getEffectiveFromTime());
                }

                if (suppliedProperties.getEffectiveToTime() != null)
                {
                    newProperties.setEffectiveToTime(suppliedProperties.getEffectiveToTime());
                }
            }
        }
        else
        {
            newProperties = suppliedProperties;

            /*
             * Preserve the effectivity dates even though the properties are being replaced.
             */
            if (existingProperties != null)
            {
                if (existingProperties.getEffectiveFromTime() != null)
                {
                    newProperties.setEffectiveFromTime(existingProperties.getEffectiveFromTime());
                }
                if (existingProperties.getEffectiveToTime() != null)
                {
                    newProperties.setEffectiveToTime(existingProperties.getEffectiveToTime());
                }
            }
        }

        return newProperties;
    }


    /**
     * Retrieve a map of entities.
     *
     * @param userId calling users
     * @param typeGUID unique identifier of the type to search for
     * @param conditions list of anchor guid as search conditions
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     * @return map of anchor entity guid to anchor entities - empty map if nothing received
     */
    private Map<String, EntityDetail> getEntityList(String                  userId,
                                                    String                  typeGUID,
                                                    List<PropertyCondition> conditions,
                                                    boolean                 forLineage,
                                                    boolean                 forDuplicateProcessing,
                                                    Date                    effectiveTime,
                                                    String                  methodName)
    {
        Map<String, EntityDetail> retrievedAnchors = new HashMap<>();

        if (!conditions.isEmpty())
        {
            SearchProperties searchProperties = new SearchProperties();

            searchProperties.setConditions(conditions);
            searchProperties.setMatchCriteria(MatchCriteria.ANY);

            try
            {
                List<EntityDetail> retrievedEntities = repositoryHandler.findEntities(userId,
                                                                                      typeGUID,
                                                                                      null,
                                                                                      searchProperties,
                                                                                      null,
                                                                                      null,
                                                                                      null,
                                                                                      null,
                                                                                      SequencingOrder.GUID,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing,
                                                                                      0,
                                                                                      invalidParameterHandler.getMaxPagingSize(),
                                                                                      effectiveTime,
                                                                                      methodName);

                if (retrievedEntities != null)
                {
                    for (EntityDetail entity : retrievedEntities)
                    {
                        if (entity != null)
                        {
                            retrievedAnchors.put(entity.getGUID(), entity);
                        }
                    }
                }
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName, GenericHandlersAuditCode.FAILED_TO_RETRIEVE_ENTITIES.getMessageDefinition(methodName,
                                                                                                                          error.getClass().getName(),
                                                                                                                          error.getMessage()));
            }
        }

        return retrievedAnchors;
    }



    /**
     * Validates that the unique property is not already in use across all types that contain the unique property.
     *
     * @param entityGUID existing entity (or null if this is a create method)
     * @param entityTypeName the unique name of the type of the entity
     * @param uniqueParameterValue the value of the unique parameter
     * @param uniqueParameterName the name of the unique parameter
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method for exceptions and error messages
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem accessing the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void validateUniqueProperty(String entityGUID,
                                       String entityTypeName,
                                       String uniqueParameterValue,
                                       String uniqueParameterName,
                                       Date   effectiveTime,
                                       String methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        List<String> propertyNames = new ArrayList<>();
        propertyNames.add(uniqueParameterName);

        String owningEntityTypeGUID = null;
        String owningEntityTypeName = null;

        String entityTypeNameToCheck = entityTypeName;
        while (owningEntityTypeGUID == null)
        {
            TypeDef potentialOwningEntityTypeDef = repositoryHelper.getTypeDefByName(methodName, entityTypeNameToCheck);
            List<TypeDefAttribute> typeDefAttributes = potentialOwningEntityTypeDef.getPropertiesDefinition();
            if (typeDefAttributes != null && !typeDefAttributes.isEmpty())
            {
                for (TypeDefAttribute typeDefAttribute:typeDefAttributes)
                {
                    if (typeDefAttribute.isUnique() && typeDefAttribute.getAttributeName().equals(uniqueParameterName))
                    {
                        owningEntityTypeGUID = potentialOwningEntityTypeDef.getGUID();
                        owningEntityTypeName = potentialOwningEntityTypeDef.getName();
                        break;
                    }
                }
            }

            TypeDefLink superTypeDefLink = potentialOwningEntityTypeDef.getSuperType();

            if (superTypeDefLink != null)
            {
                entityTypeNameToCheck = superTypeDefLink.getName();
            }
            else
            {
                // Should not happen. Log?
                break;
            }
        }

        /*
         * An entity with the Memento classification set is ignored
         */
        List<EntityDetail> existingEntities = this.getEntitiesByValue(localServerUserId,
                                                                      uniqueParameterValue,
                                                                      uniqueParameterName,
                                                                      owningEntityTypeGUID,
                                                                      owningEntityTypeName,
                                                                      propertyNames,
                                                                      true,
                                                                      false,
                                                                      null,
                                                                      OpenMetadataType.MEMENTO_CLASSIFICATION.typeName,
                                                                      null,
                                                                      null,
                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                      null,
                                                                      false,
                                                                      false,
                                                                      0,
                                                                      invalidParameterHandler.getMaxPagingSize(),
                                                                      effectiveTime,
                                                                      methodName);

        if ((existingEntities != null) && (! existingEntities.isEmpty()))
        {
            if (entityGUID != null)
            {
                for (EntityDetail existingEntity : existingEntities)
                {
                    if ((existingEntity != null) && (! entityGUID.equals(existingEntity.getGUID())))
                    {
                        invalidParameterHandler.throwUniqueNameInUse(uniqueParameterValue,
                                                                     uniqueParameterName,
                                                                     entityTypeName,
                                                                     serviceName,
                                                                     methodName);
                    }
                }
            }
            else
            {
                invalidParameterHandler.throwUniqueNameInUse(uniqueParameterValue,
                                                             uniqueParameterName,
                                                             entityTypeName,
                                                             serviceName,
                                                             methodName);
            }
        }
    }


    /**
     * Validate that new properties for an entity do not have unique properties that clash with other instances.
     *
     * @param entityGUID unique identifier of the entity to be updated (or null for a new entity).
     * @param entityTypeName the unique name of the type of the entity
     * @param newProperties properties to test
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method for exceptions and error messages
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem accessing the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void validateUniqueProperties(String             entityGUID,
                                          String             entityTypeName,
                                          InstanceProperties newProperties,
                                          Date               effectiveTime,
                                          String             methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        InstanceProperties uniqueProperties = repositoryHelper.getUniqueProperties(serviceName,
                                                                                   entityTypeName,
                                                                                   newProperties);

        if ((uniqueProperties != null) && (uniqueProperties.getPropertyCount() > 0))
        {
            Iterator<String> uniquePropertyNames = uniqueProperties.getPropertyNames();

            if (uniquePropertyNames != null)
            {
                while (uniquePropertyNames.hasNext())
                {
                    String uniquePropertyName = uniquePropertyNames.next();

                    if (uniquePropertyName != null)
                    {
                        /*
                         * This code assumes that the unique property is a string - which is fine for all current open metadata types.
                         */
                        InstancePropertyValue uniquePropertyValue = uniqueProperties.getPropertyValue(uniquePropertyName);

                        validateUniqueProperty(entityGUID,
                                               entityTypeName,
                                               uniquePropertyValue.valueAsString(),
                                               uniquePropertyName,
                                               effectiveTime,
                                               methodName);
                    }
                }
            }
        }
    }


    /**
     * Validate that the user has permission to create a new entity
     *
     * @param userId           userId of user making request.
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param newProperties properties for new entity
     * @param classifications classifications for new entity
     * @param instanceStatus status for new entity
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void validateNewEntityRequest(String               userId,
                                  String               entityTypeGUID,
                                  String               entityTypeName,
                                  InstanceProperties   newProperties,
                                  List<Classification> classifications,
                                  InstanceStatus       instanceStatus,
                                  Date                 effectiveTime,
                                  String               methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        validateUniqueProperties(null, entityTypeName, newProperties, effectiveTime, methodName);

        securityVerifier.validateUserForElementCreate(userId,
                                                      entityTypeGUID,
                                                      entityTypeName,
                                                      newProperties,
                                                      classifications,
                                                      instanceStatus,
                                                      repositoryHelper,
                                                      serviceName,
                                                      methodName);
    }


    /**
     * Create the instance properties object for the LatestChange classification.
     *
     * @param latestChangeTargetOrdinal The relationship of element that has been changed to the anchor
     * @param latestChangeActionOrdinal The type of change
     * @param classificationName If a classification name changed, this is its name
     * @param attachmentGUID If an attached entity or relationship changed, this is its unique identifier
     * @param attachmentTypeName If an attached entity or relationship changed, this is its unique type name
     * @param relationshipTypeName if a new relationship has been established, what is the type name of the relationship
     * @param userId The user identifier for the person/system making the change
     * @param actionDescription Description of the change
     * @param methodName calling method
     * @return instance properties object to null
     * @throws TypeErrorException there is a problem with one of the types used in this method - probably a logic error.
     */
    private InstanceProperties getLatestChangeClassificationProperties(int    latestChangeTargetOrdinal,
                                                                       int    latestChangeActionOrdinal,
                                                                       String classificationName,
                                                                       String attachmentGUID,
                                                                       String attachmentTypeName,
                                                                       String relationshipTypeName,
                                                                       String userId,
                                                                       String actionDescription,
                                                                       String methodName) throws TypeErrorException
    {
        InstanceProperties properties =  repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                    null,
                                                                                    OpenMetadataProperty.CHANGE_TARGET.name,
                                                                                    LatestChangeTarget.getOpenTypeGUID(),
                                                                                    LatestChangeTarget.getOpenTypeName(),
                                                                                    latestChangeTargetOrdinal,
                                                                                    methodName);

        properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.CHANGE_ACTION.name,
                                                                LatestChangeAction.getOpenTypeGUID(),
                                                                LatestChangeAction.getOpenTypeName(),
                                                                latestChangeActionOrdinal,
                                                                methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.CLASSIFICATION_NAME.name,
                                                                  classificationName,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ATTACHMENT_GUID.name,
                                                                  attachmentGUID,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ATTACHMENT_TYPE.name,
                                                                  attachmentTypeName,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.RELATIONSHIP_TYPE.name,
                                                                  relationshipTypeName,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.USER_ID.name,
                                                                  userId,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  actionDescription,
                                                                  methodName);

        return properties;
    }


    /**
     * Add details of an update to the anchor entity in the latest change classification.
     *
     * @param anchorEntity the entity to update
     * @param latestChangeTarget the type of instance
     * @param latestChangeAction the type of change
     * @param classificationName if a classification has changed, what is its name
     * @param attachmentGUID if a new relationship has been established, what is the unique identifier of the entity it is connecting to
     * @param attachmentTypeName if a new relationship has been established, what is the type name of the entity it is connecting to
     * @param relationshipTypeName if a new relationship has been established, what is the type name of the relationship
     * @param userId who is the calling user?
     * @param actionDescription what is the description of the activity
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException local server user id not authorized to update LatestChange
     * @throws PropertyServerException logic error because classification type not recognized
     */
    private void addLatestChangeToAnchor(EntityDetail       anchorEntity,
                                         LatestChangeTarget latestChangeTarget,
                                         LatestChangeAction latestChangeAction,
                                         String             classificationName,
                                         String             attachmentGUID,
                                         String             attachmentTypeName,
                                         String             relationshipTypeName,
                                         String             userId,
                                         String             actionDescription,
                                         boolean            forLineage,
                                         boolean            forDuplicateProcessing,
                                         Date               effectiveTime,
                                         String             methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String  guidParameterName = "anchorEntity.getGUID()";

        InstanceProperties newProperties = null;

        String anchorTypeName = anchorEntity.getType().getTypeDefName();

        /*
         * Only adding LatestChange classification to anchors that are interesting to watch.
         */
        if ((repositoryHelper.isTypeOf(serviceName, anchorTypeName, OpenMetadataType.ACTOR.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, anchorTypeName, OpenMetadataType.ASSET.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, anchorTypeName, OpenMetadataType.COLLECTION.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, anchorTypeName, OpenMetadataType.COMMUNITY.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, anchorTypeName, OpenMetadataType.DESIGN_MODEL_ELEMENT.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, anchorTypeName, OpenMetadataType.GLOSSARY_TERM.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, anchorTypeName, OpenMetadataType.GOVERNANCE_DEFINITION.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, anchorTypeName, OpenMetadataType.PROJECT.typeName)))
        {
            if (! OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(classificationName))
            {
                /*
                 * Do not log LatestChange for anchor classification updates
                 */
                try
                {
                    invalidParameterHandler.validateObject(anchorEntity, guidParameterName, methodName);

                    newProperties = this.getLatestChangeClassificationProperties(latestChangeTarget.getOrdinal(),
                                                                                 latestChangeAction.getOrdinal(),
                                                                                 classificationName,
                                                                                 attachmentGUID,
                                                                                 attachmentTypeName,
                                                                                 relationshipTypeName,
                                                                                 userId,
                                                                                 actionDescription,
                                                                                 methodName);

                    Classification classification = repositoryHelper.getClassificationFromEntity(serviceName,
                                                                                                 anchorEntity,
                                                                                                 OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.typeName,
                                                                                                 methodName);
                    if (classification != null)
                    {
                        repositoryHandler.reclassifyEntity(localServerUserId,
                                                           null,
                                                           null,
                                                           anchorEntity.getGUID(),
                                                           guidParameterName,
                                                           anchorEntity.getType().getTypeDefName(),
                                                           OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.typeGUID,
                                                           OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.typeName,
                                                           classification,
                                                           newProperties,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);
                    }
                }
                catch (ClassificationErrorException newClassificationNeeded)
                {
                    /*
                     * This is not an error - it just means that the classification is not present on the anchor entity.
                     */
                    try
                    {
                        repositoryHandler.classifyEntity(localServerUserId,
                                                         null,
                                                         null,
                                                         anchorEntity.getGUID(),
                                                         anchorEntity,
                                                         guidParameterName,
                                                         anchorEntity.getType().getTypeDefName(),
                                                         OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.typeGUID,
                                                         OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.typeName,
                                                         ClassificationOrigin.ASSIGNED,
                                                         null,
                                                         newProperties,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         effectiveTime,
                                                         methodName);
                    }
                    catch (PropertyServerException e)
                    {
                        // Ignore exception, possibly a race condition - the entity is already classified
                    }
                }
                catch (PropertyServerException e)
                {
                    // Ignore exception, possibly a race condition - the entity is already classified
                }
                catch (InvalidParameterException error)
                {
                    throw new PropertyServerException(error);
                }
            }
        }


        /*
         * Check whether this anchor is nested in another anchor.
         */
        AnchorIdentifiers parentAnchorIdentifiers = getAnchorsFromAnchorsClassification(anchorEntity, methodName);

        if ((parentAnchorIdentifiers != null) && (! anchorEntity.getGUID().equals(parentAnchorIdentifiers.anchorGUID)))
        {
            try
            {
                final String parentAnchorGUIDParameterName = "parentAnchorGUID";
                EntityDetail parentAnchorEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                    parentAnchorIdentifiers.anchorGUID,
                                                                                    parentAnchorGUIDParameterName,
                                                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    effectiveTime,
                                                                                    methodName);

                this.addLatestChangeToAnchor(parentAnchorEntity,
                                             latestChangeTarget,
                                             latestChangeAction,
                                             classificationName,
                                             attachmentGUID,
                                             attachmentTypeName,
                                             relationshipTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            catch (InvalidParameterException  error)
            {
                throw new PropertyServerException(error);
            }
        }
    }



    /**
     * Create a new entity in the repository assuming all parameters are ok.
     *
     * @param userId           userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param propertyBuilder builder pre-populated with the properties and classifications of the new entity
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return unique identifier of new entity
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createBeanInRepository(String                        userId,
                                         String                        externalSourceGUID,
                                         String                        externalSourceName,
                                         String                        entityTypeGUID,
                                         String                        entityTypeName,
                                         OpenMetadataAPIGenericBuilder propertyBuilder,
                                         Date                          effectiveTime,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           entityTypeGUID,
                                           entityTypeName,
                                           null,
                                           null,
                                           propertyBuilder,
                                           false,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Create a new entity in the repository assuming all parameters are ok.
     *
     * @param userId           userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param entityDomainName unique name of the type of entity's domain
     * @param entityScopeGUID unique identifier of the scope of this element
     * @param propertyBuilder builder pre-populated with the properties and classifications of the new entity
     * @param isOwnAnchor flag to indicate if the new entity should be anchored to itself
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return unique identifier of new entity
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createBeanInRepository(String                        userId,
                                         String                        externalSourceGUID,
                                         String                        externalSourceName,
                                         String                        entityTypeGUID,
                                         String                        entityTypeName,
                                         String                        entityDomainName,
                                         String                        entityScopeGUID,
                                         OpenMetadataAPIGenericBuilder propertyBuilder,
                                         boolean                       isOwnAnchor,
                                         Date                          effectiveTime,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        if (isOwnAnchor)
        {
            /*
             * A null anchorGUID meant that the element is its own Anchor.
             */
            propertyBuilder.setAnchors(userId, null, entityTypeName, entityDomainName, entityScopeGUID, methodName);

            List<String> governanceZones = securityVerifier.getDefaultZones(propertyBuilder.getInitialGovernanceZones(), userId, entityTypeName, methodName);

            propertyBuilder.setGovernanceZones(userId, governanceZones, methodName);
        }
        else if (propertyBuilder.isClassificationSet(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName))
        {
            /*
             * The new entity is going to be anchored to another element.
             */
            List<String> proposedZones = propertyBuilder.getInitialGovernanceZones();

            if (proposedZones != null)
            {
                Classification anchorClassification = propertyBuilder.getClassification(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);

                String anchorDomainName = repositoryHelper.getStringProperty(serviceName,
                                                                             OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name,
                                                                             anchorClassification.getProperties(),
                                                                             methodName);

                String anchorGUID = repositoryHelper.getStringProperty(serviceName,
                                                                       OpenMetadataProperty.ANCHOR_GUID.name,
                                                                       anchorClassification.getProperties(),
                                                                       methodName);

                throw new InvalidParameterException(GenericHandlersErrorCode.ZONES_ONLY_ON_ANCHOR.getMessageDefinition(methodName,
                                                                                                                       entityTypeName,
                                                                                                                       "<new>",
                                                                                                                       anchorDomainName,
                                                                                                                       anchorGUID),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    OpenMetadataProperty.ZONE_MEMBERSHIP.name);
            }
        }

        validateNewEntityRequest(userId,
                                 entityTypeGUID,
                                 entityTypeName,
                                 propertyBuilder.getInstanceProperties(methodName),
                                 propertyBuilder.getEntityClassifications(),
                                 propertyBuilder.getInstanceStatus(),
                                 effectiveTime,
                                 methodName);

        String entityGUID = repositoryHandler.createEntity(userId,
                                                           entityTypeGUID,
                                                           entityTypeName,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           propertyBuilder.getInstanceProperties(methodName),
                                                           propertyBuilder.getEntityClassifications(),
                                                           propertyBuilder.getInstanceStatus(),
                                                           methodName);


        if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.ASSET.typeName))
        {
            auditLog.logMessage(assetActionDescription,
                                OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_CREATE.getMessageDefinition(userId,
                                                                                                              entityTypeName,
                                                                                                              entityGUID,
                                                                                                              methodName,
                                                                                                              serviceName));
        }

        return entityGUID;
    }


    /**
     * Update one or more properties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param propertyName name of bean property to update
     * @param propertyValue new value for bean property
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the properties tin the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanPropertyInRepository(String  userId,
                                               String  externalSourceGUID,
                                               String  externalSourceName,
                                               String  entityGUID,
                                               String  entityGUIDParameterName,
                                               String  entityTypeGUID,
                                               String  entityTypeName,
                                               String  propertyName,
                                               String  propertyValue,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String propertyParameterName = "propertyValue";
        final String propertyNameParameterName = "propertyName";

        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateObject(propertyValue, propertyParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null, propertyName, propertyValue, methodName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    entityGUID,
                                    entityGUIDParameterName,
                                    entityTypeGUID,
                                    entityTypeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    properties,
                                    true,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Update one or more properties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param properties object containing the properties for the repository instances based on the properties of the bean
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the properties in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanInRepository(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       String             entityGUID,
                                       String             entityGUIDParameterName,
                                       String             entityTypeGUID,
                                       String             entityTypeName,
                                       InstanceProperties properties,
                                       boolean            isMergeUpdate,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    entityGUID,
                                    entityGUIDParameterName,
                                    entityTypeGUID,
                                    entityTypeName,
                                    false,
                                    false,
                                    properties,
                                    isMergeUpdate,
                                    new Date(),
                                    methodName);
    }


    /**
     * Update one or more updateProperties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param updateProperties object containing the properties
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateBeanInRepository(String             userId,
                                          String             externalSourceGUID,
                                          String             externalSourceName,
                                          String             entityGUID,
                                          String             entityGUIDParameterName,
                                          String             entityTypeGUID,
                                          String             entityTypeName,
                                          boolean            forLineage,
                                          boolean            forDuplicateProcessing,
                                          InstanceProperties updateProperties,
                                          boolean            isMergeUpdate,
                                          Date               effectiveTime,
                                          String             methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        entityGUID,
                                                                        entityGUIDParameterName,
                                                                        entityTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        return updateBeanInRepository(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      startingEntity,
                                      entityGUIDParameterName,
                                      entityTypeGUID,
                                      entityTypeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      updateProperties,
                                      isMergeUpdate,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Update one or more updateProperties in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param originalEntity unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param updateProperties object containing the properties
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateBeanInRepository(String             userId,
                                          String             externalSourceGUID,
                                          String             externalSourceName,
                                          EntityDetail       originalEntity,
                                          String             entityGUIDParameterName,
                                          String             entityTypeGUID,
                                          String             entityTypeName,
                                          boolean            forLineage,
                                          boolean            forDuplicateProcessing,
                                          InstanceProperties updateProperties,
                                          boolean            isMergeUpdate,
                                          Date               effectiveTime,
                                          String             methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        if ((originalEntity != null) && (originalEntity.getType() != null))
        {
            EntityDetail anchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                            entityTypeName,
                                                                            originalEntity,
                                                                            entityGUIDParameterName,
                                                                            true,
                                                                            true,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveTime,
                                                                            methodName);

            /*
             * Sort out the properties
             */
            InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                  updateProperties,
                                                                  originalEntity.getProperties());

            /*
             * If there are no properties to change then nothing more to do
             */
            if ((newProperties == null) && (originalEntity.getProperties() == null))
            {
                auditLog.logMessage(methodName,
                                    GenericHandlersAuditCode.IGNORING_UNNECESSARY_ENTITY_UPDATE.getMessageDefinition(originalEntity.getType().getTypeDefName(),
                                                                                                                     originalEntity.getGUID(),
                                                                                                                     methodName,
                                                                                                                     userId));
                return false;
            }

            /*
             * If nothing has changed in the properties then nothing to do
             */
            if ((newProperties != null) && (newProperties.equals(originalEntity.getProperties())))
            {
                auditLog.logMessage(methodName,
                                    GenericHandlersAuditCode.IGNORING_UNNECESSARY_ENTITY_UPDATE.getMessageDefinition(originalEntity.getType().getTypeDefName(),
                                                                                                                     originalEntity.getGUID(),
                                                                                                                     methodName,
                                                                                                                     userId));

                return false;
            }

            /*
             * Validate that any changes to the unique properties do not clash with other entities.
             */
            validateUniqueProperties(originalEntity.getGUID(),
                                     entityTypeName,
                                     updateProperties,
                                     effectiveTime,
                                     methodName);

            /*
             * There is an extra security check if the update is for an element.
             */
            securityVerifier.validateUserForElementDetailUpdate(userId,
                                                                originalEntity,
                                                                newProperties,
                                                                repositoryHelper,
                                                                serviceName,
                                                                methodName);

            repositoryHandler.updateEntityProperties(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     originalEntity.getGUID(),
                                                     originalEntity,
                                                     entityTypeGUID,
                                                     entityTypeName,
                                                     newProperties,
                                                     methodName);

            if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataType.ASSET.typeName))
            {
                auditLog.logMessage(assetActionDescription,
                                    OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE.getMessageDefinition(userId,
                                                                                                                  originalEntity.getType().getTypeDefName(),
                                                                                                                  originalEntity.getGUID(),
                                                                                                                  methodName,
                                                                                                                  serviceName));
            }

            /*
             * Update is OK so record that it occurred in the LatestChange classification if there is an anchor entity.
             */
            final String actionDescriptionTemplate = "Updating properties in %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, entityTypeName, originalEntity.getGUID());

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             LatestChangeTarget.ATTACHMENT_PROPERTY,
                                             LatestChangeAction.UPDATED,
                                             null,
                                             originalEntity.getGUID(),
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.REFERENCEABLE.typeName))
            {
                this.addLatestChangeToAnchor(originalEntity,
                                             LatestChangeTarget.ENTITY_PROPERTY,
                                             LatestChangeAction.UPDATED,
                                             null,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
        else
        {
            String entityGUID = "<null>";

            if (originalEntity != null)
            {
                entityGUID = originalEntity.getGUID();
            }
            invalidParameterHandler.throwUnknownElement(userId,
                                                        entityGUID,
                                                        entityTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }

        return true;
    }


    /**
     * Update the instance status in the requested entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param newStatus new status value
     * @param newStatusParameterName parameter providing the new status value
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateBeanStatusInRepository(String             userId,
                                             String             externalSourceGUID,
                                             String             externalSourceName,
                                             String             entityGUID,
                                             String             entityGUIDParameterName,
                                             String             entityTypeGUID,
                                             String             entityTypeName,
                                             boolean            forLineage,
                                             boolean            forDuplicateProcessing,
                                             InstanceStatus     newStatus,
                                             String             newStatusParameterName,
                                             Date               effectiveTime,
                                             String             methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(newStatus, newStatusParameterName, methodName);

        if ((newStatus == InstanceStatus.DELETED) || (newStatus == InstanceStatus.UNKNOWN))
        {
            invalidParameterHandler.throwInvalidParameter(newStatus, newStatusParameterName, methodName);
        }

        /*
         * This returns the entity for the connectTo element and validates it is of the correct type.
         */
        EntityDetail  originalEntity = repositoryHandler.getEntityByGUID(userId,
                                                                         entityGUID,
                                                                         entityGUIDParameterName,
                                                                         entityTypeName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        if ((originalEntity != null) && (originalEntity.getType() != null))
        {
            EntityDetail anchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                            entityTypeName,
                                                                            originalEntity,
                                                                            entityGUIDParameterName,
                                                                            true,
                                                                            true,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveTime,
                                                                            methodName);


            /*
             * Check that such updates are allowed
             */
            securityVerifier.validateUserForAnchorMemberStatusUpdate(userId,
                                                                     anchorEntity,
                                                                     originalEntity,
                                                                     newStatus,
                                                                     repositoryHelper,
                                                                     serviceName,
                                                                     methodName);

            repositoryHandler.updateEntityStatus(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 originalEntity.getGUID(),
                                                 originalEntity,
                                                 entityTypeGUID,
                                                 entityTypeName,
                                                 newStatus,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);

            if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataType.ASSET.typeName))
            {
                auditLog.logMessage(assetActionDescription,
                                    OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE.getMessageDefinition(userId,
                                                                                                                  originalEntity.getType().getTypeDefName(),
                                                                                                                  originalEntity.getGUID(),
                                                                                                                  methodName,
                                                                                                                  serviceName));
            }

            /*
             * Update is OK so record that it occurred in the LatestChange classification if there is an anchor entity.
             */
            final String actionDescriptionTemplate = "Updating instance status in %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, entityTypeName, entityGUID);

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             LatestChangeTarget.ATTACHMENT_STATUS,
                                             LatestChangeAction.UPDATED,
                                             null,
                                             entityGUID,
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.REFERENCEABLE.typeName))
            {
                this.addLatestChangeToAnchor(originalEntity,
                                             LatestChangeTarget.ENTITY_STATUS,
                                             LatestChangeAction.UPDATED,
                                             null,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
        else
        {
            invalidParameterHandler.throwUnknownElement(userId,
                                                        entityGUID,
                                                        entityTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }
    }



    /**
     * Update the zones for a specific anchor element to the list set up in the user's publish zones.
     *
     * @param userId calling user
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param entityGUID unique identifier for the asset to update
     * @param entityGUIDParameterName parameter name supplying entityGUID
     * @param entityTypeName expected type of entity
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException guid or userId is null or this is not an anchor entity
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void publishElement(String  userId,
                               String  externalSourceGUID,
                               String  externalSourceName,
                               String  entityGUID,
                               String  entityGUIDParameterName,
                               String  entityTypeName,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        /*
         * This returns the entity for the connectTo element and validates it is of the correct type.
         */
        EntityDetail  originalEntity = repositoryHandler.getEntityByGUID(userId,
                                                                         entityGUID,
                                                                         entityGUIDParameterName,
                                                                         entityTypeName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        if ((originalEntity != null) && (originalEntity.getType() != null))
        {
            AnchorIdentifiers anchorIdentifiers = this.getAnchorsFromAnchorsClassification(originalEntity, methodName);

            if ((anchorIdentifiers == null) || (anchorIdentifiers.anchorGUID == null) || (anchorIdentifiers.anchorGUID.equals(originalEntity.getGUID())))
            {
                /*
                 * The entity is either its own anchor or is unanchored.  This means it is ok to set the zone membership.
                 */
                OpenMetadataAPIGenericBuilder builder = new OpenMetadataAPIGenericBuilder(originalEntity.getType().getTypeDefGUID(),
                                                                                          originalEntity.getType().getTypeDefName(),
                                                                                          repositoryHelper,
                                                                                          serviceName,
                                                                                          serverName);

                List<String> elementZones = securityVerifier.getPublishZones(this.getEntityZones(originalEntity),
                                                                             userId,
                                                                             originalEntity.getType().getTypeDefName(),
                                                                             methodName);
                this.setClassificationInRepository(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   originalEntity.getGUID(),
                                                   entityGUIDParameterName,
                                                   originalEntity.getType().getTypeDefName(),
                                                   OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeGUID,
                                                   OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                   builder.getZoneMembershipProperties(elementZones, methodName),
                                                   true,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
            }
            else
            {
                throw new InvalidParameterException(GenericHandlersErrorCode.ZONES_ONLY_ON_ANCHOR.getMessageDefinition(methodName,
                                                                                                                       originalEntity.getType().getTypeDefName(),
                                                                                                                       originalEntity.getGUID(),
                                                                                                                       anchorIdentifiers.anchorDomainName,
                                                                                                                       anchorIdentifiers.anchorGUID),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    OpenMetadataProperty.ZONE_MEMBERSHIP.name);
            }
        }
    }


    /**
     * Update the zones for a specific asset to the list set up in publish zones.
     *
     * @param userId calling user
     * @param externalSourceGUID      unique identifier of the software capability that owns this element
     * @param externalSourceName      unique name of the software capability that owns this element
     * @param entityGUID unique identifier for the asset to update
     * @param entityGUIDParameterName parameter name supplying entityGUID
     * @param entityTypeName expected type of entity
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void withdrawElement(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  entityGUID,
                                String  entityGUIDParameterName,
                                String  entityTypeName,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        /*
         * This returns the entity for the connectTo element and validates it is of the correct type.
         */
        EntityDetail  originalEntity = repositoryHandler.getEntityByGUID(userId,
                                                                         entityGUID,
                                                                         entityGUIDParameterName,
                                                                         entityTypeName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        if ((originalEntity != null) && (originalEntity.getType() != null))
        {
            AnchorIdentifiers anchorIdentifiers = this.getAnchorsFromAnchorsClassification(originalEntity, methodName);

            if ((anchorIdentifiers == null) || (anchorIdentifiers.anchorGUID == null) || (anchorIdentifiers.anchorGUID.equals(originalEntity.getGUID())))
            {
                /*
                 * The entity is either its own anchor or is unanchored.  This means it is ok to set the zone membership.
                 */
                OpenMetadataAPIGenericBuilder builder = new OpenMetadataAPIGenericBuilder(originalEntity.getType().getTypeDefGUID(),
                                                                                          originalEntity.getType().getTypeDefName(),
                                                                                          repositoryHelper,
                                                                                          serviceName,
                                                                                          serverName);

                List<String> elementZones = securityVerifier.getDefaultZones(this.getEntityZones(originalEntity),
                                                                             userId,
                                                                             originalEntity.getType().getTypeDefName(),
                                                                             methodName);
                this.setClassificationInRepository(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   originalEntity.getGUID(),
                                                   entityGUIDParameterName,
                                                   originalEntity.getType().getTypeDefName(),
                                                   OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeGUID,
                                                   OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                   builder.getZoneMembershipProperties(elementZones, methodName),
                                                   true,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
            }
            else
            {
                throw new InvalidParameterException(GenericHandlersErrorCode.ZONES_ONLY_ON_ANCHOR.getMessageDefinition(methodName,
                                                                                                                       originalEntity.getType().getTypeDefName(),
                                                                                                                       originalEntity.getGUID(),
                                                                                                                       anchorIdentifiers.anchorDomainName,
                                                                                                                       anchorIdentifiers.anchorGUID),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    OpenMetadataProperty.ZONE_MEMBERSHIP.name);
            }
        }
    }


    /**
     * Undo the last update to the entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param originalEntity unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeName unique name of the entity's type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return recovered bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the new properties to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B    undoBeanUpdateInRepository(String             userId,
                                           String             externalSourceGUID,
                                           String             externalSourceName,
                                           EntityDetail       originalEntity,
                                           String             entityGUIDParameterName,
                                           String             entityTypeName,
                                           boolean            forLineage,
                                           boolean            forDuplicateProcessing,
                                           Date               effectiveTime,
                                           String             methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String originalEntityParameterName = "originalEntity";

        invalidParameterHandler.validateUserId(userId, methodName);

        if ((originalEntity != null) && (originalEntity.getType() != null))
        {
            EntityDetail anchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                            entityTypeName,
                                                                            originalEntity,
                                                                            entityGUIDParameterName,
                                                                            true,
                                                                            true,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveTime,
                                                                            methodName);

            EntityDetail recoveredEntity = repositoryHandler.undoEntityUpdate(userId,
                                                                              externalSourceGUID,
                                                                              externalSourceName,
                                                                              originalEntity.getGUID(),
                                                                              methodName);

            /*
             * There is an extra security check if the update is for an asset or glossary.
             */
            try
            {
                securityVerifier.validateUserForElementDetailUpdate(userId,
                                                                    originalEntity,
                                                                    recoveredEntity.getProperties(),
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    methodName);
            }
            catch (UserNotAuthorizedException notAuth)
            {
                /*
                 * Put the original entity back.
                 */
                repositoryHandler.updateEntity(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               originalEntity.getGUID(),
                                               originalEntityParameterName,
                                               originalEntity.getType().getTypeDefGUID(),
                                               originalEntity.getType().getTypeDefName(),
                                               originalEntity.getProperties(),
                                               originalEntity.getClassifications(),
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
                                               methodName);
                throw notAuth;
            }

            if (repositoryHelper.isTypeOf(serviceName, originalEntity.getType().getTypeDefName(), OpenMetadataType.ASSET.typeName))
            {
                auditLog.logMessage(assetActionDescription,
                                    OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE.getMessageDefinition(userId,
                                                                                                                  originalEntity.getType().getTypeDefName(),
                                                                                                                  originalEntity.getGUID(),
                                                                                                                  methodName,
                                                                                                                  serviceName));
            }

            /*
             * Update is OK so record that it occurred in the LatestChange classification if there is an anchor entity.
             */
            final String actionDescriptionTemplate = "Undo last update of properties in %s %s";
            String actionDescription = String.format(actionDescriptionTemplate, entityTypeName, originalEntity.getGUID());

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             LatestChangeTarget.ATTACHMENT_PROPERTY,
                                             LatestChangeAction.UPDATED,
                                             null,
                                             originalEntity.getGUID(),
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.REFERENCEABLE.typeName))
            {
                this.addLatestChangeToAnchor(originalEntity,
                                             LatestChangeTarget.ENTITY_PROPERTY,
                                             LatestChangeAction.UPDATED,
                                             null,
                                             null,
                                             null,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }

            if (recoveredEntity != null)
            {
                return converter.getNewBean(beanClass, recoveredEntity, methodName);
            }
        }
        else
        {
            String entityGUID = "<null>";

            if (originalEntity != null)
            {
                entityGUID = originalEntity.getGUID();
            }
            invalidParameterHandler.throwUnknownElement(userId,
                                                        entityGUID,
                                                        entityTypeName,
                                                        serviceName,
                                                        serverName,
                                                        methodName);
        }

        return null;
    }


    /**
     * Classify as a Memento any entity if it is anchored to the anchor entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorEntity entity anchor to match against
     * @param processedGUIDs entities that have already been processed
     * @param potentialAnchoredEntity entity to validate
     * @param classificationOriginGUID original entity that the Memento classification  was attached to
     * @param classificationProperties properties for the classification
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException problem with the parameters
     * @throws PropertyServerException problem in the repository services
     * @throws UserNotAuthorizedException calling user is not authorize to issue this request
     */
    private void archiveAnchoredEntity(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       EntityDetail       anchorEntity,
                                       List<String>       processedGUIDs,
                                       EntityProxy        potentialAnchoredEntity,
                                       String             classificationOriginGUID,
                                       InstanceProperties classificationProperties,
                                       boolean            forLineage,
                                       boolean            forDuplicateProcessing,
                                       Date               effectiveTime,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        /*
         * Only need to progress if anchor entity exists.
         */
        if (anchorEntity != null)
        {
            final String guidParameterName = "potentialAnchoredEntity";

            if ((potentialAnchoredEntity != null) && (potentialAnchoredEntity.getType() != null))
            {
                EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                        potentialAnchoredEntity.getGUID(),
                                                                        guidParameterName,
                                                                        potentialAnchoredEntity.getType().getTypeDefName(),
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

                AnchorIdentifiers anchorIdentifiers = this.getAnchorsFromAnchorsClassification(entity, methodName);

                if ((anchorIdentifiers != null) && (anchorIdentifiers.anchorGUID.equals(anchorEntity.getGUID())))
                {
                    this.archiveBeanInRepository(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 entity.getGUID(),
                                                 guidParameterName,
                                                 potentialAnchoredEntity.getType().getTypeDefName(),
                                                 ClassificationOrigin.PROPAGATED,
                                                 classificationOriginGUID,
                                                 classificationProperties,
                                                 anchorEntity,
                                                 processedGUIDs,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }
            }
        }
    }


    /**
     * Classify an entity in the repository to show that its asset/artifact counterpart in the real world has either
     * been deleted or archived. Note, this method is designed to work only on anchor entities or entities with no anchor.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeName unique name of the entity's type
     * @param classificationProperties properties for the classification
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void archiveBeanInRepository(String             userId,
                                        String             externalSourceGUID,
                                        String             externalSourceName,
                                        String             entityGUID,
                                        String             entityGUIDParameterName,
                                        String             entityTypeName,
                                        InstanceProperties classificationProperties,
                                        boolean            forLineage,
                                        boolean            forDuplicateProcessing,
                                        Date               effectiveTime,
                                        String             methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        EntityDetail anchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                        entityGUID,
                                                                        entityGUIDParameterName,
                                                                        entityTypeName,
                                                                        true,
                                                                        true,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        /*
         * At this point, archiving is only supported on the anchor entity.  This needs to change (eg to be able to archive schema elements)
         * by adding logic very similar to the templating logic that makes sure the archive processing travels down the hierarchy and does not
         * cover the whole anchored entity.
         */
        invalidParameterHandler.validateAnchorGUID(entityGUID,
                                                   entityGUIDParameterName,
                                                   anchorEntity,
                                                   entityGUID,
                                                   entityTypeName,
                                                   methodName);

        EntityDetail entity = this.archiveBeanInRepository(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           entityGUID,
                                                           entityGUIDParameterName,
                                                           entityTypeName,
                                                           ClassificationOrigin.ASSIGNED,
                                                           entityGUID,
                                                           classificationProperties,
                                                           anchorEntity,
                                                           new ArrayList<>(),
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        /*
         * Update the LatestChange in the archived entity.
         */
        if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.REFERENCEABLE.typeName))
        {
            final String actionDescriptionTemplate = "Classifying as Memento %s %s";

            String actionDescription  = String.format(actionDescriptionTemplate, entityTypeName, entityGUID);

            if (anchorEntity != null)
            {
                this.addLatestChangeToAnchor(anchorEntity,
                                             LatestChangeTarget.ATTACHMENT_CLASSIFICATION,
                                             LatestChangeAction.CREATED,
                                             OpenMetadataType.MEMENTO_CLASSIFICATION.typeName,
                                             entityGUID,
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else if (entity != null)
            {
                this.addLatestChangeToAnchor(entity,
                                             LatestChangeTarget.ENTITY_CLASSIFICATION,
                                             LatestChangeAction.CREATED,
                                             OpenMetadataType.MEMENTO_CLASSIFICATION.typeName,
                                             entityGUID,
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
    }


    /**
     * Classify an entity in the repository to show that its asset/artifact counterpart in the real world has either
     * been deleted or archived.  Note that this classification is propagated to all elements with the same
     * AnchorGUID.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName parameter name supplying entityGUID
     * @param entityTypeName unique name of the entity's type
     * @param classificationOrigin is this classification assigned or propagated?
     * @param classificationOriginGUID which entity did a propagated classification originate from?
     * @param classificationProperties properties for the classification
     * @param anchorEntity anchor entity for the bean (can be null)
     * @param processedGUIDs list of processed GUIDs
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return GUID of archived entity
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repository.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private EntityDetail archiveBeanInRepository(String               userId,
                                                 String               externalSourceGUID,
                                                 String               externalSourceName,
                                                 String               entityGUID,
                                                 String               entityGUIDParameterName,
                                                 String               entityTypeName,
                                                 ClassificationOrigin classificationOrigin,
                                                 String               classificationOriginGUID,
                                                 InstanceProperties   classificationProperties,
                                                 EntityDetail         anchorEntity,
                                                 List<String>         processedGUIDs,
                                                 boolean              forLineage,
                                                 boolean              forDuplicateProcessing,
                                                 Date                 effectiveTime,
                                                 String               methodName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        /*
         * This is to pick up any errors in the iteration through the anchored elements.
         */
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        EntityDetail targetEntity = repositoryHandler.getEntityByGUID(userId,
                                                                      entityGUID,
                                                                      entityGUIDParameterName,
                                                                      entityTypeName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

        if (targetEntity != null)
        {
            processedGUIDs.add(targetEntity.getGUID());

            /*
             * Retrieve the entities attached to this element.  Any entity that is anchored, directly or indirectly, to the anchor entity is archived.
             */
            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           invalidParameterHandler,
                                                                                           userId,
                                                                                           targetEntity,
                                                                                           entityTypeName,
                                                                                           null,
                                                                                           null,
                                                                                           0,
                                                                                           null,
                                                                                           null,
                                                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                                                           null,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           effectiveTime,
                                                                                           methodName);

            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();
                EntityProxy  otherEnd     = repositoryHandler.getOtherEnd(targetEntity.getGUID(), entityTypeName, relationship, 0, methodName);

                if (! processedGUIDs.contains(otherEnd.getGUID()))
                {
                    if (anchorEntity == null)
                    {
                        this.archiveAnchoredEntity(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   targetEntity,
                                                   processedGUIDs,
                                                   otherEnd,
                                                   classificationOriginGUID,
                                                   classificationProperties,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
                    }
                    else
                    {
                        this.archiveAnchoredEntity(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   anchorEntity,
                                                   processedGUIDs,
                                                   otherEnd,
                                                   classificationOriginGUID,
                                                   classificationProperties,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
                    }
                }
            }

            repositoryHandler.classifyEntity(userId,
                                             null,
                                             null,
                                             targetEntity.getGUID(),
                                             targetEntity,
                                             entityGUIDParameterName,
                                             entityTypeName,
                                             OpenMetadataType.MEMENTO_CLASSIFICATION.typeGUID,
                                             OpenMetadataType.MEMENTO_CLASSIFICATION.typeName,
                                             classificationOrigin,
                                             classificationOriginGUID,
                                             classificationProperties,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);


            /*
             * Update the qualified name in the archived entity.
             */
            if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.REFERENCEABLE.typeName))
            {
                String qualifiedName = repositoryHelper.getStringProperty(serviceName,
                                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                          targetEntity.getProperties(),
                                                                          methodName) + "_archivedOn_" + new Date();

                String entityTypeGUID = invalidParameterHandler.validateTypeName(entityTypeName,
                                                                                 OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                 serviceName,
                                                                                 methodName,
                                                                                 repositoryHelper);
                this.updateBeanPropertyInRepository(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    entityGUID,
                                                    entityGUIDParameterName,
                                                    entityTypeGUID,
                                                    entityTypeName,
                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                    qualifiedName,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
            }
        }

        return targetEntity;
    }


    /**
     * Remove an entity if it is anchored to the anchor entity or any of the deleted entities.
     *
     * @param anchorEntityGUID original entity's anchor
     * @param deletedEntityGUIDs entities that have been deleted
     * @param externalSourceGUID unique identifier (guid) for the external source, or null for local.
     * @param externalSourceName unique name for the external source.
     * @param potentialAnchoredEntity entity to validate
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException problem with the parameters
     * @throws PropertyServerException problem in the repository services
     * @throws UserNotAuthorizedException calling user is not authorize to issue this request
     */
    public void deleteIfAppropriatelyAnchoredEntity(String       anchorEntityGUID,
                                                    List<String> deletedEntityGUIDs,
                                                    String       externalSourceGUID,
                                                    String       externalSourceName,
                                                    EntityProxy  potentialAnchoredEntity,
                                                    boolean      cascadedDelete,
                                                    boolean      forLineage,
                                                    boolean      forDuplicateProcessing,
                                                    Date         effectiveTime,
                                                    String       methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String guidParameterName = "potentialAnchoredEntity";

        /*
         * Only need to progress if anchor entity exists.
         */
        EntityDetail entity = repositoryHandler.getEntityByGUID(localServerUserId,
                                                                potentialAnchoredEntity.getGUID(),
                                                                guidParameterName,
                                                                potentialAnchoredEntity.getType().getTypeDefName(),
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime,
                                                                methodName);

        if (entity != null)
        {
            AnchorIdentifiers anchorIdentifiers = this.getAnchorsFromAnchorsClassification(entity, methodName);

            if ((anchorIdentifiers != null) && (anchorIdentifiers.anchorGUID != null))
            {
                /*
                 * Only delete if the anchor matches one of the deleted entities.
                 */
                if (deletedEntityGUIDs.contains(anchorIdentifiers.anchorGUID))
                {
                    this.deleteAnchoredBeanInRepository(localServerUserId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        entity.getGUID(),
                                                        guidParameterName,
                                                        potentialAnchoredEntity.getType().getTypeDefGUID(),
                                                        potentialAnchoredEntity.getType().getTypeDefName(),
                                                        cascadedDelete,
                                                        null,
                                                        null,
                                                        anchorEntityGUID,
                                                        deletedEntityGUIDs,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
                }
            }
        }
    }


    /**
     * Delete an entity from the repository.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @param validatingPropertyName name of property to verify - or null if no verification is required
     * @param validatingPropertyValue value of property to verify
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteBeanInRepository(String       userId,
                                       String       externalSourceGUID,
                                       String       externalSourceName,
                                       String       entityGUID,
                                       String       entityGUIDParameterName,
                                       String       entityTypeGUID,
                                       String       entityTypeName,
                                       boolean      cascadedDelete,
                                       String       validatingPropertyName,
                                       String       validatingPropertyValue,
                                       boolean      forLineage,
                                       boolean      forDuplicateProcessing,
                                       Date         effectiveTime,
                                       String       methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        EntityDetail  entity = repositoryHandler.getEntityByGUID(userId,
                                                                 entityGUID,
                                                                 entityGUIDParameterName,
                                                                 entityTypeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);

        EntityDetail anchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                        entityTypeName,
                                                                        entity,
                                                                        entityGUIDParameterName,
                                                                        true,
                                                                        false,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        List<String> deletedEntityGUIDs = new ArrayList<>();
        deletedEntityGUIDs.add(entity.getGUID());

        /*
         * The call above has validated that the entity to delete exists.
         * The anchorEntity is only set up if the entity to delete has an anchor entity.
         * This means it is not an anchor entity itself or without an anchor.
         */
        if (anchorEntity != null)
        {
            this.deleteAnchoredBeanInRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                entityGUID,
                                                entityGUIDParameterName,
                                                entityTypeGUID,
                                                entityTypeName,
                                                cascadedDelete,
                                                validatingPropertyName,
                                                validatingPropertyValue,
                                                anchorEntity.getGUID(),
                                                deletedEntityGUIDs,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);

            /*
             * Update the LatestChange in the anchor entity if it is not the instance we have just deleted.
             */
            if (! entityGUID.equals(anchorEntity.getGUID()))
            {
                final String actionDescriptionTemplate = "Deleting %s %s";

                String actionDescription  = String.format(actionDescriptionTemplate, entityTypeName, entityGUID);

                this.addLatestChangeToAnchor(anchorEntity,
                                             LatestChangeTarget.ATTACHMENT,
                                             LatestChangeAction.DELETED,
                                             null,
                                             entityGUID,
                                             entityTypeName,
                                             null,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }
        else
        {
            this.deleteAnchoredBeanInRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                entityGUID,
                                                entityGUIDParameterName,
                                                entityTypeGUID,
                                                entityTypeName,
                                                cascadedDelete,
                                                validatingPropertyName,
                                                validatingPropertyValue,
                                                entity.getGUID(),
                                                deletedEntityGUIDs,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
        }
    }


    /**
     * Delete an entity from the repository and all elements anchored to it, and any element anchored to the anchored elements
     * that are deleted.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param entityGUID unique identifier of object to update
     * @param entityGUIDParameterName name of parameter supplying the GUID
     * @param entityTypeGUID unique identifier of the entity's type
     * @param entityTypeName unique name of the entity's type
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @param validatingPropertyName name of property to verify - null if no verification is required
     * @param validatingPropertyValue value of property to verify
     * @param anchorEntityGUID unique identifier of the anchor entity
     * @param deletedEntityGUIDs list of entities that have been deleted
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem removing the properties from the repository.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteAnchoredBeanInRepository(String       userId,
                                               String       externalSourceGUID,
                                               String       externalSourceName,
                                               String       entityGUID,
                                               String       entityGUIDParameterName,
                                               String       entityTypeGUID,
                                               String       entityTypeName,
                                               boolean      cascadedDelete,
                                               String       validatingPropertyName,
                                               String       validatingPropertyValue,
                                               String       anchorEntityGUID,
                                               List<String> deletedEntityGUIDs,
                                               boolean      forLineage,
                                               boolean      forDuplicateProcessing,
                                               Date         effectiveTime,
                                               String       methodName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entityGUID, entityGUIDParameterName, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        entityGUID,
                                                                        entityGUIDParameterName,
                                                                        entityTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        EntityDetail anchorEntity = validateEntityAndAnchorForRead(userId, entityTypeName, startingEntity, entityGUIDParameterName, true, true, forLineage, forDuplicateProcessing, effectiveTime, methodName);

        if ((anchorEntity != null) && (anchorEntity.getGUID().equals(startingEntity.getGUID())))
        {
            securityVerifier.validateUserForElementDelete(userId,
                                                          startingEntity,
                                                          repositoryHelper,
                                                          serviceName,
                                                          methodName);
        }
        else
        {
            securityVerifier.validateUserForAnchorMemberDelete(userId,
                                                               anchorEntity,
                                                               startingEntity,
                                                               repositoryHelper,
                                                               serviceName,
                                                               methodName);
        }


        /*
         * Check that the cascaded delete option is true or there are no protected relationships present.
         */
        validateCascadedDelete(userId, externalSourceGUID, externalSourceName, startingEntity, cascadedDelete, forLineage, forDuplicateProcessing, effectiveTime);

        /*
         * The delete request is ok to proceed
         */
        deletedEntityGUIDs.add(entityGUID);

        /*
         * Retrieve the entities attached to this element.  Any entity that is anchored, directly or indirectly, to the anchor entity is deleted.
         * (This is why we explicitly delete the relationship to the parent element before calling this method).
         */
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       startingEntity,
                                                                                       entityTypeName,
                                                                                       null,
                                                                                       null,
                                                                                       0,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            repositoryHandler.removeRelationship(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 relationship,
                                                 methodName);

            /*
             * Ascertain if the entity at the other end of the relationship should be deleted
             */
            this.deleteIfAppropriatelyAnchoredEntity(anchorEntityGUID,
                                                     deletedEntityGUIDs,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     repositoryHandler.getOtherEnd(startingEntity.getGUID(),
                                                                                   entityTypeName,
                                                                                   relationship,
                                                                                   0,
                                                                                   methodName),
                                                     cascadedDelete,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);
        }

        /*
         * This method explicitly removes all relationships attached to the entity before it deleted the entity.  This ensures that repository
         * events are created for all the relationships.  This is why the code above needs to deal with the nested entities first.
         */
        repositoryHandler.removeEntity(userId,
                                       externalSourceGUID,
                                       externalSourceName,
                                       entityGUID,
                                       entityGUIDParameterName,
                                       entityTypeGUID,
                                       entityTypeName,
                                       validatingPropertyName,
                                       validatingPropertyValue,
                                       null,
                                       null,
                                       SequencingOrder.CREATION_DATE_RECENT,
                                       null,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Check for relationships that would prevent a simple (non-cascaded) delete
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param startingEntity     starting entity
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException delete not allowed
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException problem with access
     */
    private void validateCascadedDelete(String       userId,
                                        String       externalSourceGUID,
                                        String       externalSourceName,
                                        EntityDetail startingEntity,
                                        boolean      cascadedDelete,
                                        boolean      forLineage,
                                        boolean      forDuplicateProcessing,
                                        Date         effectiveTime) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        if (cascadedDelete)
        {
            if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.DATABASE.typeName))
            {
                this.removeLinkedDatabaseSchemas(userId, externalSourceGUID, externalSourceName, startingEntity.getGUID(), forLineage, forDuplicateProcessing, effectiveTime);
            }
            else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.FILE_FOLDER.typeName))
            {
                this.removeFolderContents(userId, externalSourceGUID, externalSourceName, startingEntity.getGUID(), forLineage, forDuplicateProcessing, effectiveTime);
            }
            else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.IT_INFRASTRUCTURE.typeName))
            {
                this.removeDeployedAssets(userId, externalSourceGUID, externalSourceName, startingEntity.getGUID(), forLineage, forDuplicateProcessing, effectiveTime);
            }
            else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.DATA_STRUCTURE.typeName))
            {
                this.removeDataStructureMembers(userId, externalSourceGUID, externalSourceName, startingEntity.getGUID(), forLineage, forDuplicateProcessing, effectiveTime);
            }
            else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.DATA_FIELD.typeName))
            {
                this.removeNestedDataFields(userId, externalSourceGUID, externalSourceName, startingEntity.getGUID(), forLineage, forDuplicateProcessing, effectiveTime);
            }
            else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.DATA_CLASS.typeName))
            {
                this.removeLinkedDataClasses(userId, externalSourceGUID, externalSourceName, startingEntity.getGUID(), forLineage, forDuplicateProcessing, effectiveTime);
            }
            else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.COMMENT.typeName))
            {
                this.removeNestedComments(userId, externalSourceGUID, externalSourceName, startingEntity.getGUID(), forLineage, forDuplicateProcessing, effectiveTime);
            }
            else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.COLLECTION.typeName))
            {
                this.removeCollectionMembers(userId, externalSourceGUID, externalSourceName, startingEntity.getGUID(), forLineage, forDuplicateProcessing, effectiveTime);
            }

            return;
        }

        if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.ASSET.typeName))
        {
            /*
             * Note this is a special case because there is no equivalent remove method for DataSetContent relationship.
             */
            validateNoDataSetContent(userId, startingEntity, forLineage, forDuplicateProcessing, effectiveTime);

            if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName))
            {
                validateEmptyDatabaseSchema(userId, startingEntity, forLineage, forDuplicateProcessing, effectiveTime);
            }
            else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.FILE_FOLDER.typeName))
            {
                validateEmptyFolder(userId, startingEntity, forLineage, forDuplicateProcessing, effectiveTime);
            }
            else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.IT_INFRASTRUCTURE.typeName))
            {
                validateNoDeployedAssets(userId, startingEntity, forLineage, forDuplicateProcessing, effectiveTime);
            }
        }
        else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.COMMENT.typeName))
        {
            validateNoNestedComments(userId, startingEntity, forLineage, forDuplicateProcessing, effectiveTime);
        }
        else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.COLLECTION.typeName))
        {
            validateNoCollectionMembers(userId, startingEntity, forLineage, forDuplicateProcessing, effectiveTime);
        }
        else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.DATA_STRUCTURE.typeName))
        {
            validateNoMemberDataFields(userId, startingEntity, forLineage, forDuplicateProcessing, effectiveTime);
        }
        else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.DATA_FIELD.typeName))
        {
            validateNoNestedDataFields(userId, startingEntity, forLineage, forDuplicateProcessing, effectiveTime);
        }
        else if (repositoryHelper.isTypeOf(serviceName, startingEntity.getType().getTypeDefName(), OpenMetadataType.DATA_CLASS.typeName))
        {
            validateNoDependentDataClasses(userId, startingEntity, forLineage, forDuplicateProcessing, effectiveTime);
        }
    }


    /**
     * Remove any database schemas connected to the database asset by the DataSetContent relationship.
     * This is because the database schemas are not anchored to the database
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param databaseGUID unique identifier for database asset
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeLinkedDatabaseSchemas(String  userId,
                                            String  externalSourceGUID,
                                            String  externalSourceName,
                                            String  databaseGUID,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "removeLinkedDatabaseSchemas";

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       databaseGUID,
                                                                                       OpenMetadataType.DATABASE.typeName,
                                                                                       OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                                       1,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       0,
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if ((relationship != null) && (repositoryHelper.isTypeOf(serviceName,
                                                                     relationship.getEntityOneProxy().getType().getTypeDefName(),
                                                                     OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName)))
            {
                final String elementGUIDParameterName = "relationship.getEntityOneProxy().getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityOneProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeGUID,
                                            OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                            true,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }
    }


    /**
     * Remove any data classes connected to the data class by the DataClassComposition or DataClassHierarchy relationships.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param dataClassGUID unique identifier for glossary
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeLinkedDataClasses(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  dataClassGUID,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "removeLinkedDataClasses";

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       dataClassGUID,
                                                                                       OpenMetadataType.DATA_CLASS.typeName,
                                                                                       OpenMetadataType.DATA_CLASS_COMPOSITION_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.DATA_CLASS_COMPOSITION_RELATIONSHIP.typeName,
                                                                                       2,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       0,
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                final String elementGUIDParameterName = "dataClass.getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataType.DATA_CLASS.typeGUID,
                                            OpenMetadataType.DATA_CLASS.typeName,
                                            true,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }

        iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                       invalidParameterHandler,
                                                       userId,
                                                       dataClassGUID,
                                                       OpenMetadataType.DATA_CLASS.typeName,
                                                       OpenMetadataType.DATA_CLASS_HIERARCHY_RELATIONSHIP.typeGUID,
                                                       OpenMetadataType.DATA_CLASS_HIERARCHY_RELATIONSHIP.typeName,
                                                       2,
                                                       null,
                                                       null,
                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                       null,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       0,
                                                       0,
                                                       effectiveTime,
                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                final String elementGUIDParameterName = "dataClass.getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataType.DATA_CLASS.typeGUID,
                                            OpenMetadataType.DATA_CLASS.typeName,
                                            true,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }
    }


    /**
     * Remove any files and folders connected to the folder by the NestedFile or FolderHierarchy relationships.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param glossaryGUID unique identifier for glossary
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeFolderContents(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  glossaryGUID,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "removeFolderContents";

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       glossaryGUID,
                                                                                       OpenMetadataType.FILE_FOLDER.typeName,
                                                                                       OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName,
                                                                                       2,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       0,
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                final String elementGUIDParameterName = "file.getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataType.DATA_FILE.typeGUID,
                                            OpenMetadataType.DATA_FILE.typeName,
                                            true,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }

        iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                       invalidParameterHandler,
                                                       userId,
                                                       glossaryGUID,
                                                       OpenMetadataType.FILE_FOLDER.typeName,
                                                       OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeGUID,
                                                       OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName,
                                                       2,
                                                       null,
                                                       null,
                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                       null,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       0,
                                                       0,
                                                       effectiveTime,
                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                final String elementGUIDParameterName = "folder.getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataType.FILE_FOLDER.typeGUID,
                                            OpenMetadataType.FILE_FOLDER.typeName,
                                            true,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }
    }


    /**
     * Remove any members connected to the collection by the CollectionMembership  relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param collectionGUID unique identifier for glossary
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeCollectionMembers(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  collectionGUID,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName = "removeCollectionMembers";

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       collectionGUID,
                                                                                       OpenMetadataType.COLLECTION.typeName,
                                                                                       OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                       2,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       0,
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                final String elementGUIDParameterName = "member.getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataType.REFERENCEABLE.typeGUID,
                                            OpenMetadataType.REFERENCEABLE.typeName,
                                            true,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }

    }


    /**
     * Remove any members connected to the collection by the CollectionMembership  relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param dataStructureGUID unique identifier for data structure
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeDataStructureMembers(String  userId,
                                           String  externalSourceGUID,
                                           String  externalSourceName,
                                           String  dataStructureGUID,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "removeDataStructureMembers";

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       dataStructureGUID,
                                                                                       OpenMetadataType.DATA_STRUCTURE.typeName,
                                                                                       OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                                                                       2,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       0,
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                final String elementGUIDParameterName = "member.getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataType.DATA_FIELD.typeGUID,
                                            OpenMetadataType.DATA_FIELD.typeName,
                                            true,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }

    }


    /**
     * Remove any data fields connected to the data field by the NestedDataFile  relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param dataFieldGUID unique identifier for data field
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeNestedDataFields(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  dataFieldGUID,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "removeNestedDataFields";

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       dataFieldGUID,
                                                                                       OpenMetadataType.DATA_FIELD.typeName,
                                                                                       OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                                                                       2,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       0,
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                final String elementGUIDParameterName = "nestedDataField.getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataType.DATA_FIELD.typeGUID,
                                            OpenMetadataType.DATA_FIELD.typeName,
                                            true,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }
    }


    /**
     * Remove any comments connected to the comment by the AttachedComment relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param commentGUID unique identifier for comment
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeNestedComments(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  commentGUID,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "removeNestedComments";

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       commentGUID,
                                                                                       OpenMetadataType.COMMENT.typeName,
                                                                                       OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName,
                                                                                       2,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       0,
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                final String elementGUIDParameterName = "comment.getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataType.COMMENT.typeGUID,
                                            OpenMetadataType.COMMENT.typeName,
                                            true,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }
    }


    /**
     * Remove any assets connected to the infrastructure by the DeployedOn relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param itInfrastructureGUID unique identifier for infrastructure
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeDeployedAssets(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  itInfrastructureGUID,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "removeDeployedAssets";

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       itInfrastructureGUID,
                                                                                       OpenMetadataType.IT_INFRASTRUCTURE.typeName,
                                                                                       OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName,
                                                                                       1,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       0,
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                final String elementGUIDParameterName = "asset.getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataType.ASSET.typeGUID,
                                            OpenMetadataType.ASSET.typeName,
                                            true,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }
    }


    /**
     * Check that a database schema has no tables defined.
     *
     * @param userId calling user
     * @param startingEntity     starting entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException delete not allowed
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException problem with access
     */
    private void validateEmptyDatabaseSchema(String       userId,
                                             EntityDetail startingEntity,
                                             boolean      forLineage,
                                             boolean      forDuplicateProcessing,
                                             Date         effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "validateEmptyDatabaseSchema";

        Relationship schemaTypeRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                            startingEntity.getGUID(),
                                                                                            startingEntity.getType().getTypeDefName(),
                                                                                            OpenMetadataType.SCHEMA_RELATIONSHIP.typeGUID,
                                                                                            OpenMetadataType.SCHEMA_RELATIONSHIP.typeName,
                                                                                            2,
                                                                                            null,
                                                                                            null,
                                                                                            SequencingOrder.CREATION_DATE_RECENT,
                                                                                            null,
                                                                                            forLineage,
                                                                                            forDuplicateProcessing,
                                                                                            effectiveTime,
                                                                                            methodName);

        if (schemaTypeRelationship != null)
        {
            List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                        schemaTypeRelationship.getEntityTwoProxy().getGUID(),
                                                                                        schemaTypeRelationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                        OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeGUID,
                                                                                        OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                                                                        2,
                                                                                        null,
                                                                                        null,
                                                                                        SequencingOrder.CREATION_DATE_RECENT,
                                                                                        null,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        0, 0,
                                                                                        effectiveTime,
                                                                                        methodName);
            if (relationships != null)
            {
                for (Relationship dataContentRelationship : relationships)
                {
                    if (dataContentRelationship != null)
                    {
                        throw new InvalidParameterException(GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                                   startingEntity.getGUID(),
                                                                                                                                   dataContentRelationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                                                   dataContentRelationship.getEntityTwoProxy().getGUID()),
                                                            this.getClass().getName(),
                                                            "relationship: " + dataContentRelationship.getGUID(),
                                                            methodName);
                    }
                }
            }
        }
    }


    /**
     * Check that an asset is not supplying data for a data set
     *
     * @param userId calling user
     * @param startingEntity     starting entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException delete not allowed
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException problem with access
     */
    private void validateNoDataSetContent(String       userId,
                                          EntityDetail startingEntity,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "validateNoDataSetContent";

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    startingEntity.getGUID(),
                                                                                    startingEntity.getType().getTypeDefName(),
                                                                                    OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                                    1,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);
        if (relationships != null)
        {
            for (Relationship dataContentRelationship : relationships)
            {
                if (dataContentRelationship != null)
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.DATA_STORE_IN_USE.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                        startingEntity.getGUID(),
                                                                                                                        dataContentRelationship.getEntityOneProxy().getType().getTypeDefName(),
                                                                                                                        dataContentRelationship.getEntityOneProxy().getGUID()),
                                                        this.getClass().getName(),
                                                        "relationship: " + dataContentRelationship.getGUID(),
                                                        methodName);
                }
            }
        }
    }


    /**
     * Check that a comment is has no nested comments.
     *
     * @param userId calling user
     * @param startingEntity     starting entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException delete not allowed
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException problem with access
     */
    private void validateNoNestedComments(String       userId,
                                          EntityDetail startingEntity,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "validateNoNestedComments";

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    startingEntity.getGUID(),
                                                                                    startingEntity.getType().getTypeDefName(),
                                                                                    OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName,
                                                                                    2,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                               startingEntity.getGUID(),
                                                                                                                               relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                                               relationship.getEntityTwoProxy().getGUID()),
                                                        this.getClass().getName(),
                                                        "relationship: " + relationship.getGUID(),
                                                        methodName);
                }
            }
        }
    }


    /**
     * Check that an infrastructure asset has no other assets deployed on it.
     *
     * @param userId calling user
     * @param startingEntity     starting entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException delete not allowed
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException problem with access
     */
    private void validateNoDeployedAssets(String       userId,
                                          EntityDetail startingEntity,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "validateNoDeployedAssets";

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    startingEntity.getGUID(),
                                                                                    startingEntity.getType().getTypeDefName(),
                                                                                    OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName,
                                                                                    1,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                               startingEntity.getGUID(),
                                                                                                                               relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                                               relationship.getEntityTwoProxy().getGUID()),
                                                        this.getClass().getName(),
                                                        "relationship: " + relationship.getGUID(),
                                                        methodName);
                }
            }
        }
    }


    /**
     * Check that a collection is has no members.
     *
     * @param userId calling user
     * @param startingEntity     starting entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException delete not allowed
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException problem with access
     */
    private void validateNoCollectionMembers(String       userId,
                                             EntityDetail startingEntity,
                                             boolean      forLineage,
                                             boolean      forDuplicateProcessing,
                                             Date         effectiveTime) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "validateNoCollectionMembers";

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    startingEntity.getGUID(),
                                                                                    startingEntity.getType().getTypeDefName(),
                                                                                    OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                    2,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                               startingEntity.getGUID(),
                                                                                                                               relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                                               relationship.getEntityTwoProxy().getGUID()),
                                                        this.getClass().getName(),
                                                        "relationship: " + relationship.getGUID(),
                                                        methodName);
                }
            }
        }
    }


    /**
     * Check that a data structure is has no nested data fields.
     *
     * @param userId calling user
     * @param startingEntity     starting entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException delete not allowed
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException problem with access
     */
    private void validateNoMemberDataFields(String       userId,
                                            EntityDetail startingEntity,
                                            boolean      forLineage,
                                            boolean      forDuplicateProcessing,
                                            Date         effectiveTime) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "validateNoMemberDataFields";

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    startingEntity.getGUID(),
                                                                                    startingEntity.getType().getTypeDefName(),
                                                                                    OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                                                                    2,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                               startingEntity.getGUID(),
                                                                                                                               relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                                               relationship.getEntityTwoProxy().getGUID()),
                                                        this.getClass().getName(),
                                                        "relationship: " + relationship.getGUID(),
                                                        methodName);
                }
            }
        }
    }



    /**
     * Check that a data class has no nested or specialized data classes linked off of it
     *
     * @param userId calling user
     * @param startingEntity     starting entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException delete not allowed
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException problem with access
     */
    private void validateNoDependentDataClasses(String       userId,
                                                EntityDetail startingEntity,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                Date         effectiveTime) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "validateNoDependentDataClasses";

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    startingEntity.getGUID(),
                                                                                    startingEntity.getType().getTypeDefName(),
                                                                                    OpenMetadataType.DATA_CLASS_HIERARCHY_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.DATA_CLASS_HIERARCHY_RELATIONSHIP.typeName,
                                                                                    2,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                               startingEntity.getGUID(),
                                                                                                                               relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                                               relationship.getEntityTwoProxy().getGUID()),
                                                        this.getClass().getName(),
                                                        "relationship: " + relationship.getGUID(),
                                                        methodName);
                }
            }
        }

        relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                 startingEntity.getGUID(),
                                                                 startingEntity.getType().getTypeDefName(),
                                                                 OpenMetadataType.DATA_CLASS_COMPOSITION_RELATIONSHIP.typeGUID,
                                                                 OpenMetadataType.DATA_CLASS_COMPOSITION_RELATIONSHIP.typeName,
                                                                 2,
                                                                 null,
                                                                 null,
                                                                 SequencingOrder.CREATION_DATE_RECENT,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 0, 0,
                                                                 effectiveTime,
                                                                 methodName);
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                               startingEntity.getGUID(),
                                                                                                                               relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                                               relationship.getEntityTwoProxy().getGUID()),
                                                        this.getClass().getName(),
                                                        "relationship: " + relationship.getGUID(),
                                                        methodName);
                }
            }
        }
    }


    /**
     * Check that a data field is has no nested data fields.
     *
     * @param userId calling user
     * @param startingEntity     starting entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException delete not allowed
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException problem with access
     */
    private void validateNoNestedDataFields(String       userId,
                                            EntityDetail startingEntity,
                                            boolean      forLineage,
                                            boolean      forDuplicateProcessing,
                                            Date         effectiveTime) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "validateNoNestedDataFields";

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    startingEntity.getGUID(),
                                                                                    startingEntity.getType().getTypeDefName(),
                                                                                    OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                                                                    2,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                               startingEntity.getGUID(),
                                                                                                                               relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                                               relationship.getEntityTwoProxy().getGUID()),
                                                        this.getClass().getName(),
                                                        "relationship: " + relationship.getGUID(),
                                                        methodName);
                }
            }
        }
    }


    /**
     * Check that a folder is empty
     *
     * @param userId calling user
     * @param startingEntity     starting entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @throws InvalidParameterException delete not allowed
     * @throws PropertyServerException problem with repository
     * @throws UserNotAuthorizedException problem with access
     */
    private void validateEmptyFolder(String       userId,
                                     EntityDetail startingEntity,
                                     boolean      forLineage,
                                     boolean      forDuplicateProcessing,
                                     Date         effectiveTime) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "validateEmptyFolder";

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    startingEntity.getGUID(),
                                                                                    startingEntity.getType().getTypeDefName(),
                                                                                    OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName,
                                                                                    2,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    0, 0,
                                                                                    effectiveTime,
                                                                                    methodName);
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                               startingEntity.getGUID(),
                                                                                                                               relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                                               relationship.getEntityTwoProxy().getGUID()),
                                                        this.getClass().getName(),
                                                        "relationship: " + relationship.getGUID(),
                                                        methodName);
                }
            }
        }

        relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                 startingEntity.getGUID(),
                                                                 startingEntity.getType().getTypeDefName(),
                                                                 OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeGUID,
                                                                 OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName,
                                                                 2,
                                                                 null,
                                                                 null,
                                                                 SequencingOrder.CREATION_DATE_RECENT,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 0, 0,
                                                                 effectiveTime,
                                                                 methodName);
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    throw new InvalidParameterException(GenericHandlersErrorCode.DEPENDENT_ELEMENTS_FOUND.getMessageDefinition(startingEntity.getType().getTypeDefName(),
                                                                                                                               startingEntity.getGUID(),
                                                                                                                               relationship.getEntityTwoProxy().getType().getTypeDefName(),
                                                                                                                               relationship.getEntityTwoProxy().getGUID()),
                                                        this.getClass().getName(),
                                                        "relationship: " + relationship.getGUID(),
                                                        methodName);
                }
            }
        }
    }


    /**
     * Update the effectivity dates of a specific relationship.
     * The effectivity dates control the visibility of the relationship through specific APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param relationshipGUID unique identifier of the entity in the repositories
     * @param relationshipGUIDParameterName parameter name that passed the relationshipGUID
     * @param relationshipGUIDTypeName type of relationship
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelationshipEffectivityDates(String       userId,
                                                   String       externalSourceGUID,
                                                   String       externalSourceName,
                                                   String       relationshipGUID,
                                                   String       relationshipGUIDParameterName,
                                                   String       relationshipGUIDTypeName,
                                                   Date         effectiveFrom,
                                                   Date         effectiveTo,
                                                   boolean      forLineage,
                                                   boolean      forDuplicateProcessing,
                                                   Date         effectiveTime,
                                                   String       methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String relationshipTypeParameterName = "relationshipGUIDTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipGUIDTypeName, relationshipTypeParameterName, methodName);

        Relationship relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                            relationshipGUID,
                                                                            relationshipGUIDParameterName,
                                                                            relationshipGUIDTypeName,
                                                                            effectiveTime,
                                                                            methodName);

        if (relationship != null)
        {
            InstanceProperties newProperties = this.setUpEffectiveDates(relationship.getProperties(), effectiveFrom, effectiveTo);

            this.validateRelationshipChange(userId,
                                            relationship,
                                            false,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

            /*
             * User has permission to proceed
             */
            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationshipGUID,
                                                           newProperties,
                                                           methodName);

        }
    }


    /**
     * Validate that a relationship can be returned to the caller.
     *
     * @param userId calling user
     * @param relationship relationship to test
     * @param validatedAnchorGUIDs the anchor GUIDs that have been validated (don't want to do this many times).
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store

     */
    protected void validateRelationship(String       userId,
                                        Relationship relationship,
                                        List<String> validatedAnchorGUIDs,
                                        boolean      forLineage,
                                        boolean      forDuplicateProcessing,
                                        Date         effectiveTime,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String entityOneParameterName = "relationship.getEntityOneProxy().getGUID()";
        final String entityTwoParameterName = "relationship.getEntityTwoProxy().getGUID()";

        validateEntityProxyAnchor(userId,
                                  relationship.getEntityOneProxy(),
                                  entityOneParameterName,
                                  validatedAnchorGUIDs,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
                                  methodName);

        validateEntityProxyAnchor(userId,
                                  relationship.getEntityTwoProxy(),
                                  entityTwoParameterName,
                                  validatedAnchorGUIDs,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Check that a relationship returned from the repository is linked to entities that
     * are visible to the caller.
     *
     * @param userId caller
     * @param entityProxy linked entity
     * @param entityProxyParameterName name of the parameter for this end
     * @param validatedAnchorGUIDs the anchor GUIDs that have been validated (don't want to do this many times).
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    private void validateEntityProxyAnchor(String       userId,
                                           EntityProxy  entityProxy,
                                           String       entityProxyParameterName,
                                           List<String> validatedAnchorGUIDs,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        AnchorIdentifiers anchorIdentifiers = this.getAnchorsFromAnchorsClassification(entityProxy, methodName);

        if (anchorIdentifiers == null)
        {
            EntityDetail connectToEntity = repositoryHandler.getEntityByGUID(userId,
                                                                             entityProxy.getGUID(),
                                                                             entityProxyParameterName,
                                                                             OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName);

            anchorIdentifiers = this.getAnchorsFromAnchorsClassification(entityProxy, methodName);

            if ((anchorIdentifiers == null) || (anchorIdentifiers.anchorGUID == null) || (! validatedAnchorGUIDs.contains(anchorIdentifiers.anchorGUID)))
            {
                this.validateEntityAndAnchorForRead(userId,
                                                    connectToEntity.getType().getTypeDefName(),
                                                    connectToEntity,
                                                    entityProxyParameterName,
                                                    false,
                                                    false,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);

                if ((anchorIdentifiers != null) && (anchorIdentifiers.anchorGUID != null))
                {
                    validatedAnchorGUIDs.add(anchorIdentifiers.anchorGUID);
                }
            }
        }
        else
        {
            if ((anchorIdentifiers.anchorGUID == null) || (! validatedAnchorGUIDs.contains(anchorIdentifiers.anchorGUID)))
            {
                this.validateEntityAndAnchorForRead(userId,
                                                    entityProxy.getGUID(),
                                                    entityProxyParameterName,
                                                    OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                    false,
                                                    false,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);

                if (anchorIdentifiers.anchorGUID != null)
                {
                    validatedAnchorGUIDs.add(anchorIdentifiers.anchorGUID);
                }
            }
        }
    }


    /**
     * Thrown an exception if the user does not have permission to proceed with the update to a relationship.
     *
     * @param userId calling user
     * @param relationship retrieved relationship
     * @param isDelete is the action to delete
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private void validateRelationshipChange(String       userId,
                                            Relationship relationship,
                                            boolean      isDelete,
                                            boolean      forLineage,
                                            boolean      forDuplicateProcessing,
                                            Date         effectiveTime,
                                            String       methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String entityOneParameterName = "relationship.getEntityOneProxy().getGUID()";
        final String entityTwoParameterName = "relationship.getEntityTwoProxy().getGUID()";

        String relationshipGUIDTypeName = relationship.getType().getTypeDefName();

        /*
         * Retrieve the end entities using the repository handler as calls to validate the anchor
         * via the security verifier are done here.
         */
        EntityDetail bean1Entity = repositoryHandler.getEntityByGUID(userId,
                                                                     relationship.getEntityOneProxy().getGUID(),
                                                                     entityOneParameterName,
                                                                     OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

        EntityDetail anchor1Entity = this.validateEntityAndAnchorForRead(userId,
                                                                         OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                         bean1Entity,
                                                                         entityOneParameterName,
                                                                         true,
                                                                         false,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        EntityDetail bean2Entity = repositoryHandler.getEntityByGUID(userId,
                                                                     relationship.getEntityTwoProxy().getGUID(),
                                                                     entityTwoParameterName,
                                                                     OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

        EntityDetail anchor2Entity = this.validateEntityAndAnchorForRead(userId,
                                                                         OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                         bean2Entity,
                                                                         entityTwoParameterName,
                                                                         true,
                                                                         false,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        if ((repositoryHelper.isTypeOf(serviceName, relationshipGUIDTypeName, OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, relationshipGUIDTypeName, OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, relationshipGUIDTypeName, OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, relationshipGUIDTypeName, OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName)))
        {
            if (anchor1Entity != null)
            {
                if (isDelete)
                {
                    securityVerifier.validateUserForAnchorDeleteFeedback(userId,
                                                                         anchor1Entity,
                                                                         bean2Entity,
                                                                         repositoryHelper,
                                                                         serviceName,
                                                                         methodName);
                }
                else
                {
                    securityVerifier.validateUserForAnchorAddFeedback(userId,
                                                                      anchor1Entity,
                                                                      bean2Entity,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      methodName);
                }
            }
            else
            {
                if (isDelete)
                {
                    securityVerifier.validateUserForElementDeleteFeedback(userId,
                                                                          bean1Entity,
                                                                          bean2Entity,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          methodName);
                }
                else
                {
                    securityVerifier.validateUserForElementAddFeedback(userId,
                                                                       bean1Entity,
                                                                       bean2Entity,
                                                                       repositoryHelper,
                                                                       serviceName,
                                                                       methodName);
                }
            }
        }
        else if (anchor1Entity != null)
        {
            if ((anchor2Entity != null) && (anchor1Entity.getGUID().equals(anchor2Entity.getGUID())))
            {
                /*
                 * Both ends of the relationship have the same anchor, so they are members of the anchor
                 */
                securityVerifier.validateUserForAnchorMemberUpdate(userId,
                                                                   anchor1Entity,
                                                                   repositoryHelper,
                                                                   serviceName,
                                                                   methodName);
            }
            else
            {
                if (isDelete)
                {
                    securityVerifier.validateUserForAnchorDetach(userId,
                                                                 anchor1Entity,
                                                                 bean2Entity,
                                                                 relationshipGUIDTypeName,
                                                                 repositoryHelper,
                                                                 serviceName,
                                                                 methodName);
                }
                else
                {
                    securityVerifier.validateUserForAnchorAttach(userId,
                                                                 anchor1Entity,
                                                                 bean2Entity,
                                                                 relationshipGUIDTypeName,
                                                                 repositoryHelper,
                                                                 serviceName,
                                                                 methodName);
                }
            }
        }
        else if (anchor2Entity == null)
        {
            if (isDelete)
            {
                securityVerifier.validateUserForElementDetach(userId,
                                                              bean1Entity,
                                                              bean2Entity,
                                                              relationshipGUIDTypeName,
                                                              repositoryHelper,
                                                              serviceName,
                                                              methodName);
            }
            else
            {
                securityVerifier.validateUserForElementAttach(userId,
                                                              bean1Entity,
                                                              bean2Entity,
                                                              relationshipGUIDTypeName,
                                                              repositoryHelper,
                                                              serviceName,
                                                              methodName);
            }
        }
        else // anchor1 is null but anchor2 is not
        {
            if (isDelete)
            {
                securityVerifier.validateUserForAnchorDetach(userId,
                                                             anchor2Entity,
                                                             bean1Entity,
                                                             relationshipGUIDTypeName,
                                                             repositoryHelper,
                                                             serviceName,
                                                             methodName);
            }
            else
            {
                securityVerifier.validateUserForAnchorAttach(userId,
                                                             anchor2Entity,
                                                             bean1Entity,
                                                             relationshipGUIDTypeName,
                                                             repositoryHelper,
                                                             serviceName,
                                                             methodName);
            }
        }

        /*
         * User has permission to proceed
         */
    }


    /**
     * Update the properties associated with a relationship.  Effectivity dates are unchanged.
     *
     * @param userId caller's userId
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param relationshipGUID unique identifier of the relationship to update
     * @param relationshipGUIDParameterName  name of the parameter supplying the relationshipGUID
     * @param relationshipTypeName type name of relationship if known (null is ok)
     * @param isMergeUpdate should the supplied updateProperties be merged with existing properties (true) by replacing just the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param relationshipProperties new properties for the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the user is not allowed to perform this action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelationshipProperties(String             userId,
                                             String             externalSourceGUID,
                                             String             externalSourceName,
                                             String             relationshipGUID,
                                             String             relationshipGUIDParameterName,
                                             String             relationshipTypeName,
                                             boolean            isMergeUpdate,
                                             InstanceProperties relationshipProperties,
                                             boolean            forLineage,
                                             boolean            forDuplicateProcessing,
                                             Date               effectiveTime,
                                             String             methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);

        Relationship relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                            relationshipGUID,
                                                                            relationshipGUIDParameterName,
                                                                            relationshipTypeName,
                                                                            effectiveTime,
                                                                            methodName);

        if (relationship != null)
        {
            /*
             * Sort out the properties
             */
            InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                  relationshipProperties,
                                                                  relationship.getProperties());

            this.validateRelationshipChange(userId,
                                            relationship,
                                            false,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

            /*
             * If there are no properties to change then nothing more to do
             */
            if ((newProperties == null) && (relationship.getProperties() == null))
            {
                auditLog.logMessage(methodName,
                                    GenericHandlersAuditCode.IGNORING_UNNECESSARY_RELATIONSHIP_UPDATE.getMessageDefinition(relationship.getType().getTypeDefName(),
                                                                                                                           relationship.getGUID(),
                                                                                                                           methodName,
                                                                                                                           userId));
                return;
            }

            /*
             * If nothing has changed in the properties then nothing to do
             */
            if ((newProperties != null) && (newProperties.equals(relationship.getProperties())))
            {
                auditLog.logMessage(methodName,
                                    GenericHandlersAuditCode.IGNORING_UNNECESSARY_RELATIONSHIP_UPDATE.getMessageDefinition(relationship.getType().getTypeDefName(),
                                                                                                                           relationship.getGUID(),
                                                                                                                           methodName,
                                                                                                                           userId));
                return;
            }

            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationship,
                                                           newProperties,
                                                           methodName);
        }
    }


    /**
     * Return a visible relationship retrieved by its GUID.
     *
     * @param userId calling user
     * @param relationshipGUID unique identifier
     * @param relationshipGUIDParameterName parameter passing the unique identifier
     * @param relationshipTypeName type of relationship to be retrieved
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime time when the relationship is effective
     * @param methodName calling method
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public Relationship getAttachmentLink(String       userId,
                                          String       relationshipGUID,
                                          String       relationshipGUIDParameterName,
                                          String       relationshipTypeName,
                                          Date         asOfTime,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException

    {
        Relationship relationship;

        if (asOfTime == null)
        {
            relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                   relationshipGUID,
                                                                   relationshipGUIDParameterName,
                                                                   relationshipTypeName,
                                                                   effectiveTime,
                                                                   methodName);
        }
        else
        {
            relationship = repositoryHandler.getRelationshipByGUID(userId,
                                                                   relationshipGUID,
                                                                   relationshipGUIDParameterName,
                                                                   relationshipTypeName,
                                                                   asOfTime,
                                                                   effectiveTime,
                                                                   methodName);
        }

        final String entityOneParameterName = "relationship.getEntityOneProxy().getGUID()";
        final String entityTwoParameterName = "relationship.getEntityTwoProxy().getGUID()";

        /*
         * Check that both ends are readable
         */
        this.validateEntityAndAnchorForRead(userId,
                                            relationship.getEntityOneProxy().getGUID(),
                                            entityOneParameterName,
                                            OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                            true,
                                            false,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

        this.validateEntityAndAnchorForRead(userId,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            entityTwoParameterName,
                                            OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                            true,
                                            false,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

        return relationship;
    }


    /**
     * Return the relationship between the requested elements - there should be only one.  Note that the entities are not checked.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityGUID unique identifier of the entity on the other end or null if unknown
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public Relationship  getUniqueAttachmentLink(String               userId,
                                                 String               startingGUID,
                                                 String               startingGUIDParameterName,
                                                 String               startingTypeName,
                                                 String               attachmentRelationshipTypeGUID,
                                                 String               attachmentRelationshipTypeName,
                                                 String               attachmentEntityGUID,
                                                 String               attachmentEntityTypeName,
                                                 int                  attachmentEntityEnd,
                                                 List<InstanceStatus> limitResultsByStatus,
                                                 Date                 asOfTime,
                                                 SequencingOrder      sequencingOrder,
                                                 String               sequencingProperty,
                                                 boolean              forLineage,
                                                 boolean              forDuplicateProcessing,
                                                 Date                 effectiveTime,
                                                 String               methodName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   startingGUID,
                                                                   startingGUIDParameterName,
                                                                   startingTypeName,
                                                                   attachmentRelationshipTypeGUID,
                                                                   attachmentRelationshipTypeName,
                                                                   attachmentEntityGUID,
                                                                   attachmentEntityTypeName,
                                                                   attachmentEntityEnd,
                                                                   limitResultsByStatus,
                                                                   asOfTime,
                                                                   sequencingOrder,
                                                                   sequencingProperty,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   0,
                                                                   invalidParameterHandler.getMaxPagingSize(),
                                                                   effectiveTime,
                                                                   methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }
        else if (relationships.size() == 1)
        {
            return relationships.get(0);
        }
        else
        {
            errorHandler.handleAmbiguousRelationships(startingGUID,
                                                      startingTypeName,
                                                      attachmentRelationshipTypeName,
                                                      relationships,
                                                      methodName);

            return null;
        }
    }


    /**
     * Return all relationships attached to a specific entity.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<Relationship>  getAllAttachmentLinks(String               userId,
                                                     String               startingGUID,
                                                     String               startingGUIDParameterName,
                                                     String               startingTypeName,
                                                     List<InstanceStatus> limitResultsByStatus,
                                                     Date                 asOfTime,
                                                     SequencingOrder      sequencingOrder,
                                                     String               sequencingProperty,
                                                     boolean              forLineage,
                                                     boolean              forDuplicateProcessing,
                                                     Date                 effectiveTime,
                                                     String               methodName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        int startFrom = 0;

        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   startingGUID,
                                                                   startingGUIDParameterName,
                                                                   startingTypeName,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   0,
                                                                   limitResultsByStatus,
                                                                   asOfTime,
                                                                   sequencingOrder,
                                                                   sequencingProperty,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   startFrom,
                                                                   invalidParameterHandler.getMaxPagingSize(),
                                                                   effectiveTime,
                                                                   methodName);

        if (relationships != null)
        {
            List<Relationship> fullRelationshipList = new ArrayList<>();

            while (relationships != null)
            {
                /*
                 * Save the retrieved relationships.
                 */
                fullRelationshipList.addAll(relationships);

                /*
                 * Get the next set of relationships.
                 */
                startFrom = startFrom + invalidParameterHandler.getMaxPagingSize();

                relationships = this.getAttachmentLinks(userId,
                                                        startingGUID,
                                                        startingGUIDParameterName,
                                                        startingTypeName,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        0,
                                                        limitResultsByStatus,
                                                        asOfTime,
                                                        sequencingOrder,
                                                        sequencingProperty,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        startFrom,
                                                        invalidParameterHandler.getMaxPagingSize(),
                                                        effectiveTime,
                                                        methodName);
            }

            return fullRelationshipList;
        }

        return null;
    }


    /**
     * Return the relationships to required elements attached to a specific entity.  Note that the entities are not checked.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityGUID unique identifier of the entity on the other end or null if unknown
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<Relationship>  getAttachmentLinks(String               userId,
                                                  String               startingGUID,
                                                  String               startingGUIDParameterName,
                                                  String               startingTypeName,
                                                  String               attachmentRelationshipTypeGUID,
                                                  String               attachmentRelationshipTypeName,
                                                  String               attachmentEntityGUID,
                                                  String               attachmentEntityTypeName,
                                                  int                  attachmentEntityEnd,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  Date                 asOfTime,
                                                  SequencingOrder      sequencingOrder,
                                                  String               sequencingProperty,
                                                  boolean              forLineage,
                                                  boolean              forDuplicateProcessing,
                                                  int                  startingFrom,
                                                  int                  pageSize,
                                                  Date                 effectiveTime,
                                                  String               methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail startingEntity = this.getEntityFromRepository(userId,
                                                                   startingGUID,
                                                                   startingGUIDParameterName,
                                                                   startingTypeName,
                                                                   null,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);
        
        return getAttachmentLinks(userId,
                                  startingEntity,
                                  startingGUIDParameterName,
                                  startingTypeName,
                                  attachmentRelationshipTypeGUID,
                                  attachmentRelationshipTypeName,
                                  attachmentEntityGUID,
                                  attachmentEntityTypeName,
                                  attachmentEntityEnd,
                                  limitResultsByStatus,
                                  asOfTime,
                                  sequencingOrder,
                                  sequencingProperty,
                                  forLineage,
                                  forDuplicateProcessing,
                                  startingFrom,
                                  pageSize,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Return the relationships to required elements attached to a specific entity.  Note that the entities are not checked.
     *
     * @param userId     calling user
     * @param startingEntity  the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityGUID unique identifier of the entity on the other end or null if unknown
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage is this a lineage request
     * @param forDuplicateProcessing is this processing part of duplicate processing?
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<Relationship>  getAttachmentLinks(String               userId,
                                                  EntityDetail         startingEntity,
                                                  String               startingGUIDParameterName,
                                                  String               startingTypeName,
                                                  String               attachmentRelationshipTypeGUID,
                                                  String               attachmentRelationshipTypeName,
                                                  String               attachmentEntityGUID,
                                                  String               attachmentEntityTypeName,
                                                  int                  attachmentEntityEnd,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  Date                 asOfTime,
                                                  SequencingOrder      sequencingOrder,
                                                  String               sequencingProperty,
                                                  boolean              forLineage,
                                                  boolean              forDuplicateProcessing,
                                                  int                  startingFrom,
                                                  int                  pageSize,
                                                  Date                 effectiveTime,
                                                  String               methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(startingEntity, startingGUIDParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        List<Relationship> retrievedRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                             startingEntity,
                                                                                             startingTypeName,
                                                                                             attachmentRelationshipTypeGUID,
                                                                                             attachmentRelationshipTypeName,
                                                                                             attachmentEntityEnd,
                                                                                             limitResultsByStatus,
                                                                                             asOfTime,
                                                                                             sequencingOrder,
                                                                                             sequencingProperty,
                                                                                             forLineage,
                                                                                             forDuplicateProcessing,
                                                                                             startingFrom,
                                                                                             queryPageSize,
                                                                                             effectiveTime,
                                                                                             methodName);

        if (retrievedRelationships == null)
        {
            return null;
        }

        /*
         * Retrieve all of the entities linked to the relationships.  This is done as a single retrieve
         * to minimise the calls to the repositories.  It also performs security checks
         */
        Map<String, EntityDetail> retrievedEntities = this.getValidatedEntities(userId,
                                                                                startingEntity.getGUID(),
                                                                                startingEntity.getType().getTypeDefName(),
                                                                                retrievedRelationships,
                                                                                attachmentEntityTypeName,
                                                                                null,
                                                                                null,
                                                                                0,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName);

        List<Relationship> results = new ArrayList<>();

        if (retrievedEntities != null)
        {
            retrievedEntities.put(startingEntity.getGUID(), startingEntity);

            /*
             * Only return relationships that link to approved entities.
             */
            for (Relationship relationship : retrievedRelationships)
            {
                if (relationship != null)
                {
                    EntityProxy otherEnd = repositoryHandler.getOtherEnd(startingEntity.getGUID(), startingTypeName, relationship, attachmentEntityEnd, methodName);

                    /*
                     * Does the relationship point to an appropriate type of entity?
                     */
                    if (repositoryHelper.isTypeOf(serviceName, otherEnd.getType().getTypeDefName(), attachmentEntityTypeName))
                    {
                        if ((attachmentEntityGUID == null) || (attachmentEntityGUID.equals(otherEnd.getGUID())))
                        {
                            /*
                             * Only return the relationship if the entity is visible to the caller.
                             */
                            EntityDetail retrievedEntity = retrievedEntities.get(otherEnd.getGUID());

                            if (retrievedEntity != null)
                            {
                                log.debug("Accepting relationship: " + relationship.getGUID());
                                results.add(relationship);
                            }
                            else
                            {
                                log.debug("Ignoring relationship as other end is not authorized: " + relationship.getGUID());
                            }
                        }
                        else
                        {
                            log.debug("Ignoring relationship based on entity instance: " + relationship.getGUID());
                        }
                    }
                    else
                    {
                        log.debug("Ignoring relationship based on type of attachment: " + relationship.getGUID());
                    }
                }
            }
        }

        return results;
    }


    /**
     * Retrieve the list of versions for the relationship.
     *
     * @param userId calling user
     * @param guid unique identifier of object to update
     * @param guidParameterName name of parameter supplying the GUID
     * @param fromTime starting time
     * @param toTime ending time
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param sequencingOrder order of the results
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
    public List<Relationship> getRelationshipHistory(String                 userId,
                                                     String                 guid,
                                                     String                 guidParameterName,
                                                     Date                   fromTime,
                                                     Date                   toTime,
                                                     int                    startingFrom,
                                                     int                    pageSize,
                                                     HistorySequencingOrder sequencingOrder,
                                                     boolean                forLineage,
                                                     boolean                forDuplicateProcessing,
                                                     Date                   effectiveTime,
                                                     String                 methodName) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        List<Relationship> relationships = repositoryHandler.getRelationshipHistory(userId,
                                                                                    guid,
                                                                                    fromTime,
                                                                                    toTime,
                                                                                    startingFrom,
                                                                                    pageSize,
                                                                                    sequencingOrder,
                                                                                    methodName);

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    validateRelationship(userId,
                                         relationship,
                                         new ArrayList<>(),
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
                    /*
                     * Only need to validate one
                     */
                    break;
                }
            }
        }

        return relationships;
    }


    /**
     * Return a list of relationships that match the supplied criteria.  The results can be returned over many pages.
     * Note: the visibility of the relationship and both entities is checked before returning a relationship to the caller.
     *
     * @param userId caller's userId
     * @param relationshipTypeName type of interest (null means any element type)
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<Relationship> findAttachmentLinks(String                userId,
                                                  String                relationshipTypeName,
                                                  SearchProperties      searchProperties,
                                                  List<InstanceStatus>  limitResultsByStatus,
                                                  Date                  asOfTime,
                                                  String                sequencingProperty,
                                                  SequencingOrder       sequencingOrder,
                                                  boolean               forLineage,
                                                  boolean               forDuplicateProcessing,
                                                  int                   startingFrom,
                                                  int                   pageSize,
                                                  Date                  effectiveTime,
                                                  String                methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        String relationshipTypeGUID = null;

        if (relationshipTypeName != null)
        {
            relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                            null,
                                                                            serviceName,
                                                                            methodName,
                                                                            repositoryHelper);
        }

        List<Relationship> retrievedRelationships = repositoryHandler.findRelationships(userId,
                                                                                        relationshipTypeGUID,
                                                                                        null,
                                                                                        searchProperties,
                                                                                        limitResultsByStatus,
                                                                                        asOfTime,
                                                                                        sequencingProperty,
                                                                                        sequencingOrder,
                                                                                        forDuplicateProcessing,
                                                                                        startingFrom,
                                                                                        queryPageSize,
                                                                                        effectiveTime,
                                                                                        methodName);

        if (retrievedRelationships != null)
        {
            List<Relationship> results              = new ArrayList<>();
            List<String>       validatedAnchorGUIDs = new ArrayList<>();

            for (Relationship relationship : retrievedRelationships)
            {
                try
                {
                    validateRelationship(userId,
                                         relationship,
                                         validatedAnchorGUIDs,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);

                    results.add(relationship);
                }
                catch (Exception error)
                {
                    // ignore an element that is not visible to the caller
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Return the entity for the required relationship attached to a specific entity.  This method assumes the starting entity has
     * a validated anchor
     *
     * @param userId     calling user
     * @param startingElementGUID identifier for the entity that the identifier is attached to
     * @param startingElementGUIDParameterName name of the parameter used to pass the guid
     * @param startingElementTypeName type name for anchor
     * @param relationshipTypeGUID unique identifier of the attachment's relationship type
     * @param relationshipTypeName unique name of the attachment's relationship type
     * @param resultingElementTypeName unique name of the attached entity's type
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param forLineage is this part of a lineage request?
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public EntityDetail getAttachedEntity(String       userId,
                                          String       startingElementGUID,
                                          String       startingElementGUIDParameterName,
                                          String       startingElementTypeName,
                                          String       relationshipTypeGUID,
                                          String       relationshipTypeName,
                                          String       resultingElementTypeName,
                                          int          attachmentEntityEnd,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        EntityDetail startingEntity = this.getEntityFromRepository(userId,
                                                                   startingElementGUID,
                                                                   startingElementGUIDParameterName,
                                                                   startingElementTypeName,
                                                                   null,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);

        EntityDetail entity = repositoryHandler.getEntityForRelationshipType(userId,
                                                                             startingEntity,
                                                                             startingElementTypeName,
                                                                             relationshipTypeGUID,
                                                                             relationshipTypeName,
                                                                             null,
                                                                             0,
                                                                             resultingElementTypeName,
                                                                             attachmentEntityEnd,
                                                                             null,
                                                                             null,
                                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                                             null,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName);

        if (entity != null)
        {
            validateEntityAndAnchorForRead(userId,
                                           resultingElementTypeName,
                                           entity,
                                           startingElementGUIDParameterName,
                                           true,
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
        }

        return entity;
    }


    /**
     * Return the entities for the required relationships attached to a specific entity.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of the parameter used to pass the guid
     * @param startingTypeName type name for anchor
     * @param relationshipTypeGUID unique identifier of the attachment's relationship type
     * @param relationshipTypeName unique name of the attachment's relationship type
     * @param resultingElementTypeName unique name of the attached entity's type
     * @param requiredClassificationName name of a classification that must be on the entity for a match
     * @param omittedClassificationName name of a classification that must NOT be on the entity for a match
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage is this part of a lineage request?
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<EntityDetail> getAttachedEntities(String               userId,
                                                  String               startingGUID,
                                                  String               startingGUIDParameterName,
                                                  String               startingTypeName,
                                                  String               relationshipTypeGUID,
                                                  String               relationshipTypeName,
                                                  String               resultingElementTypeName,
                                                  String               requiredClassificationName,
                                                  String               omittedClassificationName,
                                                  int                  attachmentEntityEnd,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  Date                 asOfTime,
                                                  SequencingOrder      sequencingOrder,
                                                  String               sequencingProperty,
                                                  boolean              forLineage,
                                                  boolean              forDuplicateProcessing,
                                                  int                  startingFrom,
                                                  int                  pageSize,
                                                  Date                 effectiveTime,
                                                  String               methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail startingEntity = this.getEntityFromRepository(userId,
                                                                   startingGUID,
                                                                   startingGUIDParameterName,
                                                                   startingTypeName,
                                                                   null,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);

        return this.getAttachedEntities(userId,
                                        startingEntity,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        relationshipTypeGUID,
                                        relationshipTypeName,
                                        resultingElementTypeName,
                                        requiredClassificationName,
                                        omittedClassificationName,
                                        attachmentEntityEnd,
                                        limitResultsByStatus,
                                        asOfTime,
                                        sequencingOrder,
                                        sequencingProperty,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the entities for the required relationships attached to a specific entity.
     *
     * @param userId     calling user
     * @param startingElement  the entity that the required element is attached to
     * @param startingElementGUIDParameterName name of the parameter used to pass the guid
     * @param startingElementTypeName type name for anchor
     * @param relationshipTypeGUID unique identifier of the attachment's relationship type
     * @param relationshipTypeName unique name of the attachment's relationship type
     * @param resultingElementTypeName unique name of the attached entity's type
     * @param requiredClassificationName name of a classification that must be on the entity for a match
     * @param omittedClassificationName name of a classification that must NOT be on the entity for a match
     * @param attachmentEntityEnd which relationship end should the attached entity be located? 0=either end; 1=end1; 2=end2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage is this part of a lineage request?
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public List<EntityDetail> getAttachedEntities(String               userId,
                                                  EntityDetail         startingElement,
                                                  String               startingElementGUIDParameterName,
                                                  String               startingElementTypeName,
                                                  String               relationshipTypeGUID,
                                                  String               relationshipTypeName,
                                                  String               resultingElementTypeName,
                                                  String               requiredClassificationName,
                                                  String               omittedClassificationName,
                                                  int                  attachmentEntityEnd,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  Date                 asOfTime,
                                                  SequencingOrder      sequencingOrder,
                                                  String               sequencingProperty,
                                                  boolean              forLineage,
                                                  boolean              forDuplicateProcessing,
                                                  int                  startingFrom,
                                                  int                  pageSize,
                                                  Date                 effectiveTime,
                                                  String               methodName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(startingElement, startingElementGUIDParameterName, methodName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    startingElement,
                                                                                    startingElementTypeName,
                                                                                    relationshipTypeGUID,
                                                                                    relationshipTypeName,
                                                                                    attachmentEntityEnd,
                                                                                    limitResultsByStatus,
                                                                                    asOfTime,
                                                                                    sequencingOrder,
                                                                                    sequencingProperty,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    startingFrom,
                                                                                    pageSize,
                                                                                    effectiveTime,
                                                                                    methodName);

        if (relationships == null)
        {
            return null;
        }

        /*
         * Retrieve all of the entities linked to the relationships.  This is done as a single retrieve
         * to minimise the calls to the repositories.
         */
        Map<String, EntityDetail> retrievedEntities = this.getValidatedEntities(userId,
                                                                                startingElement.getGUID(),
                                                                                startingElementTypeName,
                                                                                relationships,
                                                                                resultingElementTypeName,
                                                                                requiredClassificationName,
                                                                                omittedClassificationName,
                                                                                attachmentEntityEnd,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName);

        List<EntityDetail> visibleEntities = new ArrayList<>();

        if (retrievedEntities != null)
        {
            retrievedEntities.put(startingElement.getGUID(), startingElement);

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy entityProxy = repositoryHandler.getOtherEnd(startingElement.getGUID(), startingElementTypeName, relationship, attachmentEntityEnd, methodName);

                    if ((entityProxy != null) && (entityProxy.getType() != null) &&
                            (repositoryHelper.isTypeOf(serviceName, entityProxy.getType().getTypeDefName(), resultingElementTypeName)))
                    {
                        try
                        {
                            final String guidParameterName = "entityProxy.getGUID()";

                            visibleEntities.add(this.getEntityFromRepository(userId,
                                                                             entityProxy.getGUID(),
                                                                             guidParameterName,
                                                                             resultingElementTypeName,
                                                                             requiredClassificationName,
                                                                             omittedClassificationName,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName));
                        }
                        catch (InvalidParameterException | UserNotAuthorizedException |
                               PropertyServerException inaccessibleEntity)
                        {
                            // skip entities that are not visible to this user
                            if (log.isDebugEnabled())
                            {
                                log.debug("Skipping inaccessible entity: " + inaccessibleEntity);
                            }
                        }
                    }
                }
            }
        }

        return visibleEntities;
    }


    /**
     * Return the elements of the requested type attached to an entity identified by the starting GUID.
     *
     * @param userId     calling user
     * @param anchorGUID expected anchorGUID for this element
     * @param anchorGUIDParameterName parameter supplying anchorGUID
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param attachmentRelationshipTypeGUID unique identifier of the relationship type connect to the attachment
     * @param attachmentRelationshipTypeName unique name of the relationship type connect to the attachment
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity.
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the repositories
     */
    public   List<B> getAttachedElements(String               userId,
                                         String               anchorGUID,
                                         String               anchorGUIDParameterName,
                                         String               startingGUID,
                                         String               startingGUIDParameterName,
                                         String               startingTypeName,
                                         String               attachmentRelationshipTypeGUID,
                                         String               attachmentRelationshipTypeName,
                                         String               attachmentEntityTypeName,
                                         String               requiredClassificationName,
                                         String               omittedClassificationName,
                                         int                  selectionEnd,
                                         List<InstanceStatus> limitResultsByStatus,
                                         Date                 asOfTime,
                                         SequencingOrder      sequencingOrder,
                                         String               sequencingProperty,
                                         boolean              forLineage,
                                         boolean              forDuplicateProcessing,
                                         int                  startingFrom,
                                         int                  pageSize,
                                         Date                 effectiveTime,
                                         String               methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        if (anchorGUID != null)
        {
            EntityDetail anchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                            startingGUID,
                                                                            startingGUIDParameterName,
                                                                            startingTypeName,
                                                                            true,
                                                                            false,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveTime,
                                                                            methodName);

            invalidParameterHandler.validateAnchorGUID(anchorGUID, anchorGUIDParameterName, anchorEntity, startingGUID, startingTypeName, methodName);
        }

        EntityDetail startingEntity = this.getEntityFromRepository(userId,
                                                                   startingGUID,
                                                                   startingGUIDParameterName,
                                                                   startingTypeName,
                                                                   null,
                                                                   null,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);

        /*
         * Get linked relationships
         */
        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     startingEntity,
                                                                                     startingTypeName,
                                                                                     attachmentRelationshipTypeGUID,
                                                                                     attachmentRelationshipTypeName,
                                                                                     selectionEnd,
                                                                                     limitResultsByStatus,
                                                                                     asOfTime,
                                                                                     sequencingOrder,
                                                                                     sequencingProperty,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     startingFrom,
                                                                                     pageSize,
                                                                                     effectiveTime,
                                                                                     methodName);

        if (relationships == null)
        {
            return null;
        }

        /*
         * Retrieve all of the entities linked to the relationships.  This is done as a single retrieve
         * to minimise the calls to the repositories.
         */
        Map<String, EntityDetail> retrievedEntities = this.getValidatedEntities(userId,
                                                                                startingGUID,
                                                                                startingTypeName,
                                                                                relationships,
                                                                                attachmentEntityTypeName,
                                                                                requiredClassificationName,
                                                                                omittedClassificationName,
                                                                                selectionEnd,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName);
        List<B>  results = new ArrayList<>();

        if (retrievedEntities != null)
        {
            retrievedEntities.put(startingEntity.getGUID(), startingEntity);

            for (Relationship  relationship : relationships)
            {
                if (relationship != null)
                {
                    try
                    {
                        EntityProxy entityProxy = null;

                        if (selectionEnd == 0)
                        {
                            entityProxy = repositoryHandler.getOtherEnd(startingGUID,
                                                                        startingTypeName,
                                                                        relationship,
                                                                        selectionEnd,
                                                                        methodName);
                        }
                        else if (selectionEnd == 1)
                        {
                            entityProxy = relationship.getEntityOneProxy();
                        }
                        else if (selectionEnd == 2)
                        {
                            entityProxy = relationship.getEntityTwoProxy();
                        }

                        if (entityProxy != null)
                        {
                            EntityDetail retrievedEntity = retrievedEntities.get(entityProxy.getGUID());

                            if (retrievedEntity != null)
                            {
                                B bean = converter.getNewBean(beanClass, retrievedEntity, relationship, methodName);
                                if (bean != null)
                                {
                                    results.add(bean);
                                }
                            }
                        }
                    }
                    catch (InvalidParameterException  | PropertyServerException inaccessibleEntity)
                    {
                        // skip entities that are not visible to this user
                        if (log.isDebugEnabled())
                        {
                            log.debug("Skipping inaccessible entity: " + inaccessibleEntity);
                        }
                    }
                }
            }
        }

        /*
         * Be sure to return an empty list even if all of the entities have been filtered out.
         * Null means no more to receive
         */
        return results;
    }


    /**
     * Validates a list of entities retrieved from the repository as a result of a query.
     * It validates supported zones read security and the anchor GUID.  It does not handle Mementos, effective dates
     * and element status, since they are the responsibility of the repository handler.
     *
     * @param userId           userId of user making request.
     * @param startingGUID identifier for the entity that the identifier is attached to
     * @param startingTypeName name of the type of object being attached to
     * @param retrievedRelationships  relationships retrieved from the repository
     * @param attachmentEntityTypeName unique name of the attached entity's type
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     *
     * @return map of validated entities
     */
    public Map<String, EntityDetail> getValidatedEntities(String             userId,
                                                          String             startingGUID,
                                                          String             startingTypeName,
                                                          List<Relationship> retrievedRelationships,
                                                          String             attachmentEntityTypeName,
                                                          String             requiredClassificationName,
                                                          String             omittedClassificationName,
                                                          int                selectionEnd,
                                                          boolean            forLineage,
                                                          boolean            forDuplicateProcessing,
                                                          Date               effectiveTime,
                                                          String             methodName) throws InvalidParameterException
    {
        if (retrievedRelationships != null)
        {
            final String guidParameterName = "relationship.end.guid";
            final String connectToGUIDParameterName = "relationship.entityProxy.guid";

            List<String>            extractedEntityGUIDs = new ArrayList<>();
            List<PropertyCondition> entityRetrievalQuery = new ArrayList<>();

            /*
             * Identify the entities that need to be retrieved.  The aim is to build a query that retrieves all of the
             * required entities in a single retrieve to minimize the calls to the repositories.
             */
            for (Relationship relationship : retrievedRelationships)
            {
                if (relationship != null)
                {
                    try
                    {
                        EntityProxy entityProxy = null;

                        if (selectionEnd == 0)
                        {
                            entityProxy = repositoryHandler.getOtherEnd(startingGUID,
                                                                        startingTypeName,
                                                                        relationship,
                                                                        selectionEnd,
                                                                        methodName);
                        }
                        else if (selectionEnd == 1)
                        {
                            entityProxy = relationship.getEntityOneProxy();
                        }
                        else if (selectionEnd == 2)
                        {
                            entityProxy = relationship.getEntityTwoProxy();
                        }

                        if (entityProxy != null)
                        {
                            if (! extractedEntityGUIDs.contains(entityProxy.getGUID()))
                            {
                                /*
                                 * Need to retrieve the entity for proxy so add to query
                                 */
                                PropertyCondition propertyCondition = new PropertyCondition();

                                propertyCondition.setProperty(OpenMetadataProperty.GUID.name);
                                propertyCondition.setOperator(PropertyComparisonOperator.EQ);

                                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

                                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                                primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
                                primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                                primitivePropertyValue.setPrimitiveValue(entityProxy.getGUID());

                                propertyCondition.setValue(primitivePropertyValue);

                                entityRetrievalQuery.add(propertyCondition);

                                extractedEntityGUIDs.add(entityProxy.getGUID());
                            }
                        }
                    }
                    catch (Exception invalidProxy)
                    {
                        // ignore this proxy because there is something wrong with it
                    }
                }
            }

            Map<String, EntityDetail> retrievedEntities = new HashMap<>();

            if (! extractedEntityGUIDs.isEmpty())
            {
                /*
                 * Retrieve the required entities
                 */
                if (attachmentEntityTypeName == null)
                {
                    retrievedEntities = this.getEntityList(userId,
                                                           OpenMetadataType.OPEN_METADATA_ROOT.typeGUID,
                                                           entityRetrievalQuery,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);
                }
                else
                {
                    String typeGUID = invalidParameterHandler.validateTypeName(attachmentEntityTypeName,
                                                                               OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                               serviceName,
                                                                               methodName,
                                                                               repositoryHelper);
                    retrievedEntities = this.getEntityList(userId,
                                                           typeGUID,
                                                           entityRetrievalQuery,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);
                }
            }

            /*
             * Gather visible entities
             */
            List<String>              validatedAnchors = new ArrayList<>();
            Map<String, EntityDetail> visibleEntities  = new HashMap<>();

            for (EntityDetail retrievedEntity : retrievedEntities.values())
            {
                /*
                 * Only process entities that are new.
                 */
                if ((retrievedEntity != null) && (!visibleEntities.containsKey(retrievedEntity.getGUID())))
                {
                    try
                    {
                        boolean beanValid = true;

                        if (requiredClassificationName != null)
                        {
                            try
                            {
                                if (repositoryHelper.getClassificationFromEntity(serviceName, retrievedEntity, requiredClassificationName, methodName) == null)
                                {
                                    beanValid = false;
                                }
                            }
                            catch (ClassificationErrorException error)
                            {
                                /*
                                 * Since this classification is not supported, it can not be attached to the entity
                                 */
                                beanValid = false;
                            }
                        }

                        if (omittedClassificationName != null)
                        {
                            try
                            {
                                if (repositoryHelper.getClassificationFromEntity(serviceName, retrievedEntity, omittedClassificationName, methodName) != null)
                                {
                                    beanValid = false;
                                }
                            }
                            catch (ClassificationErrorException error)
                            {
                                /*
                                 * Since this classification is not supported, it can not be attached to the entity
                                 */
                            }
                        }

                        if (beanValid)
                        {
                            /*
                             * If an entity has an anchor, the unique identifier of the anchor should be in the Anchors classifications.
                             * The exception occurs where the entity is not being managed by this handler, or something equivalent that
                             * maintains the Anchors classification.  Therefore, if the Anchors classification is missing, a new one is
                             * derived and added to the retrievedEntity.
                             */
                            AnchorIdentifiers anchorIdentifiers = this.getAnchorGUIDForEntity(retrievedEntity,
                                                                                              connectToGUIDParameterName,
                                                                                              forLineage,
                                                                                              forDuplicateProcessing,
                                                                                              effectiveTime,
                                                                                              methodName);

                            if (anchorIdentifiers != null)
                            {
                                String anchorGUID = anchorIdentifiers.anchorGUID;

                                if (anchorGUID == null)
                                {
                                    anchorGUID = retrievedEntity.getGUID();
                                }

                                if (!validatedAnchors.contains(anchorGUID))
                                {
                                    validatedAnchors.add(anchorGUID);

                                    this.validateEntityAndAnchorForRead(userId,
                                                                        attachmentEntityTypeName,
                                                                        retrievedEntity,
                                                                        guidParameterName,
                                                                        true,
                                                                        false,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);
                                }
                            }
                            else
                            {
                                /*
                                 * This first processing looks at the retrieved entity itself to ensure it is visible.
                                 */
                                validateRetrievedEntityIsVisible(userId,
                                                                 retrievedEntity,
                                                                 connectToGUIDParameterName,
                                                                 retrievedEntity.getType().getTypeDefName(),
                                                                 false,
                                                                 methodName);
                            }

                            visibleEntities.put(retrievedEntity.getGUID(), retrievedEntity);
                        }
                    }
                    catch (Exception unauthorizedEntity)
                    {
                        // ignore this entity since the user is not allowed to see it
                    }
                }
            }

            return visibleEntities;
        }

        return null;
    }


    /**
     * Return the entity for the supplied unique identifier (guid).  An exception is thrown if the entity does not exist.
     *
     * @param userId userId of the user making the request
     * @param requestedEntityGUID unique identifier of the entity to retrieve from the repository
     * @param requestedEntityGUIDParameterName name of the parameter supplying the GUID
     * @param requestedEntityTypeName name of type of entity to retrieve
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return retrieved entity
     * @throws InvalidParameterException the userId is null or invalid, the entity does not exist.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail getEntityFromRepository(String       userId,
                                                String       requestedEntityGUID,
                                                String       requestedEntityGUIDParameterName,
                                                String       requestedEntityTypeName,
                                                String       requiredClassificationName,
                                                String       omittedClassificationName,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return this.getEntityFromRepository(userId, requestedEntityGUID, requestedEntityGUIDParameterName, requestedEntityTypeName, requiredClassificationName, omittedClassificationName, forLineage, forDuplicateProcessing, null, effectiveTime, methodName);
    }


    /**
     * Return the entity for the supplied unique identifier (guid).  An exception is thrown if the entity does not exist.
     *
     * @param userId userId of the user making the request
     * @param requestedEntityGUID unique identifier of the entity to retrieve from the repository
     * @param requestedEntityGUIDParameterName name of the parameter supplying the GUID
     * @param requestedEntityTypeName name of type of entity to retrieve
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return retrieved entity
     * @throws InvalidParameterException the userId is null or invalid, the entity does not exist.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail getEntityFromRepository(String       userId,
                                                String       requestedEntityGUID,
                                                String       requestedEntityGUIDParameterName,
                                                String       requestedEntityTypeName,
                                                String       requiredClassificationName,
                                                String       omittedClassificationName,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                Date         asOfTime,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(requestedEntityGUID, requestedEntityGUIDParameterName, methodName);

        EntityDetail  retrievedEntity = repositoryHandler.getEntityByGUID(userId,
                                                                          requestedEntityGUID,
                                                                          requestedEntityGUIDParameterName,
                                                                          requestedEntityTypeName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          asOfTime,
                                                                          effectiveTime,
                                                                          methodName);

        /*
         * This method validates that the entity is visible to the calling user.
         */
        this.validateEntityAndAnchorForRead(userId,
                                            requestedEntityTypeName,
                                            retrievedEntity,
                                            requestedEntityGUIDParameterName,
                                            true,
                                            false,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

        boolean beanValid = true;

        if (requiredClassificationName != null)
        {
            try
            {
                if (repositoryHelper.getClassificationFromEntity(serviceName, retrievedEntity, requiredClassificationName, methodName) == null)
                {
                    beanValid = false;
                }
            }
            catch (ClassificationErrorException error)
            {
                /*
                 * Since this classification is not supported, it can not be attached to the entity
                 */
                beanValid = false;
            }
        }

        if (omittedClassificationName != null)
        {
            try
            {
                if (repositoryHelper.getClassificationFromEntity(serviceName, retrievedEntity, omittedClassificationName, methodName) != null)
                {
                    beanValid = false;
                }
            }
            catch (ClassificationErrorException error)
            {
                /*
                 * Since this classification is not supported, it can not be attached to the entity
                 */
            }
        }

        if (! beanValid)
        {
            return null;
        }

        return retrievedEntity;
    }


    /**
     * Use the supplied unique identifier (guid) of an entity in the repository to retrieve its contents as a bean.
     * The entity is checked to ensure it is of the required return type.
     *
     * @param userId calling user
     * @param guid unique identifier of the entity to retrieve
     * @param guidParameterName parameter supplying the unique identifier
     * @param resultTypeName type of the retrieve entity
     * @param methodName calling method
     * @return B bean
     * @throws InvalidParameterException one of the properties (probably the GUID) is invalid
     * @throws PropertyServerException the repository services hit an unexpected problem
     * @throws UserNotAuthorizedException the user is not permitted to access this entity
     */
    public B getBeanFromRepository(String userId,
                                   String guid,
                                   String guidParameterName,
                                   String resultTypeName,
                                   String methodName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          resultTypeName,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
    }


    /**
     * Return the bean for the supplied unique identifier (guid).  An exception occurs if the bean GUID is not known.
     *
     * @param userId userId of the user making the request
     * @param guid unique identifier of the entity to retrieve from the repository
     * @param guidParameterName name of the parameter supplying the GUID
     * @param entityTypeName name of type of entity to retrieve
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return new bean
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanFromRepository(String       userId,
                                   String       guid,
                                   String       guidParameterName,
                                   String       entityTypeName,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           guid,
                                                           guidParameterName,
                                                           entityTypeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        if (entity != null)
        {
            return converter.getNewBean(beanClass, entity, methodName);
        }

        return null;
    }


    /**
     * Return the bean for the supplied unique identifier (guid).  An exception occurs if the bean GUID is not known.
     *
     * @param userId userId of the user making the request
     * @param guid unique identifier of the entity to retrieve from the repository
     * @param guidParameterName name of the parameter supplying the GUID
     * @param entityTypeName name of type of entity to retrieve
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return new bean
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanFromRepository(String       userId,
                                   String       guid,
                                   String       guidParameterName,
                                   String       entityTypeName,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   Date         asOfTime,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           guid,
                                                           guidParameterName,
                                                           entityTypeName,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           asOfTime,
                                                           effectiveTime,
                                                           methodName);

        if (entity != null)
        {
            return converter.getNewBean(beanClass, entity, methodName);
        }

        return null;
    }


    /**
     * This method fills an instance properties object with the list of names properties, each with the supplied search value.
     * It is used when searching for a specific value that may be located in multiple properties.
     *
     * @param searchValue property value to store in each named property
     * @param specificMatchPropertyNames list of property names to
     * @param methodName calling method
     * @return instance properties object
     */
    private InstanceProperties getSearchInstanceProperties(String       searchValue,
                                                           List<String> specificMatchPropertyNames,
                                                           String       methodName)
    {
        InstanceProperties properties = new InstanceProperties();

        for (String propertyName : specificMatchPropertyNames)
        {
            if (propertyName != null)
            {
                properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          properties,
                                                                          propertyName,
                                                                          searchValue,
                                                                          methodName);
            }
        }

        return properties;
    }


    /**
     * Return the unique identifier of the entity that has the supplied unique name. An exception is thrown if
     * multiple entities are found with this name.
     *
     * @param userId the calling user
     * @param name  value to search
     * @param nameParameterName parameter providing value
     * @param namePropertyName open metadata property name to match on
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the requested entity/bean
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getBeanGUIDByUniqueName(String               userId,
                                          String               name,
                                          String               nameParameterName,
                                          String               namePropertyName,
                                          String               resultTypeGUID,
                                          String               resultTypeName,
                                          List<InstanceStatus> limitResultsByStatus,
                                          Date                 asOfTime,
                                          SequencingOrder      sequencingOrder,
                                          String               sequencingPropertyName,
                                          boolean              forLineage,
                                          boolean              forDuplicateProcessing,
                                          Date                 effectiveTime,
                                          String               methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int queryPageSize = invalidParameterHandler.getMaxPagingSize();

        InstanceProperties matchProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          null,
                                                                                          namePropertyName,
                                                                                          name,
                                                                                          methodName);

        List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                                   matchProperties,
                                                                                   resultTypeGUID,
                                                                                   limitResultsByStatus,
                                                                                   null,
                                                                                   asOfTime,
                                                                                   sequencingOrder,
                                                                                   sequencingPropertyName,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   0,
                                                                                   queryPageSize,
                                                                                   effectiveTime,
                                                                                   methodName);


        /*
         * The loop is necessary because some entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        String        guid = null;
        Set<String>   duplicateEntities = new HashSet<>();
        String        entityParameterName = "Entity from search of value " + name;

        if (retrievedEntities != null)
        {
            for (EntityDetail entity : retrievedEntities)
            {
                if (entity != null)
                {
                    duplicateEntities.add(entity.getGUID());

                    try
                    {
                        validateEntityAndAnchorForRead(userId,
                                                       resultTypeName,
                                                       entity,
                                                       entityParameterName,
                                                       true,
                                                       false,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       effectiveTime,
                                                       methodName);

                        /*
                         * Valid entity to return since no exception occurred.
                         */
                        if (guid == null)
                        {
                            guid = entity.getGUID();
                        }
                    }
                    catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException invisibleEntity)
                    {
                        /*
                         * Skipping entity
                         */
                    }
                }
            }
        }

        if (guid == null)
        {
            return null;
        }
        else if (duplicateEntities.size() == 1)
        {
            return guid;
        }

        throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_ENTITIES_FOUND.getMessageDefinition(resultTypeName,
                                                                                                                name,
                                                                                                                duplicateEntities.toString(),
                                                                                                                methodName,
                                                                                                                nameParameterName,
                                                                                                                serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Return the list of beans of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param name  value to search
     * @param nameParameterName parameter providing value
     * @param namePropertyName open metadata property name to match on
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanByUniqueName(String               userId,
                                 String               name,
                                 String               nameParameterName,
                                 String               namePropertyName,
                                 String               resultTypeGUID,
                                 String               resultTypeName,
                                 List<InstanceStatus> limitResultsByStatus,
                                 Date                 asOfTime,
                                 SequencingOrder      sequencingOrder,
                                 String               sequencingPropertyName,
                                 boolean              forLineage,
                                 boolean              forDuplicateProcessing,
                                 Date                 effectiveTime,
                                 String               methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int queryPageSize = invalidParameterHandler.getMaxPagingSize();

        InstanceProperties matchProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          null,
                                                                                          namePropertyName,
                                                                                          name,
                                                                                          methodName);

        List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                                   matchProperties,
                                                                                   resultTypeGUID,
                                                                                   limitResultsByStatus,
                                                                                   null,
                                                                                   asOfTime,
                                                                                   sequencingOrder,
                                                                                   sequencingPropertyName,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   0,
                                                                                   queryPageSize,
                                                                                   effectiveTime,
                                                                                   methodName);

        /*
         * The loop is necessary because some entities returned may not be visible to the calling user.
         * Once they are filtered out, more entities need to be retrieved to fill the gaps.
         */
        B            bean = null;
        Set<String>  duplicateEntities = new HashSet<>();
        String       entityParameterName = "Entity from search of value " + name;

        if (retrievedEntities != null)
        {
            for (EntityDetail entity : retrievedEntities)
            {

                if (entity != null)
                {
                    duplicateEntities.add(entity.getGUID());

                    try
                    {
                        validateEntityAndAnchorForRead(userId,
                                                       resultTypeName,
                                                       entity,
                                                       entityParameterName,
                                                       true,
                                                       false,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       effectiveTime,
                                                       methodName);

                        /*
                         * Valid entity to return since no exception occurred.
                         */
                        if (bean == null)
                        {
                            bean = converter.getNewBean(beanClass, entity, methodName);
                        }
                    }
                    catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException invisibleEntity)
                    {
                        /*
                         * Skipping entity
                         */
                    }
                }
            }
        }

        if (bean == null)
        {
            return null;
        }
        else if (duplicateEntities.size() == 1)
        {
            return bean;
        }


        throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_ENTITIES_FOUND.getMessageDefinition(resultTypeName,
                                                                                                                name,
                                                                                                                duplicateEntities.toString(),
                                                                                                                methodName,
                                                                                                                nameParameterName,
                                                                                                                serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Retrieve the version history for an entity.
     *
     * @param userId calling user
     * @param guid unique identifier of object to update
     * @param guidParameterName name of parameter supplying the GUID
     * @param fromTime starting time
     * @param toTime ending time
     * @param entityTypeName unique name of the entity's type
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param sequencingOrder order of the results
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
    public List<B> getBeanHistory(String                 userId,
                                  String                 guid,
                                  String                 guidParameterName,
                                  String                 entityTypeName,
                                  Date                   fromTime,
                                  Date                   toTime,
                                  int                    startingFrom,
                                  int                    pageSize,
                                  HistorySequencingOrder sequencingOrder,
                                  boolean                forLineage,
                                  boolean                forDuplicateProcessing,
                                  Date                   effectiveTime,
                                  String                 methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        this.validateEntityAndAnchorForRead(userId,
                                            guid,
                                            guidParameterName,
                                            entityTypeName,
                                            true,
                                            true,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

        List<EntityDetail> entities = repositoryHandler.getEntityDetailHistory(userId,
                                                                               guid,
                                                                               fromTime,
                                                                               toTime,
                                                                               startingFrom,
                                                                               pageSize,
                                                                               sequencingOrder,
                                                                               methodName);

        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                results.add(converter.getNewBean(beanClass, entity, methodName));
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Retrieve the version history for an entity's classification.
     *
     * @param userId calling user
     * @param guid unique identifier of object to update
     * @param guidParameterName name of parameter supplying the GUID
     * @param fromTime starting time
     * @param toTime ending time
     * @param entityTypeName unique name of the entity's type
     * @param classificationName name of the desired classification
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param sequencingOrder order of the results
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
    public List<AttachedClassification> getClassificationHistory(String                 userId,
                                                                 String                 guid,
                                                                 String                 guidParameterName,
                                                                 String                 entityTypeName,
                                                                 String                 classificationName,
                                                                 Date                   fromTime,
                                                                 Date                   toTime,
                                                                 int                    startingFrom,
                                                                 int                    pageSize,
                                                                 HistorySequencingOrder sequencingOrder,
                                                                 boolean                forLineage,
                                                                 boolean                forDuplicateProcessing,
                                                                 Date                   effectiveTime,
                                                                 String                 methodName) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String classificationParameterName = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        this.validateEntityAndAnchorForRead(userId,
                                            guid,
                                            guidParameterName,
                                            entityTypeName,
                                            true,
                                            true,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

        return converter.getAttachedClassifications(repositoryHandler.getClassificationHistory(userId,
                                                                                               guid,
                                                                                               classificationName,
                                                                                               fromTime,
                                                                                               toTime,
                                                                                               startingFrom,
                                                                                               pageSize,
                                                                                               sequencingOrder,
                                                                                               methodName));
    }


    /**
     * Return the bean that matches the requested value.
     *
     * @param userId identifier of calling user
     * @param value  value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return matching bean.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the bean definition.
     */
    public  B getBeanByValue(String               userId,
                             String               value,
                             String               valueParameterName,
                             String               resultTypeGUID,
                             String               resultTypeName,
                             List<String>         specificMatchPropertyNames,
                             List<InstanceStatus> limitResultsByStatus,
                             Date                 asOfTime,
                             SequencingOrder      sequencingOrder,
                             String               sequencingPropertyName,
                             boolean              forLineage,
                             boolean              forDuplicateProcessing,
                             Date                 effectiveTime,
                             String               methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        List<EntityDetail> results = this.getEntitiesByValue(userId,
                                                             value,
                                                             valueParameterName,
                                                             resultTypeGUID,
                                                             resultTypeName,
                                                             specificMatchPropertyNames,
                                                             false,
                                                             false,
                                                             null,
                                                             null,
                                                             limitResultsByStatus,
                                                             asOfTime,
                                                             sequencingOrder,
                                                             sequencingPropertyName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             0,
                                                             invalidParameterHandler.getMaxPagingSize(),
                                                             effectiveTime,
                                                             methodName);


        if ((results == null) || (results.isEmpty()))
        {
            return null;
        }
        else if (results.size() == 1)
        {
            return converter.getNewBean(beanClass, results.get(0), methodName);
        }
        else
        {
            errorHandler.handleAmbiguousEntityName(value,
                                                   valueParameterName,
                                                   resultTypeName,
                                                   results,
                                                   methodName);
        }

        return null;
    }



    /**
     * Return the list of beans matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forDuplicateProcessing this request os for duplicate processing so do not deduplicate
     * @param forLineage this request is for lineage so ignore Memento classifications
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findBeans(String               userId,
                             String               searchString,
                             String               searchStringParameterName,
                             String               resultTypeGUID,
                             String               resultTypeName,
                             List<InstanceStatus> limitResultsByStatus,
                             Date                 asOfTime,
                             SequencingOrder      sequencingOrder,
                             String               sequencingPropertyName,
                             boolean              forLineage,
                             boolean              forDuplicateProcessing,
                             int                  startFrom,
                             int                  pageSize,
                             Date                 effectiveTime,
                             String               methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return this.getBeansByValue(userId,
                                    searchString,
                                    searchStringParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    null,
                                    false,
                                    null,
                                    null,
                                    limitResultsByStatus,
                                    asOfTime,
                                    sequencingOrder,
                                    sequencingPropertyName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeName optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param searchClassifications Optional list of classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<B> findBeans(String                userId,
                             String                metadataElementTypeName,
                             List<String>          metadataElementSubtypeName,
                             SearchProperties      searchProperties,
                             List<InstanceStatus>  limitResultsByStatus,
                             SearchClassifications searchClassifications,
                             Date                  asOfTime,
                             String                sequencingProperty,
                             SequencingOrder       sequencingOrder,
                             boolean               forLineage,
                             boolean               forDuplicateProcessing,
                             int                   startingFrom,
                             int                   pageSize,
                             Date                  effectiveTime,
                             String                methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        List<EntityDetail> entities = this.findEntities(userId,
                                                        metadataElementTypeName,
                                                        metadataElementSubtypeName,
                                                        searchProperties,
                                                        limitResultsByStatus,
                                                        searchClassifications,
                                                        asOfTime,
                                                        sequencingProperty,
                                                        sequencingOrder,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        startingFrom,
                                                        pageSize,
                                                        effectiveTime,
                                                        methodName);

        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    results.add(converter.getNewBean(beanClass, entity, methodName));
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }



    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param searchClassifications Optional list of classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param startingFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<EntityDetail> findEntities(String                userId,
                                           String                metadataElementTypeName,
                                           List<String>          metadataElementSubtypeNames,
                                           SearchProperties      searchProperties,
                                           List<InstanceStatus>  limitResultsByStatus,
                                           SearchClassifications searchClassifications,
                                           Date                  asOfTime,
                                           String                sequencingProperty,
                                           SequencingOrder       sequencingOrder,
                                           boolean               forLineage,
                                           boolean               forDuplicateProcessing,
                                           int                   startingFrom,
                                           int                   pageSize,
                                           Date                  effectiveTime,
                                           String                methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        String typeName = OpenMetadataType.OPEN_METADATA_ROOT.typeName;

        if (metadataElementTypeName != null)
        {
            typeName = metadataElementTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        List<String> subTypeGUIDs = null;

        if (metadataElementSubtypeNames != null)
        {
            subTypeGUIDs = new ArrayList<>();

            for (String subTypeName : metadataElementSubtypeNames)
            {
                if (subTypeName != null)
                {
                    String subTypeGUID = invalidParameterHandler.validateTypeName(subTypeName,
                                                                                  typeName,
                                                                                  serviceName,
                                                                                  methodName,
                                                                                  repositoryHelper);

                    if (subTypeGUID != null)
                    {
                        subTypeGUIDs.add(subTypeGUID);
                    }
                }
            }
        }

        if ((searchClassifications != null) && (searchClassifications.getConditions() != null))
        {
            for (ClassificationCondition classificationCondition : searchClassifications.getConditions())
            {
                if (classificationCondition != null)
                {
                    invalidParameterHandler.validateTypeDefName(classificationCondition.getName(),
                                                                null,
                                                                serviceName,
                                                                methodName,
                                                                repositoryHelper);

                }
            }
        }

        List<EntityDetail> retrievedEntities = repositoryHandler.findEntities(userId,
                                                                              typeGUID,
                                                                              subTypeGUIDs,
                                                                              searchProperties,
                                                                              limitResultsByStatus,
                                                                              searchClassifications,
                                                                              asOfTime,
                                                                              sequencingProperty,
                                                                              sequencingOrder,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              startingFrom,
                                                                              queryPageSize,
                                                                              effectiveTime,
                                                                              methodName);

        return this.getValidatedEntities(userId,
                                         retrievedEntities,
                                         null,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Return the list of beans of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param value value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param exactValueMatch indicates whether the value must match the whole property value in a matching result, or whether it is a
     *                        RegEx partial match
     * @param requiredClassificationName  String the name of the classification that must be on the entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the entity.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getBeansByValue(String               userId,
                                   String               value,
                                   String               valueParameterName,
                                   String               resultTypeGUID,
                                   String               resultTypeName,
                                   List<String>         specificMatchPropertyNames,
                                   boolean              exactValueMatch,
                                   String               requiredClassificationName,
                                   String               omittedClassificationName,
                                   List<InstanceStatus> limitResultsByStatus,
                                   Date                 asOfTime,
                                   SequencingOrder      sequencingOrder,
                                   String               sequencingPropertyName,
                                   boolean              forLineage,
                                   boolean              forDuplicateProcessing,
                                   int                  startFrom,
                                   int                  pageSize,
                                   Date                 effectiveTime,
                                   String               methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        List<EntityDetail> entities = getEntitiesByValue(userId,
                                                         value,
                                                         valueParameterName,
                                                         resultTypeGUID,
                                                         resultTypeName,
                                                         specificMatchPropertyNames,
                                                         exactValueMatch,
                                                         false,
                                                         requiredClassificationName,
                                                         omittedClassificationName,
                                                         limitResultsByStatus,
                                                         asOfTime,
                                                         sequencingOrder,
                                                         sequencingPropertyName,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         startFrom,
                                                         pageSize,
                                                         effectiveTime,
                                                         methodName);


        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    B bean = converter.getNewBean(beanClass, entity, methodName);

                    results.add(bean);
                }
            }

            return  results;
        }

        return null;
    }


    /**
     * Return the list of entities of the requested type.
     *
     * @param userId the name of the calling user
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique name of the type that the results should match with
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of entities
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> getEntitiesByType(String               userId,
                                                String               resultTypeGUID,
                                                String               resultTypeName,
                                                List<InstanceStatus> limitResultsByStatus,
                                                Date                 asOfTime,
                                                SequencingOrder      sequencingOrder,
                                                String               sequencingPropertyName,
                                                boolean              forLineage,
                                                boolean              forDuplicateProcessing,
                                                int                  startFrom,
                                                int                  pageSize,
                                                Date                 effectiveTime,
                                                String               methodName) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesForType(userId,
                                                                                    resultTypeGUID,
                                                                                    resultTypeName,
                                                                                    limitResultsByStatus,
                                                                                    null,
                                                                                    asOfTime,
                                                                                    sequencingOrder,
                                                                                    sequencingPropertyName,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing,
                                                                                    startFrom,
                                                                                    queryPageSize,
                                                                                    effectiveTime,
                                                                                    methodName);

        return this.validateEntitiesAndAnchorsForRead(userId,
                                                      retrievedEntities,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
    }


    /**
     * Return the list of entities matching the supplied searchString.
     *
     * @param userId the searchString of the calling user.
     * @param searchString searchString of endpoint.  This may include wild card characters.
     * @param searchStringParameterName name of parameter providing search string
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param requiredClassificationName  String the name of the classification that must be on the entity.
     * @param omittedClassificationName   String the name of a classification that must not be on the entity.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of entities
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> findEntities(String               userId,
                                           String               searchString,
                                           String               searchStringParameterName,
                                           String               resultTypeGUID,
                                           String               resultTypeName,
                                           String               requiredClassificationName,
                                           String               omittedClassificationName,
                                           int                  startFrom,
                                           int                  pageSize,
                                           List<InstanceStatus> limitResultsByStatus,
                                           Date                 asOfTime,
                                           SequencingOrder      sequencingOrder,
                                           String               sequencingPropertyName,
                                           boolean              forLineage,
                                           boolean              forDuplicateProcessing,
                                           Date                 effectiveTime,
                                           String               methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        return this.getEntitiesByValue(userId,
                                       searchString,
                                       searchStringParameterName,
                                       resultTypeGUID,
                                       resultTypeName,
                                       null,
                                       false,
                                       false,
                                       requiredClassificationName,
                                       omittedClassificationName,
                                       limitResultsByStatus,
                                       asOfTime,
                                       sequencingOrder,
                                       sequencingPropertyName,
                                       forLineage,
                                       forDuplicateProcessing,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       methodName);
    }



    /**
     * Return the list of entities of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param value value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param exactValueMatch indicates whether the value must match the whole property value in a matching result, or whether it is a
     *                        RegEx partial match
     * @param caseInsensitive set to true to have a case-insensitive exact match regular expression
     * @param requiredClassificationName  String the name of the classification that must be on the attached entity
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of entities
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> getEntitiesByValue(String               userId,
                                                 String               value,
                                                 String               valueParameterName,
                                                 String               resultTypeGUID,
                                                 String               resultTypeName,
                                                 List<String>         specificMatchPropertyNames,
                                                 boolean              exactValueMatch,
                                                 boolean              caseInsensitive,
                                                 String               requiredClassificationName,
                                                 String               omittedClassificationName,
                                                 List<InstanceStatus> limitResultsByStatus,
                                                 Date                 asOfTime,
                                                 SequencingOrder      sequencingOrder,
                                                 String               sequencingPropertyName,
                                                 boolean              forLineage,
                                                 boolean              forDuplicateProcessing,
                                                 int                  startFrom,
                                                 int                  pageSize,
                                                 Date                 effectiveTime,
                                                 String               methodName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(value, valueParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<String> limitResultsByClassifications = null;

        if (requiredClassificationName != null)
        {
            limitResultsByClassifications = new ArrayList<>();

            limitResultsByClassifications.add(requiredClassificationName);
        }

        List<EntityDetail> retrievedEntities;

        if ((specificMatchPropertyNames == null) || (specificMatchPropertyNames.isEmpty()))
        {
            retrievedEntities = repositoryHandler.getEntitiesByValue(userId,
                                                                     value,
                                                                     resultTypeGUID,
                                                                     limitResultsByStatus,
                                                                     limitResultsByClassifications,
                                                                     asOfTime,
                                                                     sequencingOrder,
                                                                     sequencingPropertyName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     startFrom,
                                                                     queryPageSize,
                                                                     effectiveTime,
                                                                     methodName);
        }
        else
        {
            retrievedEntities = repositoryHandler.getEntitiesByName(userId,
                                                                    this.getSearchInstanceProperties(value, specificMatchPropertyNames, methodName),
                                                                    resultTypeGUID,
                                                                    limitResultsByStatus,
                                                                    limitResultsByClassifications,
                                                                    asOfTime,
                                                                    sequencingOrder,
                                                                    sequencingPropertyName,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    startFrom,
                                                                    queryPageSize,
                                                                    effectiveTime,
                                                                    methodName);
        }

        return getValidatedEntities(userId,
                                    retrievedEntities,
                                    omittedClassificationName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the list of entities of the requested type that match the supplied value.
     *
     * @param userId the calling user
     * @param retrievedEntities entities from the repository
     * @param omittedClassificationName   String the name of a classification that must not be on the attached entity
     * @param forLineage                   boolean indicating whether the entity is being retrieved for a lineage request or not
     * @param forDuplicateProcessing       the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans
     * @throws InvalidParameterException bad starting entity
     * @throws PropertyServerException there is a problem retrieving information from the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<EntityDetail> getValidatedEntities(String             userId,
                                                   List<EntityDetail> retrievedEntities,
                                                   String             omittedClassificationName,
                                                   boolean            forLineage,
                                                   boolean            forDuplicateProcessing,
                                                   Date               effectiveTime,
                                                   String             methodName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        List<EntityDetail> validatedEntities = this.validateEntitiesAndAnchorsForRead(userId,
                                                                                      retrievedEntities,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing,
                                                                                      effectiveTime,
                                                                                      methodName);
        if (validatedEntities != null)
        {
            /*
             * The loop is necessary because some entities returned may not be visible to the calling user.
             */
            List<EntityDetail> results = new ArrayList<>();

            for (EntityDetail entity : validatedEntities)
            {
                if (entity != null)
                {
                    boolean beanValid = true;

                    if (omittedClassificationName != null)
                    {
                        try
                        {
                            if (repositoryHelper.getClassificationFromEntity(serviceName, entity, omittedClassificationName, methodName) != null)
                            {
                                beanValid = false;
                            }
                        }
                        catch (ClassificationErrorException error)
                        {
                            // ok - don't care
                        }
                    }

                    if (beanValid)
                    {
                        results.add(entity);
                    }
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Return the entity that matches the requested value.
     *
     * @param userId identifier of calling user
     * @param value  value to search
     * @param valueParameterName parameter providing value
     * @param resultTypeGUID unique identifier of the type that the results should match with
     * @param resultTypeName unique value of the type that the results should match with
     * @param specificMatchPropertyNames list of property value to look in - if null or empty list then all string properties are checked.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return requested entity
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    public  EntityDetail getEntityByValue(String               userId,
                                          String               value,
                                          String               valueParameterName,
                                          String               resultTypeGUID,
                                          String               resultTypeName,
                                          List<String>         specificMatchPropertyNames,
                                          List<InstanceStatus> limitResultsByStatus,
                                          Date                 asOfTime,
                                          SequencingOrder      sequencingOrder,
                                          String               sequencingPropertyName,
                                          boolean              forLineage,
                                          boolean              forDuplicateProcessing,
                                          Date                 effectiveTime,
                                          String               methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        List<EntityDetail> results = this.getEntitiesByValue(userId,
                                                             value,
                                                             valueParameterName,
                                                             resultTypeGUID,
                                                             resultTypeName,
                                                             specificMatchPropertyNames,
                                                             true,
                                                             false,
                                                             null,
                                                             null,
                                                             limitResultsByStatus,
                                                             asOfTime,
                                                             sequencingOrder,
                                                             sequencingPropertyName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             0,
                                                             invalidParameterHandler.getMaxPagingSize(),
                                                             effectiveTime,
                                                             methodName);


        if (results != null)
        {
            if (results.size() == 1)
            {
                return results.get(0);
            }
            else if (results.size() > 1)
            {
                errorHandler.handleAmbiguousEntityName(value,
                                                       valueParameterName,
                                                       resultTypeName,
                                                       results,
                                                       methodName);
            }
        }

        return null;
    }


    /**
     * Determine if the entities returned from the repository should be converted to beans and returned to caller.
     *
     * @param userId calling user
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @param retrievedEntities entities retrieved from the repository
     * @return list of beans
     * @throws PropertyServerException problem in converter
     */
    public List<B> getValidatedBeans(String             userId,
                                     Date               effectiveTime,
                                     boolean            forLineage,
                                     boolean            forDuplicateProcessing,
                                     String             methodName,
                                     List<EntityDetail> retrievedEntities) throws PropertyServerException
    {
        List<EntityDetail> validatedEntities = this.validateEntitiesAndAnchorsForRead(userId,
                                                                                      retrievedEntities,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing,
                                                                                      effectiveTime,
                                                                                      methodName);

        if (validatedEntities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : validatedEntities)
            {
                if (entity != null)
                {
                    results.add(converter.getNewBean(beanClass, entity, methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Creates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the link.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param relationshipTypeGUID      unique identifier of type of the relationship to create
     * @param relationshipTypeName      unique name of type of the relationship to create
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime             the time that the retrieved elements must be effective for
     * @param methodName                calling method
     *
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.

    public String linkElementToElement(String              userId,
                                       String              externalSourceGUID,
                                       String              externalSourceName,
                                       String              startingGUID,
                                       String              startingGUIDParameterName,
                                       String              startingElementTypeName,
                                       String              attachingGUID,
                                       String              attachingGUIDParameterName,
                                       String              attachingElementTypeName,
                                       boolean             forLineage,
                                       boolean             forDuplicateProcessing,
                                       String              relationshipTypeGUID,
                                       String              relationshipTypeName,
                                       Date                effectiveFrom,
                                       Date                effectiveTo,
                                       Map<String, Object> relationshipProperties,
                                       Date                effectiveTime,
                                       String              methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        InstanceProperties instanceProperties = null;

        try
        {
            instanceProperties = repositoryHelper.addPropertyMapToInstance(serviceName, null, relationshipProperties, methodName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
        {
            final String  propertyName = "relationshipProperties";

            errorHandler.handleUnsupportedProperty(error, methodName, propertyName);
        }

        return this.linkElementToElement(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         startingGUID,
                                         startingGUIDParameterName,
                                         startingElementTypeName,
                                         attachingGUID,
                                         attachingGUIDParameterName,
                                         attachingElementTypeName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         relationshipTypeGUID,
                                         relationshipTypeName,
                                         this.setUpEffectiveDates(instanceProperties, effectiveFrom, effectiveTo),
                                         effectiveFrom,
                                         effectiveTo,
                                         effectiveTime,
                                         methodName);
    }
    */


    /**
     * Creates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the link.  If the relationship already exists with matching effectivity dates,
     * the properties are updated.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingElementGUID       unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingElementGUID      unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the relationship to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkElementToElement(String             userId,
                                       String             externalSourceGUID,
                                       String             externalSourceName,
                                       String             startingElementGUID,
                                       String             startingGUIDParameterName,
                                       String             startingElementTypeName,
                                       String             attachingElementGUID,
                                       String             attachingGUIDParameterName,
                                       String             attachingElementTypeName,
                                       boolean            forLineage,
                                       boolean            forDuplicateProcessing,
                                       String             attachmentTypeGUID,
                                       String             attachmentTypeName,
                                       InstanceProperties relationshipProperties,
                                       Date               effectiveFrom,
                                       Date               effectiveTo,
                                       Date               effectiveTime,
                                       String             methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingElementGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachingElementGUID, attachingGUIDParameterName, methodName);

        EntityDetail bean1Entity = repositoryHandler.getEntityByGUID(userId,
                                                                     startingElementGUID,
                                                                     startingGUIDParameterName,
                                                                     startingElementTypeName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

        EntityDetail anchor1Entity = this.validateEntityAndAnchorForRead(userId,
                                                                         startingElementTypeName,
                                                                         bean1Entity,
                                                                         startingGUIDParameterName,
                                                                         true,
                                                                         false,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        EntityDetail bean2Entity = repositoryHandler.getEntityByGUID(userId,
                                                                     attachingElementGUID,
                                                                     attachingGUIDParameterName,
                                                                     attachingElementTypeName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

        EntityDetail anchor2Entity = this.validateEntityAndAnchorForRead(userId,
                                                                         attachingElementTypeName,
                                                                         bean2Entity,
                                                                         attachingGUIDParameterName,
                                                                         true,
                                                                         false,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        /*
         * Check that the user is able to create relationships
         */
        if ((repositoryHelper.isTypeOf(serviceName, attachmentTypeName, OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, attachmentTypeName, OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, attachmentTypeName, OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName)) ||
                (repositoryHelper.isTypeOf(serviceName, attachmentTypeName, OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName)))
        {
            if (anchor1Entity != null)
            {
                securityVerifier.validateUserForAnchorAddFeedback(userId,
                                                                  anchor1Entity,
                                                                  bean2Entity,
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  methodName);
            }
            else
            {
                securityVerifier.validateUserForElementAddFeedback(userId,
                                                                   bean1Entity,
                                                                   bean2Entity,
                                                                   repositoryHelper,
                                                                   serviceName,
                                                                   methodName);
            }
        }
        else if (anchor1Entity != null)
        {
            if (anchor2Entity != null)
            {
                if (anchor1Entity.getGUID().equals(anchor2Entity.getGUID()))
                {
                    securityVerifier.validateUserForAnchorMemberUpdate(userId,
                                                                       anchor1Entity,
                                                                       repositoryHelper,
                                                                       serviceName,
                                                                       methodName);
                }
                else
                {
                    securityVerifier.validateUserForAnchorAttach(userId,
                                                                 anchor1Entity,
                                                                 bean2Entity,
                                                                 attachmentTypeName,
                                                                 repositoryHelper,
                                                                 serviceName,
                                                                 methodName);
                }
            }
        }
        else if (anchor2Entity == null)
        {
            securityVerifier.validateUserForElementAttach(userId,
                                                          bean1Entity,
                                                          bean2Entity,
                                                          attachmentTypeName,
                                                          repositoryHelper,
                                                          serviceName,
                                                          methodName);
        }
        else // anchor1 is null but anchor2 is not
        {
            securityVerifier.validateUserForAnchorAttach(userId,
                                                         anchor2Entity,
                                                         bean1Entity,
                                                         attachmentTypeName,
                                                         repositoryHelper,
                                                         serviceName,
                                                         methodName);
        }


        /*
         * The calls above validate the existence of the two entities and that they are visible to the user.
         * An exception is thrown if there are any problems.
         * The anchor entities are returned if there are anchor entities associated with a specific end.
         *
         * The next test ensures that the effectivity dates in the new relationship's properties are compatible with the
         * existing relationships.
         */
        List<Relationship> existingRelationships = repositoryHandler.getRelationshipsBetweenEntities(userId,
                                                                                                     bean1Entity,
                                                                                                     startingElementTypeName,
                                                                                                     bean2Entity.getGUID(),
                                                                                                     attachmentTypeGUID,
                                                                                                     attachmentTypeName,
                                                                                                     2,
                                                                                                     null,
                                                                                                     null,
                                                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                                                     null,
                                                                                                     forLineage,
                                                                                                     forDuplicateProcessing,
                                                                                                     effectiveFrom,
                                                                                                     effectiveTo,
                                                                                                     true,
                                                                                                     methodName);

        Relationship newRelationship;
        String actionDescriptionTemplate;
        LatestChangeAction actionOrdinal;

        if (existingRelationships != null)
        {
            if (existingRelationships.size() == 1)
            {
                actionDescriptionTemplate = "Updating link from %s %s to %s %s";
                actionOrdinal = LatestChangeAction.UPDATED;

                newRelationship = repositoryHandler.updateRelationshipProperties(userId,
                                                                                 externalSourceGUID,
                                                                                 externalSourceName,
                                                                                 existingRelationships.get(0),
                                                                                 relationshipProperties,
                                                                                 methodName);
            }
            else
            {
                String guids = null;

                for (Relationship relationship : existingRelationships)
                {
                    if (relationship != null)
                    {
                        if (guids == null)
                        {
                            guids = "[";
                        }
                        else
                        {
                            guids = ", ";
                        }

                        guids = guids + relationship.getGUID() + " ";
                    }
                }

                if (guids == null)
                {
                    guids = "[]";
                }
                else
                {
                    guids = guids + "]";
                }

                throw new InvalidParameterException(GenericHandlersErrorCode.MULTIPLE_RELATIONSHIPS_FOUND.getMessageDefinition(attachmentTypeName,
                                                                                                                               startingElementTypeName,
                                                                                                                               bean1Entity.getGUID(),
                                                                                                                               attachingElementTypeName,
                                                                                                                               bean2Entity.getGUID(),
                                                                                                                               guids,
                                                                                                                               methodName,
                                                                                                                               serverName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    attachmentTypeName);
            }
        }
        else
        {
            actionDescriptionTemplate = "Linking %s %s to %s %s";
            actionOrdinal = LatestChangeAction.CREATED;

            newRelationship = repositoryHandler.createRelationship(userId,
                                                                   attachmentTypeGUID,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   bean1Entity.getGUID(),
                                                                   bean2Entity.getGUID(),
                                                                   relationshipProperties,
                                                                   methodName);
        }

        /*
         * Final stage is to add the latest change classification to the anchor(s).
         * The act of creating the relationship may set up the anchor GUID in either element.
         */
        if (anchor1Entity == null)
        {
            String startingElementAnchorGUID = this.reEvaluateAnchorGUID(bean1Entity.getGUID(),
                                                                         startingGUIDParameterName,
                                                                         startingElementTypeName,
                                                                         bean1Entity,
                                                                         null,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

            if (startingElementAnchorGUID != null)
            {
                if ((anchor2Entity != null) && (anchor2Entity.getGUID().equals(startingElementAnchorGUID)))
                {
                    anchor1Entity = anchor2Entity; // save a lookup
                }
                else if (startingElementAnchorGUID.equals(bean1Entity.getGUID()))
                {
                    anchor1Entity = bean1Entity;
                }
                else if (startingElementAnchorGUID.equals(bean2Entity.getGUID()))
                {
                    anchor1Entity = bean2Entity;
                }
                else
                {
                    final String anchorGUIDParameterName = "startingElementAnchorGUID";

                    anchor1Entity = repositoryHandler.getEntityByGUID(userId,
                                                                      startingElementAnchorGUID,
                                                                      anchorGUIDParameterName,
                                                                      OpenMetadataType.REFERENCEABLE.typeName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);
                }
            }
            else
            {
                anchor1Entity = bean1Entity;
            }
        }

        if (anchor2Entity == null)
        {
            String attachingElementAnchorGUID = this.reEvaluateAnchorGUID(bean2Entity.getGUID(),
                                                                          attachingGUIDParameterName,
                                                                          attachingElementTypeName,
                                                                          bean2Entity,
                                                                          null,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

            if (attachingElementAnchorGUID != null)
            {
                if (attachingElementAnchorGUID.equals(anchor1Entity.getGUID()))
                {
                    anchor2Entity = anchor1Entity; // save a look-up
                }
                else if (attachingElementAnchorGUID.equals(bean1Entity.getGUID()))
                {
                    anchor2Entity = bean1Entity;
                }
                else if (attachingElementAnchorGUID.equals(bean2Entity.getGUID()))
                {
                    anchor2Entity = bean2Entity;
                }
                else
                {
                    final String anchorGUIDParameterName = "attachingElementAnchorGUID";

                    anchor2Entity = repositoryHandler.getEntityByGUID(userId,
                                                                      attachingElementAnchorGUID,
                                                                      anchorGUIDParameterName,
                                                                      OpenMetadataType.REFERENCEABLE.typeName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);
                }
            }
            else
            {
                anchor2Entity = bean2Entity;
            }
        }


        /*
         * Set up LatestChange classification if there are any anchor entities returned from the initial validation.
         */

        String actionDescription = String.format(actionDescriptionTemplate,
                                                 startingElementTypeName,
                                                 bean1Entity.getGUID(),
                                                 attachingElementTypeName,
                                                 bean2Entity.getGUID());

        if (! anchor1Entity.getGUID().equals(bean1Entity.getGUID()))
        {
            this.addLatestChangeToAnchor(anchor1Entity,
                                         LatestChangeTarget.ATTACHMENT_RELATIONSHIP,
                                         actionOrdinal,
                                         null,
                                         bean2Entity.getGUID(),
                                         attachingElementTypeName,
                                         attachmentTypeName,
                                         userId,
                                         actionDescription,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }
        else
        {
            if (repositoryHelper.isTypeOf(serviceName, startingElementTypeName, OpenMetadataType.REFERENCEABLE.typeName))
            {
                this.addLatestChangeToAnchor(bean1Entity,
                                             LatestChangeTarget.ENTITY_RELATIONSHIP,
                                             actionOrdinal,
                                             null,
                                             bean2Entity.getGUID(),
                                             attachingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }

        if (! anchor2Entity.getGUID().equals(bean2Entity.getGUID()))
        {
            this.addLatestChangeToAnchor(anchor2Entity,
                                         LatestChangeTarget.ATTACHMENT_RELATIONSHIP,
                                         actionOrdinal,
                                         null,
                                         bean1Entity.getGUID(),
                                         startingElementTypeName,
                                         attachmentTypeName,
                                         userId,
                                         actionDescription,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }
        else if (! bean1Entity.getGUID().equals(bean2Entity.getGUID()))
        {
            if (repositoryHelper.isTypeOf(serviceName, attachingElementTypeName, OpenMetadataType.REFERENCEABLE.typeName))
            {
                /*
                 * The attaching element is an anchor in its own right.
                 */
                this.addLatestChangeToAnchor(bean2Entity,
                                             LatestChangeTarget.ENTITY_RELATIONSHIP,
                                             actionOrdinal,
                                             null,
                                             bean1Entity.getGUID(),
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }

        if (newRelationship != null)
        {
            return newRelationship.getGUID();
        }

        return null;
    }


    /**
     * Creates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the link. No check is done for the relationship existence before creating it.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingElementGUID       unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param attachingElementGUID      unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param methodName                calling method
     *
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the relationship to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String uncheckedLinkElementToElement(String             userId,
                                                String             externalSourceGUID,
                                                String             externalSourceName,
                                                String             startingElementGUID,
                                                String             startingGUIDParameterName,
                                                String             attachingElementGUID,
                                                String             attachingGUIDParameterName,
                                                String             attachmentTypeGUID,
                                                InstanceProperties relationshipProperties,
                                                String             methodName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingElementGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachingElementGUID, attachingGUIDParameterName, methodName);

        Relationship newRelationship = repositoryHandler.createRelationship(userId,
                                                                            attachmentTypeGUID,
                                                                            externalSourceGUID,
                                                                            externalSourceName,
                                                                            startingElementGUID,
                                                                            attachingElementGUID,
                                                                            relationshipProperties,
                                                                            methodName);

        if (newRelationship != null)
        {
            return newRelationship.getGUID();
        }

        return null;
    }


    /**
     * Creates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the link.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingElementGUID       unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingElementGUID      unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the relationship to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String multiLinkElementToElement(String             userId,
                                            String             externalSourceGUID,
                                            String             externalSourceName,
                                            String             startingElementGUID,
                                            String             startingGUIDParameterName,
                                            String             startingElementTypeName,
                                            String             attachingElementGUID,
                                            String             attachingGUIDParameterName,
                                            String             attachingElementTypeName,
                                            boolean            forLineage,
                                            boolean            forDuplicateProcessing,
                                            String             attachmentTypeGUID,
                                            String             attachmentTypeName,
                                            InstanceProperties relationshipProperties,
                                            Date               effectiveTime,
                                            String             methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingElementGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachingElementGUID, attachingGUIDParameterName, methodName);

        EntityDetail bean1Entity = repositoryHandler.getEntityByGUID(userId,
                                                                     startingElementGUID,
                                                                     startingGUIDParameterName,
                                                                     startingElementTypeName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

        EntityDetail anchor1Entity = this.validateEntityAndAnchorForRead(userId,
                                                                         startingElementTypeName,
                                                                         bean1Entity,
                                                                         startingGUIDParameterName,
                                                                         true,
                                                                         false,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        EntityDetail bean2Entity = repositoryHandler.getEntityByGUID(userId,
                                                                     attachingElementGUID,
                                                                     attachingGUIDParameterName,
                                                                     attachingElementTypeName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName);

        EntityDetail anchor2Entity = this.validateEntityAndAnchorForRead(userId,
                                                                         attachingElementTypeName,
                                                                         bean2Entity,
                                                                         attachingGUIDParameterName,
                                                                         true,
                                                                         false,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        /*
         * Check that the user is able to create relationships
         */
        if (anchor1Entity != null)
        {
            if (anchor1Entity.getGUID().equals(anchor2Entity.getGUID()))
            {
                securityVerifier.validateUserForAnchorMemberUpdate(userId,
                                                                   anchor1Entity,
                                                                   repositoryHelper,
                                                                   serviceName,
                                                                   methodName);
            }
            else
            {
                securityVerifier.validateUserForAnchorAttach(userId,
                                                             anchor1Entity,
                                                             bean2Entity,
                                                             attachmentTypeName,
                                                             repositoryHelper,
                                                             serviceName,
                                                             methodName);
            }
        }
        else if (anchor2Entity == null)
        {
            securityVerifier.validateUserForElementAttach(userId,
                                                          bean1Entity,
                                                          bean2Entity,
                                                          attachmentTypeName,
                                                          repositoryHelper,
                                                          serviceName,
                                                          methodName);
        }
        else // anchor1 is null but anchor2 is not
        {
            securityVerifier.validateUserForAnchorAttach(userId,
                                                         anchor2Entity,
                                                         bean1Entity,
                                                         attachmentTypeName,
                                                         repositoryHelper,
                                                         serviceName,
                                                         methodName);
        }


        String actionDescriptionTemplate = "Linking %s %s to %s %s";
        LatestChangeAction actionOrdinal = LatestChangeAction.CREATED;

        Relationship newRelationship = repositoryHandler.createRelationship(userId,
                                                                            attachmentTypeGUID,
                                                                            externalSourceGUID,
                                                                            externalSourceName,
                                                                            bean1Entity.getGUID(),
                                                                            bean2Entity.getGUID(),
                                                                            relationshipProperties,
                                                                            methodName);

        /*
         * Final stage is to add the latest change classification to the anchor(s).
         * The act of creating the relationship may set up the anchor GUID in either element.
         */
        if (anchor1Entity == null)
        {
            String startingElementAnchorGUID = this.reEvaluateAnchorGUID(bean1Entity.getGUID(),
                                                                         startingGUIDParameterName,
                                                                         startingElementTypeName,
                                                                         bean1Entity,
                                                                         null,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

            if (startingElementAnchorGUID != null)
            {
                if ((anchor2Entity != null) && (anchor2Entity.getGUID().equals(startingElementAnchorGUID)))
                {
                    anchor1Entity = anchor2Entity; // save a lookup
                }
                else if (startingElementAnchorGUID.equals(bean1Entity.getGUID()))
                {
                    anchor1Entity = bean1Entity;
                }
                else if (startingElementAnchorGUID.equals(bean2Entity.getGUID()))
                {
                    anchor1Entity = bean2Entity;
                }
                else
                {
                    final String anchorGUIDParameterName = "startingElementAnchorGUID";

                    anchor1Entity = repositoryHandler.getEntityByGUID(userId,
                                                                      startingElementAnchorGUID,
                                                                      anchorGUIDParameterName,
                                                                      OpenMetadataType.REFERENCEABLE.typeName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);
                }
            }
            else
            {
                anchor1Entity = bean1Entity;
            }
        }

        if (anchor2Entity == null)
        {
            String attachingElementAnchorGUID = this.reEvaluateAnchorGUID(bean2Entity.getGUID(),
                                                                          attachingGUIDParameterName,
                                                                          attachingElementTypeName,
                                                                          bean2Entity,
                                                                          null,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

            if (attachingElementAnchorGUID != null)
            {
                if (attachingElementAnchorGUID.equals(anchor1Entity.getGUID()))
                {
                    anchor2Entity = anchor1Entity; // save a look-up
                }
                else if (attachingElementAnchorGUID.equals(bean1Entity.getGUID()))
                {
                    anchor2Entity = bean1Entity;
                }
                else if (attachingElementAnchorGUID.equals(bean2Entity.getGUID()))
                {
                    anchor2Entity = bean2Entity;
                }
                else
                {
                    final String anchorGUIDParameterName = "attachingElementAnchorGUID";

                    anchor2Entity = repositoryHandler.getEntityByGUID(userId,
                                                                      attachingElementAnchorGUID,
                                                                      anchorGUIDParameterName,
                                                                      OpenMetadataType.REFERENCEABLE.typeName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);
                }
            }
            else
            {
                anchor2Entity = bean2Entity;
            }
        }


        /*
         * Set up LatestChange classification if there are any anchor entities returned from the initial validation.
         */

        String actionDescription = String.format(actionDescriptionTemplate,
                                                 startingElementTypeName,
                                                 bean1Entity.getGUID(),
                                                 attachingElementTypeName,
                                                 bean2Entity.getGUID());

        if (! anchor1Entity.getGUID().equals(bean1Entity.getGUID()))
        {
            this.addLatestChangeToAnchor(anchor1Entity,
                                         LatestChangeTarget.ATTACHMENT_RELATIONSHIP,
                                         actionOrdinal,
                                         null,
                                         bean2Entity.getGUID(),
                                         attachingElementTypeName,
                                         attachmentTypeName,
                                         userId,
                                         actionDescription,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }
        else
        {
            if (repositoryHelper.isTypeOf(serviceName, startingElementTypeName, OpenMetadataType.REFERENCEABLE.typeName))
            {
                this.addLatestChangeToAnchor(bean1Entity,
                                             LatestChangeTarget.ENTITY_RELATIONSHIP,
                                             actionOrdinal,
                                             null,
                                             bean2Entity.getGUID(),
                                             attachingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }

        if (! anchor2Entity.getGUID().equals(bean2Entity.getGUID()))
        {
            this.addLatestChangeToAnchor(anchor2Entity,
                                         LatestChangeTarget.ATTACHMENT_RELATIONSHIP,
                                         actionOrdinal,
                                         null,
                                         bean1Entity.getGUID(),
                                         startingElementTypeName,
                                         attachmentTypeName,
                                         userId,
                                         actionDescription,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
        }
        else if (! bean1Entity.getGUID().equals(bean2Entity.getGUID()))
        {
            if (repositoryHelper.isTypeOf(serviceName, attachingElementTypeName, OpenMetadataType.REFERENCEABLE.typeName))
            {
                /*
                 * The attaching element is an anchor in its own right.
                 */
                this.addLatestChangeToAnchor(bean2Entity,
                                             LatestChangeTarget.ENTITY_RELATIONSHIP,
                                             actionOrdinal,
                                             null,
                                             bean1Entity.getGUID(),
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }

        if (newRelationship != null)
        {
            return newRelationship.getGUID();
        }

        return null;
    }


    /**
     * Updates a relationship between two elements and updates the LatestChange in each one's anchor entity (if they have one).
     * Both elements must be visible to the user to allow the update.
     *
     * @param userId                    userId of user making request
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachingGUID             unique id of the entity for the element that is being attached
     * @param attachingGUIDParameterName name of the parameter supplying the attachingGUID
     * @param attachingElementTypeName  type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param isMergeUpdate             should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param relationshipProperties    properties to add to the relationship or null if no properties to add
     * @param effectiveTime             the time that the retrieved elements must be effective for
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the relationship to the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateElementToElementLink(String             userId,
                                           String             externalSourceGUID,
                                           String             externalSourceName,
                                           String             startingGUID,
                                           String             startingGUIDParameterName,
                                           String             startingElementTypeName,
                                           String             attachingGUID,
                                           String             attachingGUIDParameterName,
                                           String             attachingElementTypeName,
                                           boolean            forLineage,
                                           boolean            forDuplicateProcessing,
                                           String             attachmentTypeGUID,
                                           String             attachmentTypeName,
                                           boolean            isMergeUpdate,
                                           InstanceProperties relationshipProperties,
                                           Date               effectiveTime,
                                           String             methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachingGUID, attachingGUIDParameterName, methodName);

        EntityDetail startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                               startingGUID,
                                                                               startingGUIDParameterName,
                                                                               startingElementTypeName,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               effectiveTime,
                                                                               methodName);

        EntityDetail startingElementAnchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                                       startingElementTypeName,
                                                                                       startingElementEntity,
                                                                                       startingGUIDParameterName,
                                                                                       true,
                                                                                       false,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       effectiveTime,
                                                                                       methodName);

        String startingElementAnchorGUID = startingGUID;

        if (startingElementAnchorEntity != null)
        {
            startingElementAnchorGUID = startingElementAnchorEntity.getGUID();
        }

        EntityDetail  attachingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                 attachingGUID,
                                                                                 attachingGUIDParameterName,
                                                                                 attachingElementTypeName,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 effectiveTime,
                                                                                 methodName);

        EntityDetail attachingElementAnchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                                        attachingElementTypeName,
                                                                                        attachingElementEntity,
                                                                                        attachingGUIDParameterName,
                                                                                        true,
                                                                                        false,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        methodName);

        /*
         * The calls above validate the existence of the two entities and that they are visible to the user.
         * An exception is thrown if there are any problems.
         * The anchor entities are returned if there are anchor entities associated with a specific end.
         * Next step is to find the relationship to update
         */
        Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId,
                                                                                     startingGUID,
                                                                                     startingElementTypeName,
                                                                                     attachingGUID,
                                                                                     attachmentTypeGUID,
                                                                                     attachmentTypeName,
                                                                                     null,
                                                                                     null,
                                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                                     null,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     effectiveTime,
                                                                                     methodName);

        if (relationship != null)
        {
            InstanceProperties newProperties = setUpNewProperties(isMergeUpdate,
                                                                  relationshipProperties,
                                                                  relationship.getProperties());

            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationship,
                                                           newProperties,
                                                           methodName);


            /*
             * Set up LatestChange classification if there are any anchor entities returned from the initial validation.
             */
            final String actionDescriptionTemplate = "Updating link from %s %s to %s %s";

            String actionDescription = String.format(actionDescriptionTemplate,
                                                     startingElementTypeName,
                                                     startingGUID,
                                                     attachingElementTypeName,
                                                     attachingGUID);

            if (startingElementAnchorEntity != null)
            {
                this.addLatestChangeToAnchor(startingElementAnchorEntity,
                                             LatestChangeTarget.ATTACHMENT_RELATIONSHIP,
                                             LatestChangeAction.UPDATED,
                                             null,
                                             attachingGUID,
                                             attachingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
            else
            {
                if (repositoryHelper.isTypeOf(serviceName, startingElementEntity.getType().getTypeDefName(), OpenMetadataType.REFERENCEABLE.typeName))
                {
                    this.addLatestChangeToAnchor(startingElementEntity,
                                                 LatestChangeTarget.ENTITY_RELATIONSHIP,
                                                 LatestChangeAction.UPDATED,
                                                 null,
                                                 attachingGUID,
                                                 attachingElementTypeName,
                                                 attachmentTypeName,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }

                /*
                 * Now that this relationship is in place, the anchorGUID might be set up
                 */
                this.reEvaluateAnchorGUID(startingGUID,
                                          startingGUIDParameterName,
                                          startingElementTypeName,
                                          startingElementEntity,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
            }

            if (attachingElementAnchorEntity != null)
            {
                /*
                 * Only need to add latestChange if the anchor of the attached element is different
                 */
                if (! attachingElementAnchorEntity.getGUID().equals(startingElementAnchorGUID))
                {
                    this.addLatestChangeToAnchor(attachingElementAnchorEntity,
                                                 LatestChangeTarget.ATTACHMENT_RELATIONSHIP,
                                                 LatestChangeAction.UPDATED,
                                                 null,
                                                 startingGUID,
                                                 startingElementTypeName,
                                                 attachmentTypeName,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }
            }
            else
            {
                if (repositoryHelper.isTypeOf(serviceName, startingElementEntity.getType().getTypeDefName(), OpenMetadataType.REFERENCEABLE.typeName))
                {
                    this.addLatestChangeToAnchor(attachingElementEntity,
                                                 LatestChangeTarget.ENTITY_RELATIONSHIP,
                                                 LatestChangeAction.UPDATED,
                                                 null,
                                                 startingGUID,
                                                 startingElementTypeName,
                                                 attachmentTypeName,
                                                 userId,
                                                 actionDescription,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
                }

                /*
                 * Now that this relationship is in place, the anchorGUID may now be set up
                 */
                this.reEvaluateAnchorGUID(attachingGUID,
                                          attachingGUIDParameterName,
                                          attachingElementTypeName,
                                          attachingElementEntity,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
            }
        }
    }


    /**
     * Removes a relationship between two specified elements.  If after the relationship is deleted, one of the ends has now
     * lost its anchor, then that entity is deleted. Anchored entities should not be left unanchored. This can cause a cascading effect
     * if the anchored elements are organized in a hierarchy, such as a schema or a comment conversation.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachedGUID              unique id of the entity for the element that is being detached
     * @param attachedGUIDParameterName name of the parameter supplying the attachedGUID
     * @param attachedElementTypeGUID   type GUID of the attaching element's entity
     * @param attachedElementTypeName   type name of the attaching element's entity
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param attachmentTypeGUID        unique identifier of type of the relationship to remove
     * @param attachmentTypeName        unique name of type of the relationship to remove
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating relationship in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkElementFromElement(String       userId,
                                         boolean      onlyCreatorPermitted,
                                         String       externalSourceGUID,
                                         String       externalSourceName,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingElementTypeName,
                                         String       attachedGUID,
                                         String       attachedGUIDParameterName,
                                         String       attachedElementTypeGUID,
                                         String       attachedElementTypeName,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         String       attachmentTypeGUID,
                                         String       attachmentTypeName,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachedGUID, attachedGUIDParameterName, methodName);

        Relationship  relationship = repositoryHandler.getRelationshipBetweenEntities(userId,
                                                                                      startingGUID,
                                                                                      startingElementTypeName,
                                                                                      attachedGUID,
                                                                                      attachmentTypeGUID,
                                                                                      attachmentTypeName,
                                                                                      null,
                                                                                      null,
                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                      null,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing,
                                                                                      effectiveTime,
                                                                                      methodName);

        this.unlinkElementFromElement(userId,
                                      onlyCreatorPermitted,
                                      externalSourceGUID,
                                      externalSourceName,
                                      startingGUID,
                                      startingGUIDParameterName,
                                      startingElementTypeName,
                                      attachedGUID,
                                      attachedGUIDParameterName,
                                      attachedElementTypeGUID,
                                      attachedElementTypeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      attachmentTypeName,
                                      relationship,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Removes a relationship between two specified elements.  If after the relationship is deleted, one of the ends has now
     * lost its anchor, then that entity is deleted. Anchored entities should not be left unanchored. This can cause a cascading effect
     * if the anchored elements are organized in a hierarchy, such as a schema or a comment conversation.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param attachedGUID              unique id of the entity for the element that is being detached
     * @param attachedGUIDParameterName name of the parameter supplying the attachedGUID
     * @param attachedElementTypeGUID   type GUID of the attaching element's entity
     * @param attachedElementTypeName   type name of the attaching element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param attachmentTypeName        unique name of type of the relationship to remove
     * @param relationship              specific relationship to remove
     * @param effectiveTime             the time that the retrieved elements must be effective for
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating relationship in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkElementFromElement(String       userId,
                                         boolean      onlyCreatorPermitted,
                                         String       externalSourceGUID,
                                         String       externalSourceName,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       startingElementTypeName,
                                         String       attachedGUID,
                                         String       attachedGUIDParameterName,
                                         String       attachedElementTypeGUID,
                                         String       attachedElementTypeName,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         String       attachmentTypeName,
                                         Relationship relationship,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String relationshipParameterName = "relationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(attachedGUID, attachedGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(relationship, relationshipParameterName, methodName);

        this.validateRelationshipChange(userId,
                                        relationship,
                                        true,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        EntityDetail startingElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                               startingGUID,
                                                                               startingGUIDParameterName,
                                                                               startingElementTypeName,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               effectiveTime,
                                                                               methodName);

        EntityDetail startingElementAnchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                                       startingElementTypeName,
                                                                                       startingElementEntity,
                                                                                       startingGUIDParameterName,
                                                                                       true,
                                                                                       false,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       effectiveTime,
                                                                                       methodName);

        String startingElementAnchorGUID = startingGUID;

        String newStartingAnchorGUID = null;
        String newAttachedAnchorGUID = null;


        if (startingElementAnchorEntity != null)
        {
            startingElementAnchorGUID = startingElementAnchorEntity.getGUID();
        }

        EntityDetail attachedElementEntity = repositoryHandler.getEntityByGUID(userId,
                                                                               attachedGUID,
                                                                               attachedGUIDParameterName,
                                                                               attachedElementTypeName,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               effectiveTime,
                                                                               methodName);

        EntityDetail attachedElementAnchorEntity = this.validateEntityAndAnchorForRead(userId,
                                                                                       attachedElementTypeName,
                                                                                       attachedElementEntity,
                                                                                       attachedGUIDParameterName,
                                                                                       true,
                                                                                       false,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       effectiveTime,
                                                                                       methodName);

        if ((!onlyCreatorPermitted) || (userId.equals(relationship.getCreatedBy())))
        {
            repositoryHandler.removeRelationship(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 relationship,
                                                 methodName);
        }
        else
        {
            throw new UserNotAuthorizedException(GenericHandlersErrorCode.ONLY_CREATOR_CAN_DELETE.getMessageDefinition(methodName,
                                                                                                                       startingElementTypeName,
                                                                                                                       startingGUID,
                                                                                                                       attachedElementTypeName,
                                                                                                                       attachedGUID,
                                                                                                                       userId),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        /*
         * Set up LatestChange classification if there are any anchor entities returned from the initial validation.
         */
        final String actionDescriptionTemplate = "Unlinking %s %s from %s %s";

        String actionDescription = String.format(actionDescriptionTemplate,
                                                 startingElementTypeName,
                                                 startingGUID,
                                                 attachedElementTypeName,
                                                 attachedGUID);


        if ((startingElementAnchorEntity != null) && (! startingElementEntity.getGUID().equals(startingElementAnchorEntity.getGUID())))
        {
            this.addLatestChangeToAnchor(startingElementAnchorEntity,
                                         LatestChangeTarget.ATTACHMENT_RELATIONSHIP,
                                         LatestChangeAction.DELETED,
                                         null,
                                         attachedGUID,
                                         attachedElementTypeName,
                                         attachmentTypeName,
                                         userId,
                                         actionDescription,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);

            /*
             * Now that this relationship is gone, the anchorGUID may now be wrong
             */
            newStartingAnchorGUID = this.reEvaluateAnchorGUID(startingGUID,
                                                              startingGUIDParameterName,
                                                              startingElementTypeName,
                                                              startingElementAnchorEntity.getGUID(),
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime,
                                                              methodName);
        }
        else
        {
            if (repositoryHelper.isTypeOf(serviceName, attachedElementEntity.getType().getTypeDefName(), OpenMetadataType.REFERENCEABLE.typeName))
            {
                this.addLatestChangeToAnchor(startingElementEntity,
                                             LatestChangeTarget.ENTITY_RELATIONSHIP,
                                             LatestChangeAction.DELETED,
                                             null,
                                             attachedGUID,
                                             attachedElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }

        if ((attachedElementAnchorEntity != null) && (! attachedElementAnchorEntity.getGUID().equals(attachedElementEntity.getGUID())))
        {
            /*
             * Only need to add latestChange if the anchor of the attached element is different
             */
            if (! attachedElementAnchorEntity.getGUID().equals(startingElementAnchorGUID))
            {
                this.addLatestChangeToAnchor(attachedElementAnchorEntity,
                                             LatestChangeTarget.ATTACHMENT_RELATIONSHIP,
                                             LatestChangeAction.DELETED,
                                             null,
                                             startingGUID,
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }

            /*
             * Now that this relationship is gone, the anchorGUID may now be wrong
             */
            newAttachedAnchorGUID = this.reEvaluateAnchorGUID(attachedGUID,
                                                              attachedGUIDParameterName,
                                                              attachedElementTypeName,
                                                              attachedElementAnchorEntity.getGUID(),
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime,
                                                              methodName);
        }
        else
        {
            if (repositoryHelper.isTypeOf(serviceName, attachedElementEntity.getType().getTypeDefName(), OpenMetadataType.REFERENCEABLE.typeName))
            {
                this.addLatestChangeToAnchor(startingElementEntity,
                                             LatestChangeTarget.ENTITY_RELATIONSHIP,
                                             LatestChangeAction.DELETED,
                                             null,
                                             startingGUID,
                                             startingElementTypeName,
                                             attachmentTypeName,
                                             userId,
                                             actionDescription,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
            }
        }


        /*
         * If the attached element had an anchor before the relationship deletion, but now is without an anchor, then delete the bean.
         */
        if (attachedElementAnchorEntity != null && newAttachedAnchorGUID == null)
        {
            try
            {
                this.deleteAnchoredBeanInRepository(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    attachedGUID,
                                                    attachedGUIDParameterName,
                                                    attachedElementTypeGUID,
                                                    attachedElementTypeName,
                                                    false,
                                                    null,
                                                    null,
                                                    attachedElementAnchorEntity.getGUID(),
                                                    new ArrayList<>(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
            }
            catch (InvalidParameterException | PropertyServerException| UserNotAuthorizedException error)
            {
                // This method should succeed, because the relationship has been deleted. Issue an audit log indicating that the bean delete failed
                auditLog.logException(methodName,
                                      GenericHandlersAuditCode.UNABLE_TO_DELETE_UNANCHORED_BEAN.getMessageDefinition(serviceName,
                                                                                                                     attachedGUID,
                                                                                                                     attachedElementTypeName,
                                                                                                                     attachedElementTypeGUID,
                                                                                                                     methodName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     error.getMessage()),
                                      error);

            }
        }

        /*
         * If the starting element had an anchor before the relationship deletion, but now is without an anchor, then delete the bean.
         */
        if (startingElementAnchorEntity != null && newStartingAnchorGUID == null)
        {
            final String startingElementTypeGUID = repositoryHelper.getTypeDefByName(methodName, startingElementTypeName).getGUID();

            try
            {
                this.deleteAnchoredBeanInRepository(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    startingGUID,
                                                    startingGUIDParameterName,
                                                    startingElementTypeGUID,
                                                    startingElementTypeName,
                                                    false,
                                                    null,
                                                    null,
                                                    startingElementAnchorEntity.getGUID(),
                                                    new ArrayList<>(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
            }
            catch (InvalidParameterException | PropertyServerException| UserNotAuthorizedException error)
            {
                // This method should succeed, because the relationship has been deleted. Issue an audit log indicating that the bean delete failed
                auditLog.logException(methodName,
                                      GenericHandlersAuditCode.UNABLE_TO_DELETE_UNANCHORED_BEAN.getMessageDefinition(serviceName,
                                                                                                                     startingGUID,
                                                                                                                     startingElementTypeName,
                                                                                                                     startingElementTypeGUID,
                                                                                                                     methodName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Calls unlinkElementFromElement for all relationships of a certain type emanating from the requested element.
     *
     * @param userId                    userId of user making request
     * @param onlyCreatorPermitted      operation only permitted if the userId was the same one that created the relationship
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param startingGUID              unique id for the starting element's entity
     * @param startingGUIDParameterName name of the parameter supplying the startingGUID
     * @param startingElementTypeName   type name of the starting element's entity
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param attachmentTypeGUID        unique identifier of type of the relationship to create
     * @param attachmentTypeName        unique name of type of the relationship to create
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName                calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the relationships in the repositories.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void unlinkAllElements(String       userId,
                                  boolean      onlyCreatorPermitted,
                                  String       externalSourceGUID,
                                  String       externalSourceName,
                                  String       startingGUID,
                                  String       startingGUIDParameterName,
                                  String       startingElementTypeName,
                                  boolean      forLineage,
                                  boolean      forDuplicateProcessing,
                                  String       attachmentTypeGUID,
                                  String       attachmentTypeName,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String entityProxyParameterName = "entityProxy.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        startingGUID,
                                                                        startingGUIDParameterName,
                                                                        startingElementTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       startingEntity,
                                                                                       startingElementTypeName,
                                                                                       attachmentTypeGUID,
                                                                                       attachmentTypeName,
                                                                                       2,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();
            EntityProxy  entityProxy  = repositoryHandler.getOtherEnd(startingEntity.getGUID(), startingElementTypeName, relationship, 2, methodName);

            if ((entityProxy != null) && (entityProxy.getType() != null))
            {
                this.unlinkElementFromElement(userId,
                                              onlyCreatorPermitted,
                                              externalSourceGUID,
                                              externalSourceName,
                                              startingGUID,
                                              startingGUIDParameterName,
                                              startingElementTypeName,
                                              entityProxy.getGUID(),
                                              entityProxyParameterName,
                                              entityProxy.getType().getTypeDefGUID(),
                                              entityProxy.getType().getTypeDefName(),
                                              forLineage,
                                              forDuplicateProcessing,
                                              attachmentTypeGUID,
                                              attachmentTypeName,
                                              effectiveTime,
                                              methodName);
            }
        }
    }


    /**
     * Set up the effectivity times for an element not using a builder - typically a relationship.
     *
     * @param currentProperties other property values for the element
     * @param effectiveFrom supplied starting time for this element (null for all time)
     * @param effectiveTo supplied ending time for this element
     * @return correctly filled out properties
     */
    InstanceProperties setUpEffectiveDates(InstanceProperties currentProperties,
                                           Date               effectiveFrom,
                                           Date               effectiveTo)
    {
        InstanceProperties properties = currentProperties;

        if ((effectiveFrom != null) || (effectiveTo != null))
        {
            if (properties == null)
            {
                properties = new InstanceProperties();
            }

            properties.setEffectiveFromTime(effectiveFrom);
            properties.setEffectiveToTime(effectiveTo);
        }

        return properties;
    }
}
