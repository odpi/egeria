/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.ClassificationMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntitySummaryMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.ICursor;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

import java.util.Iterator;

/**
 * Transaction function for updating InstanceProperties on a metadata instance.
 */
public class UndoEntityUpdate extends UndoInstanceUpdate {

    private static final Logger log = LoggerFactory.getLogger(UpdateEntityProperties.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "undoEntityUpdate");
    private static final String CLASS_NAME = UndoEntityUpdate.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx eid user mid] " +
            "    (with-open [history (xtdb.api/open-entity-history (xtdb.api/db ctx) eid :desc {:with-docs? true})]" +
            "      (let [tx-id (:xtdb.api/tx-id (xtdb.api/indexing-tx ctx))" +
            "            updated (.doc (" + UndoEntityUpdate.class.getCanonicalName() + ". tx-id history user eid mid))" +
            getTxnTimeCalculation("updated") + "]" +
            "           [[:xtdb.api/put updated txt]])))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param cursor lazily-evaluated history of the entity
     * @param userId doing the update
     * @param entityGUID of the entity to update
     * @param metadataCollectionId of the metadata collection in which the transaction is running
     * @throws Exception on any error
     */
    public UndoEntityUpdate(Long txId,
                            ICursor<IPersistentMap> cursor,
                            String userId,
                            String entityGUID,
                            String metadataCollectionId)
            throws Exception {

        try {
            IPersistentVector history = getPreviousVersionFromCursor(cursor);
            if (history.length() == 0) {
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        entityGUID), this.getClass().getName(), METHOD_NAME);
            }
            IPersistentMap current = (IPersistentMap) history.nth(0);
            if (current == null) {
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        entityGUID), this.getClass().getName(), METHOD_NAME);
            } else if (history.length() == 2) {
                IPersistentMap previous = (IPersistentMap) history.nth(1);
                TxnValidations.nonProxyEntity(current, entityGUID, CLASS_NAME, METHOD_NAME);
                TxnValidations.instanceCanBeUpdated(current, entityGUID, metadataCollectionId, CLASS_NAME, METHOD_NAME);
                xtdbDoc = rollbackEntity(userId, current, previous);
            } else {
                // If there is no previous version, we will make this a no-op and retain the current version
                xtdbDoc = current;
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Undo the last update to the provided entity instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the update
     * @param entityGUID of the entity on which to undo the last update
     * @return EntityDetail of the rolled-back entity
     * @throws EntityNotKnownException if the entity cannot be found
     * @throws InvalidParameterException if the entity exists but cannot be rolled-back (deleted, reference copy, etc)
     * @throws RepositoryErrorException on any other error
     */
    public static EntityDetail transact(XTDBOMRSRepositoryConnector xtdb,
                                        String userId,
                                        String entityGUID)
            throws EntityNotKnownException, InvalidParameterException, RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(entityGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId, xtdb.getMetadataCollectionId());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            EntityDetail result = xtdb.getResultingEntity(docId, results, METHOD_NAME);
            OMRSRepositoryValidator repositoryValidator = xtdb.getRepositoryValidator();
            String repositoryName = xtdb.getRepositoryName();
            repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, result, METHOD_NAME);
            repositoryValidator.validateEntityIsNotDeleted(repositoryName, result, METHOD_NAME);
            return result;
        } catch (EntityNotKnownException | InvalidParameterException | RepositoryErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               UndoEntityUpdate.class.getName(),
                                               METHOD_NAME,
                                               e);
        }
    }

    /**
     * Interface that returns the updated document to write-back from the transaction.
     * @return IPersistentMap giving the updated document in its entirety
     */
    public IPersistentMap doc() {
        log.debug("Entity being persisted: {}", xtdbDoc);
        return xtdbDoc;
    }

    /**
     * Create the transaction function within XTDB.
     * @param tx transaction through which to create the function
     */
    public static void create(Transaction.Builder tx) {
        createTransactionFunction(tx, FUNCTION_NAME, FN);
    }

    /**
     * Rolls back the provided metadata instance to its previous form.
     *
     * @param userId doing the rollback
     * @param current the current (latest) representation of the metadata instance
     * @param previous the previous form of the metadata instance
     * @return IPersistentMap giving the previous instance representation
     */
    @SuppressWarnings("unchecked")
    public static IPersistentMap rollbackEntity(String userId,
                                                IPersistentMap current,
                                                IPersistentMap previous) {

        Long currentVersion = (Long) current.valAt(Keywords.VERSION);

        // Setup the rolled-back version by applying the various maintenance detail updates
        // and incrementing its version by one beyond the _current_ version
        IPersistentMap doc = incrementVersion(userId, previous);
        doc = doc.assoc(Keywords.VERSION, currentVersion + 1);

        // Then remove any of the previous classifications
        Iterator<MapEntry> removals = (Iterator<MapEntry>) previous.iterator();
        while (removals.hasNext()) {
            MapEntry entry = removals.next();
            Object key = entry.getKey();
            String keyName = key.toString();
            if (keyName.startsWith(":" + EntitySummaryMapping.N_CLASSIFICATIONS)  || keyName.equals(":" + ClassificationMapping.N_LAST_CLASSIFICATION_CHANGE)) {
                doc = doc.without(key);
            }
        }

        // And re-apply any of the current classifications
        Iterator<MapEntry> additions = (Iterator<MapEntry>) current.iterator();
        while (additions.hasNext()) {
            MapEntry entry = additions.next();
            Object key = entry.getKey();
            String keyName = key.toString();
            if (keyName.startsWith(":" + EntitySummaryMapping.N_CLASSIFICATIONS) || keyName.equals(":" + ClassificationMapping.N_LAST_CLASSIFICATION_CHANGE)) {
                doc = doc.assoc(key, entry.getValue());
            }
        }

        return doc;

    }

}
