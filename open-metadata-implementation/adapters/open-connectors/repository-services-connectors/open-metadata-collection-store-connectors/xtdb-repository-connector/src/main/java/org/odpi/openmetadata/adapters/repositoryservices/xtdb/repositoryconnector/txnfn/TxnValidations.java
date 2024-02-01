/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.PropertyKeywords;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.TypeDefCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.ClassificationMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.InstanceAuditHeaderMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Common validations to use within transaction functions.
 */
public class TxnValidations {

    /**
     * Verify that an entity has been successfully retrieved from the repository and has valid contents.
     *
     * @param guid unique identifier used to retrieve the entity
     * @param entity the retrieved entity (or null)
     * @param className doing the validation
     * @param methodName method receiving the call
     * @throws EntityNotKnownException No entity found
     * @throws RepositoryErrorException logic error in the repository corrupted instance
     */
    public static void entityFromStore(String guid,
                                       IPersistentMap entity,
                                       String className,
                                       String methodName)
            throws RepositoryErrorException, EntityNotKnownException {
        if (entity == null) {
            throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(guid),
                                              className,
                                              methodName);
        }
        validInstance(entity, className, methodName, true);
    }

    /**
     * Verify that a relationship has been successfully retrieved from the repository and has valid contents.
     *
     * @param guid unique identifier used to retrieve the entity
     * @param relationship the retrieved relationship (or null)
     * @param className doing the validation
     * @param methodName method receiving the call
     * @throws RelationshipNotKnownException No relationship found
     * @throws RepositoryErrorException logic error in the repository corrupted instance
     */
    public static void relationshipFromStore(String guid,
                                             IPersistentMap relationship,
                                             String className,
                                             String methodName)
            throws RepositoryErrorException, RelationshipNotKnownException {
        if (relationship == null) {
            throw new RelationshipNotKnownException(XTDBErrorCode.RELATIONSHIP_NOT_KNOWN.getMessageDefinition(guid),
                                                    className,
                                                    methodName);
        }
        validRelationship(relationship, className, methodName);
    }

    /**
     * Validate that a classification is valid for the entity.
     *
     * @param classificationName unique name for a classification type
     * @param entityTypeName name of entity type
     * @param className doing the validation
     * @param methodName method receiving the call
     * @throws InvalidParameterException classification name is null
     * @throws ClassificationErrorException the classification is invalid for this entity
     */
    public static void classification(String classificationName,
                                      String entityTypeName,
                                      String className,
                                      String methodName)
            throws InvalidParameterException, ClassificationErrorException {

        validateClassificationName(classificationName, entityTypeName, className, methodName);

        if (entityTypeName != null) {
            if (!TypeDefCache.isValidClassificationForEntity(classificationName, entityTypeName)) {
                throw new ClassificationErrorException(XTDBErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(classificationName,
                                                                                                                            entityTypeName),
                                                       className,
                                                       methodName);
            }
        }
    }

    /**
     * Validates that the provided metadata instance is not in a deleted state.
     *
     * @param existing metadata instance
     * @param instanceGUID of the instance
     * @param className doing the validation
     * @param methodName doing the validation
     * @throws InvalidParameterException if the provided metadata instance is already in a deleted state
     */
    public static void instanceIsNotDeleted(IPersistentMap existing,
                                            String instanceGUID,
                                            String className,
                                            String methodName) throws InvalidParameterException {
        Integer currentStatus = (Integer) existing.valAt(Keywords.CURRENT_STATUS);
        if (currentStatus != null && currentStatus == InstanceStatus.DELETED.getOrdinal()) {
            throw new InvalidParameterException(XTDBErrorCode.INSTANCE_ALREADY_DELETED.getMessageDefinition(
                    instanceGUID), className, methodName, "instance");
        }
    }

    /**
     * Validates that the provided metadata instance is in a soft-deleted state.
     *
     * @param existing metadata instance
     * @param instanceGUID of the instance
     * @param className doing the validation
     * @param methodName doing the validation
     * @throws InvalidParameterException if the provided metadata instance is not in a deleted state
     */
    public static void instanceIsDeleted(IPersistentMap existing,
                                         String instanceGUID,
                                         String className,
                                         String methodName) throws InvalidParameterException {
        Integer currentStatus = (Integer) existing.valAt(Keywords.CURRENT_STATUS);
        if (currentStatus != null && currentStatus != InstanceStatus.DELETED.getOrdinal()) {
            throw new InvalidParameterException(XTDBErrorCode.INSTANCE_NOT_DELETED.getMessageDefinition(
                    instanceGUID), className, methodName, "instance");
        }
    }

    /**
     * Validates that the provided entity is not only an EntityProxy.
     *
     * @param existing entity to check
     * @param entityGUID of the entity
     * @param className doing the validation
     * @param methodName doing the validation
     * @throws EntityNotKnownException if the provided entity is only an EntityProxy
     */
    public static void nonProxyEntity(IPersistentMap existing,
                                      String entityGUID,
                                      String className,
                                      String methodName) throws EntityNotKnownException {
        Boolean proxyOnly = (Boolean) existing.valAt(Keywords.ENTITY_PROXY_ONLY_MARKER);
        if (proxyOnly != null && proxyOnly) {
            throw new EntityNotKnownException(XTDBErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(
                    entityGUID, null), className, methodName);
        }
    }

    /**
     * Verify that a metadata instance can be updated by the metadataCollection. The caller can update a
     * metadata instance provided: the instance is locally homed (matching metadataCollectionId) OR the
     * instance has instanceProvenanceType set to external and replicatedBy is set to the local metadataCollectionId.
     * Any other combination suggests that this is either a reference copy of an instance from the local cohort or
     * a reference copy of an external entity (and something else is responsible for its replication): in these cases
     * we are not permitted to update the metadata instance and will instead throw an InvalidParameterException.
     *
     * @param instance instance to validate
     * @param guid unique identifier for the metadata instance being updated
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param className name of the calling class
     * @param methodName name of calling method
     * @throws InvalidParameterException the instance cannot be updated due to its status or metadataCollectionId
     */
    public static void instanceCanBeUpdated(IPersistentMap instance,
                                            String guid,
                                            String metadataCollectionId,
                                            String className,
                                            String methodName) throws InvalidParameterException {
        instanceCanBeUpdated(instance, guid, metadataCollectionId, null, className, methodName);
    }

    /**
     * Verify that a metadata instance can be updated by the metadataCollection. The caller can update a
     * metadata instance provided: the instance is locally homed (matching metadataCollectionId) OR the
     * instance has instanceProvenanceType set to external and replicatedBy is set to the local metadataCollectionId.
     * Any other combination suggests that this is either a reference copy of an instance from the local cohort or
     * a reference copy of an external entity (and something else is responsible for its replication): in these cases
     * we are not permitted to update the metadata instance and will instead throw an InvalidParameterException.
     *
     * @param instance instance to validate
     * @param guid unique identifier for the metadata instance being updated
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param classificationName of the classification to be updated (or null if only the base instance)
     * @param className name of the calling class
     * @param methodName name of calling method
     * @throws InvalidParameterException the instance cannot be updated due to its status or metadataCollectionId
     */
    public static void instanceCanBeUpdated(IPersistentMap instance,
                                            String guid,
                                            String metadataCollectionId,
                                            String classificationName,
                                            String className,
                                            String methodName) throws InvalidParameterException {

        boolean updateAllowed = true;

        Keyword INSTANCE_PROVENANCE_TYPE;
        Keyword METADATA_COLLECTION_ID;
        Keyword REPLICATED_BY;
        if (classificationName != null) {
            String namespace = ClassificationMapping.getNamespaceForClassification(classificationName);
            INSTANCE_PROVENANCE_TYPE = Keyword.intern(namespace, Keywords.INSTANCE_PROVENANCE_TYPE.getName());
            METADATA_COLLECTION_ID = Keyword.intern(namespace, Keywords.METADATA_COLLECTION_ID.getName());
            REPLICATED_BY = Keyword.intern(namespace, Keywords.REPLICATED_BY.getName());
        } else {
            INSTANCE_PROVENANCE_TYPE = Keywords.INSTANCE_PROVENANCE_TYPE;
            METADATA_COLLECTION_ID = Keywords.METADATA_COLLECTION_ID;
            REPLICATED_BY = Keywords.REPLICATED_BY;
        }

        Integer instanceProvenance = (Integer) instance.valAt(INSTANCE_PROVENANCE_TYPE);
        if (instanceProvenance != null) {
            if (instanceProvenance == InstanceProvenanceType.LOCAL_COHORT.getOrdinal()) {
                String entityHome = (String) instance.valAt(METADATA_COLLECTION_ID);
                if (entityHome != null && !entityHome.equals(metadataCollectionId)) {
                    updateAllowed = false;
                }
            } else if (instanceProvenance == InstanceProvenanceType.EXTERNAL_SOURCE.getOrdinal()) {
                String replicatedBy = (String) instance.valAt(REPLICATED_BY);
                if (replicatedBy != null && !replicatedBy.equals(metadataCollectionId)) {
                    updateAllowed = false;
                }
            } else {
                updateAllowed = false;
            }
        } else {
            updateAllowed = false;
        }

        if (!updateAllowed) {
            throw new InvalidParameterException(XTDBErrorCode.INSTANCE_HOME_NOT_LOCAL.getMessageDefinition((String)instance.valAt(Keywords.METADATA_COLLECTION_ID),
                                                                                                           guid,
                                                                                                           metadataCollectionId),
                                                className,
                                                methodName,
                                                "instance");
        }

    }

    /**
     * Validates an instance status where null is not allowed.
     *
     * @param guid unique identifier for the metadata instance being updated
     * @param propertyName name of the required property
     * @param propertyValue value of the required property
     * @param className class called
     * @param methodName method called
     * @throws InvalidParameterException invalid parameter
     */
    public static void requiredProperty(String guid,
                                        String propertyName,
                                        Object propertyValue,
                                        String className,
                                        String methodName) throws InvalidParameterException {
        if (propertyValue == null) {
            throw new InvalidParameterException(XTDBErrorCode.NULL_REQUIRED_PROPERTY.getMessageDefinition(propertyName, guid),
                                                className,
                                                methodName,
                                                propertyName);
        }
    }

    /**
     * Verify that the instance retrieved from the repository has a valid instance type.
     *
     * @param instance the retrieved instance
     * @param className calling class
     * @param methodName calling method
     * @throws RepositoryErrorException logic error in the repository corrupted instance
     * @throws IOException on any error deserializing the type information
     */
    public static void instanceType(IPersistentMap instance,
                                    String className,
                                    String methodName) throws RepositoryErrorException, IOException {
        if (instance != null) {
            InstanceType instanceType = InstanceAuditHeaderMapping.getTypeFromInstance(instance, null);
            if (instanceType != null) {
                knownType(instance, instanceType.getTypeDefGUID(), instanceType.getTypeDefName(), className, methodName);
            } else {
                throw new RepositoryErrorException(
                        XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                       "type is null",
                                                                                       instance.toString()),
                        className,
                        methodName);
            }
        } else {
            throw new RepositoryErrorException(XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition("<null>",
                                                                                                              "instance is null",
                                                                                                              "<null>"),
                                               className,
                                               methodName);
        }
    }

    /**
     * Validates an instance status where null is permissible.
     *
     * @param instanceStatus initial status value
     * @param typeDef type of the instance
     * @param className calling class
     * @param methodName method called
     * @throws StatusNotSupportedException the initial status is invalid for this type
     */
    public static void instanceStatus(Integer instanceStatus,
                                      TypeDef typeDef,
                                      String className,
                                      String methodName) throws StatusNotSupportedException {
        if (typeDef == null) {
            throw new StatusNotSupportedException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                                  className,
                                                  methodName);
        } else {
            String typeDefName = typeDef.getName();
            if (instanceStatus == InstanceStatus.DELETED.getOrdinal()) {
                throw new StatusNotSupportedException(XTDBErrorCode.BAD_INSTANCE_STATUS.getMessageDefinition(InstanceStatus.DELETED.getName(),
                                                                                                             typeDefName),
                                                      className,
                                                      methodName);
            } else {
                List<InstanceStatus> validStatuses = typeDef.getValidInstanceStatusList();
                for (InstanceStatus validStatus : validStatuses) {
                    if (instanceStatus == validStatus.getOrdinal()) {
                        return;
                    }
                }
                throw new StatusNotSupportedException(XTDBErrorCode.BAD_INSTANCE_STATUS.getMessageDefinition(instanceStatus.toString(),
                                                                                                             typeDefName),
                                                      className,
                                                      methodName);
            }
        }
    }

    /**
     * Return boolean indicating whether the AttributeTypeDef/TypeDef is known, either as an open type, or one defined
     * by one or more of the members of the cohort.
     *
     * @param typeGUID unique identifier of the type
     * @param typeName unique name of the type
     * @throws RepositoryErrorException if the type is not known
     */
    private static void knownType(IPersistentMap instance,
                                  String typeGUID,
                                  String typeName,
                                  String className,
                                  String methodName) throws RepositoryErrorException {
        validTypeId(instance, typeGUID, typeName, className, methodName);
        TypeDef typeDef = TypeDefCache.getTypeDef(typeGUID);

        if (typeDef == null) {
            AttributeTypeDef attributeTypeDef = TypeDefCache.getAttributeTypeDef(typeGUID);
            if (attributeTypeDef == null) {
                throw new RepositoryErrorException(XTDBErrorCode.INACTIVE_INSTANCE_TYPE.getMessageDefinition(
                        AbstractTransactionFunction.getGUID(instance),
                        typeName,
                        typeGUID),
                        className,
                        methodName);
            }
        }
    }

    /**
     * Validate that a classification is valid for the entity.
     *
     * @param classificationName unique name for a classification type
     * @param classificationProperties properties to test
     * @param className calling class
     * @param methodName method receiving the call
     * @throws PropertyErrorException classification name is null
     * @throws RepositoryErrorException on any other error
     */
    public static void classificationProperties(String classificationName,
                                                InstanceProperties classificationProperties,
                                                String className,
                                                String methodName)
            throws PropertyErrorException, RepositoryErrorException {
        TypeDef classificationTypeDef = TypeDefCache.getTypeDefByName(classificationName);
        if (classificationTypeDef != null) {
            propertiesForType(classificationTypeDef, classificationProperties, className, methodName);
        } else {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               className,
                                               methodName);
        }
    }

    /**
     * Validate that a classification is valid for the entity.
     *
     * @param instance metadata instance with classifications
     * @param entityTypeName name of entity type
     * @param className calling class
     * @param methodName method receiving the call
     * @throws InvalidParameterException classification name is null
     * @throws ClassificationErrorException the classification is invalid for this entity
     * @throws PropertyErrorException the classification's properties are invalid for its type
     * @throws IOException on any error deserializing values
     * @throws RepositoryErrorException on any other error
     */
    public static void classificationList(IPersistentMap instance,
                                          String entityTypeName,
                                          String className,
                                          String methodName)
            throws InvalidParameterException, ClassificationErrorException, PropertyErrorException, RepositoryErrorException, IOException {

        List<Classification> classifications = ClassificationMapping.fromMap(instance);
        if (classifications != null) {
            for (Classification classification : classifications) {
                if (classification != null) {
                    classification(classification.getName(), entityTypeName, className, methodName);
                    propertiesForType(TypeDefCache.getTypeDefByName(classification.getName()), classification.getProperties(), className, methodName);
                } else {
                    throw new InvalidParameterException(
                            XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                           "null classification name",
                                                                                           instance.toString()),
                            className,
                            methodName,
                            "classificationName");
                }
            }
        }
    }

    /**
     * Test that the supplied relationship is valid.
     *
     * @param relationship relationship to test
     * @throws RepositoryErrorException if the relationship is invalid
     */
    private static void validRelationship(IPersistentMap relationship,
                                          String className,
                                          String methodName) throws RepositoryErrorException {

        // 1. Validate the instance
        validInstance(relationship, className, methodName, false);
        // 2. Validate the ends of the relationship
        IPersistentVector proxies = (IPersistentVector) relationship.valAt(Keywords.ENTITY_PROXIES);
        if (proxies == null || proxies.length() != 2) {
            throw new RepositoryErrorException(
                    XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(relationship),
                                                                                   "one or both ends are null",
                                                                                   relationship.toString()),
                    className,
                    methodName);
        }
    }

    /**
     * Test that the supplied instance is valid.
     *
     * @param instance instance to test
     * @param className calling class
     * @param methodName calling method
     * @param fromStore is the entity from the store?
     * @throws RepositoryErrorException if the entity is invalid
     */
    private static void validInstance(IPersistentMap instance,
                                      String className,
                                      String methodName,
                                      boolean fromStore) throws RepositoryErrorException {
        if (instance == null) {
            throw new RepositoryErrorException(XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition("<null>",
                                                                                                              "instance is null",
                                                                                                              "<null>"),
                                               className,
                                               methodName);
        }

        InstanceType instanceType;
        try {
            instanceType = InstanceAuditHeaderMapping.getTypeFromInstance(instance, null);
        } catch (IOException e) {
            throw new RepositoryErrorException(
                    XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                   "type cannot be deserialized",
                                                                                   instance.toString()),
                    className,
                    methodName,
                    e);
        }

        if (instanceType == null) {
            throw new RepositoryErrorException(
                    XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                   "type is null",
                                                                                   instance.toString()),
                    className,
                    methodName);
        }

        validInstanceId(instance,
                instanceType.getTypeDefGUID(),
                instanceType.getTypeDefName(),
                instanceType.getTypeDefCategory(),
                className,
                methodName);

        if (!fromStore) {
            String homeMetadataCollectionId = AbstractTransactionFunction.getMetadataCollectionId(instance);
            if (homeMetadataCollectionId == null) {
                throw new RepositoryErrorException(
                        XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                       "metadataCollectionId is null",
                                                                                       instance.toString()),
                        className,
                        methodName);
            }
        }

    }

    /**
     * Verify that the identifiers for an instance are correct.
     *
     * @param instance metadata instance being verified
     * @param typeDefGUID unique identifier for the type
     * @param typeDefName unique name for the type
     * @param category expected category of the instance
     * @param className calling class
     * @param methodName calling method
     * @throws RepositoryErrorException if the instance identifiers are invalid
     */
    private static void validInstanceId(IPersistentMap instance,
                                        String typeDefGUID,
                                        String typeDefName,
                                        TypeDefCategory category,
                                        String className,
                                        String methodName) throws RepositoryErrorException {
        validTypeDefId(instance, typeDefGUID, typeDefName, category, className, methodName);
        if (AbstractTransactionFunction.getGUID(instance) == null) {
            throw new RepositoryErrorException(XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition("<null>",
                                                                                                              "GUID is null",
                                                                                                              "<null>"),
                                               className,
                                               methodName);
        }
    }

    /**
     * Verify whether the TypeDef identifiers are valid or not.
     *
     * @param instance metadata instance being verified
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     * @param category category of TypeDef
     * @param className calling class
     * @param methodName calling method
     * @throws RepositoryErrorException if the instance identifiers are invalid
     */
    private static void validTypeDefId(IPersistentMap instance,
                                       String typeDefGUID,
                                       String typeDefName,
                                       TypeDefCategory category,
                                       String className,
                                       String methodName) throws RepositoryErrorException {

        validTypeId(instance, typeDefGUID, typeDefName, className, methodName);

        if (category == null) {
            throw new RepositoryErrorException(
                    XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                   "typeDefCategory is null",
                                                                                   instance.toString()),
                    className,
                    methodName);
        }

        TypeDef typeDef = TypeDefCache.getTypeDef(typeDefGUID);

        if (typeDef != null) {
            TypeDefCategory knownTypeDefCategory = typeDef.getCategory();
            if (category.getOrdinal() != knownTypeDefCategory.getOrdinal()) {
                throw new RepositoryErrorException(
                        XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                       "typeDefCategory is unknown",
                                                                                       instance.toString()),
                        className,
                        methodName);
            }
        }

    }

    /**
     * Verify whether the (Attribute)TypeDef identifiers are valid or not.
     *
     * @param instance metadata instance being verified
     * @param typeGUID unique identifier of the TypeDef
     * @param typeName unique name of the TypeDef
     * @param className calling class
     * @param methodName calling method
     * @throws RepositoryErrorException if the typeDef identifiers are invalid
     */
    private static void validTypeId(IPersistentMap instance,
                                    String typeGUID,
                                    String typeName,
                                    String className,
                                    String methodName) throws RepositoryErrorException {
        if (typeName == null) {
            throw new RepositoryErrorException(
                    XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                   "typeDefName is null",
                                                                                   instance.toString()),
                    className,
                    methodName);
        }

        if (typeGUID == null) {
            throw new RepositoryErrorException(
                    XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                   "typeDefGUID is null",
                                                                                   instance.toString()),
                    className,
                    methodName);
        }

        TypeDef typeDef = TypeDefCache.getTypeDef(typeGUID);

        if (typeDef != null) {
            if (!typeGUID.equals(typeDef.getGUID())) {
                throw new RepositoryErrorException(
                        XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                       "typeDefGUID does not match",
                                                                                       instance.toString()),
                        className,
                        methodName);
            }
        } else {

            // If the TypeDef is unknown, see if it is an AttributeTypeDef
            AttributeTypeDef attributeTypeDef = TypeDefCache.getAttributeTypeDef(typeGUID);

            if (attributeTypeDef == null) {
                throw new RepositoryErrorException(
                        XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                       "typeDef is unknown",
                                                                                       instance.toString()),
                        className,
                        methodName);
            } else if (!typeGUID.equals(attributeTypeDef.getGUID())) {
                    /*
                     * The requested guid does not equal the stored one.
                     */
                throw new RepositoryErrorException(
                        XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(AbstractTransactionFunction.getGUID(instance),
                                                                                       "attributeTypeDefGUID does not match",
                                                                                       instance.toString()),
                        className,
                        methodName);
            }

        }
    }

    /**
     * Validate that the properties for a metadata instance match its TypeDef.
     *
     * @param typeDef type information to validate against
     * @param properties proposed properties for instance
     * @param className calling class
     * @param methodName method receiving the call
     * @throws PropertyErrorException invalid property
     * @throws RepositoryErrorException on any other error
     */
    public static void propertiesForType(TypeDef typeDef,
                                         InstanceProperties properties,
                                         String className,
                                         String methodName)
            throws RepositoryErrorException, PropertyErrorException {

        if (properties == null || properties.getInstanceProperties() == null || properties.getInstanceProperties().isEmpty()) {
            return;
        }

        if (typeDef == null) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               className,
                                               methodName);
        }

        String typeDefCategoryName = null;
        String typeDefName = typeDef.getName();

        if (typeDef.getCategory() != null) {
            typeDefCategoryName = typeDef.getCategory().getName();
        }

        Map<String, PropertyKeywords> propertyKeywords = TypeDefCache.getAllPropertyKeywordsForTypeDef(typeDef.getGUID());

        if (propertyKeywords.isEmpty()) {
            // Properties have been provided but there are none in the type definition...
            throw new PropertyErrorException(XTDBErrorCode.NO_PROPERTIES_FOR_TYPE.getMessageDefinition(typeDefCategoryName,
                                                                                                       typeDefName),
                                             className,
                                             methodName);
        }

        // Make sure each proposed property matches the typeDef
        Iterator<?> propertyList = properties.getPropertyNames();

        while (propertyList.hasNext()) {
            String propertyName = propertyList.next().toString();

            if (propertyName == null) {
                throw new PropertyErrorException(XTDBErrorCode.NULL_PROPERTY_NAME_FOR_INSTANCE.getMessageDefinition(),
                                                 className,
                                                 methodName);
            }

            if (!propertyKeywords.containsKey(propertyName)) {
                throw new PropertyErrorException(XTDBErrorCode.BAD_PROPERTY_FOR_TYPE.getMessageDefinition(propertyName,
                                                                                                          typeDefCategoryName,
                                                                                                          typeDefName),
                                                 className,
                                                 methodName);
            }

            TypeDefAttribute typeDefAttribute = propertyKeywords.get(propertyName).getAttribute();
            AttributeTypeDefCategory propertyDefinitionType = AttributeTypeDefCategory.UNKNOWN_DEF;
            AttributeTypeDef attributeTypeDef = typeDefAttribute.getAttributeType();

            if (attributeTypeDef != null) {
                propertyDefinitionType = attributeTypeDef.getCategory();
            }

            InstancePropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue == null) {
                throw new PropertyErrorException(XTDBErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE.getMessageDefinition(propertyName),
                                                 className,
                                                 methodName);
            }

            InstancePropertyCategory propertyType = propertyValue.getInstancePropertyCategory();

            if (propertyType == null) {
                throw new PropertyErrorException(XTDBErrorCode.NULL_PROPERTY_TYPE_FOR_INSTANCE.getMessageDefinition(propertyName),
                                                 className,
                                                 methodName);
            }

            boolean validPropertyType = false;
            String actualPropertyTypeName = propertyType.getName();
            String validPropertyTypeName = propertyDefinitionType.getName();

            switch (propertyType) {
                case PRIMITIVE:
                    if (propertyDefinitionType == AttributeTypeDefCategory.PRIMITIVE) {
                        // Ensure primitive definition category is a perfect match...
                        PrimitivePropertyValue primPropertyValue = (PrimitivePropertyValue)propertyValue;
                        PrimitiveDefCategory primPropertyCategory = primPropertyValue.getPrimitiveDefCategory();
                        PrimitiveDef expectedAttributeDef = (PrimitiveDef) attributeTypeDef;
                        PrimitiveDefCategory expectedAttributeDefCategory = expectedAttributeDef.getPrimitiveDefCategory();
                        if (primPropertyCategory == expectedAttributeDefCategory) {
                            validPropertyType = true;
                        } else {
                            actualPropertyTypeName = primPropertyCategory.getName();
                            validPropertyTypeName = expectedAttributeDefCategory.getName();
                        }
                    } else if (propertyDefinitionType == AttributeTypeDefCategory.UNKNOWN_DEF) {
                        /*
                         * This property definition type may have been adopted above due to the
                         * attributeTypeDef being null. Permit primitive definition category to
                         * be a sloppy match...
                         */
                        validPropertyType = true;
                    }
                    break;
                case ENUM:
                    if (propertyDefinitionType == AttributeTypeDefCategory.ENUM_DEF) {
                        validPropertyType = true;
                    }
                    break;
                case MAP:
                case STRUCT:
                case ARRAY:
                    if (propertyDefinitionType == AttributeTypeDefCategory.COLLECTION) {
                        validPropertyType = true;
                    }
                    break;
                case UNKNOWN:
                default:
                    // Nothing to do, leave the property validity as false
                    break;
            }

            if (!validPropertyType) {
                throw new PropertyErrorException(XTDBErrorCode.BAD_PROPERTY_TYPE.getMessageDefinition(propertyName,
                                                                                                      actualPropertyTypeName,
                                                                                                      typeDefCategoryName,
                                                                                                      typeDefName,
                                                                                                      validPropertyTypeName),
                                                 className,
                                                 methodName);
            }
        }
    }

    /**
     * Validate that a classification name is not null.
     *
     * @param classificationName unique name for a classification type
     * @param entityTypeName unique name for the type of entity
     * @param className calling class
     * @param methodName method receiving the call
     * @throws InvalidParameterException  classification name is null or invalid
     */
    private static void validateClassificationName(String classificationName,
                                                      String entityTypeName,
                                                      String className,
                                                      String methodName) throws InvalidParameterException {
        if (classificationName == null) {
            throw new InvalidParameterException(XTDBErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition("<null>",
                                                                                                                     entityTypeName),
                                                className,
                                                methodName,
                                                "classificationName");
        }
        if (TypeDefCache.getTypeDefByName(classificationName) == null) {
            throw new InvalidParameterException(XTDBErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(classificationName,
                                                                                                                     entityTypeName),
                                                className,
                                                methodName,
                                                "classificationName");
        }
    }

    /**
     * Validate that the types of the two ends of a relationship match the relationship's TypeDef.
     *
     * @param entityOneGUID unique identifier of end one
     * @param entityTwoGUID unique identifier of end two
     * @param entityOneProxy content of end one
     * @param entityTwoProxy content of end two
     * @param typeDefGUID typeDefGUID for the relationship
     * @param className of the calling class
     * @param methodName of the calling method
     * @throws InvalidParameterException types do not align
     * @throws RepositoryErrorException on any other error
     */
    public static void relationshipEnds(String entityOneGUID,
                                        String entityTwoGUID,
                                        IPersistentMap entityOneProxy,
                                        IPersistentMap entityTwoProxy,
                                        String typeDefGUID,
                                        String className,
                                        String methodName) throws InvalidParameterException, RepositoryErrorException {

        TypeDef typeDef = TypeDefCache.getTypeDef(typeDefGUID);

        if ((entityOneProxy != null) && (entityTwoProxy != null) && (typeDef != null)) {

            try {
                RelationshipDef relationshipDef = (RelationshipDef) typeDef;
                RelationshipEndDef entityOneEndDef;
                RelationshipEndDef entityTwoEndDef;
                TypeDefLink entityOneTypeDef = null;
                TypeDefLink entityTwoTypeDef = null;
                String entityOneTypeDefName = null;
                String entityTwoTypeDefName = null;
                InstanceType entityOneType;
                InstanceType entityTwoType;
                String entityOneTypeName = null;
                String entityTwoTypeName = null;

                entityOneEndDef = relationshipDef.getEndDef1();
                entityTwoEndDef = relationshipDef.getEndDef2();

                if ((entityOneEndDef != null) && (entityTwoEndDef != null)) {
                    entityOneTypeDef = entityOneEndDef.getEntityType();
                    entityTwoTypeDef = entityTwoEndDef.getEntityType();
                }

                if ((entityOneTypeDef != null) && (entityTwoTypeDef != null)) {
                    entityOneTypeDefName = entityOneTypeDef.getName();
                    entityTwoTypeDefName = entityTwoTypeDef.getName();
                }

                // At this point we know the expected types of the ends, now compare to the relationship.

                entityOneType = InstanceAuditHeaderMapping.getTypeFromInstance(entityOneProxy, null);
                entityTwoType = InstanceAuditHeaderMapping.getTypeFromInstance(entityTwoProxy, null);

                if ((entityOneType != null) && (entityTwoType != null)) {
                    entityOneTypeName = entityOneType.getTypeDefName();
                    entityTwoTypeName = entityTwoType.getTypeDefName();
                }

                if (TypeDefCache.isTypeOf(entityOneTypeName, entityOneTypeDefName)
                        && TypeDefCache.isTypeOf(entityTwoTypeName, entityTwoTypeDefName)) {
                    return;
                }

                throw new InvalidParameterException(XTDBErrorCode.INVALID_RELATIONSHIP_ENDS.getMessageDefinition(typeDef.getName(),
                                                                                                                 entityOneGUID,
                                                                                                                 entityOneTypeName,
                                                                                                                 entityOneTypeDefName,
                                                                                                                 entityTwoGUID,
                                                                                                                 entityTwoTypeName,
                                                                                                                 entityTwoTypeDefName),
                                                    className,
                                                    methodName,
                                                    "relationship.End");
            } catch (InvalidParameterException error) {
                throw error;
            } catch (Exception error) {
                throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                                   className,
                                                   methodName,
                                                   error);
            }
        } else {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               className,
                                               methodName);
        }
    }

}
