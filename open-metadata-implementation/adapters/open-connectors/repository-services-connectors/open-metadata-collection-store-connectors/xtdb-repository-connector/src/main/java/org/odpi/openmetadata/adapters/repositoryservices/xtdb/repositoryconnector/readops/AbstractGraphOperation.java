/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import clojure.lang.IPersistentMap;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.Constants;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.InstanceHeaderMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.model.search.XTDBGraphQuery;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xtdb.api.IXtdbDatasource;

import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Base class that all graph operations should implement.
 */
public abstract class AbstractGraphOperation extends AbstractReadOperation {

    private static final Logger log = LoggerFactory.getLogger(AbstractGraphOperation.class);

    protected final String startEntityGUID;
    protected final List<InstanceStatus> limitResultsByStatus;

    /**
     * Create a new search operation.
     * @param xtdb connectivity to XTDB
     * @param startEntityGUID unique identifier of the starting entity for the traversal
     * @param limitResultsByStatus list of statuses by which to limit results
     * @param asOfTime the point-in-time for which to retrieve results
     */
    protected AbstractGraphOperation(XTDBOMRSRepositoryConnector xtdb,
                                     String startEntityGUID,
                                     List<InstanceStatus> limitResultsByStatus,
                                     Date asOfTime) {
        super(xtdb, asOfTime);
        this.startEntityGUID = startEntityGUID;
        this.limitResultsByStatus = limitResultsByStatus;
    }

    /**
     * Find the entities and relationships that radiate out from the supplied entity GUID.
     * The results are scoped by the provided type GUIDs, other limiters, and the level.
     * @param db already-opened point-in-time view of the datasource from which to retrieve
     * @param entityTypeGUIDs list of entity types to include in the query results (null means include all)
     * @param relationshipTypeGUIDs list of relationship types to include in the query results (null means include all)
     * @param limitResultsByClassification list of classifications that must be present on all returned entities
     * @param level the number of relationships out from the starting entity that
     * @param includeRelationships whether to include relationships in the resulting graph (true) or not (false)
     * @return InstanceGraph of the neighborhood
     * @throws EntityNotKnownException if the starting point of the neighborhood traversals cannot be found in the repository
     * @throws RepositoryErrorException if any issue closing open XTDB resources, or if the query runs longer than the defined threshold (default: 30s)
     */
    protected InstanceGraph findNeighborhood(IXtdbDatasource db,
                                             List<String> entityTypeGUIDs,
                                             List<String> relationshipTypeGUIDs,
                                             List<String> limitResultsByClassification,
                                             int level,
                                             boolean includeRelationships) throws
            EntityNotKnownException,
            RepositoryErrorException {

        final String methodName = "findNeighborhood";

        Set<List<?>> consolidated = new LinkedHashSet<>();

        Set<String> entityGUIDsRetrieved = new HashSet<>();
        Set<String> relationshipGUIDsRetrieved = new HashSet<>();
        Set<String> entityGUIDsVisited = new HashSet<>();
        Set<String> relationshipGUIDsVisited = new HashSet<>();
        List<String> nextEntityGUIDs = new ArrayList<>();
        nextEntityGUIDs.add(startEntityGUID);

        EntitySummary startingEntity = GetEntity.summaryByGuid(xtdb, db, startEntityGUID);

        if (startingEntity == null) {
            throw new EntityNotKnownException(XTDBErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(startEntityGUID),
                                              this.getClass().getName(), methodName);
        }

        entityGUIDsRetrieved.add(startEntityGUID);

        int levelTraversed = 0;
        int totalLevels = level;
        if (totalLevels < 0) {
            // If the level is negative, it means keep going until we run out of new traversals (or crash, presumably?)
            // We will set a maximum...
            totalLevels = Constants.MAX_TRAVERSAL_DEPTH;
        }
        if (totalLevels > 0) {

            do {
                Set<List<?>> nextGraph = getNextLevelNeighbors(db,
                        nextEntityGUIDs,
                        entityTypeGUIDs,
                        relationshipTypeGUIDs,
                        limitResultsByStatus,
                        limitResultsByClassification,
                        entityGUIDsVisited,
                        relationshipGUIDsVisited);
                entityGUIDsVisited.addAll(nextEntityGUIDs);
                levelTraversed++;
                // Add this subset of results into the consolidated set of results
                consolidated.addAll(nextGraph);
                // Retrieve the next set of entity GUIDs to traverse, but remove any already-visited ones from
                // the list prior to iterating again
                nextEntityGUIDs = getEntityGUIDsFromGraphResults(nextGraph);
                nextEntityGUIDs.removeAll(entityGUIDsVisited);
                // Once we either run out of GUIDs to traverse, or we've reached the desired level, we stop iterating
            } while (!nextEntityGUIDs.isEmpty() && levelTraversed < totalLevels);
        }

        return resultsToGraph(db, consolidated, entityGUIDsRetrieved, relationshipGUIDsRetrieved, includeRelationships);

    }

