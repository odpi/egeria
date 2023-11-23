/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
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
 * Implements the 'findEntities' operation of the OMRS metadata collection interface.
 */
public class FindEntities extends AbstractEntitySearchOperation {

    private final List<String> entitySubtypeGUIDs;
    private final SearchProperties matchProperties;

    /**
     * Create a new 'findEntities' executable.
     * @param xtdb connectivity to XTDB
     * @param entityTypeGUID see XTDBOMRSMetadataCollection#findEntities
     * @param entitySubtypeGUIDs see XTDBOMRSMetadataCollection#findEntities
     * @param matchProperties see XTDBOMRSMetadataCollection#findEntities
     * @param fromEntityElement see XTDBOMRSMetadataCollection#findEntities
     * @param limitResultsByStatus see XTDBOMRSMetadataCollection#findEntities
     * @param matchClassifications see XTDBOMRSMetadataCollection#findEntities
     * @param asOfTime see XTDBOMRSMetadataCollection#findEntities
     * @param sequencingProperty see XTDBOMRSMetadataCollection#findEntities
     * @param sequencingOrder see XTDBOMRSMetadataCollection#findEntities
     * @param pageSize see XTDBOMRSMetadataCollection#findEntities
     * @param userId of the user running the query
     * @see XTDBOMRSMetadataCollection#findEntities(String, String, List, SearchProperties, int, List, SearchClassifications, Date, String, SequencingOrder, int)
     */
    public FindEntities(XTDBOMRSRepositoryConnector xtdb,
                        String entityTypeGUID,
                        List<String> entitySubtypeGUIDs,
                        SearchProperties matchProperties,
                        int fromEntityElement,
                        List<InstanceStatus> limitResultsByStatus,
                        SearchClassifications matchClassifications,
                        Date asOfTime,
                        String sequencingProperty,
                        SequencingOrder sequencingOrder,
                        int pageSize,
                        String userId) {
        super(xtdb,
                entityTypeGUID,
                fromEntityElement,
                limitResultsByStatus,
                matchClassifications,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize,
                userId);
        this.entitySubtypeGUIDs = entitySubtypeGUIDs;
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
                TypeDefCategory.ENTITY_DEF,
                typeGUID,
                entitySubtypeGUIDs,
                matchProperties,
                fromElement,
                limitResultsByStatus,
                matchClassifications,
                sequencingProperty,
                sequencingOrder,
                pageSize,
                EntityDetailMapping.ENTITY_PROPERTIES_NS,
                userId
        );
    }

}
