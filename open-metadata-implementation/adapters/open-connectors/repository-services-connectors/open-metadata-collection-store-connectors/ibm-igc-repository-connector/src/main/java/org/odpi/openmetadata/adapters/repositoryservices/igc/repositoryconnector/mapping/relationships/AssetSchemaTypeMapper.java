/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.RelationalDBSchemaTypeMapper;

public class AssetSchemaTypeMapper extends RelationshipMapping {

    private static AssetSchemaTypeMapper instance = new AssetSchemaTypeMapper();
    public static AssetSchemaTypeMapper getInstance() { return instance; }

    private AssetSchemaTypeMapper() {
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
