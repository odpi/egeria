/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model;

public class ResponseContainerDatabase extends ResponseContainer {

	private static final String DB_NAME = "name";
	private static final String GUID = "guid";
	private static final String DB_TYPE = "type";
	private static final String DB_VERSION = "version";
	public static final String TYPE_DATABASE = "database";

	public ResponseContainerDatabase() {
		super(TYPE_DATABASE);
	}

	public String getDbName() {
		return (String) getAttribute(DB_NAME);
	}
	public void setDbName(String value ) {
		setAttribute(DB_NAME, value);
	}
	public String getGUID() {
		return (String) getAttribute(GUID);
	}
	public void setGUID(String value ) {
		setAttribute(GUID, value);
	}
	public String getDbType() {
		return (String) getAttribute(DB_TYPE);
	}
	public void setDbType(String value ) {
		setAttribute(DB_TYPE, value);
	}
	public String getDbVersion() {
		return (String) getAttribute(DB_VERSION);
	}
	public void setDbVersion(String value ) {
		setAttribute(DB_VERSION, value);
	}

}
