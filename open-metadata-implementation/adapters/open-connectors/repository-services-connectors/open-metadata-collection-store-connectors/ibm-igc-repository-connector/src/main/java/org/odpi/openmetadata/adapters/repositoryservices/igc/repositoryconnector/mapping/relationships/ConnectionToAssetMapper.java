/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the ConnectionToAsset relationship in OMRS.
 */
public class ConnectionToAssetMapper extends RelationshipMapping {

    private static ConnectionToAssetMapper instance = new ConnectionToAssetMapper();
    public static ConnectionToAssetMapper getInstance() { return instance; }

    private ConnectionToAssetMapper() {
        super(
                "data_connection",
                "database",
                "imports_database",
                "data_connections",
                "ConnectionToAsset",
                "connections",
                "asset"
        );
    }

}
