/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import xtdb.api.IXtdbDatasource;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Implements the 'findRelationships' operation of the OMRS metadata collection interface.
 */
public class FindRelationships extends AbstractRelationshipSearchOperation {

    private final List<String> relationshipSubtypeGUIDs;
    private final SearchProperties matchProperties;

    /**
     * Create a new 'findRelationships' executable.
     * @param xtdb connectivity to XTDB
     * @param relationshipTypeGUID see XTDBOMRSMetadataCollection#findRelationships
     * @param relationshipSubtypeGUIDs see XTDBOMRSMetadataCollection#findRelationships
     * @param matchProperties see XTDBOMRSMetadataCollection#findRelationships
     * @param fromRelationshipElement see XTDBOMRSMetadataCollection#findRelationships
     * @param limitResultsByStatus see XTDBOMRSMetadataCollection#findRelationships
     * @param asOfTime see XTDBOMRSMetadataCollection#findRelationships
     * @param sequencingProperty see XTDBOMRSMetadataCollection#findRelationships
     * @param sequencingOrder see XTDBOMRSMetadataCollection#findRelationships
     * @param pageSize see XTDBOMRSMetadataCollection#findRelationships
     * @param userId of the user running the query
     * @see XTDBOMRSMetadataCollection#findRelationships(String, String, List, SearchProperties, int, List, Date, String, SequencingOrder, int)
     */
    public FindRelationships(XTDBOMRSRepositoryConnector xtdb,
                             String relationshipTypeGUID,
                             List<String> relationshipSubtypeGUIDs,
                             SearchProperties matchProperties,
                             int fromRelationshipElement,
                             List<InstanceStatus> limitResultsByStatus,
                             Date asOfTime,
                             String sequencingProperty,
                             SequencingOrder sequencingOrder,
                             int pageSize,
                             String userId) {
        super(xtdb,
                relationshipTypeGUID,
                fromRelationshipElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize,
                userId);
        this.relationshipSubtypeGUIDs = relationshipSubtypeGUIDs;
        this.matchProperties = matchProperties;
    }

    /**
     * Interface that must be implemented to actually execute the query logic.
     * @param db the datasource against which to run the query
     * @return {@code Collection<List<?>>} of internal XT references (IDs) that match the query
     * @throws TypeErrorException if a requested type for searching is not known to the repository
     * @throws TimeoutException if the query times out
     * @throws RepositoryErrorException if there is any issue iterating through the results
     */
    @Override
    protected Collection<List<?>> runQuery(IXtdbDatasource db) throws TypeErrorException, TimeoutException, RepositoryErrorException {
        return searchXtdb(db,
                TypeDefCategory.RELATIONSHIP_DEF,
                typeGUID,
                relationshipSubtypeGUIDs,
                matchProperties,
                fromElement,
                limitResultsByStatus,
                null,
                sequencingProperty,
                sequencingOrder,
                pageSize,
                RelationshipMapping.RELATIONSHIP_PROPERTIES_NS,
                userId
        );
    }

}
