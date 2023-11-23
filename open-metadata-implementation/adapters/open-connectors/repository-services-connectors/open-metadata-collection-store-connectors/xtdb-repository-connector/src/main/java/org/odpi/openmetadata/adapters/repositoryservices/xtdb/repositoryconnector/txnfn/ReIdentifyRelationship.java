/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for updating an relationship's unique identifier.
 */
public class ReIdentifyRelationship extends ReIdentifyInstance {

    private static final Logger log = LoggerFactory.getLogger(ReIdentifyRelationship.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "reIdentifyRelationship");
    private static final String CLASS_NAME = ReIdentifyRelationship.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();

    private static final String FN = "" +
            "(fn [ctx eid user nid mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db eid)" +
            "          updates (.tuple (" + ReIdentifyRelationship.class.getCanonicalName() + ". tx-id existing user eid nid mid))]" +
            // Expand the resulting 'updates' tuple into distinct update statements
            "         (vec (for [doc updates]" +
            "                   (let [" + getTxnTimeCalculation("doc") + "]" +
            "                        [:xtdb.api/put doc txt])))))";

    private final IPersistentVector xtdbTuple;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to re-identify
     * @param userId doing the update
     * @param relationshipGUID of the relationship to re-identify
     * @param newRelationshipGUID to use for the new identity
     * @param metadataCollectionId of the metadata collection in which the transaction is running
     * @throws Exception on any error
     */
    public ReIdentifyRelationship(Long txId,
                            PersistentHashMap existing,
                            String userId,
                            String relationshipGUID,
                            String newRelationshipGUID,
                            String metadataCollectionId)
            throws Exception {

        try {
            if (existing == null) {
                throw new RelationshipNotKnownException(XTDBErrorCode.RELATIONSHIP_NOT_KNOWN.getMessageDefinition(
                        relationshipGUID), CLASS_NAME, METHOD_NAME);
            } else {
                TxnValidations.relationshipFromStore(relationshipGUID, existing, CLASS_NAME, METHOD_NAME);
                validate(existing, relationshipGUID, metadataCollectionId, CLASS_NAME, METHOD_NAME);
                xtdbTuple = reIdentifyInstance(userId, existing, relationshipGUID, newRelationshipGUID);
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Update the unique identity of the provided relationship instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the update
     * @param relationshipGUID of the relationship to be re-identified
     * @param newRelationshipGUID to apply to the relationship
     * @return Relationship the relationship with the new identity applied
     * @throws RelationshipNotKnownException if the relationship cannot be found
     * @throws InvalidParameterException if the relationship exists but cannot be updated (deleted, reference copy, etc)
     * @throws RepositoryErrorException on any other error
     */
    public static Relationship transact(XTDBOMRSRepositoryConnector xtdb,
                                        String userId,
                                        String relationshipGUID,
                                        String newRelationshipGUID)
            throws RelationshipNotKnownException, InvalidParameterException, RepositoryErrorException {
        String docId = RelationshipMapping.getReference(relationshipGUID);
        String newId = RelationshipMapping.getReference(newRelationshipGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId, newId, xtdb.getMetadataCollectionId());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            return xtdb.getResultingRelationship(newId, results, METHOD_NAME);
        } catch (RelationshipNotKnownException | InvalidParameterException | RepositoryErrorException e) {
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
    public IPersistentVector tuple() {
        log.debug("Re-identification being persisted: {}", xtdbTuple);
        return xtdbTuple;
    }

    /**
     * Create the transaction function within XTDB.
     * @param tx transaction through which to create the function
     */
    public static void create(Transaction.Builder tx) {
        createTransactionFunction(tx, FUNCTION_NAME, FN);
    }

}