    /**
     * Find the entities and relationships that radiate out directly from the supplied list of entity GUIDs.
     * The results are scoped by the provided type GUIDs, other limiters, and the level.
     * @param db already opened point-in-time view of the database
     * @param startingPoints list of entity GUIDs from which we should start radiating outwards
     * @param entityTypeGUIDs list of entity types to include in the query results (null means include all)
     * @param relationshipTypeGUIDs list of relationship types to include in the query results (null means include all)
     * @param limitResultsByStatus list of statuses to restrict results (null means include all)
     * @param limitResultsByClassification list of classifications that must be present on all returned entities
     * @param entityGUIDsVisited set of unique identifiers of entities that have already been visited
     * @param relationshipGUIDsVisited set of unique identifiers of relationships that have already been visited
     * @return {@code Set<List<?>>} of the immediate neighbors of the specified starting point GUIDs, as graph tuples [[:entityRef :relationshipRef]]
     * @throws RepositoryTimeoutException if the query runs longer than the defined threshold (default: 30s)
     */
    protected Set<List<?>> getNextLevelNeighbors(IXtdbDatasource db,
                                                 List<String> startingPoints,
                                                 List<String> entityTypeGUIDs,
                                                 List<String> relationshipTypeGUIDs,
                                                 List<InstanceStatus> limitResultsByStatus,
                                                 List<String> limitResultsByClassification,
                                                 Set<String> entityGUIDsVisited,
                                                 Set<String> relationshipGUIDsVisited) throws RepositoryTimeoutException {

        final String methodName = "getNextLevelNeighbors";
        Set<List<?>> consolidated = new LinkedHashSet<>();

        try {
            // Iterate through the provided starting entity starting points to retrieve the next level of neighbors
            for (String entityGUID : startingPoints) {
                // Results here will be a collection of tuples: [:entity/... :relationship/...]
                Collection<List<?>> nextDegree = findDirectNeighbors(db,
                        entityGUID,
                        entityTypeGUIDs,
                        relationshipTypeGUIDs,
                        limitResultsByStatus,
                        limitResultsByClassification);
                log.debug("Found neighborhood results: {}", nextDegree);
                for (List<?> candidateTuple : nextDegree) {
                    String candidateEntityRef = getEntityRefFromGraphTuple(candidateTuple);
                    String candidateRelationshipRef = getRelationshipRefFromGraphTuple(candidateTuple);
                    String entityGuid = InstanceHeaderMapping.trimGuidFromReference(candidateEntityRef);
                    String relationshipGuid = InstanceHeaderMapping.trimGuidFromReference(candidateRelationshipRef);
                    if (!entityGUIDsVisited.contains(entityGuid) || !relationshipGUIDsVisited.contains(relationshipGuid)) {
                        // If either the entity or the relationship has not been seen, add the tuple
                        consolidated.add(candidateTuple);
                        entityGUIDsVisited.add(entityGUID);
                        relationshipGUIDsVisited.add(relationshipGuid);
                    }
                }
            }
        } catch (TimeoutException e) {
            throw new RepositoryTimeoutException(XTDBErrorCode.QUERY_TIMEOUT.getMessageDefinition(xtdb.getRepositoryName()),
                                                 this.getClass().getName(), methodName, e);
        }

        return consolidated;

    }

    /**
     * Find the immediate neighbors (1-degree separated entities and the relationships between) using the provided criteria.
     * @param db already opened point-in-view of the database
     * @param entityGUID of the entity for which to find immediate relationships
     * @param entityTypeGUIDs of the entity type definitions by which to restrict entities in the results
     * @param relationshipTypeGUIDs of the relationship type definitions by which to restrict relationships in the results
     * @param limitResultsByStatus by which to limit relationships
     * @param limitResultsByClassification by which to limit the entities in the results
     * @return {@code Collection<List<?>>} of tuples of relationships and entities found in the results
     * @throws TimeoutException if the query runs longer than the defined threshold (default: 30s)
     */
    protected Collection<List<?>> findDirectNeighbors(IXtdbDatasource db,
                                                      String entityGUID,
                                                      List<String> entityTypeGUIDs,
                                                      List<String> relationshipTypeGUIDs,
                                                      List<InstanceStatus> limitResultsByStatus,
                                                      List<String> limitResultsByClassification) throws TimeoutException {
        XTDBGraphQuery query = new XTDBGraphQuery();
        query.addRelationshipLimiters(entityGUID, relationshipTypeGUIDs, limitResultsByStatus);
        query.addEntityLimiters(entityTypeGUIDs, limitResultsByClassification, limitResultsByStatus);
        IPersistentMap q = query.getQuery();
        log.debug(Constants.QUERY_WITH, q);
        return db.query(q);
    }

