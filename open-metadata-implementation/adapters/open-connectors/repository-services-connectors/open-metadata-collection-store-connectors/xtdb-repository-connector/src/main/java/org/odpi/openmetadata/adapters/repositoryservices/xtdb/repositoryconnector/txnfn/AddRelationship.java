/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.ErrorMessageCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityProxyMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

/**
 * Transaction function for adding a Relationship.
 */
public class AddRelationship extends AbstractTransactionFunction {

    private static final Logger log = LoggerFactory.getLogger(AddRelationship.class);

    public static final Keyword FUNCTION_NAME = Keyword.intern("egeria", "addRelationship");
    private static final String CLASS_NAME = AddRelationship.class.getName();
    private static final String METHOD_NAME = FUNCTION_NAME.toString();

    private static final String FN = "" +
            "(fn [ctx eid1 eid2 relationship] " +
            "    (let [db (xtdb.api/db ctx)" +
            "          tx-id (:tx-id db)" +
            "          proxy1 (xtdb.api/entity db eid1)" +
            "          proxy2 (xtdb.api/entity db eid2)" +
            "          created (.doc (" + AddRelationship.class.getCanonicalName() + ". tx-id eid1 eid2 proxy1 proxy2 relationship))" +
            getTxnTimeCalculation("created") + "]" +
            "         [[:xtdb.api/put created txt]]))";

    private final IPersistentMap xtdbDoc;

    /**
     * Constructor used to execute the transaction function.
     * @param txId the transaction ID of this function invocation
     * @param entityOneGUID the unique identifier of the first endpoint of the relationship
     * @param entityTwoGUID the unique identifier of the second endpoint of the relationship
     * @param proxy1 XTDB document representing the EntityProxy at endpoint 1 of the relationship
     * @param proxy2 XTDB document representing the EntityProxy at endpoint 2 of the relationship
     * @param relationship the relationship to create
     * @throws Exception on any error
     */
    public AddRelationship(Long txId,
                           String entityOneGUID,
                           String entityTwoGUID,
                           PersistentHashMap proxy1,
                           PersistentHashMap proxy2,
                           Relationship relationship)
            throws Exception {

        try {

            if (proxy1 == null) {
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        entityOneGUID), CLASS_NAME, METHOD_NAME);
            }
            if (proxy2 == null) {
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        entityTwoGUID), CLASS_NAME, METHOD_NAME);
            }

            TxnValidations.entityFromStore(entityOneGUID, proxy1, CLASS_NAME, METHOD_NAME);
            TxnValidations.instanceIsNotDeleted(proxy1, entityOneGUID, CLASS_NAME, METHOD_NAME);

            TxnValidations.entityFromStore(entityTwoGUID, proxy2, CLASS_NAME, METHOD_NAME);
            TxnValidations.instanceIsNotDeleted(proxy2, entityTwoGUID, CLASS_NAME, METHOD_NAME);

            TxnValidations.relationshipEnds(entityOneGUID, entityTwoGUID, proxy1, proxy2, relationship.getType().getTypeDefGUID(), CLASS_NAME, METHOD_NAME);

            EntityProxy one = EntityProxyMapping.fromMap(proxy1);
            EntityProxy two = EntityProxyMapping.fromMap(proxy2);
            relationship.setEntityOneProxy(one);
            relationship.setEntityTwoProxy(two);

            xtdbDoc = RelationshipMapping.toMap(relationship);

        } catch (Exception e) {
            throw ErrorMessageCache.add(txId, e);
        }

    }

    /**
     * Create the provided entity instance in the XTDB repository by pushing down the transaction.
     * @param xtdb connectivity
     * @param relationship to create
     * @param entityOneGUID unique identifier of the first endpoint's proxy
     * @param entityTwoGUID unique identifier of the second endpoint's proxy
     * @return Relationship as it was created
     * @throws EntityNotKnownException one of the requested entity proxy's is not known
     * @throws InvalidParameterException one of the parameters is invalid or null
     * @throws RepositoryErrorException on any other error
     */
    public static Relationship transact(XTDBOMRSRepositoryConnector xtdb,
                                        Relationship relationship,
                                        String entityOneGUID,
                                        String entityTwoGUID)
            throws EntityNotKnownException, InvalidParameterException, RepositoryErrorException {
        String docId = RelationshipMapping.getReference(relationship.getGUID());
        Transaction.Builder tx = Transaction.builder();
        tx.invokeFunction(FUNCTION_NAME, EntityProxyMapping.getReference(entityOneGUID), EntityProxyMapping.getReference(entityTwoGUID), relationship);
        TransactionInstant results = xtdb.runTx(tx.build());
        try {
            return xtdb.getResultingRelationship(docId, results, METHOD_NAME);
        } catch (EntityNotKnownException | InvalidParameterException | RepositoryErrorException e) {
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
