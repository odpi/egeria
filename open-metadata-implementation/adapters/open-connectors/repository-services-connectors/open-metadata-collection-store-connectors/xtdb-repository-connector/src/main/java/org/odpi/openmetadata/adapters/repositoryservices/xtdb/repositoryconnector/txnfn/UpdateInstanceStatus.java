/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.IPersistentMap;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;

import java.io.IOException;

/**
 * Base transaction function for updating an instance's status.
 */
public abstract class UpdateInstanceStatus extends AbstractTransactionFunction {

    /**
     * Makes the necessary changes to update a metadata instance's status.
     *
     * @param userId doing the update
     * @param existing metadata instance
     * @param instanceStatus new status for the instance
     * @return IPersistentMap giving the updated instance representation
     */
    protected static IPersistentMap updateInstanceStatus(String userId,
                                                         IPersistentMap existing,
                                                         int instanceStatus) {
        IPersistentMap doc = incrementVersion(userId, existing);
        return doc.assoc(Keywords.CURRENT_STATUS, instanceStatus);
    }

    /**
     * Validate the status change.
     *
     * @param existing metadata instance
     * @param instanceGUID unique identifier of the metadata instance
     * @param metadataCollectionId of the metadata instance
     * @param instanceStatus new status for the metadata instance
     * @param className calling class
     * @param methodName calling method
     * @throws InvalidParameterException on any null or invalid parameters
     * @throws IOException on any error deserializing values
     * @throws StatusNotSupportedException if the provided status is invalid for the metadata instance
     * @throws RepositoryErrorException on any other error
     */
    protected static void validate(IPersistentMap existing,
                                   String instanceGUID,
                                   String metadataCollectionId,
                                   Integer instanceStatus,
                                   String className,
                                   String methodName)
            throws InvalidParameterException, RepositoryErrorException, IOException, StatusNotSupportedException {
        TxnValidations.instanceIsNotDeleted(existing, instanceGUID, className, methodName);
        TxnValidations.instanceCanBeUpdated(existing, instanceGUID, metadataCollectionId, className, methodName);
        TxnValidations.instanceType(existing, className, methodName);
        TxnValidations.requiredProperty(instanceGUID, Keywords.CURRENT_STATUS.getName(), instanceStatus, className, methodName);
        TypeDef typeDef = getTypeDefForInstance(existing);
        TxnValidations.instanceStatus(instanceStatus, typeDef, className, methodName);
    }

}
