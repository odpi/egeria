/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for hard-deleting a relationship.
 */
public class PurgeRelationship extends AbstractTransactionFunction {

    private static final Logger log = LoggerFactory.getLogger(PurgeRelationship.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "purgeRelationship");
    private static final String CLASS_NAME = PurgeRelationship.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx rid force] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db rid)" +
            "          deleted (.doc (" + PurgeRelationship.class.getCanonicalName() + ". tx-id existing rid force))]" +
            "         [[:xtdb.api/evict rid]]))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to update
     * @param deletedRelationshipGUID of the relationship to purge
     * @param force if true, do not validate whether the relationship is already soft-deleted before purging
     * @throws Exception on any error
     */
    public PurgeRelationship(Long txId,
                             PersistentHashMap existing,
                             String deletedRelationshipGUID,
                             boolean force)
            throws Exception {
        try {
            if (existing == null) {
                throw new RelationshipNotKnownException(XTDBErrorCode.RELATIONSHIP_NOT_KNOWN.getMessageDefinition(
                        deletedRelationshipGUID), CLASS_NAME, METHOD_NAME);
            } else {
                if (!force) {
                    TxnValidations.relationshipFromStore(deletedRelationshipGUID, existing, CLASS_NAME, METHOD_NAME);
                    try {
                        TxnValidations.instanceIsDeleted(existing, deletedRelationshipGUID, CLASS_NAME, METHOD_NAME);
                    } catch (InvalidParameterException e) {
                        throw new RelationshipNotDeletedException(XTDBErrorCode.INSTANCE_NOT_DELETED.getMessageDefinition(
                                deletedRelationshipGUID), CLASS_NAME, METHOD_NAME);
                    }
                }
            }
            xtdbDoc = existing;
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }
    }

    /**
     * Permanently delete the relationship (and all of its history) from the XTDB repository by pushing down the transaction.
     * Note that this operation is NOT reversible!
     * @param xtdb connectivity
     * @param relationshipGUID of the relationship to permanently delete
     * @throws RelationshipNotKnownException if the relationship cannot be found
     * @throws RelationshipNotDeletedException if the relationship exists but is not in a soft-deleted state
     * @throws RepositoryErrorException on any other error
     */
    public static void transactWithValidation(XTDBOMRSRepositoryConnector xtdb,
                                              String relationshipGUID)
            throws RelationshipNotKnownException, RelationshipNotDeletedException, RepositoryErrorException {
        String docId = RelationshipMapping.getReference(relationshipGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, false);
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            xtdb.validateCommit(results, METHOD_NAME);
        } catch (RelationshipNotKnownException | RelationshipNotDeletedException | RepositoryErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               CLASS_NAME,
                                               METHOD_NAME,
                                               e);
        }
    }

    /**
     * Permanently delete the relationship (and all of its history) from the XTDB repository by pushing down the transaction.
     * Note that this operation is NOT reversible!
     * @param xtdb connectivity
     * @param relationshipGUID of the relationship to permanently delete
     * @throws RelationshipNotKnownException if the relationship cannot be found
     * @throws RepositoryErrorException on any other error
     */
    public static void transactWithoutValidation(XTDBOMRSRepositoryConnector xtdb,
                                                 String relationshipGUID)
            throws RelationshipNotKnownException, RepositoryErrorException {
        String docId = RelationshipMapping.getReference(relationshipGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, true);
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            xtdb.validateCommit(results, METHOD_NAME);
        } catch (RelationshipNotKnownException | RepositoryErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               CLASS_NAME,
                                               METHOD_NAME,
                                               e);
        }
    }

    /**
     * Interface that returns the document about to be evicted (purged).
     * @return IPersistentMap giving the purged document in its entirety
     */
    public IPersistentMap doc() {
        log.debug("Relationship being purged: {}", xtdbDoc);
        return xtdbDoc;
    }

    /**
     * Create the transaction function within XTDB.
     * @param tx transaction through whic to create the function
     */
    public static void create(Transaction.Builder tx) {
        createTransactionFunction(tx, FUNCTION_NAME, FN);
    }

}
