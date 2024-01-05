/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for restoring a deleted entity back to active.
 */
public class RestoreEntity extends RestoreInstance {

    private static final Logger log = LoggerFactory.getLogger(RestoreEntity.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "restoreEntity");
    private static final String CLASS_NAME = RestoreEntity.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx eid user mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db eid)" +
            "          restored (.doc (" + RestoreEntity.class.getCanonicalName() + ". tx-id existing user eid mid))" +
            getTxnTimeCalculation("restored") + "]" +
            "         [[:xtdb.api/put restored txt]]))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to restore
     * @param userId doing the restore
     * @param entityGUID of the entity to restore
     * @param metadataCollectionId of the metadata collection in which the transaction is running
     * @throws Exception on any error
     */
    public RestoreEntity(Long txId,
                         PersistentHashMap existing,
                         String userId,
                         String entityGUID,
                         String metadataCollectionId)
            throws Exception {

        try {
            if (existing == null) {
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        entityGUID), this.getClass().getName(), METHOD_NAME);
            } else {
                TxnValidations.nonProxyEntity(existing, entityGUID, CLASS_NAME, METHOD_NAME);
                TxnValidations.entityFromStore(entityGUID, existing, CLASS_NAME, METHOD_NAME);
                try {
                    TxnValidations.instanceIsDeleted(existing, entityGUID, CLASS_NAME, METHOD_NAME);
                } catch (InvalidParameterException e) {
                    throw new EntityNotDeletedException(XTDBErrorCode.INSTANCE_NOT_DELETED.getMessageDefinition(
                            entityGUID), CLASS_NAME, METHOD_NAME);
                }
                TxnValidations.instanceCanBeUpdated(existing, entityGUID, metadataCollectionId, CLASS_NAME, METHOD_NAME);
                xtdbDoc = restoreInstance(userId, existing);
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Restore the deleted entity instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the restore
     * @param entityGUID of the entity on which to restore
     * @return EntityDetail as restored
     * @throws EntityNotKnownException if the entity cannot be found
     * @throws EntityNotDeletedException if the entity exists but cannot be restored because it is not deleted
     * @throws InvalidParameterException if the entity exists but cannot be restored (reference copy, etc)
     * @throws RepositoryErrorException on any other error
     */
    public static EntityDetail transact(XTDBOMRSRepositoryConnector xtdb,
                                        String userId,
                                        String entityGUID)
            throws EntityNotKnownException, EntityNotDeletedException, InvalidParameterException, RepositoryErrorException {
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
        } catch (EntityNotKnownException | EntityNotDeletedException | InvalidParameterException | RepositoryErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               RestoreEntity.class.getName(),
                                               METHOD_NAME,
                                               e);
        }
    }

    /**
     * Interface that returns the restored document to write-back from the transaction.
     * @return IPersistentMap giving the restored document in its entirety
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
