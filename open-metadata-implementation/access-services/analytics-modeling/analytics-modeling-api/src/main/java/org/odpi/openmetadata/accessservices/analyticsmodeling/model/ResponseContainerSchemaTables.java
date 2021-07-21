/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.Tables;

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
	
	public static final String TYPE_TABLES = "tables";
	
	private Tables attributes = new Tables();

	public ResponseContainerSchemaTables() {
		super(TYPE_TABLES);
	}
	
	public void setTablesList(List<String> value) {
		attributes.setTables(value);
	}

	public List<String>  getTablesList() {
		return attributes.getTables();
	}

}
