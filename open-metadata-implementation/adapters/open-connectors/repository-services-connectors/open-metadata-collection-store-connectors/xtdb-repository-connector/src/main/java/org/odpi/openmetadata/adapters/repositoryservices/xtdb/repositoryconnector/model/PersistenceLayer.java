/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model;

import xtdb.api.XtdbDocument;
import xtdb.api.IXtdb;
import xtdb.api.TransactionInstant;
import xtdb.api.tx.Transaction;

/**
 * Manages the persistence layer: for now, primarily indicating the version of the connector for
 * which data has been persisted, to validate migration starting points.
 */
public class PersistenceLayer {

    public static final long LATEST_VERSION = 3L;

    private static final String PERSISTENCE_DOC = "egeria-connector-xtdb/persistence-layer";
    private static final String VERSION = "egeria-connector-xtdb/persistence-layer.version";

    private PersistenceLayer() {}

    /**
     * Retrieve the version of the persistence layer for the provided XTDB back-end.
     * @param xtdbAPI the XTDB back-end for which to retrieve the persistence layer version
     * @return the version of the persistence layer
     */
    public static long getVersion(IXtdb xtdbAPI) {
        XtdbDocument doc = getPersistenceDetails(xtdbAPI);
        if (doc != null) {
            Object version = doc.get(VERSION);
            if (version instanceof Long) {
                return (Long) version;
            }
        }
        return -1;
    }

    /**
     * Set the version of the persistence layer for the provided XTDB back-end.
     * CAUTION: this is used for migration purposes, so should not be set by anything other than
     * migration utilities.
     * @param xtdbAPI the XTDB back-end for which to set the persistence layer version
     * @param version to set
     */
    public static void setVersion(IXtdb xtdbAPI, long version) {
        XtdbDocument.Builder builder = XtdbDocument.builder(PERSISTENCE_DOC);
        builder.put(VERSION, version);
        Transaction.Builder tx = Transaction.builder();
        tx.put(builder.build());
        TransactionInstant instant = xtdbAPI.submitTx(tx.build());
        xtdbAPI.awaitTx(instant, null);
    }

    /**
     * Check whether the persistence layer for the provided XTDB back-end is the latest or not.
     * @param xtdbAPI the XTDB back-end for which to check the persistence layer version
     * @return true if it is at the latest version, otherwise false
     */
    public static boolean isLatestVersion(IXtdb xtdbAPI) {
        return getVersion(xtdbAPI) == LATEST_VERSION;
    }

    /**
     * Retrieve details about the persistence layer from the provided XTDB back-end.
     * @param xtdbAPI the XTDB back-end for which to retrieve the persistence layer details
     * @return XtdbDocument containing the details (or null if there are none)
     */
    protected static XtdbDocument getPersistenceDetails(IXtdb xtdbAPI) {
        return xtdbAPI.db().entity(PERSISTENCE_DOC);
    }

}
