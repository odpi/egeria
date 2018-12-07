/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import org.apache.ranger.plugin.policyengine.RangerAccessResourceImpl;

import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.COLUMN_RESOURCE;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.SCHEMA_RESOURCE;
import static org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.Constants.TABLE_RESOURCE;

public class RangerGaianResource extends RangerAccessResourceImpl {

    private GaianResourceType resourceType;

    public RangerGaianResource(GaianResourceType resourceType, String schema) {
        this(resourceType, schema, null, null);
    }

    public RangerGaianResource(GaianResourceType resourceType, String schema, String table) {
        this(resourceType, schema, table, null);
    }

    public RangerGaianResource(GaianResourceType resourceType, String schema, String table, String column) {
        this.resourceType = resourceType;

        switch (resourceType) {
            case SCHEMA:
                if (schema == null) {
                    schema = "*";
                }
                setValue(SCHEMA_RESOURCE.toLowerCase(), schema);
                break;

            case TABLE:
                setValue(SCHEMA_RESOURCE.toLowerCase(), schema);
                setValue(TABLE_RESOURCE.toLowerCase(), table);
                break;

            case COLUMN:
                setValue(SCHEMA_RESOURCE.toLowerCase(), schema);
                setValue(TABLE_RESOURCE.toLowerCase(), table);
                setValue(COLUMN_RESOURCE.toLowerCase(), column);
                break;

            default:
                break;
        }
    }

    public GaianResourceType getResourceType() {
        return resourceType;
    }

    public String getSchema() {
        return getValue(SCHEMA_RESOURCE.toLowerCase());
    }

    public String getTable() {
        return getValue(TABLE_RESOURCE.toLowerCase());
    }

    public String getColumn() {
        return getValue(COLUMN_RESOURCE.toLowerCase());
    }

}