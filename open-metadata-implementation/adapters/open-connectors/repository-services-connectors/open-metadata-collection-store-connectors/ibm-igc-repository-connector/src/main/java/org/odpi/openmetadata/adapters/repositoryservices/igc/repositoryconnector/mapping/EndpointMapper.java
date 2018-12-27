/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndpointMapper extends ReferenceableMapper {

    private static final Logger log = LoggerFactory.getLogger(EndpointMapper.class);

    private static final String R_CONNECTION_ENDPOINT = "ConnectionEndpoint";

    /**
     * Sets the basic criteria to use for mapping between an IGC 'host' object and an OMRS 'Endpoint' object.
     *
     * @param host the IGC 'host' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public EndpointMapper(Reference host, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                host,
                "host",
                "Endpoint",
                igcomrsRepositoryConnector,
                userId
        );

        // IGC 'host_(engine)' is equivalent, so we need to ensure that it is also added to the assets to be
        // handled by this mapper
        addOtherIGCAssetType("host_(engine)");

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("short_description", "description");

        // These relationships need complex mappings, handled by 'complexRelationshipMappings' method below
        addComplexIgcRelationship("data_connections");
        addComplexOmrsRelationship(R_CONNECTION_ENDPOINT);

    }

    /**
     * No classifications implemented for Endpoints.
     */
    @Override
    protected void getMappedClassifications() {
        // Nothing to do
    }

    @Override
    protected void complexRelationshipMappings() {

        // TODO: traverse relationship from host to contained objects; intersect with data_connections references
        //  from both sides (host -> connector <- database / file / folder)

        // For simplicity, just find any objects that reference this host (endpoint) -- don't bother
        // intersecting via the connector ('data_connections') property, as it may not hold all references anyway
        IGCSearchCondition igcSearchCondition = new IGCSearchCondition("data_connectors.host", "=", me.getId());
        IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(igcSearchCondition);
        IGCSearch igcSearch = new IGCSearch("data_connection", igcSearchConditionSet);

        ReferenceList dataConnectionsForHost = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);
        dataConnectionsForHost.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        // For each of the related data connections, create a new ConnectionEndpoint relationship
        for (Reference dataConnection : dataConnectionsForHost.getItems()) {

            /* Only proceed with the connection object if it is not a 'main_object' asset
             * (in this scenario, 'main_object' represents ColumnAnalysisMaster objects that are not accessible
             *  and will throw bad request (400) REST API errors) */
            if (dataConnection != null && !dataConnection.getType().equals("main_object")) {
                try {

                    Relationship omrsRelationship = getMappedRelationship(
                            "data_connections",
                            (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(SOURCE_NAME,
                                    R_CONNECTION_ENDPOINT),
                            "connectionEndpoint",
                            "connections",
                            dataConnection);

                    relationships.add(omrsRelationship);

                } catch (RepositoryErrorException e) {
                    log.error("Unable to map relationship.", e);
                }
            }

        }

    }

}
