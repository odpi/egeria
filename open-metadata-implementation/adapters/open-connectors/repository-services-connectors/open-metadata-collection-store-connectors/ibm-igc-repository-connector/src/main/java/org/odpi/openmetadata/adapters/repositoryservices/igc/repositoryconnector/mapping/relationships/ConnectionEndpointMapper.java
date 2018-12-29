/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the ConnectionEndpoint relationship in OMRS.
 */
public class ConnectionEndpointMapper extends RelationshipMapping {

    private static ConnectionEndpointMapper instance = new ConnectionEndpointMapper();
    public static ConnectionEndpointMapper getInstance() { return instance; }

    private ConnectionEndpointMapper() {
        super(
                "connector",
                "data_connection",
                "data_connections",
                "data_connectors",
                "ConnectionEndpoint",
                "connectionEndpoint",
                "connections"
        );
        setOptimalStart(OptimalStart.CUSTOM);
    }

}
