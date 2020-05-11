/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model;

import java.util.List;

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
