/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for restoring a deleted relationship back to active.
 */
public class RestoreRelationship extends RestoreInstance {

    private static final Logger log = LoggerFactory.getLogger(RestoreRelationship.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "restoreRelationship");
    private static final String CLASS_NAME = RestoreRelationship.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx rid user mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db rid)" +
            "          restored (.doc (" + RestoreRelationship.class.getCanonicalName() + ". tx-id existing user rid mid))" +
            getTxnTimeCalculation("restored") + "]" +
            "         [[:xtdb.api/put restored txt]]))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to restore
     * @param userId doing the restore
     * @param relationshipGUID of the relationship to restore
     * @param metadataCollectionId of the metadata collection in which the transaction is running
     * @throws Exception on any error
     */
    public RestoreRelationship(Long txId,
                               PersistentHashMap existing,
                               String userId,
                               String relationshipGUID,
                               String metadataCollectionId)
            throws Exception {

        try {
            if (existing == null) {
                throw new RelationshipNotKnownException(XTDBErrorCode.RELATIONSHIP_NOT_KNOWN.getMessageDefinition(
                        relationshipGUID), this.getClass().getName(), METHOD_NAME);
            } else {
                TxnValidations.relationshipFromStore(relationshipGUID, existing, CLASS_NAME, METHOD_NAME);
                try {
                    TxnValidations.instanceIsDeleted(existing, relationshipGUID, CLASS_NAME, METHOD_NAME);
                } catch (InvalidParameterException e) {
                    throw new RelationshipNotDeletedException(XTDBErrorCode.INSTANCE_NOT_DELETED.getMessageDefinition(
                            relationshipGUID), CLASS_NAME, METHOD_NAME);
                }
                TxnValidations.instanceCanBeUpdated(existing, relationshipGUID, metadataCollectionId, CLASS_NAME, METHOD_NAME);
                xtdbDoc = restoreInstance(userId, existing);
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Restore the deleted relationship instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the restore
     * @param relationshipGUID of the relationship on which to restore
     * @return Relationship as restored
     * @throws RelationshipNotKnownException if the relationship cannot be found
     * @throws RelationshipNotDeletedException if the relationship exists but cannot be restored because it is not deleted
     * @throws InvalidParameterException if the relationship exists but cannot be restored (reference copy, etc)
     * @throws RepositoryErrorException on any other error
     */
    public static Relationship transact(XTDBOMRSRepositoryConnector xtdb,
                                        String userId,
                                        String relationshipGUID)
            throws RelationshipNotKnownException, RelationshipNotDeletedException, InvalidParameterException, RepositoryErrorException {
        String docId = RelationshipMapping.getReference(relationshipGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId, xtdb.getMetadataCollectionId());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            Relationship result = xtdb.getResultingRelationship(docId, results, METHOD_NAME);
            OMRSRepositoryValidator repositoryValidator = xtdb.getRepositoryValidator();
            String repositoryName = xtdb.getRepositoryName();
            repositoryValidator.validateRelationshipFromStore(repositoryName, relationshipGUID, result, METHOD_NAME);
            repositoryValidator.validateRelationshipIsNotDeleted(repositoryName, result, METHOD_NAME);
            return result;
        } catch (RelationshipNotKnownException | RelationshipNotDeletedException | InvalidParameterException | RepositoryErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               RestoreRelationship.class.getName(),
                                               METHOD_NAME,
                                               e);
        }
    }

    /**
     * Interface that returns the restored document to write-back from the transaction.
     * @return IPersistentMap giving the restored document in its entirety
     */
    public IPersistentMap doc() {
        log.debug("Relationship being persisted: {}", xtdbDoc);
        return xtdbDoc;
    }

    /**
     * Create the transaction function within XTDB.
     * @param tx transaction through which to create the function
     */
    public static void create(Transaction.Builder tx) {
        createTransactionFunction(tx, FUNCTION_NAME, FN);
    }

}
