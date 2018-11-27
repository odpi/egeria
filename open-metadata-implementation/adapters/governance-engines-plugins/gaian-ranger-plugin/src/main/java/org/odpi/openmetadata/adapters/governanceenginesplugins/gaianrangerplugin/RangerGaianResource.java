/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;


import org.apache.ranger.plugin.policyengine.RangerAccessResourceImpl;



public class RangerGaianResource extends RangerAccessResourceImpl {
    public static final String KEY_SCHEMA = "schema";
    public static final String KEY_TABLE    = "table";
    public static final String KEY_COLUMN   = "column";

    private GaianResourceType resourceType = null;

    public RangerGaianResource(GaianResourceType resourceType, String schema) {
        this(resourceType, schema, null, null);
    }

    public RangerGaianResource(GaianResourceType resourceType, String schema, String table) {
        this(resourceType, schema, table, null);
    }

    public RangerGaianResource(GaianResourceType resourceType, String schema, String table, String column) {
        this.resourceType = resourceType;

        switch(resourceType) {
            case SCHEMA:
                if (schema == null) {
                    schema = "*";
                }
                setValue(KEY_SCHEMA, schema);
                break;

            case TABLE:
                setValue(KEY_SCHEMA, schema);
                setValue(KEY_TABLE, table);
                break;

            case COLUMN:
                setValue(KEY_SCHEMA, schema);
                setValue(KEY_TABLE, table);
                setValue(KEY_COLUMN, column);
                break;

            default:
                break;
        }
    }

    public GaianResourceType getResourceType() {
        return resourceType;
    }

    public String getSchema() {
        return getValue(KEY_SCHEMA);
    }

    public String getTable() {
        return getValue(KEY_TABLE);
    }

    public String getColumn() {
        return getValue(KEY_COLUMN);
    }

}

