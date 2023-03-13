/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.model.response;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Schema contains the properties and guid for a database schema.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Schema {

	String catalog;
	String schema;
	String schemaType;

	/**
	 * Get name of the database catalog.
	 * @return name of the database catalog.
	 */
	public String getCatalog() {
		return catalog;
	}
	
	/**
	 * Set name of the database catalog.
	 * @param value to set.
	 */
	public void setCatalog(String value) {
		catalog = value;
	}
	
	/**
	 * Get schema name of the database.
	 * @return schema name of the database.
	 */
	public String getSchema() {
		return schema;
	}
	/**
	 * Set schema name of the database.
	 * @param value to set.
	 */

	public void setSchema(String value) {
		schema = value;
	}
	
	/**
	 * Get type of the database schema.
	 * @return type of the database schema.
	 */
	public String getSchemaType() {
		return schemaType;
	}
	
	/**
	 * Set type of the database schema.
	 * @param value to set.
	 */
	public void setSchemaType(String value) {
		schemaType = value;
	}
	

}
