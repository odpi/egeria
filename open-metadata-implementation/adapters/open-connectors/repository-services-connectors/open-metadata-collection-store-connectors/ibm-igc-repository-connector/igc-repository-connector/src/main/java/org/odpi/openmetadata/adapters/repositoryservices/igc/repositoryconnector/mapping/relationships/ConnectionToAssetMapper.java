/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to map the OMRS "ConnectionToAsset" relationship.
 * @see ConnectionToAssetMapper_Database
 * @see ConnectionToAssetMapper_FileFolder
 */
public class ConnectionToAssetMapper extends RelationshipMapping {

    private static class Singleton {
        private static final ConnectionToAssetMapper INSTANCE = new ConnectionToAssetMapper();
    }
    public static ConnectionToAssetMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private ConnectionToAssetMapper() {
        super(
                "",
                "",
                "",
                "",
                "ConnectionToAsset",
                "connections",
                "asset"
        );
        addSubType(ConnectionToAssetMapper_Database.getInstance());
        addSubType(ConnectionToAssetMapper_FileFolder.getInstance());
    }

}
