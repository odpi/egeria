/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import xtdb.api.*;

import java.io.IOException;
import java.util.*;

/**
 * Implements the 'getRelationshipHistory' operation of the OMRS metadata collection interface.
 */
public class GetRelationshipHistory extends AbstractHistoryOperation {

    /**
     * Create a new relationship history retrieval operation.
     * @param xtdb connectivity to XTDB
     * @param instanceGUID unique identifier of a metadata instance for which to retrieve history
     * @param fromTime the earliest point in time from which to retrieve historical versions of the relationship (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the relationship (exclusive)
     * @param fromElement starting element for paged results
     * @param pageSize the number of results to include per page
     * @param sequencingOrder Enum defining how the results should be ordered.
     */
    public GetRelationshipHistory(XTDBOMRSRepositoryConnector xtdb,
                                  String instanceGUID,
                                  Date fromTime,
                                  Date toTime,
                                  int fromElement,
                                  int pageSize,
                                  HistorySequencingOrder sequencingOrder) {
        super(xtdb, instanceGUID, fromTime, toTime, fromElement, pageSize, sequencingOrder);
    }

    /**
     * Retrieve previous versions of the relationship between the provided dates, ordered as requested.
     * @return {@code List<Relationship>} giving all versions of the relationship within the range requested
     * @throws RelationshipNotKnownException if the requested relationship was not known to the repository during the specified time range
     * @throws RepositoryErrorException if any issue closing the lazy-evaluating cursor
     */
    public List<Relationship> execute() throws RelationshipNotKnownException, RepositoryErrorException {

        final String methodName = "getPreviousVersionsOfRelationship";
        List<Relationship> results = new ArrayList<>();
        String docRef = RelationshipMapping.getReference(instanceGUID);

        boolean noResults;

        IXtdb xtdbAPI = xtdb.getXtdbAPI();
        // Open the database view at the latest point against which we are interested
        try (IXtdbDatasource db = asOfTime == null ? xtdbAPI.openDB() : xtdbAPI.openDB(asOfTime)) {
            List<XtdbDocument> history = getPreviousVersions(db, docRef, fromTime, sequencingOrder);
            noResults = history.isEmpty();

            // Default to the maximum allowable page size if none was specified
            int maxResult = fromElement + (pageSize == 0 ? xtdb.getMaxPageSize() : pageSize);

            int currentIndex = 0;
            // Iterate through every doc received back and retrieve the details of the associated
            // Relationship -- adhering to requested paging parameters...
            for (XtdbDocument version : history) {
                if (currentIndex >= maxResult) {
                    break; // break out if we're beyond the page
                } else if (currentIndex >= fromElement) {
                    RelationshipMapping rm = new RelationshipMapping(xtdb, version, db);
                    Relationship detail = rm.toEgeria();
                    if (detail != null) {
                        results.add(detail);
                    }
                }
                currentIndex++;
            }
        } catch (IOException e) {
            throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                               this.getClass().getName(), methodName, e);
        }

        if (noResults)
            throw new RelationshipNotKnownException(XTDBErrorCode.RELATIONSHIP_NOT_KNOWN.getMessageDefinition(instanceGUID),
                                                    this.getClass().getName(), methodName);

        return results;

    }

}
