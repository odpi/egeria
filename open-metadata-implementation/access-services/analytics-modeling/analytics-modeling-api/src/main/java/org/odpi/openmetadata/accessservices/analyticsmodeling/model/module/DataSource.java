/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model.module;

import java.util.List;

/**
 * Data source model.
 * <br>Sample:<br>
 *	{<br>
 *	&emsp;	"property": [&nbsp;
 *	&emsp;		{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PropertyType}<br>
 *	&emsp;	],<br>
 *	&emsp;	"schema": "Person",<br>
 *	&emsp;	"catalog": "AdventureWorks2014",<br>
 *	&emsp;	"name": "AdventureWorks2014.Person",<br>
 *	&emsp;	"table": [&nbsp;
 *	&emsp;	{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.Table}
 *	&emsp;	]<br>
 *	}
 *
 *
 */
public class DataSource extends BaseObjectType {

	private String schema;
	private String catalog;
	private String name;

	private List<Table> table;

	/**
	 * Get name of the relational database schema.
	 * @return name of the relational database schema.
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * Set name of the relational database schema.
	 * @param schema name of the relational database schema.
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * Get name of the relational database catalog.
	 * @return name of the relational database catalog.
	 */
	public String getCatalog() {
		return catalog;
	}

	/**
	 * Set name of the relational database catalog.
	 * @param catalog name of the relational database catalog.
	 */
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	/**
	 * Get name of the data source.
	 * @return name of the data source.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of the data source.
	 * @param name of the data source.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get list of tables of the data source schema.
	 * @return list of tables.
	 */
	public List<Table> getTable() {
		return table;
	}

	/**
	 * Set tables of the data source schema.
	 * @param tables list of tables.
	 */
	public void setTable(List<Table> tables) {
		this.table = tables;
	}
}
