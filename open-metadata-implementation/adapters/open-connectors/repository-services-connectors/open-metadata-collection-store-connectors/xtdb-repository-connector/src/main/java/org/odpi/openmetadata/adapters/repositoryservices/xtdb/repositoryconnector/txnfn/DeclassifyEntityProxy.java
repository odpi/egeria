/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.XtdbDocument;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for removing a classification to an entity.
 */
public class DeclassifyEntityProxy extends DeclassifyEntity {

    private static final Logger log = LoggerFactory.getLogger(DeclassifyEntityProxy.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "declassifyEntityProxy");
    private static final String CLASS_NAME = DeclassifyEntityProxy.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx eid pxy cname] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db eid)" +
            "          updated (.doc (" + DeclassifyEntityProxy.class.getCanonicalName() + ". tx-id existing eid pxy cname))" +
            getTxnTimeCalculation("updated") + "]" +
            "         [[:xtdb.api/put updated txt]]))";

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to update
     * @param proxy XTDB document to update, if existing is empty
     * @param entityGUID of the entity to update
     * @param classificationName name of the classification
     * @throws Exception on any error
     */
    public DeclassifyEntityProxy(Long txId,
                                 PersistentHashMap existing,
                                 PersistentHashMap proxy,
                                 String entityGUID,
                                 String classificationName)
            throws Exception {
        super(CLASS_NAME, METHOD_NAME, txId, existing, proxy, entityGUID, classificationName);
    }

    /**
     * Update the status of the provided entity instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param entityProxy of the entity to declassify
     * @param classificationName name of the classification
     * @return Classification that was removed
     * @throws EntityNotKnownException if the entity cannot be found
     * @throws InvalidParameterException if the entity exists but cannot be updated (deleted, reference copy, etc)
     * @throws ClassificationErrorException if there is any issue related to the classification
     * @throws RepositoryErrorException on any other error
     */
    public static Classification transact(XTDBOMRSRepositoryConnector xtdb,
                                          EntityProxy entityProxy,
                                          String classificationName)
            throws EntityNotKnownException, InvalidParameterException, ClassificationErrorException, RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(entityProxy.getGUID());
        EntityProxyMapping epm = new EntityProxyMapping(xtdb, entityProxy);
        XtdbDocument epXT = epm.toXTDB();
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, epXT.toMap(), classificationName);
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
        } catch (InvalidParameterException | ClassificationErrorException | RepositoryErrorException e) {
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
