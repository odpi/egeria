/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.EntityDetailMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import xtdb.api.IXtdbDatasource;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Implements the 'findEntitiesByPropertyValue' operation of the OMRS metadata collection interface.
 */
public class FindEntitiesByPropertyValue extends AbstractEntitySearchOperation {

    private final String searchCriteria;

    /**
     * Create a new 'findEntitiesByPropertyValue' executable.
     * @param xtdb connectivity to XTDB
     * @param entityTypeGUID see XTDBOMRSMetadataCollection#findEntitiesByPropertyValue
     * @param searchCriteria see XTDBOMRSMetadataCollection#findEntitiesByPropertyValue
     * @param fromEntityElement see XTDBOMRSMetadataCollection#findEntitiesByPropertyValue
     * @param limitResultsByStatus see XTDBOMRSMetadataCollection#findEntitiesByPropertyValue
     * @param matchClassifications see XTDBOMRSMetadataCollection#findEntitiesByPropertyValue
     * @param asOfTime see XTDBOMRSMetadataCollection#findEntitiesByPropertyValue
     * @param sequencingProperty see XTDBOMRSMetadataCollection#findEntitiesByPropertyValue
     * @param sequencingOrder see XTDBOMRSMetadataCollection#findEntitiesByPropertyValue
     * @param pageSize see XTDBOMRSMetadataCollection#findEntitiesByPropertyValue
     * @param userId of the user running the query
     * @see XTDBOMRSMetadataCollection#findEntitiesByPropertyValue(String, String, String, int, List, List, Date, String, SequencingOrder, int)
     */
    public FindEntitiesByPropertyValue(XTDBOMRSRepositoryConnector xtdb,
                                       String entityTypeGUID,
                                       String searchCriteria,
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
        this.searchCriteria = searchCriteria;
    }

    /**
     * Interface that must be implemented to actually execute the query logic.
     * @param db the datasource against which to run the query
     * @return {@code Collection<List<?>>} of internal XT references (IDs) that match the query
     * @throws TypeErrorException if a requested type for searching is not known to the repository
     * @throws RepositoryErrorException if there is any issue iterating through the results
     */
    @Override
    protected Collection<List<?>> runQuery(IXtdbDatasource db) throws TypeErrorException, RepositoryErrorException {
        return searchXtdbText(db,
                TypeDefCategory.ENTITY_DEF,
                typeGUID,
                searchCriteria,
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
