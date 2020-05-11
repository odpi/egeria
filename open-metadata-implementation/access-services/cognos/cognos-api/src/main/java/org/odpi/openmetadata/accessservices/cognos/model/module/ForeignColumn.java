/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model.module;


/**
 * Database table foreign column of foreign key abstraction.
 *	Sample:
 *		{
 *			"pkColumn": "StateProvinceID",
 *			"pkCatalog": "AdventureWorks2014",
 *			"pkSchema": "Person",
 *			"pkTable": "StateProvince",
 *			"columnName": "StateProvinceID"
 *		}
 *
 * @author YEVGENIYMarchenko
 *
 */
public class ForeignColumn implements IModuleObject {

	private String pkColumn;
	private String pkCatalog;
	private String pkSchema;
	private String pkTable;
	private String columnName;

	public String getPkColumn() {
		return pkColumn;
	}
	public void setPkColumn(String pkColumn) {
		this.pkColumn = pkColumn;
	}
	public String getPkCatalog() {
		return pkCatalog;
	}
	public void setPkCatalog(String pkCatalog) {
		this.pkCatalog = pkCatalog;
	}
	public String getPkSchema() {
		return pkSchema;
	}
	public void setPkSchema(String pkSchema) {
		this.pkSchema = pkSchema;
	}
	public String getPkTable() {
		return pkTable;
	}
	public void setPkTable(String pkTable) {
		this.pkTable = pkTable;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}
