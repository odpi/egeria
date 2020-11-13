/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model.module;


/**
 * Database table foreign column of foreign key abstraction.
 *	<br>Sample:<br>
 *		{<br>
 *	&emsp;		"pkColumn": "StateProvinceID",<br>
 *	&emsp;		"pkCatalog": "AdventureWorks2014",<br>
 *	&emsp;		"pkSchema": "Person",<br>
 *	&emsp;		"pkTable": "StateProvince",<br>
 *	&emsp;		"columnName": "StateProvinceID"<br>
 *		}
 *
 *
 */
public class ForeignColumn implements IModuleObject {

	private String pkColumn;
	private String pkCatalog;
	private String pkSchema;
	private String pkTable;
	private String columnName;

	/**
	 * Get name of the primary column.
	 * @return name of the primary column.
	 */
	public String getPkColumn() {
		return pkColumn;
	}
	/**
	 * Set name of the primary column.
	 * @param pkColumn name of the primary column.
	 */
	public void setPkColumn(String pkColumn) {
		this.pkColumn = pkColumn;
	}
	/**
	 * Get name of the catalog the primary column belongs to.
	 * @return name of the catalog the primary column belongs to.
	 */
	public String getPkCatalog() {
		return pkCatalog;
	}
	
	/**
	 * Set name of the catalog the primary column belongs to.
	 * @param pkCatalog name of the catalog the primary column belongs to.
	 */
	public void setPkCatalog(String pkCatalog) {
		this.pkCatalog = pkCatalog;
	}
	/**
	 * Get name of the schema the primary column belongs to.
	 * @return name of the schema the primary column belongs to.
	 */
	public String getPkSchema() {
		return pkSchema;
	}
	/**
	 * Set name of the schema the primary column belongs to.
	 * @param pkSchema name of the schema the primary column belongs to.
	 */
	public void setPkSchema(String pkSchema) {
		this.pkSchema = pkSchema;
	}
	/**
	 * Get name of the table the primary column belongs to.
	 * @return name of the table the primary column belongs to.
	 */
	public String getPkTable() {
		return pkTable;
	}
	/**
	 * Set name of the table the primary column belongs to.
	 * @param pkTable name of the table the primary column belongs to.
	 */
	public void setPkTable(String pkTable) {
		this.pkTable = pkTable;
	}
	/**
	 * Get name of the column referencing primary key.
	 * @return name of column referencing primary key.
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * Set name of the column referencing primary key.
	 * @param columnName name of the column referencing primary key.
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}
