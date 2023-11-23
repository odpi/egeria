/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.IPersistentMap;

/**
 * Base transaction function for restoring a soft-deleted instance back to active status.
 */
public abstract class RestoreInstance extends AbstractTransactionFunction {

    /**
     * Makes the necessary changes to restore a metadata instance that was soft-deleted.
     *
     * @param userId doing the restore
     * @param existing metadata instance
     * @return IPersistentMap giving the restored instance representation
     */
    protected static IPersistentMap restoreInstance(String userId,
                                                    IPersistentMap existing) {
        IPersistentMap doc = incrementVersion(userId, existing);
        return doc
                .assoc(Keywords.CURRENT_STATUS, doc.valAt(Keywords.STATUS_ON_DELETE))
                .assoc(Keywords.STATUS_ON_DELETE, null);
    }

}
