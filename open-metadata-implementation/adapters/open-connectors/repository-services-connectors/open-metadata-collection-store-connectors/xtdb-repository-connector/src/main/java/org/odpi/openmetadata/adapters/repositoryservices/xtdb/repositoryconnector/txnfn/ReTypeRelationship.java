/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.TypeDefCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for changing a relationship's type.
 */
public class ReTypeRelationship extends ReTypeInstance {

    private static final Logger log = LoggerFactory.getLogger(ReTypeRelationship.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "reTypeRelationship");
    private static final String CLASS_NAME = ReTypeRelationship.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx rid user tname mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db rid)" +
            "          updated (.doc (" + ReTypeRelationship.class.getCanonicalName() + ". tx-id existing user rid tname mid))" +
            getTxnTimeCalculation("updated") + "]" +
            "         [[:xtdb.api/put updated txt]]))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to update
     * @param userId doing the update
     * @param relationshipGUID of the relationship to update
     * @param newTypeDefName unique identifier of the new type to apply
     * @param metadataCollectionId of the metadata collection in which the transaction is running
     * @throws Exception on any error
     */
    public ReTypeRelationship(Long txId,
                        PersistentHashMap existing,
                        String userId,
                        String relationshipGUID,
                        String newTypeDefName,
                        String metadataCollectionId)
            throws Exception {

        try {
            if (existing == null) {
                throw new RelationshipNotKnownException(XTDBErrorCode.RELATIONSHIP_NOT_KNOWN.getMessageDefinition(
                        relationshipGUID), this.getClass().getName(), METHOD_NAME);
            } else {
                TxnValidations.relationshipFromStore(relationshipGUID, existing, CLASS_NAME, METHOD_NAME);
                TypeDef typeDef = TypeDefCache.getTypeDefByName(newTypeDefName);
                validate(existing, relationshipGUID, typeDef, metadataCollectionId, CLASS_NAME, METHOD_NAME);
                xtdbDoc = reTypeInstance(userId, existing, typeDef);
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Change the type of the provided relatinship instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the update
     * @param relationshipGUID of the relationship on which to change the type
     * @param newTypeDef to apply to the relationship
     * @return Relationship the relationship with the new type applied
     * @throws RelationshipNotKnownException if the relationship cannot be found
     * @throws PropertyErrorException the properties in the instance are incompatible with the requested type
     * @throws InvalidParameterException if the relationship exists but cannot be updated (deleted, reference copy, etc)
     * @throws TypeErrorException the requested type is not known or supported by the repository
     * @throws RepositoryErrorException on any other error
     */
    public static Relationship transact(XTDBOMRSRepositoryConnector xtdb,
                                        String userId,
                                        String relationshipGUID,
                                        TypeDefSummary newTypeDef)
            throws RelationshipNotKnownException, PropertyErrorException, TypeErrorException, InvalidParameterException, RepositoryErrorException {
        String docId = RelationshipMapping.getReference(relationshipGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId, newTypeDef.getName(), xtdb.getMetadataCollectionId());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            return xtdb.getResultingRelationship(docId, results, METHOD_NAME);
        } catch (RelationshipNotKnownException | PropertyErrorException | TypeErrorException | InvalidParameterException | RepositoryErrorException e) {
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
