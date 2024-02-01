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
import xtdb.api.tx.Transaction;

/**
 * Transaction function for adding / updating a classification to an entity.
 */
public class ClassifyEntityDetail extends ClassifyEntity {

    private static final Logger log = LoggerFactory.getLogger(ClassifyEntityDetail.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "classifyEntity");
    private static final String CLASS_NAME = ClassifyEntityDetail.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();
    private static final String FN = "" +
            "(fn [ctx eid user cname ext-guid ext-name corigin corigin-guid properties mid] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          existing (xtdb.api/entity db eid)" +
            "          updated (.doc (" + ClassifyEntityDetail.class.getCanonicalName() + ". tx-id existing user eid mid cname ext-guid ext-name corigin corigin-guid properties))" +
            getTxnTimeCalculation("updated") + "]" +
            "         [[:xtdb.api/put updated txt]]))";

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param existing XTDB document to update
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
    public ClassifyEntityDetail(Long txId,
                                PersistentHashMap existing,
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
        super(CLASS_NAME, METHOD_NAME, txId, existing, null, userId, entityGUID, metadataCollectionId, classificationName, externalSourceGUID, externalSourceName, classificationOrigin, classificationOriginGUID, properties);
    }

    /**
     * Update the status of the provided entity instance in the XTDB repository by pushing the transaction
     * down into the repository itself.
     * @param xtdb connectivity
     * @param userId doing the update
     * @param entityGUID of the entity on which to update the status
     * @param classificationName name of the classification
     * @param externalSourceGUID unique identifier for the external source
     * @param externalSourceName unique name for the external source
     * @param classificationOrigin source of the classification (assigned or propagated)
     * @param classificationOriginGUID unique identifier of the entity that propagated the classification (if propagated)
     * @param properties the properties to set on the classification
     * @return EntityDetail of the entity with the new status applied
     * @throws EntityNotKnownException if the entity cannot be found
     * @throws InvalidParameterException if the entity exists but cannot be updated (deleted, reference copy, etc)
     * @throws ClassificationErrorException if there is any issue related to the classification
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different characteristics in the TypeDef for this classification type
     * @throws RepositoryErrorException on any other error
     */
    public static EntityDetail transact(XTDBOMRSRepositoryConnector xtdb,
                                        String userId,
                                        String entityGUID,
                                        String classificationName,
                                        String externalSourceGUID,
                                        String externalSourceName,
                                        ClassificationOrigin classificationOrigin,
                                        String classificationOriginGUID,
                                        InstanceProperties properties)
            throws EntityNotKnownException, InvalidParameterException, ClassificationErrorException, PropertyErrorException, RepositoryErrorException {
        String docId = EntityDetailMapping.getReference(entityGUID);
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, docId, userId, classificationName, externalSourceGUID, externalSourceName, classificationOrigin, classificationOriginGUID, properties, xtdb.getMetadataCollectionId());
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            return xtdb.getResultingEntity(docId, results, METHOD_NAME);
        } catch (EntityNotKnownException | InvalidParameterException | ClassificationErrorException | PropertyErrorException | RepositoryErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.UNKNOWN_RUNTIME_ERROR.getMessageDefinition(),
                                               ClassifyEntityDetail.class.getName(),
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
