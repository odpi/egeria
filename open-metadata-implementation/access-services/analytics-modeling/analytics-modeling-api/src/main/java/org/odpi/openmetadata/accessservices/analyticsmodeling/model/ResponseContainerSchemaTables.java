/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

import java.util.List;

/**
 * Response container for list of table names available in the schema.<br>
 * The container has following attributes:<br>
 * &emsp;tables - list of table names available in the schema.<br>
 * 
 * The type of the container is 'tables'.
 * 
 * 
 */

public class ResponseContainerSchemaTables extends ResponseContainer {
	
	private static final String TABLES = "tables";
	public static final String TYPE_TABLES = "tables";

	public ResponseContainerSchemaTables() {
		super(TYPE_TABLES);
	}
	
	public void setTablesList(List<String> value) {
		setAttribute(TABLES, value);
	}
	
	@SuppressWarnings("unchecked")
	public List<String>  getTablesList() {
		return (List<String>) getAttribute(TABLES);
	}

}
