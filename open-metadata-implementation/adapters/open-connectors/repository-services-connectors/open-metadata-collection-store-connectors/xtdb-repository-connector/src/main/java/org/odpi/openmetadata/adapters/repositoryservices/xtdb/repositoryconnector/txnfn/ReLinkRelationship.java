/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for re-linking a relationship when one of its entity proxy's identities has changed.
 */
public class ReLinkRelationship extends AbstractTransactionFunction {

    private static final Logger log = LoggerFactory.getLogger(ReLinkRelationship.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "reLinkRelationship");
    private static final String CLASS_NAME = ReLinkRelationship.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx rid eid nid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db rid)" +
            "          relinked (.doc (" + ReLinkRelationship.class.getCanonicalName() + ". tx-id existing rid eid nid))" +
            getTxnTimeCalculation("relinked") + "]" +
            "         [[:xtdb.api/put relinked txt]]))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to update
     * @param relationshipGUID unique identifier of the relationship
     * @param oldEntityGUID unique identifier of the entity that is being changed
     * @param newEntityGUID new unique identifier for that same entity
     * @throws Exception on any error
     */
    public ReLinkRelationship(Long txId,
                              PersistentHashMap existing,
                              String relationshipGUID,
                              String oldEntityGUID,
                              String newEntityGUID)
            throws Exception {

        try {
            if (existing == null) {
                throw new RelationshipNotKnownException(XTDBErrorCode.RELATIONSHIP_NOT_KNOWN.getMessageDefinition(
                        relationshipGUID), this.getClass().getName(), METHOD_NAME);
            } else {
                IPersistentVector proxies = (IPersistentVector) existing.valAt(Keywords.ENTITY_PROXIES);
                if (proxies != null && proxies.length() == 2) {
                    String proxyOneGUID = (String) proxies.nth(0);
                    String proxyTwoGUID = (String) proxies.nth(1);
                    IPersistentVector newProxies = proxies;
                    if (proxyOneGUID.equals(oldEntityGUID)) {
                        newProxies = PersistentVector.create(newEntityGUID, proxyTwoGUID);
                    } else if (proxyTwoGUID.equals(oldEntityGUID)) {
                        newProxies = PersistentVector.create(proxyOneGUID, newEntityGUID);
                    }
                    xtdbDoc = existing.assoc(Keywords.ENTITY_PROXIES, newProxies);
                } else {
                    throw new RepositoryErrorException(XTDBErrorCode.INVALID_INSTANCE_FROM_STORE.getMessageDefinition(
                            relationshipGUID,
                            "one or both proxies are missing",
                            existing.toString()),
                                                       CLASS_NAME,
                                                       METHOD_NAME);
                }
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
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
