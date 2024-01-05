/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import xtdb.api.HistoryOptions;
import xtdb.api.ICursor;
import xtdb.api.IXtdbDatasource;
import xtdb.api.XtdbDocument;

import java.util.*;

/**
 * Base class that all metadata instance history retrieval operations should implement.
 */
public abstract class AbstractHistoryOperation extends AbstractReadOperation {

    protected final String instanceGUID;
    protected final Date fromTime;
    protected final int fromElement;
    protected final int pageSize;
    protected final HistorySequencingOrder sequencingOrder;

    /**
     * Create a new history retrieval operation.
     * @param xtdb connectivity to XTDB
     * @param instanceGUID unique identifier of a metadata instance for which to retrieve history
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param fromElement starting element for paged results
     * @param pageSize the number of results to include per page
     * @param sequencingOrder Enum defining how the results should be ordered.
     */
    protected AbstractHistoryOperation(XTDBOMRSRepositoryConnector xtdb,
                                       String instanceGUID,
                                       Date fromTime,
                                       Date toTime,
                                       int fromElement,
                                       int pageSize,
                                       HistorySequencingOrder sequencingOrder) {
        super(xtdb, toTime);
        this.instanceGUID = instanceGUID;
        this.fromTime = fromTime;
        this.fromElement = fromElement;
        this.pageSize = pageSize;
        this.sequencingOrder = sequencingOrder;
    }

    /**
     * Retrieve the previous versions of the provided XTDB object, from an already-opened point-in-time view of the
     * repository back to the earliest point in time defined by the 'earliest' parameter.
     * @param db from which to retrieve the previous version
     * @param reference indicating the primary key of the object for which to retrieve the previous version
     * @param earliest the earliest version to retrieve
     * @param order indicating either chronological (forward) or reverse-chronological (backward) ordering of results
     * @return {@code List<XtdbDocument>} with all versions of the XTDB object back to the earliest point specified, ordered as requested
     * @throws RepositoryErrorException if any issue closing the lazy-evaluating cursor
     */
    protected List<XtdbDocument> getPreviousVersions(IXtdbDatasource db,
                                                     String reference,
                                                     Date earliest,
                                                     HistorySequencingOrder order) throws RepositoryErrorException {

        final String methodName = "getPreviousVersions";

        // Note: we will always retrieve the versions in reverse-order, since they are lazily-evaluated. This will
        // avoid the need to retrieve history that goes before the 'earliest' date and compare it, but does mean that
        // the results of our looping may need to reverse-order the resulting array if the sort order requested is
        // 'forward' (chronological)
        HistoryOptions options = HistoryOptions.create(HistoryOptions.SortOrder.DESC).withDocs(true);
        List<XtdbDocument> results;

        // try-with to ensure that the ICursor resource is closed, even if any exception is thrown
        try (ICursor<Map<Keyword, ?>> lazyCursor = db.openEntityHistory(reference, options)) {
            // Note that here we will not pass-through the opened DB as this method will use the embedded document
            // retrieval in the response to see the point-in-time information for the metadata instance
            results = getPreviousVersionsFromCursor(lazyCursor, earliest, order);
        } catch (Exception e) {
            throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                               this.getClass().getName(), methodName, e);
        }

        return results;

    }

    /**
     * Retrieve the previous versions of the provided XTDB reference up to the earliest point requested, from an
     * already-opened lazily-evaluated cursor.
     * @param cursor from which to lazily-evaluate the current and previous versions
     * @param earliest the earliest version to retrieve
     * @param order indicating either chronological (forward) or reverse-chronological (backward) ordering of results
     * @return {@code List<XtdbDocument>} with all versions of the XTDB object back to the earliest point specified
     */
    private List<XtdbDocument> getPreviousVersionsFromCursor(ICursor<Map<Keyword, ?>> cursor,
                                                             Date earliest,
                                                             HistorySequencingOrder order) {
        List<XtdbDocument> results = new ArrayList<>();
        // History entries themselves will just be transaction details like the following:
        // { :xtdb.tx/tx-time #inst "2021-02-01T00:28:32.533-00:00",
        //   :xtdb.tx/tx-id 2,
        //   :xtdb.db/valid-time #inst "2021-02-01T00:28:32.531-00:00",
        //   :xtdb.db/content-hash #xtdb/id "...",
        //   :xtdb.db/doc {...} }
        if (cursor != null) {
            while (cursor.hasNext()) {
                Map<Keyword, ?> version = cursor.next();
                Date versionValidFrom = (Date) version.get(Constants.XTDB_VALID_TIME);
                // Recall that the ordering requested from XTDB is always in reverse, so we will always be building up
                // the results array in reverse-order to best leverage its lazy evaluation
                // (Also, given that the earliest date could be null to indicate 'all history', we should force our
                // comparator to always continue the loop if the earliest date is null
                int comparator = earliest == null ? 1 : versionValidFrom.compareTo(earliest);
                IPersistentMap docVersion = (IPersistentMap) version.get(Constants.XTDB_DOC);
                if (docVersion != null) {
                    results.add(XtdbDocument.factory(docVersion));
                }
                if (comparator <= 0) {
                    // If the version we are examining is either the first one we see before our earliest date cut-off,
                    // or precisely on that earliest date cut-off, we have the final version we should include so
                    // we can now break out of the loop
                    break;
                }
                // (Otherwise, we are still within the versions to include, so simply continue looping)
            }
        }

        // Note: our results are reverse-chronological by default due to lazy evaluation, but if we were requested to
        // return them in forward chronological order we should reverse the array
        if (order.equals(HistorySequencingOrder.FORWARDS)) {
            Collections.reverse(results);
        }

        return results;

    }

}
