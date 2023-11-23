/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.ClassificationMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityProxyMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.InstanceAuditHeaderMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

/**
 * Transaction function for adding a reference copy classification.
 */
public abstract class PurgeClassificationReferenceCopy extends AbstractTransactionFunction {

    protected final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param className name of the implementing class
     * @param methodName name of the implemented transaction method
     * @param txId the transaction ID of this function invocation
     * @param existing the existing entity in XT, if any
     * @param entity the entity against which to apply the classification
     * @param entityGUID the GUID of the entity from which we've been asked to declassify
     * @param classification the classification to persist as a reference copy
     * @param homeMetadataCollectionId the metadataCollectionId of the repository where the transaction is running
     * @throws Exception on any error
     */
    public PurgeClassificationReferenceCopy(String className,
                                            String methodName,
                                            Long txId,
                                            PersistentHashMap existing,
                                            PersistentHashMap entity,
                                            String entityGUID,
                                            Classification classification,
                                            String homeMetadataCollectionId)
            throws Exception {

        try {

            IPersistentMap docToUpdate = null;

            // Before anything, validate the classification we are being asked to persist as a reference
            // copy is not a homed classification
            if (homeMetadataCollectionId.equals(classification.getMetadataCollectionId())) {
                throw new PropertyErrorException(
                        XTDBErrorCode.CLASSIFICATION_HOME_COLLECTION_REFERENCE.getMessageDefinition(entityGUID, homeMetadataCollectionId), className, methodName);
            }

            // As long as the classification is actually a reference copy itself...
            if (existing != null) {
                // If there is an existing entity, whether it is a reference copy or homed, use it
                docToUpdate = existing;
            } else if (!homeMetadataCollectionId.equals(getMetadataCollectionId(entity)) || EntityProxyMapping.isOnlyAProxy(entity)) {
                // Otherwise, if the entity against which to store the classification is either itself a reference copy,
                // or it is an entity proxy (reference copy or homed), store everything together as a reference copy.
                docToUpdate = entity;
            }

            if (docToUpdate != null) {
                try {
                    docToUpdate = ClassificationMapping.removeFromMap(docToUpdate, classification.getName());
                } catch (ClassificationErrorException e) {
                    // Do nothing: this simply means the repository did not have the classification reference copy stored
                    // anyway, so nothing to remove (no-op)
                    docToUpdate = null;
                }
            }
            xtdbDoc = docToUpdate;

        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Construct the transaction function specific to the subclass.
     * @param className canonical name of the subclass for which to construct the transaction function
     * @return the transaction function
     */
    protected static String getTxFn(String className) {
        return "" +
                "(fn [ctx eid e rcc mid] " +
                "    (let [db (xtdb.api/db ctx)" +
                "          tx-id (:tx-id db)" +
                "          existing (xtdb.api/entity db eid)" +
                "          updated (.doc (" + className + ". tx-id existing e eid rcc mid))" +
                // Retrieve the metadata collection ID of the updated entity (if there is one)
                "          nmid (when (some? updated)" +
                "                     (get updated :" + InstanceAuditHeaderMapping.METADATA_COLLECTION_ID + "))" +
                getTxnTimeCalculation("updated") + "]" +
                // Only proceed if there is some update to apply
                "         (when (some? nmid)" +
                "          (if (= mid nmid)" +
                // If the entity is homed in this repository, apply it as a normal put
                "            [[:xtdb.api/put updated txt]]" +
                // If we are updating a reference copy entity, instead delegate to the reference copy transaction function
                "            [[:xtdb.api/fn " + SaveEntityReferenceCopy.FUNCTION_NAME + " eid updated mid]]))))";
    }

}
