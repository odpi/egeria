/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.IPersistentMap;
import clojure.lang.IPersistentVector;
import clojure.lang.Tuple;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EnumPropertyValueMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.io.IOException;

/**
 * Base transaction function for deleting instances.
 */
public abstract class ReIdentifyInstance extends AbstractTransactionFunction {

    /**
     * Makes the necessary changes to change the unique identifier of an instance.
     *
     * @param userId doing the re-identification
     * @param existing metadata instance
     * @param instanceGUID existing unique identifier of the metadata instance
     * @param newInstanceGUID new unique identifier to use for the metadata instance
     * @return IPersistentVector tuple giving the original metadata instance followed by the re-identified metadata instance
     */
    protected static IPersistentVector reIdentifyInstance(String userId,
                                                          IPersistentMap existing,
                                                          String instanceGUID,
                                                          String newInstanceGUID) {

        IPersistentMap deleted = incrementVersion(userId, existing);
        deleted = deleted
                .assoc(Keywords.STATUS_ON_DELETE, deleted.valAt(Keywords.CURRENT_STATUS))
                .assoc(Keywords.CURRENT_STATUS, EnumPropertyValueMapping.getOrdinalForInstanceStatus(InstanceStatus.DELETED));

        IPersistentMap updated = incrementVersion(userId, existing);
        updated = updated
                .assoc(Constants.XTDB_PK, newInstanceGUID)
                .assoc(Keywords.RE_IDENTIFIED_FROM_GUID, instanceGUID);

        return Tuple.create(deleted, updated);

    }

    /**
     * Validate the status re-identification.
     *
     * @param existing metadata instance
     * @param instanceGUID unique identifier of the metadata instance
     * @param metadataCollectionId of the metadata instance
     * @param className calling class
     * @param methodName calling method
     * @throws InvalidParameterException on any null or invalid parameters
     * @throws IOException on any error deserializing values
     * @throws RepositoryErrorException on any other error
     */
    protected static void validate(IPersistentMap existing,
                                   String instanceGUID,
                                   String metadataCollectionId,
                                   String className,
                                   String methodName)
            throws InvalidParameterException, RepositoryErrorException, IOException {
        TxnValidations.instanceCanBeUpdated(existing, instanceGUID, metadataCollectionId, className, methodName);
    }

}
