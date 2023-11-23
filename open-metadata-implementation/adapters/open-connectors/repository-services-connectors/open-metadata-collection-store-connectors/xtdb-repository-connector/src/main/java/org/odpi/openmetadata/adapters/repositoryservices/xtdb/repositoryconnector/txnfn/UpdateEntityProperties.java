/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for updating InstanceProperties on a metadata instance.
 */
public class UpdateEntityProperties extends UpdateInstanceProperties {

    private static final Logger log = LoggerFactory.getLogger(UpdateEntityProperties.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "updateEntityProperties");
    private static final String CLASS_NAME = UpdateEntityProperties.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx eid user properties mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db eid)" +
            "          updated (.doc (" + UpdateEntityProperties.class.getCanonicalName() + ". tx-id existing user eid mid properties))" +
            getTxnTimeCalculation("updated") + "]" +
            "         [[:xtdb.api/put updated txt]]))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to update
     * @param userId doing the update
     * @param entityGUID of the entity to update
     * @param metadataCollectionId of the metadata collection in which the transaction is running
     * @param properties to apply to the entity
     * @throws Exception on any error
     */
    public UpdateEntityProperties(Long txId,
                                  PersistentHashMap existing,
                                  String userId,
                                  String entityGUID,
                                  String metadataCollectionId,
                                  InstanceProperties properties)
            throws Exception {

        try {
            if (existing == null) {
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        entityGUID), this.getClass().getName(), METHOD_NAME);
            } else {
                TxnValidations.nonProxyEntity(existing, entityGUID, CLASS_NAME, METHOD_NAME);
                TxnValidations.entityFromStore(entityGUID, existing, CLASS_NAME, METHOD_NAME);
                validate(existing, entityGUID, metadataCollectionId, properties, CLASS_NAME, METHOD_NAME);
                xtdbDoc = updateInstanceProperties(userId, existing, properties);
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Update the properties of the provided entity instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the update
     * @param entityGUID of the entity on which to update the properties
     * @param properties to apply to the entity
     * @return EntityDetail of the entity with the new properties applied
     * @throws EntityNotKnownException if the entity cannot be found
     * @throws InvalidParameterException if the entity exists but cannot be updated (deleted, reference copy, etc)
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different characteristics in the TypeDef for this instance's type
     * @throws RepositoryErrorException on any other error
     */
    public static EntityDetail transact(XTDBOMRSRepositoryConnector xtdb,
                                        String userId,
                                        String entityGUID,
                                        InstanceProperties properties)
            throws EntityNotKnownException, InvalidParameterException, PropertyErrorException, RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(entityGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId, properties, xtdb.getMetadataCollectionId());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            return xtdb.getResultingEntity(docId, results, METHOD_NAME);
        } catch (EntityNotKnownException | InvalidParameterException | PropertyErrorException | RepositoryErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               UpdateEntityProperties.class.getName(),
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
