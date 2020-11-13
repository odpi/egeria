/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

/**
 * Response container for Analytics Modeling schema representation.<br>
 * The container has following attributes:<br>
 * &emsp;catalog - name of the schema catalog.<br>
 * &emsp;schema -  name of the schema.<br>
 * &emsp;schemaType - this can be 'user' or 'system'. It is always set to 'user' now.<br>
 * 
 * The type of the container is 'schema'.
 * 
 * 
 */
public class ResponseContainerDatabaseSchema extends ResponseContainer {

	private static final String CATALOG = "catalog";
	private static final String SCHEMA_TYPE = "schemaType";
	private static final String SCHEMA = "schema";
	public static final String TYPE_SCHEMA = "schema";

	/**
	 * Constructor defines 'schema' type and sets schemaType to 'user'
	 */
	public ResponseContainerDatabaseSchema() {
		super(TYPE_SCHEMA);
		setAttribute(SCHEMA_TYPE, "user");
	}
	/**
	 * Get schema name.
	 * @return schema name.
	 */
	public String getSchema() {
		return (String) getAttribute(SCHEMA);
	}
	/**
	 * Set schema name.
	 * @param name of the schema. 
	 */
	public void setSchema(String name ) {
		setAttribute(SCHEMA, name);
	}
	/**
	 * Get catalog name.
	 * @return catalog name.
	 */
	public String getCatalog() {
		return (String) getAttribute(CATALOG);
	}
	/**
	 * Set catalog name.
	 * @param value catalog name.
	 */
	public void setCatalog(String value) {
		setAttribute(CATALOG, value);
	}
	/**
	 * Get schema type
	 * @return schema type.
	 */
	public String getSchemaType() {
		return (String) getAttribute(SCHEMA_TYPE);
	}
	
	/**
	 * Build id based on names of catalog and schema.
	 * @return id as "catalog/schema"
	 */
	public String buildId()  {
		String catalog = getCatalog();
		String schema = (String) getAttribute(SCHEMA);
		if (catalog == null) {
			return schema;
		} else if (schema == null) {
			return catalog;
		}
		return catalog + "/" + schema;
	}

	
}
