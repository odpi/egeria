/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSDynamicTypeMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityConflictException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.HomeEntityException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.HomeRelationshipException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidEntityException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidRelationshipException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidTypeDefException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipConflictException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefConflictException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * The GraphOMRSMetadataCollection provides a local open metadata repository that uses a graph store as its
 * persistence layer.
 */
public class GraphOMRSMetadataCollection extends OMRSDynamicTypeMetadataCollectionBase
{

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSMetadataCollection.class);

    private GraphOMRSMetadataStore graphStore = null;

    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection Id.
     *
     * @param parentConnector      - connector that this metadata collection supports.  The connector has the information
     *                             to call the metadata repository.
     * @param repositoryName       - name of the repository - used for logging.
     * @param repositoryHelper     - class used to build type definitions and instances.
     * @param repositoryValidator  - class used to validate type definitions and instances.
     * @param metadataCollectionId - unique Identifier of the metadata collection Id.
     * @param auditLog             - logging destination
     */
    GraphOMRSMetadataCollection(GraphOMRSRepositoryConnector parentConnector,
                                String                       repositoryName,
                                OMRSRepositoryHelper         repositoryHelper,
                                OMRSRepositoryValidator      repositoryValidator,
                                String                       metadataCollectionId,
                                AuditLog                     auditLog,
                                Map<String, Object>          storageProperties)
    {
        /*
         * The metadata collection Id is the unique Id for the metadata collection.  It is managed by the super class.
         */
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);

        final String methodName = "GraphOMRSMetadataCollection";
        /*
         * Save parentConnector since this has the connection information and access to the metadata about the
         * metadata cluster.
         */
        this.parentConnector = parentConnector;

        try {
            this.graphStore = new GraphOMRSMetadataStore(metadataCollectionId, repositoryName, repositoryHelper, auditLog,
                    storageProperties);
        }
        catch(RepositoryErrorException e) {
            log.error("{} could not create graph metadata collection for repository name {}", methodName, repositoryName);
            // Little point throwing the exception any higher here - the error has been logged at all levels;
        }
    }


    // verifyTypeDef will always return result from superclass because all knowledge of types is delegated to the RCM.
    public boolean verifyTypeDef(String  userId,
                                 TypeDef typeDef)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefConflictException,
            InvalidTypeDefException,
            UserNotAuthorizedException
    {
        /*
         * Validate parameters
         */
        boolean result = super.verifyTypeDef(userId, typeDef);

        TypeDefCategory typeDefCategory = typeDef.getCategory();
        switch (typeDefCategory) {
            case ENTITY_DEF:
                // Create indexes for entity primitive properties
                graphStore.createEntityIndexes(typeDef);
                break;
            case RELATIONSHIP_DEF:
                // Create indexes for relationship primitive properties
                graphStore.createRelationshipIndexes(typeDef);
                break;
            case CLASSIFICATION_DEF:
                // Create indexes for classification primitive properties
                graphStore.createClassificationIndexes(typeDef);
                break;
        }

        return result;
    }


    public EntityDetail addEntity(String                userId,
                                  String                entityTypeGUID,
                                  InstanceProperties    initialProperties,
                                  List<Classification>  initialClassifications,
                                  InstanceStatus        initialStatus)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            ClassificationErrorException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {

        final String methodName = "addEntity";

        /*
         * Validate parameters
         */
        TypeDef typeDef = super.addEntityParameterValidation(userId,
                                                             entityTypeGUID,
                                                             initialProperties,
                                                             initialClassifications,
                                                             initialStatus,
                                                             methodName);

        /*
         * Validation complete - ok to create new instance
         */
        EntityDetail newEntity = repositoryHelper.getNewEntity(repositoryName,
                metadataCollectionId,
                InstanceProvenanceType.LOCAL_COHORT,
                userId,
                typeDef.getName(),
                initialProperties,
                initialClassifications);

        /*
         * Ensure metadataCollectionName is also set
         */
        newEntity.setMetadataCollectionName(metadataCollectionName);

        /*
         * If an initial status is supplied then override the default value.
         */
        if (initialStatus != null) {
            newEntity.setStatus(initialStatus);
        }

        newEntity = graphStore.createEntityInStore(newEntity);

        return newEntity;
    }


    // addExternalEntity
    public EntityDetail addExternalEntity(String                userId,
                                          String                entityTypeGUID,
                                          String                externalSourceGUID,
                                          String                externalSourceName,
                                          InstanceProperties    initialProperties,
                                          List<Classification>  initialClassifications,
                                          InstanceStatus        initialStatus) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException,
                                                                                      ClassificationErrorException,
                                                                                      StatusNotSupportedException,
                                                                                      UserNotAuthorizedException
    {
        final String  methodName = "addExternalEntity";

        TypeDef typeDef = super.addExternalEntityParameterValidation(userId,
                                                                     entityTypeGUID,
                                                                     externalSourceGUID,
                                                                     initialProperties,
                                                                     initialClassifications,
                                                                     initialStatus,
                                                                     methodName);

        /*
         * Validation complete - ok to create new instance
         */
        EntityDetail newEntity = repositoryHelper.getNewEntity(repositoryName,
                                                               externalSourceGUID,
                                                               InstanceProvenanceType.EXTERNAL_SOURCE,
                                                               userId,
                                                               typeDef.getName(),
                                                               initialProperties,
                                                               initialClassifications);

        /*
         * Ensure metadataCollectionName also set
         */
        newEntity.setMetadataCollectionName(externalSourceName);
        newEntity.setReplicatedBy(metadataCollectionId);


        /*
         * If an initial status is supplied then override the default value.
         */
        if (initialStatus != null) {
            newEntity.setStatus(initialStatus);
        }


        newEntity = graphStore.createEntityInStore(newEntity);

        return newEntity;
    }


    // addEntityProxy
    public void addEntityProxy(String       userId,
                               EntityProxy  entityProxy)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        /*
         * Validate parameters
         */
        super.addEntityProxyParameterValidation(userId, entityProxy);

        /*
         * Validation complete
         */
        EntityDetail  entity  = this.isEntityKnown(userId, entityProxy.getGUID());
        if (entity == null)
        {
            graphStore.createEntityProxyInStore(entityProxy);
        }
    }



    // isEntityKnown
    public EntityDetail isEntityKnown(String     userId,
                                      String     guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "isEntityKnown";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */

        EntityDetail entity;
        try {
            entity = graphStore.getEntityDetailFromStore(guid);
            repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
        }
        catch (EntityProxyOnlyException | EntityNotKnownException e) {
            log.error("{} entity with GUID {} does not exist in repository {} or is a proxy", methodName, guid, repositoryName);
            entity = null;
        }

        return entity;
    }

    // isRelationshipKnown
    public Relationship  isRelationshipKnown(String     userId,
                                             String     guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "isRelationshipKnown";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Process operation
         */
        Relationship relationship;
        try {
            relationship = graphStore.getRelationshipFromStore(guid);
            repositoryValidator.validateRelationshipFromStore(repositoryName, guid, relationship, methodName);
        }
        catch (RelationshipNotKnownException e) {
            log.error("{} relationship with GUID {} does not exist in repository {}", methodName, guid, repositoryName);
            relationship = null;
        }
        return relationship;

    }

    // getEntitySummary
    public EntitySummary getEntitySummary(String     userId,
                                          String     guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName        = "getEntitySummary";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */

        EntitySummary entity = graphStore.getEntitySummaryFromStore(guid);

        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        return entity;
    }

    // getEntityDetail
    public EntityDetail getEntityDetail(String     userId,
                                        String     guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityProxyOnlyException,
            UserNotAuthorizedException
    {
        final String methodName = "getEntityDetail";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */

        EntityDetail entity = graphStore.getEntityDetailFromStore(guid);

        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        return entity;
    }


    // addRelationship
    public Relationship addRelationship(String               userId,
                                        String               relationshipTypeGUID,
                                        InstanceProperties   initialProperties,
                                        String               entityOneGUID,
                                        String               entityTwoGUID,
                                        InstanceStatus       initialStatus)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            EntityNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "addRelationship";

        /*
         * Validate parameters
         */
        TypeDef typeDef = super.addRelationshipParameterValidation(userId,
                                                                   relationshipTypeGUID,
                                                                   initialProperties,
                                                                   entityOneGUID,
                                                                   entityTwoGUID,
                                                                   initialStatus,
                                                                   methodName);


        /*
         * Validation complete - ok to create new instance
         */
        Relationship relationship = repositoryHelper.getNewRelationship(repositoryName,
                metadataCollectionId,
                InstanceProvenanceType.LOCAL_COHORT,
                userId,
                typeDef.getName(),
                initialProperties);

        /*
         * Retrieve a proxy for entity 1
         */
        EntityProxy entityOneProxy = graphStore.getEntityProxyFromStore(entityOneGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityOneGUID, entityOneProxy, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entityOneProxy, methodName);

        /*
         * Retrieve a proxy for entity 2
         */
        EntityProxy entityTwoProxy = graphStore.getEntityProxyFromStore(entityTwoGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityTwoGUID, entityTwoProxy, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entityTwoProxy, methodName);


        repositoryValidator.validateRelationshipEnds(repositoryName, entityOneProxy, entityTwoProxy, typeDef, methodName);

        relationship.setEntityOneProxy(entityOneProxy);
        relationship.setEntityTwoProxy(entityTwoProxy);

        /*
         * If an initial status is supplied then override the default value.
         */
        if (initialStatus != null)
        {
            relationship.setStatus(initialStatus);
        }

        graphStore.createRelationshipInStore(relationship);

        return relationship;
    }


    // addExternalRelationship
    public Relationship addExternalRelationship(String               userId,
                                                String               relationshipTypeGUID,
                                                String               externalSourceGUID,
                                                String               externalSourceName,
                                                InstanceProperties   initialProperties,
                                                String               entityOneGUID,
                                                String               entityTwoGUID,
                                                InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           TypeErrorException,
                                                                                           PropertyErrorException,
                                                                                           EntityNotKnownException,
                                                                                           StatusNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "addExternalRelationship";

        /*
         * Validate parameters
         */
        TypeDef typeDef = super.addRelationshipParameterValidation(userId,
                                                                   relationshipTypeGUID,
                                                                   initialProperties,
                                                                   entityOneGUID,
                                                                   entityTwoGUID,
                                                                   initialStatus,
                                                                   methodName);


        /*
         * Validation complete - ok to create new instance
         */
        Relationship relationship = repositoryHelper.getNewRelationship(repositoryName,
                                                                        externalSourceGUID,
                                                                        InstanceProvenanceType.EXTERNAL_SOURCE,
                                                                        userId,
                                                                        typeDef.getName(),
                                                                        initialProperties);

        relationship.setMetadataCollectionName(externalSourceName);
        relationship.setReplicatedBy(metadataCollectionId);


        /*
         * Retrieve a proxy for entity 1
         */
        EntityProxy entityOneProxy = graphStore.getEntityProxyFromStore(entityOneGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityOneGUID, entityOneProxy, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entityOneProxy, methodName);

        /*
         * Retrieve a proxy for entity 2
         */
        EntityProxy entityTwoProxy = graphStore.getEntityProxyFromStore(entityTwoGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityTwoGUID, entityTwoProxy, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entityTwoProxy, methodName);


        repositoryValidator.validateRelationshipEnds(repositoryName, entityOneProxy, entityTwoProxy, typeDef, methodName);

        relationship.setEntityOneProxy(entityOneProxy);
        relationship.setEntityTwoProxy(entityTwoProxy);

        /*
         * If an initial status is supplied then override the default value.
         */
        if (initialStatus != null)
        {
            relationship.setStatus(initialStatus);
        }

        graphStore.createRelationshipInStore(relationship);

        return relationship;
    }


    // getRelationship
    public Relationship getRelationship(String    userId,
                                        String    guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName = "getRelationship";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Process operation
         */
        Relationship  relationship = graphStore.getRelationshipFromStore(guid);

        repositoryValidator.validateRelationshipFromStore(repositoryName, guid, relationship, methodName);
        repositoryValidator.validateRelationshipIsNotDeleted(repositoryName, relationship, methodName);

        return relationship;
    }


    // updateEntityStatus
    public EntityDetail updateEntityStatus(String           userId,
                                           String           entityGUID,
                                           InstanceStatus   newStatus)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName               = "updateEntityStatus";
        final String  statusParameterName      = "newStatus";

        /*
         * Validate parameters
         */
        this.updateInstanceStatusParameterValidation(userId, entityGUID, newStatus, methodName);

        /*
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityDetailFromStore(entityGUID);

        }
        catch (EntityProxyOnlyException e) {
            log.error("{} entity wth GUID {} only a proxy", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(methodName,
                                                                                                   this.getClass().getName(),
                                                                                                   repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);

        }
        catch (EntityNotKnownException e) {
            log.error("{} entity wth GUID {} not found ", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, entityGUID);
            throw e;
        }


        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateEntityCanBeUpdated(repositoryName, metadataCollectionId, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        String entityTypeGUID = entity.getType().getTypeDefGUID();
        String entityTypeName = entity.getType().getTypeDefName();

        TypeDef typeDef;
        try {
            typeDef = repositoryHelper.getTypeDef(repositoryName, "entityTypeGUID", entityTypeGUID, methodName);
        }
        catch (TypeErrorException e) {

            throw new RepositoryErrorException(OMRSErrorCode.TYPEDEF_NOT_KNOWN.getMessageDefinition(entityTypeName,
                                                                                                    entityTypeGUID,
                                                                                                    "entityType",
                                                                                                    methodName,
                                                                                                    this.getClass().getName(),
                                                                                                    repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }

        repositoryValidator.validateNewStatus(repositoryName, statusParameterName, newStatus, typeDef, methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setStatus(newStatus);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);


        return updatedEntity;
    }

    // updateEntityProperties
    public EntityDetail updateEntityProperties(String               userId,
                                               String               entityGUID,
                                               InstanceProperties   properties)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "updateEntityProperties";
        final String  propertiesParameterName  = "properties";

        /*
         * Validate parameters
         */
        this.updateInstancePropertiesPropertyValidation(userId, entityGUID, properties, methodName);

        /*
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityDetailFromStore(entityGUID);

        }
        catch (EntityProxyOnlyException | EntityNotKnownException e) {
            log.error("{} entity wth GUID {} not found or only a proxy", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(methodName,
                                                                                                   this.getClass().getName(),
                                                                                                   repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, entityGUID);
            throw e;
        }


        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);
        repositoryValidator.validateEntityCanBeUpdated(repositoryName, metadataCollectionId, entity, methodName);
        repositoryValidator.validateInstanceType(repositoryName, entity);

        String entityTypeGUID = entity.getType().getTypeDefGUID();
        String entityTypeName = entity.getType().getTypeDefName();

        TypeDef typeDef;
        try {
            typeDef = repositoryHelper.getTypeDef(repositoryName, "entityTypeGUID", entityTypeGUID, methodName);
        }
        catch (TypeErrorException e) {

            throw new RepositoryErrorException(OMRSErrorCode.TYPEDEF_NOT_KNOWN.getMessageDefinition(entityTypeName,
                                                                                                    entityTypeGUID,
                                                                                                    "entityType",
                                                                                                    methodName,
                                                                                                    repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }

        repositoryValidator.validateNewPropertiesForType(repositoryName,
                propertiesParameterName,
                typeDef,
                properties,
                methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setProperties(properties);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);

        ///*
        // * The repository store maintains an entity proxy for use with relationships.
        // */
        //EntityProxy entityProxy = repositoryHelper.getNewEntityProxy(repositoryName, updatedEntity);
        //
        //graphStore.updateEntityProxyInStore(entityProxy);

        return updatedEntity;
    }


    // updateRelationshipStatus
    public Relationship updateRelationshipStatus(String           userId,
                                                 String           relationshipGUID,
                                                 InstanceStatus   newStatus)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName          = "updateRelationshipStatus";
        final String  statusParameterName = "newStatus";

        /*
         * Validate parameters
         */
        this.updateInstanceStatusParameterValidation(userId, relationshipGUID, newStatus, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateRelationshipCanBeUpdated(repositoryName, metadataCollectionId, relationship, methodName);

        repositoryValidator.validateInstanceType(repositoryName, relationship);

        String relationshipTypeGUID = relationship.getType().getTypeDefGUID();
        String relationshipTypeName = relationship.getType().getTypeDefName();

        TypeDef typeDef;
        try {
            typeDef = repositoryHelper.getTypeDef(repositoryName, "relationshipTypeGUID", relationshipTypeGUID, methodName);
        }
        catch (TypeErrorException e) {

            throw new RepositoryErrorException(OMRSErrorCode.TYPEDEF_NOT_KNOWN.getMessageDefinition(relationshipTypeName,
                                                                                                    relationshipTypeGUID,
                                                                                                    "relationshipType",
                                                                                                    methodName,
                                                                                                    repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }


        repositoryValidator.validateNewStatus(repositoryName,
                statusParameterName,
                newStatus,
                typeDef,
                methodName);

        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setStatus(newStatus);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    /**
     * Update the properties of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param properties list of the properties to update.
     * @return Resulting relationship structure with the new properties set.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this relationship's type.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public Relationship updateRelationshipProperties(String               userId,
                                                     String               relationshipGUID,
                                                     InstanceProperties   properties)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "updateRelationshipProperties";
        final String  propertiesParameterName = "properties";

        /*
         * Validate parameters
         */
        this.updateInstancePropertiesPropertyValidation(userId, relationshipGUID, properties, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateRelationshipCanBeUpdated(repositoryName, metadataCollectionId, relationship, methodName);

        repositoryValidator.validateInstanceType(repositoryName, relationship);

        String relationshipTypeGUID = relationship.getType().getTypeDefGUID();
        String relationshipTypeName = relationship.getType().getTypeDefName();

        TypeDef typeDef;
        try {
            typeDef = repositoryHelper.getTypeDef(repositoryName, "relationshipTypeGUID", relationshipTypeGUID, methodName);
        }
        catch (TypeErrorException e) {

            throw new RepositoryErrorException(OMRSErrorCode.TYPEDEF_NOT_KNOWN.getMessageDefinition(relationshipTypeName,
                                                                                                    relationshipTypeGUID,
                                                                                                    "relationshipType",
                                                                                                    methodName,
                                                                                                    repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }

        repositoryValidator.validateNewPropertiesForType(repositoryName,
                propertiesParameterName,
                typeDef,
                properties,
                methodName);


        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setProperties(properties);
        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    // purgeEntity
    public void purgeEntity(String    userId,
                            String    typeDefGUID,
                            String    typeDefName,
                            String    deletedEntityGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityNotDeletedException,
            UserNotAuthorizedException
    {
        final String  methodName  = "purgeEntity";
        final String  parameterName  = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, deletedEntityGUID, parameterName, methodName);

        /*
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityDetailFromStore(deletedEntityGUID);

        }
        catch (EntityProxyOnlyException | EntityNotKnownException e) {
            log.error("{} entity wth GUID {} not found or only a proxy", methodName, deletedEntityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(methodName,
                                                                                                   this.getClass().getName(),
                                                                                                   repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, deletedEntityGUID);
            throw e;
        }

        repositoryValidator.validateEntityFromStore(repositoryName, deletedEntityGUID, entity, methodName);

        repositoryValidator.validateTypeForInstanceDelete(repositoryName,
                typeDefGUID,
                typeDefName,
                entity,
                methodName);

        repositoryValidator.validateEntityIsDeleted(repositoryName, entity, methodName);


        /*
         * Locate/purge relationships for entity
         */
        try
        {
            List<Relationship> relationships = this.getRelationshipsForEntity(userId,
                    deletedEntityGUID,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    10000);


            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        graphStore.removeRelationshipFromStore(relationship.getGUID());
                    }
                }
            }
        }
        catch (Throwable  error)
        {
            // nothing to do - keep going
        }

        /*
         * Validation is complete - ok to remove the entity
         */
        graphStore.removeEntityFromStore(entity.getGUID());

    }

    // purgeRelationship
    public void purgeRelationship(String    userId,
                                  String    typeDefGUID,
                                  String    typeDefName,
                                  String    deletedRelationshipGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            RelationshipNotDeletedException,
            UserNotAuthorizedException
    {
        final String  methodName = "purgeRelationship";
        final String  parameterName  = "deletedRelationshipGUID";


        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, deletedRelationshipGUID, parameterName, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = graphStore.getRelationshipFromStore(deletedRelationshipGUID);

        repositoryValidator.validateRelationshipFromStore(repositoryName, deletedRelationshipGUID, relationship, methodName);
        repositoryValidator.validateTypeForInstanceDelete(repositoryName,
                typeDefGUID,
                typeDefName,
                relationship,
                methodName);

        repositoryValidator.validateRelationshipIsDeleted(repositoryName, relationship, methodName);


        /*
         * Validation is complete - ok to remove the relationship
         */

        graphStore.removeRelationshipFromStore(relationship.getGUID());
    }


    // getRelationshipsForEntity
    public List<Relationship> getRelationshipsForEntity(String                     userId,
                                                        String                     entityGUID,
                                                        String                     relationshipTypeGUID,
                                                        int                        fromRelationshipElement,
                                                        List<InstanceStatus>       limitResultsByStatus,
                                                        Date                       asOfTime,
                                                        String                     sequencingProperty,
                                                        SequencingOrder            sequencingOrder,
                                                        int                        pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            PagingErrorException,
            UserNotAuthorizedException,
            FunctionNotSupportedException
    {
        final String  methodName = "getRelationshipsForEntity";

        /*
         * Validate parameters
         */
        super.getRelationshipsForEntityParameterValidation(userId,
                entityGUID,
                relationshipTypeGUID,
                fromRelationshipElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);

        /*
         * Perform operation
         */
        EntitySummary  entity = this.getEntitySummary(userId, entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        List<Relationship> entityRelationships = new ArrayList<>();

        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }

        List<Relationship> filteredRelationships = new ArrayList<>();
        List<Relationship> relationships = graphStore.getRelationshipsForEntity(entityGUID);

        for (Relationship  relationship : relationships) {

            if (relationship != null) {

                if (relationship.getStatus() != InstanceStatus.DELETED) {
                    // exclude DELETED relationships

                    // filter results according to status filter parameter
                    if (  limitResultsByStatus == null
                          || (   limitResultsByStatus != null
                              && !limitResultsByStatus.isEmpty()
                              && limitResultsByStatus.contains(relationship.getStatus()))) {

                        // filter by typeGUID if necessary
                        if (relationshipTypeGUID == null  || relationshipTypeGUID.equals(relationship.getType().getTypeDefGUID())) {
                            filteredRelationships.add(relationship);
                        }
                    }
                }
            }
        }

        if (filteredRelationships.isEmpty())
        {
            return null;
        }

        return repositoryHelper.formatRelationshipResults(filteredRelationships,
                fromRelationshipElement,
                sequencingProperty,
                sequencingOrder,
                pageSize);
    }


    // findEntitiesByProperty
    public List<EntityDetail> findEntitiesByProperty(String                 userId,
                                                     String                 entityTypeGUID,
                                                     InstanceProperties     matchProperties,
                                                     MatchCriteria          matchCriteria,
                                                     int                    fromEntityElement,
                                                     List<InstanceStatus>   limitResultsByStatus,
                                                     List<String>           limitResultsByClassification,
                                                     Date                   asOfTime,
                                                     String                 sequencingProperty,
                                                     SequencingOrder        sequencingOrder,
                                                     int                    pageSize)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {

        final String methodName = "findEntitiesByProperty";
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        /*
         * Validate parameters
         */
        super.findEntitiesByPropertyParameterValidation(userId,
                entityTypeGUID,
                matchProperties,
                matchCriteria,
                fromEntityElement,
                limitResultsByStatus,
                limitResultsByClassification,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }


        /*
         * Perform operation
         */

        ArrayList<EntityDetail> returnEntities = null;


        String specifiedTypeName = null;
        if (entityTypeGUID != null) {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, entityTypeGUIDParameterName, entityTypeGUID, methodName);
            specifiedTypeName = typeDef.getName();
        }

        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

        for (TypeDef typeDef : allTypeDefs) {
            if (typeDef.getCategory() == TypeDefCategory.ENTITY_DEF) {

                String actualTypeName = typeDef.getName();

                // If entityTypeGUID parameter is not null there is an expected type, so check whether the
                // current type matches the expected type or is one of its sub-types.

                if (specifiedTypeName != null) {

                    boolean typeMatch = repositoryHelper.isTypeOf(metadataCollectionId, actualTypeName, specifiedTypeName);
                    if (!typeMatch) {
                        continue;
                    }

                }

                // Invoke a type specific search. The search will expect the regexp to match fully to the value.
                List<EntityDetail> entitiesForCurrentType = graphStore.findEntitiesByProperty(actualTypeName, matchProperties, matchCriteria, true);

                if (entitiesForCurrentType != null && !entitiesForCurrentType.isEmpty()) {
                    if (returnEntities == null) {
                        returnEntities = new ArrayList<>();
                    }
                    log.info("{}: for type {} found {} entities", methodName, typeDef.getName(), entitiesForCurrentType.size());
                    returnEntities.addAll(entitiesForCurrentType);
                } else {
                    log.info("{}: for type {} found no entities", methodName, typeDef.getName());
                }

            }
        }

        // Eliminate soft deleted entities and apply status and classification filtering if any was requested
        if (returnEntities == null) {
            return null;
        }
        else {
            List<EntityDetail> retainedEntities = new ArrayList<>();
            for (EntityDetail entity : returnEntities) {
                if (entity != null) {
                    if ((entity.getStatus() != InstanceStatus.DELETED)
                            && (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity))
                            && (repositoryValidator.verifyEntityIsClassified(limitResultsByClassification, entity))) {

                        retainedEntities.add(entity);
                    }
                }
            }

            return repositoryHelper.formatEntityResults(retainedEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }
    }


    // findRelationshipsByProperty
    public  List<Relationship> findRelationshipsByProperty(String                    userId,
                                                           String                    relationshipTypeGUID,
                                                           InstanceProperties        matchProperties,
                                                           MatchCriteria             matchCriteria,
                                                           int                       fromRelationshipElement,
                                                           List<InstanceStatus>      limitResultsByStatus,
                                                           Date                      asOfTime,
                                                           String                    sequencingProperty,
                                                           SequencingOrder           sequencingOrder,
                                                           int                       pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "findRelationshipsByProperty";
        final String  guidParameterName = "relationshipTypeGUID";

        /*
         * Validate parameters
         */
        super.findRelationshipsByPropertyParameterValidation(userId,
                relationshipTypeGUID,
                matchProperties,
                matchCriteria,
                fromRelationshipElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);

        this.validateTypeGUID(repositoryName, guidParameterName, relationshipTypeGUID, methodName);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }


        //if (asOfTime != null) {
        //    log.error("{} does not support asOfTime searches", methodName);
        //
        //    OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
        //
        //    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
        //            this.getClass().getName(),
        //            repositoryName);
        //
        //    throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
        //            this.getClass().getName(),
        //            methodName,
        //            errorMessage,
        //            errorCode.getSystemAction(),
        //            errorCode.getUserAction());
        //}

        /*
         * Perform operation
         */

        // There are no supertype/subtype hierarchies in relationship types, so only search the specified type or all types.

        List<Relationship> returnRelationships = null;

        String specifiedTypeName = null;

        if (relationshipTypeGUID != null) {
            // search the specified type (only)
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, guidParameterName, relationshipTypeGUID, methodName);
            specifiedTypeName = typeDef.getName();

            log.info("{}: search relationship type {}", methodName, specifiedTypeName);

            returnRelationships = graphStore.findRelationshipsByProperty(specifiedTypeName, matchProperties, matchCriteria, true);

        }
        else {
            // search all types

            TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
            List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

            for (TypeDef typeDef : allTypeDefs) {

                if (typeDef.getCategory() == TypeDefCategory.RELATIONSHIP_DEF) {

                    log.info("{}: search relationship type {}", methodName, typeDef.getName());

                    String actualTypeName = typeDef.getName();

                    // For this type, invoke a type specific search...

                    List<Relationship> relationshipsForCurrentType = graphStore.findRelationshipsByProperty(actualTypeName, matchProperties, matchCriteria, true);

                    if (relationshipsForCurrentType != null && !relationshipsForCurrentType.isEmpty()) {
                        if (returnRelationships == null) {
                            returnRelationships = new ArrayList<>();
                        }
                        log.info("{}: for type {} found {} relationships", methodName, typeDef.getName(), relationshipsForCurrentType.size());
                        returnRelationships.addAll(relationshipsForCurrentType);
                    } else {
                        log.info("{}: for type {} found no relationships", methodName, typeDef.getName());
                    }
                }
            }
        }


        // Eliminate soft deleted relationships and apply status filtering if any was requested
        if (returnRelationships == null) {
            return null;
        }
        else {
            List<Relationship> retainedRelationships = new ArrayList<>();
            for (Relationship relationship : returnRelationships) {
                if (relationship != null) {
                    if ((relationship.getStatus() != InstanceStatus.DELETED)
                            && (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship))) {

                        retainedRelationships.add(relationship);
                    }
                }
            }

            return repositoryHelper.formatRelationshipResults(retainedRelationships, fromRelationshipElement, sequencingProperty, sequencingOrder, pageSize);
        }
    }





    public  void validateTypeGUID(String sourceName,
                                  String guidParameterName,
                                  String guid,
                                  String methodName)
            throws
            TypeErrorException
    {
        if  (guid != null)
        {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, guidParameterName, guid, methodName);
            if (typeDef == null)
            {
                throw new TypeErrorException(OMRSErrorCode.TYPEDEF_ID_NOT_KNOWN.getMessageDefinition(guid, guidParameterName, methodName, sourceName),
                        this.getClass().getName(),
                        methodName);
            }
        }
    }


    // findEntitiesByPropertyValue
    public  List<EntityDetail> findEntitiesByPropertyValue(String                userId,
                                                           String                entityTypeGUID,
                                                           String                searchCriteria,
                                                           int                   fromEntityElement,
                                                           List<InstanceStatus>  limitResultsByStatus,
                                                           List<String>          limitResultsByClassification,
                                                           Date                  asOfTime,
                                                           String                sequencingProperty,
                                                           SequencingOrder       sequencingOrder,
                                                           int                   pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "findEntitiesByPropertyValue";
        final String  entityTypeGUIDParameterName = "entityTypeGUID";

        /*
         * Validate parameters
         */
        super.findEntitiesByPropertyValueParameterValidation(userId,
                entityTypeGUID,
                searchCriteria,
                fromEntityElement,
                limitResultsByStatus,
                limitResultsByClassification,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }


        /*
         * Perform operation
         */

        List<EntityDetail> returnEntities = null;

        // Include subtypes

        String specifiedTypeName = null;
        if (entityTypeGUID != null) {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, entityTypeGUIDParameterName, entityTypeGUID, methodName);
            specifiedTypeName = typeDef.getName();
        }

        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

        for (TypeDef typeDef : allTypeDefs) {
            if (typeDef.getCategory() == TypeDefCategory.ENTITY_DEF) {

                String actualTypeName = typeDef.getName();

                // If entityTypeGUID parameter is not null there is an expected type, so check whether the
                // current type matches the expected type or is one of its sub-types.

                if (specifiedTypeName != null) {

                    boolean typeMatch = repositoryHelper.isTypeOf(metadataCollectionId, actualTypeName, specifiedTypeName);
                    if (!typeMatch) {
                        continue;
                    }

                }

                InstanceProperties matchProperties = graphStore.constructMatchPropertiesForSearchCriteria(typeDef, searchCriteria, GraphOMRSConstants.ElementType.Vertex);


                // Do not tolerate substring matches - instead always the regex must match the whole value - i.e. set fullMatch parameter to true.
                List<EntityDetail> entitiesForCurrentType = graphStore.findEntitiesByProperty(actualTypeName, matchProperties, MatchCriteria.ANY, true);


                if (entitiesForCurrentType != null && !entitiesForCurrentType.isEmpty()) {
                    if (returnEntities == null) {
                        returnEntities = new ArrayList<>();
                    }
                    log.info("{}: for type {} found {} entities", methodName, typeDef.getName(), entitiesForCurrentType.size());
                    returnEntities.addAll(entitiesForCurrentType);
                } else {
                    log.info("{}: for type {} found no entities", methodName, typeDef.getName());
                }

            }
        }

        // Eliminate soft deleted entities and apply status and classification filtering if any was requested
        if (returnEntities ==  null) {
            return null;
        }
        else {
            List<EntityDetail> retainedEntities = new ArrayList<>();
            for (EntityDetail entity : returnEntities) {
                if (entity != null) {
                    if ((entity.getStatus() != InstanceStatus.DELETED)
                            && (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity))
                            && (repositoryValidator.verifyEntityIsClassified(limitResultsByClassification, entity))) {

                        retainedEntities.add(entity);
                    }
                }
            }

            return repositoryHelper.formatEntityResults(retainedEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }
    }



    // findRelationshipsByPropertyValue
    public  List<Relationship> findRelationshipsByPropertyValue(String                    userId,
                                                                String                    relationshipTypeGUID,
                                                                String                    searchCriteria,
                                                                int                       fromRelationshipElement,
                                                                List<InstanceStatus>      limitResultsByStatus,
                                                                Date                      asOfTime,
                                                                String                    sequencingProperty,
                                                                SequencingOrder           sequencingOrder,
                                                                int                       pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName = "findRelationshipsByPropertyValue";
        final String relationshipTypeGUIDParameterName = "relationshipTypeGUID";


        /*
         * Validate parameters
         */
        super.findRelationshipsByPropertyValueParameterValidation(userId,
                relationshipTypeGUID,
                searchCriteria,
                fromRelationshipElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }

        /*
         * Perform operation
         */
        List<Relationship> foundRelationships = new ArrayList<>();

        // There are no supertype/subtype hierarchies in relationship types, so only search the specified type or all types.

        List<Relationship> returnRelationships = null;

        String specifiedTypeName = null;

        List<TypeDef> typesToSearch = new ArrayList<>();


        if (relationshipTypeGUID != null) {

            // search the specified type (only)
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, relationshipTypeGUIDParameterName, relationshipTypeGUID, methodName);
            specifiedTypeName = typeDef.getName();

            log.info("{}: search relationship type {}", methodName, specifiedTypeName);
            typesToSearch.add(typeDef);
        }
        else {

            // search all types

            TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
            List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

            for (TypeDef typeDef : allTypeDefs) {

                if (typeDef.getCategory() == TypeDefCategory.RELATIONSHIP_DEF) {

                    log.info("{}: search relationship type {}", methodName, typeDef.getName());
                    typesToSearch.add(typeDef);
                }
            }
        }


        for (TypeDef typeDef : typesToSearch) {

            String currentTypeName = typeDef.getName();

            InstanceProperties matchProperties = graphStore.constructMatchPropertiesForSearchCriteria(typeDef, searchCriteria, GraphOMRSConstants.ElementType.Edge);

            // Expect the regex to fully match the value
            List<Relationship> relationshipsForCurrentType = graphStore.findRelationshipsByProperty(currentTypeName, matchProperties, MatchCriteria.ANY, true);

            if (relationshipsForCurrentType != null && !relationshipsForCurrentType.isEmpty()) {
                if (returnRelationships == null) {
                    returnRelationships = new ArrayList<>();
                }
                log.info("{}: for type {} found {} relationships", methodName, typeDef.getName(), relationshipsForCurrentType.size());
                returnRelationships.addAll(relationshipsForCurrentType);

            }
            else {
                log.info("{}: for type {} found no relationships", methodName, typeDef.getName());
            }

        }


        // Eliminate soft deleted relationships and apply status filtering if any was requested
        if (returnRelationships == null) {
            return null;
        }
        else {
            List<Relationship> retainedRelationships = new ArrayList<>();
            for (Relationship relationship : returnRelationships) {
                if (relationship != null) {
                    if ((relationship.getStatus() != InstanceStatus.DELETED)
                            && (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship))) {

                        retainedRelationships.add(relationship);
                    }
                }
            }

            return repositoryHelper.formatRelationshipResults(retainedRelationships, fromRelationshipElement, sequencingProperty, sequencingOrder, pageSize);
        }
    }



    // classifyEntity
    public EntityDetail classifyEntity(String               userId,
                                       String               entityGUID,
                                       String               classificationName,
                                       InstanceProperties   classificationProperties)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName                  = "classifyEntity";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";
        final String  propertiesParameterName     = "classificationProperties";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityDetailFromStore(entityGUID);

        }
        catch (EntityProxyOnlyException | EntityNotKnownException e) {
            log.error("{} entity wth GUID {} not found or only a proxy", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, entityGUID);
            throw e;
        }

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        InstanceType entityType = entity.getType();

        repositoryValidator.validateClassification(repositoryName, classificationParameterName, classificationName, entityType.getTypeDefName(), methodName);

        Classification newClassification;
        try
        {
            repositoryValidator.validateClassificationProperties(repositoryName,
                    classificationName,
                    propertiesParameterName,
                    classificationProperties,
                    methodName);

            /*
             * Validation complete - build the new classification
             */
            newClassification = repositoryHelper.getNewClassification(repositoryName,
                    userId,
                    classificationName,
                    entityType.getTypeDefName(),
                    ClassificationOrigin.ASSIGNED,
                    null,
                    classificationProperties);
        }
        catch (PropertyErrorException  error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            throw new ClassificationErrorException(OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(),
                    this.getClass().getName(),
                    methodName,
                    error);
        }

        /*
         * Validation complete - ok to update entity
         */

        EntityDetail updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName, entity, newClassification, methodName);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;
    }


    // declassifyEntity
    public EntityDetail declassifyEntity(String  userId,
                                         String  entityGUID,
                                         String  classificationName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "declassifyEntity";

        /*
         * Validate parameters
         */
        super.declassifyEntityParameterValidation(userId, entityGUID, classificationName);

        /*
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityDetailFromStore(entityGUID);

        }
        catch (EntityProxyOnlyException | EntityNotKnownException e) {
            log.error("{} entity wth GUID {} not found or only a proxy", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(methodName,
                                                                                                   this.getClass().getName(),
                                                                                                   repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, entityGUID);
            throw e;
        }



        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        EntityDetail updatedEntity = repositoryHelper.deleteClassificationFromEntity(repositoryName, entity, classificationName, methodName);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;
    }


    // findEntitiesByClassification
    public  List<EntityDetail> findEntitiesByClassification(String                    userId,
                                                            String                    entityTypeGUID,
                                                            String                    classificationName,
                                                            InstanceProperties        matchClassificationProperties,
                                                            MatchCriteria             matchCriteria,
                                                            int                       fromEntityElement,
                                                            List<InstanceStatus>      limitResultsByStatus,
                                                            Date                      asOfTime,
                                                            String                    sequencingProperty,
                                                            SequencingOrder           sequencingOrder,
                                                            int                       pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            ClassificationErrorException,
            FunctionNotSupportedException,
            PropertyErrorException,
            PagingErrorException,
            UserNotAuthorizedException
    {

        final String methodName = "findEntitiesByClassification";
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        /*
         * Validate parameters
         */
        super.findEntitiesByClassificationParameterValidation(userId,
                entityTypeGUID,
                classificationName,
                matchClassificationProperties,
                matchCriteria,
                fromEntityElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }


        /*
         * Perform operation
         */


        ArrayList<EntityDetail> returnEntities = null;


        String specifiedTypeName = null;
        if (entityTypeGUID != null) {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, entityTypeGUIDParameterName, entityTypeGUID, methodName);
            specifiedTypeName = typeDef.getName();
        }

        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

        for (TypeDef typeDef : allTypeDefs) {
            if (typeDef.getCategory() == TypeDefCategory.ENTITY_DEF) {

                String actualTypeName = typeDef.getName();

                // If entityTypeGUID parameter is not null there is an expected type, so check whether the
                // current type matches the expected type or is one of its sub-types.

                if (specifiedTypeName != null) {

                    boolean typeMatch = repositoryHelper.isTypeOf(metadataCollectionId, actualTypeName, specifiedTypeName);
                    if (!typeMatch) {
                        continue;
                    }

                }

                // Find all entities of this type that have the matching classification.
                //
                List<EntityDetail> entitiesForCurrentType = graphStore.findEntitiesByClassification(classificationName, matchClassificationProperties, matchCriteria, actualTypeName);


                if (entitiesForCurrentType != null && !entitiesForCurrentType.isEmpty()) {
                    if (returnEntities == null) {
                        returnEntities = new ArrayList<>();
                    }
                    log.info("{}: for type {} found {} entities", methodName, typeDef.getName(), entitiesForCurrentType.size());
                    returnEntities.addAll(entitiesForCurrentType);
                } else {
                    log.info("{}: for type {} found no entities", methodName, typeDef.getName());
                }

            }
        }


        // Eliminate soft deleted entities and apply status filtering if any was requested
        if (returnEntities == null) {
            return null;
        } else {
            List<EntityDetail> retainedEntities = new ArrayList<>();
            for (EntityDetail entity : returnEntities) {
                if (entity != null) {
                    if (   (entity.getStatus() != InstanceStatus.DELETED)
                        && (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity))) {

                        retainedEntities.add(entity);
                    }
                }
            }

            return repositoryHelper.formatEntityResults(retainedEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }
    }



    // deleteEntity
     public EntityDetail deleteEntity(String    userId,
                                      String    typeDefGUID,
                                      String    typeDefName,
                                      String    obsoleteEntityGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String methodName = "deleteEntity";
        final String parameterName  = "obsoleteEntityGUID";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, obsoleteEntityGUID, parameterName, methodName);

        /*
         * Locate Entity. Doesn't matter if it is a proxy or not, so try both
         */
        EntityDetail entityDetail = null;
        try {

            entityDetail = graphStore.getEntityDetailFromStore(obsoleteEntityGUID);
            repositoryValidator.validateEntityFromStore(repositoryName, obsoleteEntityGUID, entityDetail, methodName);

        }
        catch (EntityProxyOnlyException | EntityNotKnownException e) {

            log.error("{} entity wth GUID {} not found or only a proxy", methodName, obsoleteEntityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(obsoleteEntityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }

        repositoryValidator.validateTypeForInstanceDelete(repositoryName, typeDefGUID, typeDefName, entityDetail, methodName);

        repositoryValidator.validateInstanceStatusForDelete(repositoryName, entityDetail, methodName);

        /*
         * Locate/delete relationships for entity
         */
        try {
            List<Relationship> relationships = this.getRelationshipsForEntity(userId,
                    obsoleteEntityGUID,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    10000);


            if (relationships != null) {
                for (Relationship relationship : relationships) {
                    if (relationship != null) {
                        InstanceType type = relationship.getType();
                        if (type != null) {
                            this.deleteRelationship(userId,
                                    type.getTypeDefGUID(),
                                    type.getTypeDefName(),
                                    relationship.getGUID());
                        }
                    }
                }
            }
        } catch (Throwable error) {
            log.error("{} entity wth GUID {} caused throwable", methodName, obsoleteEntityGUID, error);
        }


        /*
         * A delete is a soft-delete that updates the status to DELETED.
         */

        EntityDetail updatedEntity = new EntityDetail(entityDetail);

        updatedEntity.setStatusOnDelete(entityDetail.getStatus());
        updatedEntity.setStatus(InstanceStatus.DELETED);

        updatedEntity = repositoryHelper.incrementVersion(userId, entityDetail, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;


    }

    // restoreEntity
    public EntityDetail restoreEntity(String    userId,
                                      String    deletedEntityGUID)
        throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityNotDeletedException,
            UserNotAuthorizedException
    {
        final String methodName    = "restoreEntity";
        final String parameterName = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId, deletedEntityGUID, parameterName, methodName);

        /*
         * Locate entity
         */
        EntityDetail entity;
        try {
            entity = graphStore.getEntityDetailFromStore(deletedEntityGUID);

            repositoryValidator.validateEntityFromStore(repositoryName, deletedEntityGUID, entity, methodName);

            repositoryValidator.validateEntityIsDeleted(repositoryName, entity, methodName);

        }
        catch (EntityProxyOnlyException e) {
            log.error("{} entity wth GUID {} only a proxy", methodName, deletedEntityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(deletedEntityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }
        /*
         * Validation is complete.  It is ok to restore the entity.
         */

        EntityDetail restoredEntity = new EntityDetail(entity);

        restoredEntity.setStatus(entity.getStatusOnDelete());
        restoredEntity.setStatusOnDelete(null);

        restoredEntity = repositoryHelper.incrementVersion(userId, entity, restoredEntity);

        graphStore.updateEntityInStore(restoredEntity);

        return restoredEntity;
    }


    // deleteRelationship
    public Relationship deleteRelationship(String    userId,
                                           String    typeDefGUID,
                                           String    typeDefName,
                                           String    obsoleteRelationshipGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName = "deleteRelationship";
        final String  parameterName = "obsoleteRelationshipGUID";


        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, obsoleteRelationshipGUID, parameterName, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, obsoleteRelationshipGUID);

        repositoryValidator.validateTypeForInstanceDelete(repositoryName, typeDefGUID, typeDefName, relationship, methodName);

        /*
         * A delete is a soft-delete that updates the status to DELETED.
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setStatusOnDelete(relationship.getStatus());
        updatedRelationship.setStatus(InstanceStatus.DELETED);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    // restoreRelationship
    public Relationship restoreRelationship(String    userId,
                                            String    deletedRelationshipGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            RelationshipNotDeletedException,
            UserNotAuthorizedException
    {
        final String methodName    = "restoreRelationship";
        final String parameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, deletedRelationshipGUID, parameterName, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = graphStore.getRelationshipFromStore(deletedRelationshipGUID);

        repositoryValidator.validateRelationshipFromStore(repositoryName, deletedRelationshipGUID, relationship, methodName);
        repositoryValidator.validateRelationshipIsDeleted(repositoryName, relationship, methodName);

        /*
         * Validation is complete.  It is ok to restore the relationship.
         */

        Relationship restoredRelationship = new Relationship(relationship);
        restoredRelationship.setStatus(relationship.getStatusOnDelete());
        relationship.setStatusOnDelete(null);

        restoredRelationship = repositoryHelper.incrementVersion(userId, relationship, restoredRelationship);

        graphStore.updateRelationshipInStore(restoredRelationship);

        return restoredRelationship;

    }



    // reIdentifyEntity
    public EntityDetail reIdentifyEntity(String     userId,
                                         String     typeDefGUID,
                                         String     typeDefName,
                                         String     entityGUID,
                                         String     newEntityGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName = "reIdentifyEntity";
        final String  instanceParameterName = "entityGUID";
        final String  newInstanceParameterName = "newEntityGUID";

        /*
         * Validate parameters
         */
        this.reIdentifyInstanceParameterValidation(userId,
                                                   typeDefGUID,
                                                   typeDefName,
                                                   entityGUID,
                                                   instanceParameterName,
                                                   newEntityGUID,
                                                   newInstanceParameterName,
                                                   methodName);

        /*
         * Locate entity
         */
        EntityDetail entity = null;
        try {
            entity = graphStore.getEntityDetailFromStore(entityGUID);

            repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        }
        catch (EntityProxyOnlyException e) {
            log.error("{} entity wth GUID {} only a proxy", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }

        repositoryValidator.validateEntityCanBeUpdated(repositoryName, metadataCollectionId, entity, methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail updatedEntity = new EntityDetail(entity);

        updatedEntity.setGUID(newEntityGUID);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.removeEntityFromStore(entityGUID);
        graphStore.createEntityInStore(updatedEntity);

        return updatedEntity;
    }



    // reHomeEntity
    public EntityDetail reHomeEntity(String         userId,
                                     String         entityGUID,
                                     String         typeDefGUID,
                                     String         typeDefName,
                                     String         homeMetadataCollectionId,
                                     String         newHomeMetadataCollectionId,
                                     String         newHomeMetadataCollectionName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String methodName                = "reHomeEntity";
        final String entityParameterName       = "entityGUID";

        /*
         * Validate parameters
         */
        super.reHomeInstanceParameterValidation(userId,
                                                entityGUID,
                                                entityParameterName,
                                                typeDefGUID,
                                                typeDefName,
                                                homeMetadataCollectionId,
                                                newHomeMetadataCollectionId,
                                                methodName);

        /*
         * Locate entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityDetailFromStore(entityGUID);

            repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);

            repositoryValidator.validateEntityCanBeRehomed(repositoryName, metadataCollectionId, entity, methodName);

        }
        catch (EntityProxyOnlyException e) {
            log.error("{} entity wth GUID {} only a proxy", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }


        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setMetadataCollectionId(newHomeMetadataCollectionId);
        updatedEntity.setMetadataCollectionName(newHomeMetadataCollectionName);
        updatedEntity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;
    }


    // reTypeEntity
    public EntityDetail reTypeEntity(String         userId,
                                     String         entityGUID,
                                     TypeDefSummary currentTypeDefSummary,
                                     TypeDefSummary newTypeDefSummary)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            ClassificationErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName                  = "reTypeEntity";
        final String  entityParameterName         = "entityGUID";
        final String  currentTypeDefParameterName = "currentTypeDefSummary";
        final String  newTypeDefParameterName     = "newTypeDefSummary";

        /*
         * Validate parameters
         */
        super.reTypeInstanceParameterValidation(userId,
                                                entityGUID,
                                                entityParameterName,
                                                TypeDefCategory.ENTITY_DEF,
                                                currentTypeDefSummary,
                                                newTypeDefSummary,
                                                methodName);
        /*
         * Locate entity
         */
        EntityDetail entity = null;
        try {
            entity = graphStore.getEntityDetailFromStore(entityGUID);

            repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);

            repositoryValidator.validateEntityCanBeUpdated(repositoryName, metadataCollectionId, entity, methodName);

            repositoryValidator.validateInstanceType(repositoryName,
                    entity,
                    currentTypeDefParameterName,
                    currentTypeDefParameterName,
                    currentTypeDefSummary.getGUID(),
                    currentTypeDefSummary.getName());

            repositoryValidator.validatePropertiesForType(repositoryName,
                    newTypeDefParameterName,
                    newTypeDefSummary,
                    entity.getProperties(),
                    methodName);

            repositoryValidator.validateClassificationList(repositoryName,
                    entityParameterName,
                    entity.getClassifications(),
                    newTypeDefSummary.getName(),
                    methodName);
        }
        catch (EntityProxyOnlyException e) {
            log.error("{} entity wth GUID {} only a proxy", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }


        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);
        InstanceType   newInstanceType = repositoryHelper.getNewInstanceType(repositoryName, newTypeDefSummary);

        updatedEntity.setType(newInstanceType);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(entity);

        return updatedEntity;
    }


    // reIdentifyRelationship
    public Relationship reIdentifyRelationship(String     userId,
                                               String     typeDefGUID,
                                               String     typeDefName,
                                               String     relationshipGUID,
                                               String     newRelationshipGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName = "reIdentifyRelationship";
        final String  instanceParameterName = "relationshipGUID";
        final String  newInstanceParameterName = "newRelationshipGUID";

        /*
         * Validate parameters
         */
        super.reIdentifyInstanceParameterValidation(userId,
                                                    typeDefGUID,
                                                    typeDefName,
                                                    relationshipGUID,
                                                    instanceParameterName,
                                                    newRelationshipGUID,
                                                    newInstanceParameterName,
                                                    methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateRelationshipCanBeUpdated(repositoryName, metadataCollectionId, relationship, methodName);


        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setGUID(newRelationshipGUID);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.removeRelationshipFromStore(relationshipGUID);
        graphStore.createRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    // reTypeRelationship
    public Relationship reTypeRelationship(String         userId,
                                           String         relationshipGUID,
                                           TypeDefSummary currentTypeDefSummary,
                                           TypeDefSummary newTypeDefSummary)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException
    {

        final String methodName                   = "reTypeRelationship";
        final String relationshipParameterName    = "relationshipGUID";
        final String currentTypeDefParameterName  = "currentTypeDefSummary";
        final String newTypeDefParameterName      = "newTypeDefSummary";

        /*
         * Validate parameters
         */
        super.reTypeInstanceParameterValidation(userId,
                                                relationshipGUID,
                                                relationshipParameterName,
                                                TypeDefCategory.RELATIONSHIP_DEF,
                                                currentTypeDefSummary,
                                                newTypeDefSummary,
                                                methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateRelationshipCanBeUpdated(repositoryName, metadataCollectionId, relationship, methodName);

        repositoryValidator.validateInstanceType(repositoryName,
                relationship,
                currentTypeDefParameterName,
                currentTypeDefParameterName,
                currentTypeDefSummary.getGUID(),
                currentTypeDefSummary.getName());


        repositoryValidator.validatePropertiesForType(repositoryName,
                newTypeDefParameterName,
                newTypeDefSummary,
                relationship.getProperties(),
                methodName);


        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);
        InstanceType   newInstanceType = repositoryHelper.getNewInstanceType(repositoryName, newTypeDefSummary);

        updatedRelationship.setType(newInstanceType);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    // reHomeRelationship
    public Relationship reHomeRelationship(String   userId,
                                           String   relationshipGUID,
                                           String   typeDefGUID,
                                           String   typeDefName,
                                           String   homeMetadataCollectionId,
                                           String   newHomeMetadataCollectionId,
                                           String   newHomeMetadataCollectionName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException
    {
        final String methodName                = "reHomeRelationship";
        final String relationshipParameterName = "relationshipGUID";

        /*
         * Validate parameters
         */
        super.reHomeInstanceParameterValidation(userId,
                                                relationshipGUID,
                                                relationshipParameterName,
                                                typeDefGUID,
                                                typeDefName,
                                                homeMetadataCollectionId,
                                                newHomeMetadataCollectionId,
                                                methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateRelationshipCanBeRehomed(repositoryName, metadataCollectionId, relationship, methodName);

        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setMetadataCollectionId(newHomeMetadataCollectionId);
        updatedRelationship.setMetadataCollectionName(newHomeMetadataCollectionName);
        updatedRelationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }



    // updateEntityClassification
    public EntityDetail updateEntityClassification(String               userId,
                                                   String               entityGUID,
                                                   String               classificationName,
                                                   InstanceProperties   properties)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "updateEntityClassification";

        /*
         * Validate parameters
         */
        super.classifyEntityParameterValidation(userId, entityGUID, classificationName, properties, methodName);

        /*
         * Locate entity
         */
        EntityDetail entity = null;
        try {
            entity = graphStore.getEntityDetailFromStore(entityGUID);

            repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
            repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);
            
        }
        catch (EntityProxyOnlyException e) {
            log.error("{} entity wth GUID {} only a proxy", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }


        Classification classification = repositoryHelper.getClassificationFromEntity(repositoryName,
                entity,
                classificationName,
                methodName);

        Classification  newClassification = new Classification(classification);

        newClassification.setProperties(properties);

        repositoryHelper.incrementVersion(userId, classification, newClassification);

        EntityDetail updatedEntity = repositoryHelper.updateClassificationInEntity(repositoryName,
                userId,
                entity,
                newClassification,
                methodName);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);


        return updatedEntity;
    }


    /*
     * Reference Copies
     */

    public void saveEntityReferenceCopy(String         userId,
                                        EntityDetail   entity)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            HomeEntityException,
            EntityConflictException,
            InvalidEntityException,
            UserNotAuthorizedException
    {
        final String  methodName            = "saveEntityReferenceCopy";
        final String  instanceParameterName = "entity";

        /*
         * Validate parameters
         */
        super.referenceInstanceParameterValidation(userId, entity, instanceParameterName, methodName);

        /*
         * Save entity
         */
        graphStore.saveEntityReferenceCopyToStore(entity);

    }


    /*
     * Removal of proxy entities: if a proxy entity existed prior to the ref copy being saved, it was replaced by the
     * ref copy - so when we now purge the ref copy there is no need to remove any proxy - it has already been subsumed.
     */
    public void purgeEntityReferenceCopy(String   userId,
                                         String   entityGUID,
                                         String   typeDefGUID,
                                         String   typeDefName,
                                         String   homeMetadataCollectionId)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            HomeEntityException,
            UserNotAuthorizedException
    {
        final String methodName                = "purgeEntityReferenceCopy";
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        super.manageReferenceInstanceParameterValidation(userId,
                                                         entityGUID,
                                                         typeDefGUID,
                                                         typeDefName,
                                                         entityParameterName,
                                                         homeMetadataCollectionId,
                                                         homeParameterName,
                                                         methodName);

        EntityDetail entity = null;
        try {
            entity = graphStore.getEntityDetailFromStore(entityGUID);
        }
        catch (EntityProxyOnlyException e) {
            log.error("{} entity wth GUID {} only a proxy", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }

        if (entity != null)
        {
            graphStore.removeEntityFromStore(entityGUID);
        }
        else
        {
            super.reportEntityNotKnown(entityGUID, methodName);
        }
    }


    public void saveRelationshipReferenceCopy(String         userId,
                                              Relationship   relationship)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            HomeRelationshipException,
            RelationshipConflictException,
            InvalidRelationshipException,
            UserNotAuthorizedException
    {
        final String  methodName            = "saveRelationshipReferenceCopy";
        final String  instanceParameterName = "relationship";

        /*
         * Validate parameters
         */
        super.referenceInstanceParameterValidation(userId, relationship, instanceParameterName, methodName);


        /*
         * Save relationship
         */
        graphStore.saveRelationshipReferenceCopyToStore(relationship);
    }


    public void purgeRelationshipReferenceCopy(String   userId,
                                               String   relationshipGUID,
                                               String   typeDefGUID,
                                               String   typeDefName,
                                               String   homeMetadataCollectionId)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            HomeRelationshipException,
            UserNotAuthorizedException
    {
        final String methodName                = "purgeRelationshipReferenceCopy";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";


        /*
         * Validate parameters
         */
        this.manageReferenceInstanceParameterValidation(userId,
                                                        relationshipGUID,
                                                        typeDefGUID,
                                                        typeDefName,
                                                        relationshipParameterName,
                                                        homeMetadataCollectionId,
                                                        homeParameterName,
                                                        methodName);

        /*
         * Purge relationship
         */
        graphStore.removeRelationshipFromStore(relationshipGUID);
    }


    // getEntityNeighborhood
    public InstanceGraph getEntityNeighborhood(String               userId,
                                               String               entityGUID,
                                               List<String>         entityTypeGUIDs,
                                               List<String>         relationshipTypeGUIDs,
                                               List<InstanceStatus> limitResultsByStatus,
                                               List<String>         limitResultsByClassification,
                                               Date                 asOfTime,
                                               int                  level)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            TypeErrorException,
            PropertyErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName                                  = "getEntityNeighborhood";
        final String entityGUIDParameterName                     = "entityGUID";
        final String entityTypeGUIDParameterName                 = "entityTypeGUIDs";
        final String relationshipTypeGUIDParameterName           = "relationshipTypeGUIDs";
        final String limitedResultsByClassificationParameterName = "limitResultsByClassification";
        final String asOfTimeParameter                           = "asOfTime";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);

        if (entityTypeGUIDs != null)
        {
            for (String guid : entityTypeGUIDs)
            {
                this.validateTypeGUID(repositoryName, entityTypeGUIDParameterName, guid, methodName);
            }
        }

        if (relationshipTypeGUIDs != null)
        {
            for (String guid : relationshipTypeGUIDs)
            {
                this.validateTypeGUID(repositoryName, relationshipTypeGUIDParameterName, guid, methodName);
            }
        }

        if (limitResultsByClassification != null)
        {
            for (String classificationName : limitResultsByClassification)
            {
                repositoryValidator.validateClassificationName(repositoryName,
                        limitedResultsByClassificationParameterName,
                        classificationName,
                        methodName);
            }
        }


        if (asOfTime != null) {
            // Not supported
            log.error("{} does not support asOfTime parameter", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }

        /*
         * Delegate to the graph store
         */

        return graphStore.getSubGraph(entityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, level);
    }




    // Return the list of entities that are of the types listed in entityTypeGUIDs and are connected, either directly or
    // indirectly to the entity identified by startEntityGUID.
    //
    // This is implemented by delegating to getEntityNeighbourhood with level = 1 and no relationship type filters
    // The specified entityType, status and classification filters are passed through to getEntityNeighbourhood.
    //

    public  List<EntityDetail> getRelatedEntities(String               userId,
                                                  String               startEntityGUID,
                                                  List<String>         entityTypeGUIDs,
                                                  int                  fromEntityElement,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  List<String>         limitResultsByClassification,
                                                  Date                 asOfTime,
                                                  String               sequencingProperty,
                                                  SequencingOrder      sequencingOrder,
                                                  int                  pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "getRelatedEntities";

        /*
         * Validate parameters
         */
        this.getRelatedEntitiesParameterValidation(userId,
                startEntityGUID,
                entityTypeGUIDs,
                fromEntityElement,
                limitResultsByStatus,
                limitResultsByClassification,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }

        /*
         * Perform operation
         */
        InstanceGraph adjacentGraph = this.getEntityNeighborhood( userId, startEntityGUID, entityTypeGUIDs, null, limitResultsByStatus, limitResultsByClassification, null, -1);

        if (adjacentGraph != null) {

            // Pick out the entities - since these are all EntityDetail objects (this method does not return proxy objects)
            List<EntityDetail> adjacentEntities = adjacentGraph.getEntities();

            return repositoryHelper.formatEntityResults(adjacentEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);

        }
        return null;

    }


    // Return all of the relationships and intermediate entities that connect the startEntity with the endEntity.
    public  InstanceGraph getLinkingEntities(String                    userId,
                                             String                    startEntityGUID,
                                             String                    endEntityGUID,
                                             List<InstanceStatus>      limitResultsByStatus,
                                             Date                      asOfTime)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName = "getLinkingEntities";

        /*
         * Validate parameters
         */
        this.getLinkingEntitiesParameterValidation(userId,
                startEntityGUID,
                endEntityGUID,
                limitResultsByStatus,
                asOfTime);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }


        /*
         * Perform operation
         */

        /*
         * Delegate to the graph store
         */


        // This method sets a couple of limits on how far or wide the graph store will traverse looking for paths.
        // The first limit is 'maxPaths' - the traversal will stop if and when this number of traversals has been found.
        // The second limit is 'maxDepth' - the traversal will stop when any traverser reaches a path length exceeding this.
        // For now these limits are set hard here - they could be made soft/configurable.
        int maxPaths = 20;
        int maxDepth = 40;
        try {

            return graphStore.getPaths(startEntityGUID, endEntityGUID, limitResultsByStatus, maxPaths, maxDepth);
        }
        catch (Exception e) {
            throw new RepositoryErrorException(GraphOMRSErrorCode.CONNECTED_ENTITIES_FAILURE.getMessageDefinition(startEntityGUID,
                                                                                                                  endEntityGUID,
                                                                                                                  methodName,
                                                                                                                  this.getClass().getName(),
                                                                                                                  repositoryName),
                    this.getClass().getName(),
                    methodName);
        }
    }
}