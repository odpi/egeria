/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.model;

import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;

/**
 * Wrapper for the Data Engine OMAS's SchemaType object, to also be able to include the userId information.
 */
public class DataEngineSchemaType {

    private SchemaType schemaType;
    private String userId;

    /**
     * Default constructor
     *
     * @param schemaType the SchemaType to be maintained
     * @param userId     the user maintaining the SchemaType
     */
    public DataEngineSchemaType(SchemaType schemaType, String userId) {
        this.schemaType = schemaType;
        this.userId = userId;
    }

    /**
     * Retrieve the SchemaType being maintained.
     *
     * @return SchemaType
     */
    public SchemaType getSchemaType() { return schemaType; }

    /**
     * Set the SchemaType to be maintained.
     *
     * @param schemaType the SchemaType to be maintained
     */
    public void setSchemaType(SchemaType schemaType) { this.schemaType = schemaType; }

    /**
     * Retrieve the user maintaining the SchemaType
     *
     * @return String
     */
    public String getUserId() { return userId; }

    /**
     * Set the user maintaining the SchemaType
     *
     * @param userId the user maintaining the SchemaType
     */
    public void setUserId(String userId) { this.userId = userId; }

}
