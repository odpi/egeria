/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityProxyMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.XtdbDocument;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for updating InstanceProperties on a metadata instance.
 */
public class UpdateEntityProxyClassification extends UpdateEntityClassification {

    private static final Logger log = LoggerFactory.getLogger(UpdateEntityProxyClassification.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "updateEntityProxyClassification");
    public static final String CLASS_NAME = UpdateEntityProxyClassification.class.getName();
    public static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx eid user pxy cname properties mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db eid)" +
            "          updated (.doc (" + UpdateEntityProxyClassification.class.getCanonicalName() + ". tx-id existing pxy user eid mid cname properties))" +
            getTxnTimeCalculation("updated") + "]" +
            "         [[:xtdb.api/put updated txt]]))";

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to update
     * @param proxy XTDB document to update, if existing is empty
     * @param userId doing the update
     * @param entityGUID of the entity to update
     * @param metadataCollectionId of the metadata collection in which the transaction is running
     * @param classificationName of the classification to update
     * @param properties to apply to the classification
     * @throws Exception on any error
     */
    public UpdateEntityProxyClassification(Long txId,
                                           PersistentHashMap existing,
                                           PersistentHashMap proxy,
                                           String userId,
                                           String entityGUID,
                                           String metadataCollectionId,
                                           String classificationName,
                                           InstanceProperties properties)
            throws Exception {
        super(CLASS_NAME, METHOD_NAME, txId, existing, proxy, userId, entityGUID, metadataCollectionId, classificationName, properties);
    }

    /**
     * Update the properties of the provided entity instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the update
     * @param entityProxy of the entity to classify
     * @param classificationName of the classification to update
     * @param properties to apply to the classification
     * @return Classification that was applied
     * @throws EntityNotKnownException if the entity cannot be found
     * @throws ClassificationErrorException if the specified classification cannot be found to update
     * @throws InvalidParameterException if the entity exists but cannot be updated (deleted, reference copy, etc)
     * @throws PropertyErrorException if one or more of the requested properties are not defined or have different characteristics in the TypeDef for this classification type
     * @throws RepositoryErrorException on any other error
     */
    public static Classification transact(XTDBOMRSRepositoryConnector xtdb,
                                          String userId,
                                          EntityProxy entityProxy,
                                          String classificationName,
                                          InstanceProperties properties)
            throws EntityNotKnownException, ClassificationErrorException, InvalidParameterException, PropertyErrorException, RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(entityProxy.getGUID());
        EntityProxyMapping epm = new EntityProxyMapping(xtdb, entityProxy);
        XtdbDocument epXT = epm.toXTDB();
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId, epXT.toMap(), classificationName, properties, xtdb.getMetadataCollectionId());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            EntitySummary result = xtdb.getResultingEntitySummary(docId, results, METHOD_NAME);
            if (result != null) {
                for (Classification cls : result.getClassifications()) {
                    if (cls.getName().equals(classificationName)) {
                        return cls;
                    }
                }
            }
            return null;
        } catch (InvalidParameterException | ClassificationErrorException | PropertyErrorException | RepositoryErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               ClassifyEntityProxy.class.getName(),
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
