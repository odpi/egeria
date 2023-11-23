/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.XtdbDocument;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for adding a reference copy entity.
 */
public class SaveEntityReferenceCopy extends AbstractTransactionFunction {

    private static final Logger log = LoggerFactory.getLogger(SaveEntityReferenceCopy.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "saveEntityReferenceCopy");
    private static final String CLASS_NAME = SaveEntityReferenceCopy.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();

    private static final String FN = "" +
            "(fn [ctx eid rce mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db eid)" +
            "          updated (.doc (" + SaveEntityReferenceCopy.class.getCanonicalName() + ". tx-id existing rce mid))" +
            getTxnTimeCalculation("updated") + "]" +
            "         [[:xtdb.api/put updated txt]]))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing the existing entity in XT, if any
     * @param updated the updated entity to replace the existing one with
     * @param homeMetadataCollectionId the metadataCollectionId of the repository where the transaction is running
     * @throws Exception on any error
     */
    public SaveEntityReferenceCopy(Long txId,
                                   PersistentHashMap existing,
                                   PersistentHashMap updated,
                                   String homeMetadataCollectionId)
            throws Exception {

        try {
            String rcMetadataCollectionId = getMetadataCollectionId(updated);
            if (rcMetadataCollectionId.equals(homeMetadataCollectionId)) {
                // If the reference copy's metadataCollectionId is the same as this repository's, it is
                // not a reference copy...
                throw new HomeEntityException(XTDBErrorCode.ENTITY_HOME_COLLECTION_REFERENCE.getMessageDefinition(
                        getGUID(updated), homeMetadataCollectionId), CLASS_NAME, METHOD_NAME);
            } else if (existing == null) {
                // Otherwise, if it does not yet exist just create it directly
                xtdbDoc = updated;
            } else {
                // Otherwise, make sure that we will not be clobbering some other homed reference copy
                String exMetadataCollectionId = getMetadataCollectionId(existing);
                if (!rcMetadataCollectionId.equals(exMetadataCollectionId)) {
                    throw new EntityConflictException(
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
     * Create the provided entity instance in the XTDB repository by pushing down the transaction.
     * @param xtdb connectivity
     * @param toSave the entity reference copy to persist
     * @throws EntityConflictException the new entity conflicts with an existing entity
     * @throws HomeEntityException if the entity belongs to the local repository so cannot be a reference copy
     * @throws InvalidParameterException one of the parameters is invalid or null
     * @throws RepositoryErrorException on any other error
     */
    public static void transact(XTDBOMRSRepositoryConnector xtdb,
                                EntityDetail toSave)
            throws EntityConflictException, HomeEntityException, InvalidParameterException, RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(toSave.getGUID());
        EntityDetailMapping edm = new EntityDetailMapping(xtdb, toSave);
        XtdbDocument toSaveXT = edm.toXTDB();
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, toSaveXT.toMap(), xtdb.getMetadataCollectionId());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            xtdb.validateCommit(results, METHOD_NAME);
        } catch (EntityConflictException | HomeEntityException | InvalidParameterException | RepositoryErrorException e) {
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
