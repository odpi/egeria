/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntitySummaryMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import xtdb.api.*;

import java.io.IOException;
import java.util.*;

/**
 * Implements the 'getEntityDetailHistory' operation of the OMRS metadata collection interface.
 */
public class GetEntityHistory extends AbstractHistoryOperation {

    /**
     * Create a new entity history retrieval operation.
     * @param xtdb connectivity to XTDB
     * @param instanceGUID unique identifier of a metadata instance for which to retrieve history
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param fromElement starting element for paged results
     * @param pageSize the number of results to include per page
     * @param sequencingOrder Enum defining how the results should be ordered.
     */
    public GetEntityHistory(XTDBOMRSRepositoryConnector xtdb,
                            String instanceGUID,
                            Date fromTime,
                            Date toTime,
                            int fromElement,
                            int pageSize,
                            HistorySequencingOrder sequencingOrder) {
        super(xtdb, instanceGUID, fromTime, toTime, fromElement, pageSize, sequencingOrder);
    }

    /**
     * Retrieve previous versions of the entity between the provided dates, ordered as requested.
     * @return {@code List<EntityDetail>} giving all versions of the entity within the range requested
     * @throws EntityNotKnownException if the requested entity was not known to the repository during the specified time range
     * @throws RepositoryErrorException if any issue closing the lazy-evaluating cursor
     */
    public List<EntityDetail> execute() throws EntityNotKnownException, RepositoryErrorException {

        final String methodName = "getPreviousVersionsOfEntity";
        List<EntityDetail> results = new ArrayList<>();
        String docRef = EntitySummaryMapping.getReference(instanceGUID);

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
            // EntityDetail -- adhering to requested paging parameters...
            for (XtdbDocument version : history) {
                if (currentIndex >= maxResult) {
                    break; // break out if we're beyond the page
                } else if (currentIndex >= fromElement) {
                    EntityDetailMapping edm = new EntityDetailMapping(xtdb, version);
                    EntityDetail detail = edm.toEgeria();
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
            throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(instanceGUID),
                                              this.getClass().getName(), methodName);

        return results;

    }

}
