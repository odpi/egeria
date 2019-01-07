/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.RelationalDBSchemaTypeMapper;

public class AssetSchemaTypeMapper_DatabaseSchema extends RelationshipMapping {

    private static class Singleton {
        private static final AssetSchemaTypeMapper_DatabaseSchema INSTANCE = new AssetSchemaTypeMapper_DatabaseSchema();
    }
    public static AssetSchemaTypeMapper_DatabaseSchema getInstance() {
        return Singleton.INSTANCE;
    }

    private AssetSchemaTypeMapper_DatabaseSchema() {
        super(
                "database_schema",
                "database_schema",
                SELF_REFERENCE_SENTINEL,
                SELF_REFERENCE_SENTINEL,
                "AssetSchemaType",
                "describesAssets",
                "schema",
                null,
                RelationalDBSchemaTypeMapper.IGC_RID_PREFIX
        );
    }

}
