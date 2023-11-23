/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityProxyMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import xtdb.api.TransactionInstant;
import xtdb.api.XtdbDocument;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for adding an EntityProxy.
 */
public class AddEntityProxy extends AbstractTransactionFunction {

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "addEntityProxy");
    private static final String CLASS_NAME = AddEntityProxy.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    // Only create the proxy if:
    // - some other entity with this GUID does not yet exist
    // - a proxy with this GUID exists, but it is only a proxy (in which case we will upsert)
    private static final String FN = "" +
            "(fn [ctx eid proxy] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          existing (xtdb.api/entity db eid)" +
            "          proxy-only (get existing :" + EntityProxyMapping.ENTITY_PROXY_ONLY_MARKER + ")" +
            "          create (if (some? proxy-only) proxy-only true)" +
            getTxnTimeCalculation("proxy") + "]" +
            "         (when create [[:xtdb.api/put proxy txt]])))";

    /**
     * Default constructor.
     */
    private AddEntityProxy() {
        // Nothing to do here, logic is entirely handled through the Clojure
    }

    /**
     * Create the provided entity instance in the XTDB repository by pushing down the transaction.
     * @param xtdb connectivity
     * @param entity to create
     * @throws RepositoryErrorException on any error
     */
    public static void transact(XTDBOMRSRepositoryConnector xtdb,
                                EntityProxy entity) throws RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(entity.getGUID());
        EntityProxyMapping epm = new EntityProxyMapping(xtdb, entity);
        XtdbDocument proxyDoc = epm.toXTDB();
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, proxyDoc.toMap());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            xtdb.validateCommit(results, METHOD_NAME);
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
