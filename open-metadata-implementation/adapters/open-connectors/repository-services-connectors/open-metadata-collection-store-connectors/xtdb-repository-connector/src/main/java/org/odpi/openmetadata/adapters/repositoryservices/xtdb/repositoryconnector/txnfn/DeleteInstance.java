/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.IPersistentMap;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EnumPropertyValueMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

/**
 * Base transaction function for deleting instances.
 */
public abstract class DeleteInstance extends AbstractTransactionFunction {

    /**
     * Makes the necessary changes to mark a metadata instance as soft-deleted.
     *
     * @param userId doing the deletion
     * @param existing metadata instance
     * @return IPersistentMap giving the deleted instance representation
     */
    protected static IPersistentMap deleteInstance(String userId,
                                                   IPersistentMap existing) {
        IPersistentMap doc = incrementVersion(userId, existing);
        return doc
                .assoc(Keywords.STATUS_ON_DELETE, doc.valAt(Keywords.CURRENT_STATUS))
                .assoc(Keywords.CURRENT_STATUS, EnumPropertyValueMapping.getOrdinalForInstanceStatus(InstanceStatus.DELETED));
    }

}
