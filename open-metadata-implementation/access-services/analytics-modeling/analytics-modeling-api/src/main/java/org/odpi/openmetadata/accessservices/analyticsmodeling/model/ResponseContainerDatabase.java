/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

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

	private static final String DB_NAME = "name";
	private static final String GUID = "guid";
	private static final String DB_TYPE = "type";
	private static final String DB_VERSION = "version";
	public static final String TYPE_DATABASE = "database";

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
		return (String) getAttribute(DB_NAME);
	}
	/**
	 * Set database name.
	 * @param value database name.
	 */
	public void setDbName(String value ) {
		setAttribute(DB_NAME, value);
	}
	/**
	 * Get global unique identifier of the database.
	 * @return global unique identifier of the database.
	 */
	public String getGUID() {
		return (String) getAttribute(GUID);
	}
	/**
	 * Set global unique identifier of the database.
	 * @param value of the global unique identifier.
	 */
	public void setGUID(String value ) {
		setAttribute(GUID, value);
	}
	/**
	 * Get type defining vendor.
	 * @return vendor of database.
	 */
	public String getDbType() {
		return (String) getAttribute(DB_TYPE);
	}
	/**
	 * Set type defining vendor.
	 * @param value database vendor.
	 */
	public void setDbType(String value ) {
		setAttribute(DB_TYPE, value);
	}
	
	/**
	 * Get version of the database.
	 * @return string version.
	 */
	public String getDbVersion() {
		return (String) getAttribute(DB_VERSION);
	}
	/**
	 * Set version of the database.
	 * @param value of database version.
	 */
	public void setDbVersion(String value ) {
		setAttribute(DB_VERSION, value);
	}

}