    /**
     * Translate the collection of XTDB tuple results (from a graph query) into an Egeria InstanceGraph.
     * @param db already opened point-in-time view of the database
     * @param xtdbResults list of result tuples, e.g. from a neighborhood or other graph search
     * @param entityGUIDsVisited the list of entity GUIDs that have already been retrieved
     * @param relationshipGUIDsVisited the list of relationship GUIDs that have already been retrieved
     * @param includeRelationships whether to include relationships in the resulting graph (true) or not (false)
     * @return InstanceGraph
     */
    protected InstanceGraph resultsToGraph(IXtdbDatasource db,
                                           Collection<List<?>> xtdbResults,
                                           Set<String> entityGUIDsVisited,
                                           Set<String> relationshipGUIDsVisited,
                                           boolean includeRelationships) {

        final String methodName = "resultsToGraph";

        // Start the InstanceGraph off with the entity starting point that was requested
        // (not clear if this is the intended logic, but follows other repository implementations)
        InstanceGraph results = new InstanceGraph();
        List<EntityDetail> entities = new ArrayList<>();
        List<Relationship> relationships = new ArrayList<>();
        try {
            EntityDetail startingEntity = GetEntity.detailByGuid(xtdb, db, startEntityGUID);
            entities.add(startingEntity);
        } catch (EntityProxyOnlyException e) {
            log.debug("Starting entity for traversals was only a proxy, not adding it to results: {}", startEntityGUID);
        }
        entityGUIDsVisited.add(startEntityGUID);

        if (xtdbResults != null) {

            for (List<?> xtdbResult : xtdbResults) {
                String entityRef = getEntityRefFromGraphTuple(xtdbResult);
                String entityGuid = InstanceHeaderMapping.trimGuidFromReference(entityRef);
                if (!entityGUIDsVisited.contains(entityGuid)) {
                    try {
                        EntityDetail entity = GetEntity.detailByRef(xtdb, db, entityRef);
                        if (entity == null) {
                            xtdb.logProblem(this.getClass().getName(),
                                            methodName,
                                            XTDBAuditCode.MAPPING_FAILURE,
                                            null,
                                            "entity",
                                            entityRef,
                                            "cannot be translated to EntityDetail");
                        } else {
                            entities.add(entity);
                        }
                    } catch (EntityProxyOnlyException e) {
                        log.debug("Found only a proxy in graph traversal, not including in results: {}", entityGuid);
                    }
                    entityGUIDsVisited.add(entityGuid);
                }
                if (includeRelationships) {
                    String relationshipRef = getRelationshipRefFromGraphTuple(xtdbResult);
                    String relationshipGuid = InstanceHeaderMapping.trimGuidFromReference(relationshipRef);
                    if (!relationshipGUIDsVisited.contains(relationshipGuid)) {
                        Relationship relationship = GetRelationship.byRef(xtdb, db, relationshipRef);
                        relationshipGUIDsVisited.add(relationshipGuid);
                        if (relationship == null) {
                            xtdb.logProblem(this.getClass().getName(),
                                            methodName,
                                            XTDBAuditCode.MAPPING_FAILURE,
                                            null,
                                            "relationship",
                                            relationshipRef,
                                            "cannot be translated to Relationship");
                        } else {
                            relationships.add(relationship);
                        }
                    }
                }
            }

        }

        results.setEntities(entities);
        results.setRelationships(relationships);
        return results;

    }

    /**
     * Retrieve the unique set of entity GUIDs from the provided graph query results.
     * @param xtdbResults graph query results
     * @return {@code List<String>}
     */
    private List<String> getEntityGUIDsFromGraphResults(Collection<List<?>> xtdbResults) {
        List<String> list = new ArrayList<>();
        for (List<?> result : xtdbResults) {
            String entityRef = getEntityRefFromGraphTuple(result);
            if (entityRef != null) {
                String guid = InstanceHeaderMapping.trimGuidFromReference(entityRef);
                if (!list.contains(guid)) {
                    list.add(guid);
                }
            }
        }
        return list;
    }

    /**
     * Retrieve the entity reference from the provided graph query result.
     * @param tuple graph query result
     * @return String reference for the entity
     */
    protected String getEntityRefFromGraphTuple(List<?> tuple) {
        return tuple == null ? null : (String) tuple.get(0);
    }

    /**
     * Retrieve the relationship reference from the provided graph query result.
     * @param tuple graph query result
     * @return String reference for the relationship
     */
    private String getRelationshipRefFromGraphTuple(List<?> tuple) {
        return tuple == null ? null : (String) tuple.get(1);
    }

}
