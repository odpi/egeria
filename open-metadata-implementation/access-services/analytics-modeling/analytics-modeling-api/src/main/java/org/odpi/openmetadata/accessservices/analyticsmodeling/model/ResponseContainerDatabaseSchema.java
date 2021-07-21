/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.Schema;

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

	public static final String TYPE_SCHEMA = "schema";

	private Schema attributes = new Schema();

	/**
	 * Constructor defines 'schema' type and sets schemaType to 'user'
	 */
	public ResponseContainerDatabaseSchema() {
		super(TYPE_SCHEMA);
		attributes.setSchemaType("user");
	}
	
	/**
	 * Get schema name.
	 * @return schema name.
	 */
	public String getSchema() {
		return attributes.getSchema();
	}
	/**
	 * Set schema name.
	 * @param name of the schema. 
	 */
	public void setSchema(String name ) {
		attributes.setSchema(name);
	}
	/**
	 * Get catalog name.
	 * @return catalog name.
	 */
	public String getCatalog() {
		return attributes.getCatalog();
	}
	/**
	 * Set catalog name.
	 * @param value catalog name.
	 */
	public void setCatalog(String value) {
		attributes.setCatalog(value);
	}
	/**
	 * Get schema type
	 * @return schema type.
	 */
	public String getSchemaType() {
		return attributes.getSchemaType();
	}


	/**
	 * Build id based on names of catalog and schema.
	 * @return id as "catalog/schema"
	 */
	public String buildId()  {
		String catalog = attributes.getCatalog();
		String schema = attributes.getSchema();
		if (catalog == null) {
			return schema;
		} else if (schema == null) {
			return catalog;
		}
		return catalog + "/" + schema;
	}

	
}
