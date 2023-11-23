/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.readops;

import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import xtdb.api.IXtdbDatasource;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Implements the 'findRelationshipsByPropertyValue' operation of the OMRS metadata collection interface.
 */
public class FindRelationshipsByPropertyValue extends AbstractRelationshipSearchOperation {

    private final String searchCriteria;

    /**
     * Create a new 'findRelationshipsByPropertyValue' executable.
     * @param xtdb connectivity to XTDB
     * @param relationshipTypeGUID see XTDBOMRSMetadataCollection#findRelationshipsByPropertyValue
     * @param searchCriteria see XTDBOMRSMetadataCollection#findRelationshipsByPropertyValue
     * @param fromRelationshipElement see XTDBOMRSMetadataCollection#findRelationshipsByPropertyValue
     * @param limitResultsByStatus see XTDBOMRSMetadataCollection#findRelationshipsByPropertyValue
     * @param asOfTime see XTDBOMRSMetadataCollection#findRelationshipsByPropertyValue
     * @param sequencingProperty see XTDBOMRSMetadataCollection#findRelationshipsByPropertyValue
     * @param sequencingOrder see XTDBOMRSMetadataCollection#findRelationshipsByPropertyValue
     * @param pageSize see XTDBOMRSMetadataCollection#findRelationshipsByPropertyValue
     * @param userId of the user running the query
     * @see XTDBOMRSMetadataCollection#findRelationshipsByPropertyValue(String, String, String, int, List, Date, String, SequencingOrder, int)
     */
    public FindRelationshipsByPropertyValue(XTDBOMRSRepositoryConnector xtdb,
                                            String relationshipTypeGUID,
                                            String searchCriteria,
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
                TypeDefCategory.RELATIONSHIP_DEF,
                typeGUID,
                searchCriteria,
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
