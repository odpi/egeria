/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import xtdb.api.IXtdbDatasource;
import xtdb.api.XtdbDocument;

import java.util.Date;

/**
 * Base class that all read operations should implement.
 */
public abstract class AbstractReadOperation {

    protected final XTDBOMRSRepositoryConnector xtdb;
    protected final Date                        asOfTime;
    protected final IXtdbDatasource existingDB;

    /**
     * Default constructor ensures that a consistent datasource is created and used for the entirety of
     * the operation, and further ensures it is fully-closed afterwards irrespective of any exceptions.
     * @param xtdb connectivity to XTDB
     * @param asOfTime (optional) validity time for which to open the datasource
     */
    protected AbstractReadOperation(XTDBOMRSRepositoryConnector xtdb, Date asOfTime) {
        this.xtdb = xtdb;
        this.asOfTime = asOfTime;
        this.existingDB = null;
    }

    /**
     * Alternative constructor to use if an existing datasource is already open that we want to re-use
     * for this operation.
     * @param xtdb connectivity to XTDB
     * @param existingDB already-opened datasource to re-use
     */
    protected AbstractReadOperation(XTDBOMRSRepositoryConnector xtdb, IXtdbDatasource existingDB) {
        this.xtdb = xtdb;
        this.asOfTime = null;
        this.existingDB = existingDB;
    }

    /**
     * Retrieve the requested reference's details from an already-open XTDB datasource.
     * @param db from which to retrieve the details
     * @param reference indicating the primary key of the XTDB object to retrieve
     * @return XtdbDocument of the object's properties
     */
    public static XtdbDocument getXtdbObjectByReference(IXtdbDatasource db, String reference) {
        return db.entity(reference);
    }

}
