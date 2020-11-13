/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model.module;

import java.util.List;

/**
 *	Abstraction of the table in MOSER module.
 *	<br>Sample:<br>
 * 	{<br>
 *	&emsp;	"property": [&nbsp;
 *	&emsp;		{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PropertyType}<br>
 *	&emsp;	],<br>
 *	&emsp;	"name": "Address",<br>
 *	&emsp;	"tableItem": [&nbsp;
 *	&emsp;		{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.TableItem}
 *	&emsp;	],<br>
 *	&emsp;	"foreignKey": [&nbsp;
 *	&emsp;		{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.ForeignKey}
 *	&emsp;	],<br>
 *	&emsp;	"primaryKey": [&nbsp;
 *	&emsp;		{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PrimaryKey}
 *	&emsp;	]<br>
 *	}
 *
 *
 */
public class Table extends BaseObjectType {

	private String name;
	private List <TableItem> tableItem;
	private List <ForeignKey> foreignKey;
	private List <PrimaryKey> primaryKey;

	/**
	 * Get name of the table.
	 * @return table name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Set table name.
	 * @param name of the table
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get list of table items.
	 * @return list of table items.
	 */
	public List<TableItem> getTableItem() {
		return tableItem;
	}
	
	/**
	 * Set list of table items.
	 * @param tableItem list of table items.
	 */
	public void setTableItem(List<TableItem> tableItem) {
		this.tableItem = tableItem;
	}
	
	/**
	 * Get list of foreign keys defined in the table.
	 * @return list of foreign keys.
	 */
	public List<ForeignKey> getForeignKey() {
		return foreignKey;
	}
	
	/**
	 * Set list of foreign keys for the table.
	 * @param foreignKey list of foreign keys.
	 */
	public void setForeignKey(List<ForeignKey> foreignKey) {
		this.foreignKey = foreignKey;
	}
	
	/**
	 * Get definition of the primary key.
	 * @return primary key definition.
	 */
	public List<PrimaryKey> getPrimaryKey() {
		return primaryKey;
	}
	/**
	 * Set primary key for the table.
	 * @param primaryKey definition.
	 */
	public void setPrimaryKey(List<PrimaryKey> primaryKey) {
		this.primaryKey = primaryKey;
	}
}
