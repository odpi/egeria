/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.InstanceAuditHeaderMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for soft-deleting an entity.
 */
public class DeleteEntity extends DeleteInstance {

    private static final Logger log = LoggerFactory.getLogger(DeleteEntity.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "deleteEntity");
    private static final String CLASS_NAME = DeleteEntity.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();

    // Query to retrieve all non-deleted relationships that point to this entity,
    // (mixed quoting is necessary to ensure the eid is evaluated, so it becomes first in the join
    // ordering of the query for performance reasons)
    private static final String RELN_QUERY = "" +
            "{:find [(quote r)]" +
            "  :where [[(quote r) :" + RelationshipMapping.ENTITY_PROXIES + " eid]" +
            "          [(quote r) :" + InstanceAuditHeaderMapping.TYPE_DEF_CATEGORY + " " + TypeDefCategory.RELATIONSHIP_DEF.getOrdinal() + "]" +
            "          [(quote r) :" + InstanceAuditHeaderMapping.CURRENT_STATUS + " (quote s)]" +
            "          [(quote (not= s " + InstanceStatus.DELETED.getOrdinal() + "))]]}";

    private static final String FN = "" +
            "(fn [ctx eid user] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          relationships (xtdb.api/q db " + RELN_QUERY + ")" +
            "          existing (xtdb.api/entity db eid)" +
            "          deleted (.doc (" + DeleteEntity.class.getCanonicalName() + ". tx-id existing user eid))" +
            getTxnTimeCalculation("deleted") + "]" +
            // For each of the relationships that was found, check the metadataCollectionId of it to determine
            // whether to delete the relationship (homed in this repo, same as entity) or to purge it (reference copy)
            // by delegating to the appropriate transaction function for those operations
            //"         (conj (vec (for [[rid mid] relationships]" +
            "         (conj (vec (for [[rid] relationships]" +
            "                     (let [home (get existing :" + InstanceAuditHeaderMapping.METADATA_COLLECTION_ID + ")" +
            "                           mid (xtdb.api/pull db [:" + InstanceAuditHeaderMapping.METADATA_COLLECTION_ID + "] rid)]" +
            "                          (if (= home (:" + InstanceAuditHeaderMapping.METADATA_COLLECTION_ID + " mid))" +
            "                              [:xtdb.api/fn " + DeleteRelationship.FUNCTION_NAME + " rid user]" +
            "                              [:xtdb.api/fn " + PurgeRelationship.FUNCTION_NAME + " rid true]))))" +
            // And of course also persist the soft-deleted entity itself as part of this transaction
            "               [:xtdb.api/put deleted txt])))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to update
     * @param userId doing the deletion
     * @param obsoleteEntityGUID of the entity to delete
     * @throws Exception on any error
     */
    public DeleteEntity(Long txId,
                        PersistentHashMap existing,
                        String userId,
                        String obsoleteEntityGUID)
            throws Exception {

        try {
            if (existing == null) {
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        obsoleteEntityGUID), CLASS_NAME, METHOD_NAME);
            } else {
                TxnValidations.nonProxyEntity(existing, obsoleteEntityGUID, CLASS_NAME, METHOD_NAME);
                TxnValidations.entityFromStore(obsoleteEntityGUID, existing, CLASS_NAME, METHOD_NAME);
                TxnValidations.instanceIsNotDeleted(existing, obsoleteEntityGUID, CLASS_NAME, METHOD_NAME);
                xtdbDoc = deleteInstance(userId, existing);
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Soft-delete the specified entity by pushing down the transaction.
     * @param xtdb connectivity
     * @param userId doing the deletion
     * @param entityGUID of the entity to be deleted
     * @return the resulting deleted entity
     * @throws EntityNotKnownException if the entity cannot be found
     * @throws InvalidParameterException if the relationship exists but is already soft-deleted
     * @throws RepositoryErrorException on any other error
     */
    public static EntityDetail transact(XTDBOMRSRepositoryConnector xtdb,
                                        String userId,
                                        String entityGUID)
            throws EntityNotKnownException, InvalidParameterException, RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(entityGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId);
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            return xtdb.getResultingEntity(docId, results, METHOD_NAME);
        } catch (EntityNotKnownException | InvalidParameterException | RepositoryErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               CLASS_NAME,
                                               METHOD_NAME,
                                               e);
        }
    }

    /**
     * Interface that returns the updated document to write-back from the transaction.
     * @return IPersistentMap giving the updated document in its entirety
     */
    public IPersistentMap doc() {
        log.debug("deleteEntity being persisted: {}", xtdbDoc);
        return xtdbDoc;
    }

    /**
     * Create the transaction function within XTDB.
     * @param tx the transaction through which to create this function
     */
    public static void create(Transaction.Builder tx) {
        createTransactionFunction(tx, FUNCTION_NAME, FN);
    }

}
