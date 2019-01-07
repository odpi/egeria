/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class AssetSchemaTypeMapper extends RelationshipMapping {

    private static class Singleton {
        private static final AssetSchemaTypeMapper INSTANCE = new AssetSchemaTypeMapper();
    }
    public static AssetSchemaTypeMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private AssetSchemaTypeMapper() {
        super(
                "",
                "",
                "",
                "",
                "AssetSchemaType",
                "describesAssets",
                "schema"
        );
        addSubType(AssetSchemaTypeMapper_DatabaseSchema.getInstance());
        addSubType(AssetSchemaTypeMapper_FileRecord.getInstance());
    }

}
