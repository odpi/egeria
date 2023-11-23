/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;

/**
 * Transaction function for updating InstanceProperties on a metadata instance.
 */
public abstract class UpdateEntityClassification extends UpdateInstanceProperties {

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "updateEntityClassification");
    public static final String CLASS_NAME = UpdateEntityClassification.class.getName();
    public static final String METHOD_NAME = FUNCTION_NAME.toString();

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
     * @param classificationName of the classification to update
     * @param properties to apply to the classification
     * @throws Exception on any error
     */
    public UpdateEntityClassification(String className,
                                      String methodName,
                                      Long txId,
                                      PersistentHashMap existing,
                                      PersistentHashMap proxy,
                                      String userId,
                                      String entityGUID,
                                      String metadataCollectionId,
                                      String classificationName,
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
                // And in case that was not provided (older updateEntityClassification method),
                // exit out with the not found exception
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        entityGUID), className, methodName);
            }
            TxnValidations.entityFromStore(entityGUID, toUpdate, className, methodName);
            TxnValidations.instanceIsNotDeleted(toUpdate, entityGUID, className, methodName);
            TxnValidations.instanceCanBeUpdated(toUpdate, entityGUID, metadataCollectionId, classificationName, className, methodName);
            xtdbDoc = updateInstanceProperties(userId, toUpdate, properties, classificationName);
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

}
