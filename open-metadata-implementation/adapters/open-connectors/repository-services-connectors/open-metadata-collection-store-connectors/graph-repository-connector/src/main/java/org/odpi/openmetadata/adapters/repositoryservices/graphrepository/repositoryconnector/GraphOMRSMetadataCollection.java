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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
                                Map<String, Object>          storageProperties) throws RepositoryErrorException
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
            /*
             * Log the error here, but also rethrow the exception to the caller so that the connector sees it and can throw an
             * OMRSLogicErrorException.
             */
            log.error("{} could not create graph metadata collection for repository name {}", methodName, repositoryName);
            throw e;
        }
    }


    // verifyTypeDef will always return result from superclass because all knowledge of types is delegated to the RCM.
    @Override
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


    @Override
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
    @Override
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
    @Override
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
    @Override
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
            log.warn("{} entity with GUID {} does not exist in repository {} or is a proxy", methodName, guid, repositoryName);
            entity = null;
        }

        return entity;
    }

    // isRelationshipKnown
    @Override
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
            log.warn("{} relationship with GUID {} does not exist in repository {}", methodName, guid, repositoryName);
            relationship = null;
        }
        return relationship;

    }

    // getEntitySummary
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
            log.warn("{} entity wth GUID {} only a proxy", methodName, entityGUID);

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
    @Override
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
            log.warn("{} entity wth GUID {} not found or only a proxy", methodName, entityGUID);

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
    @Override
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
    @Override
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
    @Override
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
        catch (Exception  error)
        {
            // nothing to do - keep going
        }

        /*
         * Validation is complete - ok to remove the entity
         */
        graphStore.removeEntityFromStore(entity.getGUID());

    }

    // purgeRelationship
    @Override
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
    @Override
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
    @Override
    public  List<EntityDetail> findEntitiesByProperty(String                 userId,
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
    TypeErrorException,
    RepositoryErrorException,
    PropertyErrorException,
    PagingErrorException,
    FunctionNotSupportedException,
    UserNotAuthorizedException
    {

        final String methodName = "findEntitiesByProperty";
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        List<EntityDetail> entities = null;


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

        // Generate a query plan
        GraphOMRSQueryPlan queryPlan = new   GraphOMRSQueryPlan(repositoryName,
                                                                metadataCollectionId,
                                                                repositoryHelper,
                                                                TypeDefCategory.ENTITY_DEF,
                                                                matchProperties,
                                                                entityTypeGUID,
                                                                null);

        /* Map from qualifiedPropertyName to TDA */
        Map<String, TypeDefAttribute> qualifiedPropertyNameToTypeDefinedAttribute = queryPlan.getQualifiedPropertyNameToTypeDefinedAttribute();

        /* Map from short property name to list of qualifiedPropertyName */
        Map<String, List<String>> shortPropertyNameToQualifiedPropertyNames = queryPlan.getShortPropertyNameToQualifiedPropertyNames();

        List<String> validTypeNames = queryPlan.getValidTypeNames();

        String filterTypeName = queryPlan.getFilterTypeName();

        if (validTypeNames.isEmpty())
        {
            /*
             * Whether filtering was requested or not, short-circuit if there are no valid types as there can be no valid results.
             */
            return null;
        }


        List<EntityDetail> foundEntities = null;

        // If there were any dups there must be horizontal duplication (across the types within the valid type set).
        if (queryPlan.getQueryStrategy() == GraphOMRSQueryPlan.QueryStrategy.Iterate)
        {
            // If there are dups in the property maps perform a per-type query
            foundEntities = findEntitiesByPropertyIteratively(validTypeNames,
                                                              matchProperties,
                                                              matchCriteria);
        }
        else
        {
            // If there are no dups in property maps perform a delegated query.
            foundEntities = graphStore.findEntitiesByPropertyForTypes(validTypeNames,
                                                                      filterTypeName,
                                                                      qualifiedPropertyNameToTypeDefinedAttribute,
                                                                      shortPropertyNameToQualifiedPropertyNames,
                                                                      matchProperties,
                                                                      matchCriteria);
        }
        // Process list of returned entities from sub-methods
        if (foundEntities != null)
        {

            // Perform status and classification filtering
            List<EntityDetail> retainedEntities = new ArrayList<>();
            for (EntityDetail entity : foundEntities)
            {
                if (entity != null)
                {
                    if ((repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity))
                            && (repositoryValidator.verifyEntityIsClassified(limitResultsByClassification, entity)))
                    {

                        retainedEntities.add(entity);
                    }
                }
            }
            // Perform sequencing and paging
            // Eliminate soft deleted entities and apply status and classification filtering if any was requested
            entities = repositoryHelper.formatEntityResults(retainedEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }

        return entities;
    }






    // findEntitiesByPropertyIteratively
    public List<EntityDetail> findEntitiesByPropertyIteratively(List<String>                  validTypeNames,
                                                                InstanceProperties            matchProperties,
                                                                MatchCriteria                 matchCriteria)
    throws
    InvalidParameterException,
    RepositoryErrorException,
    TypeErrorException,
    PropertyErrorException
    {
        final String methodName = "findEntitiesByPropertyIteratively";
        List<EntityDetail> returnEntities = null;
        // Iterate over the validTypeNames and perform a per-type query for each valid type
        for (String typeName : validTypeNames)
        {
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);

            // Invoke a type specific search. The search will expect the regexp to match fully to the value.
            List<EntityDetail> entitiesForCurrentType = graphStore.findEntitiesByPropertyForType(typeName, matchProperties, matchCriteria, true);

            if (entitiesForCurrentType != null && !entitiesForCurrentType.isEmpty())
            {
                if (returnEntities == null)
                {
                    returnEntities = new ArrayList<>();
                }
                log.info("{}: for type {} found {} entities", methodName, typeDef.getName(), entitiesForCurrentType.size());
                returnEntities.addAll(entitiesForCurrentType);
            }
            else
            {
                log.info("{}: for type {} found no entities", methodName, typeDef.getName());
            }
        }
        return returnEntities;
    }



    // findEntitiesIteratively
    public List<EntityDetail> findEntitiesIteratively(List<String>                  validTypeNames,
                                                      SearchProperties              searchProperties,
                                                      MatchCriteria                 matchCriteria)
    throws
        InvalidParameterException,
        RepositoryErrorException,
        FunctionNotSupportedException
    {
        final String methodName = "findEntitiesIteratively";
        List<EntityDetail> returnEntities = null;

        // Iterate over the validTypeNames and perform a per-type query for each valid type
        for (String typeName : validTypeNames)
        {
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);

            // Invoke a type specific search. The search will expect the regexp to match fully to the value.
            List<EntityDetail> entitiesForCurrentType = graphStore.findEntitiesForType(typeName,
                                                                                       searchProperties,
                                                                                       true);

            if (entitiesForCurrentType != null && !entitiesForCurrentType.isEmpty())
            {
                if (returnEntities == null)
                {
                    returnEntities = new ArrayList<>();
                }
                log.info("{}: for type {} found {} entities", methodName, typeDef.getName(), entitiesForCurrentType.size());
                returnEntities.addAll(entitiesForCurrentType);
            }
            else
            {
                log.info("{}: for type {} found no entities", methodName, typeDef.getName());
            }
        }
        return returnEntities;
    }



    // findRelationshipsForTypes
    public List<Relationship> findRelationshipsForTypes(List<String>                  validTypeNames,
                                                        SearchProperties              searchProperties,
                                                        MatchCriteria                 matchCriteria)
    throws
        InvalidParameterException,
        RepositoryErrorException,
        FunctionNotSupportedException
    {
        final String methodName = "findRelationshipsForTypes";
        List<Relationship> returnRelationships = null;

        // Iterate over the validTypeNames and perform a per-type query for each valid type
        for (String typeName : validTypeNames)
        {
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);

            // Invoke a type specific search. The search will expect the regexp to match fully to the value.
            List<Relationship> relationshipsForCurrentType = graphStore.findRelationshipsForType(typeName,
                                                                                                 searchProperties,
                                                                                                 true);

            if (relationshipsForCurrentType != null && !relationshipsForCurrentType.isEmpty())
            {
                if (returnRelationships == null)
                {
                    returnRelationships = new ArrayList<>();
                }
                log.info("{}: for type {} found {} relationships", methodName, typeDef.getName(), relationshipsForCurrentType.size());
                returnRelationships.addAll(relationshipsForCurrentType);
            }
            else
            {
                log.info("{}: for type {} found no relationships", methodName, typeDef.getName());
            }
        }
        return returnRelationships;
    }




    // findRelationshipsByPropertyIteratively
    public List<Relationship> findRelationshipsByPropertyIteratively(List<String>                  validTypeNames,
                                                                     InstanceProperties            matchProperties,
                                                                     MatchCriteria                 matchCriteria)
    throws
        InvalidParameterException,
        RepositoryErrorException
    {
        final String methodName = "findRelationshipsByPropertyIteratively";
        List<Relationship> returnRelationships = null;
        // Iterate over the validTypeNames and perform a per-type query for each valid type
        for (String typeName : validTypeNames)
        {
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);

            // Invoke a type specific search. The search will expect the regexp to match fully to the value.
            List<Relationship> relationshipsForCurrentType = graphStore.findRelationshipsByPropertyForType(typeName,
                                                                                                           matchProperties,
                                                                                                           matchCriteria,
                                                                                                           true);

            if (relationshipsForCurrentType != null && !relationshipsForCurrentType.isEmpty())
            {
                if (returnRelationships == null)
                {
                    returnRelationships = new ArrayList<>();
                }
                log.info("{}: for type {} found {} relationships", methodName, typeDef.getName(), relationshipsForCurrentType.size());
                returnRelationships.addAll(relationshipsForCurrentType);
            }
            else
            {
                log.info("{}: for type {} found no relationships", methodName, typeDef.getName());
            }
        }
        return returnRelationships;
    }


    // findRelationshipsByProperty
    @Override
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


        final String methodName = "findRelationshipsByProperty";
        final String guidParameterName = "relationshipTypeGUID";

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


        if (asOfTime != null)
        {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }

        /*
         * Perform operation
         */

        // Generate a query plan
        GraphOMRSQueryPlan queryPlan = new GraphOMRSQueryPlan(repositoryName,
                                                              metadataCollectionId,
                                                              repositoryHelper,
                                                              TypeDefCategory.RELATIONSHIP_DEF,
                                                              matchProperties,
                                                              relationshipTypeGUID,
                                                              null);

        /* Map from qualifiedPropertyName to TDA */
        Map<String, TypeDefAttribute> qualifiedPropertyNameToTypeDefinedAttribute = queryPlan.getQualifiedPropertyNameToTypeDefinedAttribute();

        /* Map from short property name to list of qualifiedPropertyName */
        Map<String, List<String>> shortPropertyNameToQualifiedPropertyNames = queryPlan.getShortPropertyNameToQualifiedPropertyNames();

        List<String> validTypeNames = queryPlan.getValidTypeNames();

        String filterTypeName = queryPlan.getFilterTypeName();



        if (validTypeNames.isEmpty())
        {
            /*
             * Whether filtering was requested or not, short-circuit if there are no valid types as there can be no valid results.
             */
            return null;
        }

        List<Relationship> foundRelationships = null;

        // If there were any dups there must be horizontal duplication (across the types within the valid type set).
        if (queryPlan.getQueryStrategy() == GraphOMRSQueryPlan.QueryStrategy.Iterate)
        {
            // If there are dups in the property maps perform a per-type query
            foundRelationships = findRelationshipsByPropertyIteratively(validTypeNames,
                                                                        matchProperties,
                                                                        matchCriteria);
        }
        else
        {
            // If there are no dups in property maps perform a delegated query.
            foundRelationships = graphStore.findRelationshipsByPropertyForTypes(validTypeNames,
                                                                                filterTypeName,
                                                                                qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                shortPropertyNameToQualifiedPropertyNames,
                                                                                matchProperties,
                                                                                matchCriteria);
        }

        List<Relationship> relationships = null;

        // Process list of returned relationships from sub-methods
        if (foundRelationships != null)
        {
            // Eliminate soft deleted relationships and apply status  filtering if any was requested
            List<Relationship> retainedRelationships = new ArrayList<>();
            for (Relationship relationship : foundRelationships)
            {
                if (relationship != null)
                {
                    if ((repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship)))
                    {
                        retainedRelationships.add(relationship);
                    }
                }
            }
            // Perform sequencing and paging

            relationships = repositoryHelper.formatRelationshipResults(retainedRelationships, fromRelationshipElement, sequencingProperty, sequencingOrder, pageSize);
        }

        return relationships;

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
    @Override
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

        final String methodName = "findEntitiesByPropertyValue";
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        List<EntityDetail> entities = null;

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


        // Generate a query plan
        GraphOMRSQueryPlan queryPlan = new   GraphOMRSQueryPlan(repositoryName,
                                                                metadataCollectionId,
                                                                repositoryHelper,
                                                                TypeDefCategory.ENTITY_DEF,
                                                                entityTypeGUID,
                                                                null);

        /* Map from qualifiedPropertyName to TDA */
        Map<String, TypeDefAttribute> qualifiedPropertyNameToTypeDefinedAttribute = queryPlan.getQualifiedPropertyNameToTypeDefinedAttribute();

        /* Map from short property name to list of qualifiedPropertyName */
        Map<String, List<String>> shortPropertyNameToQualifiedPropertyNames = queryPlan.getShortPropertyNameToQualifiedPropertyNames();

        List<String> validTypeNames = queryPlan.getValidTypeNames();

        String filterTypeName = queryPlan.getFilterTypeName();


        if (validTypeNames.isEmpty())
        {
            /*
             * Whether filtering was requested or not, short-circuit if there are no valid types as there can be no valid results.
             */
            return null;
        }

        List<EntityDetail> foundEntities = null;

        // For this find method the property maps will drive the query and there is no need to decide whether to iterate or delegate.

        foundEntities = findEntitiesByPropertyValueForTypes(validTypeNames,
                                                            filterTypeName,
                                                            qualifiedPropertyNameToTypeDefinedAttribute,
                                                            shortPropertyNameToQualifiedPropertyNames,
                                                            searchCriteria);

        if (foundEntities != null)
        {

            // Eliminate soft deleted entities and apply status and classification filtering if any was requested
            List<EntityDetail> retainedEntities = new ArrayList<>();
            for (EntityDetail entity : foundEntities)
            {
                if (entity != null)
                {
                    if ((repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity))
                            && (repositoryValidator.verifyEntityIsClassified(limitResultsByClassification, entity)))
                    {

                        retainedEntities.add(entity);
                    }
                }
            }
            // Perform sequencing and paging
            entities = repositoryHelper.formatEntityResults(retainedEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }

        return entities;
    }




    // findRelationshipsByPropertyValue
    @Override
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


        // Generate a query plan
        GraphOMRSQueryPlan queryPlan = new   GraphOMRSQueryPlan(repositoryName,
                                                                metadataCollectionId,
                                                                repositoryHelper,
                                                                TypeDefCategory.RELATIONSHIP_DEF,
                                                                relationshipTypeGUID,
                                                                null);

        /* Map from qualifiedPropertyName to TDA */
        Map<String, TypeDefAttribute> qualifiedPropertyNameToTypeDefinedAttribute = queryPlan.getQualifiedPropertyNameToTypeDefinedAttribute();

        /* Map from short property name to list of qualifiedPropertyName */
        Map<String, List<String>> shortPropertyNameToQualifiedPropertyNames = queryPlan.getShortPropertyNameToQualifiedPropertyNames();

        List<String> validTypeNames = queryPlan.getValidTypeNames();

        String filterTypeName = queryPlan.getFilterTypeName();


        if (validTypeNames.isEmpty())
        {
            /*
             * Whether filtering was requested or not, short-circuit if there are no valid types as there can be no valid results.
             */
            return null;
        }

        List<Relationship> foundRelationships = findRelationshipsByPropertyValueForTypes(validTypeNames,
                                                                                         filterTypeName,
                                                                                         qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                         shortPropertyNameToQualifiedPropertyNames,
                                                                                         searchCriteria);

        List<Relationship> relationships = null;

        if (foundRelationships != null)
        {
            // Eliminate soft deleted entities and apply status and classification filtering if any was requested
            List<Relationship> retainedRelationships = new ArrayList<>();
            for (Relationship relationship : foundRelationships)
            {
                if (relationship != null)
                {
                    if ((repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship)))
                    {
                        retainedRelationships.add(relationship);
                    }
                }
            }
            // Perform sequencing and paging
            relationships = repositoryHelper.formatRelationshipResults(retainedRelationships, fromRelationshipElement, sequencingProperty, sequencingOrder, pageSize);
        }

        return relationships;
    }





    // findEntitiesByPropertyValue
    public  List<EntityDetail> findEntitiesByPropertyValueForTypes(List<String>                   validTypeNames,
                                                                   String                         filterTypeName,
                                                                   Map<String, TypeDefAttribute>  qualifiedPropertyNameToTypeDefinedAttribute,
                                                                   Map<String, List<String>>      shortPropertyNameToQualifiedPropertyNames,
                                                                   String                         searchCriteria)
    throws
        InvalidParameterException,
        RepositoryErrorException

    {

        final String methodName = "findEntitiesByPropertyValue";

        /*
         * Construct a match properties object that covers the properties for all included types. This may either be a targeted set of types
         * (now stored in validTypeNames) or it could be the whole set of active entity types. The choice between them is dictated by whether
         * filterTypeName is null or not.
         */
        InstanceProperties matchProperties = graphStore.constructMatchPropertiesForSearchCriteriaForTypes(TypeDefCategory.ENTITY_DEF,
                                                                                                          searchCriteria,
                                                                                                          filterTypeName,
                                                                                                          validTypeNames);

        /*
         * Find all entities of any included type that have matching properties.
         */

        List<EntityDetail> matchingEntities = graphStore.findEntitiesByPropertyValueForTypes(validTypeNames,
                                                                                             filterTypeName,
                                                                                             qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                             shortPropertyNameToQualifiedPropertyNames,
                                                                                             matchProperties,
                                                                                             MatchCriteria.ANY);

        return matchingEntities;

    }




    // findRelationshipsByPropertyValueIteratively
    public  List<Relationship> findRelationshipsByPropertyValueForTypes(List<String>                   validTypeNames,
                                                                        String                         filterTypeName,
                                                                        Map<String, TypeDefAttribute>  qualifiedPropertyNameToTypeDefinedAttribute,
                                                                        Map<String, List<String>>      shortPropertyNameToQualifiedPropertyNames,
                                                                        String                         searchCriteria)
    throws
    InvalidParameterException,
    TypeErrorException,
    RepositoryErrorException,
    PropertyErrorException,
    PagingErrorException,
    FunctionNotSupportedException,
    UserNotAuthorizedException
    {

        final String methodName = "findRelationshipsByPropertyValueIteratively";

        /*
         * Construct a match properties object that covers the properties for all included types. This may either be a targeted set of types
         * (now stored in validTypeNames) or it could be the whole set of active relationship types. The choice between them is dictated by whether
         * filterTypeName is null or not.
         */
        InstanceProperties matchProperties = graphStore.constructMatchPropertiesForSearchCriteriaForTypes(TypeDefCategory.RELATIONSHIP_DEF,
                                                                                                          searchCriteria,
                                                                                                          filterTypeName,
                                                                                                          validTypeNames);

        /*
         * Find all relationships of any included type that have matching properties.
         */

        List<Relationship> matchingRelationships = graphStore.findRelationshipsByPropertyValueForTypes(validTypeNames,
                                                                                                       filterTypeName,
                                                                                                       qualifiedPropertyNameToTypeDefinedAttribute,
                                                                                                       shortPropertyNameToQualifiedPropertyNames,
                                                                                                       matchProperties,
                                                                                                       MatchCriteria.ANY);

        return matchingRelationships;

    }





    // findEntities
    @Override
    public List<EntityDetail> findEntities(String                    userId,
                                           String                    entityTypeGUID,
                                           List<String>              entitySubtypeGUIDs,
                                           SearchProperties          matchProperties,
                                           int                       fromEntityElement,
                                           List<InstanceStatus>      limitResultsByStatus,
                                           SearchClassifications     matchClassifications,
                                           Date                      asOfTime,
                                           String                    sequencingProperty,
                                           SequencingOrder           sequencingOrder,
                                           int                       pageSize)

    throws InvalidParameterException,
           RepositoryErrorException,
           TypeErrorException,
           PropertyErrorException,
           PagingErrorException,
           FunctionNotSupportedException


    {

        final String methodName = "findEntities";
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        List<EntityDetail> entities = null;

        /*
         * Validate parameters
         */
        super.findEntitiesParameterValidation(userId,
                                              entityTypeGUID,
                                              entitySubtypeGUIDs,
                                              matchProperties,
                                              fromEntityElement,
                                              limitResultsByStatus,
                                              matchClassifications,
                                              asOfTime,
                                              sequencingProperty,
                                              sequencingOrder,
                                              pageSize);


        if (asOfTime != null)
        {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }

        // Generate a query plan
        GraphOMRSQueryPlan queryPlan = new GraphOMRSQueryPlan(repositoryName,
                                                              metadataCollectionId,
                                                              repositoryHelper,
                                                              TypeDefCategory.ENTITY_DEF,
                                                              matchProperties,
                                                              entityTypeGUID,
                                                              entitySubtypeGUIDs);

        /* Map from qualifiedPropertyName to TDA */
        Map<String, TypeDefAttribute> qualifiedPropertyNameToTypeDefinedAttribute = queryPlan.getQualifiedPropertyNameToTypeDefinedAttribute();

        /* Map from short property name to list of qualifiedPropertyName */
        Map<String, List<String>> shortPropertyNameToQualifiedPropertyNames = queryPlan.getShortPropertyNameToQualifiedPropertyNames();

        List<String> validTypeNames = queryPlan.getValidTypeNames();

        String filterTypeName = queryPlan.getFilterTypeName();


        if (validTypeNames.isEmpty())
        {
            /*
             * Whether filtering was requested or not, short-circuit if there are no valid types as there can be no valid results.
             */
            return null;
        }

        List<EntityDetail> foundEntities = null;

        // If there were any dups there must be horizontal duplication (across the types within the valid type set).
        if (queryPlan.getQueryStrategy() == GraphOMRSQueryPlan.QueryStrategy.Iterate)
        {
            // If there are dups in the property maps perform a per-type query
            foundEntities = findEntitiesIteratively(validTypeNames,
                                                    matchProperties,
                                                    MatchCriteria.ANY);
        }
        else
        {
            // If there are no dups in property maps perform a delegated query.
            foundEntities = graphStore.findEntitiesForTypes(validTypeNames,
                                                            filterTypeName,
                                                            qualifiedPropertyNameToTypeDefinedAttribute,
                                                            shortPropertyNameToQualifiedPropertyNames,
                                                            matchProperties);
        }


        if (foundEntities != null)
        {
            /*
             * Eliminate soft deleted entities and apply status and classification filtering if any was requested
             */
            List<EntityDetail> retainedEntities = new ArrayList<>();
            for (EntityDetail entity : foundEntities)
            {
                if (entity != null)
                {
                    if ((repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity))
                            && (repositoryValidator.verifyMatchingClassifications(matchClassifications, entity)))
                    {

                        retainedEntities.add(entity);
                    }
                }
            }
            // Perform sequencing and paging
            entities = repositoryHelper.formatEntityResults(retainedEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }

        return entities;

    }




    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of
     * pages.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param relationshipSubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the
     *                                 relationshipTypeGUID to include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of relationship property conditions to match.
     * @param fromRelationshipElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public  List<Relationship> findRelationships(String                    userId,
                                                 String                    relationshipTypeGUID,
                                                 List<String>              relationshipSubtypeGUIDs,
                                                 SearchProperties          matchProperties,
                                                 int                       fromRelationshipElement,
                                                 List<InstanceStatus>      limitResultsByStatus,
                                                 Date                      asOfTime,
                                                 String                    sequencingProperty,
                                                 SequencingOrder           sequencingOrder,
                                                 int                       pageSize)

    throws InvalidParameterException,
           TypeErrorException,
           RepositoryErrorException,
           PropertyErrorException,
           PagingErrorException,
           FunctionNotSupportedException
    {

        final String methodName = "findRelationships";
        final String relationshipTypeGUIDParameterName = "relationshipTypeGUID";

        /*
         * Validate parameters
         */
        super.findRelationshipsParameterValidation(userId,
                                                   relationshipTypeGUID,
                                                   relationshipSubtypeGUIDs,
                                                   matchProperties,
                                                   fromRelationshipElement,
                                                   limitResultsByStatus,
                                                   asOfTime,
                                                   sequencingProperty,
                                                   sequencingOrder,
                                                   pageSize);


        if (asOfTime != null)
        {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }

        /*
         * Perform operation
         */

        // Generate a query plan
        GraphOMRSQueryPlan queryPlan = new GraphOMRSQueryPlan(repositoryName,
                                                              metadataCollectionId,
                                                              repositoryHelper,
                                                              TypeDefCategory.RELATIONSHIP_DEF,
                                                              matchProperties,
                                                              relationshipTypeGUID,
                                                              relationshipSubtypeGUIDs);

        /* Map from qualifiedPropertyName to TDA */
        Map<String, TypeDefAttribute> qualifiedPropertyNameToTypeDefinedAttribute = queryPlan.getQualifiedPropertyNameToTypeDefinedAttribute();

        /* Map from short property name to list of qualifiedPropertyName */
        Map<String, List<String>> shortPropertyNameToQualifiedPropertyNames = queryPlan.getShortPropertyNameToQualifiedPropertyNames();

        List<String> validTypeNames = queryPlan.getValidTypeNames();

        String filterTypeName = queryPlan.getFilterTypeName();


        if (validTypeNames.isEmpty())
        {
            /*
             * Whether filtering was requested or not, short-circuit if there are no valid types as there can be no valid results.
             */
            return null;
        }

        List<Relationship> foundRelationships = null;

        // If there were any dups there must be horizontal duplication (across the types within the valid type set).
        if (queryPlan.getQueryStrategy() == GraphOMRSQueryPlan.QueryStrategy.Iterate)
        {
            // If there are dups in the property maps perform a per-type query
            foundRelationships = findRelationshipsForTypes(validTypeNames,
                                                           matchProperties,
                                                           MatchCriteria.ANY);
        }
        else
        {
            // If there are no dups in property maps perform a delegated query.
            foundRelationships = graphStore.findRelationshipsForTypes(validTypeNames,
                                                                      filterTypeName,
                                                                      qualifiedPropertyNameToTypeDefinedAttribute,
                                                                      shortPropertyNameToQualifiedPropertyNames,
                                                                      matchProperties);
        }


        List<Relationship> relationships = null;

        if (foundRelationships != null)
        {
            /*
             * Eliminate soft deleted relationships and apply status filtering if any was requested
             */
            List<Relationship> retainedRelationships = new ArrayList<>();

            for (Relationship relationship : foundRelationships)
            {
                if (relationship != null)
                {
                    if ((repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship)))
                    {
                        retainedRelationships.add(relationship);
                    }
                }
            }
            // Perform sequencing and paging
            relationships = repositoryHelper.formatRelationshipResults(retainedRelationships, fromRelationshipElement, sequencingProperty, sequencingOrder, pageSize);
        }

        return relationships;

    }






    // classifyEntity
    @Override
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
            log.warn("{} entity wth GUID {} not found or only a proxy", methodName, entityGUID);

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
                    null,
                    InstanceProvenanceType.LOCAL_COHORT,
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
        catch (Exception   error)
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

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;
    }

    // classifyEntity
    @Override
    public Classification classifyEntity(String               userId,
                                         EntityProxy          entityProxy,
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
        final String  methodName                  = "classifyEntity (EntityProxy)";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";
        final String  propertiesParameterName     = "classificationProperties";

        /*
         * Validate parameters
         */
        String entityGUID = entityProxy.getGUID();
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        EntitySummary entity;
        try {

            entity = graphStore.getEntityDetailFromStore(entityGUID);

        }
        catch (EntityNotKnownException e) {
            if(entityProxy.getMetadataCollectionId().equals(metadataCollectionId)){
                // entity should have been stored as a detail
                log.warn("{} entity wth GUID {} not found", methodName, entityGUID);

                throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, repositoryName),
                        this.getClass().getName(),
                        methodName,
                        e);
            }else {
                // entity from another repo, store it as a proxy
                this.addEntityProxy(userId, entityProxy);
                entity = graphStore.getEntityProxyFromStore(entityGUID);
            }

        }
        catch(EntityProxyOnlyException epoe){
            // entity already stored as a proxy
            entity = graphStore.getEntityProxyFromStore(entityGUID);

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
                    null,
                    InstanceProvenanceType.LOCAL_COHORT,
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
        catch (Exception   error)
        {
            throw new ClassificationErrorException(OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(),
                    this.getClass().getName(),
                    methodName,
                    error);
        }

        /*
         * Validation complete - ok to update entity
         */
        if(entity instanceof EntityDetail) {
            EntityDetail updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                    (EntityDetail) entity,
                                                                                    newClassification,
                                                                                    methodName);
            graphStore.updateEntityInStore(updatedEntity);
        }else{
            EntityProxy updatedProxy = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                  (EntityProxy) entity,
                                                                                  newClassification,
                                                                                  methodName);
            graphStore.updateEntityInStore(updatedProxy);
        }

        return newClassification;
    }


    // classifyEntity
    @Override
    public   EntityDetail classifyEntity(String               userId,
                                         String               entityGUID,
                                         String               classificationName,
                                         String               externalSourceGUID,
                                         String               externalSourceName,
                                         ClassificationOrigin classificationOrigin,
                                         String               classificationOriginGUID,
                                         InstanceProperties   classificationProperties)
            throws InvalidParameterException,
                   RepositoryErrorException,
                   EntityNotKnownException,
                   ClassificationErrorException,
                   PropertyErrorException,
                   UserNotAuthorizedException,
                   FunctionNotSupportedException
    {
        final String  methodName = "classifyEntity (detailed)";
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
            log.warn("{} entity wth GUID {} not found or only a proxy", methodName, entityGUID);

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
            if (externalSourceGUID == null)
            {
                newClassification = repositoryHelper.getNewClassification(repositoryName,
                         null,
                         InstanceProvenanceType.LOCAL_COHORT,
                         userId,
                         classificationName,
                         entityType.getTypeDefName(),
                         classificationOrigin,
                         classificationOriginGUID,
                         classificationProperties);
            }
            else
            {
                newClassification = repositoryHelper.getNewClassification(repositoryName,
                         externalSourceGUID,
                         externalSourceName,
                         InstanceProvenanceType.EXTERNAL_SOURCE,
                         userId,
                         classificationName,
                         entityType.getTypeDefName(),
                         classificationOrigin,
                         classificationOriginGUID,
                         classificationProperties);
                newClassification.setMetadataCollectionName(externalSourceName);
                newClassification.setReplicatedBy(metadataCollectionId);
            }
        }
        catch (PropertyErrorException  error)
        {
            throw error;
        }
        catch (Exception   error)
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

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;
    }


    // classifyEntity
    @Override
    public Classification classifyEntity(String               userId,
                                         EntityProxy          entityProxy,
                                         String               classificationName,
                                         String               externalSourceGUID,
                                         String               externalSourceName,
                                         ClassificationOrigin classificationOrigin,
                                         String               classificationOriginGUID,
                                         InstanceProperties   classificationProperties)
        throws InvalidParameterException,
        RepositoryErrorException,
        EntityNotKnownException,
        ClassificationErrorException,
        PropertyErrorException,
        UserNotAuthorizedException,
        FunctionNotSupportedException
    {
        final String  methodName = "classifyEntity (detailed - EntityProxy)";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";
        final String  propertiesParameterName     = "classificationProperties";

        /*
         * Validate parameters
         */
        String entityGUID = entityProxy.getGUID();
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        EntitySummary entity;
        try {

            entity = graphStore.getEntityDetailFromStore(entityGUID);

        }
        catch (EntityNotKnownException e) {
            if(entityProxy.getMetadataCollectionId().equals(metadataCollectionId)){
                // entity should have been stored as a detail
                log.warn("{} entity wth GUID {} not found", methodName, entityGUID);

                throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, repositoryName),
                        this.getClass().getName(),
                        methodName,
                        e);
            }else {
                // entity from another repo, store it as a proxy
                this.addEntityProxy(userId, entityProxy);
                entity = graphStore.getEntityProxyFromStore(entityGUID);
            }

        }
        catch(EntityProxyOnlyException epoe){
            // entity already stored as a proxy
            entity = graphStore.getEntityProxyFromStore(entityGUID);

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
            if (externalSourceGUID == null)
            {
                newClassification = repositoryHelper.getNewClassification(repositoryName,
                        null,
                        InstanceProvenanceType.LOCAL_COHORT,
                        userId,
                        classificationName,
                        entityType.getTypeDefName(),
                        classificationOrigin,
                        classificationOriginGUID,
                        classificationProperties);
            }
            else
            {
                newClassification = repositoryHelper.getNewClassification(repositoryName,
                        externalSourceGUID,
                        externalSourceName,
                        InstanceProvenanceType.EXTERNAL_SOURCE,
                        userId,
                        classificationName,
                        entityType.getTypeDefName(),
                        classificationOrigin,
                        classificationOriginGUID,
                        classificationProperties);
                newClassification.setMetadataCollectionName(externalSourceName);
                newClassification.setReplicatedBy(metadataCollectionId);
            }
        }
        catch (PropertyErrorException  error)
        {
            throw error;
        }
        catch (Exception   error)
        {
            throw new ClassificationErrorException(OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(),
                    this.getClass().getName(),
                    methodName,
                    error);
        }

        /*
         * Validation complete - ok to update entity
         */
        if(entity instanceof EntityDetail) {
            EntityDetail updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName,
                    (EntityDetail) entity,
                    newClassification,
                    methodName);
            graphStore.updateEntityInStore(updatedEntity);
        }else{
            EntityProxy updatedProxy = repositoryHelper.addClassificationToEntity(repositoryName,
                    (EntityProxy) entity,
                    newClassification,
                    methodName);
            graphStore.updateEntityInStore(updatedProxy);
        }

        return newClassification;
    }


    // declassifyEntity
    @Override
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
        super.declassifyEntityParameterValidation(userId, entityGUID, classificationName, methodName);

        /*
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityDetailFromStore(entityGUID);

        }
        catch (EntityProxyOnlyException | EntityNotKnownException e) {
            log.warn("{} entity wth GUID {} not found or only a proxy", methodName, entityGUID);

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

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;
    }


    // declassifyEntity
    @Override
    public Classification declassifyEntity(String      userId,
                                           EntityProxy entityProxy,
                                           String      classificationName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "declassifyEntity (EntityProxy)";

        /*
         * Validate parameters
         */
        super.declassifyEntityParameterValidation(userId, entityProxy, classificationName, methodName);

        /*
         * Locate entity
         */
        EntitySummary entity;
        try
        {

            entity = graphStore.getEntityDetailFromStore(entityProxy.getGUID());

        }
        catch (EntityProxyOnlyException e){

            entity = graphStore.getEntityProxyFromStore(entityProxy.getGUID());

        }
        catch ( EntityNotKnownException e) {
            log.warn("{} entity wth GUID {} not found", methodName, entityProxy.getGUID());

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(methodName,
                    this.getClass().getName(),
                    repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, entityProxy.getGUID());
            throw e;
        }

        repositoryValidator.validateEntityFromStore(repositoryName, entityProxy.getGUID(), entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        Classification toBeRemoved = repositoryHelper.getClassificationFromEntity(repositoryName,
                                                                                  entity,
                                                                                  classificationName,
                                                                                  methodName);

        if(entity instanceof EntityDetail) {
            EntityDetail updatedEntity = repositoryHelper.deleteClassificationFromEntity(repositoryName,
                                                                                         (EntityDetail) entity,
                                                                                         classificationName,
                                                                                         methodName);
            graphStore.updateEntityInStore(updatedEntity);
        }else{
            EntityProxy updatedEntity = repositoryHelper.deleteClassificationFromEntity(repositoryName,
                    (EntityProxy) entity,
                    classificationName,
                    methodName);
            graphStore.updateEntityInStore(updatedEntity);
        }

        return toBeRemoved;
    }


    // findEntitiesByClassification
    @Override
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


        /*
         * This method compiles a list of the valid entity types and passes it to the metadata store method
         * so it can be used inside the traversal. If there is no entity filtering (entityTypeGUID is null)
         * the metadata store will skip the filtering step.
         */

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


        if (asOfTime != null)
        {
            log.error("{} does not support asOfTime searches", methodName);

            super.reportUnsupportedOptionalFunction(methodName);
        }


        /*
         * Perform operation
         */

        /*
         * There is no need for a query plan in this case, because classificationName is mandatory so the graph traversal
         * will always be specific to that classification type. Hence just convert the entityTypeGUID filter to a
         * filterTypeName and generate the set of valid (entity) types.
         */

        String filterTypeName = null;
        if (entityTypeGUID != null)
        {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, entityTypeGUIDParameterName, entityTypeGUID, methodName);
            filterTypeName = typeDef.getName();
        }

        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

        List<String> validTypeNames = new ArrayList<>();
        if (filterTypeName != null)
        {
            for (TypeDef typeDef : allTypeDefs)
            {
                if (typeDef.getCategory() == TypeDefCategory.ENTITY_DEF)
                {
                    String actualTypeName = typeDef.getName();

                    /*
                     * If entityTypeGUID parameter is not null there is an expected type, so check whether the
                     * current type matches the expected type or is one of its sub-types.
                     */
                    boolean typeMatch = repositoryHelper.isTypeOf(metadataCollectionId, actualTypeName, filterTypeName);
                    if (typeMatch)
                    {
                        validTypeNames.add(actualTypeName);
                    }
                }
            }
            if (validTypeNames.isEmpty())
            {
                /*
                 * Filtering was requested but there are no valid types based on the specified GUID.
                 */
                return null;
            }
        }


        /*
         * Find all entities of this type that have the matching classification.
         */
        List<EntityDetail> entitiesWithClassification = graphStore.findEntitiesByClassification(classificationName,
                                                                                                  matchClassificationProperties,
                                                                                                  matchCriteria,
                                                                                                filterTypeName != null,
                                                                                                  validTypeNames);

        if (entitiesWithClassification == null || entitiesWithClassification.isEmpty())
        {
            return null;
        }

        /*
         * Filter list of entities to ensure none are soft-deleted, and are within status filter if any was requested.
         */

        List<EntityDetail> retainedEntities = null;

        for (EntityDetail entity : entitiesWithClassification)
        {
            if (entity != null)
            {
                /*
                 * Assume the entity is to be retained unless it fails any of the filter conditions below...
                 */
                boolean retainEntity = true;

                /*
                 * Status filter
                 * Eliminate soft deleted entities and apply status filtering if any was requested
                 */
                if ((! repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity)))
                {
                    retainEntity = false;
                }

                /*
                 * Check if this entity is worth keeping
                 */
                if (retainEntity)
                {
                    if (retainedEntities == null)
                    {
                        retainedEntities = new ArrayList<>();
                    }
                    retainedEntities.add(entity);
                }
            }
        }

        List<EntityDetail> returnEntities = null;
        if (retainedEntities == null)
        {
            log.info("{}: found no entities", methodName);
        }
        else
        {
            log.info("{}: found {} entities", methodName, retainedEntities.size());
            returnEntities = repositoryHelper.formatEntityResults(retainedEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }

        return returnEntities;

    }



    // deleteEntity
    @Override
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
        catch (EntityProxyOnlyException e) {

            log.warn("{} entity wth GUID {} only a proxy", methodName, obsoleteEntityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(obsoleteEntityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }
        catch (EntityNotKnownException e) {

            log.error("{} entity wth GUID {} not found", methodName, obsoleteEntityGUID);

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
                            if (metadataCollectionId.equals(relationship.getMetadataCollectionId())) {
                                this.deleteRelationship(userId,
                                        type.getTypeDefGUID(),
                                        type.getTypeDefName(),
                                        relationship.getGUID());
                            } else {
                                graphStore.removeRelationshipFromStore(relationship.getGUID());
                            }
                        }
                    }
                }
            }
        } catch (Exception error) {
            log.error("{} entity wth GUID {} caused Exception", methodName, obsoleteEntityGUID, error);
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
    @Override
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
            log.warn("{} entity wth GUID {} only a proxy", methodName, deletedEntityGUID);

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
    @Override
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
    @Override
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
    @Override
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
            log.warn("{} entity wth GUID {} only a proxy", methodName, entityGUID);

            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, repositoryName),
                    this.getClass().getName(),
                    methodName,
                    e);
        }

        repositoryValidator.validateEntityCanBeUpdated(repositoryName, metadataCollectionId, entity, methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail deletedEntity = new EntityDetail(entity);
        deletedEntity.setStatusOnDelete(entity.getStatus());
        deletedEntity.setStatus(InstanceStatus.DELETED);
        deletedEntity = repositoryHelper.incrementVersion(userId, entity, deletedEntity);

        EntityDetail updatedEntity = new EntityDetail(entity);
        updatedEntity.setGUID(newEntityGUID);
        updatedEntity.setReIdentifiedFromGUID(entityGUID);
        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        EntityProxy newEntityProxy = repositoryHelper.getNewEntityProxy(repositoryName, updatedEntity);

        /*
         * Locate/re-point relationships for entity
         */
        try
        {
            List<Relationship> relationships = this.getRelationshipsForEntity(userId,
                    entityGUID,
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
                        if (relationship.getEntityOneProxy().getGUID().equals(entityGUID))
                        {
                            relationship.setEntityOneProxy(newEntityProxy);
                        }
                        else if (relationship.getEntityTwoProxy().getGUID().equals(entityGUID))
                        {
                            relationship.setEntityTwoProxy(newEntityProxy);
                        }
                        graphStore.updateRelationshipInStore(relationship);
                    }
                }
            }
        }
        catch (Exception  error)
        {
            log.error("{} entity wth GUID {} caused Exception", methodName, entityGUID, error);
        }

        graphStore.updateEntityInStore(deletedEntity);
        graphStore.createEntityInStore(updatedEntity);

        return updatedEntity;
    }



    // reHomeEntity
    @Override
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
            log.warn("{} entity wth GUID {} only a proxy", methodName, entityGUID);

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
    @Override
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
            log.warn("{} entity wth GUID {} only a proxy", methodName, entityGUID);

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
    @Override
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
        Relationship   deletedRelationship = new Relationship(relationship);
        deletedRelationship.setStatusOnDelete(relationship.getStatus());
        deletedRelationship.setStatus(InstanceStatus.DELETED);
        deletedRelationship = repositoryHelper.incrementVersion(userId, relationship, deletedRelationship);

        Relationship   updatedRelationship = new Relationship(relationship);
        updatedRelationship.setGUID(newRelationshipGUID);
        updatedRelationship.setReIdentifiedFromGUID(relationshipGUID);
        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(deletedRelationship);
        graphStore.createRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    // reTypeRelationship
    @Override
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
    @Override
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
    @Override
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
            log.warn("{} entity wth GUID {} only a proxy", methodName, entityGUID);

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

        graphStore.updateEntityInStore(updatedEntity);


        return updatedEntity;
    }


    // updateEntityClassification
    @Override
    public Classification updateEntityClassification(String               userId,
                                                     EntityProxy          entityProxy,
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
        String entityGUID = entityProxy.getGUID();
        super.classifyEntityParameterValidation(userId, entityGUID, classificationName, properties, methodName);

        /*
         * Locate entity
         */
        EntitySummary entity = null;
        try {

            entity = graphStore.getEntityDetailFromStore(entityGUID);

        }
        catch (EntityProxyOnlyException e) {
            // entity already stored as a proxy
            entity = graphStore.getEntityProxyFromStore(entityGUID);

        }

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        Classification classification = repositoryHelper.getClassificationFromEntity(repositoryName,
                                                                                     entity,
                                                                                     classificationName,
                                                                                     methodName);

        Classification  newClassification = new Classification(classification);
        newClassification.setProperties(properties);

        repositoryHelper.incrementVersion(userId, classification, newClassification);

        if(entity instanceof EntityDetail) {
            EntityDetail updatedEntity = repositoryHelper.updateClassificationInEntity(repositoryName,
                                                                                       userId,
                                                                                       (EntityDetail) entity,
                                                                                       newClassification,
                                                                                       methodName);
            graphStore.updateEntityInStore(updatedEntity);
        }else{
            EntityProxy updatedProxy = repositoryHelper.updateClassificationInEntity(repositoryName,
                                                                                     userId,
                                                                                     (EntityProxy) entity,
                                                                                     newClassification,
                                                                                     methodName);
            graphStore.updateEntityInStore(updatedProxy);
        }

        return newClassification;
    }

    /*
     * Reference Copies
     */

    @Override
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


    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID)
            throws InvalidParameterException,
                   RepositoryErrorException,
                   EntityNotKnownException,
                   UserNotAuthorizedException,
                   FunctionNotSupportedException
    {
        final String  methodName = "getHomeClassifications";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        EntityDetail retrievedEntity = null;
        try {
            retrievedEntity = graphStore.getEntityDetailFromStore(entityGUID);
        }
        catch (EntityProxyOnlyException | EntityNotKnownException e) {
            log.debug("{} entity wth GUID {} not known or only a proxy", methodName, entityGUID);
        }

        List<Classification> homeClassifications = new ArrayList<>();

        if (retrievedEntity != null) {
            List<Classification> retrievedClassifications = retrievedEntity.getClassifications();

            if (retrievedClassifications != null) {
                for (Classification retrievedClassification : retrievedClassifications) {
                    if (retrievedClassification != null) {
                        if (metadataCollectionId.equals(retrievedClassification.getMetadataCollectionId())) {
                            /*
                             * Locally homed classification
                             */
                            homeClassifications.add(retrievedClassification);
                        }
                    }
                }
            }
        }
        else
        {
            EntityProxy entityProxy = graphStore.getEntityProxyFromStore(entityGUID);

            if (entityProxy != null) {
                List<Classification> retrievedClassifications = entityProxy.getClassifications();

                if (retrievedClassifications != null) {
                    for (Classification retrievedClassification : retrievedClassifications) {
                        if (retrievedClassification != null) {
                            if (metadataCollectionId.equals(retrievedClassification.getMetadataCollectionId())) {
                                /*
                                 * Locally homed classification
                                 */
                                homeClassifications.add(retrievedClassification);
                            }
                        }
                    }
                }
            }
        }

        if (homeClassifications.isEmpty()) {
            return null;
        }

        return homeClassifications;
    }


    /*
     * Removal of proxy entities: if a proxy entity existed prior to the ref copy being saved, it was replaced by the
     * ref copy - so when we now purge the ref copy there is no need to remove any proxy - it has already been subsumed.
     */
    @Override
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
            log.warn("{} entity wth GUID {} only a proxy", methodName, entityGUID);

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


    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityDetail   entity,
                                                Classification classification)
            throws InvalidParameterException,
                   RepositoryErrorException,
                   TypeErrorException,
                   EntityConflictException,
                   InvalidEntityException,
                   PropertyErrorException,
                   UserNotAuthorizedException,
                   FunctionNotSupportedException
    {
        final String  methodName = "saveClassificationReferenceCopy";
        final String  classificationParameterName = "classification";
        final String  propertiesParameterName = "classification.getProperties()";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        EntityDetail retrievedEntity = null;
        try {
            retrievedEntity = graphStore.getEntityDetailFromStore(entity.getGUID());
        }
        catch (EntityProxyOnlyException | EntityNotKnownException e) {
            log.debug("{} entity wth GUID {} not known or only a proxy", methodName, entity.getGUID());
        }

        if ((retrievedEntity == null) && (!metadataCollectionId.equals(entity.getMetadataCollectionId()))) {
            /*
             * If the entity is a reference copy then it can be stored in the repository.
             */
            retrievedEntity = entity;
        }

        if (retrievedEntity != null) {
            try {
                repositoryValidator.validateEntityFromStore(repositoryName, entity.getGUID(), retrievedEntity, methodName);
                repositoryValidator.validateEntityIsNotDeleted(repositoryName, retrievedEntity, methodName);

                repositoryValidator.validateInstanceType(repositoryName, entity);

                InstanceType entityType = entity.getType();

                repositoryValidator.validateClassification(repositoryName,
                                                           classificationParameterName,
                                                           classification.getName(),
                                                           entityType.getTypeDefName(),
                                                           methodName);

                repositoryValidator.validateClassificationProperties(repositoryName,
                                                                     classification.getName(),
                                                                     propertiesParameterName,
                                                                     classification.getProperties(),
                                                                     methodName);

                /*
                 * Validation complete - ok to update entity
                 */

                EntityDetail updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                        retrievedEntity,
                                                                                        classification,
                                                                                        methodName);

                if (metadataCollectionId.equals(entity.getMetadataCollectionId())) {
                    graphStore.updateEntityInStore(updatedEntity);
                }
                else {
                    graphStore.saveEntityReferenceCopyToStore(updatedEntity);
                }
            }
            catch (EntityNotKnownException  error) {
                // Ignore since the entity has been removed since the classification was added
            }
            catch (ClassificationErrorException error) {
                throw new TypeErrorException(error);
            }
        }
    }




    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityProxy    entity,
                                                Classification classification)
            throws InvalidParameterException,
                   RepositoryErrorException,
                   TypeErrorException,
                   EntityConflictException,
                   InvalidEntityException,
                   PropertyErrorException,
                   UserNotAuthorizedException,
                   FunctionNotSupportedException
    {
        final String  methodName = "saveClassificationReferenceCopy";
        final String  classificationParameterName = "classification";
        final String  propertiesParameterName = "classification.getProperties()";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        EntityProxy retrievedEntity =  graphStore.getEntityProxyFromStore(entity.getGUID());

        if ((retrievedEntity == null) && (!metadataCollectionId.equals(entity.getMetadataCollectionId()))) {
            /*
             * If the entity is a reference copy then it can be stored in the repository.
             */
            retrievedEntity = entity;
        }

        if (retrievedEntity != null) {
            try {
                repositoryValidator.validateEntityFromStore(repositoryName, entity.getGUID(), retrievedEntity, methodName);
                repositoryValidator.validateEntityIsNotDeleted(repositoryName, retrievedEntity, methodName);

                repositoryValidator.validateInstanceType(repositoryName, entity);

                InstanceType entityType = entity.getType();

                repositoryValidator.validateClassification(repositoryName,
                                                           classificationParameterName,
                                                           classification.getName(),
                                                           entityType.getTypeDefName(),
                                                           methodName);

                repositoryValidator.validateClassificationProperties(repositoryName,
                                                                     classification.getName(),
                                                                     propertiesParameterName,
                                                                     classification.getProperties(),
                                                                     methodName);

                /*
                 * Validation complete - ok to update entity
                 */

                EntityProxy updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName,
                                                                                        retrievedEntity,
                                                                                        classification,
                                                                                        methodName);

                if (metadataCollectionId.equals(entity.getMetadataCollectionId())) {
                    graphStore.updateEntityInStore(updatedEntity);
                }
                else {
                    graphStore.saveEntityReferenceCopyToStore(updatedEntity);
                }
            }
            catch (EntityNotKnownException  error) {
                // Ignore since the entity has been removed since the classification was added
            }
            catch (ClassificationErrorException error) {
                throw new TypeErrorException(error);
            }
        }
    }



    @Override
    public  void purgeClassificationReferenceCopy(String         userId,
                                                  EntityDetail   entity,
                                                  Classification classification)
            throws InvalidParameterException,
                   TypeErrorException,
                   PropertyErrorException,
                   EntityConflictException,
                   InvalidEntityException,
                   RepositoryErrorException,
                   UserNotAuthorizedException,
                   FunctionNotSupportedException
    {
        final String methodName = "purgeClassificationReferenceCopy";

        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        EntityDetail retrievedEntity = null;
        try {
            retrievedEntity = graphStore.getEntityDetailFromStore(entity.getGUID());
        }
        catch (EntityProxyOnlyException | EntityNotKnownException e) {
            log.debug("{} entity wth GUID {} not known or only a proxy", methodName, entity.getGUID());
        }

        if ((retrievedEntity == null) && (!metadataCollectionId.equals(entity.getMetadataCollectionId()))) {
            /*
             * If the entity is a reference copy then it can be stored in the repository.
             */
            retrievedEntity = entity;
        }

        if (retrievedEntity != null) {
            try {
                EntityDetail updatedEntity = repositoryHelper.deleteClassificationFromEntity(repositoryName,
                                                                                             entity,
                                                                                             classification.getName(),
                                                                                             methodName);

                if (metadataCollectionId.equals(entity.getMetadataCollectionId())) {
                    updatedEntity = repositoryHelper.incrementVersion(userId, retrievedEntity, updatedEntity);
                    graphStore.updateEntityInStore(updatedEntity);
                }
                else {
                    graphStore.saveEntityReferenceCopyToStore(entity);
                }
            }
            catch (ClassificationErrorException error) {
                // Do nothing: this simply means the repository did not have the classification reference copy stored
                // anyway, so nothing to remove (no-op)
                log.debug("{} entity wth GUID {} had no classification {}, so nothing to purge", methodName, entity.getGUID(), classification.getName());
            }
        }
    }



    @Override
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


    @Override
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
    @Override
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

    @Override
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


    // Return all the relationships and intermediate entities that connect the startEntity with the endEntity.
    @Override
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