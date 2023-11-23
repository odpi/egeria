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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for updating an entity's unique identifier.
 */
public class ReIdentifyEntity extends ReIdentifyInstance {

    private static final Logger log = LoggerFactory.getLogger(ReIdentifyEntity.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "reIdentifyEntity");
    private static final String CLASS_NAME = ReIdentifyEntity.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();

    // Query to retrieve all homed relationships that have this entity as one of their endpoints
    // (mixed quoting is necessary to ensure the eid is evaluated, so it becomes first in the join
    // ordering of the query for performance reasons)
    private static final String RELN_QUERY = "" +
            "{:find [(quote r)]" +
            " :where [[(quote r) :" + RelationshipMapping.ENTITY_PROXIES + " eid]" +
            "         [(quote r) :" + InstanceAuditHeaderMapping.TYPE_DEF_CATEGORY + " " + TypeDefCategory.RELATIONSHIP_DEF.getOrdinal() + "]" +
            "         [(quote r) :" + InstanceAuditHeaderMapping.METADATA_COLLECTION_ID + " mid]]}";

    private static final String FN = "" +
            "(fn [ctx eid user nid mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          relationships (xtdb.api/q db " + RELN_QUERY + ")" +
            "          existing (xtdb.api/entity db eid)" +
            "          updates (.tuple (" + ReIdentifyEntity.class.getCanonicalName() + ". tx-id existing user eid nid mid))]" +
            "         (vec (concat (vec (for [[rid] relationships]" +
            "                                 [:xtdb.api/fn " + ReLinkRelationship.FUNCTION_NAME + " rid eid nid]))" +
            // And also expand the resulting 'updates' tuple into distinct update statements
            "                      (vec (for [doc updates]" +
            "                                (let [" + getTxnTimeCalculation("doc") + "]" +
            "                                     [:xtdb.api/put doc txt])))))))";

    private final IPersistentVector xtdbTuple;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to re-identify
     * @param userId doing the update
     * @param entityGUID of the entity to re-identify
     * @param newEntityGUID to use for the new identity
     * @param metadataCollectionId of the metadata collection in which the transaction is running
     * @throws Exception on any error
     */
    public ReIdentifyEntity(Long txId,
                            PersistentHashMap existing,
                            String userId,
                            String entityGUID,
                            String newEntityGUID,
                            String metadataCollectionId)
            throws Exception {

        try {
            if (existing == null) {
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        entityGUID), CLASS_NAME, METHOD_NAME);
            } else {
                TxnValidations.nonProxyEntity(existing, entityGUID, CLASS_NAME, METHOD_NAME);
                TxnValidations.entityFromStore(entityGUID, existing, CLASS_NAME, METHOD_NAME);
                validate(existing, entityGUID, metadataCollectionId, CLASS_NAME, METHOD_NAME);
                xtdbTuple = reIdentifyInstance(userId, existing, entityGUID, newEntityGUID);
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Update the unique identity of the provided entity instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the update
     * @param entityGUID of the entity to be re-identified
     * @param newEntityGUID to apply to the entity
     * @return EntityDetail of the entity with the new identity applied
     * @throws EntityNotKnownException if the entity cannot be found
     * @throws InvalidParameterException if the entity exists but cannot be updated (deleted, reference copy, etc)
     * @throws RepositoryErrorException on any other error
     */
    public static EntityDetail transact(XTDBOMRSRepositoryConnector xtdb,
                                        String userId,
                                        String entityGUID,
                                        String newEntityGUID)
            throws EntityNotKnownException, InvalidParameterException, RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(entityGUID);
        String newId = EntityDetailMapping.getReference(newEntityGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId, newId, xtdb.getMetadataCollectionId());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            return xtdb.getResultingEntity(newId, results, METHOD_NAME);
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
