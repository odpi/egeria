/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.IPersistentMap;
import clojure.lang.PersistentHashMap;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.TypeDefCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.ClassificationMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.InstanceAuditHeaderMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;

public abstract class ClassifyEntity extends AbstractTransactionFunction {

    protected final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param className name of the implementing class
     * @param methodName name of the implemented transaction method
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to update
     * @param proxy XTDB document to update, if existing is empty
     * @param userId doing the update
     * @param entityGUID of the entity to update
     * @param metadataCollectionId of the metadata collection in which the transaction is running
     * @param classificationName name of the classification
     * @param externalSourceGUID unique identifier for the external source
     * @param externalSourceName unique name for the external source
     * @param classificationOrigin source of the classification (assigned or propagated)
     * @param classificationOriginGUID unique identifier of the entity that propagated the classification (if propagated)
     * @param properties the properties to set on the classification
     * @throws Exception on any error
     */
    protected ClassifyEntity(String className,
                             String methodName,
                             Long txId,
                             PersistentHashMap existing,
                             PersistentHashMap proxy,
                             String userId,
                             String entityGUID,
                             String metadataCollectionId,
                             String classificationName,
                             String externalSourceGUID,
                             String externalSourceName,
                             ClassificationOrigin classificationOrigin,
                             String classificationOriginGUID,
                             InstanceProperties properties)
            throws Exception {

        try {
            PersistentHashMap toUpdate;
            if (existing != null) {
                // If we found an existing entity with this GUID, use it (not the provided proxy)
                toUpdate = existing;
            } else if (proxy != null) {
                // Otherwise, fallback to the proxy we've been asked to create
                toUpdate = proxy;
            } else {
                // And in case that was not provided (older classifyEntity method),
                // exit out with the not found exception
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        entityGUID), className, methodName);
            }

            TxnValidations.entityFromStore(entityGUID, toUpdate, className, methodName);
            TxnValidations.instanceIsNotDeleted(toUpdate, entityGUID, className, methodName);
            TxnValidations.instanceType(toUpdate, className, methodName);
            InstanceType entityType = InstanceAuditHeaderMapping.getTypeFromInstance(toUpdate, null);
            TxnValidations.classification(classificationName, entityType.getTypeDefName(), className, methodName);

            String entityTypeDefGUID = getTypeDefGUID(toUpdate);
            TypeDef entityTypeDef = TypeDefCache.getTypeDef(entityTypeDefGUID);
            String entityTypeDefName = entityTypeDef.getName();

            Classification newClassification;
            try {
                TxnValidations.classificationProperties(classificationName, properties, className, methodName);
                if (externalSourceGUID == null) {
                    newClassification = getNewClassification(
                            metadataCollectionId,
                            null,
                            InstanceProvenanceType.LOCAL_COHORT,
                            userId,
                            classificationName,
                            entityTypeDefName,
                            classificationOrigin == null ? ClassificationOrigin.ASSIGNED : classificationOrigin,
                            classificationOriginGUID,
                            properties);
                } else {
                    newClassification = getNewClassification(
                            externalSourceGUID,
                            externalSourceName,
                            InstanceProvenanceType.EXTERNAL_SOURCE,
                            userId,
                            classificationName,
                            entityTypeDefName,
                            classificationOrigin == null ? ClassificationOrigin.ASSIGNED : classificationOrigin,
                            classificationOriginGUID,
                            properties);
                    newClassification.setMetadataCollectionName(externalSourceName);
                    newClassification.setReplicatedBy(metadataCollectionId);
                }
            } catch (TypeErrorException e) {
                throw new ClassificationErrorException(XTDBErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(
                        classificationName, entityTypeDefName), className, methodName, e);
            }

            xtdbDoc = ClassificationMapping.addToMap(toUpdate, newClassification);

        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Return a complete, new classification.
     * @param metadataCollectionId unique identifier for the home metadata collection
     * @param metadataCollectionName unique name for the home metadata collection
     * @param provenanceType origin of the classification
     * @param userName name of the creator
     * @param classificationTypeName name of the type definition for the classification
     * @param entityTypeName name of the type definition for the entity the classification will be applied to
     * @param classificationOrigin indicating whether this was explicitly assigned or propagated
     * @param classificationOriginGUID if propagated, this is the GUID of the origin
     * @param properties properties for the classification
     * @return Classification
     * @throws TypeErrorException if the type names are not recognized or are not valid relative to each other
     */
    private static Classification getNewClassification(String metadataCollectionId,
                                                       String metadataCollectionName,
                                                       InstanceProvenanceType provenanceType,
                                                       String userName,
                                                       String classificationTypeName,
                                                       String entityTypeName,
                                                       ClassificationOrigin classificationOrigin,
                                                       String classificationOriginGUID,
                                                       InstanceProperties properties) throws TypeErrorException {

        final String methodName = "getNewClassification";

        if (TypeDefCache.isValidClassificationForEntity(classificationTypeName, entityTypeName)) {

            Classification classification = new Classification();

            classification.setHeaderVersion(InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION);
            classification.setInstanceProvenanceType(provenanceType);
            classification.setMetadataCollectionId(metadataCollectionId);
            classification.setMetadataCollectionName(metadataCollectionName);
            classification.setName(classificationTypeName);
            classification.setCreateTime(new Date());
            classification.setCreatedBy(userName);
            classification.setVersion(1L);
            classification.setType(TypeDefCache.getInstanceType(TypeDefCategory.CLASSIFICATION_DEF, classificationTypeName));
            classification.setStatus(TypeDefCache.getInitialStatus(classificationTypeName));

            classification.setClassificationOrigin(classificationOrigin);
            classification.setClassificationOriginGUID(classificationOriginGUID);
            classification.setProperties(properties);

            return classification;

        } else {
            throw new TypeErrorException(XTDBErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(
                    classificationTypeName, entityTypeName),
                                         ClassifyEntityProxy.class.getName(),
                                         methodName);
        }

    }

}
