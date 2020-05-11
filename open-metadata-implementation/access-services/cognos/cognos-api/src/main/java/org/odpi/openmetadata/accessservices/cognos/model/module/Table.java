/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model.module;

import java.util.List;

/**
 *
 * 	{
 *		"name": "Address",
 *		"tableItem": [
 *			{ @see org.odpi.openmetadata.accessservices.cognos.model.module.tableItem }
 *		],
 *		"foreignKey": [
 *			{ @see org.odpi.openmetadata.accessservices.cognos.model.module.ForeignKey }
 *		],
 *		"primaryKey": [
 *			{ @see org.odpi.openmetadata.accessservices.cognos.model.module.PrimaryKey }
 *		]
 *	}
 *
 * @author YEVGENIYMarchenko
 *
 */
public class Table implements IModuleObject {

	private String name;
	private List <TableItem> tableItem;
	private List <ForeignKey> foreignKey;
	private List <PrimaryKey> primaryKey;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TableItem> getTableItem() {
		return tableItem;
	}
	public void setTableItem(List<TableItem> tableItem) {
		this.tableItem = tableItem;
	}
	public List<ForeignKey> getForeignKey() {
		return foreignKey;
	}
	public void setForeignKey(List<ForeignKey> foreignKey) {
		this.foreignKey = foreignKey;
	}
	public List<PrimaryKey> getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(List<PrimaryKey> primaryKey) {
		this.primaryKey = primaryKey;
	}
}
