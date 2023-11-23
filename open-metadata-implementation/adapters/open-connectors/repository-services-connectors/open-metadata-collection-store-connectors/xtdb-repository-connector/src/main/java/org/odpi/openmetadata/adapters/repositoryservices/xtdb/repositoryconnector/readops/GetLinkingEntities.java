/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntitySummaryMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.InstanceHeaderMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.IXtdb;
import xtdb.api.IXtdbDatasource;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Implements the 'getLinkingEntities' operation of the OMRS metadata collection interface.
 */
public class GetLinkingEntities extends AbstractGraphOperation {

    private static final Logger log = LoggerFactory.getLogger(GetLinkingEntities.class);

    private final String endEntityGUID;

    /**
     * Create a new getLinkingEntities operation.
     * @param xtdb connectivity to XTDB
     * @param startEntityGUID the starting point of the query
     * @param endEntityGUID the finishing point of the query
     * @param limitResultsByStatus list of statuses by which to limit results
     * @param asOfTime the point-in-time for which to retrieve results
     */
    public GetLinkingEntities(XTDBOMRSRepositoryConnector xtdb,
                              String startEntityGUID,
                              String endEntityGUID,
                              List<InstanceStatus> limitResultsByStatus,
                              Date asOfTime) {
        super(xtdb, startEntityGUID, limitResultsByStatus, asOfTime);
        this.endEntityGUID = endEntityGUID;
    }

    /**
     * Execute the getLinkingEntities operation.
     * @return InstanceGraph containing all relationships and entities between the start and end
     * @throws EntityNotKnownException if the requested starting point is not known to the repository
     * @throws RepositoryErrorException if any issue closing an open XTDB resource
     */
    public InstanceGraph execute() throws EntityNotKnownException, RepositoryErrorException {

        final String methodName = "getLinkingEntities";
        InstanceGraph instanceGraph;

        IXtdb xtdbAPI = xtdb.getXtdbAPI();
        // Since a relationship involves not only the relationship object, but also some details from each proxy,
        // we will open a database up-front to re-use for multiple queries (try-with to ensure it is closed after).
        try (IXtdbDatasource db = asOfTime == null ? xtdbAPI.openDB() : xtdbAPI.openDB(asOfTime)) {

            Set<String> entityGUIDsVisited = new HashSet<>();
            Set<String> relationshipGUIDsVisited = new HashSet<>();

            // Start the InstanceGraph off with the entity starting point that was requested
            // (not clear if this is the intended logic, but follows other repository implementations)
            EntitySummary startingEntity = GetEntity.summaryByGuid(xtdb, db, startEntityGUID);

            if (startingEntity == null) {
                throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(
                        startEntityGUID), this.getClass().getName(), methodName);
            }

            entityGUIDsVisited.add(startEntityGUID);

            Set<String> traversedGuids = new HashSet<>();
            traversedGuids.add(startEntityGUID);
            Set<List<?>> successfulTraversals = traverseToEnd(db,
                    startEntityGUID,
                    endEntityGUID,
                    limitResultsByStatus,
                    traversedGuids,
                    1);

            instanceGraph = resultsToGraph(db, successfulTraversals, entityGUIDsVisited, relationshipGUIDsVisited, true);
            if (instanceGraph != null && instanceGraph.getEntities() != null && instanceGraph.getEntities().size() == 1) {
                // If there were no entities other than the starting entity, return an empty graph
                instanceGraph = null;
            }

        } catch (IOException e) {
            throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                               this.getClass().getName(), methodName, e);
        }

        return instanceGraph;

    }

    /**
     * Recursively traverses from the starting entity to the end entity, up to a maximum depth (to avoid potential
     * stack overflow).
     * @param db already opened view of the database at a point-in-time
     * @param startEntityGUID from which to start traversing
     * @param endEntityGUID at which to stop traversing
     * @param limitResultsByStatus to limit which entities are traversed based on their status
     * @param entityGUIDsVisited to avoid traversing the same entity multiple times (for efficiency)
     * @param currentDepth tracks the current depth (once we reach maximum, bail out)
     * @return {@code Set<List<?>>} of the unique combinations of entities and relationships that successfully link between the start and end
     * @see Constants#MAX_TRAVERSAL_DEPTH
     * @throws RepositoryTimeoutException if the query runs longer than the defined threshold (default: 30s)
     */
    private Set<List<?>> traverseToEnd(IXtdbDatasource db,
                                       String startEntityGUID,
                                       String endEntityGUID,
                                       List<InstanceStatus> limitResultsByStatus,
                                       Set<String> entityGUIDsVisited,
                                       int currentDepth) throws RepositoryTimeoutException {

        final String methodName = "traverseToEnd";
        Set<List<?>> consolidated = new LinkedHashSet<>();

        // As long as we have not reached the maximum depth, keep traversing...
        if (currentDepth < Constants.MAX_TRAVERSAL_DEPTH) {
            try {
                Collection<List<?>> nextLevel = findDirectNeighbors(db,
                        startEntityGUID,
                        null,
                        null,
                        limitResultsByStatus,
                        null);
                log.debug("Found traversal results: {}", nextLevel);
                String startRef = EntitySummaryMapping.getReference(startEntityGUID);
                String endRef = EntitySummaryMapping.getReference(endEntityGUID);
                if (nextLevel != null && !nextLevel.isEmpty()) {
                    // As long as there is something to check in the next level, do so...
                    for (List<?> candidateTuple : nextLevel) {
                        String candidateEntityRef = getEntityRefFromGraphTuple(candidateTuple);
                        if (endRef.equals(candidateEntityRef)) {
                            // If we found the endEntityGUID in the results, add it to the set of successful traversals
                            consolidated.add(candidateTuple);
                        } else if (!startRef.equals(candidateEntityRef)) {
                            // Otherwise, so long as we have not circled back to the starting point, continue traversing
                            String nextStartGuid = InstanceHeaderMapping.trimGuidFromReference(candidateEntityRef);
                            if (!entityGUIDsVisited.contains(nextStartGuid)) {
                                // If we have not already traversed this GUID, continue traversing...
                                entityGUIDsVisited.add(nextStartGuid);
                                Set<List<?>> nextTraversal = traverseToEnd(db,
                                        nextStartGuid,
                                        endEntityGUID,
                                        limitResultsByStatus,
                                        entityGUIDsVisited,
                                        currentDepth + 1);
                                // If the traversal returns a non-empty result, it was successful
                                if (!nextTraversal.isEmpty()) {
                                    // So add the traversal up to now (since it led to a successful outcome)
                                    consolidated.add(candidateTuple);
                                    // And the successful sub-traversal(s) that were returned
                                    consolidated.addAll(nextTraversal);
                                }
                            }
                        }
                    }
                }
            } catch (TimeoutException e) {
                throw new RepositoryTimeoutException(XTDBErrorCode.QUERY_TIMEOUT.getMessageDefinition(xtdb.getRepositoryName()),
                                                     this.getClass().getName(), methodName, e);
            }
        }

        // return the consolidated set of successful traversals, or null if none were found
        return consolidated;

    }

}
