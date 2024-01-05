/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.IPersistentMap;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EnumPropertyValueMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;

/**
 * Base transaction function for updating an instance's home metadata repository.
 */
public abstract class ReHomeInstance extends AbstractTransactionFunction {

    /**
     * Makes the necessary changes to update a metadata instance's home metadata repository.
     *
     * @param userId doing the update
     * @param existing metadata instance
     * @param newHomeMetadataCollectionId unique identifier of the instance's new home
     * @param newHomeMetadataCollectionName name of the instance's new home
     * @return IPersistentMap giving the updated instance representation
     */
    protected static IPersistentMap reHomeInstance(String userId,
                                                   IPersistentMap existing,
                                                   String newHomeMetadataCollectionId,
                                                   String newHomeMetadataCollectionName) {
        IPersistentMap doc = incrementVersion(userId, existing);
        return doc
                .assoc(Keywords.METADATA_COLLECTION_ID, newHomeMetadataCollectionId)
                .assoc(Keywords.METADATA_COLLECTION_NAME, newHomeMetadataCollectionName)
                .assoc(Keywords.INSTANCE_PROVENANCE_TYPE, EnumPropertyValueMapping.getOrdinalForInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT));
    }

    /**
     * Validate the re-home. The instance must originate in the liocal cohort and NOT be locally homed already,
     * or the entity must have an instanceProvenanceType set to external and a replicatedBy that is NOT the local
     * metadataCollectionId. Any other combination indicates a non-reference copy, and therefore cannot be re-homed.
     *
     * @param existing metadata instance
     * @param metadataCollectionId of the metadata instance
     * @param className calling class
     * @param methodName calling method
     * @throws InvalidParameterException if the instance cannot be re-homed
     */
    protected static void validate(IPersistentMap existing,
                                   String metadataCollectionId,
                                   String className,
                                   String methodName)
            throws InvalidParameterException {

        boolean updateAllowed = false;

        Integer instanceProvenance = getInstanceProvenanceType(existing);
        if (instanceProvenance != null) {
            if (instanceProvenance == InstanceProvenanceType.LOCAL_COHORT.getOrdinal()) {
                String entityHome = getMetadataCollectionId(existing);
                if (entityHome != null && !entityHome.equals(metadataCollectionId)) {
                    updateAllowed = true;
                }
            } else if (instanceProvenance == InstanceProvenanceType.EXTERNAL_SOURCE.getOrdinal()) {
                String replicatedBy = (String) existing.valAt(Keywords.REPLICATED_BY);
                if (replicatedBy != null && !replicatedBy.equals(metadataCollectionId)) {
                    updateAllowed = true;
                }
            }
        }

        if (!updateAllowed) {
            throw new InvalidParameterException(XTDBErrorCode.INSTANCE_HOME_NOT_LOCAL.getMessageDefinition(getMetadataCollectionId(existing),
                                                                                                           getGUID(existing),
                                                                                                           metadataCollectionId),
                                                className,
                                                methodName,
                                                "instance");
        }

    }

}
