/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class ConnectionToAssetMapper_Database extends RelationshipMapping {

    private static class Singleton {
        private static final ConnectionToAssetMapper_Database INSTANCE = new ConnectionToAssetMapper_Database();
    }
    public static ConnectionToAssetMapper_Database getInstance() {
        return Singleton.INSTANCE;
    }

    private ConnectionToAssetMapper_Database() {
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
