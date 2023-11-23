/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import xtdb.api.TransactionInstant;
import xtdb.api.XtdbDocument;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for adding an entity.
 */
public class AddEntity extends AbstractTransactionFunction {

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "addEntity");
    private static final String CLASS_NAME = AddEntity.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    // Always upsert the entity:
    // - creates it if it does not yet exist
    // - "upgrades" a proxy if a proxy with the same GUID already exists
    // - replaces any existing full entity definition (if it exists)
    private static final String FN = "" +
            "(fn [ctx eid full] " +
            "    (let [db (xtdb.api/db ctx)" +
            getTxnTimeCalculation("full") + "]" +
            "         [[:xtdb.api/put full txt]]))";

    /**
     * Default constructor.
     */
    private AddEntity() {
        // Nothing to do here, logic is entirely handled through the Clojure
    }

    /**
     * Create the provided entity instance in the XTDB repository by pushing down the transaction.
     * @param xtdb connectivity
     * @param entity to create
     * @return EntityDetail the entity that was created
     * @throws RepositoryErrorException on any error
     */
    public static EntityDetail transact(XTDBOMRSRepositoryConnector xtdb,
                                        EntityDetail entity) throws RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(entity.getGUID());
        EntityDetailMapping edm = new EntityDetailMapping(xtdb, entity);
        XtdbDocument doc = edm.toXTDB();
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, doc.toMap());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            return xtdb.getResultingEntity(docId, results, METHOD_NAME);
        } catch (RepositoryErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               CLASS_NAME,
                                               METHOD_NAME,
                                               e);
        }
    }

    /**
     * Create the transaction function within XTDB.
     * @param tx transaction through which to create the function
     */
    public static void create(Transaction.Builder tx) {
        createTransactionFunction(tx, FUNCTION_NAME, FN);
    }

}
