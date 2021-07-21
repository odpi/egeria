/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.Database;

/**
 * Response container for database.<br>
 * The container has following attributes:<br>
 * &emsp;name - database name.<br>
 * &emsp;guid - unique identifier to access database metdata.<br>
 * &emsp;type - of the database, vendor.<br>
 * &emsp;version - database version.<br>
 * 
 * The type of the container is 'database'.
 * 
 * 
 */
public class ResponseContainerDatabase extends ResponseContainer {

	public static final String TYPE_DATABASE = "database";
	
	
	private Database attributes = new Database();

	/**
	 * Constructor defines 'database' type
	 */
	public ResponseContainerDatabase() {
		super(TYPE_DATABASE);
	}

	/**
	 * Get database name.
	 * @return database name.
	 */
	public String getDbName() {
		return attributes.getName();
	}
	/**
	 * Set database name.
	 * @param value database name.
	 */
	public void setDbName(String value ) {
		attributes.setName(value);
	}
	/**
	 * Get global unique identifier of the database.
	 * @return global unique identifier of the database.
	 */
	public String getGUID() {
		return attributes.getGuid();
	}
	/**
	 * Set global unique identifier of the database.
	 * @param value of the global unique identifier.
	 */
	public void setGUID(String value ) {
		attributes.setGuid(value);
	}
	/**
	 * Get type defining vendor.
	 * @return vendor of database.
	 */
	public String getDbType() {
		return attributes.getType();
	}
	/**
	 * Set type defining vendor.
	 * @param value database vendor.
	 */
	public void setDbType(String value ) {
		attributes.setType(value);
	}
	
	/**
	 * Get version of the database.
	 * @return string version.
	 */
	public String getDbVersion() {
		return attributes.getVersion();
	}
	/**
	 * Set version of the database.
	 * @param value of database version.
	 */
	public void setDbVersion(String value ) {
		attributes.setVersion(value);
	}
}
