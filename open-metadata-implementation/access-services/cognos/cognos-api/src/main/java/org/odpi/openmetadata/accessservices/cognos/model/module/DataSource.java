/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model.module;

import java.util.List;

/**
 * Data source model.
 * Sample:
 *	{
 *		"schema": "Person",
 *		"catalog": "AdventureWorks2014",
 *		"name": ".AdventureWorks2014.Person",
 *		"table": [
 *			{ @link org.odpi.openmetadata.accessservices.cognos.model.module.Table }
 *		]
 *	}
 *
 * @author YEVGENIYMarchenko
 *
 */
public class DataSource implements IModuleObject {

	private String schema;
	private String catalog;
	private String name;

	private List<Table> table;

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Table> getTable() {
		return table;
	}

	public void setTable(List<Table> table) {
		this.table = table;
	}
}
