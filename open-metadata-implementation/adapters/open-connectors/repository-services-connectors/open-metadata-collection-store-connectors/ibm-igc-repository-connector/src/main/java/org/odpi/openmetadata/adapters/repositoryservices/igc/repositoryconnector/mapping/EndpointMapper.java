/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;

public class EndpointMapper extends ReferenceableMapper {

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

        // This relationship can only be retrieved inverted
        // (relationship in IGC is cannot be traversed in other direction)
        addInvertedRelationshipMapping(
                "data_connection",
                "data_connectors.host",
                R_CONNECTION_ENDPOINT,
                "connections",
                "connectionEndpoint"
        );

    }

    /**
     * No classifications implemented for Endpoints.
     */
    @Override
    protected void getMappedClassifications() {
        // Nothing to do
    }

}
