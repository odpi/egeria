/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;

public class ConnectorTypeMapper extends ReferenceableMapper {

    /**
     * Sets the basic criteria to use for mapping between an IGC 'connector' object and an OMRS 'ConnectorType' object.
     *
     * @param dataConnector the IGC 'connector' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public ConnectorTypeMapper(Reference dataConnector, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                dataConnector,
                "connector",
                "ConnectorType",
                igcomrsRepositoryConnector, userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "name");
        addSimplePropertyMapping("short_description", "description");

        // The list of relationships that should be mapped
        addSimpleRelationshipMapping(
                "data_connections",
                "ConnectionConnectorType",
                "connectorType",
                "connections"
        );

    }

    /**
     * No classifications implemented for DataClasses.
     */
    @Override
    protected void getMappedClassifications() {
        // Nothing to do
    }

}
