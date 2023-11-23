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
 * Transaction function for updating a relationship's home repository.
 */
public class ReHomeRelationship extends ReHomeInstance {

    private static final Logger log = LoggerFactory.getLogger(ReHomeRelationship.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "reHomeRelationship");
    private static final String CLASS_NAME = ReHomeRelationship.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx eid user mid nmid nmname] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db eid)" +
            "          updated (.doc (" + ReHomeRelationship.class.getCanonicalName() + ". tx-id existing user eid mid nmid nmname))" +
            getTxnTimeCalculation("updated") + "]" +
            "         [[:xtdb.api/put updated txt]]))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to re-home
     * @param userId doing the update
     * @param relationshipGUID of the relationship to re-home
     * @param metadataCollectionId of the metadata collection in which the transaction is running
     * @param newMetadataCollectionId in which to re-home to the relationship
     * @param newMetadataCollectionName in which to re-home the relationship
     * @throws Exception on any error
     */
    public ReHomeRelationship(Long txId,
                              PersistentHashMap existing,
                              String userId,
                              String relationshipGUID,
                              String metadataCollectionId,
                              String newMetadataCollectionId,
                              String newMetadataCollectionName)
            throws Exception {

        try {
            if (existing == null) {
                throw new RelationshipNotKnownException(XTDBErrorCode.RELATIONSHIP_NOT_KNOWN.getMessageDefinition(
                        relationshipGUID), CLASS_NAME, METHOD_NAME);
            } else {
                TxnValidations.relationshipFromStore(relationshipGUID, existing, CLASS_NAME, METHOD_NAME);
                validate(existing, metadataCollectionId, CLASS_NAME, METHOD_NAME);
                xtdbDoc = reHomeInstance(userId, existing, newMetadataCollectionId, newMetadataCollectionName);
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Change the home repository of the provided relationship instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the update
     * @param relationshipGUID of the relationship on which to change the home repository
     * @param newMetadataCollectionId in which to re-home to the relationship
     * @param newMetadataCollectionName in which to re-home the relationship
     * @return Relationship the relationship with the new home repository applied
     * @throws RelationshipNotKnownException if the relationship cannot be found
     * @throws InvalidParameterException if the relationship exists but cannot be re-homed (i.e. not a reference copy)
     * @throws RepositoryErrorException on any other error
     */
    public static Relationship transact(XTDBOMRSRepositoryConnector xtdb,
                                        String userId,
                                        String relationshipGUID,
                                        String newMetadataCollectionId,
                                        String newMetadataCollectionName)
            throws RelationshipNotKnownException, InvalidParameterException, RepositoryErrorException {
        String docId = RelationshipMapping.getReference(relationshipGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId, xtdb.getMetadataCollectionId(), newMetadataCollectionId, newMetadataCollectionName);
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            return xtdb.getResultingRelationship(docId, results, METHOD_NAME);
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

}
