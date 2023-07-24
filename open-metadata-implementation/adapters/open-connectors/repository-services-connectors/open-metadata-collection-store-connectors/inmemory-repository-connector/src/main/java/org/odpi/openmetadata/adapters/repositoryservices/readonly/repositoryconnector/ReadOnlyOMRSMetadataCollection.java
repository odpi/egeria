/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.readonly.repositoryconnector;

import org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;


import java.util.*;

/**
 * The ReadOnlyOMRSMetadataCollection represents a metadata repository that supports an in-memory repository whose content
 * is read-only.  It is typically populated by open metadata archives.
 */
public class ReadOnlyOMRSMetadataCollection extends InMemoryOMRSMetadataCollection
{
    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection id.
     *
     * @param parentConnector connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param repositoryName name of the repository - used for logging.
     * @param repositoryHelper class used to build type definitions and instances.
     * @param repositoryValidator class used to validate type definitions and instances.
     * @param metadataCollectionId unique Identifier of the metadata collection id.
     */
    ReadOnlyOMRSMetadataCollection(ReadOnlyOMRSRepositoryConnector parentConnector,
                                   String                          repositoryName,
                                   OMRSRepositoryHelper            repositoryHelper,
                                   OMRSRepositoryValidator         repositoryValidator,
                                   String                          metadataCollectionId)
    {
        /*
         * The metadata collection id is the unique identifier for the metadata collection.  It is managed by the super class.
         */
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);
    }



    /* ======================================================
     * Group 4: Maintaining entity and relationship instances
     */

    /**
     * Create a new entity and put it in the requested state.  The new entity is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity - null means no properties.
     * @param initialClassifications initial list of classifications for the new entity - null means no classifications.
     * @param initialStatus initial status - typically DRAFT, PREPARED or ACTIVE.
     * @return EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail addEntity(String                     userId,
                                  String                     entityTypeGUID,
                                  InstanceProperties         initialProperties,
                                  List<Classification>       initialClassifications,
                                  InstanceStatus             initialStatus) throws FunctionNotSupportedException
    {
        final String methodName = "addEntity";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Save a new entity that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Server Capability (guid and qualifiedName respectively).
     * The new entity is assigned a new GUID and put
     * in the requested state.  The new entity is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @return EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail addExternalEntity(String                userId,
                                          String                entityTypeGUID,
                                          String                externalSourceGUID,
                                          String                externalSourceName,
                                          InstanceProperties    initialProperties,
                                          List<Classification>  initialClassifications,
                                          InstanceStatus        initialStatus) throws FunctionNotSupportedException
    {
        final String  methodName = "addExternalEntity";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Create an entity proxy in the metadata collection.  This is used to store relationships that span metadata
     * repositories.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy details of entity to add.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public void addEntityProxy(String       userId,
                               EntityProxy  entityProxy) throws FunctionNotSupportedException
    {
        final String  methodName = "addEntityProxy";

        super.reportUnsupportedOptionalFunction(methodName);
    }


    /**
     * Update the status for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the requested entity.
     * @param newStatus new InstanceStatus for the entity.
     * @return EntityDetail showing the current entity header, properties and classifications.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail updateEntityStatus(String           userId,
                                           String           entityGUID,
                                           InstanceStatus   newStatus) throws FunctionNotSupportedException
    {
        final String  methodName               = "updateEntityStatus";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Update selected properties in an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param properties a list of properties to change.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail updateEntityProperties(String               userId,
                                               String               entityGUID,
                                               InstanceProperties   properties) throws FunctionNotSupportedException
    {
        final String  methodName = "updateEntityProperties";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Undo the last update to an entity and return the previous content.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail undoEntityUpdate(String  userId,
                                         String  entityGUID) throws FunctionNotSupportedException
    {
        final String  methodName = "undoEntityUpdate";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Delete an entity.  The entity is soft-deleted.  This means it is still in the graph, but it is no longer returned
     * on queries.  All homed relationships to the entity are also soft-deleted and will no longer be usable, while any
     * reference copy relationships to the entity will be purged (and will no longer be accessible in this repository).
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the delete() call.
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use; however,
     * this will not restore any of the relationships that were soft-deleted as part of the original deleteEntity() call.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to delete.
     * @param typeDefName unique name of the type of the entity to delete.
     * @param obsoleteEntityGUID String unique identifier (guid) for the entity.
     * @return deleted entity
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail   deleteEntity(String    userId,
                                       String    typeDefGUID,
                                       String    typeDefName,
                                       String    obsoleteEntityGUID) throws FunctionNotSupportedException
    {
        final String methodName    = "deleteEntity";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Permanently removes a deleted entity from the metadata collection. All relationships to the entity -- both homed
     * and reference copies -- will also be purged to maintain referential integrity within the repository. This request
     * can not be undone.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to purge.
     * @param typeDefName unique name of the type of the entity to purge.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public void purgeEntity(String    userId,
                            String    typeDefGUID,
                            String    typeDefName,
                            String    deletedEntityGUID) throws FunctionNotSupportedException
    {
        final String methodName    = "purgeEntity";

        super.reportUnsupportedOptionalFunction(methodName);
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @return EntityDetail showing the restored entity header, properties and classifications.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail restoreEntity(String    userId,
                                      String    deletedEntityGUID) throws FunctionNotSupportedException
    {
        final String  methodName = "restoreEntity";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail classifyEntity(String               userId,
                                       String               entityGUID,
                                       String               classificationName,
                                       InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             UserNotAuthorizedException,
                                                                                             FunctionNotSupportedException
    {
        final String  methodName                  = "classifyEntity";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @return Classification showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Classification classifyEntity(String               userId,
                                         EntityProxy          entityProxy,
                                         String               classificationName,
                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               EntityNotKnownException,
                                                                                               ClassificationErrorException,
                                                                                               PropertyErrorException,
                                                                                               UserNotAuthorizedException,
                                                                                               FunctionNotSupportedException
    {
        final String  methodName                  = "classifyEntity (EntityProxy)";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param classificationOrigin source of the classification
     * @param classificationOriginGUID if the classification is propagated, this is the unique identifier of the entity where
     * @param classificationProperties list of properties to set in the classification.
     * @return EntitySummary showing the resulting entity header and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public   EntityDetail classifyEntity(String               userId,
                                         String               entityGUID,
                                         String               classificationName,
                                         String               externalSourceGUID,
                                         String               externalSourceName,
                                         ClassificationOrigin classificationOrigin,
                                         String               classificationOriginGUID,
                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               EntityNotKnownException,
                                                                                               ClassificationErrorException,
                                                                                               PropertyErrorException,
                                                                                               UserNotAuthorizedException,
                                                                                               FunctionNotSupportedException
    {
        final String  methodName = "classifyEntity (detailed)";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }

    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param classificationOrigin source of the classification
     * @param classificationOriginGUID if the classification is propagated, this is the unique identifier of the entity where
     * @param classificationProperties list of properties to set in the classification.
     * @return Classification showing the resulting entity header and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Classification classifyEntity(String               userId,
                                         EntityProxy          entityProxy,
                                         String               classificationName,
                                         String               externalSourceGUID,
                                         String               externalSourceName,
                                         ClassificationOrigin classificationOrigin,
                                         String               classificationOriginGUID,
                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               EntityNotKnownException,
                                                                                               ClassificationErrorException,
                                                                                               PropertyErrorException,
                                                                                               UserNotAuthorizedException,
                                                                                               FunctionNotSupportedException
    {
        final String  methodName = "classifyEntity (detailed - EntityProxy)";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not set on the entity.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail declassifyEntity(String  userId,
                                         String  entityGUID,
                                         String  classificationName) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            EntityNotKnownException,
                                                                            ClassificationErrorException,
                                                                            UserNotAuthorizedException,
                                                                            FunctionNotSupportedException
    {
        final String  methodName                  = "declassifyEntity";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @return Classification showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not set on the entity.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Classification declassifyEntity(String      userId,
                                           EntityProxy entityProxy,
                                           String      classificationName) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  EntityNotKnownException,
                                                                                  ClassificationErrorException,
                                                                                  UserNotAuthorizedException,
                                                                                  FunctionNotSupportedException
    {
        final String  methodName                  = "declassifyEntity (EntityProxy)";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not attached to the classification.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityClassification(String               userId,
                                                   String               entityGUID,
                                                   String               classificationName,
                                                   InstanceProperties   properties) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           EntityNotKnownException,
                                                                                           ClassificationErrorException,
                                                                                           PropertyErrorException,
                                                                                           UserNotAuthorizedException,
                                                                                           FunctionNotSupportedException
    {
        final String  methodName = "updateEntityClassification";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return Classification showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not attached to the classification.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Classification updateEntityClassification(String               userId,
                                                     EntityProxy          entityProxy,
                                                     String               classificationName,
                                                     InstanceProperties   properties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             UserNotAuthorizedException,
                                                                                             FunctionNotSupportedException
    {
        final String  methodName = "updateEntityClassification (EntityProxy)";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }



    /**
     * Add a new relationship between two entities to the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity - null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status - typically DRAFT, PREPARED or ACTIVE.
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship addRelationship(String               userId,
                                        String               relationshipTypeGUID,
                                        InstanceProperties   initialProperties,
                                        String               entityOneGUID,
                                        String               entityTwoGUID,
                                        InstanceStatus       initialStatus) throws FunctionNotSupportedException
    {
        final String  methodName = "addRelationship";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Save a new relationship that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Server Capability (guid and qualifiedName respectively).
     * The new relationship is assigned a new GUID and put
     * in the requested state.  The new relationship is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status; typically DRAFT, PREPARED or ACTIVE.
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship addExternalRelationship(String               userId,
                                                String               relationshipTypeGUID,
                                                String               externalSourceGUID,
                                                String               externalSourceName,
                                                InstanceProperties   initialProperties,
                                                String               entityOneGUID,
                                                String               entityTwoGUID,
                                                InstanceStatus       initialStatus) throws FunctionNotSupportedException
    {
        final String methodName = "addExternalRelationship";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Update the status of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param newStatus new InstanceStatus for the relationship.
     * @return Resulting relationship structure with the new status set.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship updateRelationshipStatus(String           userId,
                                                 String           relationshipGUID,
                                                 InstanceStatus   newStatus) throws FunctionNotSupportedException
    {
        final String  methodName          = "updateRelationshipStatus";
        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Update the properties of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param properties list of the properties to update.
     * @return Resulting relationship structure with the new properties set.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship updateRelationshipProperties(String               userId,
                                                     String               relationshipGUID,
                                                     InstanceProperties   properties) throws FunctionNotSupportedException
    {
        final String  methodName = "updateRelationshipProperties";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Undo the latest change to a relationship (either a change of properties or status).
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @return Relationship structure with the new current header, requested entities and properties.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship undoRelationshipUpdate(String  userId,
                                               String  relationshipGUID) throws FunctionNotSupportedException
    {
        final String  methodName = "undoRelationshipUpdate";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Delete a specific relationship.  This is a soft-delete which means the relationship's status is updated to
     * DELETED, and it is no longer available for queries.  To remove the relationship permanently from the
     * metadata collection, use purgeRelationship().
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to delete.
     * @param typeDefName unique name of the type of the relationship to delete.
     * @param obsoleteRelationshipGUID String unique identifier (guid) for the relationship.
     * @return deleted relationship
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship deleteRelationship(String    userId,
                                           String    typeDefGUID,
                                           String    typeDefName,
                                           String    obsoleteRelationshipGUID) throws FunctionNotSupportedException
    {
        final String  methodName = "deleteRelationship";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Permanently delete the relationship from the repository.  There is no means to undo this request.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to purge.
     * @param typeDefName unique name of the type of the relationship to purge.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public void purgeRelationship(String    userId,
                                  String    typeDefGUID,
                                  String    typeDefName,
                                  String    deletedRelationshipGUID) throws FunctionNotSupportedException
    {
        final String  methodName    = "purgeRelationship";

        super.reportUnsupportedOptionalFunction(methodName);
    }


    /**
     * Restore a deleted relationship into the metadata collection.  The new status will be ACTIVE and the
     * restored details of the relationship are returned to the caller.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @return Relationship structure with the restored header, requested entities and properties.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship restoreRelationship(String    userId,
                                            String    deletedRelationshipGUID) throws FunctionNotSupportedException
    {
        final String  methodName    = "restoreRelationship";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /* ======================================================================
     * Group 5: Change the control information in entities and relationships
     */


    /**
     * Change the guid of an existing entity to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the entity - used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity - used to verify the entity identity.
     * @param entityGUID the existing identifier for the entity.
     * @param newEntityGUID new unique identifier for the entity.
     * @return entity - new values for this entity, including the new guid.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail reIdentifyEntity(String     userId,
                                         String     typeDefGUID,
                                         String     typeDefName,
                                         String     entityGUID,
                                         String     newEntityGUID) throws FunctionNotSupportedException
    {
        final String  methodName = "reIdentifyEntity";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Change the type for an existing entity.  Typically, this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type and the properties adjusted.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param currentTypeDefSummary the current details of the TypeDef for the entity - used to verify the entity identity
     * @param newTypeDefSummary details of this entity's new TypeDef.
     * @return entity - new values for this entity, including the new type information.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail reTypeEntity(String         userId,
                                     String         entityGUID,
                                     TypeDefSummary currentTypeDefSummary,
                                     TypeDefSummary newTypeDefSummary) throws FunctionNotSupportedException
    {
        final String  methodName = "reTypeEntity";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Change the home of an existing entity.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param typeDefGUID the guid of the TypeDef for the entity - used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity - used to verify the entity identity.
     * @param homeMetadataCollectionId the existing identifier for this entity's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @return entity - new values for this entity, including the new home information.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail reHomeEntity(String         userId,
                                     String         entityGUID,
                                     String         typeDefGUID,
                                     String         typeDefName,
                                     String         homeMetadataCollectionId,
                                     String         newHomeMetadataCollectionId,
                                     String         newHomeMetadataCollectionName) throws FunctionNotSupportedException
    {
        final String methodName          = "reHomeEntity";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Change the guid of an existing relationship.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the relationship - used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship - used to verify the relationship identity.
     * @param relationshipGUID the existing identifier for the relationship.
     * @param newRelationshipGUID  the new unique identifier for the relationship.
     * @return relationship - new values for this relationship, including the new guid.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship reIdentifyRelationship(String     userId,
                                               String     typeDefGUID,
                                               String     typeDefName,
                                               String     relationshipGUID,
                                               String     newRelationshipGUID) throws FunctionNotSupportedException
    {
        final String  methodName = "reIdentifyRelationship";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Change the type for an existing relationship.  Typically, this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param currentTypeDefSummary the details of the TypeDef for the relationship - used to verify the relationship identity.
     * @param newTypeDefSummary details of this relationship's new TypeDef.
     * @return relationship - new values for this relationship, including the new type information.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship reTypeRelationship(String         userId,
                                           String         relationshipGUID,
                                           TypeDefSummary currentTypeDefSummary,
                                           TypeDefSummary newTypeDefSummary) throws FunctionNotSupportedException
    {
        final String methodName = "reTypeRelationship";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Change the home of an existing relationship.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID  the unique identifier for the relationship.
     * @param typeDefGUID  the guid of the TypeDef for the relationship - used to verify the relationship identity.
     * @param typeDefName  the name of the TypeDef for the relationship - used to verify the relationship identity.
     * @param homeMetadataCollectionId  the existing identifier for this relationship's home.
     * @param newHomeMetadataCollectionId  unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @return relationship - new values for this relationship, including the new home information.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship reHomeRelationship(String   userId,
                                           String   relationshipGUID,
                                           String   typeDefGUID,
                                           String   typeDefName,
                                           String   homeMetadataCollectionId,
                                           String   newHomeMetadataCollectionId,
                                           String   newHomeMetadataCollectionName) throws FunctionNotSupportedException
    {
        final String methodName = "reHomeRelationship";

        super.reportUnsupportedOptionalFunction(methodName);

        return null;
    }


    /**
     * Save the entity as a reference copy.  The id of the home metadata collection is already set up in the
     * entity.
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to save.
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveEntityReferenceCopy(String       userId,
                                        EntityDetail entity) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    TypeErrorException,
                                                                    PropertyErrorException,
                                                                    HomeEntityException,
                                                                    EntityConflictException,
                                                                    InvalidEntityException,
                                                                    UserNotAuthorizedException
    {
        if ((entity != null) && ((entity.getInstanceProvenanceType() == InstanceProvenanceType.CONTENT_PACK) ||
                                 (entity.getInstanceProvenanceType() == InstanceProvenanceType.EXPORT_ARCHIVE) ||
                                        entity.getMetadataCollectionId().equals(metadataCollectionId)))
        {
            List<Classification> classifications = entity.getClassifications();

            if (classifications != null)
            {
                EntityDetail strippedEntity = new EntityDetail(entity);
                List<Classification>  strippedClassifications = new ArrayList<>();

                for (Classification classification : classifications)
                {
                    if ((classification != null) && ((classification.getInstanceProvenanceType() == InstanceProvenanceType.CONTENT_PACK) ||
                                                     (classification.getInstanceProvenanceType() == InstanceProvenanceType.EXPORT_ARCHIVE) ||
                                                             classification.getMetadataCollectionId().equals(metadataCollectionId)))
                    {
                        strippedClassifications.add(classification);
                    }
                }

                strippedEntity.setClassifications(strippedClassifications);

                super.saveEntityReferenceCopy(userId, strippedEntity);
            }
            else
            {
                super.saveEntityReferenceCopy(userId, entity);
            }
        }
    }


    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to save.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     */
    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityDetail   entity,
                                                Classification classification) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException
    {
        if ((classification != null) && ((classification.getInstanceProvenanceType() == InstanceProvenanceType.CONTENT_PACK) ||
                                         (classification.getInstanceProvenanceType() == InstanceProvenanceType.EXPORT_ARCHIVE) ||
                                          classification.getMetadataCollectionId().equals(metadataCollectionId)))
        {
            super.saveClassificationReferenceCopy(userId, entity, classification);
        }
    }


    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy entity that the classification is attached to.
     * @param classification classification to save.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     */
    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityProxy    entityProxy,
                                                Classification classification) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException
    {
        if ((classification != null) && ((classification.getInstanceProvenanceType() == InstanceProvenanceType.CONTENT_PACK) ||
                                         (classification.getInstanceProvenanceType() == InstanceProvenanceType.EXPORT_ARCHIVE) ||
                                          classification.getMetadataCollectionId().equals(metadataCollectionId)))
        {
            super.saveClassificationReferenceCopy(userId, entityProxy, classification);
        }
    }


    /**
     * Save the relationship as a reference copy.  The id of the home metadata collection is already set up in the
     * relationship.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to save.
     *
     * @throws InvalidParameterException the relationship is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws RelationshipConflictException the new relationship conflicts with an existing relationship.
     * @throws InvalidRelationshipException the new relationship has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveRelationshipReferenceCopy(String       userId,
                                              Relationship relationship) throws InvalidParameterException,
                                                                                RepositoryErrorException,
                                                                                TypeErrorException,
                                                                                EntityNotKnownException,
                                                                                PropertyErrorException,
                                                                                HomeRelationshipException,
                                                                                RelationshipConflictException,
                                                                                InvalidRelationshipException,
                                                                                UserNotAuthorizedException
    {
        if ((relationship != null) && ((relationship.getInstanceProvenanceType() == InstanceProvenanceType.CONTENT_PACK) ||
                                       (relationship.getInstanceProvenanceType() == InstanceProvenanceType.EXPORT_ARCHIVE) ||
                                        relationship.getMetadataCollectionId().equals(metadataCollectionId)))
        {
            super.saveRelationshipReferenceCopy(userId, relationship);
        }
    }
}
