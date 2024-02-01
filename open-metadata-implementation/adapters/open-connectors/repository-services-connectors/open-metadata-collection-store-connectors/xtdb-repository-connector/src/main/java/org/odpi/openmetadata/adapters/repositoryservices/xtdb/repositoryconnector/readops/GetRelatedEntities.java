/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import xtdb.api.IXtdb;
import xtdb.api.IXtdbDatasource;

import java.io.IOException;
import java.util.*;

/**
 * Implements the 'getRelatedEntities' operation of the OMRS metadata collection interface.
 */
public class GetRelatedEntities extends AbstractGraphOperation {

    private final List<String> entityTypeGUIDs;
    private final List<String> limitResultsByClassification;

    /**
     * Create a new getRelatedEntities operation.
     * @param xtdb connectivity to XTDB
     * @param entityGUID the starting point of the query
     * @param entityTypeGUIDs list of entity types to include in the query results (null means include all entities found, irrespective of their type)
     * @param limitResultsByStatus list of statuses by which to limit results
     * @param limitResultsByClassification list of classifications that must be present on all returned entities
     * @param asOfTime the point-in-time for which to retrieve results
     */
    public GetRelatedEntities(XTDBOMRSRepositoryConnector xtdb,
                              String entityGUID,
                              List<String> entityTypeGUIDs,
                              List<InstanceStatus> limitResultsByStatus,
                              List<String> limitResultsByClassification,
                              Date asOfTime) {
        super(xtdb, entityGUID, limitResultsByStatus, asOfTime);
        this.entityTypeGUIDs = entityTypeGUIDs;
        this.limitResultsByClassification = limitResultsByClassification;
    }

    /**
     * Execute the getRelatedEntities operation.
     * @return InstanceGraph of results
     * @throws EntityNotKnownException if the starting entity for the traversal cannot be found
     * @throws RepositoryErrorException on any other connectivity or resource usage error
     */
    public InstanceGraph execute() throws EntityNotKnownException, RepositoryErrorException {

        final String methodName = "getRelatedEntities";
        InstanceGraph instanceGraph;

        IXtdb xtdbAPI = xtdb.getXtdbAPI();
        // Since a relationship involves not only the relationship object, but also some details from each proxy,
        // we will open a database up-front to re-use for multiple queries (try-with to ensure it is closed after).
        try (IXtdbDatasource db = asOfTime == null ? xtdbAPI.openDB() : xtdbAPI.openDB(asOfTime)) {
            instanceGraph = findNeighborhood(db,
                    entityTypeGUIDs,
                    null,
                    limitResultsByClassification,
                    -1,
                    false);
        } catch (IOException e) {
            throw new RepositoryErrorException(XTDBErrorCode.CANNOT_CLOSE_RESOURCE.getMessageDefinition(),
                                               this.getClass().getName(), methodName, e);
        }

        return instanceGraph;

    }

}
