/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityProxyMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.XtdbDocument;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for adding a reference copy relationship.
 */
public class SaveRelationshipReferenceCopy extends AbstractTransactionFunction {

    private static final Logger log = LoggerFactory.getLogger(SaveRelationshipReferenceCopy.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "saveRelationshipReferenceCopy");
    private static final String CLASS_NAME = SaveRelationshipReferenceCopy.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();

    private static final String FN = "" +
            "(fn [ctx rid eid1 eid2 rcr rcp1 rcp2 mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          proxy1 (xtdb.api/entity db eid1)" +
            "          proxy2 (xtdb.api/entity db eid2)" +
            "          existing (xtdb.api/entity db rid)" +
            "          updated (.doc (" + SaveRelationshipReferenceCopy.class.getCanonicalName() + ". tx-id existing rcr mid))" +
            getTxnTimeCalculation("updated") + "]" +
            // These delegated function calls will only create the proxy if a non-proxy for that entity does
            // not already exist, while the final one will put the relationship itself
            "         [[:xtdb.api/fn " + AddEntityProxy.FUNCTION_NAME + " eid1 rcp1]" +
            "          [:xtdb.api/fn " + AddEntityProxy.FUNCTION_NAME + " eid2 rcp2]" +
            "          [:xtdb.api/put updated txt]]))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing the existing relationship in XT, if any
     * @param updated the updated relationship to replace the existing one with
     * @param homeMetadataCollectionId the metadataCollectionId of the repository where the transaction is running
     * @throws Exception on any error
     */
    public SaveRelationshipReferenceCopy(Long txId,
                                         PersistentHashMap existing,
                                         PersistentHashMap updated,
                                         String homeMetadataCollectionId)
            throws Exception {

        try {
            String rcMetadataCollectionId = getMetadataCollectionId(updated);
            if (rcMetadataCollectionId.equals(homeMetadataCollectionId)) {
                // If the reference copy's metadataCollectionId is the same as this repository's, it is
                // not a reference copy...
                throw new HomeRelationshipException(XTDBErrorCode.RELATIONSHIP_HOME_COLLECTION_REFERENCE.getMessageDefinition(
                        getGUID(updated), homeMetadataCollectionId), CLASS_NAME, METHOD_NAME);
            } else if (existing == null) {
                // Otherwise, if it does not yet exist just create it directly
                xtdbDoc = updated;
            } else {
                // Otherwise, make sure that we will not be clobbering some other homed reference copy
                String exMetadataCollectionId = getMetadataCollectionId(existing);
                if (!rcMetadataCollectionId.equals(exMetadataCollectionId)) {
                    throw new RelationshipConflictException(
                            XTDBErrorCode.METADATA_COLLECTION_CONFLICT.getMessageDefinition(
                                    getGUID(updated)),
                            CLASS_NAME, METHOD_NAME);
                } else {
                    xtdbDoc = updated;
                }
            }
        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Create the provided relationship instance in the XTDB repository by pushing down the transaction.
     * @param xtdb connectivity
     * @param toSave the relationship reference copy to persist
     * @throws RelationshipConflictException the new relationship conflicts with an existing relationship
     * @throws HomeRelationshipException if the relationship belongs to the local repository so cannot be a reference copy
     * @throws InvalidParameterException one of the parameters is invalid or null
     * @throws RepositoryErrorException on any other error
     */
    public static void transact(XTDBOMRSRepositoryConnector xtdb,
                                Relationship toSave)
            throws RelationshipConflictException, HomeRelationshipException, InvalidParameterException, RepositoryErrorException {
        String docId = RelationshipMapping.getReference(toSave.getGUID());
        EntityProxy ep1 = toSave.getEntityOneProxy();
        EntityProxy ep2 = toSave.getEntityTwoProxy();
        EntityProxyMapping epm1 = new EntityProxyMapping(xtdb, ep1);
        EntityProxyMapping epm2 = new EntityProxyMapping(xtdb, ep2);
        XtdbDocument ep1XT = epm1.toXTDB();
        XtdbDocument ep2XT = epm2.toXTDB();
        String proxy1Id = EntityDetailMapping.getReference(ep1.getGUID());
        String proxy2Id = EntityDetailMapping.getReference(ep2.getGUID());
        RelationshipMapping rm = new RelationshipMapping(xtdb, toSave);
        XtdbDocument toSaveXT = rm.toXTDB();
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, proxy1Id, proxy2Id, toSaveXT.toMap(), ep1XT.toMap(), ep2XT.toMap(), xtdb.getMetadataCollectionId());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            xtdb.validateCommit(results, METHOD_NAME);
        } catch (RelationshipConflictException | HomeRelationshipException | InvalidParameterException | RepositoryErrorException e) {
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
