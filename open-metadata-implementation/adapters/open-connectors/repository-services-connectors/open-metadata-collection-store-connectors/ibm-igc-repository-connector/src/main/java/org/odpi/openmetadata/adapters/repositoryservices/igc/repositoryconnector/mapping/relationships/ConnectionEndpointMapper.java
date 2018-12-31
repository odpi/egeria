/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the ConnectionEndpoint relationship in OMRS.
 */
public class ConnectionEndpointMapper extends RelationshipMapping {

    private static class Singleton {
        private static final ConnectionEndpointMapper INSTANCE = new ConnectionEndpointMapper();
    }
    public static ConnectionEndpointMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private ConnectionEndpointMapper() {
        super(
                "host",
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
