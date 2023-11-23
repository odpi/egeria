/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.XtdbDocument;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for adding / updating a classification to an entity proxy
 * (including creating that entity proxy if it does not already exist in the repository).
 */
public class ClassifyEntityProxy extends ClassifyEntity {

    private static final Logger log = LoggerFactory.getLogger(ClassifyEntityProxy.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "classifyEntityProxy");
    private static final String CLASS_NAME = ClassifyEntityProxy.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx eid user pxy cname ext-guid ext-name corigin corigin-guid properties mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db eid)" +
            "          updated (.doc (" + ClassifyEntityProxy.class.getCanonicalName() + ". tx-id existing pxy user eid mid cname ext-guid ext-name corigin corigin-guid properties))" +
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
     * @param classificationName name of the classification
     * @param externalSourceGUID unique identifier for the external source
     * @param externalSourceName unique name for the external source
     * @param classificationOrigin source of the classification (assigned or propagated)
     * @param classificationOriginGUID unique identifier of the entity that propagated the classification (if propagated)
     * @param properties the properties to set on the classification
     * @throws Exception on any error
     */
    public ClassifyEntityProxy(Long txId,
                               PersistentHashMap existing,
                               PersistentHashMap proxy,
                               String userId,
                               String entityGUID,
                               String metadataCollectionId,
                               String classificationName,
                               String externalSourceGUID,
                               String externalSourceName,
                               ClassificationOrigin classificationOrigin,
                               String classificationOriginGUID,
                               InstanceProperties properties)
            throws Exception {
        super(CLASS_NAME, METHOD_NAME, txId, existing, proxy, userId, entityGUID, metadataCollectionId, classificationName, externalSourceGUID, externalSourceName, classificationOrigin, classificationOriginGUID, properties);
    }

    /**
     * Add the classification to the provided entity proxy in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the update
     * @param entityProxy of the entity to classify
     * @param classificationName name of the classification
     * @param externalSourceGUID unique identifier for the external source
     * @param externalSourceName unique name for the external source
     * @param classificationOrigin source of the classification (assigned or propagated)
     * @param classificationOriginGUID unique identifier of the entity that propagated the classification (if propagated)
     * @param properties the properties to set on the classification
     * @return Classification that was applied
     * @throws InvalidParameterException if the entity exists but cannot be updated (deleted, reference copy, etc)
     * @throws ClassificationErrorException if there is any issue related to the classification
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different characteristics in the TypeDef for this classification type
     * @throws RepositoryErrorException on any other error
     */
    public static Classification transact(XTDBOMRSRepositoryConnector xtdb,
                                          String userId,
                                          EntityProxy entityProxy,
                                          String classificationName,
                                          String externalSourceGUID,
                                          String externalSourceName,
                                          ClassificationOrigin classificationOrigin,
                                          String classificationOriginGUID,
                                          InstanceProperties properties)
            throws InvalidParameterException, ClassificationErrorException, PropertyErrorException, RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(entityProxy.getGUID());
        EntityProxyMapping epm = new EntityProxyMapping(xtdb, entityProxy);
        XtdbDocument epXT = epm.toXTDB();
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId, epXT.toMap(), classificationName, externalSourceGUID, externalSourceName, classificationOrigin, classificationOriginGUID, properties, xtdb.getMetadataCollectionId());
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
