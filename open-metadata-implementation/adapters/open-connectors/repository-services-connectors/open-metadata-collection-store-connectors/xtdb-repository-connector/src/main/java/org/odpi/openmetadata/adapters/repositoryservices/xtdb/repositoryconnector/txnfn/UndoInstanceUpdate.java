/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.IPersistentMap;
import clojure.lang.IPersistentVector;
import clojure.lang.PersistentVector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import xtdb.api.ICursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Base transaction function for rolling back the last changes made to an instance's properties or status.
 */
public abstract class UndoInstanceUpdate extends AbstractTransactionFunction {

    /**
     * Retrieve only the current and previous versions of the provided XTDB reference, from an already-opened lazily-
     * evaluated cursor.
     * @param cursor from which to lazily-evaluate the current and previous versions
     * @return IPersistentVector with the current version as the first element, and the previous version as the second element (or null if there is no previous version)
     */
    protected static IPersistentVector getPreviousVersionFromCursor(ICursor<IPersistentMap> cursor) {
        List<IPersistentMap> results = new ArrayList<>();
        // History entries themselves will just be transaction details like the following:
        // { :xtdb.tx/tx-time #inst "2021-02-01T00:28:32.533-00:00",
        //   :xtdb.tx/tx-id 2,
        //   :xtdb.db/valid-time #inst "2021-02-01T00:28:32.531-00:00",
        //   :xtdb.db/content-hash #xtdb/id "...",
        //   :xtdb.api/doc { ... } }
        if (cursor != null) {
            if (cursor.hasNext()) {
                IPersistentMap currentVersionTxn = cursor.next();
                // ... so use these transaction details to retrieve the actual objects
                IPersistentMap current = (IPersistentMap) currentVersionTxn.valAt(Constants.XTDB_DOC);
                if (current != null) {
                    long currentVersion = (Long) current.valAt(Keywords.VERSION);
                    IPersistentMap previous = null;
                    while (previous == null && cursor.hasNext()) {
                        IPersistentMap candidateTxn = cursor.next();
                        // This candidate MUST be a previous _version_ of the instance itself rather than
                        // just a previously-dated view (for example, if an entity had a classification added to it the
                        // entity's version will not have changed but it will have a new historical entry -- this is NOT
                        // a previous _version_ of the entity, and thus must be skipped over)
                        IPersistentMap candidate = (IPersistentMap) candidateTxn.valAt(Constants.XTDB_DOC);
                        long candidateVersion  = (Long) candidate.valAt(Keywords.VERSION);
                        if (candidateVersion < currentVersion) {
                            previous = candidate;
                        }
                    }
                    // If we have exited the loop above with a non-null previous version, then put both into
                    // the results (otherwise do not put anything in the results, as there was no previous
                    // version to include)
                    if (previous != null) {
                        results.add(current);
                        results.add(previous);
                    }
                }
            }
        }
        return PersistentVector.create(results);
    }

}
