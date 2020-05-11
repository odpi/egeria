/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model;

/**
 * Cognos schema representation with serialization in json format.
 * @author YEVGENIYMarchenko
 *
 */
public class ResponseContainerDatabaseSchema extends ResponseContainer {

	private static final String CATALOG = "catalog";
	private static final String SCHEMA_TYPE = "schemaType";
	private static final String SCHEMA = "schema";
	public static final String TYPE_SCHEMA = "schema";

	public ResponseContainerDatabaseSchema() {
		super(TYPE_SCHEMA);
		setAttribute(SCHEMA_TYPE, "user");
	}
	
	public String getSchema() {
		return (String) getAttribute(SCHEMA);
	}
	public void setSchema(String value ) {
		setAttribute(SCHEMA, value);
	}
	public String getCatalog() {
		return (String) getAttribute(CATALOG);
	}
	
	public void setCatalog(String value) {
		setAttribute(CATALOG, value);
	}
	public String getSchemaType() {
		return (String) getAttribute(SCHEMA_TYPE);
	}
	
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
